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
 * Provides all amounts (tax included, duty free, tax rate), used currency and percentage to apply for Price Alteration.
 **/

@JsonTypeName("ProductPriceValue")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class ProductPriceValue   {
  private @Valid Float percentage;
  private @Valid String taxCategory;
  private @Valid Float taxRate;
  private @Valid Money dutyFreeAmount;
  private @Valid Money taxIncludedAmount;
  private @Valid String atBaseType;
  private @Valid URI atSchemaLocation;
  private @Valid String atType;

  /**
   * Percentage to apply for ProdOfferPriceAlteration
   **/
  public ProductPriceValue percentage(Float percentage) {
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
   * Tax category
   **/
  public ProductPriceValue taxCategory(String taxCategory) {
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
   * Tax rate
   **/
  public ProductPriceValue taxRate(Float taxRate) {
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
  public ProductPriceValue dutyFreeAmount(Money dutyFreeAmount) {
    this.dutyFreeAmount = dutyFreeAmount;
    return this;
  }

  
  @JsonProperty("dutyFreeAmount")
  public Money getDutyFreeAmount() {
    return dutyFreeAmount;
  }

  @JsonProperty("dutyFreeAmount")
  public void setDutyFreeAmount(Money dutyFreeAmount) {
    this.dutyFreeAmount = dutyFreeAmount;
  }

  /**
   **/
  public ProductPriceValue taxIncludedAmount(Money taxIncludedAmount) {
    this.taxIncludedAmount = taxIncludedAmount;
    return this;
  }

  
  @JsonProperty("taxIncludedAmount")
  public Money getTaxIncludedAmount() {
    return taxIncludedAmount;
  }

  @JsonProperty("taxIncludedAmount")
  public void setTaxIncludedAmount(Money taxIncludedAmount) {
    this.taxIncludedAmount = taxIncludedAmount;
  }

  /**
   * When sub-classing, this defines the super-class
   **/
  public ProductPriceValue atBaseType(String atBaseType) {
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
  public ProductPriceValue atSchemaLocation(URI atSchemaLocation) {
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
  public ProductPriceValue atType(String atType) {
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
    ProductPriceValue productPriceValue = (ProductPriceValue) o;
    return Objects.equals(this.percentage, productPriceValue.percentage) &&
        Objects.equals(this.taxCategory, productPriceValue.taxCategory) &&
        Objects.equals(this.taxRate, productPriceValue.taxRate) &&
        Objects.equals(this.dutyFreeAmount, productPriceValue.dutyFreeAmount) &&
        Objects.equals(this.taxIncludedAmount, productPriceValue.taxIncludedAmount) &&
        Objects.equals(this.atBaseType, productPriceValue.atBaseType) &&
        Objects.equals(this.atSchemaLocation, productPriceValue.atSchemaLocation) &&
        Objects.equals(this.atType, productPriceValue.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(percentage, taxCategory, taxRate, dutyFreeAmount, taxIncludedAmount, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductPriceValue {\n");
    
    sb.append("    percentage: ").append(toIndentedString(percentage)).append("\n");
    sb.append("    taxCategory: ").append(toIndentedString(taxCategory)).append("\n");
    sb.append("    taxRate: ").append(toIndentedString(taxRate)).append("\n");
    sb.append("    dutyFreeAmount: ").append(toIndentedString(dutyFreeAmount)).append("\n");
    sb.append("    taxIncludedAmount: ").append(toIndentedString(taxIncludedAmount)).append("\n");
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

