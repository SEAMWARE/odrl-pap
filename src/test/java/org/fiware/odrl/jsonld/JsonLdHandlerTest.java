package org.fiware.odrl.jsonld;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.ext.ReaderInterceptorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@QuarkusTest
public class JsonLdHandlerTest {

    @Inject
    private JsonLdHandler jsonLdHandler;

    @Inject
    private ObjectMapper objectMapper;


    @ParameterizedTest
    @MethodSource("getJsonPairs")
    public void testCompaction(String policy, String expectedPolicyPath) throws Exception {
        ReaderInterceptorContext crc = mock(ReaderInterceptorContext.class);
        InputStream policyStream = this.getClass().getResourceAsStream(policy);
        String compactedPolicyString = jsonLdHandler.handleJsonLd(policyStream);
        InputStream expectedPolicyStream = this.getClass().getResourceAsStream(expectedPolicyPath);

        Map<String, Object> expectedPolicy = objectMapper.readValue(expectedPolicyStream, new TypeReference<Map<String, Object>>() {
        });
        Map<String, Object> compactedPolicy = objectMapper.readValue(compactedPolicyString, new TypeReference<Map<String, Object>>() {
        });
        assertEquals(expectedPolicy, compactedPolicy, "The policy should have been compacted properly.");
    }

    @ParameterizedTest
    @MethodSource("getAdditionalContextCases")
    public void testCompactionWithAdditionalContexts(String description,
                                                     List<Map<String, Object>> additionalContexts,
                                                     String newPrefix,
                                                     String replacedPrefix) throws Exception {
        InputStream policyStream = this.getClass().getResourceAsStream(
                "/examples/odrl/3000/_3000-original.json");
        String compactedJson = jsonLdHandler.handleJsonLd(policyStream, additionalContexts);
        Map<String, Object> compacted = objectMapper.readValue(
                compactedJson, new TypeReference<>() {});

        @SuppressWarnings("unchecked")
        Map<String, Object> context = (Map<String, Object>) compacted.get("@context");
        assertTrue(context.containsKey(newPrefix),
                String.format("[%s] @context should contain new prefix '%s': %s",
                        description, newPrefix, context));
        assertFalse(context.containsKey(replacedPrefix),
                String.format("[%s] @context should not contain replaced prefix '%s': %s",
                        description, replacedPrefix, context));

        String typeValue = (String) compacted.get("@type");
        assertTrue(typeValue.startsWith(newPrefix + ":"),
                String.format("[%s] @type should use new prefix '%s:' but was: %s",
                        description, newPrefix, typeValue));
    }

    @Test
    public void testCompactionWithNullAdditionalContexts() throws Exception {
        InputStream policyStream = this.getClass().getResourceAsStream(
                "/examples/odrl/3000/_3000-original.json");
        String withNull = jsonLdHandler.handleJsonLd(policyStream, null);

        policyStream = this.getClass().getResourceAsStream(
                "/examples/odrl/3000/_3000-original.json");
        String withoutParam = jsonLdHandler.handleJsonLd(policyStream);

        Map<String, Object> withNullMap = objectMapper.readValue(
                withNull, new TypeReference<>() {});
        Map<String, Object> withoutParamMap = objectMapper.readValue(
                withoutParam, new TypeReference<>() {});
        assertEquals(withoutParamMap, withNullMap,
                "Passing null additional contexts should produce the same result as the no-arg overload.");
    }

    @Test
    public void testCompactionWithEmptyAdditionalContexts() throws Exception {
        InputStream policyStream = this.getClass().getResourceAsStream(
                "/examples/odrl/3000/_3000-original.json");
        String withEmpty = jsonLdHandler.handleJsonLd(policyStream, List.of());

        policyStream = this.getClass().getResourceAsStream(
                "/examples/odrl/3000/_3000-original.json");
        String withoutParam = jsonLdHandler.handleJsonLd(policyStream);

        Map<String, Object> withEmptyMap = objectMapper.readValue(
                withEmpty, new TypeReference<>() {});
        Map<String, Object> withoutParamMap = objectMapper.readValue(
                withoutParam, new TypeReference<>() {});
        assertEquals(withoutParamMap, withEmptyMap,
                "Passing empty additional contexts should produce the same result as the no-arg overload.");
    }

    @Test
    public void testAdditionalContextAddsNewPrefix() throws Exception {
        Map<String, Object> customContext = Map.of(
                "custom", Map.of("@id", "https://example.org/custom#", "@prefix", true));

        InputStream policyStream = this.getClass().getResourceAsStream(
                "/examples/odrl/3000/_3000-original.json");
        String compactedJson = jsonLdHandler.handleJsonLd(policyStream, List.of(customContext));

        assertTrue(compactedJson.contains("odrl:"),
                "Base odrl: prefix should still be present when adding a non-overlapping context.");
    }

