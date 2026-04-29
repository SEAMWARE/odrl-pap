package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
  * Status
 **/
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200SingleResultResultStatus  {

    /**
      * Service
     **/
    private String service;

    /**
    * Service
    * @return service
    **/
    @JsonProperty("service")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getService() {
        return service;
    }

    /**
     * Set service
     **/
    @JsonProperty("service")
    public void setService(String service) {
        this.service = service;
    }

    public Model200SingleResultResultStatus service(String service) {
        this.service = service;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200SingleResultResultStatus {\n");

        sb.append("    service: ").append(toIndentedString(service)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200SingleResultResultStatus} object that
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

        Model200SingleResultResultStatus model = (Model200SingleResultResultStatus) obj;

        return java.util.Objects.equals(service, model.service);
    }

    /**
     * Returns a hash code for a {@code Model200SingleResultResultStatus}.
     *
     * @return a hash code value for a {@code Model200SingleResultResultStatus}.
     **/
    @Override
    public int hashCode() {
    return java.util.Objects.hash(service);
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
      * Status
     **/
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class Model200SingleResultResultStatusQueryParam  {

        /**
          * Status
         **/
        @jakarta.ws.rs.QueryParam("service")
        private String service;

        /**
        * Service
        * @return service
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("service")
        public String getService() {
            return service;
        }

        /**
         * Set service
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("service")
        public void setService(String service) {
            this.service = service;
        }

        public Model200SingleResultResultStatusQueryParam service(String service) {
            this.service = service;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200SingleResultResultStatusQueryParam {\n");

            sb.append("    service: ").append(toIndentedString(service)).append("\n");
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
