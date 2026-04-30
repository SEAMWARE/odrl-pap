package org.fiware.odrl.jsonld;

import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.JsonLdOptions;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.JsonDocument;
import com.apicatalog.jsonld.loader.HttpLoader;
import com.apicatalog.jsonld.loader.SchemeRouter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Handles JSON-LD expansion and compaction for ODRL policies, and detects the
 * evaluation context (HTTP vs. generic JSON) from the compacted policy's
 * namespace usage.
 */
@Slf4j
@ApplicationScoped
public class JsonLdHandler {

    /**
     * Prefix used by the PAP evaluation-context namespace in compacted JSON-LD
     * (e.g., {@code "pap:evaluationContext"}).
     */
    private static final String PAP_EVALUATION_CONTEXT_KEY = "pap:evaluationContext";

    /**
     * Value of {@code pap:evaluationContext} that explicitly selects the generic
     * JSON evaluation path.
     */
    private static final String JSON_EVALUATION_CONTEXT_VALUE = "json";

    /**
     * Compiled regex that matches any compacted JSON-LD term prefixed with
     * {@code "json:"} (e.g., {@code "json:read"}, {@code "json:payloadValue"}).
     */
    private static final Pattern JSON_NAMESPACE_PATTERN = Pattern.compile("\"json:");

    @Inject
    private CloseableHttpClient httpClient;

    @Inject
    private CompactionContext compactionContext;

    @Inject
    private ObjectMapper objectMapper;

    /**
     * Expands and then compacts the given JSON-LD input using the configured
     * compaction context. This normalises namespace prefixes so that downstream
     * mapping logic can rely on predictable prefixed terms (e.g.,
     * {@code odrl:read}, {@code json:payloadValue}).
     *
     * @param jsonLdInput the raw JSON-LD policy as an {@link InputStream}
     * @return the compacted JSON string
     * @throws JsonLdError if expansion or compaction fails
     */
    public String handleJsonLd(InputStream jsonLdInput) throws JsonLdError {
        HttpLoader httpLoader = new HttpLoader(new JsonLdApacheHttpClient(httpClient));
        SchemeRouter schemeRouter = new SchemeRouter()
                .set("https", httpLoader)
                .set("http", httpLoader)
                .set("file", httpLoader);

        JsonLdOptions jsonLdOptions = new JsonLdOptions(schemeRouter);
        JsonReader jsonReader = Json.createReader(jsonLdInput);

        JsonObject originalJson = jsonReader.readObject();
        Document orginalDocument = JsonDocument.of(originalJson);
        // expand to properly prefix all terms according to there context.
        Document expandedDocument = JsonDocument.of(new QuarkusExpansionApi(orginalDocument, jsonLdOptions).get());
        // compact to set the namespace prefixes.
        JsonObject jsonObject = new QuarkusCompactionApi(expandedDocument, compactionContext.getContext(), jsonLdOptions).get();
        String jsonString = jsonObject.toString();
        log.debug("Compacted json {}", jsonString);
        return jsonString;
    }

    /**
     * Detects the evaluation context for a compacted ODRL policy. The routing
     * decision is driven entirely by the policy's own JSON-LD content — the
     * {@link org.fiware.odrl.Pep} enum is not involved.
     *
     * <p>Detection logic (in priority order):
     * <ol>
     *   <li>If the policy (or any sub-policy in {@code @graph}) contains an
     *       explicit {@code pap:evaluationContext} field whose value is
     *       {@code "json"}, return {@link EvaluationContext#JSON}.</li>
     *   <li>If the serialised JSON contains any {@code "json:"}-prefixed term
     *       (action, left operand, etc.), return
     *       {@link EvaluationContext#JSON}.</li>
     *   <li>Otherwise, return {@link EvaluationContext#HTTP} (the default).</li>
     * </ol>
     *
     * @param compactedJson the compacted JSON-LD string produced by
     *                      {@link #handleJsonLd(InputStream)}
     * @return the detected {@link EvaluationContext}
     */
    public EvaluationContext detectEvaluationContext(String compactedJson) {
        // 1. Check for explicit pap:evaluationContext field
        try {
            Map<String, Object> policyMap = objectMapper.readValue(
                    compactedJson, new TypeReference<Map<String, Object>>() {});

            if (hasJsonEvaluationContext(policyMap)) {
                log.debug("Detected JSON evaluation context via explicit pap:evaluationContext field");
                return EvaluationContext.JSON;
            }

            // Also check inside @graph if present
            Object graph = policyMap.get("@graph");
            if (graph instanceof List<?> graphList) {
                for (Object entry : graphList) {
                    if (entry instanceof Map<?, ?>) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> graphEntry = (Map<String, Object>) entry;
                        if (hasJsonEvaluationContext(graphEntry)) {
                            log.debug("Detected JSON evaluation context via pap:evaluationContext in @graph entry");
                            return EvaluationContext.JSON;
                        }
                    }
                }
            }
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse compacted JSON for evaluation context detection, falling back to text scan", e);
        }

        // 2. Fall back to scanning for json: namespace prefixed terms
        if (JSON_NAMESPACE_PATTERN.matcher(compactedJson).find()) {
            log.debug("Detected JSON evaluation context via json:-prefixed terms in compacted policy");
            return EvaluationContext.JSON;
        }

        // 3. Default to HTTP
        log.debug("No JSON evaluation context indicators found, defaulting to HTTP");
        return EvaluationContext.HTTP;
    }

    /**
     * Checks whether the given policy map contains an explicit
     * {@code pap:evaluationContext} field with the value {@code "json"}.
     *
     * @param policyMap a parsed compacted JSON-LD policy
     * @return {@code true} if the JSON evaluation context is explicitly declared
     */
    private boolean hasJsonEvaluationContext(Map<String, Object> policyMap) {
        Object evalCtx = policyMap.get(PAP_EVALUATION_CONTEXT_KEY);
        if (evalCtx instanceof String evalCtxString) {
            return JSON_EVALUATION_CONTEXT_VALUE.equalsIgnoreCase(evalCtxString);
        }
        return false;
    }

}
