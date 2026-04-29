package org.fiware.tmforum.api;

import org.fiware.tmforum.model.CatalogAttributeValueChangeEvent;
import org.fiware.tmforum.model.CatalogBatchEvent;
import org.fiware.tmforum.model.CatalogCreateEvent;
import org.fiware.tmforum.model.CatalogDeleteEvent;
import org.fiware.tmforum.model.CatalogStateChangeEvent;
import org.fiware.tmforum.model.CategoryAttributeValueChangeEvent;
import org.fiware.tmforum.model.CategoryCreateEvent;
import org.fiware.tmforum.model.CategoryDeleteEvent;
import org.fiware.tmforum.model.CategoryStateChangeEvent;
import org.fiware.tmforum.model.Error;
import org.fiware.tmforum.model.EventSubscription;
import org.fiware.tmforum.model.ProductOfferingAttributeValueChangeEvent;
import org.fiware.tmforum.model.ProductOfferingCreateEvent;
import org.fiware.tmforum.model.ProductOfferingDeleteEvent;
import org.fiware.tmforum.model.ProductOfferingPriceAttributeValueChangeEvent;
import org.fiware.tmforum.model.ProductOfferingPriceCreateEvent;
import org.fiware.tmforum.model.ProductOfferingPriceDeleteEvent;
import org.fiware.tmforum.model.ProductOfferingPriceStateChangeEvent;
import org.fiware.tmforum.model.ProductOfferingStateChangeEvent;
import org.fiware.tmforum.model.ProductSpecificationAttributeValueChangeEvent;
import org.fiware.tmforum.model.ProductSpecificationCreateEvent;
import org.fiware.tmforum.model.ProductSpecificationDeleteEvent;
import org.fiware.tmforum.model.ProductSpecificationStateChangeEvent;

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
@Path("/listener")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public interface ListenerApi {

    /**
     * Example of a client listener for receiving the notification CatalogAttributeValueChangeEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/catalogAttributeValueChangeEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToCatalogAttributeValueChangeEvent(@Valid @NotNull CatalogAttributeValueChangeEvent data);


    /**
     * Example of a client listener for receiving the notification CatalogBatchEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/catalogBatchEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToCatalogBatchEvent(@Valid @NotNull CatalogBatchEvent data);


    /**
     * Example of a client listener for receiving the notification CatalogCreateEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/catalogCreateEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToCatalogCreateEvent(@Valid @NotNull CatalogCreateEvent data);


    /**
     * Example of a client listener for receiving the notification CatalogDeleteEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/catalogDeleteEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToCatalogDeleteEvent(@Valid @NotNull CatalogDeleteEvent data);


    /**
     * Example of a client listener for receiving the notification CatalogStateChangeEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/catalogStateChangeEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToCatalogStateChangeEvent(@Valid @NotNull CatalogStateChangeEvent data);


    /**
     * Example of a client listener for receiving the notification CategoryAttributeValueChangeEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/categoryAttributeValueChangeEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToCategoryAttributeValueChangeEvent(@Valid @NotNull CategoryAttributeValueChangeEvent data);


    /**
     * Example of a client listener for receiving the notification CategoryCreateEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/categoryCreateEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToCategoryCreateEvent(@Valid @NotNull CategoryCreateEvent data);


    /**
     * Example of a client listener for receiving the notification CategoryDeleteEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/categoryDeleteEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToCategoryDeleteEvent(@Valid @NotNull CategoryDeleteEvent data);


    /**
     * Example of a client listener for receiving the notification CategoryStateChangeEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/categoryStateChangeEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToCategoryStateChangeEvent(@Valid @NotNull CategoryStateChangeEvent data);


    /**
     * Example of a client listener for receiving the notification ProductOfferingAttributeValueChangeEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/productOfferingAttributeValueChangeEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToProductOfferingAttributeValueChangeEvent(@Valid @NotNull ProductOfferingAttributeValueChangeEvent data);


    /**
     * Example of a client listener for receiving the notification ProductOfferingCreateEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/productOfferingCreateEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToProductOfferingCreateEvent(@Valid @NotNull ProductOfferingCreateEvent data);


    /**
     * Example of a client listener for receiving the notification ProductOfferingDeleteEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/productOfferingDeleteEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToProductOfferingDeleteEvent(@Valid @NotNull ProductOfferingDeleteEvent data);


    /**
     * Example of a client listener for receiving the notification ProductOfferingPriceAttributeValueChangeEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/productOfferingPriceAttributeValueChangeEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToProductOfferingPriceAttributeValueChangeEvent(@Valid @NotNull ProductOfferingPriceAttributeValueChangeEvent data);


    /**
     * Example of a client listener for receiving the notification ProductOfferingPriceCreateEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/productOfferingPriceCreateEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToProductOfferingPriceCreateEvent(@Valid @NotNull ProductOfferingPriceCreateEvent data);


    /**
     * Example of a client listener for receiving the notification ProductOfferingPriceDeleteEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/productOfferingPriceDeleteEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToProductOfferingPriceDeleteEvent(@Valid @NotNull ProductOfferingPriceDeleteEvent data);


    /**
     * Example of a client listener for receiving the notification ProductOfferingPriceStateChangeEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/productOfferingPriceStateChangeEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToProductOfferingPriceStateChangeEvent(@Valid @NotNull ProductOfferingPriceStateChangeEvent data);


    /**
     * Example of a client listener for receiving the notification ProductOfferingStateChangeEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/productOfferingStateChangeEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToProductOfferingStateChangeEvent(@Valid @NotNull ProductOfferingStateChangeEvent data);


    /**
     * Example of a client listener for receiving the notification ProductSpecificationAttributeValueChangeEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/productSpecificationAttributeValueChangeEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToProductSpecificationAttributeValueChangeEvent(@Valid @NotNull ProductSpecificationAttributeValueChangeEvent data);


    /**
     * Example of a client listener for receiving the notification ProductSpecificationCreateEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/productSpecificationCreateEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToProductSpecificationCreateEvent(@Valid @NotNull ProductSpecificationCreateEvent data);


    /**
     * Example of a client listener for receiving the notification ProductSpecificationDeleteEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/productSpecificationDeleteEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToProductSpecificationDeleteEvent(@Valid @NotNull ProductSpecificationDeleteEvent data);


    /**
     * Example of a client listener for receiving the notification ProductSpecificationStateChangeEvent
     *
     * @param data The event data
     * @return Notified
     * @return Bad Request
     * @return Unauthorized
     * @return Forbidden
     * @return Not Found
     * @return Method Not allowed
     * @return Conflict
     * @return Internal Server Error
     */
    @POST
    @Path("/productSpecificationStateChangeEvent")
    @Consumes({ "application/json;charset=utf-8" })
    @Produces({ "application/json;charset=utf-8" })
    Response listenToProductSpecificationStateChangeEvent(@Valid @NotNull ProductSpecificationStateChangeEvent data);

}
