package org.fiware.tmforum.model;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.fiware.tmforum.model.ConstraintRef;
import org.fiware.tmforum.model.POPAlteration;
import org.fiware.tmforum.model.ProductPriceValue;
import org.fiware.tmforum.model.Quantity;
import org.fiware.tmforum.model.TimePeriod;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A new product offering price being created by value or a reference to an existing product offering price that alreasy created. The polymorphic attributes @type, @schemaLocation &amp; @referredType are related to the product offering price and not to this ReforValue structure
 **/

@JsonTypeName("ProductOfferingPriceRefOrValue")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class ProductOfferingPriceRefOrValue   {
  private @Valid String id;
  private @Valid URI href;
  private @Valid String description;
  private @Valid OffsetDateTime lastUpdate;
  private @Valid String lifecycleStatus;
  private @Valid String name;
  private @Valid String priceType;
  private @Valid String recurringChargePeriod;
  private @Valid Integer recurringChargePeriodLength;
  private @Valid String version;
  private @Valid List<@Valid ConstraintRef> constraint;
  private @Valid ProductPriceValue price;
  private @Valid List<@Valid POPAlteration> priceAlteration;
  private @Valid Quantity unitOfMeasure;
  private @Valid TimePeriod validFor;
  private @Valid String atBaseType;
  private @Valid URI atSchemaLocation;
  private @Valid String atType;
  private @Valid String atReferredType;

  /**
   * unique identifier
   **/
  public ProductOfferingPriceRefOrValue id(String id) {
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
  public ProductOfferingPriceRefOrValue href(URI href) {
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
   * Description of the productOfferingPrice
   **/
  public ProductOfferingPriceRefOrValue description(String description) {
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
   * the last update time of this ProductOfferingPrice
   **/
  public ProductOfferingPriceRefOrValue lastUpdate(OffsetDateTime lastUpdate) {
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
  public ProductOfferingPriceRefOrValue lifecycleStatus(String lifecycleStatus) {
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
  public ProductOfferingPriceRefOrValue name(String name) {
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
   * A category that describes the price charge, such as recurring, penalty, One time fee and so forth.
   **/
  public ProductOfferingPriceRefOrValue priceType(String priceType) {
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
   * The period type to repeat the application of the price Could be month, week...
   **/
  public ProductOfferingPriceRefOrValue recurringChargePeriod(String recurringChargePeriod) {
    this.recurringChargePeriod = recurringChargePeriod;
    return this;
  }

  
  @JsonProperty("recurringChargePeriod")
  public String getRecurringChargePeriod() {
    return recurringChargePeriod;
  }

  @JsonProperty("recurringChargePeriod")
  public void setRecurringChargePeriod(String recurringChargePeriod) {
    this.recurringChargePeriod = recurringChargePeriod;
  }

  /**
   * the period of the recurring charge:  1, 2, ... .It sets to zero if it is not applicable
   **/
  public ProductOfferingPriceRefOrValue recurringChargePeriodLength(Integer recurringChargePeriodLength) {
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
   * ProductOffering version
   **/
  public ProductOfferingPriceRefOrValue version(String version) {
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
   * The Constraint resource represents a policy/rule applied to ProductOfferingPrice.
   **/
  public ProductOfferingPriceRefOrValue constraint(List<@Valid ConstraintRef> constraint) {
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

  public ProductOfferingPriceRefOrValue addConstraintItem(ConstraintRef constraintItem) {
    if (this.constraint == null) {
      this.constraint = new ArrayList<>();
    }

    this.constraint.add(constraintItem);
    return this;
  }

  public ProductOfferingPriceRefOrValue removeConstraintItem(ConstraintRef constraintItem) {
    if (constraintItem != null && this.constraint != null) {
      this.constraint.remove(constraintItem);
    }

    return this;
  }
  /**
   **/
  public ProductOfferingPriceRefOrValue price(ProductPriceValue price) {
    this.price = price;
    return this;
  }

  
  @JsonProperty("price")
  public ProductPriceValue getPrice() {
    return price;
  }

  @JsonProperty("price")
  public void setPrice(ProductPriceValue price) {
    this.price = price;
  }

  /**
   **/
  public ProductOfferingPriceRefOrValue priceAlteration(List<@Valid POPAlteration> priceAlteration) {
    this.priceAlteration = priceAlteration;
    return this;
  }

  
  @JsonProperty("priceAlteration")
  public List<POPAlteration> getPriceAlteration() {
    return priceAlteration;
  }

  @JsonProperty("priceAlteration")
  public void setPriceAlteration(List<@Valid POPAlteration> priceAlteration) {
    this.priceAlteration = priceAlteration;
  }

  public ProductOfferingPriceRefOrValue addPriceAlterationItem(POPAlteration priceAlterationItem) {
    if (this.priceAlteration == null) {
      this.priceAlteration = new ArrayList<>();
    }

    this.priceAlteration.add(priceAlterationItem);
    return this;
  }

  public ProductOfferingPriceRefOrValue removePriceAlterationItem(POPAlteration priceAlterationItem) {
    if (priceAlterationItem != null && this.priceAlteration != null) {
      this.priceAlteration.remove(priceAlterationItem);
    }

    return this;
  }
  /**
   **/
  public ProductOfferingPriceRefOrValue unitOfMeasure(Quantity unitOfMeasure) {
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
  public ProductOfferingPriceRefOrValue validFor(TimePeriod validFor) {
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
  public ProductOfferingPriceRefOrValue atBaseType(String atBaseType) {
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
  public ProductOfferingPriceRefOrValue atSchemaLocation(URI atSchemaLocation) {
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
  public ProductOfferingPriceRefOrValue atType(String atType) {
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
  public ProductOfferingPriceRefOrValue atReferredType(String atReferredType) {
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
    ProductOfferingPriceRefOrValue productOfferingPriceRefOrValue = (ProductOfferingPriceRefOrValue) o;
    return Objects.equals(this.id, productOfferingPriceRefOrValue.id) &&
        Objects.equals(this.href, productOfferingPriceRefOrValue.href) &&
        Objects.equals(this.description, productOfferingPriceRefOrValue.description) &&
        Objects.equals(this.lastUpdate, productOfferingPriceRefOrValue.lastUpdate) &&
        Objects.equals(this.lifecycleStatus, productOfferingPriceRefOrValue.lifecycleStatus) &&
        Objects.equals(this.name, productOfferingPriceRefOrValue.name) &&
        Objects.equals(this.priceType, productOfferingPriceRefOrValue.priceType) &&
        Objects.equals(this.recurringChargePeriod, productOfferingPriceRefOrValue.recurringChargePeriod) &&
        Objects.equals(this.recurringChargePeriodLength, productOfferingPriceRefOrValue.recurringChargePeriodLength) &&
        Objects.equals(this.version, productOfferingPriceRefOrValue.version) &&
        Objects.equals(this.constraint, productOfferingPriceRefOrValue.constraint) &&
        Objects.equals(this.price, productOfferingPriceRefOrValue.price) &&
        Objects.equals(this.priceAlteration, productOfferingPriceRefOrValue.priceAlteration) &&
        Objects.equals(this.unitOfMeasure, productOfferingPriceRefOrValue.unitOfMeasure) &&
        Objects.equals(this.validFor, productOfferingPriceRefOrValue.validFor) &&
        Objects.equals(this.atBaseType, productOfferingPriceRefOrValue.atBaseType) &&
        Objects.equals(this.atSchemaLocation, productOfferingPriceRefOrValue.atSchemaLocation) &&
        Objects.equals(this.atType, productOfferingPriceRefOrValue.atType) &&
        Objects.equals(this.atReferredType, productOfferingPriceRefOrValue.atReferredType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, description, lastUpdate, lifecycleStatus, name, priceType, recurringChargePeriod, recurringChargePeriodLength, version, constraint, price, priceAlteration, unitOfMeasure, validFor, atBaseType, atSchemaLocation, atType, atReferredType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductOfferingPriceRefOrValue {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    lastUpdate: ").append(toIndentedString(lastUpdate)).append("\n");
    sb.append("    lifecycleStatus: ").append(toIndentedString(lifecycleStatus)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    priceType: ").append(toIndentedString(priceType)).append("\n");
    sb.append("    recurringChargePeriod: ").append(toIndentedString(recurringChargePeriod)).append("\n");
    sb.append("    recurringChargePeriodLength: ").append(toIndentedString(recurringChargePeriodLength)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    constraint: ").append(toIndentedString(constraint)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    priceAlteration: ").append(toIndentedString(priceAlteration)).append("\n");
    sb.append("    unitOfMeasure: ").append(toIndentedString(unitOfMeasure)).append("\n");
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

