package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
  * The type/value pairing for this rule's head
 **/
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200ResultResultInnerAstRulesInnerHeadKey  {

    /**
      * The type of the head
     **/
    private String type;
    /**
      * The value of the head
     **/
    private String value;

    /**
    * The type of the head
    * @return type
    **/
    @JsonProperty("type")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getType() {
        return type;
    }

    /**
     * Set type
     **/
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public Model200ResultResultInnerAstRulesInnerHeadKey type(String type) {
        this.type = type;
        return this;
    }

    /**
    * The value of the head
    * @return value
    **/
    @JsonProperty("value")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getValue() {
        return value;
    }

    /**
     * Set value
     **/
    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    public Model200ResultResultInnerAstRulesInnerHeadKey value(String value) {
        this.value = value;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200ResultResultInnerAstRulesInnerHeadKey {\n");

        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200ResultResultInnerAstRulesInnerHeadKey} object that
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

        Model200ResultResultInnerAstRulesInnerHeadKey model = (Model200ResultResultInnerAstRulesInnerHeadKey) obj;

        return java.util.Objects.equals(type, model.type) &&
        java.util.Objects.equals(value, model.value);
    }

    /**
     * Returns a hash code for a {@code Model200ResultResultInnerAstRulesInnerHeadKey}.
     *
     * @return a hash code value for a {@code Model200ResultResultInnerAstRulesInnerHeadKey}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(type,
        value);
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
      * The type/value pairing for this rule's head
     **/
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class Model200ResultResultInnerAstRulesInnerHeadKeyQueryParam  {

        /**
          * The type/value pairing for this rule's head
         **/
        @jakarta.ws.rs.QueryParam("type")
        private String type;
        /**
          * The type/value pairing for this rule's head
         **/
        @jakarta.ws.rs.QueryParam("value")
        private String value;

        /**
        * The type of the head
        * @return type
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("type")
        public String getType() {
            return type;
        }

        /**
         * Set type
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("type")
        public void setType(String type) {
            this.type = type;
        }

        public Model200ResultResultInnerAstRulesInnerHeadKeyQueryParam type(String type) {
            this.type = type;
            return this;
        }

        /**
        * The value of the head
        * @return value
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("value")
        public String getValue() {
            return value;
        }

        /**
         * Set value
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("value")
        public void setValue(String value) {
            this.value = value;
        }

        public Model200ResultResultInnerAstRulesInnerHeadKeyQueryParam value(String value) {
            this.value = value;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200ResultResultInnerAstRulesInnerHeadKeyQueryParam {\n");

            sb.append("    type: ").append(toIndentedString(type)).append("\n");
            sb.append("    value: ").append(toIndentedString(value)).append("\n");
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
