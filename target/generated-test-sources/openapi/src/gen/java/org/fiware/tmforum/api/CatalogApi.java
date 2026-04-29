package org.fiware.tmforum.api;

import org.fiware.tmforum.model.Catalog;
import org.fiware.tmforum.model.CatalogCreate;
import org.fiware.tmforum.model.CatalogUpdate;
import org.fiware.tmforum.model.Error;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;


import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

/**
* Represents a collection of functions to interact with the API endpoints.
*/
@Path("/catalog")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public interface CatalogApi {

    /**
     * This operation creates a Catalog entity.
     *
     * @param catalog The Catalog to be created
     * @return Created
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response createCatalog(@Valid @NotNull CatalogCreate catalog);


    /**
     * This operation deletes a Catalog entity.
     *
     * @param id Identifier of the Catalog
     * @return Deleted
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @DELETE
    @Path("/{id}")
    @Produces({ "application/json;charset=utf-8" })
    Response deleteCatalog(@PathParam("id") String id);


    /**
     * This operation list or find Catalog entities
     *
     * @param fields Comma-separated properties to be provided in response
     * @param offset Requested index for start of resources to be provided in response
     * @param limit Requested number of resources to be provided in response
     * @return Success
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @GET
    @Produces({ "application/json;charset=utf-8" })
    Response listCatalog(@QueryParam("fields")   String fields,@QueryParam("offset")   Integer offset,@QueryParam("limit")   Integer limit);


    /**
     * This operation updates partially a Catalog entity.
     *
     * @param id Identifier of the Catalog
     * @param catalog The Catalog to be updated
     * @return Updated
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @PATCH
    @Path("/{id}")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response patchCatalog(@PathParam("id") String id,@Valid @NotNull CatalogUpdate catalog);


    /**
     * This operation retrieves a Catalog entity. Attribute selection is enabled for all first level attributes.
     *
     * @param id Identifier of the Catalog
     * @param fields Comma-separated properties to provide in response
     * @return Success
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @GET
    @Path("/{id}")
    @Produces({ "application/json;charset=utf-8" })
    Response retrieveCatalog(@PathParam("id") String id,@QueryParam("fields")   String fields);

}
