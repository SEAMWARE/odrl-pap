package org.fiware.odrl;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.openapi.quarkus.odrl_yaml.model.ServiceCreate;

@RegisterForReflection(
        targets = {
                ServiceCreate.class
        },
        classNames = {
                "com.github.benmanes.caffeine.cache.SSLSMSW"
        },
        registerFullHierarchy = true
)
public class ModelReflectionConfig {
}

