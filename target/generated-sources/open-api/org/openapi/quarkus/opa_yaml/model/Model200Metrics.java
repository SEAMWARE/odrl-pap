package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapi.quarkus.opa_yaml.model.Model200MetricsMetrics;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200Metrics  {

    private Model200MetricsMetrics metrics;

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

    public Model200Metrics metrics(Model200MetricsMetrics metrics) {
        this.metrics = metrics;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200Metrics {\n");

        sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200Metrics} object that
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

        Model200Metrics model = (Model200Metrics) obj;

        return java.util.Objects.equals(metrics, model.metrics);
    }

    /**
     * Returns a hash code for a {@code Model200Metrics}.
     *
     * @return a hash code value for a {@code Model200Metrics}.
     **/
    @Override
    public int hashCode() {
    return java.util.Objects.hash(metrics);
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
    public static class Model200MetricsQueryParam  {

        @jakarta.ws.rs.QueryParam("metrics")
        private Model200MetricsMetrics metrics;

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

        public Model200MetricsQueryParam metrics(Model200MetricsMetrics metrics) {
            this.metrics = metrics;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200MetricsQueryParam {\n");

            sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
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
