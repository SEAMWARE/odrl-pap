package org.fiware.tmforum.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.fiware.tmforum.model.BundledProductOfferingPriceRelationship;
import org.fiware.tmforum.model.ConstraintRef;
import org.fiware.tmforum.model.Money;
import org.fiware.tmforum.model.PlaceRef;
import org.fiware.tmforum.model.PricingLogicAlgorithm;
import org.fiware.tmforum.model.ProductOfferingPriceRelationship;
import org.fiware.tmforum.model.ProductOfferingTerm;
import org.fiware.tmforum.model.ProductSpecificationCharacteristicValueUse;
import org.fiware.tmforum.model.Quantity;
import org.fiware.tmforum.model.TaxItem;
import org.fiware.tmforum.model.TimePeriod;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Is based on both the basic cost to develop and produce products and the enterprises policy on revenue targets. This price may be further revised through discounting (a Product Offering Price that reflects an alteration). The price, applied for a productOffering may also be influenced by the productOfferingTerm, the customer selected, eg: a productOffering can be offered with multiple terms, like commitment periods for the contract. The price may be influenced by this productOfferingTerm. A productOffering may be cheaper with a 24 month commitment than with a 12 month commitment.
 **/

@JsonTypeName("ProductOfferingPrice")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class ProductOfferingPrice   {
  private @Valid String id;
  private @Valid String href;
  private @Valid String description;
  private @Valid Boolean isBundle;
  private @Valid OffsetDateTime lastUpdate;
  private @Valid String lifecycleStatus;
  private @Valid String name;
  private @Valid Float percentage;
  private @Valid String priceType;
  private @Valid Integer recurringChargePeriodLength;
  private @Valid String recurringChargePeriodType;
  private @Valid String version;
  private @Valid List<@Valid BundledProductOfferingPriceRelationship> bundledPopRelationship;
  private @Valid List<@Valid ConstraintRef> constraint;
  private @Valid List<@Valid PlaceRef> place;
  private @Valid List<@Valid ProductOfferingPriceRelationship> popRelationship;
  private @Valid Money price;
  private @Valid List<@Valid PricingLogicAlgorithm> pricingLogicAlgorithm;
  private @Valid List<@Valid ProductSpecificationCharacteristicValueUse> prodSpecCharValueUse;
  private @Valid List<@Valid ProductOfferingTerm> productOfferingTerm;
  private @Valid List<@Valid TaxItem> tax;
  private @Valid Quantity unitOfMeasure;
  private @Valid TimePeriod validFor;
  private @Valid String atBaseType;
  private @Valid String atSchemaLocation;
  private @Valid String atType;

  /**
   * unique id of this resource
   **/
  public ProductOfferingPrice id(String id) {
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
   * Reference of the ProductOfferingPrice
   **/
  public ProductOfferingPrice href(String href) {
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
   * Description of the productOfferingPrice
   **/
  public ProductOfferingPrice description(String description) {
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
   * A flag indicating if this ProductOfferingPrice is composite (bundle) or not
   **/
  public ProductOfferingPrice isBundle(Boolean isBundle) {
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
   * the last update time of this ProductOfferingPrice
   **/
  public ProductOfferingPrice lastUpdate(OffsetDateTime lastUpdate) {
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
   * the lifecycle status of this ProductOfferingPrice
   **/
  public ProductOfferingPrice lifecycleStatus(String lifecycleStatus) {
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
   * Name of the productOfferingPrice
   **/
  public ProductOfferingPrice name(String name) {
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
   * Percentage to apply if this Product Offering Price is an Alteration (such as a Discount)
   **/
  public ProductOfferingPrice percentage(Float percentage) {
    this.percentage = percentage;
    return this;
  }

  
  @JsonProperty("percentage")
  public Float getPercentage() {
    return percentage;
  }

  @JsonProperty("percentage")
  public void setPercentage(Float percentage) {
    this.percentage = percentage;
  }

  /**
   * A category that describes the price, such as recurring, discount, allowance, penalty, and so forth.
   **/
  public ProductOfferingPrice priceType(String priceType) {
    this.priceType = priceType;
    return this;
  }

  
  @JsonProperty("priceType")
  public String getPriceType() {
    return priceType;
  }

  @JsonProperty("priceType")
  public void setPriceType(String priceType) {
    this.priceType = priceType;
  }

  /**
   * the period of the recurring charge:  1, 2, ... .It sets to zero if it is not applicable
   **/
  public ProductOfferingPrice recurringChargePeriodLength(Integer recurringChargePeriodLength) {
    this.recurringChargePeriodLength = recurringChargePeriodLength;
    return this;
  }

  
  @JsonProperty("recurringChargePeriodLength")
  public Integer getRecurringChargePeriodLength() {
    return recurringChargePeriodLength;
  }

  @JsonProperty("recurringChargePeriodLength")
  public void setRecurringChargePeriodLength(Integer recurringChargePeriodLength) {
    this.recurringChargePeriodLength = recurringChargePeriodLength;
  }

  /**
   * The period to repeat the application of the price Could be month, week...
   **/
  public ProductOfferingPrice recurringChargePeriodType(String recurringChargePeriodType) {
    this.recurringChargePeriodType = recurringChargePeriodType;
    return this;
  }

  
  @JsonProperty("recurringChargePeriodType")
  public String getRecurringChargePeriodType() {
    return recurringChargePeriodType;
  }

  @JsonProperty("recurringChargePeriodType")
  public void setRecurringChargePeriodType(String recurringChargePeriodType) {
    this.recurringChargePeriodType = recurringChargePeriodType;
  }

  /**
   * ProductOfferingPrice version
   **/
  public ProductOfferingPrice version(String version) {
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
   * this object represents a bundle relationship from a bundle product offering price (parent) to a simple product offering price (child). A simple product offering price may participate in more than one bundle relationship.
   **/
  public ProductOfferingPrice bundledPopRelationship(List<@Valid BundledProductOfferingPriceRelationship> bundledPopRelationship) {
    this.bundledPopRelationship = bundledPopRelationship;
    return this;
  }

  
  @JsonProperty("bundledPopRelationship")
  public List<BundledProductOfferingPriceRelationship> getBundledPopRelationship() {
    return bundledPopRelationship;
  }

  @JsonProperty("bundledPopRelationship")
  public void setBundledPopRelationship(List<@Valid BundledProductOfferingPriceRelationship> bundledPopRelationship) {
    this.bundledPopRelationship = bundledPopRelationship;
  }

  public ProductOfferingPrice addBundledPopRelationshipItem(BundledProductOfferingPriceRelationship bundledPopRelationshipItem) {
    if (this.bundledPopRelationship == null) {
      this.bundledPopRelationship = new ArrayList<>();
    }

    this.bundledPopRelationship.add(bundledPopRelationshipItem);
    return this;
  }

  public ProductOfferingPrice removeBundledPopRelationshipItem(BundledProductOfferingPriceRelationship bundledPopRelationshipItem) {
    if (bundledPopRelationshipItem != null && this.bundledPopRelationship != null) {
      this.bundledPopRelationship.remove(bundledPopRelationshipItem);
    }

    return this;
  }
  /**
   * The Constraint resource represents a policy/rule applied to ProductOfferingPrice.
   **/
  public ProductOfferingPrice constraint(List<@Valid ConstraintRef> constraint) {
    this.constraint = constraint;
    return this;
  }

  
  @JsonProperty("constraint")
  public List<ConstraintRef> getConstraint() {
    return constraint;
  }

  @JsonProperty("constraint")
  public void setConstraint(List<@Valid ConstraintRef> constraint) {
    this.constraint = constraint;
  }

  public ProductOfferingPrice addConstraintItem(ConstraintRef constraintItem) {
    if (this.constraint == null) {
      this.constraint = new ArrayList<>();
    }

    this.constraint.add(constraintItem);
    return this;
  }

  public ProductOfferingPrice removeConstraintItem(ConstraintRef constraintItem) {
    if (constraintItem != null && this.constraint != null) {
      this.constraint.remove(constraintItem);
    }

    return this;
  }
  /**
   * Place defines the places where the products are sold or delivered.
   **/
  public ProductOfferingPrice place(List<@Valid PlaceRef> place) {
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

  public ProductOfferingPrice addPlaceItem(PlaceRef placeItem) {
    if (this.place == null) {
      this.place = new ArrayList<>();
    }

    this.place.add(placeItem);
    return this;
  }

  public ProductOfferingPrice removePlaceItem(PlaceRef placeItem) {
    if (placeItem != null && this.place != null) {
      this.place.remove(placeItem);
    }

    return this;
  }
  /**
   * Product Offering Prices related to this Product Offering Price, for example a price alteration such as allowance or discount
   **/
  public ProductOfferingPrice popRelationship(List<@Valid ProductOfferingPriceRelationship> popRelationship) {
    this.popRelationship = popRelationship;
    return this;
  }

  
  @JsonProperty("popRelationship")
  public List<ProductOfferingPriceRelationship> getPopRelationship() {
    return popRelationship;
  }

  @JsonProperty("popRelationship")
  public void setPopRelationship(List<@Valid ProductOfferingPriceRelationship> popRelationship) {
    this.popRelationship = popRelationship;
  }

  public ProductOfferingPrice addPopRelationshipItem(ProductOfferingPriceRelationship popRelationshipItem) {
    if (this.popRelationship == null) {
      this.popRelationship = new ArrayList<>();
    }

    this.popRelationship.add(popRelationshipItem);
    return this;
  }

  public ProductOfferingPrice removePopRelationshipItem(ProductOfferingPriceRelationship popRelationshipItem) {
    if (popRelationshipItem != null && this.popRelationship != null) {
      this.popRelationship.remove(popRelationshipItem);
    }

    return this;
  }
  /**
   **/
  public ProductOfferingPrice price(Money price) {
    this.price = price;
    return this;
  }

  
  @JsonProperty("price")
  public Money getPrice() {
    return price;
  }

  @JsonProperty("price")
  public void setPrice(Money price) {
    this.price = price;
  }

  /**
   * The PricingLogicAlgorithm entity represents an instantiation of an interface specification to external rating function (without a modeled behavior in SID). Some of the parameters of the interface definition may be already set (such as price per unit) and some may be gathered during the rating process from the event (such as call duration) or from ProductCharacteristicValues (such as assigned bandwidth).
   **/
  public ProductOfferingPrice pricingLogicAlgorithm(List<@Valid PricingLogicAlgorithm> pricingLogicAlgorithm) {
    this.pricingLogicAlgorithm = pricingLogicAlgorithm;
    return this;
  }

  
  @JsonProperty("pricingLogicAlgorithm")
  public List<PricingLogicAlgorithm> getPricingLogicAlgorithm() {
    return pricingLogicAlgorithm;
  }

  @JsonProperty("pricingLogicAlgorithm")
  public void setPricingLogicAlgorithm(List<@Valid PricingLogicAlgorithm> pricingLogicAlgorithm) {
    this.pricingLogicAlgorithm = pricingLogicAlgorithm;
  }

  public ProductOfferingPrice addPricingLogicAlgorithmItem(PricingLogicAlgorithm pricingLogicAlgorithmItem) {
    if (this.pricingLogicAlgorithm == null) {
      this.pricingLogicAlgorithm = new ArrayList<>();
    }

    this.pricingLogicAlgorithm.add(pricingLogicAlgorithmItem);
    return this;
  }

  public ProductOfferingPrice removePricingLogicAlgorithmItem(PricingLogicAlgorithm pricingLogicAlgorithmItem) {
    if (pricingLogicAlgorithmItem != null && this.pricingLogicAlgorithm != null) {
      this.pricingLogicAlgorithm.remove(pricingLogicAlgorithmItem);
    }

    return this;
  }
  /**
   * A use of the ProductSpecificationCharacteristicValue by a ProductOfferingPrice to which additional properties (attributes) apply or override the properties of similar properties contained in ProductSpecificationCharacteristicValue. It should be noted that characteristics which their value(s) addressed by this object must exist in corresponding product specification. The available characteristic values for a ProductSpecificationCharacteristic in a Product specification can be modified at the ProductOffering and ProcuctOfferingPrice level. The list of values in ProductSpecificationCharacteristicValueUse is a strict subset of the list of values as defined in the corresponding product specification characteristics.
   **/
  public ProductOfferingPrice prodSpecCharValueUse(List<@Valid ProductSpecificationCharacteristicValueUse> prodSpecCharValueUse) {
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

  public ProductOfferingPrice addProdSpecCharValueUseItem(ProductSpecificationCharacteristicValueUse prodSpecCharValueUseItem) {
    if (this.prodSpecCharValueUse == null) {
      this.prodSpecCharValueUse = new ArrayList<>();
    }

    this.prodSpecCharValueUse.add(prodSpecCharValueUseItem);
    return this;
  }

  public ProductOfferingPrice removeProdSpecCharValueUseItem(ProductSpecificationCharacteristicValueUse prodSpecCharValueUseItem) {
    if (prodSpecCharValueUseItem != null && this.prodSpecCharValueUse != null) {
      this.prodSpecCharValueUse.remove(prodSpecCharValueUseItem);
    }

    return this;
  }
  /**
   * A list of conditions under which a ProductOfferingPrice is made available to Customers. For instance, a Product Offering Price can be offered with multiple commitment periods.
   **/
  public ProductOfferingPrice productOfferingTerm(List<@Valid ProductOfferingTerm> productOfferingTerm) {
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

  public ProductOfferingPrice addProductOfferingTermItem(ProductOfferingTerm productOfferingTermItem) {
    if (this.productOfferingTerm == null) {
      this.productOfferingTerm = new ArrayList<>();
    }

    this.productOfferingTerm.add(productOfferingTermItem);
    return this;
  }

  public ProductOfferingPrice removeProductOfferingTermItem(ProductOfferingTerm productOfferingTermItem) {
    if (productOfferingTermItem != null && this.productOfferingTerm != null) {
      this.productOfferingTerm.remove(productOfferingTermItem);
    }

    return this;
  }
  /**
   * An amount of money levied on the price of a Product by a legislative body.
   **/
  public ProductOfferingPrice tax(List<@Valid TaxItem> tax) {
    this.tax = tax;
    return this;
  }

  
  @JsonProperty("tax")
  public List<TaxItem> getTax() {
    return tax;
  }

  @JsonProperty("tax")
  public void setTax(List<@Valid TaxItem> tax) {
    this.tax = tax;
  }

  public ProductOfferingPrice addTaxItem(TaxItem taxItem) {
    if (this.tax == null) {
      this.tax = new ArrayList<>();
    }

    this.tax.add(taxItem);
    return this;
  }

  public ProductOfferingPrice removeTaxItem(TaxItem taxItem) {
    if (taxItem != null && this.tax != null) {
      this.tax.remove(taxItem);
    }

    return this;
  }
  /**
   **/
  public ProductOfferingPrice unitOfMeasure(Quantity unitOfMeasure) {
    this.unitOfMeasure = unitOfMeasure;
    return this;
  }

  
  @JsonProperty("unitOfMeasure")
  public Quantity getUnitOfMeasure() {
    return unitOfMeasure;
  }

  @JsonProperty("unitOfMeasure")
  public void setUnitOfMeasure(Quantity unitOfMeasure) {
    this.unitOfMeasure = unitOfMeasure;
  }

  /**
   **/
  public ProductOfferingPrice validFor(TimePeriod validFor) {
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
   * the immediate base class type of this product offering price
   **/
  public ProductOfferingPrice atBaseType(String atBaseType) {
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
   * hyperlink reference to the schema describing this resource
   **/
  public ProductOfferingPrice atSchemaLocation(String atSchemaLocation) {
    this.atSchemaLocation = atSchemaLocation;
    return this;
  }

  
  @JsonProperty("@schemaLocation")
  public String getAtSchemaLocation() {
    return atSchemaLocation;
  }

  @JsonProperty("@schemaLocation")
  public void setAtSchemaLocation(String atSchemaLocation) {
    this.atSchemaLocation = atSchemaLocation;
  }

  /**
   * The class type of this Product offering price
   **/
  public ProductOfferingPrice atType(String atType) {
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
    ProductOfferingPrice productOfferingPrice = (ProductOfferingPrice) o;
    return Objects.equals(this.id, productOfferingPrice.id) &&
        Objects.equals(this.href, productOfferingPrice.href) &&
        Objects.equals(this.description, productOfferingPrice.description) &&
        Objects.equals(this.isBundle, productOfferingPrice.isBundle) &&
        Objects.equals(this.lastUpdate, productOfferingPrice.lastUpdate) &&
        Objects.equals(this.lifecycleStatus, productOfferingPrice.lifecycleStatus) &&
        Objects.equals(this.name, productOfferingPrice.name) &&
        Objects.equals(this.percentage, productOfferingPrice.percentage) &&
        Objects.equals(this.priceType, productOfferingPrice.priceType) &&
        Objects.equals(this.recurringChargePeriodLength, productOfferingPrice.recurringChargePeriodLength) &&
        Objects.equals(this.recurringChargePeriodType, productOfferingPrice.recurringChargePeriodType) &&
        Objects.equals(this.version, productOfferingPrice.version) &&
        Objects.equals(this.bundledPopRelationship, productOfferingPrice.bundledPopRelationship) &&
        Objects.equals(this.constraint, productOfferingPrice.constraint) &&
        Objects.equals(this.place, productOfferingPrice.place) &&
        Objects.equals(this.popRelationship, productOfferingPrice.popRelationship) &&
        Objects.equals(this.price, productOfferingPrice.price) &&
        Objects.equals(this.pricingLogicAlgorithm, productOfferingPrice.pricingLogicAlgorithm) &&
        Objects.equals(this.prodSpecCharValueUse, productOfferingPrice.prodSpecCharValueUse) &&
        Objects.equals(this.productOfferingTerm, productOfferingPrice.productOfferingTerm) &&
        Objects.equals(this.tax, productOfferingPrice.tax) &&
        Objects.equals(this.unitOfMeasure, productOfferingPrice.unitOfMeasure) &&
        Objects.equals(this.validFor, productOfferingPrice.validFor) &&
        Objects.equals(this.atBaseType, productOfferingPrice.atBaseType) &&
        Objects.equals(this.atSchemaLocation, productOfferingPrice.atSchemaLocation) &&
        Objects.equals(this.atType, productOfferingPrice.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, description, isBundle, lastUpdate, lifecycleStatus, name, percentage, priceType, recurringChargePeriodLength, recurringChargePeriodType, version, bundledPopRelationship, constraint, place, popRelationship, price, pricingLogicAlgorithm, prodSpecCharValueUse, productOfferingTerm, tax, unitOfMeasure, validFor, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductOfferingPrice {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    isBundle: ").append(toIndentedString(isBundle)).append("\n");
    sb.append("    lastUpdate: ").append(toIndentedString(lastUpdate)).append("\n");
    sb.append("    lifecycleStatus: ").append(toIndentedString(lifecycleStatus)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    percentage: ").append(toIndentedString(percentage)).append("\n");
    sb.append("    priceType: ").append(toIndentedString(priceType)).append("\n");
    sb.append("    recurringChargePeriodLength: ").append(toIndentedString(recurringChargePeriodLength)).append("\n");
    sb.append("    recurringChargePeriodType: ").append(toIndentedString(recurringChargePeriodType)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    bundledPopRelationship: ").append(toIndentedString(bundledPopRelationship)).append("\n");
    sb.append("    constraint: ").append(toIndentedString(constraint)).append("\n");
    sb.append("    place: ").append(toIndentedString(place)).append("\n");
    sb.append("    popRelationship: ").append(toIndentedString(popRelationship)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    pricingLogicAlgorithm: ").append(toIndentedString(pricingLogicAlgorithm)).append("\n");
    sb.append("    prodSpecCharValueUse: ").append(toIndentedString(prodSpecCharValueUse)).append("\n");
    sb.append("    productOfferingTerm: ").append(toIndentedString(productOfferingTerm)).append("\n");
    sb.append("    tax: ").append(toIndentedString(tax)).append("\n");
    sb.append("    unitOfMeasure: ").append(toIndentedString(unitOfMeasure)).append("\n");
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

