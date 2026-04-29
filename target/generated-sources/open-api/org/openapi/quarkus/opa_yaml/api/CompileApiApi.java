package org.openapi.quarkus.opa_yaml.api;

import java.util.List;
import java.util.Map;


import org.openapi.quarkus.opa_yaml.model.GetQuery200Response;
import org.openapi.quarkus.opa_yaml.model.Model400;
/**
  * Open Policy Agent (OPA) REST API
  * <p>OPA provides policy-based control for cloud native environments. The following *endpoints* (such as `PUT /v1/policies`) provide reference documentation for the OPA REST API.  ### API specification viewing options  - **[View the specification in *Redoc* (default)](index.html)** - **[View the specification in *Swagger UI*](swagger-ui.html)**</p>
  */
@jakarta.ws.rs.Path("/v1/compile")
@org.eclipse.microprofile.rest.client.inject.RegisterRestClient(configKey="opa_yaml")
@io.quarkiverse.openapi.generator.annotations.GeneratedClass(value="opa.yaml", tag = "CompileApi")
@jakarta.enterprise.context.ApplicationScoped
public interface CompileApiApi {

     /**
     * Compile
     *
     * This API endpoint allows you to partially evaluate Rego queries and obtain a simplified version of the policy. The example below assumes that OPA has been given the following policy (use `PUT /v1/policies/{path}`):  ```yaml package example allow {   input.subject.clearance_level >= data.reports[_].clearance_level } ``` Compile API **request body** so that it contain the following fields:  | Field | Type | Required | Description | | --- | --- | --- | --- | | `query` | `string` | Yes | The query to partially evaluate and compile. | | `input` | `any` | No | The input document to use during partial evaluation (default: undefined). | | `unknowns` | `array[string]` | No | The terms to treat as unknown during partial evaluation (default: `[\"input\"]`]). |  For example:  ```json {   \"query\": \"data.example.allow == true\",   \"input\": {     \"subject\": {       \"clearance_level\": 4     }   },   \"unknowns\": [     \"data.reports\"     ] } ``` ### Partial evaluation In some cases, the result of partial valuation is a conclusive, unconditional answer. See [the guidance](https://www.openpolicyagent.org/docs/latest/rest-api/#unconditional-results-from-partial-evaluation) for details.
     *
     * @param pretty If true, response will be in a human-readable format.
     * @param explain If set to *full*, response will include query explanations in addition to the result.
     * @param metrics If true, compiler performance metrics will be returned in the response.
     * @param instrument If true, response will return additional performance metrics in addition to the result and the standard metrics.  **Caution:** This can add significant overhead to query evaluation. The recommendation is to only use this parameter if you are debugging a performance problem.
     * @param requestBody The query (in JSON format)
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="opa_yaml", operationId="postCompile", method="POST", path="/v1/compile")
    @jakarta.ws.rs.POST
    @jakarta.ws.rs.Consumes({"application/json"})
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("postCompile")
    public jakarta.ws.rs.core.Response postCompile(
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("pretty") @jakarta.ws.rs.QueryParam("pretty") Boolean pretty, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("explain") @jakarta.ws.rs.QueryParam("explain") String explain, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("metrics") @jakarta.ws.rs.QueryParam("metrics") Boolean metrics, 
        @io.quarkiverse.openapi.generator.annotations.GeneratedParam("instrument") @jakarta.ws.rs.QueryParam("instrument") Boolean instrument, 
        Map<String, Object> requestBody
    );

}