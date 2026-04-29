package org.openapi.quarkus.odrl_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Policy  {

    private String id;
    private String odrlUid;
    private String odrl;
    private String rego;

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

    public Policy id(String id) {
        this.id = id;
        return this;
    }

    /**
    * Get odrlUid
    * @return odrlUid
    **/
    @JsonProperty("odrl:uid")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getOdrlUid() {
        return odrlUid;
    }

    /**
     * Set odrlUid
     **/
    @JsonProperty("odrl:uid")
    public void setOdrlUid(String odrlUid) {
        this.odrlUid = odrlUid;
    }

    public Policy odrlUid(String odrlUid) {
        this.odrlUid = odrlUid;
        return this;
    }

    /**
    * Get odrl
    * @return odrl
    **/
    @JsonProperty("odrl")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getOdrl() {
        return odrl;
    }

    /**
     * Set odrl
     **/
    @JsonProperty("odrl")
    public void setOdrl(String odrl) {
        this.odrl = odrl;
    }

    public Policy odrl(String odrl) {
        this.odrl = odrl;
        return this;
    }

    /**
    * Get rego
    * @return rego
    **/
    @JsonProperty("rego")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getRego() {
        return rego;
    }

    /**
     * Set rego
     **/
    @JsonProperty("rego")
    public void setRego(String rego) {
        this.rego = rego;
    }

    public Policy rego(String rego) {
        this.rego = rego;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Policy {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    odrlUid: ").append(toIndentedString(odrlUid)).append("\n");
        sb.append("    odrl: ").append(toIndentedString(odrl)).append("\n");
        sb.append("    rego: ").append(toIndentedString(rego)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Policy} object that
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

        Policy model = (Policy) obj;

        return java.util.Objects.equals(id, model.id) &&
        java.util.Objects.equals(odrlUid, model.odrlUid) &&
        java.util.Objects.equals(odrl, model.odrl) &&
        java.util.Objects.equals(rego, model.rego);
    }

    /**
     * Returns a hash code for a {@code Policy}.
     *
     * @return a hash code value for a {@code Policy}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id,
        odrlUid,
        odrl,
        rego);
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
    public static class PolicyQueryParam  {

        @jakarta.ws.rs.QueryParam("id")
        private String id;
        @jakarta.ws.rs.QueryParam("odrlUid")
        private String odrlUid;
        @jakarta.ws.rs.QueryParam("odrl")
        private String odrl;
        @jakarta.ws.rs.QueryParam("rego")
        private String rego;

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

        public PolicyQueryParam id(String id) {
            this.id = id;
            return this;
        }

        /**
        * Get odrlUid
        * @return odrlUid
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("odrl:uid")
        public String getOdrlUid() {
            return odrlUid;
        }

        /**
         * Set odrlUid
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("odrl:uid")
        public void setOdrlUid(String odrlUid) {
            this.odrlUid = odrlUid;
        }

        public PolicyQueryParam odrlUid(String odrlUid) {
            this.odrlUid = odrlUid;
            return this;
        }

        /**
        * Get odrl
        * @return odrl
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("odrl")
        public String getOdrl() {
            return odrl;
        }

        /**
         * Set odrl
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("odrl")
        public void setOdrl(String odrl) {
            this.odrl = odrl;
        }

        public PolicyQueryParam odrl(String odrl) {
            this.odrl = odrl;
            return this;
        }

        /**
        * Get rego
        * @return rego
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("rego")
        public String getRego() {
            return rego;
        }

        /**
         * Set rego
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("rego")
        public void setRego(String rego) {
            this.rego = rego;
        }

        public PolicyQueryParam rego(String rego) {
            this.rego = rego;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class PolicyQueryParam {\n");

            sb.append("    id: ").append(toIndentedString(id)).append("\n");
            sb.append("    odrlUid: ").append(toIndentedString(odrlUid)).append("\n");
            sb.append("    odrl: ").append(toIndentedString(odrl)).append("\n");
            sb.append("    rego: ").append(toIndentedString(rego)).append("\n");
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
