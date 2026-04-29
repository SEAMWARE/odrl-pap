package org.fiware.tmforum.model;

import org.fiware.tmforum.model.ProductOffering;
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

@JsonTypeName("ProductOfferingAttributeValueChangeEventPayload")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class ProductOfferingAttributeValueChangeEventPayload   {
  private @Valid ProductOffering productOffering;

  /**
   **/
  public ProductOfferingAttributeValueChangeEventPayload productOffering(ProductOffering productOffering) {
    this.productOffering = productOffering;
    return this;
  }

  
  @JsonProperty("productOffering")
  public ProductOffering getProductOffering() {
    return productOffering;
  }

  @JsonProperty("productOffering")
  public void setProductOffering(ProductOffering productOffering) {
    this.productOffering = productOffering;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductOfferingAttributeValueChangeEventPayload productOfferingAttributeValueChangeEventPayload = (ProductOfferingAttributeValueChangeEventPayload) o;
    return Objects.equals(this.productOffering, productOfferingAttributeValueChangeEventPayload.productOffering);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productOffering);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductOfferingAttributeValueChangeEventPayload {\n");
    
    sb.append("    productOffering: ").append(toIndentedString(productOffering)).append("\n");
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

