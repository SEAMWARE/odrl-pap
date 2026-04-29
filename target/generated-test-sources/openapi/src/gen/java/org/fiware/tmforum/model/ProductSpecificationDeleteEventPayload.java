package org.fiware.tmforum.model;

import org.fiware.tmforum.model.ProductSpecification;
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

@JsonTypeName("ProductSpecificationDeleteEventPayload")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class ProductSpecificationDeleteEventPayload   {
  private @Valid ProductSpecification productSpecification;

  /**
   **/
  public ProductSpecificationDeleteEventPayload productSpecification(ProductSpecification productSpecification) {
    this.productSpecification = productSpecification;
    return this;
  }

  
  @JsonProperty("productSpecification")
  public ProductSpecification getProductSpecification() {
    return productSpecification;
  }

  @JsonProperty("productSpecification")
  public void setProductSpecification(ProductSpecification productSpecification) {
    this.productSpecification = productSpecification;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductSpecificationDeleteEventPayload productSpecificationDeleteEventPayload = (ProductSpecificationDeleteEventPayload) o;
    return Objects.equals(this.productSpecification, productSpecificationDeleteEventPayload.productSpecification);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productSpecification);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductSpecificationDeleteEventPayload {\n");
    
    sb.append("    productSpecification: ").append(toIndentedString(productSpecification)).append("\n");
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

