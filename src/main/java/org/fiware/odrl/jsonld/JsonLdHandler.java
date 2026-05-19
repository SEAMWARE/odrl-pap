package org.fiware.odrl.jsonld;

import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.JsonLdOptions;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.JsonDocument;
import com.apicatalog.jsonld.loader.DocumentLoader;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@ApplicationScoped
public class JsonLdHandler {

    @Inject
    private DocumentLoader documentLoader;

    @Inject
    private CompactionContext compactionContext;

    public String handleJsonLd(InputStream jsonLdInput) throws JsonLdError {
        JsonLdOptions jsonLdOptions = new JsonLdOptions(documentLoader);
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

}
