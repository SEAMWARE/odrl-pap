package org.fiware.odrl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.fiware.odrl.jsonld.EvaluationContext;
import org.fiware.odrl.jsonld.JsonLdHandler;
import org.fiware.odrl.mapping.*;
import org.fiware.odrl.persistence.PolicyRepository;
import org.fiware.odrl.rego.DataResponse;
import org.openapi.quarkus.odrl_yaml.api.UiApi;
import org.openapi.quarkus.odrl_yaml.model.GenericJsonInput;
import org.openapi.quarkus.odrl_yaml.model.Mapping;
import org.openapi.quarkus.odrl_yaml.model.Mappings;
import org.openapi.quarkus.odrl_yaml.model.ValidationRequest;
import org.openapi.quarkus.odrl_yaml.model.ValidationResponse;
import org.openapi.quarkus.opa_yaml.api.DataApiApi;
import org.openapi.quarkus.opa_yaml.api.PolicyApiApi;

import java.io.ByteArrayInputStream;
import java.util.*;

/**
 * Implementation of the validation API to support testing of policies.
 *
 * <p>Supports two evaluation modes selected by the policy's JSON-LD context:</p>
 * <ul>
 *   <li><b>HTTP</b> (default) — evaluates policies against an HTTP
 *       {@code TestRequest}, used when the policy references HTTP-oriented
 *       namespaces (e.g., {@code odrl:read}).</li>
 *   <li><b>JSON</b> — evaluates policies against an arbitrary JSON payload
 *       ({@code GenericJsonInput}), used when the policy references the
 *       {@code json:} namespace or declares {@code pap:evaluationContext}
 *       as {@code "json"}.</li>
 * </ul>
 *
 * <p>The evaluation mode is determined by
 * {@link JsonLdHandler#detectEvaluationContext(String)}, NOT by the
 * {@link Pep} enum (which selects the API gateway type only).</p>
 */
@Slf4j
public class ValidationResource implements UiApi {

    /** Key used to wrap the request object inside the OPA input structure. */
    private static final String REQUEST_KEY = "request";

    /** Key used to wrap the top-level input object sent to OPA. */
    private static final String INPUT_KEY = "input";

    /** Key for the payload field inside the generic JSON request structure. */
    private static final String PAYLOAD_KEY = "payload";

    /** Key for the subject field inside the generic JSON request structure. */
    private static final String SUBJECT_KEY = "subject";

    /** OPA data API path suffix for the is_allowed rule. */
    private static final String IS_ALLOWED_PATH_TEMPLATE = "%s/is_allowed";

    @RestClient
    public PolicyApiApi opaPolicyApi;

    @RestClient
    private DataApiApi dataApiApi;

    @Inject
    private PolicyRepository policyRepository;

    @Inject
    private OdrlMapper odrlMapper;

    @Inject
    private MappingConfiguration mappingConfiguration;

    @Inject
    private JsonLdHandler jsonLdHandler;

    @Inject
    private ObjectMapper objectMapper;

    /**
     * Validates a policy against a test input. The policy is compacted via
     * JSON-LD, translated to Rego, uploaded to OPA, evaluated, and the result
     * returned.
     *
     * <p>The evaluation context (HTTP vs. JSON) is detected from the compacted
     * policy's namespace usage. For HTTP policies the {@code testRequest} field
     * is used; for JSON policies the {@code jsonInput} field is used.</p>
     *
     * @param validationRequest the request containing the policy and test input
     * @return a {@link Response} containing a {@link ValidationResponse}
     */
    @Override
    public Response validatePolicy(ValidationRequest validationRequest) {
        if (dataApiApi == null) {
            throw new UnsupportedOperationException("Policy validation is not enabled.");
        }
        String tempId = PolicyRepository.generatePolicyId();
        try {
            String compactedJson = jsonLdHandler.handleJsonLd(
                    new ByteArrayInputStream(objectMapper.writeValueAsBytes(validationRequest.getPolicy())));

            EvaluationContext evaluationContext = jsonLdHandler.detectEvaluationContext(compactedJson);
            log.info("Detected evaluation context: {}", evaluationContext);

            Map<String, Object> policyAsMap = objectMapper.readValue(
                    compactedJson, new TypeReference<Map<String, Object>>() {});
            MappingResult mappingResult = odrlMapper.mapOdrl(policyAsMap);
            if (mappingResult.isFailed()) {
                throw new IllegalArgumentException(
                        String.format("Was not able to map the policy. Reason: %s", mappingResult.getFailureReasons()));
            }

            Response creation = opaPolicyApi.putPolicyModule(tempId, mappingResult.getRego(tempId), false, false);
            if (creation.getStatus() != 200) {
                throw new IllegalArgumentException(
                        String.format("Cannot create policy. Reason: %s", creation.readEntity(String.class)));
            }

            Map<String, Object> opaInput = buildOpaInput(evaluationContext, validationRequest);

            Response dataResponse = dataApiApi.getDocumentWithPath(
                    String.format(IS_ALLOWED_PATH_TEMPLATE, tempId), opaInput, true, false, "full", false, false);
            DataResponse dataResponseObject = dataResponse.readEntity(DataResponse.class);

            ValidationResponse validationResponse = new ValidationResponse().allow(dataResponseObject.result());
            if (!dataResponseObject.result()) {
                validationResponse.explanation(dataResponseObject.explanation());
            }
            return Response.ok(validationResponse).build();
        } catch (Exception e) {
            log.warn("Error", e);
            throw new RuntimeException(e);
        } finally {
            Response deletionResponse = opaPolicyApi.deletePolicyModule(tempId, false);
            if (deletionResponse.getStatus() != 200) {
                log.warn("Was not able to delete the policy {}. Reason: {}",
                        tempId, deletionResponse.readEntity(String.class));
            }
        }
    }

