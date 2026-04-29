package org.fiware.tmforum.api;

import org.fiware.tmforum.model.Error;
import org.fiware.tmforum.model.ProductOfferingPrice;
import org.fiware.tmforum.model.ProductOfferingPriceCreate;
import org.fiware.tmforum.model.ProductOfferingPriceUpdate;

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
@Path("/productOfferingPrice")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public interface ProductOfferingPriceApi {

    /**
     * This operation creates a ProductOfferingPrice entity.
     *
     * @param productOfferingPrice The ProductOfferingPrice to be created
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
    Response createProductOfferingPrice(@Valid @NotNull ProductOfferingPriceCreate productOfferingPrice);


    /**
     * This operation deletes a ProductOfferingPrice entity.
     *
     * @param id Identifier of the ProductOfferingPrice
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
    Response deleteProductOfferingPrice(@PathParam("id") String id);


    /**
     * This operation list or find ProductOfferingPrice entities
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
    Response listProductOfferingPrice(@QueryParam("fields")   String fields,@QueryParam("offset")   Integer offset,@QueryParam("limit")   Integer limit);


    /**
     * This operation updates partially a ProductOfferingPrice entity.
     *
     * @param id Identifier of the ProductOfferingPrice
     * @param productOfferingPrice The ProductOfferingPrice to be updated
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
    Response patchProductOfferingPrice(@PathParam("id") String id,@Valid @NotNull ProductOfferingPriceUpdate productOfferingPrice);


    /**
     * This operation retrieves a ProductOfferingPrice entity. Attribute selection is enabled for all first level attributes.
     *
     * @param id Identifier of the ProductOfferingPrice
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
    Response retrieveProductOfferingPrice(@PathParam("id") String id,@QueryParam("fields")   String fields);

}
