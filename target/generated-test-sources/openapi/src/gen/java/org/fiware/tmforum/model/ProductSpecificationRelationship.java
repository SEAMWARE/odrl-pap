package org.fiware.tmforum.model;

import java.net.URI;
import org.fiware.tmforum.model.TimePeriod;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A migration, substitution, dependency or exclusivity relationship between/among product specifications.
 **/

@JsonTypeName("ProductSpecificationRelationship")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class ProductSpecificationRelationship   {
  private @Valid String id;
  private @Valid URI href;
  private @Valid String name;
  private @Valid String relationshipType;
  private @Valid TimePeriod validFor;
  private @Valid String atBaseType;
  private @Valid URI atSchemaLocation;
  private @Valid String atType;
  private @Valid String atReferredType;

  /**
   * unique identifier
   **/
  public ProductSpecificationRelationship id(String id) {
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
   * Hyperlink reference
   **/
  public ProductSpecificationRelationship href(URI href) {
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
  public ProductSpecificationRelationship name(String name) {
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
   * Type of relationship such as migration, substitution, dependency, exclusivity
   **/
  public ProductSpecificationRelationship relationshipType(String relationshipType) {
    this.relationshipType = relationshipType;
    return this;
  }

  
  @JsonProperty("relationshipType")
  public String getRelationshipType() {
    return relationshipType;
  }

  @JsonProperty("relationshipType")
  public void setRelationshipType(String relationshipType) {
    this.relationshipType = relationshipType;
  }

  /**
   **/
  public ProductSpecificationRelationship validFor(TimePeriod validFor) {
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
  public ProductSpecificationRelationship atBaseType(String atBaseType) {
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
  public ProductSpecificationRelationship atSchemaLocation(URI atSchemaLocation) {
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
  public ProductSpecificationRelationship atType(String atType) {
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
  public ProductSpecificationRelationship atReferredType(String atReferredType) {
    this.atReferredType = atReferredType;
    return this;
  }

  
  @JsonProperty("@referredType")
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
    ProductSpecificationRelationship productSpecificationRelationship = (ProductSpecificationRelationship) o;
    return Objects.equals(this.id, productSpecificationRelationship.id) &&
        Objects.equals(this.href, productSpecificationRelationship.href) &&
        Objects.equals(this.name, productSpecificationRelationship.name) &&
        Objects.equals(this.relationshipType, productSpecificationRelationship.relationshipType) &&
        Objects.equals(this.validFor, productSpecificationRelationship.validFor) &&
        Objects.equals(this.atBaseType, productSpecificationRelationship.atBaseType) &&
        Objects.equals(this.atSchemaLocation, productSpecificationRelationship.atSchemaLocation) &&
        Objects.equals(this.atType, productSpecificationRelationship.atType) &&
        Objects.equals(this.atReferredType, productSpecificationRelationship.atReferredType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, name, relationshipType, validFor, atBaseType, atSchemaLocation, atType, atReferredType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductSpecificationRelationship {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    relationshipType: ").append(toIndentedString(relationshipType)).append("\n");
    sb.append("    validFor: ").append(toIndentedString(validFor)).append("\n");
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

