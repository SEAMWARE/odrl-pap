package org.fiware.tmforum.model;

import java.time.OffsetDateTime;
import org.fiware.tmforum.model.ProductOfferingAttributeValueChangeEventPayload;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The notification data structure
 **/

@JsonTypeName("ProductOfferingAttributeValueChangeEvent")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class ProductOfferingAttributeValueChangeEvent   {
  private @Valid String eventId;
  private @Valid OffsetDateTime eventTime;
  private @Valid String eventType;
  private @Valid String correlationId;
  private @Valid String domain;
  private @Valid String title;
  private @Valid String description;
  private @Valid String priority;
  private @Valid OffsetDateTime timeOcurred;
  private @Valid String fieldPath;
  private @Valid ProductOfferingAttributeValueChangeEventPayload event;

  /**
   * The identifier of the notification.
   **/
  public ProductOfferingAttributeValueChangeEvent eventId(String eventId) {
    this.eventId = eventId;
    return this;
  }

  
  @JsonProperty("eventId")
  public String getEventId() {
    return eventId;
  }

  @JsonProperty("eventId")
  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  /**
   * Time of the event occurrence.
   **/
  public ProductOfferingAttributeValueChangeEvent eventTime(OffsetDateTime eventTime) {
    this.eventTime = eventTime;
    return this;
  }

  
  @JsonProperty("eventTime")
  public OffsetDateTime getEventTime() {
    return eventTime;
  }

  @JsonProperty("eventTime")
  public void setEventTime(OffsetDateTime eventTime) {
    this.eventTime = eventTime;
  }

  /**
   * The type of the notification.
   **/
  public ProductOfferingAttributeValueChangeEvent eventType(String eventType) {
    this.eventType = eventType;
    return this;
  }

  
  @JsonProperty("eventType")
  public String getEventType() {
    return eventType;
  }

  @JsonProperty("eventType")
  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  /**
   * The correlation id for this event.
   **/
  public ProductOfferingAttributeValueChangeEvent correlationId(String correlationId) {
    this.correlationId = correlationId;
    return this;
  }

  
  @JsonProperty("correlationId")
  public String getCorrelationId() {
    return correlationId;
  }

  @JsonProperty("correlationId")
  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  /**
   * The domain of the event.
   **/
  public ProductOfferingAttributeValueChangeEvent domain(String domain) {
    this.domain = domain;
    return this;
  }

  
  @JsonProperty("domain")
  public String getDomain() {
    return domain;
  }

  @JsonProperty("domain")
  public void setDomain(String domain) {
    this.domain = domain;
  }

  /**
   * The title of the event.
   **/
  public ProductOfferingAttributeValueChangeEvent title(String title) {
    this.title = title;
    return this;
  }

  
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  @JsonProperty("title")
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * An explnatory of the event.
   **/
  public ProductOfferingAttributeValueChangeEvent description(String description) {
    this.description = description;
    return this;
  }

  
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * A priority.
   **/
  public ProductOfferingAttributeValueChangeEvent priority(String priority) {
    this.priority = priority;
    return this;
  }

  
  @JsonProperty("priority")
  public String getPriority() {
    return priority;
  }

  @JsonProperty("priority")
  public void setPriority(String priority) {
    this.priority = priority;
  }

  /**
   * The time the event occured.
   **/
  public ProductOfferingAttributeValueChangeEvent timeOcurred(OffsetDateTime timeOcurred) {
    this.timeOcurred = timeOcurred;
    return this;
  }

  
  @JsonProperty("timeOcurred")
  public OffsetDateTime getTimeOcurred() {
    return timeOcurred;
  }

  @JsonProperty("timeOcurred")
  public void setTimeOcurred(OffsetDateTime timeOcurred) {
    this.timeOcurred = timeOcurred;
  }

  /**
   * The path identifying the object field concerned by this notification.
   **/
  public ProductOfferingAttributeValueChangeEvent fieldPath(String fieldPath) {
    this.fieldPath = fieldPath;
    return this;
  }

  
  @JsonProperty("fieldPath")
  public String getFieldPath() {
    return fieldPath;
  }

  @JsonProperty("fieldPath")
  public void setFieldPath(String fieldPath) {
    this.fieldPath = fieldPath;
  }

  /**
   **/
  public ProductOfferingAttributeValueChangeEvent event(ProductOfferingAttributeValueChangeEventPayload event) {
    this.event = event;
    return this;
  }

  
  @JsonProperty("event")
  public ProductOfferingAttributeValueChangeEventPayload getEvent() {
    return event;
  }

  @JsonProperty("event")
  public void setEvent(ProductOfferingAttributeValueChangeEventPayload event) {
    this.event = event;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductOfferingAttributeValueChangeEvent productOfferingAttributeValueChangeEvent = (ProductOfferingAttributeValueChangeEvent) o;
    return Objects.equals(this.eventId, productOfferingAttributeValueChangeEvent.eventId) &&
        Objects.equals(this.eventTime, productOfferingAttributeValueChangeEvent.eventTime) &&
        Objects.equals(this.eventType, productOfferingAttributeValueChangeEvent.eventType) &&
        Objects.equals(this.correlationId, productOfferingAttributeValueChangeEvent.correlationId) &&
        Objects.equals(this.domain, productOfferingAttributeValueChangeEvent.domain) &&
        Objects.equals(this.title, productOfferingAttributeValueChangeEvent.title) &&
        Objects.equals(this.description, productOfferingAttributeValueChangeEvent.description) &&
        Objects.equals(this.priority, productOfferingAttributeValueChangeEvent.priority) &&
        Objects.equals(this.timeOcurred, productOfferingAttributeValueChangeEvent.timeOcurred) &&
        Objects.equals(this.fieldPath, productOfferingAttributeValueChangeEvent.fieldPath) &&
        Objects.equals(this.event, productOfferingAttributeValueChangeEvent.event);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, eventTime, eventType, correlationId, domain, title, description, priority, timeOcurred, fieldPath, event);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductOfferingAttributeValueChangeEvent {\n");
    
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    eventTime: ").append(toIndentedString(eventTime)).append("\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    correlationId: ").append(toIndentedString(correlationId)).append("\n");
    sb.append("    domain: ").append(toIndentedString(domain)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    timeOcurred: ").append(toIndentedString(timeOcurred)).append("\n");
    sb.append("    fieldPath: ").append(toIndentedString(fieldPath)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


}

