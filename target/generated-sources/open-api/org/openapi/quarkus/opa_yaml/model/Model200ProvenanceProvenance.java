package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200ProvenanceProvenance  {

    /**
      * The version of this OPA instance
     **/
    private String version;
    /**
      * The Git commit id of this OPA build.
     **/
    private String buildCommit;
    /**
      * When this OPA instance was built (in [ISO8601 format](https://www.w3.org/TR/NOTE-datetime))
     **/
    private String buildTimestamp;
    /**
      * The hostname where this instance was built.
     **/
    private String buildHostname;
    /**
      * A set of key-value pairs describing each bundle activated on the server.
     **/
    private Map<String, Object> bundles;

    /**
    * The version of this OPA instance
    * @return version
    **/
    @JsonProperty("version")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getVersion() {
        return version;
    }

    /**
     * Set version
     **/
    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    public Model200ProvenanceProvenance version(String version) {
        this.version = version;
        return this;
    }

    /**
    * The Git commit id of this OPA build.
    * @return buildCommit
    **/
    @JsonProperty("build_commit")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getBuildCommit() {
        return buildCommit;
    }

    /**
     * Set buildCommit
     **/
    @JsonProperty("build_commit")
    public void setBuildCommit(String buildCommit) {
        this.buildCommit = buildCommit;
    }

    public Model200ProvenanceProvenance buildCommit(String buildCommit) {
        this.buildCommit = buildCommit;
        return this;
    }

    /**
    * When this OPA instance was built (in [ISO8601 format](https://www.w3.org/TR/NOTE-datetime))
    * @return buildTimestamp
    **/
    @JsonProperty("build_timestamp")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getBuildTimestamp() {
        return buildTimestamp;
    }

    /**
     * Set buildTimestamp
     **/
    @JsonProperty("build_timestamp")
    public void setBuildTimestamp(String buildTimestamp) {
        this.buildTimestamp = buildTimestamp;
    }

    public Model200ProvenanceProvenance buildTimestamp(String buildTimestamp) {
        this.buildTimestamp = buildTimestamp;
        return this;
    }

    /**
    * The hostname where this instance was built.
    * @return buildHostname
    **/
    @JsonProperty("build_hostname")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getBuildHostname() {
        return buildHostname;
    }

    /**
     * Set buildHostname
     **/
    @JsonProperty("build_hostname")
    public void setBuildHostname(String buildHostname) {
        this.buildHostname = buildHostname;
    }

    public Model200ProvenanceProvenance buildHostname(String buildHostname) {
        this.buildHostname = buildHostname;
        return this;
    }

    /**
    * A set of key-value pairs describing each bundle activated on the server.
    * @return bundles
    **/
    @JsonProperty("bundles")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Map<String, Object> getBundles() {
        return bundles;
    }

    /**
     * Set bundles
     **/
    @JsonProperty("bundles")
    public void setBundles(Map<String, Object> bundles) {
        this.bundles = bundles;
    }

    public Model200ProvenanceProvenance bundles(Map<String, Object> bundles) {
        this.bundles = bundles;
        return this;
    }
    public Model200ProvenanceProvenance putBundlesItem(String mapKey, Object bundlesItem) {
           if (this.bundles == null){
                bundles = new HashMap<>();
            }
        this.bundles.put(mapKey, bundlesItem);
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200ProvenanceProvenance {\n");

        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    buildCommit: ").append(toIndentedString(buildCommit)).append("\n");
        sb.append("    buildTimestamp: ").append(toIndentedString(buildTimestamp)).append("\n");
        sb.append("    buildHostname: ").append(toIndentedString(buildHostname)).append("\n");
        sb.append("    bundles: ").append(toIndentedString(bundles)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200ProvenanceProvenance} object that
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

        Model200ProvenanceProvenance model = (Model200ProvenanceProvenance) obj;

        return java.util.Objects.equals(version, model.version) &&
        java.util.Objects.equals(buildCommit, model.buildCommit) &&
        java.util.Objects.equals(buildTimestamp, model.buildTimestamp) &&
        java.util.Objects.equals(buildHostname, model.buildHostname) &&
        java.util.Objects.equals(bundles, model.bundles);
    }

    /**
     * Returns a hash code for a {@code Model200ProvenanceProvenance}.
     *
     * @return a hash code value for a {@code Model200ProvenanceProvenance}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(version,
        buildCommit,
        buildTimestamp,
        buildHostname,
        bundles);
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
    public static class Model200ProvenanceProvenanceQueryParam  {

        @jakarta.ws.rs.QueryParam("version")
        private String version;
        @jakarta.ws.rs.QueryParam("buildCommit")
        private String buildCommit;
        @jakarta.ws.rs.QueryParam("buildTimestamp")
        private String buildTimestamp;
        @jakarta.ws.rs.QueryParam("buildHostname")
        private String buildHostname;
        @jakarta.ws.rs.QueryParam("bundles")
        private Map<String, Object> bundles = null;

        /**
        * The version of this OPA instance
        * @return version
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("version")
        public String getVersion() {
            return version;
        }

        /**
         * Set version
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("version")
        public void setVersion(String version) {
            this.version = version;
        }

        public Model200ProvenanceProvenanceQueryParam version(String version) {
            this.version = version;
            return this;
        }

        /**
        * The Git commit id of this OPA build.
        * @return buildCommit
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("build_commit")
        public String getBuildCommit() {
            return buildCommit;
        }

        /**
         * Set buildCommit
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("build_commit")
        public void setBuildCommit(String buildCommit) {
            this.buildCommit = buildCommit;
        }

        public Model200ProvenanceProvenanceQueryParam buildCommit(String buildCommit) {
            this.buildCommit = buildCommit;
            return this;
        }

        /**
        * When this OPA instance was built (in [ISO8601 format](https://www.w3.org/TR/NOTE-datetime))
        * @return buildTimestamp
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("build_timestamp")
        public String getBuildTimestamp() {
            return buildTimestamp;
        }

        /**
         * Set buildTimestamp
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("build_timestamp")
        public void setBuildTimestamp(String buildTimestamp) {
            this.buildTimestamp = buildTimestamp;
        }

        public Model200ProvenanceProvenanceQueryParam buildTimestamp(String buildTimestamp) {
            this.buildTimestamp = buildTimestamp;
            return this;
        }

        /**
        * The hostname where this instance was built.
        * @return buildHostname
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("build_hostname")
        public String getBuildHostname() {
            return buildHostname;
        }

        /**
         * Set buildHostname
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("build_hostname")
        public void setBuildHostname(String buildHostname) {
            this.buildHostname = buildHostname;
        }

        public Model200ProvenanceProvenanceQueryParam buildHostname(String buildHostname) {
            this.buildHostname = buildHostname;
            return this;
        }

        /**
        * A set of key-value pairs describing each bundle activated on the server.
        * @return bundles
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("bundles")
        public Map<String, Object> getBundles() {
            return bundles;
        }

        /**
         * Set bundles
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("bundles")
        public void setBundles(Map<String, Object> bundles) {
            this.bundles = bundles;
        }

        public Model200ProvenanceProvenanceQueryParam bundles(Map<String, Object> bundles) {
            this.bundles = bundles;
            return this;
        }
        public Model200ProvenanceProvenanceQueryParam putBundlesItem(String mapKey, Object bundlesItem) {
            this.bundles.put(mapKey, bundlesItem);
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200ProvenanceProvenanceQueryParam {\n");

            sb.append("    version: ").append(toIndentedString(version)).append("\n");
            sb.append("    buildCommit: ").append(toIndentedString(buildCommit)).append("\n");
            sb.append("    buildTimestamp: ").append(toIndentedString(buildTimestamp)).append("\n");
            sb.append("    buildHostname: ").append(toIndentedString(buildHostname)).append("\n");
            sb.append("    bundles: ").append(toIndentedString(bundles)).append("\n");
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
