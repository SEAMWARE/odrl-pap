package org.fiware.odrl.rego;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Encoded;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.Map;

/**
 * Custom OPA Data API client that preserves literal slashes in path parameters.
 *
 * <p>The Quarkus OpenAPI-generated {@code DataApiApi} uses a plain
 * {@code @PathParam("path")} which URL-encodes the {@code /} separator to
 * {@code %2F}. OPA then treats the entire string as a single document key
 * instead of traversing the data tree, causing every query to return
 * undefined.</p>
 *
 * <p>This interface applies {@link Encoded @Encoded} to the path parameter so
 * that {@code cofktfjuam/is_allowed} is sent as two path segments
 * ({@code /v1/data/cofktfjuam/is_allowed}) rather than one
 * ({@code /v1/data/cofktfjuam%2Fis_allowed}).</p>
 */
@Path("/")
@RegisterRestClient(configKey = "opa_data")
public interface OpaDataApi {

    /**
     * Evaluates an OPA document with the given input.
     *
     * @param path       slash-delimited data path (e.g. {@code "mypackage/is_allowed"})
     * @param input      the OPA input document wrapped as {@code {"input": ...}}
     * @param pretty     if true, format the response for readability
     * @param provenance if true, include build/version provenance
     * @param explain    explanation level (e.g. {@code "full"})
     * @param metrics    if true, include evaluation metrics
     * @param instrument if true, include detailed instrumentation metrics
     * @return the OPA evaluation response
     */
    @POST
    @Path("/v1/data/{path}")
    @Consumes("application/json")
    @Produces("application/json")
    Response getDocumentWithPath(
            @Encoded @PathParam("path") String path,
            Map<String, Object> input,
            @QueryParam("pretty") Boolean pretty,
            @QueryParam("provenance") Boolean provenance,
            @QueryParam("explain") String explain,
            @QueryParam("metrics") Boolean metrics,
            @QueryParam("instrument") Boolean instrument
    );
}
