package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapi.quarkus.opa_yaml.model.Model200SingleResultResultServicesAcmecorp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
  * The types of services
 **/
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200SingleResultResultServices  {

    private Model200SingleResultResultServicesAcmecorp acmecorp;

    /**
    * Get acmecorp
    * @return acmecorp
    **/
    @JsonProperty("acmecorp")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200SingleResultResultServicesAcmecorp getAcmecorp() {
        return acmecorp;
    }

    /**
     * Set acmecorp
     **/
    @JsonProperty("acmecorp")
    public void setAcmecorp(Model200SingleResultResultServicesAcmecorp acmecorp) {
        this.acmecorp = acmecorp;
    }

    public Model200SingleResultResultServices acmecorp(Model200SingleResultResultServicesAcmecorp acmecorp) {
        this.acmecorp = acmecorp;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200SingleResultResultServices {\n");

        sb.append("    acmecorp: ").append(toIndentedString(acmecorp)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200SingleResultResultServices} object that
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

        Model200SingleResultResultServices model = (Model200SingleResultResultServices) obj;

        return java.util.Objects.equals(acmecorp, model.acmecorp);
    }

    /**
     * Returns a hash code for a {@code Model200SingleResultResultServices}.
     *
     * @return a hash code value for a {@code Model200SingleResultResultServices}.
     **/
    @Override
    public int hashCode() {
    return java.util.Objects.hash(acmecorp);
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
      * The types of services
     **/
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class Model200SingleResultResultServicesQueryParam  {

        /**
          * The types of services
         **/
        @jakarta.ws.rs.QueryParam("acmecorp")
        private Model200SingleResultResultServicesAcmecorp acmecorp;

        /**
        * Get acmecorp
        * @return acmecorp
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("acmecorp")
        public Model200SingleResultResultServicesAcmecorp getAcmecorp() {
            return acmecorp;
        }

        /**
         * Set acmecorp
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("acmecorp")
        public void setAcmecorp(Model200SingleResultResultServicesAcmecorp acmecorp) {
            this.acmecorp = acmecorp;
        }

        public Model200SingleResultResultServicesQueryParam acmecorp(Model200SingleResultResultServicesAcmecorp acmecorp) {
            this.acmecorp = acmecorp;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200SingleResultResultServicesQueryParam {\n");

            sb.append("    acmecorp: ").append(toIndentedString(acmecorp)).append("\n");
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
