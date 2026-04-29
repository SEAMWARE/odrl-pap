package org.openapi.quarkus.odrl_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationResponse  {

    /**
      * indicates if the request was allowed by the policy
     **/
    private Boolean allow;
    /**
      * Explanation of the detailed error, in case the policy evaluation failed.
     **/
    private List<String> explanation;

    /**
    * indicates if the request was allowed by the policy
    * @return allow
    **/
    @JsonProperty("allow")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Boolean getAllow() {
        return allow;
    }

    /**
     * Set allow
     **/
    @JsonProperty("allow")
    public void setAllow(Boolean allow) {
        this.allow = allow;
    }

    public ValidationResponse allow(Boolean allow) {
        this.allow = allow;
        return this;
    }

    /**
    * Explanation of the detailed error, in case the policy evaluation failed.
    * @return explanation
    **/
    @JsonProperty("explanation")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public List<String> getExplanation() {
        return explanation;
    }

    /**
     * Set explanation
     **/
    @JsonProperty("explanation")
    public void setExplanation(List<String> explanation) {
        this.explanation = explanation;
    }

    public ValidationResponse explanation(List<String> explanation) {
        this.explanation = explanation;
        return this;
    }
    public ValidationResponse addExplanationItem(String explanationItem) {
        if (this.explanation == null){
            explanation = new ArrayList<>();
        }
        this.explanation.add(explanationItem);
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ValidationResponse {\n");

        sb.append("    allow: ").append(toIndentedString(allow)).append("\n");
        sb.append("    explanation: ").append(toIndentedString(explanation)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code ValidationResponse} object that
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

        ValidationResponse model = (ValidationResponse) obj;

        return java.util.Objects.equals(allow, model.allow) &&
        java.util.Objects.equals(explanation, model.explanation);
    }

    /**
     * Returns a hash code for a {@code ValidationResponse}.
     *
     * @return a hash code value for a {@code ValidationResponse}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(allow,
        explanation);
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
    public static class ValidationResponseQueryParam  {

        @jakarta.ws.rs.QueryParam("allow")
        private Boolean allow;
        @jakarta.ws.rs.QueryParam("explanation")
        private List<String> explanation = null;

        /**
        * indicates if the request was allowed by the policy
        * @return allow
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("allow")
        public Boolean getAllow() {
            return allow;
        }

        /**
         * Set allow
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("allow")
        public void setAllow(Boolean allow) {
            this.allow = allow;
        }

        public ValidationResponseQueryParam allow(Boolean allow) {
            this.allow = allow;
            return this;
        }

        /**
        * Explanation of the detailed error, in case the policy evaluation failed.
        * @return explanation
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("explanation")
        public List<String> getExplanation() {
            return explanation;
        }

        /**
         * Set explanation
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("explanation")
        public void setExplanation(List<String> explanation) {
            this.explanation = explanation;
        }

        public ValidationResponseQueryParam explanation(List<String> explanation) {
            this.explanation = explanation;
            return this;
        }
        public ValidationResponseQueryParam addExplanationItem(String explanationItem) {
            this.explanation.add(explanationItem);
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class ValidationResponseQueryParam {\n");

            sb.append("    allow: ").append(toIndentedString(allow)).append("\n");
            sb.append("    explanation: ").append(toIndentedString(explanation)).append("\n");
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
