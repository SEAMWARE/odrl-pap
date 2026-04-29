package org.fiware.tmforum.model;

import java.net.URI;
import org.fiware.tmforum.model.BundledProductOfferingOption;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A type of ProductOffering that belongs to a grouping of ProductOfferings made available to the market. It inherits of all attributes of ProductOffering.
 **/

@JsonTypeName("BundledProductOffering")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class BundledProductOffering   {
  private @Valid String id;
  private @Valid String href;
  private @Valid String lifecycleStatus;
  private @Valid String name;
  private @Valid BundledProductOfferingOption bundledProductOfferingOption;
  private @Valid String atBaseType;
  private @Valid URI atSchemaLocation;
  private @Valid String atType;

  /**
   * Unique identifier of the BundledProductOffering
   **/
  public BundledProductOffering id(String id) {
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
   * Unique reference of the BundledProductOffering
   **/
  public BundledProductOffering href(String href) {
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
  public BundledProductOffering lifecycleStatus(String lifecycleStatus) {
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
   * Name of the BundledProductOffering
   **/
  public BundledProductOffering name(String name) {
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
  public BundledProductOffering bundledProductOfferingOption(BundledProductOfferingOption bundledProductOfferingOption) {
    this.bundledProductOfferingOption = bundledProductOfferingOption;
    return this;
  }

  
  @JsonProperty("bundledProductOfferingOption")
  public BundledProductOfferingOption getBundledProductOfferingOption() {
    return bundledProductOfferingOption;
  }

  @JsonProperty("bundledProductOfferingOption")
  public void setBundledProductOfferingOption(BundledProductOfferingOption bundledProductOfferingOption) {
    this.bundledProductOfferingOption = bundledProductOfferingOption;
  }

  /**
   * When sub-classing, this defines the super-class
   **/
  public BundledProductOffering atBaseType(String atBaseType) {
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
  public BundledProductOffering atSchemaLocation(URI atSchemaLocation) {
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
  public BundledProductOffering atType(String atType) {
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
    BundledProductOffering bundledProductOffering = (BundledProductOffering) o;
    return Objects.equals(this.id, bundledProductOffering.id) &&
        Objects.equals(this.href, bundledProductOffering.href) &&
        Objects.equals(this.lifecycleStatus, bundledProductOffering.lifecycleStatus) &&
        Objects.equals(this.name, bundledProductOffering.name) &&
        Objects.equals(this.bundledProductOfferingOption, bundledProductOffering.bundledProductOfferingOption) &&
        Objects.equals(this.atBaseType, bundledProductOffering.atBaseType) &&
        Objects.equals(this.atSchemaLocation, bundledProductOffering.atSchemaLocation) &&
        Objects.equals(this.atType, bundledProductOffering.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, lifecycleStatus, name, bundledProductOfferingOption, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BundledProductOffering {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    lifecycleStatus: ").append(toIndentedString(lifecycleStatus)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    bundledProductOfferingOption: ").append(toIndentedString(bundledProductOfferingOption)).append("\n");
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

