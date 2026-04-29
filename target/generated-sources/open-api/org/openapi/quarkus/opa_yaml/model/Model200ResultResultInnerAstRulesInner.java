package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapi.quarkus.opa_yaml.model.Model200ResultResultInnerAstRulesInnerBodyInner;
import org.openapi.quarkus.opa_yaml.model.Model200ResultResultInnerAstRulesInnerHead;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200ResultResultInnerAstRulesInner  {

    private Model200ResultResultInnerAstRulesInnerHead head;
    /**
      * A list of the terms in this rule
     **/
    private List<Model200ResultResultInnerAstRulesInnerBodyInner> body;

    /**
    * Get head
    * @return head
    **/
    @JsonProperty("head")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200ResultResultInnerAstRulesInnerHead getHead() {
        return head;
    }

    /**
     * Set head
     **/
    @JsonProperty("head")
    public void setHead(Model200ResultResultInnerAstRulesInnerHead head) {
        this.head = head;
    }

    public Model200ResultResultInnerAstRulesInner head(Model200ResultResultInnerAstRulesInnerHead head) {
        this.head = head;
        return this;
    }

    /**
    * A list of the terms in this rule
    * @return body
    **/
    @JsonProperty("body")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public List<Model200ResultResultInnerAstRulesInnerBodyInner> getBody() {
        return body;
    }

    /**
     * Set body
     **/
    @JsonProperty("body")
    public void setBody(List<Model200ResultResultInnerAstRulesInnerBodyInner> body) {
        this.body = body;
    }

    public Model200ResultResultInnerAstRulesInner body(List<Model200ResultResultInnerAstRulesInnerBodyInner> body) {
        this.body = body;
        return this;
    }
    public Model200ResultResultInnerAstRulesInner addBodyItem(Model200ResultResultInnerAstRulesInnerBodyInner bodyItem) {
        if (this.body == null){
            body = new ArrayList<>();
        }
        this.body.add(bodyItem);
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200ResultResultInnerAstRulesInner {\n");

        sb.append("    head: ").append(toIndentedString(head)).append("\n");
        sb.append("    body: ").append(toIndentedString(body)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200ResultResultInnerAstRulesInner} object that
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

        Model200ResultResultInnerAstRulesInner model = (Model200ResultResultInnerAstRulesInner) obj;

        return java.util.Objects.equals(head, model.head) &&
        java.util.Objects.equals(body, model.body);
    }

    /**
     * Returns a hash code for a {@code Model200ResultResultInnerAstRulesInner}.
     *
     * @return a hash code value for a {@code Model200ResultResultInnerAstRulesInner}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(head,
        body);
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
    public static class Model200ResultResultInnerAstRulesInnerQueryParam  {

        @jakarta.ws.rs.QueryParam("head")
        private Model200ResultResultInnerAstRulesInnerHead head;
        @jakarta.ws.rs.QueryParam("body")
        private List<Model200ResultResultInnerAstRulesInnerBodyInner> body = null;

        /**
        * Get head
        * @return head
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("head")
        public Model200ResultResultInnerAstRulesInnerHead getHead() {
            return head;
        }

        /**
         * Set head
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("head")
        public void setHead(Model200ResultResultInnerAstRulesInnerHead head) {
            this.head = head;
        }

        public Model200ResultResultInnerAstRulesInnerQueryParam head(Model200ResultResultInnerAstRulesInnerHead head) {
            this.head = head;
            return this;
        }

        /**
        * A list of the terms in this rule
        * @return body
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("body")
        public List<Model200ResultResultInnerAstRulesInnerBodyInner> getBody() {
            return body;
        }

        /**
         * Set body
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("body")
        public void setBody(List<Model200ResultResultInnerAstRulesInnerBodyInner> body) {
            this.body = body;
        }

        public Model200ResultResultInnerAstRulesInnerQueryParam body(List<Model200ResultResultInnerAstRulesInnerBodyInner> body) {
            this.body = body;
            return this;
        }
        public Model200ResultResultInnerAstRulesInnerQueryParam addBodyItem(Model200ResultResultInnerAstRulesInnerBodyInner bodyItem) {
            this.body.add(bodyItem);
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200ResultResultInnerAstRulesInnerQueryParam {\n");

            sb.append("    head: ").append(toIndentedString(head)).append("\n");
            sb.append("    body: ").append(toIndentedString(body)).append("\n");
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
