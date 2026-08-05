package org.fiware.odrl;

import io.smallrye.config.ConfigMapping;

import java.io.File;
import java.util.Optional;

/**
 * Runtime configuration for the optional filesystem paths that extend the built-in behaviour
 * (additional mapping, additional rego methods, alternative compaction context).
 * <p>
 * This is deliberately a <em>runtime</em> {@link ConfigMapping} (not {@code @StaticInitSafe}): the
 * paths are only consumed at runtime ({@link org.fiware.odrl.AppConfig#mappingConfiguration()} and
 * {@link org.fiware.odrl.BundleResource}'s startup observer). Marking it {@code @StaticInitSafe}
 * causes the values to be resolved at native-image build time, so environment variables such as
 * {@code PATHS_MAPPING}/{@code PATHS_REGO} set only at container start would be ignored.
 *
 * @author <a href="https://github.com/wistefan">Stefan Wiedemann</a>
 */
@ConfigMapping(prefix = "paths")
public interface PathsConfiguration {

    // path to an additional @link{MappingConfiguration} to be merged with the defaults
    Optional<File> mapping();

    // Path to additional rego-methods to be added to the built-in methods. Duplications will be overwritten
    Optional<File> rego();

    // Path to an alternative compactionContext to be used.
    Optional<File> compactionContext();
}
