package org.fiware.odrl.jsonld;

import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.Document;

import java.net.URI;
import java.util.Optional;

public interface ContextRepository {
    Optional<Document> find(URI url) throws JsonLdError;
}
