package org.fiware.tmforum.model;

import java.net.URI;
import org.fiware.tmforum.model.Quantity;
import org.fiware.tmforum.model.TimePeriod;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Complements the description of an element (for instance a product) through video, pictures...
 **/

@JsonTypeName("Attachment")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class Attachment   {
  private @Valid String id;
  private @Valid URI href;
  private @Valid String attachmentType;
  private @Valid String content;
  private @Valid String description;
  private @Valid String mimeType;
  private @Valid String name;
  private @Valid URI url;
  private @Valid Quantity size;
  private @Valid TimePeriod validFor;
  private @Valid String atBaseType;
  private @Valid URI atSchemaLocation;
  private @Valid String atType;

  /**
   * Unique identifier for this particular attachment
   **/
  public Attachment id(String id) {
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
   * URI for this Attachment
   **/
  public Attachment href(URI href) {
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
   * Attachment type such as video, picture
   **/
  public Attachment attachmentType(String attachmentType) {
    this.attachmentType = attachmentType;
    return this;
  }

  
  @JsonProperty("attachmentType")
  public String getAttachmentType() {
    return attachmentType;
  }

  @JsonProperty("attachmentType")
  public void setAttachmentType(String attachmentType) {
    this.attachmentType = attachmentType;
  }

  /**
   * The actual contents of the attachment object, if embedded, encoded as base64
   **/
  public Attachment content(String content) {
    this.content = content;
    return this;
  }

  
  @JsonProperty("content")
  public String getContent() {
    return content;
  }

  @JsonProperty("content")
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * A narrative text describing the content of the attachment
   **/
  public Attachment description(String description) {
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
   * Attachment mime type such as extension file for video, picture and document
   **/
  public Attachment mimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }

  
  @JsonProperty("mimeType")
  public String getMimeType() {
    return mimeType;
  }

  @JsonProperty("mimeType")
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   * The name of the attachment
   **/
  public Attachment name(String name) {
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
   * Uniform Resource Locator, is a web page address (a subset of URI)
   **/
  public Attachment url(URI url) {
    this.url = url;
    return this;
  }

  
  @JsonProperty("url")
  public URI getUrl() {
    return url;
  }

  @JsonProperty("url")
  public void setUrl(URI url) {
    this.url = url;
  }

  /**
   **/
  public Attachment size(Quantity size) {
    this.size = size;
    return this;
  }

  
  @JsonProperty("size")
  public Quantity getSize() {
    return size;
  }

  @JsonProperty("size")
  public void setSize(Quantity size) {
    this.size = size;
  }

  /**
   **/
  public Attachment validFor(TimePeriod validFor) {
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
  public Attachment atBaseType(String atBaseType) {
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
  public Attachment atSchemaLocation(URI atSchemaLocation) {
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
  public Attachment atType(String atType) {
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
    Attachment attachment = (Attachment) o;
    return Objects.equals(this.id, attachment.id) &&
        Objects.equals(this.href, attachment.href) &&
        Objects.equals(this.attachmentType, attachment.attachmentType) &&
        Objects.equals(this.content, attachment.content) &&
        Objects.equals(this.description, attachment.description) &&
        Objects.equals(this.mimeType, attachment.mimeType) &&
        Objects.equals(this.name, attachment.name) &&
        Objects.equals(this.url, attachment.url) &&
        Objects.equals(this.size, attachment.size) &&
        Objects.equals(this.validFor, attachment.validFor) &&
        Objects.equals(this.atBaseType, attachment.atBaseType) &&
        Objects.equals(this.atSchemaLocation, attachment.atSchemaLocation) &&
        Objects.equals(this.atType, attachment.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, attachmentType, content, description, mimeType, name, url, size, validFor, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Attachment {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    attachmentType: ").append(toIndentedString(attachmentType)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
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

