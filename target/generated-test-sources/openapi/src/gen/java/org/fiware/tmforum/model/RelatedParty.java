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
 * Related Party reference. A related party defines party or party role linked to a specific entity.
 **/

@JsonTypeName("RelatedParty")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class RelatedParty   {
  private @Valid String id;
  private @Valid URI href;
  private @Valid String name;
  private @Valid String role;
  private @Valid String atBaseType;
  private @Valid URI atSchemaLocation;
  private @Valid String atType;
  private @Valid String atReferredType;

  /**
   * unique identifier
   **/
  public RelatedParty id(String id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  @NotNull
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Hyperlink reference
   **/
  public RelatedParty href(URI href) {
    this.href = href;
    return this;
  }

  
  @JsonProperty("href")
  public URI getHref() {
    return href;
  }

  @JsonProperty("href")
  public void setHref(URI href) {
    this.href = href;
  }

  /**
   * Name of the related entity.
   **/
  public RelatedParty name(String name) {
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
   * Role played by the related party
   **/
  public RelatedParty role(String role) {
    this.role = role;
    return this;
  }

  
  @JsonProperty("role")
  public String getRole() {
    return role;
  }

  @JsonProperty("role")
  public void setRole(String role) {
    this.role = role;
  }

  /**
   * When sub-classing, this defines the super-class
   **/
  public RelatedParty atBaseType(String atBaseType) {
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
  public RelatedParty atSchemaLocation(URI atSchemaLocation) {
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
  public RelatedParty atType(String atType) {
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

  /**
   * The actual type of the target instance when needed for disambiguation.
   **/
  public RelatedParty atReferredType(String atReferredType) {
    this.atReferredType = atReferredType;
    return this;
  }

  
  @JsonProperty("@referredType")
  @NotNull
  public String getAtReferredType() {
    return atReferredType;
  }

  @JsonProperty("@referredType")
  public void setAtReferredType(String atReferredType) {
    this.atReferredType = atReferredType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RelatedParty relatedParty = (RelatedParty) o;
    return Objects.equals(this.id, relatedParty.id) &&
        Objects.equals(this.href, relatedParty.href) &&
        Objects.equals(this.name, relatedParty.name) &&
        Objects.equals(this.role, relatedParty.role) &&
        Objects.equals(this.atBaseType, relatedParty.atBaseType) &&
        Objects.equals(this.atSchemaLocation, relatedParty.atSchemaLocation) &&
        Objects.equals(this.atType, relatedParty.atType) &&
        Objects.equals(this.atReferredType, relatedParty.atReferredType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, name, role, atBaseType, atSchemaLocation, atType, atReferredType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RelatedParty {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    atBaseType: ").append(toIndentedString(atBaseType)).append("\n");
    sb.append("    atSchemaLocation: ").append(toIndentedString(atSchemaLocation)).append("\n");
    sb.append("    atType: ").append(toIndentedString(atType)).append("\n");
    sb.append("    atReferredType: ").append(toIndentedString(atReferredType)).append("\n");
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

