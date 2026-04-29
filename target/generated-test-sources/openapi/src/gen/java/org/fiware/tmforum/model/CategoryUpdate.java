package org.fiware.tmforum.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.fiware.tmforum.model.CategoryRef;
import org.fiware.tmforum.model.ProductOfferingRef;
import org.fiware.tmforum.model.TimePeriod;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The category resource is used to group product offerings, service and resource candidates in logical containers. Categories can contain other categories and/or product offerings, resource or service candidates. Skipped properties: id,href,lastUpdate,@type,@baseType
 **/

@JsonTypeName("Category_Update")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class CategoryUpdate   {
  private @Valid String description;
  private @Valid Boolean isRoot;
  private @Valid String lifecycleStatus;
  private @Valid String name;
  private @Valid String parentId;
  private @Valid String version;
  private @Valid List<@Valid ProductOfferingRef> productOffering;
  private @Valid List<@Valid CategoryRef> subCategory;
  private @Valid TimePeriod validFor;
  private @Valid URI atSchemaLocation;

  /**
   * Description of the category
   **/
  public CategoryUpdate description(String description) {
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
   * If true, this Boolean indicates that the category is a root of categories
   **/
  public CategoryUpdate isRoot(Boolean isRoot) {
    this.isRoot = isRoot;
    return this;
  }

  
  @JsonProperty("isRoot")
  public Boolean getIsRoot() {
    return isRoot;
  }

  @JsonProperty("isRoot")
  public void setIsRoot(Boolean isRoot) {
    this.isRoot = isRoot;
  }

  /**
   * Used to indicate the current lifecycle status
   **/
  public CategoryUpdate lifecycleStatus(String lifecycleStatus) {
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
   * Name of the category
   **/
  public CategoryUpdate name(String name) {
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
   * Unique identifier of the parent category
   **/
  public CategoryUpdate parentId(String parentId) {
    this.parentId = parentId;
    return this;
  }

  
  @JsonProperty("parentId")
  public String getParentId() {
    return parentId;
  }

  @JsonProperty("parentId")
  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  /**
   * Category version
   **/
  public CategoryUpdate version(String version) {
    this.version = version;
    return this;
  }

  
  @JsonProperty("version")
  public String getVersion() {
    return version;
  }

  @JsonProperty("version")
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * A product offering represents entities that are orderable from the provider of the catalog, this resource includes pricing information.
   **/
  public CategoryUpdate productOffering(List<@Valid ProductOfferingRef> productOffering) {
    this.productOffering = productOffering;
    return this;
  }

  
  @JsonProperty("productOffering")
  public List<ProductOfferingRef> getProductOffering() {
    return productOffering;
  }

  @JsonProperty("productOffering")
  public void setProductOffering(List<@Valid ProductOfferingRef> productOffering) {
    this.productOffering = productOffering;
  }

  public CategoryUpdate addProductOfferingItem(ProductOfferingRef productOfferingItem) {
    if (this.productOffering == null) {
      this.productOffering = new ArrayList<>();
    }

    this.productOffering.add(productOfferingItem);
    return this;
  }

  public CategoryUpdate removeProductOfferingItem(ProductOfferingRef productOfferingItem) {
    if (productOfferingItem != null && this.productOffering != null) {
      this.productOffering.remove(productOfferingItem);
    }

    return this;
  }
  /**
   * The category resource is used to group product offerings, service and resource candidates in logical containers. Categories can contain other (sub-)categories and/or product offerings.
   **/
  public CategoryUpdate subCategory(List<@Valid CategoryRef> subCategory) {
    this.subCategory = subCategory;
    return this;
  }

  
  @JsonProperty("subCategory")
  public List<CategoryRef> getSubCategory() {
    return subCategory;
  }

  @JsonProperty("subCategory")
  public void setSubCategory(List<@Valid CategoryRef> subCategory) {
    this.subCategory = subCategory;
  }

  public CategoryUpdate addSubCategoryItem(CategoryRef subCategoryItem) {
    if (this.subCategory == null) {
      this.subCategory = new ArrayList<>();
    }

    this.subCategory.add(subCategoryItem);
    return this;
  }

  public CategoryUpdate removeSubCategoryItem(CategoryRef subCategoryItem) {
    if (subCategoryItem != null && this.subCategory != null) {
      this.subCategory.remove(subCategoryItem);
    }

    return this;
  }
  /**
   **/
  public CategoryUpdate validFor(TimePeriod validFor) {
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
   * A URI to a JSON-Schema file that defines additional attributes and relationships
   **/
  public CategoryUpdate atSchemaLocation(URI atSchemaLocation) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CategoryUpdate categoryUpdate = (CategoryUpdate) o;
    return Objects.equals(this.description, categoryUpdate.description) &&
        Objects.equals(this.isRoot, categoryUpdate.isRoot) &&
        Objects.equals(this.lifecycleStatus, categoryUpdate.lifecycleStatus) &&
        Objects.equals(this.name, categoryUpdate.name) &&
        Objects.equals(this.parentId, categoryUpdate.parentId) &&
        Objects.equals(this.version, categoryUpdate.version) &&
        Objects.equals(this.productOffering, categoryUpdate.productOffering) &&
        Objects.equals(this.subCategory, categoryUpdate.subCategory) &&
        Objects.equals(this.validFor, categoryUpdate.validFor) &&
        Objects.equals(this.atSchemaLocation, categoryUpdate.atSchemaLocation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, isRoot, lifecycleStatus, name, parentId, version, productOffering, subCategory, validFor, atSchemaLocation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CategoryUpdate {\n");
    
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    isRoot: ").append(toIndentedString(isRoot)).append("\n");
    sb.append("    lifecycleStatus: ").append(toIndentedString(lifecycleStatus)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    productOffering: ").append(toIndentedString(productOffering)).append("\n");
    sb.append("    subCategory: ").append(toIndentedString(subCategory)).append("\n");
    sb.append("    validFor: ").append(toIndentedString(validFor)).append("\n");
    sb.append("    atSchemaLocation: ").append(toIndentedString(atSchemaLocation)).append("\n");
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

