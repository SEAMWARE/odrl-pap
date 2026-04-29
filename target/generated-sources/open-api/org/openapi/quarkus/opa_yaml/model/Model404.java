package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model404  {

    /**
      * The error code name
     **/
    private String code;
    /**
      * The description of the error (including the name of any undefined policy module)
     **/
    private String message;

    /**
    * The error code name
    * @return code
    **/
    @JsonProperty("code")
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

    public Model404 code(String code) {
        this.code = code;
        return this;
    }

    /**
    * The description of the error (including the name of any undefined policy module)
    * @return message
    **/
    @JsonProperty("message")
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

    public Model404 message(String message) {
        this.message = message;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model404 {\n");

        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model404} object that
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

        Model404 model = (Model404) obj;

        return java.util.Objects.equals(code, model.code) &&
        java.util.Objects.equals(message, model.message);
    }

    /**
     * Returns a hash code for a {@code Model404}.
     *
     * @return a hash code value for a {@code Model404}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(code,
        message);
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
    public static class Model404QueryParam  {

        @jakarta.ws.rs.QueryParam("code")
        private String code;
        @jakarta.ws.rs.QueryParam("message")
        private String message;

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

        public Model404QueryParam code(String code) {
            this.code = code;
            return this;
        }

        /**
        * The description of the error (including the name of any undefined policy module)
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

        public Model404QueryParam message(String message) {
            this.message = message;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model404QueryParam {\n");

            sb.append("    code: ").append(toIndentedString(code)).append("\n");
            sb.append("    message: ").append(toIndentedString(message)).append("\n");
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
