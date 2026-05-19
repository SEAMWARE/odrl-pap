package org.fiware.odrl.jsonld;

import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.JsonLdErrorCode;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.loader.DocumentLoader;
import com.apicatalog.jsonld.loader.DocumentLoaderOptions;

import java.net.URI;
import java.util.List;
import java.util.Optional;

public class CompositeDocumentLoader implements DocumentLoader {

    private final List<ContextRepository> repositories;

    public CompositeDocumentLoader(List<ContextRepository> repositories) {
        this.repositories = repositories;
    }

    @Override
    public Document loadDocument(URI url, DocumentLoaderOptions options) throws JsonLdError {
        for (ContextRepository repository : repositories) {
            Optional<Document> doc = repository.find(url);
            if (doc.isPresent()) {
                return doc.get();
            }
        }
        throw new JsonLdError(JsonLdErrorCode.LOADING_DOCUMENT_FAILED, url.toString());
    }
}
