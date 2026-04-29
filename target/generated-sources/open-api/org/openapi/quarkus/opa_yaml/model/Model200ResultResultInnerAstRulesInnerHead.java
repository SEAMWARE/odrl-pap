package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapi.quarkus.opa_yaml.model.Model200ResultResultInnerAstRulesInnerHeadKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200ResultResultInnerAstRulesInnerHead  {

    /**
      * The head of the rule
     **/
    private String name;
    private Model200ResultResultInnerAstRulesInnerHeadKey key;

    /**
    * The head of the rule
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

    public Model200ResultResultInnerAstRulesInnerHead name(String name) {
        this.name = name;
        return this;
    }

    /**
    * Get key
    * @return key
    **/
    @JsonProperty("key")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200ResultResultInnerAstRulesInnerHeadKey getKey() {
        return key;
    }

    /**
     * Set key
     **/
    @JsonProperty("key")
    public void setKey(Model200ResultResultInnerAstRulesInnerHeadKey key) {
        this.key = key;
    }

    public Model200ResultResultInnerAstRulesInnerHead key(Model200ResultResultInnerAstRulesInnerHeadKey key) {
        this.key = key;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200ResultResultInnerAstRulesInnerHead {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    key: ").append(toIndentedString(key)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200ResultResultInnerAstRulesInnerHead} object that
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

        Model200ResultResultInnerAstRulesInnerHead model = (Model200ResultResultInnerAstRulesInnerHead) obj;

        return java.util.Objects.equals(name, model.name) &&
        java.util.Objects.equals(key, model.key);
    }

    /**
     * Returns a hash code for a {@code Model200ResultResultInnerAstRulesInnerHead}.
     *
     * @return a hash code value for a {@code Model200ResultResultInnerAstRulesInnerHead}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(name,
        key);
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
    public static class Model200ResultResultInnerAstRulesInnerHeadQueryParam  {

        @jakarta.ws.rs.QueryParam("name")
        private String name;
        @jakarta.ws.rs.QueryParam("key")
        private Model200ResultResultInnerAstRulesInnerHeadKey key;

        /**
        * The head of the rule
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

        public Model200ResultResultInnerAstRulesInnerHeadQueryParam name(String name) {
            this.name = name;
            return this;
        }

        /**
        * Get key
        * @return key
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("key")
        public Model200ResultResultInnerAstRulesInnerHeadKey getKey() {
            return key;
        }

        /**
         * Set key
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("key")
        public void setKey(Model200ResultResultInnerAstRulesInnerHeadKey key) {
            this.key = key;
        }

        public Model200ResultResultInnerAstRulesInnerHeadQueryParam key(Model200ResultResultInnerAstRulesInnerHeadKey key) {
            this.key = key;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200ResultResultInnerAstRulesInnerHeadQueryParam {\n");

            sb.append("    name: ").append(toIndentedString(name)).append("\n");
            sb.append("    key: ").append(toIndentedString(key)).append("\n");
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
