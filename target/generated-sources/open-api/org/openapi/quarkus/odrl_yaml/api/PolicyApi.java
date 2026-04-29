package org.openapi.quarkus.odrl_yaml.api;

import java.util.List;
import java.util.Map;


import org.openapi.quarkus.odrl_yaml.model.Policy;
/**
  * ODRL PAP
  * <p>ODRL PAP</p>
  */
@jakarta.ws.rs.Path("/policy")
@org.eclipse.microprofile.rest.client.inject.RegisterRestClient(configKey="odrl_yaml")
@io.quarkiverse.openapi.generator.annotations.GeneratedClass(value="odrl.yaml", tag = "Policy")
@jakarta.enterprise.context.ApplicationScoped
public interface PolicyApi {

     /**
     * Creates a new policy from the given odrl-json
     *
     * @param requestBody 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="createPolicy", method="POST", path="/policy")
    @jakarta.ws.rs.POST
    @jakarta.ws.rs.Consumes({"application/json"})
    @jakarta.ws.rs.Produces({"text/plain"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("createPolicy")
    public jakarta.ws.rs.core.Response createPolicy(
        Map<String, Object> requestBody
    );

     /**
     * Creates or overwrites the given policy.
     *
     * @param id 
     * @param requestBody 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="createPolicyWithId", method="PUT", path="/policy/{id}")
    @jakarta.ws.rs.PUT
    @jakarta.ws.rs.Path("/{id}")
    @jakarta.ws.rs.Consumes({"application/json"})
    @jakarta.ws.rs.Produces({"text/plain"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("createPolicyWithId")
    public jakarta.ws.rs.core.Response createPolicyWithId(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("id") @jakarta.ws.rs.PathParam("id")String id, 
        Map<String, Object> requestBody
    );

     /**
     * Delete the given policy.
     *
     * @param id 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="deletePolicyById", method="DELETE", path="/policy/{id}")
    @jakarta.ws.rs.DELETE
    @jakarta.ws.rs.Path("/{id}")
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("deletePolicyById")
    public jakarta.ws.rs.core.Response deletePolicyById(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("id") @jakarta.ws.rs.PathParam("id")String id
    );

     /**
     * Delete the given policy.
     *
     * @param id 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="deletePolicyByUid", method="DELETE", path="/policy/odrl/{id}")
    @jakarta.ws.rs.DELETE
    @jakarta.ws.rs.Path("/odrl/{id}")
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("deletePolicyByUid")
    public jakarta.ws.rs.core.Response deletePolicyByUid(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("id") @jakarta.ws.rs.PathParam("id")String id
    );

     /**
     * Get the policies in the ODRL-Format
     *
     * @param page 
     * @param pageSize 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="getPolicies", method="GET", path="/policy")
    @jakarta.ws.rs.GET
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getPolicies")
    public jakarta.ws.rs.core.Response getPolicies(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("page") @jakarta.ws.rs.QueryParam("page") Integer page, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("pageSize") @jakarta.ws.rs.QueryParam("pageSize") Integer pageSize
    );

     /**
     * Return the given policy by its ID.
     *
     * @param id 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="getPolicyById", method="GET", path="/policy/{id}")
    @jakarta.ws.rs.GET
    @jakarta.ws.rs.Path("/{id}")
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getPolicyById")
    public jakarta.ws.rs.core.Response getPolicyById(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("id") @jakarta.ws.rs.PathParam("id")String id
    );

     /**
     * Return the given policy by its ID.
     *
     * @param id 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="getPolicyByUid", method="GET", path="/policy/odrl/{id}")
    @jakarta.ws.rs.GET
    @jakarta.ws.rs.Path("/odrl/{id}")
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getPolicyByUid")
    public jakarta.ws.rs.core.Response getPolicyByUid(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("id") @jakarta.ws.rs.PathParam("id")String id
    );

}