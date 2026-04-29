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
 * This class defines a characteristic specification.
 **/

@JsonTypeName("CharacteristicSpecificationBase")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class CharacteristicSpecificationBase   {
  private @Valid String id;
  private @Valid Boolean configurable;
  private @Valid String description;
  private @Valid Boolean extensible;
  private @Valid Boolean isUnique;
  private @Valid Integer maxCardinality;
  private @Valid Integer minCardinality;
  private @Valid String name;
  private @Valid String regex;
  private @Valid String valueType;
  private @Valid TimePeriod validFor;
  private @Valid String atBaseType;
  private @Valid URI atSchemaLocation;
  private @Valid String atType;
  private @Valid String atValueSchemaLocation;

  /**
   * Unique ID for the characteristic
   **/
  public CharacteristicSpecificationBase id(String id) {
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
   * If true, the Boolean indicates that the target Characteristic is configurable
   **/
  public CharacteristicSpecificationBase configurable(Boolean configurable) {
    this.configurable = configurable;
    return this;
  }

  
  @JsonProperty("configurable")
  public Boolean getConfigurable() {
    return configurable;
  }

  @JsonProperty("configurable")
  public void setConfigurable(Boolean configurable) {
    this.configurable = configurable;
  }

  /**
   * A narrative that explains the CharacteristicSpecification.
   **/
  public CharacteristicSpecificationBase description(String description) {
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
   * An indicator that specifies that the values for the characteristic can be extended by adding new values when instantiating a characteristic for a resource.
   **/
  public CharacteristicSpecificationBase extensible(Boolean extensible) {
    this.extensible = extensible;
    return this;
  }

  
  @JsonProperty("extensible")
  public Boolean getExtensible() {
    return extensible;
  }

  @JsonProperty("extensible")
  public void setExtensible(Boolean extensible) {
    this.extensible = extensible;
  }

  /**
   * An indicator that specifies if a value is unique for the specification. Possible values are; \&quot;unique while value is in effect\&quot; and \&quot;unique whether value is in effect or not\&quot;
   **/
  public CharacteristicSpecificationBase isUnique(Boolean isUnique) {
    this.isUnique = isUnique;
    return this;
  }

  
  @JsonProperty("isUnique")
  public Boolean getIsUnique() {
    return isUnique;
  }

  @JsonProperty("isUnique")
  public void setIsUnique(Boolean isUnique) {
    this.isUnique = isUnique;
  }

  /**
   * The maximum number of instances a CharacteristicValue can take on. For example, zero to five phone numbers in a group calling plan, where five is the value for the maxCardinality.
   **/
  public CharacteristicSpecificationBase maxCardinality(Integer maxCardinality) {
    this.maxCardinality = maxCardinality;
    return this;
  }

  
  @JsonProperty("maxCardinality")
  public Integer getMaxCardinality() {
    return maxCardinality;
  }

  @JsonProperty("maxCardinality")
  public void setMaxCardinality(Integer maxCardinality) {
    this.maxCardinality = maxCardinality;
  }

  /**
   * The minimum number of instances a CharacteristicValue can take on. For example, zero to five phone numbers in a group calling plan, where zero is the value for the minCardinality.
   **/
  public CharacteristicSpecificationBase minCardinality(Integer minCardinality) {
    this.minCardinality = minCardinality;
    return this;
  }

  
  @JsonProperty("minCardinality")
  public Integer getMinCardinality() {
    return minCardinality;
  }

  @JsonProperty("minCardinality")
  public void setMinCardinality(Integer minCardinality) {
    this.minCardinality = minCardinality;
  }

  /**
   * A word, term, or phrase by which this characteristic specification is known and distinguished from other characteristic specifications.
   **/
  public CharacteristicSpecificationBase name(String name) {
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
   * A rule or principle represented in regular expression used to derive the value of a characteristic value.
   **/
  public CharacteristicSpecificationBase regex(String regex) {
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
   * A kind of value that the characteristic can take on, such as numeric, text and so forth
   **/
  public CharacteristicSpecificationBase valueType(String valueType) {
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
  public CharacteristicSpecificationBase validFor(TimePeriod validFor) {
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
  public CharacteristicSpecificationBase atBaseType(String atBaseType) {
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
  public CharacteristicSpecificationBase atSchemaLocation(URI atSchemaLocation) {
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
  public CharacteristicSpecificationBase atType(String atType) {
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
   * This (optional) field provides a link to the schema describing the value type.
   **/
  public CharacteristicSpecificationBase atValueSchemaLocation(String atValueSchemaLocation) {
    this.atValueSchemaLocation = atValueSchemaLocation;
    return this;
  }

  
  @JsonProperty("@valueSchemaLocation")
  public String getAtValueSchemaLocation() {
    return atValueSchemaLocation;
  }

  @JsonProperty("@valueSchemaLocation")
  public void setAtValueSchemaLocation(String atValueSchemaLocation) {
    this.atValueSchemaLocation = atValueSchemaLocation;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacteristicSpecificationBase characteristicSpecificationBase = (CharacteristicSpecificationBase) o;
    return Objects.equals(this.id, characteristicSpecificationBase.id) &&
        Objects.equals(this.configurable, characteristicSpecificationBase.configurable) &&
        Objects.equals(this.description, characteristicSpecificationBase.description) &&
        Objects.equals(this.extensible, characteristicSpecificationBase.extensible) &&
        Objects.equals(this.isUnique, characteristicSpecificationBase.isUnique) &&
        Objects.equals(this.maxCardinality, characteristicSpecificationBase.maxCardinality) &&
        Objects.equals(this.minCardinality, characteristicSpecificationBase.minCardinality) &&
        Objects.equals(this.name, characteristicSpecificationBase.name) &&
        Objects.equals(this.regex, characteristicSpecificationBase.regex) &&
        Objects.equals(this.valueType, characteristicSpecificationBase.valueType) &&
        Objects.equals(this.validFor, characteristicSpecificationBase.validFor) &&
        Objects.equals(this.atBaseType, characteristicSpecificationBase.atBaseType) &&
        Objects.equals(this.atSchemaLocation, characteristicSpecificationBase.atSchemaLocation) &&
        Objects.equals(this.atType, characteristicSpecificationBase.atType) &&
        Objects.equals(this.atValueSchemaLocation, characteristicSpecificationBase.atValueSchemaLocation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, configurable, description, extensible, isUnique, maxCardinality, minCardinality, name, regex, valueType, validFor, atBaseType, atSchemaLocation, atType, atValueSchemaLocation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacteristicSpecificationBase {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    configurable: ").append(toIndentedString(configurable)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    extensible: ").append(toIndentedString(extensible)).append("\n");
    sb.append("    isUnique: ").append(toIndentedString(isUnique)).append("\n");
    sb.append("    maxCardinality: ").append(toIndentedString(maxCardinality)).append("\n");
    sb.append("    minCardinality: ").append(toIndentedString(minCardinality)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    regex: ").append(toIndentedString(regex)).append("\n");
    sb.append("    valueType: ").append(toIndentedString(valueType)).append("\n");
    sb.append("    validFor: ").append(toIndentedString(validFor)).append("\n");
    sb.append("    atBaseType: ").append(toIndentedString(atBaseType)).append("\n");
    sb.append("    atSchemaLocation: ").append(toIndentedString(atSchemaLocation)).append("\n");
    sb.append("    atType: ").append(toIndentedString(atType)).append("\n");
    sb.append("    atValueSchemaLocation: ").append(toIndentedString(atValueSchemaLocation)).append("\n");
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

