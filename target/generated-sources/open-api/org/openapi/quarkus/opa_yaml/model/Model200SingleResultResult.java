package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapi.quarkus.opa_yaml.model.Model200SingleResultResultBundles;
import org.openapi.quarkus.opa_yaml.model.Model200SingleResultResultDecisionLogs;
import org.openapi.quarkus.opa_yaml.model.Model200SingleResultResultKeys;
import org.openapi.quarkus.opa_yaml.model.Model200SingleResultResultLabels;
import org.openapi.quarkus.opa_yaml.model.Model200SingleResultResultServices;
import org.openapi.quarkus.opa_yaml.model.Model200SingleResultResultStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200SingleResultResult  {

    private Model200SingleResultResultServices services;
    private Model200SingleResultResultLabels labels;
    private Model200SingleResultResultKeys keys;
    private Model200SingleResultResultDecisionLogs decisionLogs;
    private Model200SingleResultResultStatus status;
    private Model200SingleResultResultBundles bundles;
    private String defaultAuthorizationDecision;
    private String defaultDecision;

    /**
    * Get services
    * @return services
    **/
    @JsonProperty("services")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200SingleResultResultServices getServices() {
        return services;
    }

    /**
     * Set services
     **/
    @JsonProperty("services")
    public void setServices(Model200SingleResultResultServices services) {
        this.services = services;
    }

    public Model200SingleResultResult services(Model200SingleResultResultServices services) {
        this.services = services;
        return this;
    }

    /**
    * Get labels
    * @return labels
    **/
    @JsonProperty("labels")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200SingleResultResultLabels getLabels() {
        return labels;
    }

    /**
     * Set labels
     **/
    @JsonProperty("labels")
    public void setLabels(Model200SingleResultResultLabels labels) {
        this.labels = labels;
    }

    public Model200SingleResultResult labels(Model200SingleResultResultLabels labels) {
        this.labels = labels;
        return this;
    }

    /**
    * Get keys
    * @return keys
    **/
    @JsonProperty("keys")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200SingleResultResultKeys getKeys() {
        return keys;
    }

    /**
     * Set keys
     **/
    @JsonProperty("keys")
    public void setKeys(Model200SingleResultResultKeys keys) {
        this.keys = keys;
    }

    public Model200SingleResultResult keys(Model200SingleResultResultKeys keys) {
        this.keys = keys;
        return this;
    }

    /**
    * Get decisionLogs
    * @return decisionLogs
    **/
    @JsonProperty("decision_logs")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200SingleResultResultDecisionLogs getDecisionLogs() {
        return decisionLogs;
    }

    /**
     * Set decisionLogs
     **/
    @JsonProperty("decision_logs")
    public void setDecisionLogs(Model200SingleResultResultDecisionLogs decisionLogs) {
        this.decisionLogs = decisionLogs;
    }

    public Model200SingleResultResult decisionLogs(Model200SingleResultResultDecisionLogs decisionLogs) {
        this.decisionLogs = decisionLogs;
        return this;
    }

    /**
    * Get status
    * @return status
    **/
    @JsonProperty("status")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200SingleResultResultStatus getStatus() {
        return status;
    }

    /**
     * Set status
     **/
    @JsonProperty("status")
    public void setStatus(Model200SingleResultResultStatus status) {
        this.status = status;
    }

    public Model200SingleResultResult status(Model200SingleResultResultStatus status) {
        this.status = status;
        return this;
    }

    /**
    * Get bundles
    * @return bundles
    **/
    @JsonProperty("bundles")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200SingleResultResultBundles getBundles() {
        return bundles;
    }

    /**
     * Set bundles
     **/
    @JsonProperty("bundles")
    public void setBundles(Model200SingleResultResultBundles bundles) {
        this.bundles = bundles;
    }

    public Model200SingleResultResult bundles(Model200SingleResultResultBundles bundles) {
        this.bundles = bundles;
        return this;
    }

    /**
    * Get defaultAuthorizationDecision
    * @return defaultAuthorizationDecision
    **/
    @JsonProperty("default_authorization_decision")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getDefaultAuthorizationDecision() {
        return defaultAuthorizationDecision;
    }

    /**
     * Set defaultAuthorizationDecision
     **/
    @JsonProperty("default_authorization_decision")
    public void setDefaultAuthorizationDecision(String defaultAuthorizationDecision) {
        this.defaultAuthorizationDecision = defaultAuthorizationDecision;
    }

    public Model200SingleResultResult defaultAuthorizationDecision(String defaultAuthorizationDecision) {
        this.defaultAuthorizationDecision = defaultAuthorizationDecision;
        return this;
    }

    /**
    * Get defaultDecision
    * @return defaultDecision
    **/
    @JsonProperty("default_decision")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getDefaultDecision() {
        return defaultDecision;
    }

    /**
     * Set defaultDecision
     **/
    @JsonProperty("default_decision")
    public void setDefaultDecision(String defaultDecision) {
        this.defaultDecision = defaultDecision;
    }

    public Model200SingleResultResult defaultDecision(String defaultDecision) {
        this.defaultDecision = defaultDecision;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200SingleResultResult {\n");

        sb.append("    services: ").append(toIndentedString(services)).append("\n");
        sb.append("    labels: ").append(toIndentedString(labels)).append("\n");
        sb.append("    keys: ").append(toIndentedString(keys)).append("\n");
        sb.append("    decisionLogs: ").append(toIndentedString(decisionLogs)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    bundles: ").append(toIndentedString(bundles)).append("\n");
        sb.append("    defaultAuthorizationDecision: ").append(toIndentedString(defaultAuthorizationDecision)).append("\n");
        sb.append("    defaultDecision: ").append(toIndentedString(defaultDecision)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200SingleResultResult} object that
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

        Model200SingleResultResult model = (Model200SingleResultResult) obj;

        return java.util.Objects.equals(services, model.services) &&
        java.util.Objects.equals(labels, model.labels) &&
        java.util.Objects.equals(keys, model.keys) &&
        java.util.Objects.equals(decisionLogs, model.decisionLogs) &&
        java.util.Objects.equals(status, model.status) &&
        java.util.Objects.equals(bundles, model.bundles) &&
        java.util.Objects.equals(defaultAuthorizationDecision, model.defaultAuthorizationDecision) &&
        java.util.Objects.equals(defaultDecision, model.defaultDecision);
    }

    /**
     * Returns a hash code for a {@code Model200SingleResultResult}.
     *
     * @return a hash code value for a {@code Model200SingleResultResult}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(services,
        labels,
        keys,
        decisionLogs,
        status,
        bundles,
        defaultAuthorizationDecision,
        defaultDecision);
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
    public static class Model200SingleResultResultQueryParam  {

        @jakarta.ws.rs.QueryParam("services")
        private Model200SingleResultResultServices services;
        @jakarta.ws.rs.QueryParam("labels")
        private Model200SingleResultResultLabels labels;
        @jakarta.ws.rs.QueryParam("keys")
        private Model200SingleResultResultKeys keys;
        @jakarta.ws.rs.QueryParam("decisionLogs")
        private Model200SingleResultResultDecisionLogs decisionLogs;
        @jakarta.ws.rs.QueryParam("status")
        private Model200SingleResultResultStatus status;
        @jakarta.ws.rs.QueryParam("bundles")
        private Model200SingleResultResultBundles bundles;
        @jakarta.ws.rs.QueryParam("defaultAuthorizationDecision")
        private String defaultAuthorizationDecision;
        @jakarta.ws.rs.QueryParam("defaultDecision")
        private String defaultDecision;

        /**
        * Get services
        * @return services
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("services")
        public Model200SingleResultResultServices getServices() {
            return services;
        }

        /**
         * Set services
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("services")
        public void setServices(Model200SingleResultResultServices services) {
            this.services = services;
        }

        public Model200SingleResultResultQueryParam services(Model200SingleResultResultServices services) {
            this.services = services;
            return this;
        }

        /**
        * Get labels
        * @return labels
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("labels")
        public Model200SingleResultResultLabels getLabels() {
            return labels;
        }

        /**
         * Set labels
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("labels")
        public void setLabels(Model200SingleResultResultLabels labels) {
            this.labels = labels;
        }

        public Model200SingleResultResultQueryParam labels(Model200SingleResultResultLabels labels) {
            this.labels = labels;
            return this;
        }

        /**
        * Get keys
        * @return keys
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("keys")
        public Model200SingleResultResultKeys getKeys() {
            return keys;
        }

        /**
         * Set keys
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("keys")
        public void setKeys(Model200SingleResultResultKeys keys) {
            this.keys = keys;
        }

        public Model200SingleResultResultQueryParam keys(Model200SingleResultResultKeys keys) {
            this.keys = keys;
            return this;
        }

        /**
        * Get decisionLogs
        * @return decisionLogs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("decision_logs")
        public Model200SingleResultResultDecisionLogs getDecisionLogs() {
            return decisionLogs;
        }

        /**
         * Set decisionLogs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("decision_logs")
        public void setDecisionLogs(Model200SingleResultResultDecisionLogs decisionLogs) {
            this.decisionLogs = decisionLogs;
        }

        public Model200SingleResultResultQueryParam decisionLogs(Model200SingleResultResultDecisionLogs decisionLogs) {
            this.decisionLogs = decisionLogs;
            return this;
        }

        /**
        * Get status
        * @return status
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("status")
        public Model200SingleResultResultStatus getStatus() {
            return status;
        }

        /**
         * Set status
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("status")
        public void setStatus(Model200SingleResultResultStatus status) {
            this.status = status;
        }

        public Model200SingleResultResultQueryParam status(Model200SingleResultResultStatus status) {
            this.status = status;
            return this;
        }

        /**
        * Get bundles
        * @return bundles
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("bundles")
        public Model200SingleResultResultBundles getBundles() {
            return bundles;
        }

        /**
         * Set bundles
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("bundles")
        public void setBundles(Model200SingleResultResultBundles bundles) {
            this.bundles = bundles;
        }

        public Model200SingleResultResultQueryParam bundles(Model200SingleResultResultBundles bundles) {
            this.bundles = bundles;
            return this;
        }

        /**
        * Get defaultAuthorizationDecision
        * @return defaultAuthorizationDecision
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("default_authorization_decision")
        public String getDefaultAuthorizationDecision() {
            return defaultAuthorizationDecision;
        }

        /**
         * Set defaultAuthorizationDecision
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("default_authorization_decision")
        public void setDefaultAuthorizationDecision(String defaultAuthorizationDecision) {
            this.defaultAuthorizationDecision = defaultAuthorizationDecision;
        }

        public Model200SingleResultResultQueryParam defaultAuthorizationDecision(String defaultAuthorizationDecision) {
            this.defaultAuthorizationDecision = defaultAuthorizationDecision;
            return this;
        }

        /**
        * Get defaultDecision
        * @return defaultDecision
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("default_decision")
        public String getDefaultDecision() {
            return defaultDecision;
        }

        /**
         * Set defaultDecision
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("default_decision")
        public void setDefaultDecision(String defaultDecision) {
            this.defaultDecision = defaultDecision;
        }

        public Model200SingleResultResultQueryParam defaultDecision(String defaultDecision) {
            this.defaultDecision = defaultDecision;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200SingleResultResultQueryParam {\n");

            sb.append("    services: ").append(toIndentedString(services)).append("\n");
            sb.append("    labels: ").append(toIndentedString(labels)).append("\n");
            sb.append("    keys: ").append(toIndentedString(keys)).append("\n");
            sb.append("    decisionLogs: ").append(toIndentedString(decisionLogs)).append("\n");
            sb.append("    status: ").append(toIndentedString(status)).append("\n");
            sb.append("    bundles: ").append(toIndentedString(bundles)).append("\n");
            sb.append("    defaultAuthorizationDecision: ").append(toIndentedString(defaultAuthorizationDecision)).append("\n");
            sb.append("    defaultDecision: ").append(toIndentedString(defaultDecision)).append("\n");
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
