package org.fiware.odrl.jsonld;

import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.JsonDocument;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

public class LocalContextRepository implements ContextRepository {

    // Keys are normalized: no scheme, no trailing slash
    private static final Map<String, String> LOCAL_CONTEXTS = Map.of(
            "www.w3.org/ns/odrl/2", "/jsonld/odrl-context.jsonld"
    );

    @Override
    public Optional<Document> find(URI url) throws JsonLdError {
        String localPath = LOCAL_CONTEXTS.get(normalize(url));
        if (localPath == null) {
            return Optional.empty();
        }
        InputStream stream = getClass().getResourceAsStream(localPath);
        if (stream == null) {
            return Optional.empty();
        }
        return Optional.of(JsonDocument.of(stream));
    }

    private static String normalize(URI url) {
        // only strip scheme and trailing slash
        String normalized = url.getHost() + url.getPath();
        if (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }
}
