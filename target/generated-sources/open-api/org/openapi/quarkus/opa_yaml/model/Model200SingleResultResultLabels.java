package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
  * Labels
 **/
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200SingleResultResultLabels  {

    /**
      * Label ID
     **/
    private String id;
    /**
      * Version
     **/
    private String version;

    /**
    * Label ID
    * @return id
    **/
    @JsonProperty("id")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getId() {
        return id;
    }

    /**
     * Set id
     **/
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public Model200SingleResultResultLabels id(String id) {
        this.id = id;
        return this;
    }

    /**
    * Version
    * @return version
    **/
    @JsonProperty("version")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getVersion() {
        return version;
    }

    /**
     * Set version
     **/
    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    public Model200SingleResultResultLabels version(String version) {
        this.version = version;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200SingleResultResultLabels {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200SingleResultResultLabels} object that
     * contains the same values as this object.
     *
     * @param   obj   the object to compare with.
     * @return  {@code true} if the objects are the same;
     *          {@code false} otherwise.
     **/
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Model200SingleResultResultLabels model = (Model200SingleResultResultLabels) obj;

        return java.util.Objects.equals(id, model.id) &&
        java.util.Objects.equals(version, model.version);
    }

    /**
     * Returns a hash code for a {@code Model200SingleResultResultLabels}.
     *
     * @return a hash code value for a {@code Model200SingleResultResultLabels}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id,
        version);
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private static String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    /**
      * Labels
     **/
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class Model200SingleResultResultLabelsQueryParam  {

        /**
          * Labels
         **/
        @jakarta.ws.rs.QueryParam("id")
        private String id;
        /**
          * Labels
         **/
        @jakarta.ws.rs.QueryParam("version")
        private String version;

        /**
        * Label ID
        * @return id
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("id")
        public String getId() {
            return id;
        }

        /**
         * Set id
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("id")
        public void setId(String id) {
            this.id = id;
        }

        public Model200SingleResultResultLabelsQueryParam id(String id) {
            this.id = id;
            return this;
        }

        /**
        * Version
        * @return version
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("version")
        public String getVersion() {
            return version;
        }

        /**
         * Set version
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("version")
        public void setVersion(String version) {
            this.version = version;
        }

        public Model200SingleResultResultLabelsQueryParam version(String version) {
            this.version = version;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200SingleResultResultLabelsQueryParam {\n");

            sb.append("    id: ").append(toIndentedString(id)).append("\n");
            sb.append("    version: ").append(toIndentedString(version)).append("\n");
            sb.append("}");
            return sb.toString();
        }

        /**
         * Convert the given object to string with each line indented by 4 spaces
         * (except the first line).
         */
        private static String toIndentedString(Object o) {
            if (o == null) {
                return "null";
            }
            return o.toString().replace("\n", "\n    ");
        }
    }}
