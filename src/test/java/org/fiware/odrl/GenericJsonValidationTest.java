package org.fiware.odrl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.inject.Instance;
import org.fiware.odrl.jsonld.EvaluationContext;
import org.fiware.odrl.jsonld.JsonLdHandler;
import org.fiware.odrl.mapping.ConstraintMapper;
import org.fiware.odrl.mapping.LeftOperandMapper;
import org.fiware.odrl.mapping.MappingConfiguration;
import org.fiware.odrl.mapping.MappingResult;
import org.fiware.odrl.mapping.NamespacedMap;
import org.fiware.odrl.mapping.OdrlAttribute;
import org.fiware.odrl.mapping.OdrlMapper;
import org.fiware.odrl.mapping.OperatorMapper;
import org.fiware.odrl.mapping.RegoMap;
import org.fiware.odrl.mapping.RightOperandMapper;
import org.fiware.odrl.rego.RegoMethod;
import org.fiware.odrl.verification.TypeVerifier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Comprehensive unit tests for the non-HTTP (generic JSON) evaluation path.
 *
 * <p>Verifies that:</p>
 * <ul>
 *   <li>{@code json:*} mapping entries in {@code mapping.json} are present and
 *       resolve to the correct rego methods.</li>
 *   <li>{@link OdrlMapper} generates correct Rego code for policies using
 *       {@code json:} namespaced terms (actions, left operands, etc.).</li>
 *   <li>The {@link EvaluationContext} is correctly detected for JSON-context
 *       policies.</li>
 *   <li>Existing HTTP policies continue to produce valid Rego (backward
 *       compatibility).</li>
 *   <li>The {@link Pep} enum is not involved in JSON evaluation routing.</li>
 * </ul>
 *
 * <p>This is a plain JUnit test (no Quarkus / TestContainers) — it loads
 * {@code mapping.json} directly and constructs the mapper manually.</p>
 */
