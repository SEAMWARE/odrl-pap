package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapi.quarkus.opa_yaml.model.Model200SingleResultResultBundlesAuthz;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
  * Bundles
 **/
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200SingleResultResultBundles  {

    private Model200SingleResultResultBundlesAuthz authz;

    /**
    * Get authz
    * @return authz
    **/
    @JsonProperty("authz")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200SingleResultResultBundlesAuthz getAuthz() {
        return authz;
    }

    /**
     * Set authz
     **/
    @JsonProperty("authz")
    public void setAuthz(Model200SingleResultResultBundlesAuthz authz) {
        this.authz = authz;
    }

    public Model200SingleResultResultBundles authz(Model200SingleResultResultBundlesAuthz authz) {
        this.authz = authz;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200SingleResultResultBundles {\n");

        sb.append("    authz: ").append(toIndentedString(authz)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200SingleResultResultBundles} object that
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

        Model200SingleResultResultBundles model = (Model200SingleResultResultBundles) obj;

        return java.util.Objects.equals(authz, model.authz);
    }

    /**
     * Returns a hash code for a {@code Model200SingleResultResultBundles}.
     *
     * @return a hash code value for a {@code Model200SingleResultResultBundles}.
     **/
    @Override
    public int hashCode() {
    return java.util.Objects.hash(authz);
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
      * Bundles
     **/
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class Model200SingleResultResultBundlesQueryParam  {

        /**
          * Bundles
         **/
        @jakarta.ws.rs.QueryParam("authz")
        private Model200SingleResultResultBundlesAuthz authz;

        /**
        * Get authz
        * @return authz
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("authz")
        public Model200SingleResultResultBundlesAuthz getAuthz() {
            return authz;
        }

        /**
         * Set authz
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("authz")
        public void setAuthz(Model200SingleResultResultBundlesAuthz authz) {
            this.authz = authz;
        }

        public Model200SingleResultResultBundlesQueryParam authz(Model200SingleResultResultBundlesAuthz authz) {
            this.authz = authz;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200SingleResultResultBundlesQueryParam {\n");

            sb.append("    authz: ").append(toIndentedString(authz)).append("\n");
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
