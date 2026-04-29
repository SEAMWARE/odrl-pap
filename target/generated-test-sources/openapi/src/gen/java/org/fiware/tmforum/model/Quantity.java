package org.fiware.tmforum.model;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * An amount in a given unit
 **/

@JsonTypeName("Quantity")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class Quantity   {
  private @Valid Float amount = 1.0f;
  private @Valid String units;

  /**
   * Numeric value in a given unit
   **/
  public Quantity amount(Float amount) {
    this.amount = amount;
    return this;
  }

  
  @JsonProperty("amount")
  public Float getAmount() {
    return amount;
  }

  @JsonProperty("amount")
  public void setAmount(Float amount) {
    this.amount = amount;
  }

  /**
   * Unit
   **/
  public Quantity units(String units) {
    this.units = units;
    return this;
  }

  
  @JsonProperty("units")
  public String getUnits() {
    return units;
  }

  @JsonProperty("units")
  public void setUnits(String units) {
    this.units = units;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Quantity quantity = (Quantity) o;
    return Objects.equals(this.amount, quantity.amount) &&
        Objects.equals(this.units, quantity.units);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, units);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Quantity {\n");
    
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    units: ").append(toIndentedString(units)).append("\n");
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