public class GenericJsonValidationTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /** The test package name used when generating rego from MappingResult. */
    private static final String TEST_PACKAGE_NAME = "test_policy";

    private static MappingConfiguration mappingConfiguration;

    /**
     * Loads the production {@code mapping.json} into a
     * {@link MappingConfiguration} for use across all tests.
     */
    @BeforeAll
    static void loadMappingConfiguration() throws Exception {
        try (InputStream is = GenericJsonValidationTest.class.getClassLoader()
                .getResourceAsStream("mapping.json")) {
            assertNotNull(is, "mapping.json should be on the classpath");
            mappingConfiguration = OBJECT_MAPPER.readValue(is, MappingConfiguration.class);
        }
    }

    // -----------------------------------------------------------------------
    // Mapping resolution tests — verify json: entries exist in mapping.json
    // -----------------------------------------------------------------------

    /**
     * Verifies that all expected {@code json:*} action mappings are present in
     * {@code mapping.json} and resolve to the correct rego method signatures.
     */
    @ParameterizedTest(name = "json:{0} action -> {1}")
    @MethodSource("jsonActionMappings")
    @DisplayName("JSON action mappings should be present and correct")
    void testJsonActionMappingResolution(String actionName,
                                         String expectedRegoMethod) {
        NamespacedMap actionMap = mappingConfiguration.get(OdrlAttribute.ACTION);
        assertNotNull(actionMap, "Action mappings should exist");
        RegoMap jsonActions = actionMap.get("json");
        assertNotNull(jsonActions, "json namespace should exist in action mappings");
        RegoMethod method = jsonActions.get(actionName);
        assertNotNull(method, String.format("json:%s action should be mapped", actionName));
        assertEquals(expectedRegoMethod, method.regoMethod(),
                String.format("json:%s rego method should match", actionName));
    }

    /**
     * Provides test cases for all {@code json:*} action mapping entries.
     */
    static Stream<Arguments> jsonActionMappings() {
        return Stream.of(
                Arguments.of("read", "json_action.is_read(generic.payload)"),
                Arguments.of("write", "json_action.is_write(generic.payload)"),
                Arguments.of("create", "json_action.is_create(generic.payload)"),
                Arguments.of("delete", "json_action.is_delete(generic.payload)"),
                Arguments.of("use", "json_action.is_use(generic.payload)")
        );
    }

    /**
     * Verifies that all expected {@code json:*} left-operand mappings are
     * present in {@code mapping.json} and resolve to the correct rego methods.
     */
    @ParameterizedTest(name = "json:{0} leftOperand -> {1}")
    @MethodSource("jsonLeftOperandMappings")
    @DisplayName("JSON leftOperand mappings should be present and correct")
    void testJsonLeftOperandMappingResolution(String operandName,
                                              String expectedRegoMethod) {
        NamespacedMap loMap = mappingConfiguration.get(OdrlAttribute.LEFT_OPERAND);
        assertNotNull(loMap, "LeftOperand mappings should exist");
        RegoMap jsonLo = loMap.get("json");
        assertNotNull(jsonLo, "json namespace should exist in leftOperand mappings");
        RegoMethod method = jsonLo.get(operandName);
        assertNotNull(method, String.format("json:%s leftOperand should be mapped", operandName));
        assertEquals(expectedRegoMethod, method.regoMethod(),
                String.format("json:%s rego method should match", operandName));
    }

    /**
     * Provides test cases for all {@code json:*} left-operand mapping entries.
     */
    static Stream<Arguments> jsonLeftOperandMappings() {
        return Stream.of(
                Arguments.of("payloadValue", "json_lo.payload_value(generic.payload, %s)"),
                Arguments.of("subjectValue", "json_lo.subject_value(generic.subject, %s)"),
                Arguments.of("payloadType", "json_lo.payload_type(generic.payload)")
        );
    }

    /**
     * Verifies that {@code json:*} target entries exist in {@code mapping.json}.
     */
    @ParameterizedTest(name = "json:{0} target should be mapped")
    @MethodSource("jsonTargetMappings")
    @DisplayName("JSON target mappings should be present")
    void testJsonTargetMappingResolution(String targetName, String expectedRegoFragment) {
        NamespacedMap targetMap = mappingConfiguration.get(OdrlAttribute.TARGET);
        assertNotNull(targetMap, "Target mappings should exist");
        RegoMap jsonTargets = targetMap.get("json");
        assertNotNull(jsonTargets, "json namespace should exist in target mappings");
        RegoMethod method = jsonTargets.get(targetName);
        assertNotNull(method, String.format("json:%s target should be mapped", targetName));
        assertTrue(method.regoMethod().contains(expectedRegoFragment),
                String.format("json:%s should reference %s", targetName, expectedRegoFragment));
    }

    /**
     * Provides test cases for {@code json:*} target mapping entries.
     */
    static Stream<Arguments> jsonTargetMappings() {
        return Stream.of(
                Arguments.of("uid", "generic.target"),
                Arguments.of("target", "generic.target")
        );
    }

    /**
     * Verifies that {@code json:*} assignee entries exist in
     * {@code mapping.json}.
     */
    @ParameterizedTest(name = "json:{0} assignee should be mapped")
    @MethodSource("jsonAssigneeMappings")
    @DisplayName("JSON assignee mappings should be present")
    void testJsonAssigneeMappingResolution(String assigneeName) {
        NamespacedMap assigneeMap = mappingConfiguration.get(OdrlAttribute.ASSIGNEE);
        assertNotNull(assigneeMap, "Assignee mappings should exist");
        RegoMap jsonAssignees = assigneeMap.get("json");
        assertNotNull(jsonAssignees, "json namespace should exist in assignee mappings");
        assertNotNull(jsonAssignees.get(assigneeName),
                String.format("json:%s assignee should be mapped", assigneeName));
    }

    /**
     * Provides test cases for {@code json:*} assignee mapping entries.
     */
    static Stream<Arguments> jsonAssigneeMappings() {
        return Stream.of(
                Arguments.of("assignee"),
                Arguments.of("any")
        );
    }

    // -----------------------------------------------------------------------
    // Rego generation tests — OdrlMapper with json: policies
    // -----------------------------------------------------------------------

    /**
     * Verifies that the mapper generates correct Rego for a simple
     * {@code json:read} policy with a target and a {@code json:any} assignee.
     * Corresponds to test policy 7001.
     */
    @Test
    @DisplayName("OdrlMapper should generate correct rego for json:read policy (7001)")
    void testMapperGeneratesCorrectRegoForJsonReadPolicy() {
        OdrlMapper mapper = createMapper();
        Map<String, Object> policy = createJsonReadPolicy();

        MappingResult result = mapper.mapOdrl(policy);
        assertFalse(result.isFailed(),
                "Mapping should not fail: " + result.getFailureReasons());

        String rego = result.getRego(TEST_PACKAGE_NAME);

        // Verify package declaration
        assertTrue(rego.contains("package " + TEST_PACKAGE_NAME),
                "Rego should declare the test package");

        // Verify imports
        assertTrue(rego.contains("import data.json.action as json_action"),
                "Should import json action module");
        assertTrue(rego.contains("import data.odrl.target as odrl_target"),
                "Should import odrl target module");
        assertTrue(rego.contains("import data.vc.assignee as vc_assignee"),
                "Should import vc assignee module");
        assertTrue(rego.contains("import data.utils.generic as generic"),
                "Should import utils.generic for json evaluation");
        assertTrue(rego.contains("import data.utils.helper as helper"),
                "Should import utils.helper (always present)");

        // Verify rules
        assertTrue(rego.contains("json_action.is_read(generic.payload)"),
                "Should have json:read action rule referencing generic.payload");
        assertTrue(rego.contains("vc_assignee.is_any"),
                "Should have json:any assignee rule");
        assertTrue(rego.contains(
                        "odrl_target.is_target(helper.target,\"urn:example:resource:1\")"),
                "Should have target check for resource:1");

        // Verify UID was captured
        assertEquals("urn:example:policy:json-read-7001", result.getUid(),
                "Policy UID should be captured");
    }

    /**
     * Verifies that the mapper generates correct Rego for a {@code json:use}
     * policy with a constraint that uses {@code json:payloadType} as a left
     * operand. Corresponds to test policy 7002.
     */
    @Test
    @DisplayName("OdrlMapper should generate correct rego for json policy with constraint (7002)")
    void testMapperGeneratesCorrectRegoForJsonPolicyWithConstraint() {
        OdrlMapper mapper = createMapper();
        Map<String, Object> policy = createJsonConstraintPolicy();

        MappingResult result = mapper.mapOdrl(policy);
        assertFalse(result.isFailed(),
                "Mapping should not fail: " + result.getFailureReasons());

        String rego = result.getRego(TEST_PACKAGE_NAME);

        // Verify json action
        assertTrue(rego.contains("import data.json.action as json_action"),
                "Should import json action module");
        assertTrue(rego.contains("json_action.is_use(generic.payload)"),
                "Should have json:use action rule");

        // Verify constraint with json:payloadType left operand
        assertTrue(rego.contains("import data.json.leftOperand as json_lo"),
                "Should import json leftOperand module");
        assertTrue(rego.contains("import data.odrl.operator as odrl_operator"),
                "Should import odrl operator module");
        assertTrue(rego.contains(
                        "odrl_operator.eq_operator(json_lo.payload_type(generic.payload),\"document\")"),
                "Should have eq constraint checking payloadType == document");

        assertEquals("urn:example:policy:json-constraint-7002", result.getUid(),
                "Policy UID should be captured");
    }

    /**
     * Verifies that the generic utils import is present in all generated Rego,
     * ensuring that {@code generic.payload}, {@code generic.subject}, etc. are
     * always resolvable at OPA evaluation time.
     */
    @Test
    @DisplayName("MappingResult should always include the generic utils import")
    void testMappingResultIncludesGenericImport() {
        MappingResult result = new MappingResult();
        result.setUid("test-uid");
        result.addRule("some_rule");

        String rego = result.getRego(TEST_PACKAGE_NAME);
        assertTrue(rego.contains("import data.utils.generic as generic"),
                "All generated rego should import utils.generic");
        assertTrue(rego.contains("import data.utils.helper as helper"),
                "All generated rego should import utils.helper");
    }

    // -----------------------------------------------------------------------
    // Evaluation context detection tests
    // -----------------------------------------------------------------------

    /**
     * Verifies that the JSON test policy (7001) is correctly detected as
     * requiring JSON evaluation context, based on its
     * {@code pap:evaluationContext} field.
     */
    @Test
    @DisplayName("JSON test policy 7001 should be detected as JSON evaluation context")
    void testJsonReadPolicyDetectedAsJsonContext() throws Exception {
        JsonLdHandler handler = createJsonLdHandler();
        String json = OBJECT_MAPPER.writeValueAsString(createJsonReadPolicy());

        EvaluationContext ctx = handler.detectEvaluationContext(json);
        assertEquals(EvaluationContext.JSON, ctx,
                "Policy with pap:evaluationContext=json should be JSON context");
    }

    /**
     * Verifies that the JSON test policy (7002, with constraint) is correctly
     * detected as requiring JSON evaluation context.
     */
    @Test
    @DisplayName("JSON test policy 7002 should be detected as JSON evaluation context")
    void testJsonConstraintPolicyDetectedAsJsonContext() throws Exception {
        JsonLdHandler handler = createJsonLdHandler();
        String json = OBJECT_MAPPER.writeValueAsString(createJsonConstraintPolicy());

        EvaluationContext ctx = handler.detectEvaluationContext(json);
        assertEquals(EvaluationContext.JSON, ctx,
                "Policy with json: namespace terms should be JSON context");
    }

    /**
     * Verifies that a standard ODRL HTTP policy is detected as requiring
     * HTTP evaluation context (backward compatibility).
     */
    @Test
    @DisplayName("Standard ODRL HTTP policy should be detected as HTTP context")
    void testHttpPolicyDetectedAsHttpContext() throws Exception {
        JsonLdHandler handler = createJsonLdHandler();
        Map<String, Object> httpPolicy = createHttpPolicy();
        String json = OBJECT_MAPPER.writeValueAsString(httpPolicy);

        EvaluationContext ctx = handler.detectEvaluationContext(json);
        assertEquals(EvaluationContext.HTTP, ctx,
                "Standard ODRL policy without json: namespace should be HTTP context");
    }

    // -----------------------------------------------------------------------
    // Backward compatibility tests
    // -----------------------------------------------------------------------

    /**
     * Verifies that the mapper still generates valid Rego for standard HTTP
     * policies (e.g., {@code odrl:read}) after the JSON evaluation changes.
     */
    @Test
    @DisplayName("OdrlMapper should still generate correct rego for HTTP policies")
    void testBackwardCompatibilityWithHttpPolicy() {
        OdrlMapper mapper = createMapper();
        Map<String, Object> httpPolicy = createHttpPolicy();

        MappingResult result = mapper.mapOdrl(httpPolicy);
        assertFalse(result.isFailed(),
                "HTTP policy mapping should not fail: " + result.getFailureReasons());

        String rego = result.getRego(TEST_PACKAGE_NAME);

        // HTTP action should use odrl_action with helper.http_part
        assertTrue(rego.contains("import data.odrl.action as odrl_action"),
                "Should import odrl action for HTTP policy");
        assertTrue(rego.contains("odrl_action.is_read(helper.http_part)"),
                "Should have HTTP read action rule using helper.http_part");

        // Assignee should use odrl_assignee with helper.issuer
        assertTrue(rego.contains("import data.odrl.assignee as odrl_assignee"),
                "Should import odrl assignee for HTTP policy");
    }

    // -----------------------------------------------------------------------
    // Pep enum isolation test
    // -----------------------------------------------------------------------

    /**
     * Verifies that the {@link Pep} enum only contains API gateway types
     * (APISIX, KONG) and does not include a JSON or GENERIC variant. The
     * evaluation context routing is driven by the policy's JSON-LD context,
     * not by the Pep enum.
     */
    @Test
    @DisplayName("Pep enum should only contain HTTP gateway types, not JSON")
    void testPepEnumNotInvolvedInJsonEvaluation() {
        Pep[] peps = Pep.values();
        for (Pep pep : peps) {
            assertNotEquals("JSON", pep.name(),
                    "Pep enum should not have a JSON value");
            assertNotEquals("GENERIC", pep.name(),
                    "Pep enum should not have a GENERIC value");
        }
        assertEquals(2, peps.length,
                "Pep enum should have exactly 2 values (APISIX, KONG)");
    }

    // -----------------------------------------------------------------------
    // Error case tests
    // -----------------------------------------------------------------------

    /**
     * Verifies that a policy without a valid type is rejected by the mapper
     * regardless of whether it uses JSON namespace terms.
     */
    @Test
    @DisplayName("Policy without valid @type should fail even with json: terms")
    void testInvalidPolicyTypeWithJsonTermsFails() {
        OdrlMapper mapper = createMapper();
        Map<String, Object> invalidPolicy = new LinkedHashMap<>();
        invalidPolicy.put("@type", "invalid:Type");
        invalidPolicy.put("odrl:uid", "urn:example:invalid");
        invalidPolicy.put("pap:evaluationContext", "json");
        invalidPolicy.put("odrl:permission", Map.of(
                "odrl:action", Map.of("@id", "json:read"),
                "odrl:target", "urn:example:resource",
                "odrl:assignee", "json:any"
        ));

        MappingResult result = mapper.mapOdrl(invalidPolicy);
        assertTrue(result.isFailed(),
                "Mapping should fail for unsupported policy type");
    }

    /**
     * Verifies that a JSON policy missing the required permission block is
     * rejected.
     */
    @Test
    @DisplayName("JSON policy without permission should fail")
    void testJsonPolicyWithoutPermissionFails() {
        OdrlMapper mapper = createMapper();
        Map<String, Object> noPermissionPolicy = new LinkedHashMap<>();
        noPermissionPolicy.put("@type", "odrl:Policy");
        noPermissionPolicy.put("odrl:uid", "urn:example:no-perm");
        noPermissionPolicy.put("pap:evaluationContext", "json");
        // No odrl:permission

        MappingResult result = mapper.mapOdrl(noPermissionPolicy);
        assertTrue(result.isFailed(),
                "Mapping should fail for policy without permission");
    }

    /**
     * Verifies that a JSON policy with a missing action in the permission is
     * rejected.
     */
    @Test
    @DisplayName("JSON policy permission without action should fail")
    void testJsonPolicyPermissionWithoutActionFails() {
        OdrlMapper mapper = createMapper();
        Map<String, Object> noActionPolicy = new LinkedHashMap<>();
        noActionPolicy.put("@type", "odrl:Policy");
        noActionPolicy.put("odrl:uid", "urn:example:no-action");
        noActionPolicy.put("odrl:permission", Map.of(
                "odrl:target", "urn:example:resource",
                "odrl:assignee", "json:any"
                // No odrl:action
        ));

        MappingResult result = mapper.mapOdrl(noActionPolicy);
        assertTrue(result.isFailed(),
                "Mapping should fail for permission without action");
    }

    // -----------------------------------------------------------------------
    // Helper methods: policy construction
    // -----------------------------------------------------------------------

    /**
     * Creates a compacted JSON representation of test policy 7001: a simple
     * {@code json:read} policy with {@code json:any} assignee.
     */
    private static Map<String, Object> createJsonReadPolicy() {
        Map<String, Object> policy = new LinkedHashMap<>();
        policy.put("@type", "odrl:Policy");
        policy.put("odrl:uid", "urn:example:policy:json-read-7001");
        policy.put("pap:evaluationContext", "json");

        Map<String, Object> permission = new LinkedHashMap<>();
        permission.put("odrl:target", "urn:example:resource:1");
        permission.put("odrl:assignee", "json:any");
        permission.put("odrl:action", Map.of("@id", "json:read"));

        policy.put("odrl:permission", permission);
        return policy;
    }

    /**
     * Creates a compacted JSON representation of test policy 7002: a
     * {@code json:use} policy with a {@code json:payloadType == "document"}
     * constraint.
     */
    private static Map<String, Object> createJsonConstraintPolicy() {
        Map<String, Object> policy = new LinkedHashMap<>();
        policy.put("@type", "odrl:Policy");
        policy.put("odrl:uid", "urn:example:policy:json-constraint-7002");
        policy.put("pap:evaluationContext", "json");

        Map<String, Object> constraint = new LinkedHashMap<>();
        constraint.put("odrl:leftOperand", "json:payloadType");
        constraint.put("odrl:operator", "odrl:eq");
        constraint.put("odrl:rightOperand", "document");

        Map<String, Object> permission = new LinkedHashMap<>();
        permission.put("odrl:target", "urn:example:resource:2");
        permission.put("odrl:assignee", "json:any");
        permission.put("odrl:action", Map.of("@id", "json:use"));
        permission.put("odrl:constraint", constraint);

        policy.put("odrl:permission", permission);
        return policy;
    }

    /**
     * Creates a standard ODRL HTTP policy using {@code odrl:read} action for
     * backward-compatibility testing.
     */
    private static Map<String, Object> createHttpPolicy() {
        Map<String, Object> policy = new LinkedHashMap<>();
        policy.put("@type", "odrl:Policy");
        policy.put("odrl:uid", "urn:example:policy:http-compat-1");
        policy.put("odrl:permission", Map.of(
                "odrl:target", "urn:example:api:endpoint",
                "odrl:assignee", "urn:example:org:1",
                "odrl:action", Map.of("@id", "odrl:read")
        ));
        return policy;
    }

    // -----------------------------------------------------------------------
    // Helper methods: infrastructure
    // -----------------------------------------------------------------------

    /**
     * Creates an {@link OdrlMapper} wired with the production
     * {@link MappingConfiguration} and all required sub-mappers, but without
     * CDI (plain constructor injection with a mocked {@code TypeVerifier}
     * instance).
     */
    @SuppressWarnings("unchecked")
    private OdrlMapper createMapper() {
        ConstraintMapper constraintMapper =
                new ConstraintMapper(OBJECT_MAPPER, mappingConfiguration);
        LeftOperandMapper leftOperandMapper =
                new LeftOperandMapper(OBJECT_MAPPER, mappingConfiguration);
        OperatorMapper operatorMapper =
                new OperatorMapper(OBJECT_MAPPER, mappingConfiguration);
        RightOperandMapper rightOperandMapper =
                new RightOperandMapper(OBJECT_MAPPER, mappingConfiguration);

        Instance<TypeVerifier> typeVerifiers = Mockito.mock(Instance.class);
        Mockito.when(typeVerifiers.stream()).thenReturn(Stream.empty());

        return new OdrlMapper(OBJECT_MAPPER, mappingConfiguration,
                typeVerifiers, leftOperandMapper, constraintMapper,
                operatorMapper, rightOperandMapper);
    }

    /**
     * Creates a minimal {@link JsonLdHandler} with only the
     * {@link ObjectMapper} injected (via reflection). This is sufficient for
     * {@code detectEvaluationContext}, which only parses JSON.
     */
    private JsonLdHandler createJsonLdHandler() throws Exception {
        JsonLdHandler handler = new JsonLdHandler();
        Field objectMapperField =
                JsonLdHandler.class.getDeclaredField("objectMapper");
        objectMapperField.setAccessible(true);
        objectMapperField.set(handler, OBJECT_MAPPER);
        return handler;
    }
}
