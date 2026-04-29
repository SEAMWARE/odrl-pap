package org.fiware.tmforum.model;

import java.net.URI;
import org.fiware.tmforum.model.TimePeriod;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * specification of a value (number or text or an object) that can be assigned to a Characteristic.
 **/

@JsonTypeName("CharacteristicValueSpecification")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class CharacteristicValueSpecification   {
  private @Valid Boolean isDefault;
  private @Valid String rangeInterval;
  private @Valid String regex;
  private @Valid String unitOfMeasure;
  private @Valid Integer valueFrom;
  private @Valid Integer valueTo;
  private @Valid String valueType;
  private @Valid TimePeriod validFor;
  private @Valid Object value;
  private @Valid String atBaseType;
  private @Valid URI atSchemaLocation;
  private @Valid String atType;

  /**
   * If true, the Boolean Indicates if the value is the default value for a characteristic
   **/
  public CharacteristicValueSpecification isDefault(Boolean isDefault) {
    this.isDefault = isDefault;
    return this;
  }

  
  @JsonProperty("isDefault")
  public Boolean getIsDefault() {
    return isDefault;
  }

  @JsonProperty("isDefault")
  public void setIsDefault(Boolean isDefault) {
    this.isDefault = isDefault;
  }

  /**
   * An indicator that specifies the inclusion or exclusion of the valueFrom and valueTo attributes. If applicable, possible values are \&quot;open\&quot;, \&quot;closed\&quot;, \&quot;closedBottom\&quot; and \&quot;closedTop\&quot;.
   **/
  public CharacteristicValueSpecification rangeInterval(String rangeInterval) {
    this.rangeInterval = rangeInterval;
    return this;
  }

  
  @JsonProperty("rangeInterval")
  public String getRangeInterval() {
    return rangeInterval;
  }

  @JsonProperty("rangeInterval")
  public void setRangeInterval(String rangeInterval) {
    this.rangeInterval = rangeInterval;
  }

  /**
   * A regular expression constraint for given value
   **/
  public CharacteristicValueSpecification regex(String regex) {
    this.regex = regex;
    return this;
  }

  
  @JsonProperty("regex")
  public String getRegex() {
    return regex;
  }

  @JsonProperty("regex")
  public void setRegex(String regex) {
    this.regex = regex;
  }

  /**
   * A length, surface, volume, dry measure, liquid measure, money, weight, time, and the like. In general, a determinate quantity or magnitude of the kind designated, taken as a standard of comparison for others of the same kind, in assigning to them numerical values, as 1 foot, 1 yard, 1 mile, 1 square foot.
   **/
  public CharacteristicValueSpecification unitOfMeasure(String unitOfMeasure) {
    this.unitOfMeasure = unitOfMeasure;
    return this;
  }

  
  @JsonProperty("unitOfMeasure")
  public String getUnitOfMeasure() {
    return unitOfMeasure;
  }

  @JsonProperty("unitOfMeasure")
  public void setUnitOfMeasure(String unitOfMeasure) {
    this.unitOfMeasure = unitOfMeasure;
  }

  /**
   * The low range value that a characteristic can take on
   **/
  public CharacteristicValueSpecification valueFrom(Integer valueFrom) {
    this.valueFrom = valueFrom;
    return this;
  }

  
  @JsonProperty("valueFrom")
  public Integer getValueFrom() {
    return valueFrom;
  }

  @JsonProperty("valueFrom")
  public void setValueFrom(Integer valueFrom) {
    this.valueFrom = valueFrom;
  }

  /**
   * The upper range value that a characteristic can take on
   **/
  public CharacteristicValueSpecification valueTo(Integer valueTo) {
    this.valueTo = valueTo;
    return this;
  }

  
  @JsonProperty("valueTo")
  public Integer getValueTo() {
    return valueTo;
  }

  @JsonProperty("valueTo")
  public void setValueTo(Integer valueTo) {
    this.valueTo = valueTo;
  }

  /**
   * A kind of value that the characteristic value can take on, such as numeric, text and so forth
   **/
  public CharacteristicValueSpecification valueType(String valueType) {
    this.valueType = valueType;
    return this;
  }

  
  @JsonProperty("valueType")
  public String getValueType() {
    return valueType;
  }

  @JsonProperty("valueType")
  public void setValueType(String valueType) {
    this.valueType = valueType;
  }

  /**
   **/
  public CharacteristicValueSpecification validFor(TimePeriod validFor) {
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
   **/
  public CharacteristicValueSpecification value(Object value) {
    this.value = value;
    return this;
  }

  
  @JsonProperty("value")
  public Object getValue() {
    return value;
  }

  @JsonProperty("value")
  public void setValue(Object value) {
    this.value = value;
  }

  /**
   * When sub-classing, this defines the super-class
   **/
  public CharacteristicValueSpecification atBaseType(String atBaseType) {
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
  public CharacteristicValueSpecification atSchemaLocation(URI atSchemaLocation) {
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
  public CharacteristicValueSpecification atType(String atType) {
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
    CharacteristicValueSpecification characteristicValueSpecification = (CharacteristicValueSpecification) o;
    return Objects.equals(this.isDefault, characteristicValueSpecification.isDefault) &&
        Objects.equals(this.rangeInterval, characteristicValueSpecification.rangeInterval) &&
        Objects.equals(this.regex, characteristicValueSpecification.regex) &&
        Objects.equals(this.unitOfMeasure, characteristicValueSpecification.unitOfMeasure) &&
        Objects.equals(this.valueFrom, characteristicValueSpecification.valueFrom) &&
        Objects.equals(this.valueTo, characteristicValueSpecification.valueTo) &&
        Objects.equals(this.valueType, characteristicValueSpecification.valueType) &&
        Objects.equals(this.validFor, characteristicValueSpecification.validFor) &&
        Objects.equals(this.value, characteristicValueSpecification.value) &&
        Objects.equals(this.atBaseType, characteristicValueSpecification.atBaseType) &&
        Objects.equals(this.atSchemaLocation, characteristicValueSpecification.atSchemaLocation) &&
        Objects.equals(this.atType, characteristicValueSpecification.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isDefault, rangeInterval, regex, unitOfMeasure, valueFrom, valueTo, valueType, validFor, value, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacteristicValueSpecification {\n");
    
    sb.append("    isDefault: ").append(toIndentedString(isDefault)).append("\n");
    sb.append("    rangeInterval: ").append(toIndentedString(rangeInterval)).append("\n");
    sb.append("    regex: ").append(toIndentedString(regex)).append("\n");
    sb.append("    unitOfMeasure: ").append(toIndentedString(unitOfMeasure)).append("\n");
    sb.append("    valueFrom: ").append(toIndentedString(valueFrom)).append("\n");
    sb.append("    valueTo: ").append(toIndentedString(valueTo)).append("\n");
    sb.append("    valueType: ").append(toIndentedString(valueType)).append("\n");
    sb.append("    validFor: ").append(toIndentedString(validFor)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

