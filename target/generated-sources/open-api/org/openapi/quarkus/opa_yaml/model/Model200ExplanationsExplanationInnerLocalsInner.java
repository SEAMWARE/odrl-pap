package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapi.quarkus.opa_yaml.model.Model200ExplanationsExplanationInnerLocalsInnerKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200ExplanationsExplanationInnerLocalsInner  {

    private Model200ExplanationsExplanationInnerLocalsInnerKey key;
    private Model200ExplanationsExplanationInnerLocalsInnerKey value;

    /**
    * Get key
    * @return key
    **/
    @JsonProperty("key")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200ExplanationsExplanationInnerLocalsInnerKey getKey() {
        return key;
    }

    /**
     * Set key
     **/
    @JsonProperty("key")
    public void setKey(Model200ExplanationsExplanationInnerLocalsInnerKey key) {
        this.key = key;
    }

    public Model200ExplanationsExplanationInnerLocalsInner key(Model200ExplanationsExplanationInnerLocalsInnerKey key) {
        this.key = key;
        return this;
    }

    /**
    * Get value
    * @return value
    **/
    @JsonProperty("value")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200ExplanationsExplanationInnerLocalsInnerKey getValue() {
        return value;
    }

    /**
     * Set value
     **/
    @JsonProperty("value")
    public void setValue(Model200ExplanationsExplanationInnerLocalsInnerKey value) {
        this.value = value;
    }

    public Model200ExplanationsExplanationInnerLocalsInner value(Model200ExplanationsExplanationInnerLocalsInnerKey value) {
        this.value = value;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200ExplanationsExplanationInnerLocalsInner {\n");

        sb.append("    key: ").append(toIndentedString(key)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200ExplanationsExplanationInnerLocalsInner} object that
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

        Model200ExplanationsExplanationInnerLocalsInner model = (Model200ExplanationsExplanationInnerLocalsInner) obj;

        return java.util.Objects.equals(key, model.key) &&
        java.util.Objects.equals(value, model.value);
    }

    /**
     * Returns a hash code for a {@code Model200ExplanationsExplanationInnerLocalsInner}.
     *
     * @return a hash code value for a {@code Model200ExplanationsExplanationInnerLocalsInner}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(key,
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
    public static class Model200ExplanationsExplanationInnerLocalsInnerQueryParam  {

        @jakarta.ws.rs.QueryParam("key")
        private Model200ExplanationsExplanationInnerLocalsInnerKey key;
        @jakarta.ws.rs.QueryParam("value")
        private Model200ExplanationsExplanationInnerLocalsInnerKey value;

        /**
        * Get key
        * @return key
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("key")
        public Model200ExplanationsExplanationInnerLocalsInnerKey getKey() {
            return key;
        }

        /**
         * Set key
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("key")
        public void setKey(Model200ExplanationsExplanationInnerLocalsInnerKey key) {
            this.key = key;
        }

        public Model200ExplanationsExplanationInnerLocalsInnerQueryParam key(Model200ExplanationsExplanationInnerLocalsInnerKey key) {
            this.key = key;
            return this;
        }

        /**
        * Get value
        * @return value
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("value")
        public Model200ExplanationsExplanationInnerLocalsInnerKey getValue() {
            return value;
        }

        /**
         * Set value
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("value")
        public void setValue(Model200ExplanationsExplanationInnerLocalsInnerKey value) {
            this.value = value;
        }

        public Model200ExplanationsExplanationInnerLocalsInnerQueryParam value(Model200ExplanationsExplanationInnerLocalsInnerKey value) {
            this.value = value;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200ExplanationsExplanationInnerLocalsInnerQueryParam {\n");

            sb.append("    key: ").append(toIndentedString(key)).append("\n");
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
