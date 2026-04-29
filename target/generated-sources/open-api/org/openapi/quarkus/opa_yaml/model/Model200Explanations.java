package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapi.quarkus.opa_yaml.model.Model200ExplanationsExplanationInner;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200Explanations  {

    private List<Model200ExplanationsExplanationInner> explanation;

    /**
    * Get explanation
    * @return explanation
    **/
    @JsonProperty("explanation")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public List<Model200ExplanationsExplanationInner> getExplanation() {
        return explanation;
    }

    /**
     * Set explanation
     **/
    @JsonProperty("explanation")
    public void setExplanation(List<Model200ExplanationsExplanationInner> explanation) {
        this.explanation = explanation;
    }

    public Model200Explanations explanation(List<Model200ExplanationsExplanationInner> explanation) {
        this.explanation = explanation;
        return this;
    }
    public Model200Explanations addExplanationItem(Model200ExplanationsExplanationInner explanationItem) {
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
        sb.append("class Model200Explanations {\n");

        sb.append("    explanation: ").append(toIndentedString(explanation)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200Explanations} object that
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

        Model200Explanations model = (Model200Explanations) obj;

        return java.util.Objects.equals(explanation, model.explanation);
    }

    /**
     * Returns a hash code for a {@code Model200Explanations}.
     *
     * @return a hash code value for a {@code Model200Explanations}.
     **/
    @Override
    public int hashCode() {
    return java.util.Objects.hash(explanation);
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
    public static class Model200ExplanationsQueryParam  {

        @jakarta.ws.rs.QueryParam("explanation")
        private List<Model200ExplanationsExplanationInner> explanation = null;

        /**
        * Get explanation
        * @return explanation
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("explanation")
        public List<Model200ExplanationsExplanationInner> getExplanation() {
            return explanation;
        }

        /**
         * Set explanation
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("explanation")
        public void setExplanation(List<Model200ExplanationsExplanationInner> explanation) {
            this.explanation = explanation;
        }

        public Model200ExplanationsQueryParam explanation(List<Model200ExplanationsExplanationInner> explanation) {
            this.explanation = explanation;
            return this;
        }
        public Model200ExplanationsQueryParam addExplanationItem(Model200ExplanationsExplanationInner explanationItem) {
            this.explanation.add(explanationItem);
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200ExplanationsQueryParam {\n");

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
