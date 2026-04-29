package org.fiware.tmforum.model;

import java.time.OffsetDateTime;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A period of time, either as a deadline (endDateTime only) a startDateTime only, or both
 **/

@JsonTypeName("TimePeriod")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class TimePeriod   {
  private @Valid OffsetDateTime endDateTime;
  private @Valid OffsetDateTime startDateTime;

  /**
   * End of the time period, using IETC-RFC-3339 format
   **/
  public TimePeriod endDateTime(OffsetDateTime endDateTime) {
    this.endDateTime = endDateTime;
    return this;
  }

  
  @JsonProperty("endDateTime")
  public OffsetDateTime getEndDateTime() {
    return endDateTime;
  }

  @JsonProperty("endDateTime")
  public void setEndDateTime(OffsetDateTime endDateTime) {
    this.endDateTime = endDateTime;
  }

  /**
   * Start of the time period, using IETC-RFC-3339 format
   **/
  public TimePeriod startDateTime(OffsetDateTime startDateTime) {
    this.startDateTime = startDateTime;
    return this;
  }

  
  @JsonProperty("startDateTime")
  public OffsetDateTime getStartDateTime() {
    return startDateTime;
  }

  @JsonProperty("startDateTime")
  public void setStartDateTime(OffsetDateTime startDateTime) {
    this.startDateTime = startDateTime;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimePeriod timePeriod = (TimePeriod) o;
    return Objects.equals(this.endDateTime, timePeriod.endDateTime) &&
        Objects.equals(this.startDateTime, timePeriod.startDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endDateTime, startDateTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimePeriod {\n");
    
    sb.append("    endDateTime: ").append(toIndentedString(endDateTime)).append("\n");
    sb.append("    startDateTime: ").append(toIndentedString(startDateTime)).append("\n");
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

