package org.openapi.quarkus.odrl_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Headers  {

    private String accept;
    private String authorization;
    private String contentType;

    /**
    * Get accept
    * @return accept
    **/
    @JsonProperty("accept")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getAccept() {
        return accept;
    }

    /**
     * Set accept
     **/
    @JsonProperty("accept")
    public void setAccept(String accept) {
        this.accept = accept;
    }

    public Headers accept(String accept) {
        this.accept = accept;
        return this;
    }

    /**
    * Get authorization
    * @return authorization
    **/
    @JsonProperty("authorization")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getAuthorization() {
        return authorization;
    }

    /**
     * Set authorization
     **/
    @JsonProperty("authorization")
    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public Headers authorization(String authorization) {
        this.authorization = authorization;
        return this;
    }

    /**
    * Get contentType
    * @return contentType
    **/
    @JsonProperty("content-type")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getContentType() {
        return contentType;
    }

    /**
     * Set contentType
     **/
    @JsonProperty("content-type")
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Headers contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Headers {\n");

        sb.append("    accept: ").append(toIndentedString(accept)).append("\n");
        sb.append("    authorization: ").append(toIndentedString(authorization)).append("\n");
        sb.append("    contentType: ").append(toIndentedString(contentType)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Headers} object that
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

        Headers model = (Headers) obj;

        return java.util.Objects.equals(accept, model.accept) &&
        java.util.Objects.equals(authorization, model.authorization) &&
        java.util.Objects.equals(contentType, model.contentType);
    }

    /**
     * Returns a hash code for a {@code Headers}.
     *
     * @return a hash code value for a {@code Headers}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(accept,
        authorization,
        contentType);
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
    public static class HeadersQueryParam  {

        @jakarta.ws.rs.QueryParam("accept")
        private String accept;
        @jakarta.ws.rs.QueryParam("authorization")
        private String authorization;
        @jakarta.ws.rs.QueryParam("contentType")
        private String contentType;

        /**
        * Get accept
        * @return accept
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("accept")
        public String getAccept() {
            return accept;
        }

        /**
         * Set accept
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("accept")
        public void setAccept(String accept) {
            this.accept = accept;
        }

        public HeadersQueryParam accept(String accept) {
            this.accept = accept;
            return this;
        }

        /**
        * Get authorization
        * @return authorization
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("authorization")
        public String getAuthorization() {
            return authorization;
        }

        /**
         * Set authorization
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("authorization")
        public void setAuthorization(String authorization) {
            this.authorization = authorization;
        }

        public HeadersQueryParam authorization(String authorization) {
            this.authorization = authorization;
            return this;
        }

        /**
        * Get contentType
        * @return contentType
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("content-type")
        public String getContentType() {
            return contentType;
        }

        /**
         * Set contentType
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("content-type")
        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public HeadersQueryParam contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class HeadersQueryParam {\n");

            sb.append("    accept: ").append(toIndentedString(accept)).append("\n");
            sb.append("    authorization: ").append(toIndentedString(authorization)).append("\n");
            sb.append("    contentType: ").append(toIndentedString(contentType)).append("\n");
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
