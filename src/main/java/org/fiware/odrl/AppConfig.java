package org.fiware.odrl;

import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.JsonDocument;
import com.apicatalog.jsonld.loader.DocumentLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.Produces;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.fiware.odrl.jsonld.CachingContextRepository;
import org.fiware.odrl.jsonld.CompactionContext;
import org.fiware.odrl.jsonld.CompositeDocumentLoader;
import org.fiware.odrl.jsonld.LocalContextRepository;
import org.fiware.odrl.mapping.ConstraintMapper;
import org.fiware.odrl.mapping.LeftOperandMapper;
import org.fiware.odrl.mapping.MappingConfiguration;
import org.fiware.odrl.mapping.OperatorMapper;
import org.fiware.odrl.mapping.RightOperandMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Clock;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/wistefan">Stefan Wiedemann</a>
 */
@Slf4j
@ApplicationScoped
public class AppConfig {

    private static final String DEFAULT_MAPPING_PATH = "mapping.json";
    private static final String DEFAULT_COMPACTION_CONTEXT_PATH = "compaction-context.jsonld";

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private PathsConfiguration pathsConfiguration;

    @Produces
    @ApplicationScoped
    public MappingConfiguration mappingConfiguration() {
        MappingConfiguration mappingConfiguration = new MappingConfiguration();
        InputStream defaultMappingInputStream = this.getClass().getClassLoader().getResourceAsStream(DEFAULT_MAPPING_PATH);
        try {
            mappingConfiguration.deepMerge(objectMapper.readValue(defaultMappingInputStream, MappingConfiguration.class));
        } catch (IOException e) {
            throw new IllegalArgumentException("Was not able to read the default mapping.", e);
        }

        // Merge the additional mapping per namespace so it can add custom operands (e.g. a `consent`
        // leftOperand) without dropping the built-in namespaces of the same attribute. See
        // MappingConfiguration#deepMerge.
        if (pathsConfiguration.mapping().isPresent() && pathsConfiguration.mapping().get().exists()) {
            try {
                mappingConfiguration.deepMerge(objectMapper.readValue(pathsConfiguration.mapping().get(), MappingConfiguration.class));
            } catch (IOException e) {
                log.warn("Was not able to load the additional mappings.", e);
            }
        }
        return mappingConfiguration;
    }

    @Produces
    @ApplicationScoped
    public CompactionContext compactionContext() {
        if (pathsConfiguration.compactionContext().isPresent() && pathsConfiguration.compactionContext().get().exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(pathsConfiguration.compactionContext().get());
                return new CompactionContext(JsonDocument.of(fileInputStream));
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException(String.format("Was not able to find compaction context at %s", pathsConfiguration.compactionContext().get().getAbsolutePath()), e);
            } catch (JsonLdError e) {
                throw new IllegalArgumentException(String.format("Was not able to read the compaction context at %s", pathsConfiguration.compactionContext().get().getAbsolutePath()), e);
            }
        }
        try {
            InputStream defaultCompactionContextInputStream = this.getClass().getClassLoader().getResourceAsStream(DEFAULT_COMPACTION_CONTEXT_PATH);
            return new CompactionContext(JsonDocument.of(defaultCompactionContextInputStream));
        } catch (JsonLdError e) {
            throw new IllegalArgumentException("Was not able to read the default compaction context", e);
        }
    }


    @Produces
    @ApplicationScoped
    public ConstraintMapper constraintMapper(ObjectMapper objectMapper, MappingConfiguration mappingConfiguration) {
        return new ConstraintMapper(objectMapper, mappingConfiguration);
    }

    @Produces
    @ApplicationScoped
    public LeftOperandMapper leftOperandMapper(ObjectMapper objectMapper, MappingConfiguration mappingConfiguration) {
        return new LeftOperandMapper(objectMapper, mappingConfiguration);
    }

    @Produces
    @ApplicationScoped
    public OperatorMapper operatorMapper(ObjectMapper objectMapper, MappingConfiguration mappingConfiguration) {
        return new OperatorMapper(objectMapper, mappingConfiguration);
    }

    @Produces
    @ApplicationScoped
    public RightOperandMapper rightOperandMapper(ObjectMapper objectMapper, MappingConfiguration mappingConfiguration) {
        return new RightOperandMapper(objectMapper, mappingConfiguration);
    }

    @Inject
    private HttpClientConfiguration httpClientConfiguration;

    @Produces
    @ApplicationScoped
    @Named("jsonld-context-cache")
    public Cache<String, Document> jsonLdContextCache(JsonLdCacheConfiguration cacheConfiguration, MeterRegistry meterRegistry) {
        Cache<String, Document> cache = Caffeine.newBuilder()
                .expireAfterWrite(cacheConfiguration.ttlSeconds(), TimeUnit.SECONDS)
                .maximumSize(cacheConfiguration.maxSize())
                .recordStats()
                .removalListener((key, value, cause) ->
                        log.debug("JSON-LD context evicted: key={}, cause={}", key, cause))
                .build();
        CaffeineCacheMetrics.monitor(meterRegistry, cache, "jsonld-context");
        return cache;
    }

    @Produces
    @ApplicationScoped
    public DocumentLoader documentLoader(CachingContextRepository cachingRepo) {
        return new CompositeDocumentLoader(List.of(new LocalContextRepository(), cachingRepo));
    }

    @Produces
    @ApplicationScoped
    public CloseableHttpClient httpClient() {
        int timeout = httpClientConfiguration.timeoutMs();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .build();
        return HttpClients.custom()
                .disableRedirectHandling()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    @Produces
    @ApplicationScoped
    public Clock clock() {
        return Clock.systemUTC();
    }
}
