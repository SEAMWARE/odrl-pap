package org.fiware.tmforum.api;

import org.fiware.tmforum.model.Category;
import org.fiware.tmforum.model.CategoryCreate;
import org.fiware.tmforum.model.CategoryUpdate;
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
@Path("/category")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public interface CategoryApi {

    /**
     * This operation creates a Category entity.
     *
     * @param category The Category to be created
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
    Response createCategory(@Valid @NotNull CategoryCreate category);


    /**
     * This operation deletes a Category entity.
     *
     * @param id Identifier of the Category
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
    Response deleteCategory(@PathParam("id") String id);


    /**
     * This operation list or find Category entities
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
    Response listCategory(@QueryParam("fields")   String fields,@QueryParam("offset")   Integer offset,@QueryParam("limit")   Integer limit);


    /**
     * This operation updates partially a Category entity.
     *
     * @param id Identifier of the Category
     * @param category The Category to be updated
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
    Response patchCategory(@PathParam("id") String id,@Valid @NotNull CategoryUpdate category);


    /**
     * This operation retrieves a Category entity. Attribute selection is enabled for all first level attributes.
     *
     * @param id Identifier of the Category
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
    Response retrieveCategory(@PathParam("id") String id,@QueryParam("fields")   String fields);

}
