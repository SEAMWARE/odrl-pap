package org.openapi.quarkus.opa_yaml.api;

import java.util.List;
import java.util.Map;


import org.openapi.quarkus.opa_yaml.model.Model200SingleResult;
import org.openapi.quarkus.opa_yaml.model.Model400;
/**
  * Open Policy Agent (OPA) REST API
  * <p>OPA provides policy-based control for cloud native environments. The following *endpoints* (such as `PUT /v1/policies`) provide reference documentation for the OPA REST API.  ### API specification viewing options  - **[View the specification in *Redoc* (default)](index.html)** - **[View the specification in *Swagger UI*](swagger-ui.html)**</p>
  */
@jakarta.ws.rs.Path("/v1/config")
@org.eclipse.microprofile.rest.client.inject.RegisterRestClient(configKey="opa_yaml")
@io.quarkiverse.openapi.generator.annotations.GeneratedClass(value="opa.yaml", tag = "ConfigApi")
@jakarta.enterprise.context.ApplicationScoped
public interface ConfigApiApi {

     /**
     * Get configurations
     *
     * This API endpoint responds with active configuration (result response)  --- **Note** The `credentials` field in the `services` configuration and  The `private_key` and `key` fields in the `keys` configuration will be omitted from the API response  ---
     *
     * @param pretty If true, response will be in a human-readable format.
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="opa_yaml", operationId="getConfig", method="GET", path="/v1/config")
    @jakarta.ws.rs.GET
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getConfig")
    public jakarta.ws.rs.core.Response getConfig(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("pretty") @jakarta.ws.rs.QueryParam("pretty") Boolean pretty
    );

}