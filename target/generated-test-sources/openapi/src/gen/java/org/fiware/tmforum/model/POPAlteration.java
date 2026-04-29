package org.fiware.tmforum.model;

import java.net.URI;
import org.fiware.tmforum.model.Duration;
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
 * Is an amount, usually of money, that modifies the price charged for an order item.
 **/

@JsonTypeName("POPAlteration")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class POPAlteration   {
  private @Valid String id;
  private @Valid URI href;
  private @Valid String description;
  private @Valid String name;
  private @Valid String priceType;
  private @Valid Integer priority;
  private @Valid String recurringChargePeriod;
  private @Valid Duration applicationDuration;
  private @Valid ProductPriceValue price;
  private @Valid Quantity unitOfMeasure;
  private @Valid TimePeriod validFor;
  private @Valid String atBaseType;
  private @Valid URI atSchemaLocation;
  private @Valid String atType;

  /**
   * unique identifier
   **/
  public POPAlteration id(String id) {
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
  public POPAlteration href(URI href) {
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
   * A narrative that explains in detail the semantics of this order item price alteration
   **/
  public POPAlteration description(String description) {
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
   * Name given to this price alteration
   **/
  public POPAlteration name(String name) {
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
   * A category that describes the price such as recurring, one time and usage.
   **/
  public POPAlteration priceType(String priceType) {
    this.priceType = priceType;
    return this;
  }

  
  @JsonProperty("priceType")
  @NotNull
  public String getPriceType() {
    return priceType;
  }

  @JsonProperty("priceType")
  public void setPriceType(String priceType) {
    this.priceType = priceType;
  }

  /**
   * Priority level for applying this alteration among all the defined alterations on the order item price
   **/
  public POPAlteration priority(Integer priority) {
    this.priority = priority;
    return this;
  }

  
  @JsonProperty("priority")
  public Integer getPriority() {
    return priority;
  }

  @JsonProperty("priority")
  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  /**
   * Could be month, week...
   **/
  public POPAlteration recurringChargePeriod(String recurringChargePeriod) {
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
   **/
  public POPAlteration applicationDuration(Duration applicationDuration) {
    this.applicationDuration = applicationDuration;
    return this;
  }

  
  @JsonProperty("applicationDuration")
  public Duration getApplicationDuration() {
    return applicationDuration;
  }

  @JsonProperty("applicationDuration")
  public void setApplicationDuration(Duration applicationDuration) {
    this.applicationDuration = applicationDuration;
  }

  /**
   **/
  public POPAlteration price(ProductPriceValue price) {
    this.price = price;
    return this;
  }

  
  @JsonProperty("price")
  @NotNull
  public ProductPriceValue getPrice() {
    return price;
  }

  @JsonProperty("price")
  public void setPrice(ProductPriceValue price) {
    this.price = price;
  }

  /**
   **/
  public POPAlteration unitOfMeasure(Quantity unitOfMeasure) {
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
  public POPAlteration validFor(TimePeriod validFor) {
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
  public POPAlteration atBaseType(String atBaseType) {
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
  public POPAlteration atSchemaLocation(URI atSchemaLocation) {
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
  public POPAlteration atType(String atType) {
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
    POPAlteration poPAlteration = (POPAlteration) o;
    return Objects.equals(this.id, poPAlteration.id) &&
        Objects.equals(this.href, poPAlteration.href) &&
        Objects.equals(this.description, poPAlteration.description) &&
        Objects.equals(this.name, poPAlteration.name) &&
        Objects.equals(this.priceType, poPAlteration.priceType) &&
        Objects.equals(this.priority, poPAlteration.priority) &&
        Objects.equals(this.recurringChargePeriod, poPAlteration.recurringChargePeriod) &&
        Objects.equals(this.applicationDuration, poPAlteration.applicationDuration) &&
        Objects.equals(this.price, poPAlteration.price) &&
        Objects.equals(this.unitOfMeasure, poPAlteration.unitOfMeasure) &&
        Objects.equals(this.validFor, poPAlteration.validFor) &&
        Objects.equals(this.atBaseType, poPAlteration.atBaseType) &&
        Objects.equals(this.atSchemaLocation, poPAlteration.atSchemaLocation) &&
        Objects.equals(this.atType, poPAlteration.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, description, name, priceType, priority, recurringChargePeriod, applicationDuration, price, unitOfMeasure, validFor, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class POPAlteration {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    priceType: ").append(toIndentedString(priceType)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    recurringChargePeriod: ").append(toIndentedString(recurringChargePeriod)).append("\n");
    sb.append("    applicationDuration: ").append(toIndentedString(applicationDuration)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
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

