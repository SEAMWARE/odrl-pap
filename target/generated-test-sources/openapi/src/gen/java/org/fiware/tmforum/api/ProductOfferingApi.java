package org.fiware.tmforum.api;

import org.fiware.tmforum.model.Error;
import org.fiware.tmforum.model.ProductOffering;
import org.fiware.tmforum.model.ProductOfferingCreate;
import org.fiware.tmforum.model.ProductOfferingUpdate;

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
@Path("/productOffering")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public interface ProductOfferingApi {

    /**
     * This operation creates a ProductOffering entity.
     *
     * @param productOffering The ProductOffering to be created
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
    Response createProductOffering(@Valid @NotNull ProductOfferingCreate productOffering);


    /**
     * This operation deletes a ProductOffering entity.
     *
     * @param id Identifier of the ProductOffering
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
    Response deleteProductOffering(@PathParam("id") String id);


    /**
     * This operation list or find ProductOffering entities
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
    Response listProductOffering(@QueryParam("fields")   String fields,@QueryParam("offset")   Integer offset,@QueryParam("limit")   Integer limit);


    /**
     * This operation updates partially a ProductOffering entity.
     *
     * @param id Identifier of the ProductOffering
     * @param productOffering The ProductOffering to be updated
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
    Response patchProductOffering(@PathParam("id") String id,@Valid @NotNull ProductOfferingUpdate productOffering);


    /**
     * This operation retrieves a ProductOffering entity. Attribute selection is enabled for all first level attributes.
     *
     * @param id Identifier of the ProductOffering
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
    Response retrieveProductOffering(@PathParam("id") String id,@QueryParam("fields")   String fields);

}
