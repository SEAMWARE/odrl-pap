package org.fiware.tmforum.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.net.URI;
import java.time.OffsetDateTime;
import org.fiware.tmforum.model.JobStateType;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represents a task used to export resources to a file Skipped properties: id,href
 **/

@JsonTypeName("ExportJob_Create")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-04-29T06:23:47.414223106Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class ExportJobCreate   {
  private @Valid OffsetDateTime completionDate;
  private @Valid String contentType;
  private @Valid OffsetDateTime creationDate;
  private @Valid String errorLog;
  private @Valid String path;
  private @Valid String query;
  private @Valid URI url;
  private @Valid JobStateType status;
  private @Valid String atBaseType;
  private @Valid URI atSchemaLocation;
  private @Valid String atType;

  /**
   * Data at which the job was completed
   **/
  public ExportJobCreate completionDate(OffsetDateTime completionDate) {
    this.completionDate = completionDate;
    return this;
  }

  
  @JsonProperty("completionDate")
  public OffsetDateTime getCompletionDate() {
    return completionDate;
  }

  @JsonProperty("completionDate")
  public void setCompletionDate(OffsetDateTime completionDate) {
    this.completionDate = completionDate;
  }

  /**
   * The format of the exported data
   **/
  public ExportJobCreate contentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

  
  @JsonProperty("contentType")
  public String getContentType() {
    return contentType;
  }

  @JsonProperty("contentType")
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * Date at which the job was created
   **/
  public ExportJobCreate creationDate(OffsetDateTime creationDate) {
    this.creationDate = creationDate;
    return this;
  }

  
  @JsonProperty("creationDate")
  public OffsetDateTime getCreationDate() {
    return creationDate;
  }

  @JsonProperty("creationDate")
  public void setCreationDate(OffsetDateTime creationDate) {
    this.creationDate = creationDate;
  }

  /**
   * Reason for failure
   **/
  public ExportJobCreate errorLog(String errorLog) {
    this.errorLog = errorLog;
    return this;
  }

  
  @JsonProperty("errorLog")
  public String getErrorLog() {
    return errorLog;
  }

  @JsonProperty("errorLog")
  public void setErrorLog(String errorLog) {
    this.errorLog = errorLog;
  }

  /**
   * URL of the root resource acting as the source for streaming content to the file specified by the export job
   **/
  public ExportJobCreate path(String path) {
    this.path = path;
    return this;
  }

  
  @JsonProperty("path")
  public String getPath() {
    return path;
  }

  @JsonProperty("path")
  public void setPath(String path) {
    this.path = path;
  }

  /**
   * Used to scope the exported data
   **/
  public ExportJobCreate query(String query) {
    this.query = query;
    return this;
  }

  
  @JsonProperty("query")
  public String getQuery() {
    return query;
  }

  @JsonProperty("query")
  public void setQuery(String query) {
    this.query = query;
  }

  /**
   * URL of the file containing the data to be exported
   **/
  public ExportJobCreate url(URI url) {
    this.url = url;
    return this;
  }

  
  @JsonProperty("url")
  @NotNull
  public URI getUrl() {
    return url;
  }

  @JsonProperty("url")
  public void setUrl(URI url) {
    this.url = url;
  }

  /**
   **/
  public ExportJobCreate status(JobStateType status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  public JobStateType getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(JobStateType status) {
    this.status = status;
  }

  /**
   * When sub-classing, this defines the super-class
   **/
  public ExportJobCreate atBaseType(String atBaseType) {
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
  public ExportJobCreate atSchemaLocation(URI atSchemaLocation) {
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
  public ExportJobCreate atType(String atType) {
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
    ExportJobCreate exportJobCreate = (ExportJobCreate) o;
    return Objects.equals(this.completionDate, exportJobCreate.completionDate) &&
        Objects.equals(this.contentType, exportJobCreate.contentType) &&
        Objects.equals(this.creationDate, exportJobCreate.creationDate) &&
        Objects.equals(this.errorLog, exportJobCreate.errorLog) &&
        Objects.equals(this.path, exportJobCreate.path) &&
        Objects.equals(this.query, exportJobCreate.query) &&
        Objects.equals(this.url, exportJobCreate.url) &&
        Objects.equals(this.status, exportJobCreate.status) &&
        Objects.equals(this.atBaseType, exportJobCreate.atBaseType) &&
        Objects.equals(this.atSchemaLocation, exportJobCreate.atSchemaLocation) &&
        Objects.equals(this.atType, exportJobCreate.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(completionDate, contentType, creationDate, errorLog, path, query, url, status, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExportJobCreate {\n");
    
    sb.append("    completionDate: ").append(toIndentedString(completionDate)).append("\n");
    sb.append("    contentType: ").append(toIndentedString(contentType)).append("\n");
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
    sb.append("    errorLog: ").append(toIndentedString(errorLog)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    query: ").append(toIndentedString(query)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

