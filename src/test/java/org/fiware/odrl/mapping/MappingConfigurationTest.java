package org.fiware.odrl.mapping;

import org.fiware.odrl.rego.RegoMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link MappingConfiguration#deepMerge(MappingConfiguration)}, ensuring that an additional
 * mapping (e.g. one provided via {@code paths.mapping}) extends the built-in mapping instead of
 * replacing whole attributes.
 */
public class MappingConfigurationTest {

    private static final RegoMethod NGSI_LD_ENTITY_TYPE =
            new RegoMethod("ngsild.leftOperand as ngsild_lo", "ngsild_lo.entity_type(helper.http_part)");
    private static final RegoMethod VC_ROLE =
            new RegoMethod("vc.leftOperand as vc_lo", "vc_lo.role(helper.verifiable_credential)");
    private static final RegoMethod CONSENT_HAS_VALID_CONSENT =
            new RegoMethod("consent.leftOperand as consent_lo", "consent_lo.has_valid_consent(helper.verifiable_credential)");

    /**
     * Builds a mapping configuration for a single attribute from a {@code namespace -> (key -> method)} map.
     */
    private static MappingConfiguration mapping(OdrlAttribute attribute, Map<String, Map<String, RegoMethod>> namespaces) {
        MappingConfiguration configuration = new MappingConfiguration();
        NamespacedMap namespacedMap = new NamespacedMap();
        namespaces.forEach((namespace, operands) -> {
            RegoMap regoMap = new RegoMap();
            regoMap.putAll(operands);
            namespacedMap.put(namespace, regoMap);
        });
        configuration.put(attribute, namespacedMap);
        return configuration;
    }

    @Test
    @DisplayName("Merging an additional namespace keeps the existing namespaces of the same attribute.")
    public void deepMerge_addsNamespaceWithoutDroppingDefaults() {
        MappingConfiguration defaults = mapping(OdrlAttribute.LEFT_OPERAND, Map.of(
                "ngsi-ld", Map.of("entityType", NGSI_LD_ENTITY_TYPE),
                "vc", Map.of("role", VC_ROLE)));
        MappingConfiguration additional = mapping(OdrlAttribute.LEFT_OPERAND, Map.of(
                "consent", Map.of("hasValidConsent", CONSENT_HAS_VALID_CONSENT)));

        defaults.deepMerge(additional);

        assertEquals(Set.of("ngsi-ld:entityType", "vc:role", "consent:hasValidConsent"),
                defaults.getKeys(OdrlAttribute.LEFT_OPERAND),
                "The additional namespace must be added while all default namespaces are kept.");
    }

    @Test
    @DisplayName("Merging an operand into an existing namespace keeps the other operands of that namespace.")
    public void deepMerge_addsOperandToExistingNamespace() {
        MappingConfiguration defaults = mapping(OdrlAttribute.LEFT_OPERAND, Map.of(
                "ngsi-ld", Map.of("entityType", NGSI_LD_ENTITY_TYPE)));
        MappingConfiguration additional = mapping(OdrlAttribute.LEFT_OPERAND, Map.of(
                "ngsi-ld", Map.of("id", new RegoMethod("ngsild.leftOperand as ngsild_lo", "ngsild_lo.id(helper.http_part)"))));

        defaults.deepMerge(additional);

        assertEquals(Set.of("ngsi-ld:entityType", "ngsi-ld:id"),
                defaults.getKeys(OdrlAttribute.LEFT_OPERAND),
                "The additional operand must be added alongside the existing one in the same namespace.");
    }

    @Test
    @DisplayName("An operand from the additional mapping overrides the same namespace:key of the default.")
    public void deepMerge_overridesExistingOperand() {
        RegoMethod overriding = new RegoMethod("ngsild.leftOperand as ngsild_lo", "ngsild_lo.entity_type_v2(helper.http_part)");
        MappingConfiguration defaults = mapping(OdrlAttribute.LEFT_OPERAND, Map.of(
                "ngsi-ld", Map.of("entityType", NGSI_LD_ENTITY_TYPE)));
        MappingConfiguration additional = mapping(OdrlAttribute.LEFT_OPERAND, Map.of(
                "ngsi-ld", Map.of("entityType", overriding)));

        defaults.deepMerge(additional);

        assertEquals(overriding,
                defaults.get(OdrlAttribute.LEFT_OPERAND).get("ngsi-ld").get("entityType"),
                "The additional operand must override the default with the same namespace:key.");
    }

    @Test
    @DisplayName("Merging a new attribute adds it without touching the existing attributes.")
    public void deepMerge_addsNewAttribute() {
        MappingConfiguration defaults = mapping(OdrlAttribute.LEFT_OPERAND, Map.of(
                "ngsi-ld", Map.of("entityType", NGSI_LD_ENTITY_TYPE)));
        MappingConfiguration additional = mapping(OdrlAttribute.OPERATOR, Map.of(
                "odrl", Map.of("eq", new RegoMethod("odrl.operator as odrl_operator", "odrl_operator.eq_operator"))));

        defaults.deepMerge(additional);

        assertEquals(Set.of("ngsi-ld:entityType"), defaults.getKeys(OdrlAttribute.LEFT_OPERAND),
                "The existing attribute must be untouched.");
        assertEquals(Set.of("odrl:eq"), defaults.getKeys(OdrlAttribute.OPERATOR),
                "The new attribute must be added.");
    }

    @Test
    @DisplayName("Merging null leaves the configuration unchanged.")
    public void deepMerge_nullIsNoOp() {
        MappingConfiguration defaults = mapping(OdrlAttribute.LEFT_OPERAND, Map.of(
                "ngsi-ld", Map.of("entityType", NGSI_LD_ENTITY_TYPE)));

        defaults.deepMerge(null);

        assertEquals(Set.of("ngsi-ld:entityType"), defaults.getKeys(OdrlAttribute.LEFT_OPERAND));
    }
}
