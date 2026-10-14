package org.fiware.odrl.rego;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link RegoMethod}, especially the detection of rego-method templates that require parameters
 * to be provided by the policy.
 */
class RegoMethodTest {

    /** Package used for the test fixtures, irrelevant for the parameter detection. */
    private static final String TEST_PACKAGE = "test.package as test";

    @ParameterizedTest(name = "`{0}` should declare {1} parameter(s)")
    @MethodSource("regoMethods")
    @DisplayName("The number of parameters should be derived from the rego-method template")
    void testParameterCount(String regoMethod, int expectedParameterCount) {
        RegoMethod theMethod = new RegoMethod(TEST_PACKAGE, regoMethod);

        assertEquals(expectedParameterCount, theMethod.parameterCount(),
                "The declared parameters should be counted.");
        assertEquals(expectedParameterCount > 0, theMethod.isParameterized(),
                "A template with parameters should be reported as parameterized.");
    }

    static Stream<Arguments> regoMethods() {
        return Stream.of(
                // real-world templates from mapping.json
                Arguments.of("http_lo.path(helper.http_part)", 0),
                Arguments.of("odrl_lo.current_time", 0),
                Arguments.of("vc_assignee.is_any", 0),
                Arguments.of("vc_lo.role(helper.verifiable_credential, helper.organization_did)", 0),
                Arguments.of("http_lo.body_value(helper.http_part.body, %s)", 1),
                Arguments.of("gaia_x_lo.getClaim(helper.verifiable_credential,%s)", 1),
                Arguments.of("json_lo.payload_value(generic.payload, %s)", 1),
                Arguments.of("odrl_operator.eq_operator(%s,%s)", 2),
                Arguments.of("odrl_target.is_target(generic.get_value(%s),%s)", 2),
                // escaped percent signs do not denote parameters
                Arguments.of("some_method(\"100%%\")", 0),
                Arguments.of("some_method(\"100%%\", %s)", 1),
                // other format specifiers are parameters as well
                Arguments.of("some_method(%d)", 1),
                Arguments.of("some_method(%1$s, %2$s)", 2),
                // no template at all
                Arguments.of(null, 0)
        );
    }
}
