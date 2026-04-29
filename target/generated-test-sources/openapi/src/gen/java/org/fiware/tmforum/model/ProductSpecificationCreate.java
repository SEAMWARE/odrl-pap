package org.fiware.tmforum.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.fiware.tmforum.model.AttachmentRefOrValue;
import org.fiware.tmforum.model.BundledProductSpecification;
import org.fiware.tmforum.model.ProductSpecificationCharacteristic;
import org.fiware.tmforum.model.ProductSpecificationRelationship;
import org.fiware.tmforum.model.RelatedParty;
import org.fiware.tmforum.model.ResourceSpecificationRef;
import org.fiware.tmforum.model.ServiceSpecificationRef;
import org.fiware.tmforum.model.TargetProductSchema;
import org.fiware.tmforum.model.TimePeriod;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Is a detailed description of a tangible or intangible object made available externally in the form of a ProductOffering to customers or other parties playing a party role. Skipped properties: id,href
 **/

@JsonTypeName("ProductSpecification_Create")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class ProductSpecificationCreate   {
  private @Valid String brand;
  private @Valid String description;
  private @Valid Boolean isBundle;
  private @Valid OffsetDateTime lastUpdate;
  private @Valid String lifecycleStatus;
  private @Valid String name;
  private @Valid String productNumber;
  private @Valid String version;
  private @Valid List<@Valid AttachmentRefOrValue> attachment;
  private @Valid List<@Valid BundledProductSpecification> bundledProductSpecification;
  private @Valid List<@Valid ProductSpecificationCharacteristic> productSpecCharacteristic;
  private @Valid List<@Valid ProductSpecificationRelationship> productSpecificationRelationship;
  private @Valid List<@Valid RelatedParty> relatedParty;
  private @Valid List<@Valid ResourceSpecificationRef> resourceSpecification;
  private @Valid List<@Valid ServiceSpecificationRef> serviceSpecification;
  private @Valid TargetProductSchema targetProductSchema;
  private @Valid TimePeriod validFor;
  private @Valid String atBaseType;
  private @Valid URI atSchemaLocation;
  private @Valid String atType;

  /**
   * The manufacturer or trademark of the specification
   **/
  public ProductSpecificationCreate brand(String brand) {
    this.brand = brand;
    return this;
  }

  
  @JsonProperty("brand")
  public String getBrand() {
    return brand;
  }

  @JsonProperty("brand")
  public void setBrand(String brand) {
    this.brand = brand;
  }

  /**
   * A narrative that explains in detail what the product specification is
   **/
  public ProductSpecificationCreate description(String description) {
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
   * isBundle determines whether a productSpecification represents a single productSpecification (false), or a bundle of productSpecification (true).
   **/
  public ProductSpecificationCreate isBundle(Boolean isBundle) {
    this.isBundle = isBundle;
    return this;
  }

  
  @JsonProperty("isBundle")
  public Boolean getIsBundle() {
    return isBundle;
  }

  @JsonProperty("isBundle")
  public void setIsBundle(Boolean isBundle) {
    this.isBundle = isBundle;
  }

  /**
   * Date and time of the last update
   **/
  public ProductSpecificationCreate lastUpdate(OffsetDateTime lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }

  
  @JsonProperty("lastUpdate")
  public OffsetDateTime getLastUpdate() {
    return lastUpdate;
  }

  @JsonProperty("lastUpdate")
  public void setLastUpdate(OffsetDateTime lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  /**
   * Used to indicate the current lifecycle status
   **/
  public ProductSpecificationCreate lifecycleStatus(String lifecycleStatus) {
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
  public ProductSpecificationCreate name(String name) {
    this.name = name;
    return this;
  }

  
  @JsonProperty("name")
  @NotNull
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  /**
   * An identification number assigned to uniquely identity the specification
   **/
  public ProductSpecificationCreate productNumber(String productNumber) {
    this.productNumber = productNumber;
    return this;
  }

  
  @JsonProperty("productNumber")
  public String getProductNumber() {
    return productNumber;
  }

  @JsonProperty("productNumber")
  public void setProductNumber(String productNumber) {
    this.productNumber = productNumber;
  }

  /**
   * Product specification version
   **/
  public ProductSpecificationCreate version(String version) {
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
   * Complements the description of an element (for instance a product) through video, pictures...
   **/
  public ProductSpecificationCreate attachment(List<@Valid AttachmentRefOrValue> attachment) {
    this.attachment = attachment;
    return this;
  }

  
  @JsonProperty("attachment")
  public List<AttachmentRefOrValue> getAttachment() {
    return attachment;
  }

  @JsonProperty("attachment")
  public void setAttachment(List<@Valid AttachmentRefOrValue> attachment) {
    this.attachment = attachment;
  }

  public ProductSpecificationCreate addAttachmentItem(AttachmentRefOrValue attachmentItem) {
    if (this.attachment == null) {
      this.attachment = new ArrayList<>();
    }

    this.attachment.add(attachmentItem);
    return this;
  }

  public ProductSpecificationCreate removeAttachmentItem(AttachmentRefOrValue attachmentItem) {
    if (attachmentItem != null && this.attachment != null) {
      this.attachment.remove(attachmentItem);
    }

    return this;
  }
  /**
   * A type of ProductSpecification that belongs to a grouping of ProductSpecifications made available to the market. It inherits of all attributes of ProductSpecification.
   **/
  public ProductSpecificationCreate bundledProductSpecification(List<@Valid BundledProductSpecification> bundledProductSpecification) {
    this.bundledProductSpecification = bundledProductSpecification;
    return this;
  }

  
  @JsonProperty("bundledProductSpecification")
  public List<BundledProductSpecification> getBundledProductSpecification() {
    return bundledProductSpecification;
  }

  @JsonProperty("bundledProductSpecification")
  public void setBundledProductSpecification(List<@Valid BundledProductSpecification> bundledProductSpecification) {
    this.bundledProductSpecification = bundledProductSpecification;
  }

  public ProductSpecificationCreate addBundledProductSpecificationItem(BundledProductSpecification bundledProductSpecificationItem) {
    if (this.bundledProductSpecification == null) {
      this.bundledProductSpecification = new ArrayList<>();
    }

    this.bundledProductSpecification.add(bundledProductSpecificationItem);
    return this;
  }

  public ProductSpecificationCreate removeBundledProductSpecificationItem(BundledProductSpecification bundledProductSpecificationItem) {
    if (bundledProductSpecificationItem != null && this.bundledProductSpecification != null) {
      this.bundledProductSpecification.remove(bundledProductSpecificationItem);
    }

    return this;
  }
  /**
   * A characteristic quality or distinctive feature of a ProductSpecification.  The characteristic can be take on a discrete value, such as color, can take on a range of values, (for example, sensitivity of 100-240 mV), or can be derived from a formula (for example, usage time (hrs) &#x3D; 30 - talk time *3). Certain characteristics, such as color, may be configured during the ordering or some other process.
   **/
  public ProductSpecificationCreate productSpecCharacteristic(List<@Valid ProductSpecificationCharacteristic> productSpecCharacteristic) {
    this.productSpecCharacteristic = productSpecCharacteristic;
    return this;
  }

  
  @JsonProperty("productSpecCharacteristic")
  public List<ProductSpecificationCharacteristic> getProductSpecCharacteristic() {
    return productSpecCharacteristic;
  }

  @JsonProperty("productSpecCharacteristic")
  public void setProductSpecCharacteristic(List<@Valid ProductSpecificationCharacteristic> productSpecCharacteristic) {
    this.productSpecCharacteristic = productSpecCharacteristic;
  }

  public ProductSpecificationCreate addProductSpecCharacteristicItem(ProductSpecificationCharacteristic productSpecCharacteristicItem) {
    if (this.productSpecCharacteristic == null) {
      this.productSpecCharacteristic = new ArrayList<>();
    }

    this.productSpecCharacteristic.add(productSpecCharacteristicItem);
    return this;
  }

  public ProductSpecificationCreate removeProductSpecCharacteristicItem(ProductSpecificationCharacteristic productSpecCharacteristicItem) {
    if (productSpecCharacteristicItem != null && this.productSpecCharacteristic != null) {
      this.productSpecCharacteristic.remove(productSpecCharacteristicItem);
    }

    return this;
  }
  /**
   * A migration, substitution, dependency or exclusivity relationship between/among product specifications.
   **/
  public ProductSpecificationCreate productSpecificationRelationship(List<@Valid ProductSpecificationRelationship> productSpecificationRelationship) {
    this.productSpecificationRelationship = productSpecificationRelationship;
    return this;
  }

  
  @JsonProperty("productSpecificationRelationship")
  public List<ProductSpecificationRelationship> getProductSpecificationRelationship() {
    return productSpecificationRelationship;
  }

  @JsonProperty("productSpecificationRelationship")
  public void setProductSpecificationRelationship(List<@Valid ProductSpecificationRelationship> productSpecificationRelationship) {
    this.productSpecificationRelationship = productSpecificationRelationship;
  }

  public ProductSpecificationCreate addProductSpecificationRelationshipItem(ProductSpecificationRelationship productSpecificationRelationshipItem) {
    if (this.productSpecificationRelationship == null) {
      this.productSpecificationRelationship = new ArrayList<>();
    }

    this.productSpecificationRelationship.add(productSpecificationRelationshipItem);
    return this;
  }

  public ProductSpecificationCreate removeProductSpecificationRelationshipItem(ProductSpecificationRelationship productSpecificationRelationshipItem) {
    if (productSpecificationRelationshipItem != null && this.productSpecificationRelationship != null) {
      this.productSpecificationRelationship.remove(productSpecificationRelationshipItem);
    }

    return this;
  }
  /**
   * A related party defines party or party role linked to a specific entity.
   **/
  public ProductSpecificationCreate relatedParty(List<@Valid RelatedParty> relatedParty) {
    this.relatedParty = relatedParty;
    return this;
  }

  
  @JsonProperty("relatedParty")
  public List<RelatedParty> getRelatedParty() {
    return relatedParty;
  }

  @JsonProperty("relatedParty")
  public void setRelatedParty(List<@Valid RelatedParty> relatedParty) {
    this.relatedParty = relatedParty;
  }

  public ProductSpecificationCreate addRelatedPartyItem(RelatedParty relatedPartyItem) {
    if (this.relatedParty == null) {
      this.relatedParty = new ArrayList<>();
    }

    this.relatedParty.add(relatedPartyItem);
    return this;
  }

  public ProductSpecificationCreate removeRelatedPartyItem(RelatedParty relatedPartyItem) {
    if (relatedPartyItem != null && this.relatedParty != null) {
      this.relatedParty.remove(relatedPartyItem);
    }

    return this;
  }
  /**
   * The ResourceSpecification is required to realize a ProductSpecification.
   **/
  public ProductSpecificationCreate resourceSpecification(List<@Valid ResourceSpecificationRef> resourceSpecification) {
    this.resourceSpecification = resourceSpecification;
    return this;
  }

  
  @JsonProperty("resourceSpecification")
  public List<ResourceSpecificationRef> getResourceSpecification() {
    return resourceSpecification;
  }

  @JsonProperty("resourceSpecification")
  public void setResourceSpecification(List<@Valid ResourceSpecificationRef> resourceSpecification) {
    this.resourceSpecification = resourceSpecification;
  }

  public ProductSpecificationCreate addResourceSpecificationItem(ResourceSpecificationRef resourceSpecificationItem) {
    if (this.resourceSpecification == null) {
      this.resourceSpecification = new ArrayList<>();
    }

    this.resourceSpecification.add(resourceSpecificationItem);
    return this;
  }

  public ProductSpecificationCreate removeResourceSpecificationItem(ResourceSpecificationRef resourceSpecificationItem) {
    if (resourceSpecificationItem != null && this.resourceSpecification != null) {
      this.resourceSpecification.remove(resourceSpecificationItem);
    }

    return this;
  }
  /**
   * ServiceSpecification(s) required to realize a ProductSpecification.
   **/
  public ProductSpecificationCreate serviceSpecification(List<@Valid ServiceSpecificationRef> serviceSpecification) {
    this.serviceSpecification = serviceSpecification;
    return this;
  }

  
  @JsonProperty("serviceSpecification")
  public List<ServiceSpecificationRef> getServiceSpecification() {
    return serviceSpecification;
  }

  @JsonProperty("serviceSpecification")
  public void setServiceSpecification(List<@Valid ServiceSpecificationRef> serviceSpecification) {
    this.serviceSpecification = serviceSpecification;
  }

  public ProductSpecificationCreate addServiceSpecificationItem(ServiceSpecificationRef serviceSpecificationItem) {
    if (this.serviceSpecification == null) {
      this.serviceSpecification = new ArrayList<>();
    }

    this.serviceSpecification.add(serviceSpecificationItem);
    return this;
  }

  public ProductSpecificationCreate removeServiceSpecificationItem(ServiceSpecificationRef serviceSpecificationItem) {
    if (serviceSpecificationItem != null && this.serviceSpecification != null) {
      this.serviceSpecification.remove(serviceSpecificationItem);
    }

    return this;
  }
  /**
   **/
  public ProductSpecificationCreate targetProductSchema(TargetProductSchema targetProductSchema) {
    this.targetProductSchema = targetProductSchema;
    return this;
  }

  
  @JsonProperty("targetProductSchema")
  public TargetProductSchema getTargetProductSchema() {
    return targetProductSchema;
  }

  @JsonProperty("targetProductSchema")
  public void setTargetProductSchema(TargetProductSchema targetProductSchema) {
    this.targetProductSchema = targetProductSchema;
  }

  /**
   **/
  public ProductSpecificationCreate validFor(TimePeriod validFor) {
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
  public ProductSpecificationCreate atBaseType(String atBaseType) {
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
  public ProductSpecificationCreate atSchemaLocation(URI atSchemaLocation) {
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
  public ProductSpecificationCreate atType(String atType) {
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
    ProductSpecificationCreate productSpecificationCreate = (ProductSpecificationCreate) o;
    return Objects.equals(this.brand, productSpecificationCreate.brand) &&
        Objects.equals(this.description, productSpecificationCreate.description) &&
        Objects.equals(this.isBundle, productSpecificationCreate.isBundle) &&
        Objects.equals(this.lastUpdate, productSpecificationCreate.lastUpdate) &&
        Objects.equals(this.lifecycleStatus, productSpecificationCreate.lifecycleStatus) &&
        Objects.equals(this.name, productSpecificationCreate.name) &&
        Objects.equals(this.productNumber, productSpecificationCreate.productNumber) &&
        Objects.equals(this.version, productSpecificationCreate.version) &&
        Objects.equals(this.attachment, productSpecificationCreate.attachment) &&
        Objects.equals(this.bundledProductSpecification, productSpecificationCreate.bundledProductSpecification) &&
        Objects.equals(this.productSpecCharacteristic, productSpecificationCreate.productSpecCharacteristic) &&
        Objects.equals(this.productSpecificationRelationship, productSpecificationCreate.productSpecificationRelationship) &&
        Objects.equals(this.relatedParty, productSpecificationCreate.relatedParty) &&
        Objects.equals(this.resourceSpecification, productSpecificationCreate.resourceSpecification) &&
        Objects.equals(this.serviceSpecification, productSpecificationCreate.serviceSpecification) &&
        Objects.equals(this.targetProductSchema, productSpecificationCreate.targetProductSchema) &&
        Objects.equals(this.validFor, productSpecificationCreate.validFor) &&
        Objects.equals(this.atBaseType, productSpecificationCreate.atBaseType) &&
        Objects.equals(this.atSchemaLocation, productSpecificationCreate.atSchemaLocation) &&
        Objects.equals(this.atType, productSpecificationCreate.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(brand, description, isBundle, lastUpdate, lifecycleStatus, name, productNumber, version, attachment, bundledProductSpecification, productSpecCharacteristic, productSpecificationRelationship, relatedParty, resourceSpecification, serviceSpecification, targetProductSchema, validFor, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductSpecificationCreate {\n");
    
    sb.append("    brand: ").append(toIndentedString(brand)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    isBundle: ").append(toIndentedString(isBundle)).append("\n");
    sb.append("    lastUpdate: ").append(toIndentedString(lastUpdate)).append("\n");
    sb.append("    lifecycleStatus: ").append(toIndentedString(lifecycleStatus)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    productNumber: ").append(toIndentedString(productNumber)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    attachment: ").append(toIndentedString(attachment)).append("\n");
    sb.append("    bundledProductSpecification: ").append(toIndentedString(bundledProductSpecification)).append("\n");
    sb.append("    productSpecCharacteristic: ").append(toIndentedString(productSpecCharacteristic)).append("\n");
    sb.append("    productSpecificationRelationship: ").append(toIndentedString(productSpecificationRelationship)).append("\n");
    sb.append("    relatedParty: ").append(toIndentedString(relatedParty)).append("\n");
    sb.append("    resourceSpecification: ").append(toIndentedString(resourceSpecification)).append("\n");
    sb.append("    serviceSpecification: ").append(toIndentedString(serviceSpecification)).append("\n");
    sb.append("    targetProductSchema: ").append(toIndentedString(targetProductSchema)).append("\n");
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

