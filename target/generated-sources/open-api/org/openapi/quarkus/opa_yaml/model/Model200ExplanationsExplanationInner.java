package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapi.quarkus.opa_yaml.model.Model200ExplanationsExplanationInnerLocalsInner;
import org.openapi.quarkus.opa_yaml.model.Model200ExplanationsExplanationInnerNode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200ExplanationsExplanationInner  {


    public enum OpEnum {
        ENTER(String.valueOf("enter")), EXIT(String.valueOf("exit")), EVAL(String.valueOf("eval")), FAIL(String.valueOf("fail")), REDO(String.valueOf("redo"));

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
      * The kind of *trace event*  Each trace event represents a step in the query evaluation process. Trace events are emitted at the following points: - enter - before a body or rule is evaluated - exit - after a body or rule has evaluated successfully - eval - before an expression is evaluated - fail - after an expression has evaluated to false. - redo - before evaluation restarts from a body, rule, or expression.  By default, OPA searches for all sets of term bindings that make all expressions in the query evaluate to true. Because there may be multiple answers, the search can restart when OPA determines the query is true or false. When the search restarts, a *redo trace event* is emitted.
     **/
    private OpEnum op;
    /**
      * The query that the trace event was emitted for. Use this field to distinguish trace events emitted by from different queries.
     **/
    private BigDecimal queryId;
    /**
      * The parent query. Use this field to identify trace events from related queries.  For example, if query A references rule R, trace events emitted when evaluating rule R will have the *parent_id* field set to query A’s *query_id*.
     **/
    private BigDecimal parentId;

    public enum TypeEnum {
        EXPR(String.valueOf("expr")), RULE(String.valueOf("rule")), BODY(String.valueOf("body"));

        // caching enum access
        private static final java.util.EnumSet<TypeEnum> values = java.util.EnumSet.allOf(TypeEnum.class);

        String value;

        TypeEnum (String v) {
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
        public static TypeEnum fromString(String v) {
            for (TypeEnum b : values) {
                if (String.valueOf(b.value).equalsIgnoreCase(v)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + v + "'");
        }
    }
    /**
      * The type of the **node** field
     **/
    private TypeEnum type;
    private Model200ExplanationsExplanationInnerNode node;
    /**
      * The query's term bindings at the point when the trace event was emitted.
     **/
    private List<Model200ExplanationsExplanationInnerLocalsInner> locals;

    /**
    * The kind of *trace event*  Each trace event represents a step in the query evaluation process. Trace events are emitted at the following points: - enter - before a body or rule is evaluated - exit - after a body or rule has evaluated successfully - eval - before an expression is evaluated - fail - after an expression has evaluated to false. - redo - before evaluation restarts from a body, rule, or expression.  By default, OPA searches for all sets of term bindings that make all expressions in the query evaluate to true. Because there may be multiple answers, the search can restart when OPA determines the query is true or false. When the search restarts, a *redo trace event* is emitted.
    * @return op
    **/
    @JsonProperty("op")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
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

    public Model200ExplanationsExplanationInner op(OpEnum op) {
        this.op = op;
        return this;
    }

    /**
    * The query that the trace event was emitted for. Use this field to distinguish trace events emitted by from different queries.
    * minimum: 0
    * @return queryId
    **/
    @JsonProperty("query_id")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getQueryId() {
        return queryId;
    }

    /**
     * Set queryId
     **/
    @JsonProperty("query_id")
    public void setQueryId(BigDecimal queryId) {
        this.queryId = queryId;
    }

    public Model200ExplanationsExplanationInner queryId(BigDecimal queryId) {
        this.queryId = queryId;
        return this;
    }

    /**
    * The parent query. Use this field to identify trace events from related queries.  For example, if query A references rule R, trace events emitted when evaluating rule R will have the *parent_id* field set to query A’s *query_id*.
    * minimum: 0
    * @return parentId
    **/
    @JsonProperty("parent_id")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getParentId() {
        return parentId;
    }

    /**
     * Set parentId
     **/
    @JsonProperty("parent_id")
    public void setParentId(BigDecimal parentId) {
        this.parentId = parentId;
    }

    public Model200ExplanationsExplanationInner parentId(BigDecimal parentId) {
        this.parentId = parentId;
        return this;
    }

    /**
    * The type of the **node** field
    * @return type
    **/
    @JsonProperty("type")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public TypeEnum getType() {
        return type;
    }

    /**
     * Set type
     **/
    @JsonProperty("type")
    public void setType(TypeEnum type) {
        this.type = type;
    }

    public Model200ExplanationsExplanationInner type(TypeEnum type) {
        this.type = type;
        return this;
    }

    /**
    * Get node
    * @return node
    **/
    @JsonProperty("node")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Model200ExplanationsExplanationInnerNode getNode() {
        return node;
    }

    /**
     * Set node
     **/
    @JsonProperty("node")
    public void setNode(Model200ExplanationsExplanationInnerNode node) {
        this.node = node;
    }

    public Model200ExplanationsExplanationInner node(Model200ExplanationsExplanationInnerNode node) {
        this.node = node;
        return this;
    }

    /**
    * The query's term bindings at the point when the trace event was emitted.
    * @return locals
    **/
    @JsonProperty("locals")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public List<Model200ExplanationsExplanationInnerLocalsInner> getLocals() {
        return locals;
    }

    /**
     * Set locals
     **/
    @JsonProperty("locals")
    public void setLocals(List<Model200ExplanationsExplanationInnerLocalsInner> locals) {
        this.locals = locals;
    }

    public Model200ExplanationsExplanationInner locals(List<Model200ExplanationsExplanationInnerLocalsInner> locals) {
        this.locals = locals;
        return this;
    }
    public Model200ExplanationsExplanationInner addLocalsItem(Model200ExplanationsExplanationInnerLocalsInner localsItem) {
        if (this.locals == null){
            locals = new ArrayList<>();
        }
        this.locals.add(localsItem);
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200ExplanationsExplanationInner {\n");

        sb.append("    op: ").append(toIndentedString(op)).append("\n");
        sb.append("    queryId: ").append(toIndentedString(queryId)).append("\n");
        sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    node: ").append(toIndentedString(node)).append("\n");
        sb.append("    locals: ").append(toIndentedString(locals)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200ExplanationsExplanationInner} object that
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

        Model200ExplanationsExplanationInner model = (Model200ExplanationsExplanationInner) obj;

        return java.util.Objects.equals(op, model.op) &&
        java.util.Objects.equals(queryId, model.queryId) &&
        java.util.Objects.equals(parentId, model.parentId) &&
        java.util.Objects.equals(type, model.type) &&
        java.util.Objects.equals(node, model.node) &&
        java.util.Objects.equals(locals, model.locals);
    }

    /**
     * Returns a hash code for a {@code Model200ExplanationsExplanationInner}.
     *
     * @return a hash code value for a {@code Model200ExplanationsExplanationInner}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(op,
        queryId,
        parentId,
        type,
        node,
        locals);
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
    public static class Model200ExplanationsExplanationInnerQueryParam  {


    public enum OpEnum {
        ENTER(String.valueOf("enter")), EXIT(String.valueOf("exit")), EVAL(String.valueOf("eval")), FAIL(String.valueOf("fail")), REDO(String.valueOf("redo"));

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
        private OpEnum op;
        @jakarta.ws.rs.QueryParam("queryId")
        private BigDecimal queryId;
        @jakarta.ws.rs.QueryParam("parentId")
        private BigDecimal parentId;

    public enum TypeEnum {
        EXPR(String.valueOf("expr")), RULE(String.valueOf("rule")), BODY(String.valueOf("body"));

        // caching enum access
        private static final java.util.EnumSet<TypeEnum> values = java.util.EnumSet.allOf(TypeEnum.class);

        String value;

        TypeEnum (String v) {
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
        public static TypeEnum fromString(String v) {
            for (TypeEnum b : values) {
                if (String.valueOf(b.value).equalsIgnoreCase(v)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + v + "'");
        }
    }
        private TypeEnum type;
        @jakarta.ws.rs.QueryParam("node")
        private Model200ExplanationsExplanationInnerNode node;
        @jakarta.ws.rs.QueryParam("locals")
        private List<Model200ExplanationsExplanationInnerLocalsInner> locals = null;

        /**
        * The kind of *trace event*  Each trace event represents a step in the query evaluation process. Trace events are emitted at the following points: - enter - before a body or rule is evaluated - exit - after a body or rule has evaluated successfully - eval - before an expression is evaluated - fail - after an expression has evaluated to false. - redo - before evaluation restarts from a body, rule, or expression.  By default, OPA searches for all sets of term bindings that make all expressions in the query evaluate to true. Because there may be multiple answers, the search can restart when OPA determines the query is true or false. When the search restarts, a *redo trace event* is emitted.
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

        public Model200ExplanationsExplanationInnerQueryParam op(OpEnum op) {
            this.op = op;
            return this;
        }

        /**
        * The query that the trace event was emitted for. Use this field to distinguish trace events emitted by from different queries.
        * minimum: 0
        * @return queryId
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("query_id")
        public BigDecimal getQueryId() {
            return queryId;
        }

        /**
         * Set queryId
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("query_id")
        public void setQueryId(BigDecimal queryId) {
            this.queryId = queryId;
        }

        public Model200ExplanationsExplanationInnerQueryParam queryId(BigDecimal queryId) {
            this.queryId = queryId;
            return this;
        }

        /**
        * The parent query. Use this field to identify trace events from related queries.  For example, if query A references rule R, trace events emitted when evaluating rule R will have the *parent_id* field set to query A’s *query_id*.
        * minimum: 0
        * @return parentId
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("parent_id")
        public BigDecimal getParentId() {
            return parentId;
        }

        /**
         * Set parentId
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("parent_id")
        public void setParentId(BigDecimal parentId) {
            this.parentId = parentId;
        }

        public Model200ExplanationsExplanationInnerQueryParam parentId(BigDecimal parentId) {
            this.parentId = parentId;
            return this;
        }

        /**
        * The type of the **node** field
        * @return type
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("type")
        public TypeEnum getType() {
            return type;
        }

        /**
         * Set type
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("type")
        public void setType(TypeEnum type) {
            this.type = type;
        }

        public Model200ExplanationsExplanationInnerQueryParam type(TypeEnum type) {
            this.type = type;
            return this;
        }

        /**
        * Get node
        * @return node
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("node")
        public Model200ExplanationsExplanationInnerNode getNode() {
            return node;
        }

        /**
         * Set node
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("node")
        public void setNode(Model200ExplanationsExplanationInnerNode node) {
            this.node = node;
        }

        public Model200ExplanationsExplanationInnerQueryParam node(Model200ExplanationsExplanationInnerNode node) {
            this.node = node;
            return this;
        }

        /**
        * The query's term bindings at the point when the trace event was emitted.
        * @return locals
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("locals")
        public List<Model200ExplanationsExplanationInnerLocalsInner> getLocals() {
            return locals;
        }

        /**
         * Set locals
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("locals")
        public void setLocals(List<Model200ExplanationsExplanationInnerLocalsInner> locals) {
            this.locals = locals;
        }

        public Model200ExplanationsExplanationInnerQueryParam locals(List<Model200ExplanationsExplanationInnerLocalsInner> locals) {
            this.locals = locals;
            return this;
        }
        public Model200ExplanationsExplanationInnerQueryParam addLocalsItem(Model200ExplanationsExplanationInnerLocalsInner localsItem) {
            this.locals.add(localsItem);
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200ExplanationsExplanationInnerQueryParam {\n");

            sb.append("    op: ").append(toIndentedString(op)).append("\n");
            sb.append("    queryId: ").append(toIndentedString(queryId)).append("\n");
            sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
            sb.append("    type: ").append(toIndentedString(type)).append("\n");
            sb.append("    node: ").append(toIndentedString(node)).append("\n");
            sb.append("    locals: ").append(toIndentedString(locals)).append("\n");
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
