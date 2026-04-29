package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapi.quarkus.opa_yaml.model.Model200SingleResultResultKeysGlobalKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
  * Keys
 **/
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200SingleResultResultKeys  {

    private Model200SingleResultResultKeysGlobalKey globalKey;

    /**
    * Get globalKey
    * @return globalKey
    **/
    @JsonProperty("global-key")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200SingleResultResultKeysGlobalKey getGlobalKey() {
        return globalKey;
    }

    /**
     * Set globalKey
     **/
    @JsonProperty("global-key")
    public void setGlobalKey(Model200SingleResultResultKeysGlobalKey globalKey) {
        this.globalKey = globalKey;
    }

    public Model200SingleResultResultKeys globalKey(Model200SingleResultResultKeysGlobalKey globalKey) {
        this.globalKey = globalKey;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200SingleResultResultKeys {\n");

        sb.append("    globalKey: ").append(toIndentedString(globalKey)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200SingleResultResultKeys} object that
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

        Model200SingleResultResultKeys model = (Model200SingleResultResultKeys) obj;

        return java.util.Objects.equals(globalKey, model.globalKey);
    }

    /**
     * Returns a hash code for a {@code Model200SingleResultResultKeys}.
     *
     * @return a hash code value for a {@code Model200SingleResultResultKeys}.
     **/
    @Override
    public int hashCode() {
    return java.util.Objects.hash(globalKey);
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
      * Keys
     **/
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class Model200SingleResultResultKeysQueryParam  {

        /**
          * Keys
         **/
        @jakarta.ws.rs.QueryParam("globalKey")
        private Model200SingleResultResultKeysGlobalKey globalKey;

        /**
        * Get globalKey
        * @return globalKey
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("global-key")
        public Model200SingleResultResultKeysGlobalKey getGlobalKey() {
            return globalKey;
        }

        /**
         * Set globalKey
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("global-key")
        public void setGlobalKey(Model200SingleResultResultKeysGlobalKey globalKey) {
            this.globalKey = globalKey;
        }

        public Model200SingleResultResultKeysQueryParam globalKey(Model200SingleResultResultKeysGlobalKey globalKey) {
            this.globalKey = globalKey;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200SingleResultResultKeysQueryParam {\n");

            sb.append("    globalKey: ").append(toIndentedString(globalKey)).append("\n");
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
