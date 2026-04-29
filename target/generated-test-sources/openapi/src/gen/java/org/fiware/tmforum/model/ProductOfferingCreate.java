package org.fiware.tmforum.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.fiware.tmforum.model.AgreementRef;
import org.fiware.tmforum.model.AttachmentRefOrValue;
import org.fiware.tmforum.model.BundledProductOffering;
import org.fiware.tmforum.model.CategoryRef;
import org.fiware.tmforum.model.ChannelRef;
import org.fiware.tmforum.model.MarketSegmentRef;
import org.fiware.tmforum.model.PlaceRef;
import org.fiware.tmforum.model.ProductOfferingPriceRefOrValue;
import org.fiware.tmforum.model.ProductOfferingRelationship;
import org.fiware.tmforum.model.ProductOfferingTerm;
import org.fiware.tmforum.model.ProductSpecificationCharacteristicValueUse;
import org.fiware.tmforum.model.ProductSpecificationRef;
import org.fiware.tmforum.model.ResourceCandidateRef;
import org.fiware.tmforum.model.SLARef;
import org.fiware.tmforum.model.ServiceCandidateRef;
import org.fiware.tmforum.model.TimePeriod;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represents entities that are orderable from the provider of the catalog, this resource includes pricing information. Skipped properties: id,href
 **/

