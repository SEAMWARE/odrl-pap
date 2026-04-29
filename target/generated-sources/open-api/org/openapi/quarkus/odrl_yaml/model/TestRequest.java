package org.openapi.quarkus.odrl_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapi.quarkus.odrl_yaml.model.Headers;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class TestRequest  {


    public enum MethodEnum {
        POST(String.valueOf("POST")), PATCH(String.valueOf("PATCH")), PUT(String.valueOf("PUT")), GET(String.valueOf("GET")), DELETE(String.valueOf("DELETE"));

        // caching enum access
        private static final java.util.EnumSet<MethodEnum> values = java.util.EnumSet.allOf(MethodEnum.class);

        String value;

        MethodEnum (String v) {
            value = v;
        }

        @com.fasterxml.jackson.annotation.JsonValue
        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @com.fasterxml.jackson.annotation.JsonCreator
        public static MethodEnum fromString(String v) {
            for (MethodEnum b : values) {
                if (String.valueOf(b.value).equalsIgnoreCase(v)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + v + "'");
        }
    }
    private MethodEnum method;
    private String host;
    private String path;

    public enum ProtocolEnum {
        HTTPS(String.valueOf("https"));

        // caching enum access
        private static final java.util.EnumSet<ProtocolEnum> values = java.util.EnumSet.allOf(ProtocolEnum.class);

        String value;

        ProtocolEnum (String v) {
            value = v;
        }

        @com.fasterxml.jackson.annotation.JsonValue
        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @com.fasterxml.jackson.annotation.JsonCreator
        public static ProtocolEnum fromString(String v) {
            for (ProtocolEnum b : values) {
                if (String.valueOf(b.value).equalsIgnoreCase(v)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + v + "'");
        }
    }
    private ProtocolEnum protocol;
    private Object body;
    private Headers headers;

    /**
    * Get method
    * @return method
    **/
    @JsonProperty("method")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public MethodEnum getMethod() {
        return method;
    }

    /**
     * Set method
     **/
    @JsonProperty("method")
    public void setMethod(MethodEnum method) {
        this.method = method;
    }

    public TestRequest method(MethodEnum method) {
        this.method = method;
        return this;
    }

    /**
    * Get host
    * @return host
    **/
    @JsonProperty("host")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getHost() {
        return host;
    }

    /**
     * Set host
     **/
    @JsonProperty("host")
    public void setHost(String host) {
        this.host = host;
    }

    public TestRequest host(String host) {
        this.host = host;
        return this;
    }

    /**
    * Get path
    * @return path
    **/
    @JsonProperty("path")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getPath() {
        return path;
    }

    /**
     * Set path
     **/
    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    public TestRequest path(String path) {
        this.path = path;
        return this;
    }

    /**
    * Get protocol
    * @return protocol
    **/
    @JsonProperty("protocol")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public ProtocolEnum getProtocol() {
        return protocol;
    }

    /**
     * Set protocol
     **/
    @JsonProperty("protocol")
    public void setProtocol(ProtocolEnum protocol) {
        this.protocol = protocol;
    }

    public TestRequest protocol(ProtocolEnum protocol) {
        this.protocol = protocol;
        return this;
    }

    /**
    * Get body
    * @return body
    **/
    @JsonProperty("body")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Object getBody() {
        return body;
    }

    /**
     * Set body
     **/
    @JsonProperty("body")
    public void setBody(Object body) {
        this.body = body;
    }

    public TestRequest body(Object body) {
        this.body = body;
        return this;
    }

    /**
    * Get headers
    * @return headers
    **/
    @JsonProperty("headers")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Headers getHeaders() {
        return headers;
    }

    /**
     * Set headers
     **/
    @JsonProperty("headers")
    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public TestRequest headers(Headers headers) {
        this.headers = headers;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TestRequest {\n");

        sb.append("    method: ").append(toIndentedString(method)).append("\n");
        sb.append("    host: ").append(toIndentedString(host)).append("\n");
        sb.append("    path: ").append(toIndentedString(path)).append("\n");
        sb.append("    protocol: ").append(toIndentedString(protocol)).append("\n");
        sb.append("    body: ").append(toIndentedString(body)).append("\n");
        sb.append("    headers: ").append(toIndentedString(headers)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code TestRequest} object that
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

        TestRequest model = (TestRequest) obj;

        return java.util.Objects.equals(method, model.method) &&
        java.util.Objects.equals(host, model.host) &&
        java.util.Objects.equals(path, model.path) &&
        java.util.Objects.equals(protocol, model.protocol) &&
        java.util.Objects.equals(body, model.body) &&
        java.util.Objects.equals(headers, model.headers);
    }

    /**
     * Returns a hash code for a {@code TestRequest}.
     *
     * @return a hash code value for a {@code TestRequest}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(method,
        host,
        path,
        protocol,
        body,
        headers);
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
    public static class TestRequestQueryParam  {


    public enum MethodEnum {
        POST(String.valueOf("POST")), PATCH(String.valueOf("PATCH")), PUT(String.valueOf("PUT")), GET(String.valueOf("GET")), DELETE(String.valueOf("DELETE"));

        // caching enum access
        private static final java.util.EnumSet<MethodEnum> values = java.util.EnumSet.allOf(MethodEnum.class);

        String value;

        MethodEnum (String v) {
            value = v;
        }

        @com.fasterxml.jackson.annotation.JsonValue
        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @com.fasterxml.jackson.annotation.JsonCreator
        public static MethodEnum fromString(String v) {
            for (MethodEnum b : values) {
                if (String.valueOf(b.value).equalsIgnoreCase(v)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + v + "'");
        }
    }
        private MethodEnum method;
        @jakarta.ws.rs.QueryParam("host")
        private String host;
        @jakarta.ws.rs.QueryParam("path")
        private String path;

    public enum ProtocolEnum {
        HTTPS(String.valueOf("https"));

        // caching enum access
        private static final java.util.EnumSet<ProtocolEnum> values = java.util.EnumSet.allOf(ProtocolEnum.class);

        String value;

        ProtocolEnum (String v) {
            value = v;
        }

        @com.fasterxml.jackson.annotation.JsonValue
        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @com.fasterxml.jackson.annotation.JsonCreator
        public static ProtocolEnum fromString(String v) {
            for (ProtocolEnum b : values) {
                if (String.valueOf(b.value).equalsIgnoreCase(v)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + v + "'");
        }
    }
        private ProtocolEnum protocol;
        @jakarta.ws.rs.QueryParam("body")
        private Object body;
        @jakarta.ws.rs.QueryParam("headers")
        private Headers headers;

        /**
        * Get method
        * @return method
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("method")
        public MethodEnum getMethod() {
            return method;
        }

        /**
         * Set method
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("method")
        public void setMethod(MethodEnum method) {
            this.method = method;
        }

        public TestRequestQueryParam method(MethodEnum method) {
            this.method = method;
            return this;
        }

        /**
        * Get host
        * @return host
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("host")
        public String getHost() {
            return host;
        }

        /**
         * Set host
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("host")
        public void setHost(String host) {
            this.host = host;
        }

        public TestRequestQueryParam host(String host) {
            this.host = host;
            return this;
        }

        /**
        * Get path
        * @return path
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("path")
        public String getPath() {
            return path;
        }

        /**
         * Set path
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("path")
        public void setPath(String path) {
            this.path = path;
        }

        public TestRequestQueryParam path(String path) {
            this.path = path;
            return this;
        }

        /**
        * Get protocol
        * @return protocol
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("protocol")
        public ProtocolEnum getProtocol() {
            return protocol;
        }

        /**
         * Set protocol
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("protocol")
        public void setProtocol(ProtocolEnum protocol) {
            this.protocol = protocol;
        }

        public TestRequestQueryParam protocol(ProtocolEnum protocol) {
            this.protocol = protocol;
            return this;
        }

        /**
        * Get body
        * @return body
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("body")
        public Object getBody() {
            return body;
        }

        /**
         * Set body
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("body")
        public void setBody(Object body) {
            this.body = body;
        }

        public TestRequestQueryParam body(Object body) {
            this.body = body;
            return this;
        }

        /**
        * Get headers
        * @return headers
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("headers")
        public Headers getHeaders() {
            return headers;
        }

        /**
         * Set headers
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("headers")
        public void setHeaders(Headers headers) {
            this.headers = headers;
        }

        public TestRequestQueryParam headers(Headers headers) {
            this.headers = headers;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class TestRequestQueryParam {\n");

            sb.append("    method: ").append(toIndentedString(method)).append("\n");
            sb.append("    host: ").append(toIndentedString(host)).append("\n");
            sb.append("    path: ").append(toIndentedString(path)).append("\n");
            sb.append("    protocol: ").append(toIndentedString(protocol)).append("\n");
            sb.append("    body: ").append(toIndentedString(body)).append("\n");
            sb.append("    headers: ").append(toIndentedString(headers)).append("\n");
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
