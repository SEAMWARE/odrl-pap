package org.fiware.odrl;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "http-client")
public interface HttpClientConfiguration {

    @WithName("timeout-ms")
    @WithDefault("10000")
    int timeoutMs();
}
