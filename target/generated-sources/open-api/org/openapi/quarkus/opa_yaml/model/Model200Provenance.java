package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapi.quarkus.opa_yaml.model.Model200ProvenanceProvenance;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200Provenance  {

    private Model200ProvenanceProvenance provenance;

    /**
    * Get provenance
    * @return provenance
    **/
    @JsonProperty("provenance")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200ProvenanceProvenance getProvenance() {
        return provenance;
    }

    /**
     * Set provenance
     **/
    @JsonProperty("provenance")
    public void setProvenance(Model200ProvenanceProvenance provenance) {
        this.provenance = provenance;
    }

    public Model200Provenance provenance(Model200ProvenanceProvenance provenance) {
        this.provenance = provenance;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200Provenance {\n");

        sb.append("    provenance: ").append(toIndentedString(provenance)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200Provenance} object that
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

        Model200Provenance model = (Model200Provenance) obj;

        return java.util.Objects.equals(provenance, model.provenance);
    }

    /**
     * Returns a hash code for a {@code Model200Provenance}.
     *
     * @return a hash code value for a {@code Model200Provenance}.
     **/
    @Override
    public int hashCode() {
    return java.util.Objects.hash(provenance);
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
    public static class Model200ProvenanceQueryParam  {

        @jakarta.ws.rs.QueryParam("provenance")
        private Model200ProvenanceProvenance provenance;

        /**
        * Get provenance
        * @return provenance
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("provenance")
        public Model200ProvenanceProvenance getProvenance() {
            return provenance;
        }

        /**
         * Set provenance
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("provenance")
        public void setProvenance(Model200ProvenanceProvenance provenance) {
            this.provenance = provenance;
        }

        public Model200ProvenanceQueryParam provenance(Model200ProvenanceProvenance provenance) {
            this.provenance = provenance;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200ProvenanceQueryParam {\n");

            sb.append("    provenance: ").append(toIndentedString(provenance)).append("\n");
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
