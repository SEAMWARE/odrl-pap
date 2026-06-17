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
import com.apicatalog.jsonld.loader.DocumentLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    @Inject
    private DocumentLoader documentLoader;

    @Inject
    private CompactionContext compactionContext;

    @Inject
    private ObjectMapper objectMapper;

    /**
     * JSON-LD context key that identifies the IRI a prefix is mapped to.
     */
    private static final String JSON_LD_ID_KEY = "@id";

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
        return handleJsonLd(jsonLdInput, null);
    }

    /**
     * Expands and then compacts the given JSON-LD input using the configured
     * compaction context merged with any additional contexts. Additional context
     * entries take precedence: if an additional context maps a prefix to an IRI
     * that already exists in the base context under a different prefix, the
     * base prefix is replaced.
     *
     * @param jsonLdInput          the raw JSON-LD policy as an {@link InputStream}
     * @param additionalContexts   optional list of additional JSON-LD context
     *                             objects to merge with the base compaction context;
     *                             may be {@code null} or empty
     * @return the compacted JSON string
     * @throws JsonLdError if expansion or compaction fails
     */
    public String handleJsonLd(InputStream jsonLdInput,
                               List<Map<String, Object>> additionalContexts) throws JsonLdError {
        JsonLdOptions jsonLdOptions = new JsonLdOptions(documentLoader);
        JsonReader jsonReader = Json.createReader(jsonLdInput);

        JsonObject originalJson = jsonReader.readObject();
        Document orginalDocument = JsonDocument.of(originalJson);
        Document expandedDocument = JsonDocument.of(new QuarkusExpansionApi(orginalDocument, jsonLdOptions).get());

        Document effectiveContext = mergeContexts(additionalContexts);
        JsonObject jsonObject = new QuarkusCompactionApi(expandedDocument, effectiveContext, jsonLdOptions).get();
        String jsonString = jsonObject.toString();
        log.debug("Compacted json {}", jsonString);
        return jsonString;
    }

    /**
     * Merges the base compaction context with additional context entries. If an
     * additional context entry maps a prefix to the same IRI as an existing base
     * entry (under a different key), the base entry is removed so that the new
     * prefix is used during compaction.
     *
     * @param additionalContexts additional context objects to merge; may be
     *                           {@code null} or empty
     * @return the effective compaction context document
     * @throws JsonLdError if the merged context cannot be converted to a document
     */
    private Document mergeContexts(List<Map<String, Object>> additionalContexts) throws JsonLdError {
        if (additionalContexts == null || additionalContexts.isEmpty()) {
            return compactionContext.getContext();
        }

        JsonStructure baseStructure = compactionContext.getContext().getJsonContent()
                .orElseThrow(() -> new IllegalStateException("Base compaction context has no JSON content"));
        JsonObject baseJson = baseStructure.asJsonObject();

        Map<String, JsonValue> additionalEntries = new LinkedHashMap<>();
        for (Map<String, Object> ctx : additionalContexts) {
            JsonObject additionalJson = toJakartaJsonObject(ctx);
            additionalJson.forEach(additionalEntries::put);
        }

        Set<String> additionalIris = additionalEntries.values().stream()
                .filter(v -> v.getValueType() == JsonValue.ValueType.OBJECT)
                .map(v -> v.asJsonObject().getString(JSON_LD_ID_KEY, null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        JsonObjectBuilder builder = Json.createObjectBuilder();
        baseJson.forEach((key, value) -> {
            String iri = null;
            if (value.getValueType() == JsonValue.ValueType.OBJECT) {
                iri = value.asJsonObject().getString(JSON_LD_ID_KEY, null);
            }
            if (iri == null || !additionalIris.contains(iri)) {
                builder.add(key, value);
            }
        });
        additionalEntries.forEach(builder::add);

        log.debug("Merged compaction context with {} additional entries", additionalEntries.size());
        return JsonDocument.of(builder.build());
    }

    /**
     * Converts a Jackson-deserialized map to a Jakarta JSON-P {@link JsonObject}.
     *
     * @param map the map to convert
     * @return the corresponding Jakarta JSON object
     */
    private JsonObject toJakartaJsonObject(Map<String, Object> map) {
        try {
            String jsonString = objectMapper.writeValueAsString(map);
            try (JsonReader reader = Json.createReader(new StringReader(jsonString))) {
                return reader.readObject();
            }
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to convert additional context to JSON", e);
        }
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
