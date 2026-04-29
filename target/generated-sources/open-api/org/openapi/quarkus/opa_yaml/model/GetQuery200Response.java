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
import org.openapi.quarkus.opa_yaml.model.Model200MetricsMetrics;
import org.openapi.quarkus.opa_yaml.model.Model200ResultResultInner;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class GetQuery200Response  {

    private List<Model200ResultResultInner> result;
    private Model200MetricsMetrics metrics;
    private List<Model200ExplanationsExplanationInner> explanation;

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

    public GetQuery200Response result(List<Model200ResultResultInner> result) {
        this.result = result;
        return this;
    }
    public GetQuery200Response addResultItem(Model200ResultResultInner resultItem) {
        if (this.result == null){
            result = new ArrayList<>();
        }
        this.result.add(resultItem);
        return this;
    }

    /**
    * Get metrics
    * @return metrics
    **/
    @JsonProperty("metrics")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200MetricsMetrics getMetrics() {
        return metrics;
    }

    /**
     * Set metrics
     **/
    @JsonProperty("metrics")
    public void setMetrics(Model200MetricsMetrics metrics) {
        this.metrics = metrics;
    }

    public GetQuery200Response metrics(Model200MetricsMetrics metrics) {
        this.metrics = metrics;
        return this;
    }

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

    public GetQuery200Response explanation(List<Model200ExplanationsExplanationInner> explanation) {
        this.explanation = explanation;
        return this;
    }
    public GetQuery200Response addExplanationItem(Model200ExplanationsExplanationInner explanationItem) {
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
        sb.append("class GetQuery200Response {\n");

        sb.append("    result: ").append(toIndentedString(result)).append("\n");
        sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
        sb.append("    explanation: ").append(toIndentedString(explanation)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code GetQuery200Response} object that
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

        GetQuery200Response model = (GetQuery200Response) obj;

        return java.util.Objects.equals(result, model.result) &&
        java.util.Objects.equals(metrics, model.metrics) &&
        java.util.Objects.equals(explanation, model.explanation);
    }

    /**
     * Returns a hash code for a {@code GetQuery200Response}.
     *
     * @return a hash code value for a {@code GetQuery200Response}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(result,
        metrics,
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
    public static class GetQuery200ResponseQueryParam  {

        @jakarta.ws.rs.QueryParam("result")
        private List<Model200ResultResultInner> result = null;
        @jakarta.ws.rs.QueryParam("metrics")
        private Model200MetricsMetrics metrics;
        @jakarta.ws.rs.QueryParam("explanation")
        private List<Model200ExplanationsExplanationInner> explanation = null;

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

        public GetQuery200ResponseQueryParam result(List<Model200ResultResultInner> result) {
            this.result = result;
            return this;
        }
        public GetQuery200ResponseQueryParam addResultItem(Model200ResultResultInner resultItem) {
            this.result.add(resultItem);
            return this;
        }

        /**
        * Get metrics
        * @return metrics
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("metrics")
        public Model200MetricsMetrics getMetrics() {
            return metrics;
        }

        /**
         * Set metrics
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("metrics")
        public void setMetrics(Model200MetricsMetrics metrics) {
            this.metrics = metrics;
        }

        public GetQuery200ResponseQueryParam metrics(Model200MetricsMetrics metrics) {
            this.metrics = metrics;
            return this;
        }

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

        public GetQuery200ResponseQueryParam explanation(List<Model200ExplanationsExplanationInner> explanation) {
            this.explanation = explanation;
            return this;
        }
        public GetQuery200ResponseQueryParam addExplanationItem(Model200ExplanationsExplanationInner explanationItem) {
            this.explanation.add(explanationItem);
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class GetQuery200ResponseQueryParam {\n");

            sb.append("    result: ").append(toIndentedString(result)).append("\n");
            sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
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
