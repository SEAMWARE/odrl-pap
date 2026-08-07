package org.fiware.odrl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openapi.quarkus.odrl_yaml.model.TemplateCreate;
import org.openapi.quarkus.odrl_yaml.model.TemplatePlaceholder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TemplateResource} validation and placeholder extraction logic.
 *
 * <p>Tests cover:</p>
 * <ul>
 *   <li>Placeholder key extraction from text</li>
 *   <li>Validation of template creation payloads (duplicate keys, orphaned keys, missing placeholders)</li>
 *   <li>Edge cases: null/blank keys, empty placeholder lists, nested ODRL maps</li>
 * </ul>
 */
public class TemplateResourceTest {

    // -------------------------------------------------------------------------
    // Placeholder extraction tests
    // -------------------------------------------------------------------------

    @DisplayName("Placeholder keys should be extracted from text containing {{KEY}} tokens")
    @ParameterizedTest
    @MethodSource("textsWithPlaceholders")
    void testExtractPlaceholderKeys(String text, Set<String> expectedKeys) {
        Set<String> actual = TemplateResource.extractPlaceholderKeys(text);
        assertEquals(expectedKeys, actual, "Extracted keys should match expected set");
    }

    static Stream<Arguments> textsWithPlaceholders() {
        return Stream.of(
                Arguments.of("Allow access to {{RESOURCE_ID}}", Set.of("RESOURCE_ID")),
                Arguments.of("{{ROLE}} can read {{RESOURCE}}", Set.of("ROLE", "RESOURCE")),
                Arguments.of("No placeholders here", Set.of()),
                Arguments.of("{{A}} and {{A}} duplicated", Set.of("A")),
                Arguments.of("{{MULTI_WORD_KEY_123}}", Set.of("MULTI_WORD_KEY_123")),
                Arguments.of(null, Set.of()),
                Arguments.of("", Set.of()),
                Arguments.of("{{_UNDERSCORE_START}}", Set.of("_UNDERSCORE_START"))
        );
    }

    @Test
    @DisplayName("Lowercase placeholder tokens should not be extracted")
    void testExtractPlaceholderKeys_lowercaseIgnored() {
        Set<String> keys = TemplateResource.extractPlaceholderKeys("{{lowercase}} and {{UPPER}}");
        assertEquals(Set.of("UPPER"), keys, "Only uppercase keys should be extracted");
    }

    // -------------------------------------------------------------------------
    // Template validation tests — valid payloads
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("A valid template with all placeholders referenced should pass validation")
    void testValidateTemplate_valid() {
        TemplateCreate tc = createValidTemplate();
        List<String> errors = TemplateResource.validateTemplate(tc);
        assertTrue(errors.isEmpty(), "Valid template should produce no errors, got: " + errors);
    }

    @Test
    @DisplayName("A valid template with placeholder only in natural language should pass")
    void testValidateTemplate_placeholderInNaturalLanguageOnly() {
        TemplateCreate tc = new TemplateCreate();
        tc.setName("Test");
        tc.setDescription("Test template");
        tc.setOdrl(Map.of("@type", "odrl:Set"));
        tc.setNaturalLanguage("Allow access to {{RESOURCE_ID}}");
        tc.setPlaceholders(List.of(createPlaceholder("RESOURCE_ID", "Resource", "string")));

        List<String> errors = TemplateResource.validateTemplate(tc);
        assertTrue(errors.isEmpty(), "Placeholder in natural language only should be valid, got: " + errors);
    }

    @Test
    @DisplayName("A valid template with placeholder only in ODRL should pass")
    void testValidateTemplate_placeholderInOdrlOnly() {
        TemplateCreate tc = new TemplateCreate();
        tc.setName("Test");
        tc.setDescription("Test template");
        tc.setOdrl(Map.of("odrl:target", "{{RESOURCE_ID}}"));
        tc.setNaturalLanguage("Some policy");
        tc.setPlaceholders(List.of(createPlaceholder("RESOURCE_ID", "Resource", "string")));

        List<String> errors = TemplateResource.validateTemplate(tc);
        assertTrue(errors.isEmpty(), "Placeholder in ODRL only should be valid, got: " + errors);
    }

    // -------------------------------------------------------------------------
    // Template validation tests — invalid payloads
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Null placeholder list should fail validation")
    void testValidateTemplate_nullPlaceholders() {
        TemplateCreate tc = new TemplateCreate();
        tc.setName("Test");
        tc.setPlaceholders(null);

        List<String> errors = TemplateResource.validateTemplate(tc);
        assertFalse(errors.isEmpty(), "Null placeholders should fail validation");
        assertTrue(errors.stream().anyMatch(e -> e.contains("At least one placeholder")),
                "Error should mention missing placeholders");
    }

