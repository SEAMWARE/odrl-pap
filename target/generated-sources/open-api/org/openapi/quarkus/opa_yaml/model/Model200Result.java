package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapi.quarkus.opa_yaml.model.Model200ResultResultInner;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200Result  {

    private List<Model200ResultResultInner> result;

    /**
    * Get result
    * @return result
    **/
    @JsonProperty("result")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public List<Model200ResultResultInner> getResult() {
        return result;
    }

    /**
     * Set result
     **/
    @JsonProperty("result")
    public void setResult(List<Model200ResultResultInner> result) {
        this.result = result;
    }

    public Model200Result result(List<Model200ResultResultInner> result) {
        this.result = result;
        return this;
    }
    public Model200Result addResultItem(Model200ResultResultInner resultItem) {
        if (this.result == null){
            result = new ArrayList<>();
        }
        this.result.add(resultItem);
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200Result {\n");

        sb.append("    result: ").append(toIndentedString(result)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200Result} object that
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

        Model200Result model = (Model200Result) obj;

        return java.util.Objects.equals(result, model.result);
    }

    /**
     * Returns a hash code for a {@code Model200Result}.
     *
     * @return a hash code value for a {@code Model200Result}.
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
    public static class Model200ResultQueryParam  {

        @jakarta.ws.rs.QueryParam("result")
        private List<Model200ResultResultInner> result = null;

        /**
        * Get result
        * @return result
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("result")
        public List<Model200ResultResultInner> getResult() {
            return result;
        }

        /**
         * Set result
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("result")
        public void setResult(List<Model200ResultResultInner> result) {
            this.result = result;
        }

        public Model200ResultQueryParam result(List<Model200ResultResultInner> result) {
            this.result = result;
            return this;
        }
        public Model200ResultQueryParam addResultItem(Model200ResultResultInner resultItem) {
            this.result.add(resultItem);
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200ResultQueryParam {\n");

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
