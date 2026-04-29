package org.fiware.tmforum.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.net.URI;
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
 * Is a detailed description of a tangible or intangible object made available externally in the form of a ProductOffering to customers or other parties playing a party role. Skipped properties: id,href,lastUpdate,@type,@baseType
 **/

@JsonTypeName("ProductSpecification_Update")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class ProductSpecificationUpdate   {
  private @Valid String brand;
  private @Valid String description;
  private @Valid Boolean isBundle;
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
  private @Valid URI atSchemaLocation;

  /**
   * The manufacturer or trademark of the specification
   **/
  public ProductSpecificationUpdate brand(String brand) {
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
  public ProductSpecificationUpdate description(String description) {
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
  public ProductSpecificationUpdate isBundle(Boolean isBundle) {
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
   * Used to indicate the current lifecycle status
   **/
  public ProductSpecificationUpdate lifecycleStatus(String lifecycleStatus) {
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
  public ProductSpecificationUpdate name(String name) {
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
   * An identification number assigned to uniquely identity the specification
   **/
  public ProductSpecificationUpdate productNumber(String productNumber) {
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
  public ProductSpecificationUpdate version(String version) {
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
  public ProductSpecificationUpdate attachment(List<@Valid AttachmentRefOrValue> attachment) {
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

  public ProductSpecificationUpdate addAttachmentItem(AttachmentRefOrValue attachmentItem) {
    if (this.attachment == null) {
      this.attachment = new ArrayList<>();
    }

    this.attachment.add(attachmentItem);
    return this;
  }

  public ProductSpecificationUpdate removeAttachmentItem(AttachmentRefOrValue attachmentItem) {
    if (attachmentItem != null && this.attachment != null) {
      this.attachment.remove(attachmentItem);
    }

    return this;
  }
  /**
   * A type of ProductSpecification that belongs to a grouping of ProductSpecifications made available to the market. It inherits of all attributes of ProductSpecification.
   **/
  public ProductSpecificationUpdate bundledProductSpecification(List<@Valid BundledProductSpecification> bundledProductSpecification) {
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

  public ProductSpecificationUpdate addBundledProductSpecificationItem(BundledProductSpecification bundledProductSpecificationItem) {
    if (this.bundledProductSpecification == null) {
      this.bundledProductSpecification = new ArrayList<>();
    }

    this.bundledProductSpecification.add(bundledProductSpecificationItem);
    return this;
  }

  public ProductSpecificationUpdate removeBundledProductSpecificationItem(BundledProductSpecification bundledProductSpecificationItem) {
    if (bundledProductSpecificationItem != null && this.bundledProductSpecification != null) {
      this.bundledProductSpecification.remove(bundledProductSpecificationItem);
    }

    return this;
  }
  /**
   * A characteristic quality or distinctive feature of a ProductSpecification.  The characteristic can be take on a discrete value, such as color, can take on a range of values, (for example, sensitivity of 100-240 mV), or can be derived from a formula (for example, usage time (hrs) &#x3D; 30 - talk time *3). Certain characteristics, such as color, may be configured during the ordering or some other process.
   **/
  public ProductSpecificationUpdate productSpecCharacteristic(List<@Valid ProductSpecificationCharacteristic> productSpecCharacteristic) {
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

  public ProductSpecificationUpdate addProductSpecCharacteristicItem(ProductSpecificationCharacteristic productSpecCharacteristicItem) {
    if (this.productSpecCharacteristic == null) {
      this.productSpecCharacteristic = new ArrayList<>();
    }

    this.productSpecCharacteristic.add(productSpecCharacteristicItem);
    return this;
  }

  public ProductSpecificationUpdate removeProductSpecCharacteristicItem(ProductSpecificationCharacteristic productSpecCharacteristicItem) {
    if (productSpecCharacteristicItem != null && this.productSpecCharacteristic != null) {
      this.productSpecCharacteristic.remove(productSpecCharacteristicItem);
    }

    return this;
  }
  /**
   * A migration, substitution, dependency or exclusivity relationship between/among product specifications.
   **/
  public ProductSpecificationUpdate productSpecificationRelationship(List<@Valid ProductSpecificationRelationship> productSpecificationRelationship) {
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

  public ProductSpecificationUpdate addProductSpecificationRelationshipItem(ProductSpecificationRelationship productSpecificationRelationshipItem) {
    if (this.productSpecificationRelationship == null) {
      this.productSpecificationRelationship = new ArrayList<>();
    }

    this.productSpecificationRelationship.add(productSpecificationRelationshipItem);
    return this;
  }

  public ProductSpecificationUpdate removeProductSpecificationRelationshipItem(ProductSpecificationRelationship productSpecificationRelationshipItem) {
    if (productSpecificationRelationshipItem != null && this.productSpecificationRelationship != null) {
      this.productSpecificationRelationship.remove(productSpecificationRelationshipItem);
    }

    return this;
  }
  /**
   * A related party defines party or party role linked to a specific entity.
   **/
  public ProductSpecificationUpdate relatedParty(List<@Valid RelatedParty> relatedParty) {
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

  public ProductSpecificationUpdate addRelatedPartyItem(RelatedParty relatedPartyItem) {
    if (this.relatedParty == null) {
      this.relatedParty = new ArrayList<>();
    }

    this.relatedParty.add(relatedPartyItem);
    return this;
  }

  public ProductSpecificationUpdate removeRelatedPartyItem(RelatedParty relatedPartyItem) {
    if (relatedPartyItem != null && this.relatedParty != null) {
      this.relatedParty.remove(relatedPartyItem);
    }

    return this;
  }
  /**
   * The ResourceSpecification is required to realize a ProductSpecification.
   **/
  public ProductSpecificationUpdate resourceSpecification(List<@Valid ResourceSpecificationRef> resourceSpecification) {
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

  public ProductSpecificationUpdate addResourceSpecificationItem(ResourceSpecificationRef resourceSpecificationItem) {
    if (this.resourceSpecification == null) {
      this.resourceSpecification = new ArrayList<>();
    }

    this.resourceSpecification.add(resourceSpecificationItem);
    return this;
  }

  public ProductSpecificationUpdate removeResourceSpecificationItem(ResourceSpecificationRef resourceSpecificationItem) {
    if (resourceSpecificationItem != null && this.resourceSpecification != null) {
      this.resourceSpecification.remove(resourceSpecificationItem);
    }

    return this;
  }
  /**
   * ServiceSpecification(s) required to realize a ProductSpecification.
   **/
  public ProductSpecificationUpdate serviceSpecification(List<@Valid ServiceSpecificationRef> serviceSpecification) {
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

  public ProductSpecificationUpdate addServiceSpecificationItem(ServiceSpecificationRef serviceSpecificationItem) {
    if (this.serviceSpecification == null) {
      this.serviceSpecification = new ArrayList<>();
    }

    this.serviceSpecification.add(serviceSpecificationItem);
    return this;
  }

  public ProductSpecificationUpdate removeServiceSpecificationItem(ServiceSpecificationRef serviceSpecificationItem) {
    if (serviceSpecificationItem != null && this.serviceSpecification != null) {
      this.serviceSpecification.remove(serviceSpecificationItem);
    }

    return this;
  }
  /**
   **/
  public ProductSpecificationUpdate targetProductSchema(TargetProductSchema targetProductSchema) {
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
  public ProductSpecificationUpdate validFor(TimePeriod validFor) {
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
  public ProductSpecificationUpdate atSchemaLocation(URI atSchemaLocation) {
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
    ProductSpecificationUpdate productSpecificationUpdate = (ProductSpecificationUpdate) o;
    return Objects.equals(this.brand, productSpecificationUpdate.brand) &&
        Objects.equals(this.description, productSpecificationUpdate.description) &&
        Objects.equals(this.isBundle, productSpecificationUpdate.isBundle) &&
        Objects.equals(this.lifecycleStatus, productSpecificationUpdate.lifecycleStatus) &&
        Objects.equals(this.name, productSpecificationUpdate.name) &&
        Objects.equals(this.productNumber, productSpecificationUpdate.productNumber) &&
        Objects.equals(this.version, productSpecificationUpdate.version) &&
        Objects.equals(this.attachment, productSpecificationUpdate.attachment) &&
        Objects.equals(this.bundledProductSpecification, productSpecificationUpdate.bundledProductSpecification) &&
        Objects.equals(this.productSpecCharacteristic, productSpecificationUpdate.productSpecCharacteristic) &&
        Objects.equals(this.productSpecificationRelationship, productSpecificationUpdate.productSpecificationRelationship) &&
        Objects.equals(this.relatedParty, productSpecificationUpdate.relatedParty) &&
        Objects.equals(this.resourceSpecification, productSpecificationUpdate.resourceSpecification) &&
        Objects.equals(this.serviceSpecification, productSpecificationUpdate.serviceSpecification) &&
        Objects.equals(this.targetProductSchema, productSpecificationUpdate.targetProductSchema) &&
        Objects.equals(this.validFor, productSpecificationUpdate.validFor) &&
        Objects.equals(this.atSchemaLocation, productSpecificationUpdate.atSchemaLocation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(brand, description, isBundle, lifecycleStatus, name, productNumber, version, attachment, bundledProductSpecification, productSpecCharacteristic, productSpecificationRelationship, relatedParty, resourceSpecification, serviceSpecification, targetProductSchema, validFor, atSchemaLocation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductSpecificationUpdate {\n");
    
    sb.append("    brand: ").append(toIndentedString(brand)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    isBundle: ").append(toIndentedString(isBundle)).append("\n");
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

