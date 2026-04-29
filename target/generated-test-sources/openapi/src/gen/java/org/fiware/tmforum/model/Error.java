package org.fiware.tmforum.model;

import java.net.URI;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Used when an API throws an Error, typically with a HTTP error response-code (3xx, 4xx, 5xx)
 **/

@JsonTypeName("Error")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class Error   {
  private @Valid String code;
  private @Valid String reason;
  private @Valid String message;
  private @Valid String status;
  private @Valid URI referenceError;
  private @Valid String atBaseType;
  private @Valid URI atSchemaLocation;
  private @Valid String atType;

  /**
   * Application relevant detail, defined in the API or a common list.
   **/
  public Error code(String code) {
    this.code = code;
    return this;
  }

  
  @JsonProperty("code")
  @NotNull
  public String getCode() {
    return code;
  }

  @JsonProperty("code")
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Explanation of the reason for the error which can be shown to a client user.
   **/
  public Error reason(String reason) {
    this.reason = reason;
    return this;
  }

  
  @JsonProperty("reason")
  @NotNull
  public String getReason() {
    return reason;
  }

  @JsonProperty("reason")
  public void setReason(String reason) {
    this.reason = reason;
  }

  /**
   * More details and corrective actions related to the error which can be shown to a client user.
   **/
  public Error message(String message) {
    this.message = message;
    return this;
  }

  
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  @JsonProperty("message")
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * HTTP Error code extension
   **/
  public Error status(String status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * URI of documentation describing the error.
   **/
  public Error referenceError(URI referenceError) {
    this.referenceError = referenceError;
    return this;
  }

  
  @JsonProperty("referenceError")
  public URI getReferenceError() {
    return referenceError;
  }

  @JsonProperty("referenceError")
  public void setReferenceError(URI referenceError) {
    this.referenceError = referenceError;
  }

  /**
   * When sub-classing, this defines the super-class.
   **/
  public Error atBaseType(String atBaseType) {
    this.atBaseType = atBaseType;
    return this;
  }

  
  @JsonProperty("@baseType")
  public String getAtBaseType() {
    return atBaseType;
  }

  @JsonProperty("@baseType")
  public void setAtBaseType(String atBaseType) {
    this.atBaseType = atBaseType;
  }

  /**
   * A URI to a JSON-Schema file that defines additional attributes and relationships
   **/
  public Error atSchemaLocation(URI atSchemaLocation) {
    this.atSchemaLocation = atSchemaLocation;
    return this;
  }

  
  @JsonProperty("@schemaLocation")
  public URI getAtSchemaLocation() {
    return atSchemaLocation;
  }

  @JsonProperty("@schemaLocation")
  public void setAtSchemaLocation(URI atSchemaLocation) {
    this.atSchemaLocation = atSchemaLocation;
  }

  /**
   * When sub-classing, this defines the sub-class entity name.
   **/
  public Error atType(String atType) {
    this.atType = atType;
    return this;
  }

  
  @JsonProperty("@type")
  public String getAtType() {
    return atType;
  }

  @JsonProperty("@type")
  public void setAtType(String atType) {
    this.atType = atType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Error error = (Error) o;
    return Objects.equals(this.code, error.code) &&
        Objects.equals(this.reason, error.reason) &&
        Objects.equals(this.message, error.message) &&
        Objects.equals(this.status, error.status) &&
        Objects.equals(this.referenceError, error.referenceError) &&
        Objects.equals(this.atBaseType, error.atBaseType) &&
        Objects.equals(this.atSchemaLocation, error.atSchemaLocation) &&
        Objects.equals(this.atType, error.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, reason, message, status, referenceError, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Error {\n");
    
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    referenceError: ").append(toIndentedString(referenceError)).append("\n");
    sb.append("    atBaseType: ").append(toIndentedString(atBaseType)).append("\n");
    sb.append("    atSchemaLocation: ").append(toIndentedString(atSchemaLocation)).append("\n");
    sb.append("    atType: ").append(toIndentedString(atType)).append("\n");
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

