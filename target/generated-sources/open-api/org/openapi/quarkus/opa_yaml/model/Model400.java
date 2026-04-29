package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import org.openapi.quarkus.opa_yaml.model.Model400ErrorsInner;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model400  {

    /**
      * The error code name
     **/
    private String code;
    /**
      * The description of the error
     **/
    private String message;
    /**
      * Errors that may have been generated during the parse, compile, or installation of a policy module
     **/
    private Set<Model400ErrorsInner> errors;

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

    public Model400 code(String code) {
        this.code = code;
        return this;
    }

    /**
    * The description of the error
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

    public Model400 message(String message) {
        this.message = message;
        return this;
    }

    /**
    * Errors that may have been generated during the parse, compile, or installation of a policy module
    * @return errors
    **/
    @JsonProperty("errors")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Set<Model400ErrorsInner> getErrors() {
        return errors;
    }

    /**
     * Set errors
     **/
    @JsonProperty("errors")
    public void setErrors(Set<Model400ErrorsInner> errors) {
        this.errors = errors;
    }

    public Model400 errors(Set<Model400ErrorsInner> errors) {
        this.errors = errors;
        return this;
    }
    public Model400 addErrorsItem(Model400ErrorsInner errorsItem) {
        if (this.errors == null){
            errors = new LinkedHashSet<>();
        }
        this.errors.add(errorsItem);
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model400 {\n");

        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("    errors: ").append(toIndentedString(errors)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model400} object that
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

        Model400 model = (Model400) obj;

        return java.util.Objects.equals(code, model.code) &&
        java.util.Objects.equals(message, model.message) &&
        java.util.Objects.equals(errors, model.errors);
    }

    /**
     * Returns a hash code for a {@code Model400}.
     *
     * @return a hash code value for a {@code Model400}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(code,
        message,
        errors);
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
    public static class Model400QueryParam  {

        @jakarta.ws.rs.QueryParam("code")
        private String code;
        @jakarta.ws.rs.QueryParam("message")
        private String message;
        @jakarta.ws.rs.QueryParam("errors")
        private Set<Model400ErrorsInner> errors = null;

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

        public Model400QueryParam code(String code) {
            this.code = code;
            return this;
        }

        /**
        * The description of the error
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

        public Model400QueryParam message(String message) {
            this.message = message;
            return this;
        }

        /**
        * Errors that may have been generated during the parse, compile, or installation of a policy module
        * @return errors
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("errors")
        public Set<Model400ErrorsInner> getErrors() {
            return errors;
        }

        /**
         * Set errors
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("errors")
        public void setErrors(Set<Model400ErrorsInner> errors) {
            this.errors = errors;
        }

        public Model400QueryParam errors(Set<Model400ErrorsInner> errors) {
            this.errors = errors;
            return this;
        }
        public Model400QueryParam addErrorsItem(Model400ErrorsInner errorsItem) {
            this.errors.add(errorsItem);
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model400QueryParam {\n");

            sb.append("    code: ").append(toIndentedString(code)).append("\n");
            sb.append("    message: ").append(toIndentedString(message)).append("\n");
            sb.append("    errors: ").append(toIndentedString(errors)).append("\n");
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
