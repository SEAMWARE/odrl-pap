package org.openapi.quarkus.odrl_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
  * A concrete mapping.
 **/
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Mapping  {

    private String name;
    private String description;

    /**
    * Get name
    * @return name
    **/
    @JsonProperty("name")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getName() {
        return name;
    }

    /**
     * Set name
     **/
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Mapping name(String name) {
        this.name = name;
        return this;
    }

    /**
    * Get description
    * @return description
    **/
    @JsonProperty("description")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getDescription() {
        return description;
    }

    /**
     * Set description
     **/
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    public Mapping description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Mapping {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Mapping} object that
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

        Mapping model = (Mapping) obj;

        return java.util.Objects.equals(name, model.name) &&
        java.util.Objects.equals(description, model.description);
    }

    /**
     * Returns a hash code for a {@code Mapping}.
     *
     * @return a hash code value for a {@code Mapping}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(name,
        description);
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
      * A concrete mapping.
     **/
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class MappingQueryParam  {

        /**
          * A concrete mapping.
         **/
        @jakarta.ws.rs.QueryParam("name")
        private String name;
        /**
          * A concrete mapping.
         **/
        @jakarta.ws.rs.QueryParam("description")
        private String description;

        /**
        * Get name
        * @return name
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("name")
        public String getName() {
            return name;
        }

        /**
         * Set name
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("name")
        public void setName(String name) {
            this.name = name;
        }

        public MappingQueryParam name(String name) {
            this.name = name;
            return this;
        }

        /**
        * Get description
        * @return description
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("description")
        public String getDescription() {
            return description;
        }

        /**
         * Set description
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("description")
        public void setDescription(String description) {
            this.description = description;
        }

        public MappingQueryParam description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class MappingQueryParam {\n");

            sb.append("    name: ").append(toIndentedString(name)).append("\n");
            sb.append("    description: ").append(toIndentedString(description)).append("\n");
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