@JsonTypeName("ProductOffering_Create")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class ProductOfferingCreate   {
  private @Valid String description;
  private @Valid Boolean isBundle;
  private @Valid Boolean isSellable;
  private @Valid OffsetDateTime lastUpdate;
  private @Valid String lifecycleStatus;
  private @Valid String name;
  private @Valid String statusReason;
  private @Valid String version;
  private @Valid List<@Valid AgreementRef> agreement;
  private @Valid List<@Valid AttachmentRefOrValue> attachment;
  private @Valid List<@Valid BundledProductOffering> bundledProductOffering;
  private @Valid List<@Valid CategoryRef> category;
  private @Valid List<@Valid ChannelRef> channel;
  private @Valid List<@Valid MarketSegmentRef> marketSegment;
  private @Valid List<@Valid PlaceRef> place;
  private @Valid List<@Valid ProductSpecificationCharacteristicValueUse> prodSpecCharValueUse;
  private @Valid List<@Valid ProductOfferingPriceRefOrValue> productOfferingPrice;
  private @Valid List<@Valid ProductOfferingRelationship> productOfferingRelationship;
  private @Valid List<@Valid ProductOfferingTerm> productOfferingTerm;
  private @Valid ProductSpecificationRef productSpecification;
  private @Valid ResourceCandidateRef resourceCandidate;
  private @Valid ServiceCandidateRef serviceCandidate;
  private @Valid SLARef serviceLevelAgreement;
  private @Valid TimePeriod validFor;
  private @Valid String atBaseType;
  private @Valid URI atSchemaLocation;
  private @Valid String atType;

  /**
   * Description of the productOffering
   **/
  public ProductOfferingCreate description(String description) {
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
   * isBundle determines whether a productOffering represents a single productOffering (false), or a bundle of productOfferings (true).
   **/
  public ProductOfferingCreate isBundle(Boolean isBundle) {
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
   * A flag indicating if this product offer can be sold stand-alone for sale or not. If this flag is false it indicates that the offer can only be sold within a bundle.
   **/
  public ProductOfferingCreate isSellable(Boolean isSellable) {
    this.isSellable = isSellable;
    return this;
  }

  
  @JsonProperty("isSellable")
  public Boolean getIsSellable() {
    return isSellable;
  }

  @JsonProperty("isSellable")
  public void setIsSellable(Boolean isSellable) {
    this.isSellable = isSellable;
  }

  /**
   * Date and time of the last update
   **/
  public ProductOfferingCreate lastUpdate(OffsetDateTime lastUpdate) {
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
  public ProductOfferingCreate lifecycleStatus(String lifecycleStatus) {
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
   * Name of the productOffering
   **/
  public ProductOfferingCreate name(String name) {
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
   * A string providing a complementary information on the value of the lifecycle status attribute.
   **/
  public ProductOfferingCreate statusReason(String statusReason) {
    this.statusReason = statusReason;
    return this;
  }

  
  @JsonProperty("statusReason")
  public String getStatusReason() {
    return statusReason;
  }

  @JsonProperty("statusReason")
  public void setStatusReason(String statusReason) {
    this.statusReason = statusReason;
  }

  /**
   * ProductOffering version
   **/
  public ProductOfferingCreate version(String version) {
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
   * An agreement represents a contract or arrangement, either written or verbal and sometimes enforceable by law, such as a service level agreement or a customer price agreement. An agreement involves a number of other business entities, such as products, services, and resources and/or their specifications.
   **/
  public ProductOfferingCreate agreement(List<@Valid AgreementRef> agreement) {
    this.agreement = agreement;
    return this;
  }

  
  @JsonProperty("agreement")
  public List<AgreementRef> getAgreement() {
    return agreement;
  }

  @JsonProperty("agreement")
  public void setAgreement(List<@Valid AgreementRef> agreement) {
    this.agreement = agreement;
  }

  public ProductOfferingCreate addAgreementItem(AgreementRef agreementItem) {
    if (this.agreement == null) {
      this.agreement = new ArrayList<>();
    }

    this.agreement.add(agreementItem);
    return this;
  }

  public ProductOfferingCreate removeAgreementItem(AgreementRef agreementItem) {
    if (agreementItem != null && this.agreement != null) {
      this.agreement.remove(agreementItem);
    }

    return this;
  }
  /**
   * Complements the description of an element (for instance a product) through video, pictures...
   **/
  public ProductOfferingCreate attachment(List<@Valid AttachmentRefOrValue> attachment) {
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

  public ProductOfferingCreate addAttachmentItem(AttachmentRefOrValue attachmentItem) {
    if (this.attachment == null) {
      this.attachment = new ArrayList<>();
    }

    this.attachment.add(attachmentItem);
    return this;
  }

  public ProductOfferingCreate removeAttachmentItem(AttachmentRefOrValue attachmentItem) {
    if (attachmentItem != null && this.attachment != null) {
      this.attachment.remove(attachmentItem);
    }

    return this;
  }
  /**
   * A type of ProductOffering that belongs to a grouping of ProductOfferings made available to the market. It inherits of all attributes of ProductOffering.
   **/
  public ProductOfferingCreate bundledProductOffering(List<@Valid BundledProductOffering> bundledProductOffering) {
    this.bundledProductOffering = bundledProductOffering;
    return this;
  }

  
  @JsonProperty("bundledProductOffering")
  public List<BundledProductOffering> getBundledProductOffering() {
    return bundledProductOffering;
  }

  @JsonProperty("bundledProductOffering")
  public void setBundledProductOffering(List<@Valid BundledProductOffering> bundledProductOffering) {
    this.bundledProductOffering = bundledProductOffering;
  }

  public ProductOfferingCreate addBundledProductOfferingItem(BundledProductOffering bundledProductOfferingItem) {
    if (this.bundledProductOffering == null) {
      this.bundledProductOffering = new ArrayList<>();
    }

    this.bundledProductOffering.add(bundledProductOfferingItem);
    return this;
  }

  public ProductOfferingCreate removeBundledProductOfferingItem(BundledProductOffering bundledProductOfferingItem) {
    if (bundledProductOfferingItem != null && this.bundledProductOffering != null) {
      this.bundledProductOffering.remove(bundledProductOfferingItem);
    }

    return this;
  }
  /**
   * The category resource is used to group product offerings, service and resource candidates in logical containers. Categories can contain other categories and/or product offerings, resource or service candidates.
   **/
  public ProductOfferingCreate category(List<@Valid CategoryRef> category) {
    this.category = category;
    return this;
  }

  
  @JsonProperty("category")
  public List<CategoryRef> getCategory() {
    return category;
  }

  @JsonProperty("category")
  public void setCategory(List<@Valid CategoryRef> category) {
    this.category = category;
  }

  public ProductOfferingCreate addCategoryItem(CategoryRef categoryItem) {
    if (this.category == null) {
      this.category = new ArrayList<>();
    }

    this.category.add(categoryItem);
    return this;
  }

  public ProductOfferingCreate removeCategoryItem(CategoryRef categoryItem) {
    if (categoryItem != null && this.category != null) {
      this.category.remove(categoryItem);
    }

    return this;
  }
  /**
   * The channel defines the channel for selling product offerings.
   **/
  public ProductOfferingCreate channel(List<@Valid ChannelRef> channel) {
    this.channel = channel;
    return this;
  }

  
  @JsonProperty("channel")
  public List<ChannelRef> getChannel() {
    return channel;
  }

  @JsonProperty("channel")
  public void setChannel(List<@Valid ChannelRef> channel) {
    this.channel = channel;
  }

  public ProductOfferingCreate addChannelItem(ChannelRef channelItem) {
    if (this.channel == null) {
      this.channel = new ArrayList<>();
    }

    this.channel.add(channelItem);
    return this;
  }

  public ProductOfferingCreate removeChannelItem(ChannelRef channelItem) {
    if (channelItem != null && this.channel != null) {
      this.channel.remove(channelItem);
    }

    return this;
  }
  /**
   * provides references to the corresponding market segment as target of product offerings. A market segment is grouping of Parties, GeographicAreas, SalesChannels, and so forth.
   **/
  public ProductOfferingCreate marketSegment(List<@Valid MarketSegmentRef> marketSegment) {
    this.marketSegment = marketSegment;
    return this;
  }

  
  @JsonProperty("marketSegment")
  public List<MarketSegmentRef> getMarketSegment() {
    return marketSegment;
  }

  @JsonProperty("marketSegment")
  public void setMarketSegment(List<@Valid MarketSegmentRef> marketSegment) {
    this.marketSegment = marketSegment;
  }

  public ProductOfferingCreate addMarketSegmentItem(MarketSegmentRef marketSegmentItem) {
    if (this.marketSegment == null) {
      this.marketSegment = new ArrayList<>();
    }

    this.marketSegment.add(marketSegmentItem);
    return this;
  }

  public ProductOfferingCreate removeMarketSegmentItem(MarketSegmentRef marketSegmentItem) {
    if (marketSegmentItem != null && this.marketSegment != null) {
      this.marketSegment.remove(marketSegmentItem);
    }

    return this;
  }
  /**
   * Place defines the places where the products are sold or delivered.
   **/
  public ProductOfferingCreate place(List<@Valid PlaceRef> place) {
    this.place = place;
    return this;
  }

  
  @JsonProperty("place")
  public List<PlaceRef> getPlace() {
    return place;
  }

  @JsonProperty("place")
  public void setPlace(List<@Valid PlaceRef> place) {
    this.place = place;
  }

  public ProductOfferingCreate addPlaceItem(PlaceRef placeItem) {
    if (this.place == null) {
      this.place = new ArrayList<>();
    }

    this.place.add(placeItem);
    return this;
  }

  public ProductOfferingCreate removePlaceItem(PlaceRef placeItem) {
    if (placeItem != null && this.place != null) {
      this.place.remove(placeItem);
    }

    return this;
  }
  /**
   * A use of the ProductSpecificationCharacteristicValue by a ProductOffering to which additional properties (attributes) apply or override the properties of similar properties contained in ProductSpecificationCharacteristicValue. It should be noted that characteristics which their value(s) addressed by this object must exist in corresponding product specification. The available characteristic values for a ProductSpecificationCharacteristic in a Product specification can be modified at the ProductOffering level. For example, a characteristic &#39;Color&#39; might have values White, Blue, Green, and Red. But, the list of values can be restricted to e.g. White and Blue in an associated product offering. It should be noted that the list of values in &#39;ProductSpecificationCharacteristicValueUse&#39; is a strict subset of the list of values as defined in the corresponding product specification characteristics.
   **/
  public ProductOfferingCreate prodSpecCharValueUse(List<@Valid ProductSpecificationCharacteristicValueUse> prodSpecCharValueUse) {
    this.prodSpecCharValueUse = prodSpecCharValueUse;
    return this;
  }

  
  @JsonProperty("prodSpecCharValueUse")
  public List<ProductSpecificationCharacteristicValueUse> getProdSpecCharValueUse() {
    return prodSpecCharValueUse;
  }

  @JsonProperty("prodSpecCharValueUse")
  public void setProdSpecCharValueUse(List<@Valid ProductSpecificationCharacteristicValueUse> prodSpecCharValueUse) {
    this.prodSpecCharValueUse = prodSpecCharValueUse;
  }

  public ProductOfferingCreate addProdSpecCharValueUseItem(ProductSpecificationCharacteristicValueUse prodSpecCharValueUseItem) {
    if (this.prodSpecCharValueUse == null) {
      this.prodSpecCharValueUse = new ArrayList<>();
    }

    this.prodSpecCharValueUse.add(prodSpecCharValueUseItem);
    return this;
  }

  public ProductOfferingCreate removeProdSpecCharValueUseItem(ProductSpecificationCharacteristicValueUse prodSpecCharValueUseItem) {
    if (prodSpecCharValueUseItem != null && this.prodSpecCharValueUse != null) {
      this.prodSpecCharValueUse.remove(prodSpecCharValueUseItem);
    }

    return this;
  }
  /**
   * An amount, usually of money, that is asked for or allowed when a ProductOffering is bought, rented, or leased. The price is valid for a defined period of time and may not represent the actual price paid by a customer.
   **/
  public ProductOfferingCreate productOfferingPrice(List<@Valid ProductOfferingPriceRefOrValue> productOfferingPrice) {
    this.productOfferingPrice = productOfferingPrice;
    return this;
  }

  
  @JsonProperty("productOfferingPrice")
  public List<ProductOfferingPriceRefOrValue> getProductOfferingPrice() {
    return productOfferingPrice;
  }

  @JsonProperty("productOfferingPrice")
  public void setProductOfferingPrice(List<@Valid ProductOfferingPriceRefOrValue> productOfferingPrice) {
    this.productOfferingPrice = productOfferingPrice;
  }

  public ProductOfferingCreate addProductOfferingPriceItem(ProductOfferingPriceRefOrValue productOfferingPriceItem) {
    if (this.productOfferingPrice == null) {
      this.productOfferingPrice = new ArrayList<>();
    }

    this.productOfferingPrice.add(productOfferingPriceItem);
    return this;
  }

  public ProductOfferingCreate removeProductOfferingPriceItem(ProductOfferingPriceRefOrValue productOfferingPriceItem) {
    if (productOfferingPriceItem != null && this.productOfferingPrice != null) {
      this.productOfferingPrice.remove(productOfferingPriceItem);
    }

    return this;
  }
  /**
   * A relationship between this product offering and other product offerings.
   **/
  public ProductOfferingCreate productOfferingRelationship(List<@Valid ProductOfferingRelationship> productOfferingRelationship) {
    this.productOfferingRelationship = productOfferingRelationship;
    return this;
  }

  
  @JsonProperty("productOfferingRelationship")
  public List<ProductOfferingRelationship> getProductOfferingRelationship() {
    return productOfferingRelationship;
  }

  @JsonProperty("productOfferingRelationship")
  public void setProductOfferingRelationship(List<@Valid ProductOfferingRelationship> productOfferingRelationship) {
    this.productOfferingRelationship = productOfferingRelationship;
  }

  public ProductOfferingCreate addProductOfferingRelationshipItem(ProductOfferingRelationship productOfferingRelationshipItem) {
    if (this.productOfferingRelationship == null) {
      this.productOfferingRelationship = new ArrayList<>();
    }

    this.productOfferingRelationship.add(productOfferingRelationshipItem);
    return this;
  }

  public ProductOfferingCreate removeProductOfferingRelationshipItem(ProductOfferingRelationship productOfferingRelationshipItem) {
    if (productOfferingRelationshipItem != null && this.productOfferingRelationship != null) {
      this.productOfferingRelationship.remove(productOfferingRelationshipItem);
    }

    return this;
  }
  /**
   * A condition under which a ProductOffering is made available to Customers. For instance, a productOffering can be offered with multiple commitment periods.
   **/
  public ProductOfferingCreate productOfferingTerm(List<@Valid ProductOfferingTerm> productOfferingTerm) {
    this.productOfferingTerm = productOfferingTerm;
    return this;
  }

  
  @JsonProperty("productOfferingTerm")
  public List<ProductOfferingTerm> getProductOfferingTerm() {
    return productOfferingTerm;
  }

  @JsonProperty("productOfferingTerm")
  public void setProductOfferingTerm(List<@Valid ProductOfferingTerm> productOfferingTerm) {
    this.productOfferingTerm = productOfferingTerm;
  }

  public ProductOfferingCreate addProductOfferingTermItem(ProductOfferingTerm productOfferingTermItem) {
    if (this.productOfferingTerm == null) {
      this.productOfferingTerm = new ArrayList<>();
    }

    this.productOfferingTerm.add(productOfferingTermItem);
    return this;
  }

  public ProductOfferingCreate removeProductOfferingTermItem(ProductOfferingTerm productOfferingTermItem) {
    if (productOfferingTermItem != null && this.productOfferingTerm != null) {
      this.productOfferingTerm.remove(productOfferingTermItem);
    }

    return this;
  }
  /**
   **/
  public ProductOfferingCreate productSpecification(ProductSpecificationRef productSpecification) {
    this.productSpecification = productSpecification;
    return this;
  }

  
  @JsonProperty("productSpecification")
  public ProductSpecificationRef getProductSpecification() {
    return productSpecification;
  }

  @JsonProperty("productSpecification")
  public void setProductSpecification(ProductSpecificationRef productSpecification) {
    this.productSpecification = productSpecification;
  }

  /**
   **/
  public ProductOfferingCreate resourceCandidate(ResourceCandidateRef resourceCandidate) {
    this.resourceCandidate = resourceCandidate;
    return this;
  }

  
  @JsonProperty("resourceCandidate")
  public ResourceCandidateRef getResourceCandidate() {
    return resourceCandidate;
  }

  @JsonProperty("resourceCandidate")
  public void setResourceCandidate(ResourceCandidateRef resourceCandidate) {
    this.resourceCandidate = resourceCandidate;
  }

  /**
   **/
  public ProductOfferingCreate serviceCandidate(ServiceCandidateRef serviceCandidate) {
    this.serviceCandidate = serviceCandidate;
    return this;
  }

  
  @JsonProperty("serviceCandidate")
  public ServiceCandidateRef getServiceCandidate() {
    return serviceCandidate;
  }

  @JsonProperty("serviceCandidate")
  public void setServiceCandidate(ServiceCandidateRef serviceCandidate) {
    this.serviceCandidate = serviceCandidate;
  }

  /**
   **/
  public ProductOfferingCreate serviceLevelAgreement(SLARef serviceLevelAgreement) {
    this.serviceLevelAgreement = serviceLevelAgreement;
    return this;
  }

  
  @JsonProperty("serviceLevelAgreement")
  public SLARef getServiceLevelAgreement() {
    return serviceLevelAgreement;
  }

  @JsonProperty("serviceLevelAgreement")
  public void setServiceLevelAgreement(SLARef serviceLevelAgreement) {
    this.serviceLevelAgreement = serviceLevelAgreement;
  }

  /**
   **/
  public ProductOfferingCreate validFor(TimePeriod validFor) {
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
  public ProductOfferingCreate atBaseType(String atBaseType) {
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
  public ProductOfferingCreate atSchemaLocation(URI atSchemaLocation) {
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
  public ProductOfferingCreate atType(String atType) {
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
    ProductOfferingCreate productOfferingCreate = (ProductOfferingCreate) o;
    return Objects.equals(this.description, productOfferingCreate.description) &&
        Objects.equals(this.isBundle, productOfferingCreate.isBundle) &&
        Objects.equals(this.isSellable, productOfferingCreate.isSellable) &&
        Objects.equals(this.lastUpdate, productOfferingCreate.lastUpdate) &&
        Objects.equals(this.lifecycleStatus, productOfferingCreate.lifecycleStatus) &&
        Objects.equals(this.name, productOfferingCreate.name) &&
        Objects.equals(this.statusReason, productOfferingCreate.statusReason) &&
        Objects.equals(this.version, productOfferingCreate.version) &&
        Objects.equals(this.agreement, productOfferingCreate.agreement) &&
        Objects.equals(this.attachment, productOfferingCreate.attachment) &&
        Objects.equals(this.bundledProductOffering, productOfferingCreate.bundledProductOffering) &&
        Objects.equals(this.category, productOfferingCreate.category) &&
        Objects.equals(this.channel, productOfferingCreate.channel) &&
        Objects.equals(this.marketSegment, productOfferingCreate.marketSegment) &&
        Objects.equals(this.place, productOfferingCreate.place) &&
        Objects.equals(this.prodSpecCharValueUse, productOfferingCreate.prodSpecCharValueUse) &&
        Objects.equals(this.productOfferingPrice, productOfferingCreate.productOfferingPrice) &&
        Objects.equals(this.productOfferingRelationship, productOfferingCreate.productOfferingRelationship) &&
        Objects.equals(this.productOfferingTerm, productOfferingCreate.productOfferingTerm) &&
        Objects.equals(this.productSpecification, productOfferingCreate.productSpecification) &&
        Objects.equals(this.resourceCandidate, productOfferingCreate.resourceCandidate) &&
        Objects.equals(this.serviceCandidate, productOfferingCreate.serviceCandidate) &&
        Objects.equals(this.serviceLevelAgreement, productOfferingCreate.serviceLevelAgreement) &&
        Objects.equals(this.validFor, productOfferingCreate.validFor) &&
        Objects.equals(this.atBaseType, productOfferingCreate.atBaseType) &&
        Objects.equals(this.atSchemaLocation, productOfferingCreate.atSchemaLocation) &&
        Objects.equals(this.atType, productOfferingCreate.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, isBundle, isSellable, lastUpdate, lifecycleStatus, name, statusReason, version, agreement, attachment, bundledProductOffering, category, channel, marketSegment, place, prodSpecCharValueUse, productOfferingPrice, productOfferingRelationship, productOfferingTerm, productSpecification, resourceCandidate, serviceCandidate, serviceLevelAgreement, validFor, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductOfferingCreate {\n");
    
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    isBundle: ").append(toIndentedString(isBundle)).append("\n");
    sb.append("    isSellable: ").append(toIndentedString(isSellable)).append("\n");
    sb.append("    lastUpdate: ").append(toIndentedString(lastUpdate)).append("\n");
    sb.append("    lifecycleStatus: ").append(toIndentedString(lifecycleStatus)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    statusReason: ").append(toIndentedString(statusReason)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    agreement: ").append(toIndentedString(agreement)).append("\n");
    sb.append("    attachment: ").append(toIndentedString(attachment)).append("\n");
    sb.append("    bundledProductOffering: ").append(toIndentedString(bundledProductOffering)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    marketSegment: ").append(toIndentedString(marketSegment)).append("\n");
    sb.append("    place: ").append(toIndentedString(place)).append("\n");
    sb.append("    prodSpecCharValueUse: ").append(toIndentedString(prodSpecCharValueUse)).append("\n");
    sb.append("    productOfferingPrice: ").append(toIndentedString(productOfferingPrice)).append("\n");
    sb.append("    productOfferingRelationship: ").append(toIndentedString(productOfferingRelationship)).append("\n");
    sb.append("    productOfferingTerm: ").append(toIndentedString(productOfferingTerm)).append("\n");
    sb.append("    productSpecification: ").append(toIndentedString(productSpecification)).append("\n");
    sb.append("    resourceCandidate: ").append(toIndentedString(resourceCandidate)).append("\n");
    sb.append("    serviceCandidate: ").append(toIndentedString(serviceCandidate)).append("\n");
    sb.append("    serviceLevelAgreement: ").append(toIndentedString(serviceLevelAgreement)).append("\n");
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

