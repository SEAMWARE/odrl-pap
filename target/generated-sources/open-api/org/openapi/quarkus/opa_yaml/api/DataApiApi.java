package org.openapi.quarkus.opa_yaml.api;

import java.util.List;
import java.util.Map;


import org.openapi.quarkus.opa_yaml.model.DeletePolicyModule200Response;
import org.openapi.quarkus.opa_yaml.model.Model400;
import org.openapi.quarkus.opa_yaml.model.Model404;
import org.openapi.quarkus.opa_yaml.model.PatchesSchemaInner;
/**
  * Open Policy Agent (OPA) REST API
  * <p>OPA provides policy-based control for cloud native environments. The following *endpoints* (such as `PUT /v1/policies`) provide reference documentation for the OPA REST API.  ### API specification viewing options  - **[View the specification in *Redoc* (default)](index.html)** - **[View the specification in *Swagger UI*](swagger-ui.html)**</p>
  */
@jakarta.ws.rs.Path("")
@org.eclipse.microprofile.rest.client.inject.RegisterRestClient(configKey="opa_yaml")
@io.quarkiverse.openapi.generator.annotations.GeneratedClass(value="opa.yaml", tag = "DataApi")
@jakarta.enterprise.context.ApplicationScoped
public interface DataApiApi {

