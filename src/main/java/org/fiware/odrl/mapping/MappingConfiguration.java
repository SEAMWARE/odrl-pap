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

    /**
     * Deep-merges the given mapping configuration into this one.
     * <p>
     * In contrast to {@link java.util.HashMap#putAll(java.util.Map)}, entries are merged per
     * {@link OdrlAttribute} <em>and</em> per namespace instead of replacing a whole attribute. This
     * allows an additional mapping (e.g. one provided via {@code paths.mapping}) to <em>extend</em>
     * the built-in mapping with a single custom namespace - such as a {@code consent} leftOperand -
     * without dropping all default namespaces (e.g. {@code ngsi-ld}, {@code vc}) of that attribute,
     * as a plain {@code putAll} would. Operands supplied by {@code other} override an existing operand
     * with the same {@code namespace:key}.
     *
     * @param other the mapping configuration whose entries are merged into this one; {@code null} is ignored
     */
    public void deepMerge(MappingConfiguration other) {
        if (other == null) {
            return;
        }
        other.forEach((attribute, namespacedMap) -> {
            NamespacedMap targetNamespacedMap = computeIfAbsent(attribute, key -> new NamespacedMap());
            namespacedMap.forEach((namespace, regoMap) -> {
                RegoMap targetRegoMap = targetNamespacedMap.computeIfAbsent(namespace, key -> new RegoMap());
                targetRegoMap.putAll(regoMap);
            });
        });
    }
}