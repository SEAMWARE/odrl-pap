package org.fiware.odrl.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.inject.Instance;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for mappings that declare a parameter in their rego-method template, e.g.
 * {@code http:bodyValue} with {@code http_lo.body_value(helper.http_part.body, %s)}.
 *
 * <p>Such a parameter is taken from the value of the operand in the policy. If the policy does not
 * provide one, the mapping has to fail instead of emitting the unresolved template, which would end up
 * as literal {@code %s} in the generated rego and break the compilation of the whole OPA bundle.</p>
 *
 * <p>This is a plain JUnit test - it loads the production {@code mapping.json} and constructs the mapper
 * manually, without Quarkus.</p>
 */
class ParameterizedMappingTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /** The package name used when generating rego from the mapping result. */
    private static final String TEST_PACKAGE_NAME = "test_policy";

    /** The format parameter that must never show up in generated rego. */
    private static final String UNRESOLVED_PARAMETER = "%s";

    private static MappingConfiguration mappingConfiguration;

    @BeforeAll
    static void loadMappingConfiguration() throws Exception {
        try (InputStream is = ParameterizedMappingTest.class.getClassLoader()
                .getResourceAsStream("mapping.json")) {
            assertNotNull(is, "mapping.json should be on the classpath");
            mappingConfiguration = OBJECT_MAPPER.readValue(is, MappingConfiguration.class);
        }
    }

    /**
     * Verifies that a parameterized leftOperand is only accepted if the policy provides the parameter,
     * and that the parameter is substituted into the generated rego.
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("leftOperands")
    @DisplayName("A parameterized leftOperand should either be resolved or fail the mapping")
    void testParameterizedLeftOperand(String description,
                                      Map<String, Object> leftOperandFragment,
                                      String expectedRegoFragment) {
        MappingResult result = createMapper().mapOdrl(createPolicy(leftOperandFragment));

        if (expectedRegoFragment == null) {
            assertTrue(result.isFailed(),
                    "A leftOperand without the required parameter should fail the mapping.");
            assertTrue(result.getFailureReasons().stream().anyMatch(reason -> reason.contains("http:bodyValue")),
                    "The failure should name the operand that misses the parameter, but was: "
                            + result.getFailureReasons());
        } else {
            assertFalse(result.isFailed(),
                    "The mapping should not fail: " + result.getFailureReasons());
            String rego = result.getRego(TEST_PACKAGE_NAME);
            assertTrue(rego.contains(expectedRegoFragment),
                    String.format("The rego should contain `%s`, but was: %s", expectedRegoFragment, rego));
            assertFalse(rego.contains(UNRESOLVED_PARAMETER),
                    "The generated rego should not contain an unresolved parameter: " + rego);
        }
    }

    /**
     * Provides the supported and unsupported spellings of the parameterized {@code http:bodyValue} operand.
     */
    static Stream<Arguments> leftOperands() {
        String resolved = "http_lo.body_value(helper.http_part.body, \"$.numNodes.value\")";
        return Stream.of(
                Arguments.of("the operand key carries the parameter as a plain value",
                        Map.of("http:bodyValue", "$.numNodes.value"),
                        resolved),
                Arguments.of("the operand key carries the parameter as a value object",
                        Map.of("http:bodyValue", Map.of("@value", "$.numNodes.value")),
                        resolved),
                Arguments.of("odrl:leftOperand references the operand by id, without a parameter",
                        Map.of("odrl:leftOperand", Map.of("@id", "http:bodyValue")),
                        null),
                Arguments.of("odrl:leftOperand references the operand by name, without a parameter",
                        Map.of("odrl:leftOperand", "http:bodyValue"),
                        null)
        );
    }

    /**
     * Verifies that a leftOperand without parameters keeps working, to ensure the guard does not reject
     * the regular case.
     */
    @ParameterizedTest(name = "{0} should map to {1}")
    @MethodSource("unparameterizedLeftOperands")
    @DisplayName("A leftOperand without parameters should still be mapped")
    void testUnparameterizedLeftOperand(String leftOperand, String expectedRegoFragment) {
        MappingResult result = createMapper()
                .mapOdrl(createPolicy(Map.of("odrl:leftOperand", Map.of("@id", leftOperand))));

        assertFalse(result.isFailed(), "The mapping should not fail: " + result.getFailureReasons());
        String rego = result.getRego(TEST_PACKAGE_NAME);
        assertTrue(rego.contains(expectedRegoFragment),
                String.format("The rego should contain `%s`, but was: %s", expectedRegoFragment, rego));
    }

    static Stream<Arguments> unparameterizedLeftOperands() {
        return Stream.of(
                Arguments.of("http:path", "http_lo.path(helper.http_part)"),
                Arguments.of("odrl:dateTime", "odrl_lo.current_time"),
                Arguments.of("vc:type", "vc_lo.types(helper.verifiable_credential)")
        );
    }

    /**
     * Verifies that the guard also covers attributes that are used without a value from the policy -
     * here an action, which never provides one. Since no parameterized action is configured in the
     * production mapping, the configuration is extended with one for this test.
     */
    @Test
    @DisplayName("A parameterized action should fail the mapping instead of leaking the parameter")
    void testParameterizedActionFailsTheMapping() {
        MappingConfiguration configuration = new MappingConfiguration();
        configuration.deepMerge(mappingConfiguration);
        RegoMap testActions = new RegoMap();
        testActions.put("parameterized",
                new RegoMethod("odrl.action as odrl_action", "odrl_action.is_action(helper.http_part, %s)"));
        configuration.get(OdrlAttribute.ACTION).put("test", testActions);

        Map<String, Object> policy = createPolicy(Map.of("http:bodyValue", "$.numNodes.value"));
        Map<String, Object> permission = permissionOf(policy);
        permission.put("odrl:action", Map.of("@id", "test:parameterized"));

        MappingResult result = createMapper(configuration).mapOdrl(policy);

        assertTrue(result.isFailed(), "A parameterized action should fail the mapping.");
        assertTrue(result.getFailureReasons().stream().anyMatch(reason -> reason.contains("test:parameterized")),
                "The failure should name the action that misses the parameter, but was: "
                        + result.getFailureReasons());
    }

    /**
     * Creates a policy with a single refinement constraint, built from the given leftOperand fragment.
     */
    private Map<String, Object> createPolicy(Map<String, Object> leftOperandFragment) {
        Map<String, Object> constraint = new LinkedHashMap<>();
        constraint.put("@type", "odrl:Constraint");
        constraint.putAll(leftOperandFragment);
        constraint.put("odrl:operator", Map.of("@id", "odrl:eq"));
        constraint.put("odrl:rightOperand", "3");

        Map<String, Object> permission = new LinkedHashMap<>();
        permission.put("odrl:assignee", Map.of("@id", "vc:any"));
        permission.put("odrl:action", Map.of("@id", "odrl:use"));
        permission.put("odrl:target", Map.of(
                "@type", "odrl:AssetCollection",
                "odrl:source", "urn:asset",
                "odrl:refinement", List.of(constraint)));

        Map<String, Object> policy = new LinkedHashMap<>();
        policy.put("@type", "odrl:Policy");
        policy.put("odrl:uid", "urn:test:policy:parameterized");
        policy.put("odrl:permission", permission);
        return policy;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> permissionOf(Map<String, Object> policy) {
        return (Map<String, Object>) policy.get("odrl:permission");
    }

    private OdrlMapper createMapper() {
        return createMapper(mappingConfiguration);
    }

    private OdrlMapper createMapper(MappingConfiguration configuration) {
        Instance<TypeVerifier> typeVerifiers = Mockito.mock(Instance.class);
        Mockito.when(typeVerifiers.stream()).thenReturn(Stream.empty());

        return new OdrlMapper(OBJECT_MAPPER, configuration, typeVerifiers,
                new LeftOperandMapper(OBJECT_MAPPER, configuration),
                new ConstraintMapper(OBJECT_MAPPER, configuration),
                new OperatorMapper(OBJECT_MAPPER, configuration),
                new RightOperandMapper(OBJECT_MAPPER, configuration));
    }
}
