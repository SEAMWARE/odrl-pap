package org.fiware.odrl.jsonld;

import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.loader.DocumentLoader;
import com.apicatalog.jsonld.loader.DocumentLoaderOptions;

import java.net.URI;
import java.util.List;
import java.util.Optional;

public class CompositeDocumentLoader implements DocumentLoader {

    private final List<ContextRepository> repositories;
    private final DocumentLoader httpFallback;

    public CompositeDocumentLoader(List<ContextRepository> repositories, DocumentLoader httpFallback) {
        this.repositories = repositories;
        this.httpFallback = httpFallback;
    }

    @Override
    public Document loadDocument(URI url, DocumentLoaderOptions options) throws JsonLdError {
        for (ContextRepository repository : repositories) {
            Optional<Document> doc = repository.find(url);
            if (doc.isPresent()) {
                return doc.get();
            }
        }
        return httpFallback.loadDocument(url, options);
    }
}
