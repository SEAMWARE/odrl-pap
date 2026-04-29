package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapi.quarkus.opa_yaml.model.Model200SingleResultResult;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200SingleResult  {

    private Model200SingleResultResult result;

    /**
    * Get result
    * @return result
    **/
    @JsonProperty("result")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200SingleResultResult getResult() {
        return result;
    }

    /**
     * Set result
     **/
    @JsonProperty("result")
    public void setResult(Model200SingleResultResult result) {
        this.result = result;
    }

    public Model200SingleResult result(Model200SingleResultResult result) {
        this.result = result;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200SingleResult {\n");

        sb.append("    result: ").append(toIndentedString(result)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200SingleResult} object that
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

        Model200SingleResult model = (Model200SingleResult) obj;

        return java.util.Objects.equals(result, model.result);
    }

    /**
     * Returns a hash code for a {@code Model200SingleResult}.
     *
     * @return a hash code value for a {@code Model200SingleResult}.
     **/
    @Override
    public int hashCode() {
    return java.util.Objects.hash(result);
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
    public static class Model200SingleResultQueryParam  {

        @jakarta.ws.rs.QueryParam("result")
        private Model200SingleResultResult result;

        /**
        * Get result
        * @return result
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("result")
        public Model200SingleResultResult getResult() {
            return result;
        }

        /**
         * Set result
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("result")
        public void setResult(Model200SingleResultResult result) {
            this.result = result;
        }

        public Model200SingleResultQueryParam result(Model200SingleResultResult result) {
            this.result = result;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200SingleResultQueryParam {\n");

            sb.append("    result: ").append(toIndentedString(result)).append("\n");
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
