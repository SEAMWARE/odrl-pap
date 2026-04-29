package org.fiware.tmforum.model;

import java.net.URI;
import org.fiware.tmforum.model.Duration;
import org.fiware.tmforum.model.TimePeriod;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A condition under which a ProductOffering is made available to Customers. For instance, a productOffering can be offered with multiple commitment periods.
 **/

@JsonTypeName("ProductOfferingTerm")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class ProductOfferingTerm   {
  private @Valid String description;
  private @Valid String name;
  private @Valid Duration duration;
  private @Valid TimePeriod validFor;
  private @Valid String atBaseType;
  private @Valid URI atSchemaLocation;
  private @Valid String atType;

  /**
   * Description of the productOfferingTerm
   **/
  public ProductOfferingTerm description(String description) {
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
   * Name of the productOfferingTerm
   **/
  public ProductOfferingTerm name(String name) {
    this.name = name;
    return this;
  }

  
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  public ProductOfferingTerm duration(Duration duration) {
    this.duration = duration;
    return this;
  }

  
  @JsonProperty("duration")
  public Duration getDuration() {
    return duration;
  }

  @JsonProperty("duration")
  public void setDuration(Duration duration) {
    this.duration = duration;
  }

  /**
   **/
  public ProductOfferingTerm validFor(TimePeriod validFor) {
    this.validFor = validFor;
    return this;
  }

  
  @JsonProperty("validFor")
  public TimePeriod getValidFor() {
    return validFor;
  }

  @JsonProperty("validFor")
  public void setValidFor(TimePeriod validFor) {
    this.validFor = validFor;
  }

  /**
   * When sub-classing, this defines the super-class
   **/
  public ProductOfferingTerm atBaseType(String atBaseType) {
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
  public ProductOfferingTerm atSchemaLocation(URI atSchemaLocation) {
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
   * When sub-classing, this defines the sub-class Extensible name
   **/
  public ProductOfferingTerm atType(String atType) {
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
    ProductOfferingTerm productOfferingTerm = (ProductOfferingTerm) o;
    return Objects.equals(this.description, productOfferingTerm.description) &&
        Objects.equals(this.name, productOfferingTerm.name) &&
        Objects.equals(this.duration, productOfferingTerm.duration) &&
        Objects.equals(this.validFor, productOfferingTerm.validFor) &&
        Objects.equals(this.atBaseType, productOfferingTerm.atBaseType) &&
        Objects.equals(this.atSchemaLocation, productOfferingTerm.atSchemaLocation) &&
        Objects.equals(this.atType, productOfferingTerm.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, name, duration, validFor, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductOfferingTerm {\n");
    
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    validFor: ").append(toIndentedString(validFor)).append("\n");
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

