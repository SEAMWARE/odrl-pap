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
 * A type of ProductSpecification that belongs to a grouping of ProductSpecifications made available to the market. It inherits of all attributes of ProductSpecification.
 **/

@JsonTypeName("BundledProductSpecification")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class BundledProductSpecification   {
  private @Valid String id;
  private @Valid String href;
  private @Valid String lifecycleStatus;
  private @Valid String name;
  private @Valid String atBaseType;
  private @Valid URI atSchemaLocation;
  private @Valid String atType;

  /**
   * Unique identifier of the product specification
   **/
  public BundledProductSpecification id(String id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Reference of the product specification
   **/
  public BundledProductSpecification href(String href) {
    this.href = href;
    return this;
  }

  
  @JsonProperty("href")
  public String getHref() {
    return href;
  }

  @JsonProperty("href")
  public void setHref(String href) {
    this.href = href;
  }

  /**
   * Used to indicate the current lifecycle status
   **/
  public BundledProductSpecification lifecycleStatus(String lifecycleStatus) {
    this.lifecycleStatus = lifecycleStatus;
    return this;
  }

  
  @JsonProperty("lifecycleStatus")
  public String getLifecycleStatus() {
    return lifecycleStatus;
  }

  @JsonProperty("lifecycleStatus")
  public void setLifecycleStatus(String lifecycleStatus) {
    this.lifecycleStatus = lifecycleStatus;
  }

  /**
   * Name of the product specification
   **/
  public BundledProductSpecification name(String name) {
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
   * When sub-classing, this defines the super-class
   **/
  public BundledProductSpecification atBaseType(String atBaseType) {
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
  public BundledProductSpecification atSchemaLocation(URI atSchemaLocation) {
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
  public BundledProductSpecification atType(String atType) {
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
    BundledProductSpecification bundledProductSpecification = (BundledProductSpecification) o;
    return Objects.equals(this.id, bundledProductSpecification.id) &&
        Objects.equals(this.href, bundledProductSpecification.href) &&
        Objects.equals(this.lifecycleStatus, bundledProductSpecification.lifecycleStatus) &&
        Objects.equals(this.name, bundledProductSpecification.name) &&
        Objects.equals(this.atBaseType, bundledProductSpecification.atBaseType) &&
        Objects.equals(this.atSchemaLocation, bundledProductSpecification.atSchemaLocation) &&
        Objects.equals(this.atType, bundledProductSpecification.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, lifecycleStatus, name, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BundledProductSpecification {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    lifecycleStatus: ").append(toIndentedString(lifecycleStatus)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

