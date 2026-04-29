package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200ExplanationsExplanationInnerNodeTermsInner  {

    private String type;
    private String value;

    /**
    * Get type
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

    public Model200ExplanationsExplanationInnerNodeTermsInner type(String type) {
        this.type = type;
        return this;
    }

    /**
    * Get value
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

    public Model200ExplanationsExplanationInnerNodeTermsInner value(String value) {
        this.value = value;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200ExplanationsExplanationInnerNodeTermsInner {\n");

        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200ExplanationsExplanationInnerNodeTermsInner} object that
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

        Model200ExplanationsExplanationInnerNodeTermsInner model = (Model200ExplanationsExplanationInnerNodeTermsInner) obj;

        return java.util.Objects.equals(type, model.type) &&
        java.util.Objects.equals(value, model.value);
    }

    /**
     * Returns a hash code for a {@code Model200ExplanationsExplanationInnerNodeTermsInner}.
     *
     * @return a hash code value for a {@code Model200ExplanationsExplanationInnerNodeTermsInner}.
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

    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class Model200ExplanationsExplanationInnerNodeTermsInnerQueryParam  {

        @jakarta.ws.rs.QueryParam("type")
        private String type;
        @jakarta.ws.rs.QueryParam("value")
        private String value;

        /**
        * Get type
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

        public Model200ExplanationsExplanationInnerNodeTermsInnerQueryParam type(String type) {
            this.type = type;
            return this;
        }

        /**
        * Get value
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

        public Model200ExplanationsExplanationInnerNodeTermsInnerQueryParam value(String value) {
            this.value = value;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200ExplanationsExplanationInnerNodeTermsInnerQueryParam {\n");

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
