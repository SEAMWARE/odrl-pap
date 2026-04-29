package org.openapi.quarkus.opa_yaml.api;

import java.util.List;
import java.util.Map;


/**
  * Open Policy Agent (OPA) REST API
  * <p>OPA provides policy-based control for cloud native environments. The following *endpoints* (such as `PUT /v1/policies`) provide reference documentation for the OPA REST API.  ### API specification viewing options  - **[View the specification in *Redoc* (default)](index.html)** - **[View the specification in *Swagger UI*](swagger-ui.html)**</p>
  */
@jakarta.ws.rs.Path("/health")
@org.eclipse.microprofile.rest.client.inject.RegisterRestClient(configKey="opa_yaml")
@io.quarkiverse.openapi.generator.annotations.GeneratedClass(value="opa.yaml", tag = "HealthApi")
@jakarta.enterprise.context.ApplicationScoped
public interface HealthApiApi {

     /**
     * Health
     *
     * This API endpoint verifies that the server is operational.  The response from the server is either 200 or 500: - **200** - OPA service is healthy. If `bundles` is true, then all configured bundles have been activated. If `plugins` is true, then all plugins are in an 'OK' state. - **500** - OPA service is *not* healthy. If `bundles` is true, at least one of configured bundles has not yet been activated. If `plugins` is true, at least one plugins is in a 'not OK' state.  --- **Note** This check is only for initial bundle activation. Subsequent downloads will not affect the health check.  Use the **status** endpoint (in the (management API)[management.html]) for more fine-grained bundle status monitoring.  ---
     *
     * @param bundles Reports on bundle activation status (useful for 'ready' checks at startup).  This includes any discovery bundles or bundles defined in the loaded discovery configuration.
     * @param plugins Reports on plugin status
     * @param excludePlugin String parameter to exclude a plugin from status checks. Can be added multiple times. Does nothing if plugins is not true. This parameter is useful for special use cases where a plugin depends on the server being fully initialized before it can fully initialize itself.
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="opa_yaml", operationId="getHealth", method="GET", path="/health")
    @jakarta.ws.rs.GET
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getHealth")
    public jakarta.ws.rs.core.Response getHealth(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("bundles") @jakarta.ws.rs.QueryParam("bundles") Boolean bundles, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("plugins") @jakarta.ws.rs.QueryParam("plugins") Boolean plugins, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("exclude-plugin") @jakarta.ws.rs.QueryParam("exclude-plugin") List<String> excludePlugin
    );

}