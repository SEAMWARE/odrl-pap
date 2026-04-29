package org.fiware.tmforum.model;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A base / value business entity used to represent money
 **/

@JsonTypeName("Money")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class Money   {
  private @Valid String unit;
  private @Valid Float value;

  /**
   * Currency (ISO4217 norm uses 3 letters to define the currency)
   **/
  public Money unit(String unit) {
    this.unit = unit;
    return this;
  }

  
  @JsonProperty("unit")
  public String getUnit() {
    return unit;
  }

  @JsonProperty("unit")
  public void setUnit(String unit) {
    this.unit = unit;
  }

  /**
   * A positive floating point number
   **/
  public Money value(Float value) {
    this.value = value;
    return this;
  }

  
  @JsonProperty("value")
  public Float getValue() {
    return value;
  }

  @JsonProperty("value")
  public void setValue(Float value) {
    this.value = value;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Money money = (Money) o;
    return Objects.equals(this.unit, money.unit) &&
        Objects.equals(this.value, money.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(unit, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Money {\n");
    
    sb.append("    unit: ").append(toIndentedString(unit)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

