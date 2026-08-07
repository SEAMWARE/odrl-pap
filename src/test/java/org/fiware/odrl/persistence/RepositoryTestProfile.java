package org.fiware.odrl.persistence;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

/**
 * Quarkus test profile for persistence-layer tests.
 *
 * <p>Disables global test resources (OPA container, MockServer) that are
 * registered by other test classes and would fail in environments without
 * Docker. Uses an embedded H2 in-memory database instead of the TCP-mode
 * H2 configured in the default test {@code application.properties}.</p>
 */
public class RepositoryTestProfile implements QuarkusTestProfile {

    @Override
    public boolean disableGlobalTestResources() {
        return true;
    }

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
                "quarkus.datasource.jdbc.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
        );
    }
}
