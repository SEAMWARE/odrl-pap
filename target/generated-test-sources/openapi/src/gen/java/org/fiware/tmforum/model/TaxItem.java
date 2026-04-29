package org.fiware.tmforum.model;

import java.net.URI;
import org.fiware.tmforum.model.Money;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A tax item is created for each tax rate and tax type used in the bill.
 **/

@JsonTypeName("TaxItem")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class TaxItem   {
  private @Valid String id;
  private @Valid URI href;
  private @Valid String taxCategory;
  private @Valid Float taxRate;
  private @Valid Money taxAmount;
  private @Valid String atBaseType;
  private @Valid URI atSchemaLocation;
  private @Valid String atType;

  /**
   * unique identifier
   **/
  public TaxItem id(String id) {
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
  public TaxItem href(URI href) {
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
   * Tax category
   **/
  public TaxItem taxCategory(String taxCategory) {
    this.taxCategory = taxCategory;
    return this;
  }

  
  @JsonProperty("taxCategory")
  public String getTaxCategory() {
    return taxCategory;
  }

  @JsonProperty("taxCategory")
  public void setTaxCategory(String taxCategory) {
    this.taxCategory = taxCategory;
  }

  /**
   * Applied rate of the tax
   **/
  public TaxItem taxRate(Float taxRate) {
    this.taxRate = taxRate;
    return this;
  }

  
  @JsonProperty("taxRate")
  public Float getTaxRate() {
    return taxRate;
  }

  @JsonProperty("taxRate")
  public void setTaxRate(Float taxRate) {
    this.taxRate = taxRate;
  }

  /**
   **/
  public TaxItem taxAmount(Money taxAmount) {
    this.taxAmount = taxAmount;
    return this;
  }

  
  @JsonProperty("taxAmount")
  public Money getTaxAmount() {
    return taxAmount;
  }

  @JsonProperty("taxAmount")
  public void setTaxAmount(Money taxAmount) {
    this.taxAmount = taxAmount;
  }

  /**
   * When sub-classing, this defines the super-class
   **/
  public TaxItem atBaseType(String atBaseType) {
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
  public TaxItem atSchemaLocation(URI atSchemaLocation) {
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
  public TaxItem atType(String atType) {
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
    TaxItem taxItem = (TaxItem) o;
    return Objects.equals(this.id, taxItem.id) &&
        Objects.equals(this.href, taxItem.href) &&
        Objects.equals(this.taxCategory, taxItem.taxCategory) &&
        Objects.equals(this.taxRate, taxItem.taxRate) &&
        Objects.equals(this.taxAmount, taxItem.taxAmount) &&
        Objects.equals(this.atBaseType, taxItem.atBaseType) &&
        Objects.equals(this.atSchemaLocation, taxItem.atSchemaLocation) &&
        Objects.equals(this.atType, taxItem.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, taxCategory, taxRate, taxAmount, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TaxItem {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    taxCategory: ").append(toIndentedString(taxCategory)).append("\n");
    sb.append("    taxRate: ").append(toIndentedString(taxRate)).append("\n");
    sb.append("    taxAmount: ").append(toIndentedString(taxAmount)).append("\n");
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

