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

/**
  * A JSON patch operation
 **/
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class PatchesSchemaInner  {


    public enum OpEnum {
        ADD(String.valueOf("add")), REMOVE(String.valueOf("remove")), REPLACE(String.valueOf("replace")), MOVE(String.valueOf("move")), COPY(String.valueOf("copy")), TEST(String.valueOf("test"));

        // caching enum access
        private static final java.util.EnumSet<OpEnum> values = java.util.EnumSet.allOf(OpEnum.class);

        String value;

        OpEnum (String v) {
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
        public static OpEnum fromString(String v) {
            for (OpEnum b : values) {
                if (String.valueOf(b.value).equalsIgnoreCase(v)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + v + "'");
        }
    }
    /**
      * JSON patch operation type
     **/
    private OpEnum op;
    /**
      * A [JSON pointer](https://tools.ietf.org/html/rfc6901) to a location within the target document where the operation is performed.  The *effective path* is this value prefixed with the API call's `path` parameter.  The server will return a *bad request* (404) response if:  - The *parent* of the effective path does not refer to an existing document - For **remove** and **replace** operations, the effective path does not refer to an existing document.
     **/
    private String path;
    /**
      * The value to add, replace or test.
     **/
    private Map<String, Object> value;

    /**
    * JSON patch operation type
    * @return op
    **/
    @JsonProperty("op")
    public OpEnum getOp() {
        return op;
    }

    /**
     * Set op
     **/
    @JsonProperty("op")
    public void setOp(OpEnum op) {
        this.op = op;
    }

    public PatchesSchemaInner op(OpEnum op) {
        this.op = op;
        return this;
    }

    /**
    * A [JSON pointer](https://tools.ietf.org/html/rfc6901) to a location within the target document where the operation is performed.  The *effective path* is this value prefixed with the API call's `path` parameter.  The server will return a *bad request* (404) response if:  - The *parent* of the effective path does not refer to an existing document - For **remove** and **replace** operations, the effective path does not refer to an existing document.
    * @return path
    **/
    @JsonProperty("path")
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

    public PatchesSchemaInner path(String path) {
        this.path = path;
        return this;
    }

    /**
    * The value to add, replace or test.
    * @return value
    **/
    @JsonProperty("value")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Map<String, Object> getValue() {
        return value;
    }

    /**
     * Set value
     **/
    @JsonProperty("value")
    public void setValue(Map<String, Object> value) {
        this.value = value;
    }

    public PatchesSchemaInner value(Map<String, Object> value) {
        this.value = value;
        return this;
    }
    public PatchesSchemaInner putValueItem(String mapKey, Object valueItem) {
           if (this.value == null){
                value = new HashMap<>();
            }
        this.value.put(mapKey, valueItem);
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PatchesSchemaInner {\n");

        sb.append("    op: ").append(toIndentedString(op)).append("\n");
        sb.append("    path: ").append(toIndentedString(path)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code PatchesSchemaInner} object that
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

        PatchesSchemaInner model = (PatchesSchemaInner) obj;

        return java.util.Objects.equals(op, model.op) &&
        java.util.Objects.equals(path, model.path) &&
        java.util.Objects.equals(value, model.value);
    }

    /**
     * Returns a hash code for a {@code PatchesSchemaInner}.
     *
     * @return a hash code value for a {@code PatchesSchemaInner}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(op,
        path,
        value);
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

    /**
      * A JSON patch operation
     **/
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class PatchesSchemaInnerQueryParam  {


    public enum OpEnum {
        ADD(String.valueOf("add")), REMOVE(String.valueOf("remove")), REPLACE(String.valueOf("replace")), MOVE(String.valueOf("move")), COPY(String.valueOf("copy")), TEST(String.valueOf("test"));

        // caching enum access
        private static final java.util.EnumSet<OpEnum> values = java.util.EnumSet.allOf(OpEnum.class);

        String value;

        OpEnum (String v) {
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
        public static OpEnum fromString(String v) {
            for (OpEnum b : values) {
                if (String.valueOf(b.value).equalsIgnoreCase(v)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + v + "'");
        }
    }
        /**
          * A JSON patch operation
         **/
        private OpEnum op;
        /**
          * A JSON patch operation
         **/
        @jakarta.ws.rs.QueryParam("path")
        private String path;
        /**
          * A JSON patch operation
         **/
        @jakarta.ws.rs.QueryParam("value")
        private Map<String, Object> value = null;

        /**
        * JSON patch operation type
        * @return op
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("op")
        public OpEnum getOp() {
            return op;
        }

        /**
         * Set op
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("op")
        public void setOp(OpEnum op) {
            this.op = op;
        }

        public PatchesSchemaInnerQueryParam op(OpEnum op) {
            this.op = op;
            return this;
        }

        /**
        * A [JSON pointer](https://tools.ietf.org/html/rfc6901) to a location within the target document where the operation is performed.  The *effective path* is this value prefixed with the API call's `path` parameter.  The server will return a *bad request* (404) response if:  - The *parent* of the effective path does not refer to an existing document - For **remove** and **replace** operations, the effective path does not refer to an existing document.
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

        public PatchesSchemaInnerQueryParam path(String path) {
            this.path = path;
            return this;
        }

        /**
        * The value to add, replace or test.
        * @return value
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("value")
        public Map<String, Object> getValue() {
            return value;
        }

        /**
         * Set value
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("value")
        public void setValue(Map<String, Object> value) {
            this.value = value;
        }

        public PatchesSchemaInnerQueryParam value(Map<String, Object> value) {
            this.value = value;
            return this;
        }
        public PatchesSchemaInnerQueryParam putValueItem(String mapKey, Object valueItem) {
            this.value.put(mapKey, valueItem);
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class PatchesSchemaInnerQueryParam {\n");

            sb.append("    op: ").append(toIndentedString(op)).append("\n");
            sb.append("    path: ").append(toIndentedString(path)).append("\n");
            sb.append("    value: ").append(toIndentedString(value)).append("\n");
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
