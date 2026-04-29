package org.openapi.quarkus.odrl_yaml.api;

import java.util.List;
import java.util.Map;


import org.openapi.quarkus.odrl_yaml.model.Policy;
import org.openapi.quarkus.odrl_yaml.model.PolicyPath;
import org.openapi.quarkus.odrl_yaml.model.Service;
import org.openapi.quarkus.odrl_yaml.model.ServiceCreate;
import org.openapi.quarkus.odrl_yaml.model.ServiceListInner;
/**
  * ODRL PAP
  * <p>ODRL PAP</p>
  */
@jakarta.ws.rs.Path("/service")
@org.eclipse.microprofile.rest.client.inject.RegisterRestClient(configKey="odrl_yaml")
@io.quarkiverse.openapi.generator.annotations.GeneratedClass(value="odrl.yaml", tag = "Service")
@jakarta.enterprise.context.ApplicationScoped
public interface ServiceApi {

     /**
     * Create a new service to group policies
     *
     * @param serviceCreate 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="createService", method="POST", path="/service")
    @jakarta.ws.rs.POST
    @jakarta.ws.rs.Consumes({"application/json"})
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("createService")
    public jakarta.ws.rs.core.Response createService(
        ServiceCreate serviceCreate
    );

     /**
     * Creates a new policy from the given odrl-json under the service-id
     *
     * @param serviceId 
     * @param requestBody 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="createServicePolicy", method="POST", path="/service/{service-id}/policy")
    @jakarta.ws.rs.POST
    @jakarta.ws.rs.Path("/{service-id}/policy")
    @jakarta.ws.rs.Consumes({"application/json"})
    @jakarta.ws.rs.Produces({"text/plain"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("createServicePolicy")
    public jakarta.ws.rs.core.Response createServicePolicy(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("service-id") @jakarta.ws.rs.PathParam("service-id")String serviceId, 
        Map<String, Object> requestBody
    );

     /**
     * Creates or overwrites the given policy under the service-id.
     *
     * @param serviceId 
     * @param id 
     * @param requestBody 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="createServicePolicyWithId", method="PUT", path="/service/{service-id}/policy/{id}")
    @jakarta.ws.rs.PUT
    @jakarta.ws.rs.Path("/{service-id}/policy/{id}")
    @jakarta.ws.rs.Consumes({"application/json"})
    @jakarta.ws.rs.Produces({"text/plain"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("createServicePolicyWithId")
    public jakarta.ws.rs.core.Response createServicePolicyWithId(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("service-id") @jakarta.ws.rs.PathParam("service-id")String serviceId, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("id") @jakarta.ws.rs.PathParam("id")String id, 
        Map<String, Object> requestBody
    );

     /**
     * Delete the service with the given id and all policies below
     *
     * @param serviceId 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="deleteService", method="DELETE", path="/service/{service-id}")
    @jakarta.ws.rs.DELETE
    @jakarta.ws.rs.Path("/{service-id}")
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("deleteService")
    public jakarta.ws.rs.core.Response deleteService(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("service-id") @jakarta.ws.rs.PathParam("service-id")String serviceId
    );

     /**
     * Delete the given policy under the service-id.
     *
     * @param serviceId 
     * @param id 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="deleteServicePolicyById", method="DELETE", path="/service/{service-id}/policy/{id}")
    @jakarta.ws.rs.DELETE
    @jakarta.ws.rs.Path("/{service-id}/policy/{id}")
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("deleteServicePolicyById")
    public jakarta.ws.rs.core.Response deleteServicePolicyById(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("service-id") @jakarta.ws.rs.PathParam("service-id")String serviceId, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("id") @jakarta.ws.rs.PathParam("id")String id
    );

     /**
     * Delete the given policy under the service-id.
     *
     * @param serviceId 
     * @param id 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="deleteServicePolicyByUid", method="DELETE", path="/service/{service-id}/policy/odrl/{id}")
    @jakarta.ws.rs.DELETE
    @jakarta.ws.rs.Path("/{service-id}/policy/odrl/{id}")
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("deleteServicePolicyByUid")
    public jakarta.ws.rs.core.Response deleteServicePolicyByUid(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("service-id") @jakarta.ws.rs.PathParam("service-id")String serviceId, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("id") @jakarta.ws.rs.PathParam("id")String id
    );

     /**
     * Get the service with the given id
     *
     * @param serviceId 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="getService", method="GET", path="/service/{service-id}")
    @jakarta.ws.rs.GET
    @jakarta.ws.rs.Path("/{service-id}")
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getService")
    public jakarta.ws.rs.core.Response getService(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("service-id") @jakarta.ws.rs.PathParam("service-id")String serviceId
    );

     /**
     * Get the policies in the ODRL-Format under the service-id
     *
     * @param serviceId 
     * @param page 
     * @param pageSize 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="getServicePolicies", method="GET", path="/service/{service-id}/policy")
    @jakarta.ws.rs.GET
    @jakarta.ws.rs.Path("/{service-id}/policy")
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getServicePolicies")
    public jakarta.ws.rs.core.Response getServicePolicies(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("service-id") @jakarta.ws.rs.PathParam("service-id")String serviceId, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("page") @jakarta.ws.rs.QueryParam("page") Integer page, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("pageSize") @jakarta.ws.rs.QueryParam("pageSize") Integer pageSize
    );

     /**
     * Return the given policy by its ID under the service-id.
     *
     * @param serviceId 
     * @param id 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="getServicePolicyById", method="GET", path="/service/{service-id}/policy/{id}")
    @jakarta.ws.rs.GET
    @jakarta.ws.rs.Path("/{service-id}/policy/{id}")
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getServicePolicyById")
    public jakarta.ws.rs.core.Response getServicePolicyById(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("service-id") @jakarta.ws.rs.PathParam("service-id")String serviceId, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("id") @jakarta.ws.rs.PathParam("id")String id
    );

     /**
     * Return the given policy by its ID under the service-id.
     *
     * @param serviceId 
     * @param id 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="getServicePolicyByUid", method="GET", path="/service/{service-id}/policy/odrl/{id}")
    @jakarta.ws.rs.GET
    @jakarta.ws.rs.Path("/{service-id}/policy/odrl/{id}")
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getServicePolicyByUid")
    public jakarta.ws.rs.core.Response getServicePolicyByUid(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("service-id") @jakarta.ws.rs.PathParam("service-id")String serviceId, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("id") @jakarta.ws.rs.PathParam("id")String id
    );

     /**
     * Get all services and the path to their policy
     *
     * @param page 
     * @param pageSize 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="getServices", method="GET", path="/service")
    @jakarta.ws.rs.GET
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getServices")
    public jakarta.ws.rs.core.Response getServices(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("page") @jakarta.ws.rs.QueryParam("page") Integer page, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("pageSize") @jakarta.ws.rs.QueryParam("pageSize") Integer pageSize
    );

}