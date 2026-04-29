package org.fiware.tmforum.api;

import org.fiware.tmforum.model.Error;
import org.fiware.tmforum.model.EventSubscription;
import org.fiware.tmforum.model.EventSubscriptionInput;

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
@Path("/hub")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public interface HubApi {

    /**
     * Sets the communication endpoint address the service instance must use to deliver information about its health state, execution state, failures and metrics.
     *
     * @param data Data containing the callback endpoint to deliver the information
     * @return Subscribed
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response registerListener(@Valid @NotNull EventSubscriptionInput data);


    /**
     * Resets the communication endpoint address the service instance must use to deliver information about its health state, execution state, failures and metrics.
     *
     * @param id The id of the registered listener
     * @return Deleted
     * @return Bad request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method not allowed
     * @return Internal Server Error
     */
    @DELETE
    @Path("/{id}")
    @Produces({ "application/json;charset=utf-8" })
    Response unregisterListener(@PathParam("id") String id);

}
