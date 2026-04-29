package org.openapi.quarkus.odrl_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceListInner  {

    private String id;
    private String policyPath;

    /**
    * Get id
    * @return id
    **/
    @JsonProperty("id")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getId() {
        return id;
    }

    /**
     * Set id
     **/
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public ServiceListInner id(String id) {
        this.id = id;
        return this;
    }

    /**
    * Get policyPath
    * @return policyPath
    **/
    @JsonProperty("policyPath")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getPolicyPath() {
        return policyPath;
    }

    /**
     * Set policyPath
     **/
    @JsonProperty("policyPath")
    public void setPolicyPath(String policyPath) {
        this.policyPath = policyPath;
    }

    public ServiceListInner policyPath(String policyPath) {
        this.policyPath = policyPath;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ServiceListInner {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    policyPath: ").append(toIndentedString(policyPath)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code ServiceListInner} object that
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

        ServiceListInner model = (ServiceListInner) obj;

        return java.util.Objects.equals(id, model.id) &&
        java.util.Objects.equals(policyPath, model.policyPath);
    }

    /**
     * Returns a hash code for a {@code ServiceListInner}.
     *
     * @return a hash code value for a {@code ServiceListInner}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id,
        policyPath);
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
    public static class ServiceListInnerQueryParam  {

        @jakarta.ws.rs.QueryParam("id")
        private String id;
        @jakarta.ws.rs.QueryParam("policyPath")
        private String policyPath;

        /**
        * Get id
        * @return id
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("id")
        public String getId() {
            return id;
        }

        /**
         * Set id
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("id")
        public void setId(String id) {
            this.id = id;
        }

        public ServiceListInnerQueryParam id(String id) {
            this.id = id;
            return this;
        }

        /**
        * Get policyPath
        * @return policyPath
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("policyPath")
        public String getPolicyPath() {
            return policyPath;
        }

        /**
         * Set policyPath
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("policyPath")
        public void setPolicyPath(String policyPath) {
            this.policyPath = policyPath;
        }

        public ServiceListInnerQueryParam policyPath(String policyPath) {
            this.policyPath = policyPath;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class ServiceListInnerQueryParam {\n");

            sb.append("    id: ").append(toIndentedString(id)).append("\n");
            sb.append("    policyPath: ").append(toIndentedString(policyPath)).append("\n");
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
