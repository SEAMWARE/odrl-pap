package org.openapi.quarkus.opa_yaml.api;

import java.util.List;
import java.util.Map;


import org.openapi.quarkus.opa_yaml.model.GetQuery200Response;
import org.openapi.quarkus.opa_yaml.model.Model400;
import org.openapi.quarkus.opa_yaml.model.Model404;
/**
  * Open Policy Agent (OPA) REST API
  * <p>OPA provides policy-based control for cloud native environments. The following *endpoints* (such as `PUT /v1/policies`) provide reference documentation for the OPA REST API.  ### API specification viewing options  - **[View the specification in *Redoc* (default)](index.html)** - **[View the specification in *Swagger UI*](swagger-ui.html)**</p>
  */
@jakarta.ws.rs.Path("")
@org.eclipse.microprofile.rest.client.inject.RegisterRestClient(configKey="opa_yaml")
@io.quarkiverse.openapi.generator.annotations.GeneratedClass(value="opa.yaml", tag = "QueryApi")
@jakarta.enterprise.context.ApplicationScoped
public interface QueryApiApi {

     /**
     * Execute an ad-hoc query (simple)
     *
     * This API endpoint returns bindings for the variables in the query.  For more complex JSON queries, use `POST /v1/query` instead.
     *
     * @param q The [URL-encoded](https://www.w3schools.com/tags/ref_urlencode.ASP) ad-hoc query to execute.
     * @param pretty If true, response will be in a human-readable format.
     * @param explain If set to *full*, response will include query explanations in addition to the result.
     * @param metrics If true, compiler performance metrics will be returned in the response.
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="opa_yaml", operationId="getQuery", method="GET", path="/v1/query")
    @jakarta.ws.rs.GET
    @jakarta.ws.rs.Path("/v1/query")
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getQuery")
    public jakarta.ws.rs.core.Response getQuery(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("q") @jakarta.ws.rs.QueryParam("q") String q, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("pretty") @jakarta.ws.rs.QueryParam("pretty") Boolean pretty, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("explain") @jakarta.ws.rs.QueryParam("explain") String explain, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("metrics") @jakarta.ws.rs.QueryParam("metrics") Boolean metrics
    );

     /**
     * Execute an ad-hoc query (complex)
     *
     * This API endpoint returns bindings for the variables in the query.  For simpler JSON queries, you may use `GET /v1/query` instead.
     *
     * @param pretty If true, response will be in a human-readable format.
     * @param explain If set to *full*, response will include query explanations in addition to the result.
     * @param metrics If true, compiler performance metrics will be returned in the response.
     * @param requestBody The test of the query (in JSON format)
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="opa_yaml", operationId="postQuery", method="POST", path="/v1/query")
    @jakarta.ws.rs.POST
    @jakarta.ws.rs.Path("/v1/query")
    @jakarta.ws.rs.Consumes({"application/json"})
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("postQuery")
    public jakarta.ws.rs.core.Response postQuery(
        Map<String, Object> requestBody, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("pretty") @jakarta.ws.rs.QueryParam("pretty") Boolean pretty, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("explain") @jakarta.ws.rs.QueryParam("explain") String explain, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("metrics") @jakarta.ws.rs.QueryParam("metrics") Boolean metrics
    );

     /**
     * Execute a simple query
     *
     * This API queries the document at *_/data/system/main* by default (however, you can [configure OPA](https://www.openpolicyagent.org/docs/latest/configuration/) to use a different path to serve these queries). That document defines the response. For example, use the following in `PUT /v1/policies/{path}`) to define a rule that will produce a value for the *_/data/system/main* document:    ```yaml   package system   main = msg {     msg := sprintf(\"hello, %v\", input.user)   }   ```  The server will return a *not found* (404) response if *_/data/system/main* is undefined.
     *
     * @param pretty If true, response will be in a human-readable format.
     * @param requestBody The text of the input document (in JSON format)
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="opa_yaml", operationId="postSimpleQuery", method="POST", path="/")
    @jakarta.ws.rs.POST
    @jakarta.ws.rs.Path("/")
    @jakarta.ws.rs.Consumes({"application/json"})
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("postSimpleQuery")
    public jakarta.ws.rs.core.Response postSimpleQuery(
        Map<String, Object> requestBody, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("pretty") @jakarta.ws.rs.QueryParam("pretty") Boolean pretty
    );

}