    @Test
    @DisplayName("Empty placeholder list should fail validation")
    void testValidateTemplate_emptyPlaceholders() {
        TemplateCreate tc = new TemplateCreate();
        tc.setName("Test");
        tc.setPlaceholders(Collections.emptyList());

        List<String> errors = TemplateResource.validateTemplate(tc);
        assertFalse(errors.isEmpty(), "Empty placeholders should fail validation");
        assertTrue(errors.stream().anyMatch(e -> e.contains("At least one placeholder")),
                "Error should mention missing placeholders");
    }

    @Test
    @DisplayName("Duplicate placeholder keys should fail validation")
    void testValidateTemplate_duplicateKeys() {
        TemplateCreate tc = new TemplateCreate();
        tc.setName("Test");
        tc.setOdrl(Map.of("target", "{{RESOURCE_ID}}"));
        tc.setNaturalLanguage("Access {{RESOURCE_ID}}");
        tc.setPlaceholders(List.of(
                createPlaceholder("RESOURCE_ID", "Resource 1", "string"),
                createPlaceholder("RESOURCE_ID", "Resource 2", "string")
        ));

        List<String> errors = TemplateResource.validateTemplate(tc);
        assertFalse(errors.isEmpty(), "Duplicate keys should fail validation");
        assertTrue(errors.stream().anyMatch(e -> e.contains("Duplicate placeholder key")),
                "Error should mention duplicate key");
    }

    @Test
    @DisplayName("Orphaned placeholder key (not in ODRL or natural language) should fail validation")
    void testValidateTemplate_orphanedKey() {
        TemplateCreate tc = new TemplateCreate();
        tc.setName("Test");
        tc.setOdrl(Map.of("target", "some-value"));
        tc.setNaturalLanguage("No placeholders here");
        tc.setPlaceholders(List.of(createPlaceholder("MISSING_REF", "Missing", "string")));

        List<String> errors = TemplateResource.validateTemplate(tc);
        assertFalse(errors.isEmpty(), "Orphaned key should fail validation");
        assertTrue(errors.stream().anyMatch(e -> e.contains("MISSING_REF") && e.contains("not referenced")),
                "Error should mention the unreferenced key");
    }

    @Test
    @DisplayName("Blank placeholder key should fail validation")
    void testValidateTemplate_blankKey() {
        TemplateCreate tc = new TemplateCreate();
        tc.setName("Test");
        tc.setOdrl(Map.of("target", "value"));
        tc.setPlaceholders(List.of(createPlaceholder("", "Empty Key", "string")));

        List<String> errors = TemplateResource.validateTemplate(tc);
        assertFalse(errors.isEmpty(), "Blank key should fail validation");
        assertTrue(errors.stream().anyMatch(e -> e.contains("null or blank")),
                "Error should mention null or blank key");
    }

    @Test
    @DisplayName("Null placeholder key should fail validation")
    void testValidateTemplate_nullKey() {
        TemplateCreate tc = new TemplateCreate();
        tc.setName("Test");
        tc.setOdrl(Map.of("target", "value"));
        tc.setPlaceholders(List.of(createPlaceholder(null, "Null Key", "string")));

        List<String> errors = TemplateResource.validateTemplate(tc);
        assertFalse(errors.isEmpty(), "Null key should fail validation");
        assertTrue(errors.stream().anyMatch(e -> e.contains("null or blank")),
                "Error should mention null or blank key");
    }

    @Test
    @DisplayName("Multiple validation errors should all be reported")
    void testValidateTemplate_multipleErrors() {
        TemplateCreate tc = new TemplateCreate();
        tc.setName("Test");
        tc.setOdrl(Map.of("target", "no-placeholders-here"));
        tc.setNaturalLanguage("no placeholders");
        tc.setPlaceholders(List.of(
                createPlaceholder("ORPHAN_A", "First orphan", "string"),
                createPlaceholder("ORPHAN_A", "Duplicate orphan", "string"),
                createPlaceholder("ORPHAN_B", "Second orphan", "number")
        ));

        List<String> errors = TemplateResource.validateTemplate(tc);
        assertTrue(errors.size() >= 3,
                "Should report duplicate + at least 2 orphaned keys, got: " + errors);
    }

