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
import org.openapi.quarkus.opa_yaml.model.Model200ResultResultInnerAstPackage;
import org.openapi.quarkus.opa_yaml.model.Model200ResultResultInnerAstRulesInner;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
  * The types for declarations and runtime objects passed to your implementation. This consists of an abstract syntax tree (AST) of policy modules, package and import declarations, rules, expressions, and terms.
 **/
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200ResultResultInnerAst  {

    private Model200ResultResultInnerAstPackage _package;
    /**
      * When OPA evaluates a rule, it generates the content of a [virtual documents](https://www.openpolicyagent.org/docs/latest/philosophy/#the-opa-document-model)
     **/
    private Set<Model200ResultResultInnerAstRulesInner> rules;

    /**
    * Get _package
    * @return _package
    **/
    @JsonProperty("package")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200ResultResultInnerAstPackage getPackage() {
        return _package;
    }

    /**
     * Set _package
     **/
    @JsonProperty("package")
    public void setPackage(Model200ResultResultInnerAstPackage _package) {
        this._package = _package;
    }

    public Model200ResultResultInnerAst _package(Model200ResultResultInnerAstPackage _package) {
        this._package = _package;
        return this;
    }

    /**
    * When OPA evaluates a rule, it generates the content of a [virtual documents](https://www.openpolicyagent.org/docs/latest/philosophy/#the-opa-document-model)
    * @return rules
    **/
    @JsonProperty("rules")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Set<Model200ResultResultInnerAstRulesInner> getRules() {
        return rules;
    }

    /**
     * Set rules
     **/
    @JsonProperty("rules")
    public void setRules(Set<Model200ResultResultInnerAstRulesInner> rules) {
        this.rules = rules;
    }

    public Model200ResultResultInnerAst rules(Set<Model200ResultResultInnerAstRulesInner> rules) {
        this.rules = rules;
        return this;
    }
    public Model200ResultResultInnerAst addRulesItem(Model200ResultResultInnerAstRulesInner rulesItem) {
        if (this.rules == null){
            rules = new LinkedHashSet<>();
        }
        this.rules.add(rulesItem);
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200ResultResultInnerAst {\n");

        sb.append("    _package: ").append(toIndentedString(_package)).append("\n");
        sb.append("    rules: ").append(toIndentedString(rules)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200ResultResultInnerAst} object that
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

        Model200ResultResultInnerAst model = (Model200ResultResultInnerAst) obj;

        return java.util.Objects.equals(_package, model._package) &&
        java.util.Objects.equals(rules, model.rules);
    }

    /**
     * Returns a hash code for a {@code Model200ResultResultInnerAst}.
     *
     * @return a hash code value for a {@code Model200ResultResultInnerAst}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(_package,
        rules);
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
      * The types for declarations and runtime objects passed to your implementation. This consists of an abstract syntax tree (AST) of policy modules, package and import declarations, rules, expressions, and terms.
     **/
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class Model200ResultResultInnerAstQueryParam  {

        /**
          * The types for declarations and runtime objects passed to your implementation. This consists of an abstract syntax tree (AST) of policy modules, package and import declarations, rules, expressions, and terms.
         **/
        @jakarta.ws.rs.QueryParam("_package")
        private Model200ResultResultInnerAstPackage _package;
        /**
          * The types for declarations and runtime objects passed to your implementation. This consists of an abstract syntax tree (AST) of policy modules, package and import declarations, rules, expressions, and terms.
         **/
        @jakarta.ws.rs.QueryParam("rules")
        private Set<Model200ResultResultInnerAstRulesInner> rules = null;

        /**
        * Get _package
        * @return _package
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("package")
        public Model200ResultResultInnerAstPackage getPackage() {
            return _package;
        }

        /**
         * Set _package
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("package")
        public void setPackage(Model200ResultResultInnerAstPackage _package) {
            this._package = _package;
        }

        public Model200ResultResultInnerAstQueryParam _package(Model200ResultResultInnerAstPackage _package) {
            this._package = _package;
            return this;
        }

        /**
        * When OPA evaluates a rule, it generates the content of a [virtual documents](https://www.openpolicyagent.org/docs/latest/philosophy/#the-opa-document-model)
        * @return rules
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("rules")
        public Set<Model200ResultResultInnerAstRulesInner> getRules() {
            return rules;
        }

        /**
         * Set rules
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("rules")
        public void setRules(Set<Model200ResultResultInnerAstRulesInner> rules) {
            this.rules = rules;
        }

        public Model200ResultResultInnerAstQueryParam rules(Set<Model200ResultResultInnerAstRulesInner> rules) {
            this.rules = rules;
            return this;
        }
        public Model200ResultResultInnerAstQueryParam addRulesItem(Model200ResultResultInnerAstRulesInner rulesItem) {
            this.rules.add(rulesItem);
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200ResultResultInnerAstQueryParam {\n");

            sb.append("    _package: ").append(toIndentedString(_package)).append("\n");
            sb.append("    rules: ").append(toIndentedString(rules)).append("\n");
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
