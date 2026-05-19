package org.fiware.odrl.jsonld;

import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.loader.DocumentLoader;
import com.apicatalog.jsonld.loader.DocumentLoaderOptions;
import com.apicatalog.jsonld.loader.HttpLoader;
import com.apicatalog.jsonld.loader.SchemeRouter;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.fiware.odrl.JsonLdCacheConfiguration;

import java.net.URI;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class CachingContextRepository implements ContextRepository {

    @Inject
    CloseableHttpClient httpClient;

    @Inject
    JsonLdCacheConfiguration cacheConfiguration;

    @Inject
    @Named("jsonld-context-cache")
    Cache<String, Document> cache;

    private DocumentLoader httpLoader;

    @PostConstruct
    void init() {
        HttpLoader loader = new HttpLoader(new JsonLdApacheHttpClient(httpClient));
        httpLoader = new SchemeRouter()
                .set("https", loader)
                .set("http", loader)
                .set("file", loader);
        if (!cacheConfiguration.enabled()) {
            log.debug("JSON-LD context cache is disabled");
        }
    }

    @Override
    public Optional<Document> find(URI url) throws JsonLdError {
        if (!cacheConfiguration.enabled()) {
            return Optional.of(httpLoader.loadDocument(url, new DocumentLoaderOptions()));
        }
        String key = url.toString();
        Document cached = cache.getIfPresent(key);
        if (cached != null) {
            log.debug("Cache hit for JSON-LD context: {}", url);
            return Optional.of(cached);
        }
        log.debug("Cache miss for JSON-LD context: {}, fetching via HTTP", url);
        Document doc = httpLoader.loadDocument(url, new DocumentLoaderOptions());
        cache.put(key, doc);
        return Optional.of(doc);
    }
}