    // -------------------------------------------------------------------------
    // Template validation tests — ODRL edge cases
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Placeholder in nested ODRL map value should be found")
    void testValidateTemplate_nestedOdrlPlaceholder() {
        Map<String, Object> nestedOdrl = Map.of(
                "@type", "odrl:Set",
                "odrl:permission", Map.of(
                        "odrl:target", "{{RESOURCE_ID}}",
                        "odrl:action", "odrl:read"
                )
        );

        TemplateCreate tc = new TemplateCreate();
        tc.setName("Nested test");
        tc.setOdrl(nestedOdrl);
        tc.setNaturalLanguage("Access {{RESOURCE_ID}}");
        tc.setPlaceholders(List.of(createPlaceholder("RESOURCE_ID", "Resource", "string")));

        List<String> errors = TemplateResource.validateTemplate(tc);
        assertTrue(errors.isEmpty(),
                "Placeholder in nested ODRL should be found, got: " + errors);
    }

    @Test
    @DisplayName("Null ODRL with placeholder only in natural language should pass")
    void testValidateTemplate_nullOdrl() {
        TemplateCreate tc = new TemplateCreate();
        tc.setName("Test");
        tc.setOdrl(null);
        tc.setNaturalLanguage("Allow {{ROLE}} to read");
        tc.setPlaceholders(List.of(createPlaceholder("ROLE", "Role", "string")));

        List<String> errors = TemplateResource.validateTemplate(tc);
        assertTrue(errors.isEmpty(),
                "Null ODRL with placeholder in natural language should be valid, got: " + errors);
    }

    @Test
    @DisplayName("Null natural language with placeholder only in ODRL should pass")
    void testValidateTemplate_nullNaturalLanguage() {
        TemplateCreate tc = new TemplateCreate();
        tc.setName("Test");
        tc.setOdrl(Map.of("odrl:target", "{{TARGET_ID}}"));
        tc.setNaturalLanguage(null);
        tc.setPlaceholders(List.of(createPlaceholder("TARGET_ID", "Target", "string")));

        List<String> errors = TemplateResource.validateTemplate(tc);
        assertTrue(errors.isEmpty(),
                "Null natural language with placeholder in ODRL should be valid, got: " + errors);
    }

    // -------------------------------------------------------------------------
    // PLACEHOLDER_PATTERN tests
    // -------------------------------------------------------------------------

    @DisplayName("PLACEHOLDER_PATTERN should match valid placeholder tokens")
    @ParameterizedTest
    @ValueSource(strings = {"{{A}}", "{{ABC}}", "{{A_B}}", "{{_A}}", "{{A1}}", "{{AB_CD_123}}"})
    void testPlaceholderPattern_matches(String token) {
        assertTrue(TemplateResource.PLACEHOLDER_PATTERN.matcher(token).find(),
                "Pattern should match: " + token);
    }

    @DisplayName("PLACEHOLDER_PATTERN should not match invalid placeholder tokens")
    @ParameterizedTest
    @ValueSource(strings = {"{{1ABC}}", "{{abc}}", "{SINGLE}", "{{a_b}}", "plain text", "{{}}"})
    void testPlaceholderPattern_noMatch(String token) {
        assertFalse(TemplateResource.PLACEHOLDER_PATTERN.matcher(token).find(),
                "Pattern should not match: " + token);
    }

    // -------------------------------------------------------------------------
    // Helper methods
    // -------------------------------------------------------------------------

    /**
     * Creates a valid {@link TemplateCreate} payload with one placeholder referenced
     * in both the ODRL JSON and natural language text.
     */
    private static TemplateCreate createValidTemplate() {
        TemplateCreate tc = new TemplateCreate();
        tc.setName("Test Template");
        tc.setDescription("A test template for unit testing");
        tc.setOdrl(Map.of(
                "@context", "http://www.w3.org/ns/odrl.jsonld",
                "@type", "odrl:Set",
                "odrl:permission", Map.of(
                        "odrl:target", "{{RESOURCE_ID}}",
                        "odrl:action", "odrl:read"
                )
        ));
        tc.setNaturalLanguage("Allow read access to {{RESOURCE_ID}}");
        tc.setPlaceholders(List.of(
                createPlaceholder("RESOURCE_ID", "Resource Identifier", "string")
        ));
        return tc;
    }

    /**
     * Creates a {@link TemplatePlaceholder} with the specified key, name, and type.
     *
     * @param key  the placeholder key (e.g. "RESOURCE_ID")
     * @param name the display name
     * @param type the type string (e.g. "string", "number")
     * @return a configured placeholder instance
     */
    private static TemplatePlaceholder createPlaceholder(String key, String name, String type) {
        TemplatePlaceholder p = new TemplatePlaceholder();
        p.setKey(key);
        p.setName(name);
        if (type != null) {
            p.setType(TemplatePlaceholder.TypeEnum.fromString(type));
        }
        return p;
    }
}
