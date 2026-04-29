package org.openapi.quarkus.odrl_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyPath  {

    private String policyPath;

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

    public PolicyPath policyPath(String policyPath) {
        this.policyPath = policyPath;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PolicyPath {\n");

        sb.append("    policyPath: ").append(toIndentedString(policyPath)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code PolicyPath} object that
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

        PolicyPath model = (PolicyPath) obj;

        return java.util.Objects.equals(policyPath, model.policyPath);
    }

    /**
     * Returns a hash code for a {@code PolicyPath}.
     *
     * @return a hash code value for a {@code PolicyPath}.
     **/
    @Override
    public int hashCode() {
    return java.util.Objects.hash(policyPath);
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
    public static class PolicyPathQueryParam  {

        @jakarta.ws.rs.QueryParam("policyPath")
        private String policyPath;

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

        public PolicyPathQueryParam policyPath(String policyPath) {
            this.policyPath = policyPath;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class PolicyPathQueryParam {\n");

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
