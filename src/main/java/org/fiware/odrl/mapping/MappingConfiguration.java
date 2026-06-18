package org.fiware.odrl.mapping;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/wistefan">Stefan Wiedemann</a>
 */
@RegisterForReflection
public class MappingConfiguration extends HashMap<OdrlAttribute, NamespacedMap> {

    public MappingConfiguration() {
        super();
    }

    /**
     * Returns all valid policy keys (namespace:key combinations) for the given attribute, derived from the mapping configuration.
     */
    public Set<String> getKeys(OdrlAttribute attribute) {
        NamespacedMap namespacedMap = get(attribute);
        if (namespacedMap == null) {
            return Set.of();
        }
        return namespacedMap.entrySet().stream()
                .flatMap(entry -> entry.getValue().keySet().stream()
                        .map(key -> entry.getKey() + ":" + key))
                .collect(Collectors.toSet());
    }
}