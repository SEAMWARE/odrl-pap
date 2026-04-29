package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200SingleResultResultServicesAcmecorp  {

    private String url;

    /**
    * Get url
    * @return url
    **/
    @JsonProperty("url")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getUrl() {
        return url;
    }

    /**
     * Set url
     **/
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    public Model200SingleResultResultServicesAcmecorp url(String url) {
        this.url = url;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200SingleResultResultServicesAcmecorp {\n");

        sb.append("    url: ").append(toIndentedString(url)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200SingleResultResultServicesAcmecorp} object that
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

        Model200SingleResultResultServicesAcmecorp model = (Model200SingleResultResultServicesAcmecorp) obj;

        return java.util.Objects.equals(url, model.url);
    }

    /**
     * Returns a hash code for a {@code Model200SingleResultResultServicesAcmecorp}.
     *
     * @return a hash code value for a {@code Model200SingleResultResultServicesAcmecorp}.
     **/
    @Override
    public int hashCode() {
    return java.util.Objects.hash(url);
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
    public static class Model200SingleResultResultServicesAcmecorpQueryParam  {

        @jakarta.ws.rs.QueryParam("url")
        private String url;

        /**
        * Get url
        * @return url
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("url")
        public String getUrl() {
            return url;
        }

        /**
         * Set url
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("url")
        public void setUrl(String url) {
            this.url = url;
        }

        public Model200SingleResultResultServicesAcmecorpQueryParam url(String url) {
            this.url = url;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200SingleResultResultServicesAcmecorpQueryParam {\n");

            sb.append("    url: ").append(toIndentedString(url)).append("\n");
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
