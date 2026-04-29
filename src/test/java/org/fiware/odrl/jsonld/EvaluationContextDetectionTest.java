package org.fiware.odrl.jsonld;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link JsonLdHandler#detectEvaluationContext(String)}.
 *
 * <p>Verifies that the detection logic correctly identifies the evaluation
 * context (HTTP or JSON) based on the compacted policy's content — either
 * from an explicit {@code pap:evaluationContext} field or from the presence
 * of {@code json:}-prefixed terms.</p>
 *
 * <p>This is a plain JUnit test (no Quarkus / TestContainers) because the
 * detection method only depends on an {@link ObjectMapper}.</p>
 */
public class EvaluationContextDetectionTest {

    private JsonLdHandler jsonLdHandler;

    /**
     * Creates a minimal {@link JsonLdHandler} with only the
     * {@link ObjectMapper} injected (via reflection), which is all
     * {@code detectEvaluationContext} needs.
     */
    @BeforeEach
    void setUp() throws Exception {
        jsonLdHandler = new JsonLdHandler();
        Field objectMapperField = JsonLdHandler.class.getDeclaredField("objectMapper");
        objectMapperField.setAccessible(true);
        objectMapperField.set(jsonLdHandler, new ObjectMapper());
    }

    /**
     * Tests that {@code detectEvaluationContext} returns the expected
     * {@link EvaluationContext} for a range of compacted policy JSON strings.
     *
     * @param description   human-readable test-case description
     * @param compactedJson the compacted JSON-LD string to analyse
     * @param expected      the expected evaluation context
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("evaluationContextCases")
    @DisplayName("detectEvaluationContext should route correctly")
    public void testDetectEvaluationContext(String description,
                                            String compactedJson,
                                            EvaluationContext expected) {
        EvaluationContext actual = jsonLdHandler.detectEvaluationContext(compactedJson);
        assertEquals(expected, actual, description);
    }

    /**
     * Provides parameterized test cases covering explicit {@code pap:evaluationContext},
     * {@code json:} namespace scanning, {@code @graph} structures, and default (HTTP)
     * fallback.
     */
    public static Stream<Arguments> evaluationContextCases() {
        return Stream.of(
                // --- Explicit pap:evaluationContext field ---
                Arguments.of(
                        "Explicit pap:evaluationContext = json at top level",
                        """
                        {
                          "@type": "odrl:Set",
                          "pap:evaluationContext": "json",
                          "odrl:permission": []
                        }
                        """,
                        EvaluationContext.JSON
                ),
                Arguments.of(
                        "Explicit pap:evaluationContext = json (case-insensitive)",
                        """
                        {
                          "@type": "odrl:Set",
                          "pap:evaluationContext": "JSON",
                          "odrl:permission": []
                        }
                        """,
                        EvaluationContext.JSON
                ),
                Arguments.of(
                        "Explicit pap:evaluationContext = http should yield HTTP context",
                        """
                        {
                          "@type": "odrl:Set",
                          "pap:evaluationContext": "http",
                          "odrl:permission": []
                        }
                        """,
                        EvaluationContext.HTTP
                ),
                // --- pap:evaluationContext inside @graph ---
                Arguments.of(
                        "pap:evaluationContext in @graph entry",
                        """
                        {
                          "@graph": [
                            {
                              "@type": "odrl:Set",
                              "pap:evaluationContext": "json",
                              "odrl:permission": []
                            }
                          ]
                        }
                        """,
                        EvaluationContext.JSON
                ),
                Arguments.of(
                        "pap:evaluationContext = http in @graph should yield HTTP",
                        """
                        {
                          "@graph": [
                            {
                              "@type": "odrl:Set",
                              "pap:evaluationContext": "http",
                              "odrl:permission": []
                            }
                          ]
                        }
                        """,
                        EvaluationContext.HTTP
                ),
                // --- json: namespace detection ---
                Arguments.of(
                        "json: action prefix triggers JSON context",
                        """
                        {
                          "@type": "odrl:Set",
                          "odrl:permission": [{
                            "odrl:action": { "@id": "json:read" },
                            "odrl:target": "urn:example:resource"
                          }]
                        }
                        """,
                        EvaluationContext.JSON
                ),
                Arguments.of(
                        "json: leftOperand prefix triggers JSON context",
                        """
                        {
                          "@type": "odrl:Set",
                          "odrl:permission": [{
                            "odrl:action": { "@id": "odrl:use" },
                            "odrl:constraint": {
                              "odrl:leftOperand": "json:payloadValue",
                              "odrl:operator": "odrl:eq",
                              "odrl:rightOperand": "someValue"
                            }
                          }]
                        }
                        """,
                        EvaluationContext.JSON
                ),
                // --- Default HTTP context ---
                Arguments.of(
                        "Standard ODRL policy without json: namespace yields HTTP",
                        """
                        {
                          "@type": "odrl:Set",
                          "odrl:uid": "urn:example:policy:1",
                          "odrl:permission": [{
                            "odrl:action": { "@id": "odrl:read" },
                            "odrl:target": "urn:example:api"
                          }]
                        }
                        """,
                        EvaluationContext.HTTP
                ),
                Arguments.of(
                        "Empty JSON object yields HTTP (default)",
                        "{}",
                        EvaluationContext.HTTP
                ),
                Arguments.of(
                        "DOME policy without json namespace yields HTTP",
                        """
                        {
                          "@type": "odrl:Set",
                          "odrl:uid": "urn:dome:policy:1",
                          "odrl:permission": [{
                            "odrl:action": { "@id": "dome-op:use" },
                            "odrl:target": "urn:dome:offering:1",
                            "odrl:assignee": "did:example:org"
                          }]
                        }
                        """,
                        EvaluationContext.HTTP
                ),
                // --- Edge cases ---
                Arguments.of(
                        "Malformed JSON falls back to text scan (no json: prefix => HTTP)",
                        "this is not valid json at all",
                        EvaluationContext.HTTP
                ),
                Arguments.of(
                        "Malformed JSON with json: in text triggers JSON context via regex",
                        "this has \"json:read\" in it",
                        EvaluationContext.JSON
                )
        );
    }
}
