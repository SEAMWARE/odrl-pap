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
 * This represents a bundling pricing relationship, allowing a price to be composed of multiple other prices (e.g. a recurring charge and a onetime charge).
 **/

@JsonTypeName("BundledProductOfferingPriceRelationship")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class BundledProductOfferingPriceRelationship   {
  private @Valid String id;
  private @Valid String href;
  private @Valid String name;
  private @Valid String atBaseType;
  private @Valid URI atSchemaLocation;
  private @Valid String atType;

  /**
   * Unique identifier of the bundled product offering price
   **/
  public BundledProductOfferingPriceRelationship id(String id) {
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
   * hyperlink reference of the bundled product offering price
   **/
  public BundledProductOfferingPriceRelationship href(String href) {
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
   * Name of the bundled product offering price
   **/
  public BundledProductOfferingPriceRelationship name(String name) {
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
  public BundledProductOfferingPriceRelationship atBaseType(String atBaseType) {
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
  public BundledProductOfferingPriceRelationship atSchemaLocation(URI atSchemaLocation) {
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
  public BundledProductOfferingPriceRelationship atType(String atType) {
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
    BundledProductOfferingPriceRelationship bundledProductOfferingPriceRelationship = (BundledProductOfferingPriceRelationship) o;
    return Objects.equals(this.id, bundledProductOfferingPriceRelationship.id) &&
        Objects.equals(this.href, bundledProductOfferingPriceRelationship.href) &&
        Objects.equals(this.name, bundledProductOfferingPriceRelationship.name) &&
        Objects.equals(this.atBaseType, bundledProductOfferingPriceRelationship.atBaseType) &&
        Objects.equals(this.atSchemaLocation, bundledProductOfferingPriceRelationship.atSchemaLocation) &&
        Objects.equals(this.atType, bundledProductOfferingPriceRelationship.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, name, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BundledProductOfferingPriceRelationship {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
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

