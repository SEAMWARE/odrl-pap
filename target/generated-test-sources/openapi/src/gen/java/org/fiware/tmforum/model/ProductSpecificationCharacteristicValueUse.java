package org.fiware.tmforum.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.fiware.tmforum.model.CharacteristicValueSpecification;
import org.fiware.tmforum.model.ProductSpecificationRef;
import org.fiware.tmforum.model.TimePeriod;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A use of the ProductSpecificationCharacteristicValue by a ProductOffering to which additional properties (attributes) apply or override the properties of similar properties contained in ProductSpecificationCharacteristicValue. It should be noted that characteristics which their value(s) addressed by this object must exist in corresponding product specification. The available characteristic values for a ProductSpecificationCharacteristic in a Product specification can be modified at the ProductOffering level. For example, a characteristic &#39;Color&#39; might have values White, Blue, Green, and Red. But, the list of values can be restricted to e.g. White and Blue in an associated product offering. It should be noted that the list of values in &#39;ProductSpecificationCharacteristicValueUse&#39; is a strict subset of the list of values as defined in the corresponding product specification characteristics.
 **/

@JsonTypeName("ProductSpecificationCharacteristicValueUse")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class ProductSpecificationCharacteristicValueUse   {
  private @Valid String id;
  private @Valid String description;
  private @Valid Integer maxCardinality;
  private @Valid Integer minCardinality;
  private @Valid String name;
  private @Valid String valueType;
  private @Valid List<@Valid CharacteristicValueSpecification> productSpecCharacteristicValue;
  private @Valid ProductSpecificationRef productSpecification;
  private @Valid TimePeriod validFor;
  private @Valid String atBaseType;
  private @Valid URI atSchemaLocation;
  private @Valid String atType;

  /**
   * Unique ID for the characteristic
   **/
  public ProductSpecificationCharacteristicValueUse id(String id) {
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
   * A narrative that explains in detail what the productSpecificationCharacteristic is
   **/
  public ProductSpecificationCharacteristicValueUse description(String description) {
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
   * The maximum number of instances a CharacteristicValue can take on. For example, zero to five phone numbers in a group calling plan, where five is the value for the maxCardinality.
   **/
  public ProductSpecificationCharacteristicValueUse maxCardinality(Integer maxCardinality) {
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
  public ProductSpecificationCharacteristicValueUse minCardinality(Integer minCardinality) {
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
   * Name of the associated productSpecificationCharacteristic
   **/
  public ProductSpecificationCharacteristicValueUse name(String name) {
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
   * A kind of value that the characteristic can take on, such as numeric, text and so forth
   **/
  public ProductSpecificationCharacteristicValueUse valueType(String valueType) {
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
   * A number or text that can be assigned to a ProductSpecificationCharacteristic.
   **/
  public ProductSpecificationCharacteristicValueUse productSpecCharacteristicValue(List<@Valid CharacteristicValueSpecification> productSpecCharacteristicValue) {
    this.productSpecCharacteristicValue = productSpecCharacteristicValue;
    return this;
  }

  
  @JsonProperty("productSpecCharacteristicValue")
  public List<CharacteristicValueSpecification> getProductSpecCharacteristicValue() {
    return productSpecCharacteristicValue;
  }

  @JsonProperty("productSpecCharacteristicValue")
  public void setProductSpecCharacteristicValue(List<@Valid CharacteristicValueSpecification> productSpecCharacteristicValue) {
    this.productSpecCharacteristicValue = productSpecCharacteristicValue;
  }

  public ProductSpecificationCharacteristicValueUse addProductSpecCharacteristicValueItem(CharacteristicValueSpecification productSpecCharacteristicValueItem) {
    if (this.productSpecCharacteristicValue == null) {
      this.productSpecCharacteristicValue = new ArrayList<>();
    }

    this.productSpecCharacteristicValue.add(productSpecCharacteristicValueItem);
    return this;
  }

  public ProductSpecificationCharacteristicValueUse removeProductSpecCharacteristicValueItem(CharacteristicValueSpecification productSpecCharacteristicValueItem) {
    if (productSpecCharacteristicValueItem != null && this.productSpecCharacteristicValue != null) {
      this.productSpecCharacteristicValue.remove(productSpecCharacteristicValueItem);
    }

    return this;
  }
  /**
   **/
  public ProductSpecificationCharacteristicValueUse productSpecification(ProductSpecificationRef productSpecification) {
    this.productSpecification = productSpecification;
    return this;
  }

  
  @JsonProperty("productSpecification")
  public ProductSpecificationRef getProductSpecification() {
    return productSpecification;
  }

  @JsonProperty("productSpecification")
  public void setProductSpecification(ProductSpecificationRef productSpecification) {
    this.productSpecification = productSpecification;
  }

  /**
   **/
  public ProductSpecificationCharacteristicValueUse validFor(TimePeriod validFor) {
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
  public ProductSpecificationCharacteristicValueUse atBaseType(String atBaseType) {
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
  public ProductSpecificationCharacteristicValueUse atSchemaLocation(URI atSchemaLocation) {
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
  public ProductSpecificationCharacteristicValueUse atType(String atType) {
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
    ProductSpecificationCharacteristicValueUse productSpecificationCharacteristicValueUse = (ProductSpecificationCharacteristicValueUse) o;
    return Objects.equals(this.id, productSpecificationCharacteristicValueUse.id) &&
        Objects.equals(this.description, productSpecificationCharacteristicValueUse.description) &&
        Objects.equals(this.maxCardinality, productSpecificationCharacteristicValueUse.maxCardinality) &&
        Objects.equals(this.minCardinality, productSpecificationCharacteristicValueUse.minCardinality) &&
        Objects.equals(this.name, productSpecificationCharacteristicValueUse.name) &&
        Objects.equals(this.valueType, productSpecificationCharacteristicValueUse.valueType) &&
        Objects.equals(this.productSpecCharacteristicValue, productSpecificationCharacteristicValueUse.productSpecCharacteristicValue) &&
        Objects.equals(this.productSpecification, productSpecificationCharacteristicValueUse.productSpecification) &&
        Objects.equals(this.validFor, productSpecificationCharacteristicValueUse.validFor) &&
        Objects.equals(this.atBaseType, productSpecificationCharacteristicValueUse.atBaseType) &&
        Objects.equals(this.atSchemaLocation, productSpecificationCharacteristicValueUse.atSchemaLocation) &&
        Objects.equals(this.atType, productSpecificationCharacteristicValueUse.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, description, maxCardinality, minCardinality, name, valueType, productSpecCharacteristicValue, productSpecification, validFor, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductSpecificationCharacteristicValueUse {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    maxCardinality: ").append(toIndentedString(maxCardinality)).append("\n");
    sb.append("    minCardinality: ").append(toIndentedString(minCardinality)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    valueType: ").append(toIndentedString(valueType)).append("\n");
    sb.append("    productSpecCharacteristicValue: ").append(toIndentedString(productSpecCharacteristicValue)).append("\n");
    sb.append("    productSpecification: ").append(toIndentedString(productSpecification)).append("\n");
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

