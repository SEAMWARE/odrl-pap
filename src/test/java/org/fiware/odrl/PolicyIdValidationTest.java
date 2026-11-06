package org.fiware.odrl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The policy id becomes the rego package of the policy and its import alias in the main policy,
 * so only ids OPA can parse there are accepted.
 */
public class PolicyIdValidationTest {

    @ParameterizedTest
    @ValueSource(strings = {"_policy", "EnergyReportRead", "energy_report_read", "policy1", "rego", "cgpeofeevp"})
    public void testValidPolicyIds(String id) {
        assertTrue(ApiResource.isValidPolicyId(id), "A valid rego identifier should be accepted as policy id.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"energy-report-read", "3f2a9c1e-7b4d-4e8a-9c2f-1a2b3c4d5e6f", "1policy", "my.policy",
            "urn:policy:1", "https://mp-operation.org/policy/common/type", "my policy", "",
            "as", "contains", "default", "else", "every", "false", "if", "import", "in", "not", "null",
            "package", "some", "true", "with", "data", "input"})
    public void testInvalidPolicyIds(String id) {
        assertFalse(ApiResource.isValidPolicyId(id), "An id that breaks the generated rego should be rejected.");
    }
}
