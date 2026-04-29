package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapi.quarkus.opa_yaml.model.Model400ErrorsInnerLocation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model400ErrorsInner  {

    /**
      * The error code name
     **/
    private String code;
    /**
      * A general description of the error
     **/
    private String message;
    private Model400ErrorsInnerLocation location;

    /**
    * The error code name
    * @return code
    **/
    @JsonProperty("code")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getCode() {
        return code;
    }

    /**
     * Set code
     **/
    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    public Model400ErrorsInner code(String code) {
        this.code = code;
        return this;
    }

    /**
    * A general description of the error
    * @return message
    **/
    @JsonProperty("message")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getMessage() {
        return message;
    }

    /**
     * Set message
     **/
    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    public Model400ErrorsInner message(String message) {
        this.message = message;
        return this;
    }

    /**
    * Get location
    * @return location
    **/
    @JsonProperty("location")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model400ErrorsInnerLocation getLocation() {
        return location;
    }

    /**
     * Set location
     **/
    @JsonProperty("location")
    public void setLocation(Model400ErrorsInnerLocation location) {
        this.location = location;
    }

    public Model400ErrorsInner location(Model400ErrorsInnerLocation location) {
        this.location = location;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model400ErrorsInner {\n");

        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("    location: ").append(toIndentedString(location)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model400ErrorsInner} object that
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

        Model400ErrorsInner model = (Model400ErrorsInner) obj;

        return java.util.Objects.equals(code, model.code) &&
        java.util.Objects.equals(message, model.message) &&
        java.util.Objects.equals(location, model.location);
    }

    /**
     * Returns a hash code for a {@code Model400ErrorsInner}.
     *
     * @return a hash code value for a {@code Model400ErrorsInner}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(code,
        message,
        location);
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
    public static class Model400ErrorsInnerQueryParam  {

        @jakarta.ws.rs.QueryParam("code")
        private String code;
        @jakarta.ws.rs.QueryParam("message")
        private String message;
        @jakarta.ws.rs.QueryParam("location")
        private Model400ErrorsInnerLocation location;

        /**
        * The error code name
        * @return code
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("code")
        public String getCode() {
            return code;
        }

        /**
         * Set code
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("code")
        public void setCode(String code) {
            this.code = code;
        }

        public Model400ErrorsInnerQueryParam code(String code) {
            this.code = code;
            return this;
        }

        /**
        * A general description of the error
        * @return message
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("message")
        public String getMessage() {
            return message;
        }

        /**
         * Set message
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("message")
        public void setMessage(String message) {
            this.message = message;
        }

        public Model400ErrorsInnerQueryParam message(String message) {
            this.message = message;
            return this;
        }

        /**
        * Get location
        * @return location
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("location")
        public Model400ErrorsInnerLocation getLocation() {
            return location;
        }

        /**
         * Set location
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("location")
        public void setLocation(Model400ErrorsInnerLocation location) {
            this.location = location;
        }

        public Model400ErrorsInnerQueryParam location(Model400ErrorsInnerLocation location) {
            this.location = location;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model400ErrorsInnerQueryParam {\n");

            sb.append("    code: ").append(toIndentedString(code)).append("\n");
            sb.append("    message: ").append(toIndentedString(message)).append("\n");
            sb.append("    location: ").append(toIndentedString(location)).append("\n");
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
