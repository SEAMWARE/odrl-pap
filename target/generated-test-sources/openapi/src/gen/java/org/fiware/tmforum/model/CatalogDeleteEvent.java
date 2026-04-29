package org.fiware.tmforum.model;

import java.time.OffsetDateTime;
import org.fiware.tmforum.model.CatalogDeleteEventPayload;
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

@JsonTypeName("CatalogDeleteEvent")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class CatalogDeleteEvent   {
  private @Valid CatalogDeleteEventPayload event;
  private @Valid String eventId;
  private @Valid OffsetDateTime eventTime;
  private @Valid String eventType;
  private @Valid String correlationId;
  private @Valid String domain;
  private @Valid String title;
  private @Valid String description;
  private @Valid String priority;
  private @Valid OffsetDateTime timeOcurred;

  /**
   **/
  public CatalogDeleteEvent event(CatalogDeleteEventPayload event) {
    this.event = event;
    return this;
  }

  
  @JsonProperty("event")
  public CatalogDeleteEventPayload getEvent() {
    return event;
  }

  @JsonProperty("event")
  public void setEvent(CatalogDeleteEventPayload event) {
    this.event = event;
  }

  /**
   * The identifier of the notification.
   **/
  public CatalogDeleteEvent eventId(String eventId) {
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
  public CatalogDeleteEvent eventTime(OffsetDateTime eventTime) {
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
  public CatalogDeleteEvent eventType(String eventType) {
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
  public CatalogDeleteEvent correlationId(String correlationId) {
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
  public CatalogDeleteEvent domain(String domain) {
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
  public CatalogDeleteEvent title(String title) {
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
  public CatalogDeleteEvent description(String description) {
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
  public CatalogDeleteEvent priority(String priority) {
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
  public CatalogDeleteEvent timeOcurred(OffsetDateTime timeOcurred) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CatalogDeleteEvent catalogDeleteEvent = (CatalogDeleteEvent) o;
    return Objects.equals(this.event, catalogDeleteEvent.event) &&
        Objects.equals(this.eventId, catalogDeleteEvent.eventId) &&
        Objects.equals(this.eventTime, catalogDeleteEvent.eventTime) &&
        Objects.equals(this.eventType, catalogDeleteEvent.eventType) &&
        Objects.equals(this.correlationId, catalogDeleteEvent.correlationId) &&
        Objects.equals(this.domain, catalogDeleteEvent.domain) &&
        Objects.equals(this.title, catalogDeleteEvent.title) &&
        Objects.equals(this.description, catalogDeleteEvent.description) &&
        Objects.equals(this.priority, catalogDeleteEvent.priority) &&
        Objects.equals(this.timeOcurred, catalogDeleteEvent.timeOcurred);
  }

  @Override
  public int hashCode() {
    return Objects.hash(event, eventId, eventTime, eventType, correlationId, domain, title, description, priority, timeOcurred);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CatalogDeleteEvent {\n");
    
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    eventTime: ").append(toIndentedString(eventTime)).append("\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    correlationId: ").append(toIndentedString(correlationId)).append("\n");
    sb.append("    domain: ").append(toIndentedString(domain)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    timeOcurred: ").append(toIndentedString(timeOcurred)).append("\n");
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

