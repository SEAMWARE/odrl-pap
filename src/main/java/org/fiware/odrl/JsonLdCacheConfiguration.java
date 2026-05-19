package org.fiware.odrl;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "jsonld-cache")
public interface JsonLdCacheConfiguration {
    @WithName("enabled")
    @WithDefault("true")
    boolean enabled();

    @WithName("ttl-seconds")
    @WithDefault("3600")
    long ttlSeconds();

    @WithName("max-size")
    @WithDefault("100")
    long maxSize();
}