    @Test
    public void testScopedContextRemapsActionOnly() throws Exception {
        Map<String, Object> scopedContext = new HashMap<>();
        scopedContext.put("odrl", null);
        scopedContext.put("mcp", Map.of("@id", "http://www.w3.org/ns/odrl/2/", "@prefix", true));

        Map<String, Object> scopedActionContext = Map.of(
                "odrl:action", Map.of(
                        "@id", "http://www.w3.org/ns/odrl/2/action",
                        "@type", "@id",
                        "@context", scopedContext
                )
        );

        InputStream policyStream = this.getClass().getResourceAsStream(
                "/examples/odrl/3000/_3000-original.json");
        String compactedJson = jsonLdHandler.handleJsonLd(policyStream, List.of(scopedActionContext));
        Map<String, Object> compacted = objectMapper.readValue(
                compactedJson, new TypeReference<>() {});

        assertTrue(compactedJson.contains("\"odrl:permission\""),
                "Structure keys should still use odrl: prefix");
        assertTrue(compactedJson.contains("\"odrl:constraint\""),
                "Structure keys should still use odrl: prefix");

        @SuppressWarnings("unchecked")
        Map<String, Object> permission = (Map<String, Object>) compacted.get("odrl:permission");
        Object actionValue = permission.get("odrl:action");

        String actionId;
        if (actionValue instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> actionMap = (Map<String, Object>) actionValue;
            actionId = (String) actionMap.get("@id");
        } else {
            actionId = (String) actionValue;
        }

        assertEquals("mcp:read", actionId,
                "Action @id should be remapped to mcp: prefix via scoped context");
    }

    /**
     * Provides test cases for additional context compaction (full namespace replacement).
     * Each case: description, additionalContexts list, new prefix, replaced prefix.
     */
    public static Stream<Arguments> getAdditionalContextCases() {
        Map<String, Object> mcpOdrlContext = Map.of(
                "mcp", Map.of("@id", "http://www.w3.org/ns/odrl/2/", "@prefix", true));

        Map<String, Object> dcpOdrlContext = Map.of(
                "dcp", Map.of("@id", "http://www.w3.org/ns/odrl/2/", "@prefix", true));

        return Stream.of(
                Arguments.of(
                        "Replace odrl: with mcp:",
                        List.of(mcpOdrlContext),
                        "mcp",
                        "odrl"),
                Arguments.of(
                        "Replace odrl: with dcp:",
                        List.of(dcpOdrlContext),
                        "dcp",
                        "odrl")
        );
    }

    public static Stream<Arguments> getJsonPairs() {
        return Stream.of(
                Arguments.of("/examples/edc/edc.json", "/examples/edc/compacted-edc.json"),
                Arguments.of("/examples/ngsi-ld/types/expanded-types.json", "/examples/ngsi-ld/types/types.json"),
                Arguments.of("/examples/dome/1001/_1001-original.json", "/examples/dome/1001/_1001.json"),
                Arguments.of("/examples/dome/1001-2/_1001-2-expanded.json", "/examples/dome/1001-2/_1001-2.json"),
                Arguments.of("/examples/dome/1002/_1002-original.json", "/examples/dome/1002/_1002.json"),
                Arguments.of("/examples/dome/1003/_1003-original.json", "/examples/dome/1003/_1003.json"),
                Arguments.of("/examples/dome/1004/1004-original.json", "/examples/dome/1004/1004.json"),
                Arguments.of("/examples/dome/1005/_1005-original.json", "/examples/dome/1005/_1005.json"),
                Arguments.of("/examples/dome/2001/_2001-original.json", "/examples/dome/2001/_2001.json"),
                Arguments.of("/examples/dome/2001-2/_2001-2-original.json", "/examples/dome/2001-2/_2001-2.json"),
                Arguments.of("/examples/dome/2001-3/_2001-3-original.json", "/examples/dome/2001-3/_2001-3.json"),
                Arguments.of("/examples/dome/2002/_2002-original.json", "/examples/dome/2002/_2002.json"),
                Arguments.of("/examples/dome/2003/_2003-original.json", "/examples/dome/2003/_2003.json"),
                Arguments.of("/examples/dome/6600/_6600-original.json", "/examples/dome/6600/_6600.json"),
                Arguments.of("/examples/dome/6700/_6700-original.json", "/examples/dome/6700/_6700.json"),
                Arguments.of("/examples/dome/6800/_6800-original.json", "/examples/dome/6800/_6800.json"),
                Arguments.of("/examples/gaia-x/ovc-constraint-original.json", "/examples/gaia-x/ovc-constraint.json"),
                Arguments.of("/examples/odrl/3000/_3000-original.json", "/examples/odrl/3000/_3000.json")
        );
    }

}
