package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
  * Global Key
 **/
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200SingleResultResultKeysGlobalKey  {

    /**
      * Scope
     **/
    private String scope;

    /**
    * Scope
    * @return scope
    **/
    @JsonProperty("scope")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getScope() {
        return scope;
    }

    /**
     * Set scope
     **/
    @JsonProperty("scope")
    public void setScope(String scope) {
        this.scope = scope;
    }

    public Model200SingleResultResultKeysGlobalKey scope(String scope) {
        this.scope = scope;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200SingleResultResultKeysGlobalKey {\n");

        sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200SingleResultResultKeysGlobalKey} object that
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

        Model200SingleResultResultKeysGlobalKey model = (Model200SingleResultResultKeysGlobalKey) obj;

        return java.util.Objects.equals(scope, model.scope);
    }

    /**
     * Returns a hash code for a {@code Model200SingleResultResultKeysGlobalKey}.
     *
     * @return a hash code value for a {@code Model200SingleResultResultKeysGlobalKey}.
     **/
    @Override
    public int hashCode() {
    return java.util.Objects.hash(scope);
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
      * Global Key
     **/
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class Model200SingleResultResultKeysGlobalKeyQueryParam  {

        /**
          * Global Key
         **/
        @jakarta.ws.rs.QueryParam("scope")
        private String scope;

        /**
        * Scope
        * @return scope
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("scope")
        public String getScope() {
            return scope;
        }

        /**
         * Set scope
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("scope")
        public void setScope(String scope) {
            this.scope = scope;
        }

        public Model200SingleResultResultKeysGlobalKeyQueryParam scope(String scope) {
            this.scope = scope;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200SingleResultResultKeysGlobalKeyQueryParam {\n");

            sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
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
