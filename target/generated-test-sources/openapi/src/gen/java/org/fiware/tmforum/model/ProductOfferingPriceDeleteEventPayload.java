package org.fiware.tmforum.model;

import org.fiware.tmforum.model.ProductOfferingPrice;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The event data structure
 **/

@JsonTypeName("ProductOfferingPriceDeleteEventPayload")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class ProductOfferingPriceDeleteEventPayload   {
  private @Valid ProductOfferingPrice productOfferingPrice;

  /**
   **/
  public ProductOfferingPriceDeleteEventPayload productOfferingPrice(ProductOfferingPrice productOfferingPrice) {
    this.productOfferingPrice = productOfferingPrice;
    return this;
  }

  
  @JsonProperty("productOfferingPrice")
  public ProductOfferingPrice getProductOfferingPrice() {
    return productOfferingPrice;
  }

  @JsonProperty("productOfferingPrice")
  public void setProductOfferingPrice(ProductOfferingPrice productOfferingPrice) {
    this.productOfferingPrice = productOfferingPrice;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductOfferingPriceDeleteEventPayload productOfferingPriceDeleteEventPayload = (ProductOfferingPriceDeleteEventPayload) o;
    return Objects.equals(this.productOfferingPrice, productOfferingPriceDeleteEventPayload.productOfferingPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productOfferingPrice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductOfferingPriceDeleteEventPayload {\n");
    
    sb.append("    productOfferingPrice: ").append(toIndentedString(productOfferingPrice)).append("\n");
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

