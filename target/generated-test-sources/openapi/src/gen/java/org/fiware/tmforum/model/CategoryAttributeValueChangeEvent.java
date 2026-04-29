package org.fiware.tmforum.model;

import java.time.OffsetDateTime;
import org.fiware.tmforum.model.CategoryAttributeValueChangeEventPayload;
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

@JsonTypeName("CategoryAttributeValueChangeEvent")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class CategoryAttributeValueChangeEvent   {
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
  private @Valid CategoryAttributeValueChangeEventPayload event;

  /**
   * The identifier of the notification.
   **/
  public CategoryAttributeValueChangeEvent eventId(String eventId) {
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
  public CategoryAttributeValueChangeEvent eventTime(OffsetDateTime eventTime) {
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
  public CategoryAttributeValueChangeEvent eventType(String eventType) {
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
  public CategoryAttributeValueChangeEvent correlationId(String correlationId) {
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
  public CategoryAttributeValueChangeEvent domain(String domain) {
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
  public CategoryAttributeValueChangeEvent title(String title) {
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
  public CategoryAttributeValueChangeEvent description(String description) {
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
  public CategoryAttributeValueChangeEvent priority(String priority) {
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
  public CategoryAttributeValueChangeEvent timeOcurred(OffsetDateTime timeOcurred) {
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
  public CategoryAttributeValueChangeEvent fieldPath(String fieldPath) {
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
  public CategoryAttributeValueChangeEvent event(CategoryAttributeValueChangeEventPayload event) {
    this.event = event;
    return this;
  }

  
  @JsonProperty("event")
  public CategoryAttributeValueChangeEventPayload getEvent() {
    return event;
  }

  @JsonProperty("event")
  public void setEvent(CategoryAttributeValueChangeEventPayload event) {
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
    CategoryAttributeValueChangeEvent categoryAttributeValueChangeEvent = (CategoryAttributeValueChangeEvent) o;
    return Objects.equals(this.eventId, categoryAttributeValueChangeEvent.eventId) &&
        Objects.equals(this.eventTime, categoryAttributeValueChangeEvent.eventTime) &&
        Objects.equals(this.eventType, categoryAttributeValueChangeEvent.eventType) &&
        Objects.equals(this.correlationId, categoryAttributeValueChangeEvent.correlationId) &&
        Objects.equals(this.domain, categoryAttributeValueChangeEvent.domain) &&
        Objects.equals(this.title, categoryAttributeValueChangeEvent.title) &&
        Objects.equals(this.description, categoryAttributeValueChangeEvent.description) &&
        Objects.equals(this.priority, categoryAttributeValueChangeEvent.priority) &&
        Objects.equals(this.timeOcurred, categoryAttributeValueChangeEvent.timeOcurred) &&
        Objects.equals(this.fieldPath, categoryAttributeValueChangeEvent.fieldPath) &&
        Objects.equals(this.event, categoryAttributeValueChangeEvent.event);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, eventTime, eventType, correlationId, domain, title, description, priority, timeOcurred, fieldPath, event);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CategoryAttributeValueChangeEvent {\n");
    
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

