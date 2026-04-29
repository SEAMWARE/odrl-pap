package org.openapi.quarkus.bundle_yaml.api;

import java.util.List;
import java.util.Map;


import java.io.File;
/**
  * OPA Bundle API
  * <p>OPA Bundle API</p>
  */
@jakarta.ws.rs.Path("/bundles/service/v1")
@org.eclipse.microprofile.rest.client.inject.RegisterRestClient(configKey="bundle_yaml")
@io.quarkiverse.openapi.generator.annotations.GeneratedClass(value="bundle.yaml", tag = "Default")
@jakarta.enterprise.context.ApplicationScoped
public interface DefaultApi {

     /**
     * Return the additional data to be used for policy evaluation.
     *
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="bundle_yaml", operationId="getData", method="GET", path="/bundles/service/v1/data.tar.gz")
    @jakarta.ws.rs.GET
    @jakarta.ws.rs.Path("/data.tar.gz")
    @jakarta.ws.rs.Produces({"application/octet-stream"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getData")
    public jakarta.ws.rs.core.Response getData(
    );

     /**
     * Return the methods bundle.
     *
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="bundle_yaml", operationId="getMethods", method="GET", path="/bundles/service/v1/methods.tar.gz")
    @jakarta.ws.rs.GET
    @jakarta.ws.rs.Path("/methods.tar.gz")
    @jakarta.ws.rs.Produces({"application/octet-stream"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getMethods")
    public jakarta.ws.rs.core.Response getMethods(
    );

     /**
     * Return the policies bundle.
     *
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="bundle_yaml", operationId="getPolicies", method="GET", path="/bundles/service/v1/policies.tar.gz")
    @jakarta.ws.rs.GET
    @jakarta.ws.rs.Path("/policies.tar.gz")
    @jakarta.ws.rs.Produces({"application/octet-stream"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getPolicies")
    public jakarta.ws.rs.core.Response getPolicies(
    );

}