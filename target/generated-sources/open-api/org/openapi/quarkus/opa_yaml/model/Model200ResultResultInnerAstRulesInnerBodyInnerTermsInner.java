package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapi.quarkus.opa_yaml.model.Model200ResultResultInnerAstRulesInnerBodyInnerTermsInnerValueInner;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner  {

    /**
      * The type of the term variable
     **/
    private String type;
    /**
      * The list of types and values for the term variable
     **/
    private List<Model200ResultResultInnerAstRulesInnerBodyInnerTermsInnerValueInner> value;

    /**
    * The type of the term variable
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

    public Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner type(String type) {
        this.type = type;
        return this;
    }

    /**
    * The list of types and values for the term variable
    * @return value
    **/
    @JsonProperty("value")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public List<Model200ResultResultInnerAstRulesInnerBodyInnerTermsInnerValueInner> getValue() {
        return value;
    }

    /**
     * Set value
     **/
    @JsonProperty("value")
    public void setValue(List<Model200ResultResultInnerAstRulesInnerBodyInnerTermsInnerValueInner> value) {
        this.value = value;
    }

    public Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner value(List<Model200ResultResultInnerAstRulesInnerBodyInnerTermsInnerValueInner> value) {
        this.value = value;
        return this;
    }
    public Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner addValueItem(Model200ResultResultInnerAstRulesInnerBodyInnerTermsInnerValueInner valueItem) {
        if (this.value == null){
            value = new ArrayList<>();
        }
        this.value.add(valueItem);
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner {\n");

        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner} object that
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

        Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner model = (Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner) obj;

        return java.util.Objects.equals(type, model.type) &&
        java.util.Objects.equals(value, model.value);
    }

    /**
     * Returns a hash code for a {@code Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner}.
     *
     * @return a hash code value for a {@code Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner}.
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
    public static class Model200ResultResultInnerAstRulesInnerBodyInnerTermsInnerQueryParam  {

        @jakarta.ws.rs.QueryParam("type")
        private String type;
        @jakarta.ws.rs.QueryParam("value")
        private List<Model200ResultResultInnerAstRulesInnerBodyInnerTermsInnerValueInner> value = null;

        /**
        * The type of the term variable
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

        public Model200ResultResultInnerAstRulesInnerBodyInnerTermsInnerQueryParam type(String type) {
            this.type = type;
            return this;
        }

        /**
        * The list of types and values for the term variable
        * @return value
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("value")
        public List<Model200ResultResultInnerAstRulesInnerBodyInnerTermsInnerValueInner> getValue() {
            return value;
        }

        /**
         * Set value
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("value")
        public void setValue(List<Model200ResultResultInnerAstRulesInnerBodyInnerTermsInnerValueInner> value) {
            this.value = value;
        }

        public Model200ResultResultInnerAstRulesInnerBodyInnerTermsInnerQueryParam value(List<Model200ResultResultInnerAstRulesInnerBodyInnerTermsInnerValueInner> value) {
            this.value = value;
            return this;
        }
        public Model200ResultResultInnerAstRulesInnerBodyInnerTermsInnerQueryParam addValueItem(Model200ResultResultInnerAstRulesInnerBodyInnerTermsInnerValueInner valueItem) {
            this.value.add(valueItem);
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200ResultResultInnerAstRulesInnerBodyInnerTermsInnerQueryParam {\n");

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
