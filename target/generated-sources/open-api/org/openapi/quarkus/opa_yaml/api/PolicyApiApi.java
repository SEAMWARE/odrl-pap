package org.openapi.quarkus.opa_yaml.api;

import java.util.List;
import java.util.Map;


import org.openapi.quarkus.opa_yaml.model.DeletePolicyModule200Response;
import org.openapi.quarkus.opa_yaml.model.Model200Result;
import org.openapi.quarkus.opa_yaml.model.Model400;
import org.openapi.quarkus.opa_yaml.model.Model404;
/**
  * Open Policy Agent (OPA) REST API
  * <p>OPA provides policy-based control for cloud native environments. The following *endpoints* (such as `PUT /v1/policies`) provide reference documentation for the OPA REST API.  ### API specification viewing options  - **[View the specification in *Redoc* (default)](index.html)** - **[View the specification in *Swagger UI*](swagger-ui.html)**</p>
  */
@jakarta.ws.rs.Path("/v1/policies")
@org.eclipse.microprofile.rest.client.inject.RegisterRestClient(configKey="opa_yaml")
@io.quarkiverse.openapi.generator.annotations.GeneratedClass(value="opa.yaml", tag = "PolicyApi")
@jakarta.enterprise.context.ApplicationScoped
public interface PolicyApiApi {

     /**
     * Delete a policy module
     *
     * This API endpoint removes an existing policy module from the server
     *
     * @param id The name of a policy module
     * @param pretty If true, response will be in a human-readable format.
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="opa_yaml", operationId="deletePolicyModule", method="DELETE", path="/v1/policies/{id}")
    @jakarta.ws.rs.DELETE
    @jakarta.ws.rs.Path("/{id}")
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("deletePolicyModule")
    public jakarta.ws.rs.core.Response deletePolicyModule(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("id") @jakarta.ws.rs.PathParam("id")String id, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("pretty") @jakarta.ws.rs.QueryParam("pretty") Boolean pretty
    );

     /**
     * List policies
     *
     * This API endpoint responds with a list of all policy modules on the server (result response)
     *
     * @param pretty If true, response will be in a human-readable format.
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="opa_yaml", operationId="getPolicies", method="GET", path="/v1/policies")
    @jakarta.ws.rs.GET
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getPolicies")
    public jakarta.ws.rs.core.Response getPolicies(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("pretty") @jakarta.ws.rs.QueryParam("pretty") Boolean pretty
    );

     /**
     * Get a policy module
     *
     * This API endpoint returns the details of the specified policy module (`{id}`)
     *
     * @param id The name of a policy module
     * @param pretty If true, response will be in a human-readable format.
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="opa_yaml", operationId="getPolicyModule", method="GET", path="/v1/policies/{id}")
    @jakarta.ws.rs.GET
    @jakarta.ws.rs.Path("/{id}")
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getPolicyModule")
    public jakarta.ws.rs.core.Response getPolicyModule(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("id") @jakarta.ws.rs.PathParam("id")String id, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("pretty") @jakarta.ws.rs.QueryParam("pretty") Boolean pretty
    );

     /**
     * Create or update a policy module
     *
     * - If the policy module does not exist, it is created. - If the policy module already exists, it is replaced.  If the policy module isn't correctly defined, a *bad request* (400) response is returned.  ### Example policy module ```yaml package opa.examples  import data.servers import data.networks import data.ports  public_servers[server] {   some k, m    server := servers[_]    server.ports[_] == ports[k].id    ports[k].networks[_] == networks[m].id    networks[m].public == true } ```
     *
     * @param id The name of a policy module
     * @param pretty If true, response will be in a human-readable format.
     * @param metrics If true, compiler performance metrics will be returned in the response.
     * @param body 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="opa_yaml", operationId="putPolicyModule", method="PUT", path="/v1/policies/{id}")
    @jakarta.ws.rs.PUT
    @jakarta.ws.rs.Path("/{id}")
    @jakarta.ws.rs.Consumes({"text/plain"})
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("putPolicyModule")
    public jakarta.ws.rs.core.Response putPolicyModule(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("id") @jakarta.ws.rs.PathParam("id")String id, 
        String body, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("pretty") @jakarta.ws.rs.QueryParam("pretty") Boolean pretty, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("metrics") @jakarta.ws.rs.QueryParam("metrics") Boolean metrics
    );

}