     /**
     * Delete a document
     *
     * This API endpoint deletes an existing document from the server
     *
     * @param path A backslash (/) delimited path to access values inside object and array documents. If the path points to an array, the server will attempt to convert the array index to an integer. If the path element cannot be converted to an integer, the server will respond with 404.
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="opa_yaml", operationId="deleteDocument", method="DELETE", path="/v1/data/{path}")
    @jakarta.ws.rs.DELETE
    @jakarta.ws.rs.Path("/v1/data/{path}")
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("deleteDocument")
    public jakarta.ws.rs.core.Response deleteDocument(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("path") @jakarta.ws.rs.PathParam("path")String path
    );

     /**
     * Get a document
     *
     * This API endpoint returns the document specified by `path`.  The server will return a *bad request* (400) response if either: - The query requires an input document and you do not provide it - You provide the input document but the query has already defined it.
     *
     * @param path A backslash (/) delimited path to access values inside object and array documents. If the path points to an array, the server will attempt to convert the array index to an integer. If the path element cannot be converted to an integer, the server will respond with 404.
     * @param input Provide the text for an [input document](https://www.openpolicyagent.org/docs/latest/kubernetes-primer/#input-document) in JSON format
     * @param pretty If true, response will be in a human-readable format.
     * @param provenance If true, response will include build and version information in addition to the result.
     * @param explain If set to *full*, response will include query explanations in addition to the result.
     * @param metrics If true, compiler performance metrics will be returned in the response.
     * @param instrument If true, response will return additional performance metrics in addition to the result and the standard metrics.  **Caution:** This can add significant overhead to query evaluation. The recommendation is to only use this parameter if you are debugging a performance problem.
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="opa_yaml", operationId="getDocument", method="GET", path="/v1/data/{path}")
    @jakarta.ws.rs.GET
    @jakarta.ws.rs.Path("/v1/data/{path}")
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getDocument")
    public jakarta.ws.rs.core.Response getDocument(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("path") @jakarta.ws.rs.PathParam("path")String path, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("input") @jakarta.ws.rs.QueryParam("input") Map<String, Object> input, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("pretty") @jakarta.ws.rs.QueryParam("pretty") Boolean pretty, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("provenance") @jakarta.ws.rs.QueryParam("provenance") Boolean provenance, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("explain") @jakarta.ws.rs.QueryParam("explain") String explain, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("metrics") @jakarta.ws.rs.QueryParam("metrics") Boolean metrics, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("instrument") @jakarta.ws.rs.QueryParam("instrument") Boolean instrument
    );

     /**
     * Get a document (with input)
     *
     * The server will return a *bad request* (400) response if either: - The query requires an input document and you do not provide it - You provided an input document but the query has already defined it.  If `path` indexes into an array, the server will attempt to convert the array index to an integer. If the path element cannot be converted to an integer, a *not found* response (404) will be returned.
     *
     * @param path A backslash (/) delimited path to access values inside object and array documents. If the path points to an array, the server will attempt to convert the array index to an integer. If the path element cannot be converted to an integer, the server will respond with 404.
     * @param pretty If true, response will be in a human-readable format.
     * @param provenance If true, response will include build and version information in addition to the result.
     * @param explain If set to *full*, response will include query explanations in addition to the result.
     * @param metrics If true, compiler performance metrics will be returned in the response.
     * @param instrument If true, response will return additional performance metrics in addition to the result and the standard metrics.  **Caution:** This can add significant overhead to query evaluation. The recommendation is to only use this parameter if you are debugging a performance problem.
     * @param requestBody The input document (in JSON format)
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="opa_yaml", operationId="getDocumentWithPath", method="POST", path="/v1/data/{path}")
    @jakarta.ws.rs.POST
    @jakarta.ws.rs.Path("/v1/data/{path}")
    @jakarta.ws.rs.Consumes({"application/json"})
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getDocumentWithPath")
    public jakarta.ws.rs.core.Response getDocumentWithPath(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("path") @jakarta.ws.rs.PathParam("path")String path, 
        Map<String, Object> requestBody, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("pretty") @jakarta.ws.rs.QueryParam("pretty") Boolean pretty, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("provenance") @jakarta.ws.rs.QueryParam("provenance") Boolean provenance, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("explain") @jakarta.ws.rs.QueryParam("explain") String explain, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("metrics") @jakarta.ws.rs.QueryParam("metrics") Boolean metrics, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("instrument") @jakarta.ws.rs.QueryParam("instrument") Boolean instrument
    );

     /**
     * Get a document (with webhook)
     *
     * The example given here assumes you have created a policy (with `PUT /v1/policies/{path}`), such as:    ```yaml   package opa.examples   import input.example.flag   allow_request { flag == true }   ```  The server will return a *not found* (404) response if the requested document is missing or undefined. 
     *
     * @param path A backslash (/) delimited path to access values inside object and array documents. If the path points to an array, the server will attempt to convert the array index to an integer. If the path element cannot be converted to an integer, the server will respond with 404.
     * @param pretty If true, response will be in a human-readable format.
     * @param requestBody The input document (in JSON format)
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="opa_yaml", operationId="getDocumentWithWebHook", method="POST", path="/v0/data/{path}")
    @jakarta.ws.rs.POST
    @jakarta.ws.rs.Path("/v0/data/{path}")
    @jakarta.ws.rs.Consumes({"application/yaml"})
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getDocumentWithWebHook")
    public jakarta.ws.rs.core.Response getDocumentWithWebHook(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("path") @jakarta.ws.rs.PathParam("path")String path, 
        Map<String, Object> requestBody, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("pretty") @jakarta.ws.rs.QueryParam("pretty") Boolean pretty
    );

     /**
     * Update a document
     *
     * This API endpoint updates an existing document on the server by describing the changes required (using [JSON patch operations](http://jsonpatch.com/))
     *
     * @param path A backslash (/) delimited path to access values inside object and array documents. If the path points to an array, the server will attempt to convert the array index to an integer. If the path element cannot be converted to an integer, the server will respond with 404.
     * @param patchesSchemaInner The list of JSON patch operations.
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="opa_yaml", operationId="patchDocument", method="PATCH", path="/v1/data/{path}")
    @jakarta.ws.rs.PATCH
    @jakarta.ws.rs.Path("/v1/data/{path}")
    @jakarta.ws.rs.Consumes({"application/json"})
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("patchDocument")
    public jakarta.ws.rs.core.Response patchDocument(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("path") @jakarta.ws.rs.PathParam("path")String path, 
        List<PatchesSchemaInner> patchesSchemaInner
    );

     /**
     * Create or overwrite a document
     *
     * If the path does not refer to an existing document (for example *us-west/servers*), the server will attempt to create all the necessary containing documents.  This behavior is similar to the Unix command [mkdir -p](https://en.wikipedia.org/wiki/Mkdir#Options).
     *
     * @param path A backslash (/) delimited path to access values inside object and array documents. If the path points to an array, the server will attempt to convert the array index to an integer. If the path element cannot be converted to an integer, the server will respond with 404.
     * @param body The JSON document to write to the specified path.
     * @param ifNoneMatch The server will respect the If-None-Match header if it is set to * (in other words, it will not overwrite an existing document located at the specified `path`).
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="opa_yaml", operationId="putDocument", method="PUT", path="/v1/data/{path}")
    @jakarta.ws.rs.PUT
    @jakarta.ws.rs.Path("/v1/data/{path}")
    @jakarta.ws.rs.Consumes({"application/json"})
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("putDocument")
    public jakarta.ws.rs.core.Response putDocument(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("path") @jakarta.ws.rs.PathParam("path")String path, 
        Object body, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("If-None-Match") @jakarta.ws.rs.HeaderParam("If-None-Match")String ifNoneMatch
    );

}