    /**
     * Builds the OPA input structure appropriate for the given evaluation context.
     *
     * <p>For {@link EvaluationContext#HTTP}:
     * {@code {"input": {"request": <testRequest>}}}</p>
     *
     * <p>For {@link EvaluationContext#JSON}:
     * {@code {"input": {"request": {"payload": <payload>, "subject": <subject>}}}}</p>
     *
     * @param evaluationContext the detected evaluation context
     * @param validationRequest the original validation request
     * @return the OPA input map
     * @throws IllegalArgumentException if the required input field is missing
     */
    private Map<String, Object> buildOpaInput(EvaluationContext evaluationContext,
                                               ValidationRequest validationRequest) {
        Map<String, Object> requestContent;

        if (evaluationContext == EvaluationContext.JSON) {
            GenericJsonInput jsonInput = validationRequest.getJsonInput();
            if (jsonInput == null || jsonInput.getPayload() == null) {
                throw new IllegalArgumentException(
                        "Policy uses JSON evaluation context (json: namespace) but no 'jsonInput' with a 'payload' "
                                + "was provided in the validation request. "
                                + "Please supply a 'jsonInput' object with at least a 'payload' field.");
            }
            Map<String, Object> jsonRequest = new HashMap<>();
            jsonRequest.put(PAYLOAD_KEY, jsonInput.getPayload());
            if (jsonInput.getSubject() != null) {
                jsonRequest.put(SUBJECT_KEY, jsonInput.getSubject());
            }
            requestContent = jsonRequest;
        } else {
            // HTTP evaluation context (default)
            if (validationRequest.getTestRequest() == null) {
                throw new IllegalArgumentException(
                        "Policy uses HTTP evaluation context but no 'testRequest' was provided in the "
                                + "validation request. Please supply a 'testRequest' object with method, host, path, etc.");
            }
            requestContent = objectMapper.convertValue(validationRequest.getTestRequest(),
                    new TypeReference<Map<String, Object>>() {});
        }

        Map<String, Object> request = new HashMap<>();
        request.put(REQUEST_KEY, requestContent);
        Map<String, Object> input = new HashMap<>();
        input.put(INPUT_KEY, request);
        return input;
    }

    @Override
    public Response getMappings() {
        return Response.ok(getMappingsFromConfig()).build();
    }

    private Mappings getMappingsFromConfig() {
        Mappings mappings = new Mappings();
        Arrays.stream(OdrlAttribute.values())
                .forEach(attribute -> {
                    List<Mapping> mappingList = toMappingList(mappingConfiguration.get(attribute));
                    switch (attribute) {
                        case LEFT_OPERAND -> mappings.leftOperands(mappingList);
                        case RIGHT_OPERAND -> mappings.rightOperands(mappingList);
                        case OPERATOR -> mappings.operators(mappingList);
                        case CONSTRAINT -> mappings.constraints(mappingList);
                        case OPERAND -> mappings.operands(mappingList);
                        case ASSIGNEE -> mappings.assignees(mappingList);
                        case ACTION -> mappings.actions(mappingList);
                        case TARGET -> mappings.targets(mappingList);
                    }

                });
        return mappings;
    }

    private List<Mapping> toMappingList(NamespacedMap namespacedMap) {
        List<Mapping> mappings = new ArrayList<>();
        namespacedMap.forEach((namespace, value) -> value.entrySet()
                .stream()
                .map(regoMapEntry -> new Mapping()
                        .description(regoMapEntry.getValue().description())
                        .name(String.format("%s:%s", namespace, regoMapEntry.getKey())))
                .forEach(mappings::add));
        return mappings;
    }

}
