package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapi.quarkus.opa_yaml.model.Model200ResultResultInnerAst;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200ResultResultInner  {

    /**
      * The name of a policy module
     **/
    private String id;
    /**
      * A string representation of the full Rego policy
     **/
    private String raw;
    private Model200ResultResultInnerAst ast;

    /**
    * The name of a policy module
    * @return id
    **/
    @JsonProperty("id")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getId() {
        return id;
    }

    /**
     * Set id
     **/
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public Model200ResultResultInner id(String id) {
        this.id = id;
        return this;
    }

    /**
    * A string representation of the full Rego policy
    * @return raw
    **/
    @JsonProperty("raw")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getRaw() {
        return raw;
    }

    /**
     * Set raw
     **/
    @JsonProperty("raw")
    public void setRaw(String raw) {
        this.raw = raw;
    }

    public Model200ResultResultInner raw(String raw) {
        this.raw = raw;
        return this;
    }

    /**
    * Get ast
    * @return ast
    **/
    @JsonProperty("ast")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200ResultResultInnerAst getAst() {
        return ast;
    }

    /**
     * Set ast
     **/
    @JsonProperty("ast")
    public void setAst(Model200ResultResultInnerAst ast) {
        this.ast = ast;
    }

    public Model200ResultResultInner ast(Model200ResultResultInnerAst ast) {
        this.ast = ast;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200ResultResultInner {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    raw: ").append(toIndentedString(raw)).append("\n");
        sb.append("    ast: ").append(toIndentedString(ast)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200ResultResultInner} object that
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

        Model200ResultResultInner model = (Model200ResultResultInner) obj;

        return java.util.Objects.equals(id, model.id) &&
        java.util.Objects.equals(raw, model.raw) &&
        java.util.Objects.equals(ast, model.ast);
    }

    /**
     * Returns a hash code for a {@code Model200ResultResultInner}.
     *
     * @return a hash code value for a {@code Model200ResultResultInner}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id,
        raw,
        ast);
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
    public static class Model200ResultResultInnerQueryParam  {

        @jakarta.ws.rs.QueryParam("id")
        private String id;
        @jakarta.ws.rs.QueryParam("raw")
        private String raw;
        @jakarta.ws.rs.QueryParam("ast")
        private Model200ResultResultInnerAst ast;

        /**
        * The name of a policy module
        * @return id
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("id")
        public String getId() {
            return id;
        }

        /**
         * Set id
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("id")
        public void setId(String id) {
            this.id = id;
        }

        public Model200ResultResultInnerQueryParam id(String id) {
            this.id = id;
            return this;
        }

        /**
        * A string representation of the full Rego policy
        * @return raw
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("raw")
        public String getRaw() {
            return raw;
        }

        /**
         * Set raw
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("raw")
        public void setRaw(String raw) {
            this.raw = raw;
        }

        public Model200ResultResultInnerQueryParam raw(String raw) {
            this.raw = raw;
            return this;
        }

        /**
        * Get ast
        * @return ast
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("ast")
        public Model200ResultResultInnerAst getAst() {
            return ast;
        }

        /**
         * Set ast
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("ast")
        public void setAst(Model200ResultResultInnerAst ast) {
            this.ast = ast;
        }

        public Model200ResultResultInnerQueryParam ast(Model200ResultResultInnerAst ast) {
            this.ast = ast;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200ResultResultInnerQueryParam {\n");

            sb.append("    id: ").append(toIndentedString(id)).append("\n");
            sb.append("    raw: ").append(toIndentedString(raw)).append("\n");
            sb.append("    ast: ").append(toIndentedString(ast)).append("\n");
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
