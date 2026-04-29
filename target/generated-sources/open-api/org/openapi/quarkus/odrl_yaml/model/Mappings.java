package org.openapi.quarkus.odrl_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapi.quarkus.odrl_yaml.model.Mapping;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
  * Mappings available to be used for building policies
 **/
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Mappings  {

    private List<Mapping> actions;
    private List<Mapping> operators;
    private List<Mapping> operands;
    private List<Mapping> rightOperands;
    private List<Mapping> leftOperands;
    private List<Mapping> assignees;
    private List<Mapping> targets;
    private List<Mapping> constraints;

    /**
    * Get actions
    * @return actions
    **/
    @JsonProperty("actions")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public List<Mapping> getActions() {
        return actions;
    }

    /**
     * Set actions
     **/
    @JsonProperty("actions")
    public void setActions(List<Mapping> actions) {
        this.actions = actions;
    }

    public Mappings actions(List<Mapping> actions) {
        this.actions = actions;
        return this;
    }
    public Mappings addActionsItem(Mapping actionsItem) {
        if (this.actions == null){
            actions = new ArrayList<>();
        }
        this.actions.add(actionsItem);
        return this;
    }

    /**
    * Get operators
    * @return operators
    **/
    @JsonProperty("operators")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public List<Mapping> getOperators() {
        return operators;
    }

    /**
     * Set operators
     **/
    @JsonProperty("operators")
    public void setOperators(List<Mapping> operators) {
        this.operators = operators;
    }

    public Mappings operators(List<Mapping> operators) {
        this.operators = operators;
        return this;
    }
    public Mappings addOperatorsItem(Mapping operatorsItem) {
        if (this.operators == null){
            operators = new ArrayList<>();
        }
        this.operators.add(operatorsItem);
        return this;
    }

    /**
    * Get operands
    * @return operands
    **/
    @JsonProperty("operands")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public List<Mapping> getOperands() {
        return operands;
    }

    /**
     * Set operands
     **/
    @JsonProperty("operands")
    public void setOperands(List<Mapping> operands) {
        this.operands = operands;
    }

    public Mappings operands(List<Mapping> operands) {
        this.operands = operands;
        return this;
    }
    public Mappings addOperandsItem(Mapping operandsItem) {
        if (this.operands == null){
            operands = new ArrayList<>();
        }
        this.operands.add(operandsItem);
        return this;
    }

    /**
    * Get rightOperands
    * @return rightOperands
    **/
    @JsonProperty("rightOperands")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public List<Mapping> getRightOperands() {
        return rightOperands;
    }

    /**
     * Set rightOperands
     **/
    @JsonProperty("rightOperands")
    public void setRightOperands(List<Mapping> rightOperands) {
        this.rightOperands = rightOperands;
    }

    public Mappings rightOperands(List<Mapping> rightOperands) {
        this.rightOperands = rightOperands;
        return this;
    }
    public Mappings addRightOperandsItem(Mapping rightOperandsItem) {
        if (this.rightOperands == null){
            rightOperands = new ArrayList<>();
        }
        this.rightOperands.add(rightOperandsItem);
        return this;
    }

    /**
    * Get leftOperands
    * @return leftOperands
    **/
    @JsonProperty("leftOperands")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public List<Mapping> getLeftOperands() {
        return leftOperands;
    }

    /**
     * Set leftOperands
     **/
    @JsonProperty("leftOperands")
    public void setLeftOperands(List<Mapping> leftOperands) {
        this.leftOperands = leftOperands;
    }

    public Mappings leftOperands(List<Mapping> leftOperands) {
        this.leftOperands = leftOperands;
        return this;
    }
    public Mappings addLeftOperandsItem(Mapping leftOperandsItem) {
        if (this.leftOperands == null){
            leftOperands = new ArrayList<>();
        }
        this.leftOperands.add(leftOperandsItem);
        return this;
    }

    /**
    * Get assignees
    * @return assignees
    **/
    @JsonProperty("assignees")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public List<Mapping> getAssignees() {
        return assignees;
    }

    /**
     * Set assignees
     **/
    @JsonProperty("assignees")
    public void setAssignees(List<Mapping> assignees) {
        this.assignees = assignees;
    }

    public Mappings assignees(List<Mapping> assignees) {
        this.assignees = assignees;
        return this;
    }
    public Mappings addAssigneesItem(Mapping assigneesItem) {
        if (this.assignees == null){
            assignees = new ArrayList<>();
        }
        this.assignees.add(assigneesItem);
        return this;
    }

    /**
    * Get targets
    * @return targets
    **/
    @JsonProperty("targets")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public List<Mapping> getTargets() {
        return targets;
    }

    /**
     * Set targets
     **/
    @JsonProperty("targets")
    public void setTargets(List<Mapping> targets) {
        this.targets = targets;
    }

    public Mappings targets(List<Mapping> targets) {
        this.targets = targets;
        return this;
    }
    public Mappings addTargetsItem(Mapping targetsItem) {
        if (this.targets == null){
            targets = new ArrayList<>();
        }
        this.targets.add(targetsItem);
        return this;
    }

    /**
    * Get constraints
    * @return constraints
    **/
    @JsonProperty("constraints")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public List<Mapping> getConstraints() {
        return constraints;
    }

    /**
     * Set constraints
     **/
    @JsonProperty("constraints")
    public void setConstraints(List<Mapping> constraints) {
        this.constraints = constraints;
    }

    public Mappings constraints(List<Mapping> constraints) {
        this.constraints = constraints;
        return this;
    }
    public Mappings addConstraintsItem(Mapping constraintsItem) {
        if (this.constraints == null){
            constraints = new ArrayList<>();
        }
        this.constraints.add(constraintsItem);
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Mappings {\n");

        sb.append("    actions: ").append(toIndentedString(actions)).append("\n");
        sb.append("    operators: ").append(toIndentedString(operators)).append("\n");
        sb.append("    operands: ").append(toIndentedString(operands)).append("\n");
        sb.append("    rightOperands: ").append(toIndentedString(rightOperands)).append("\n");
        sb.append("    leftOperands: ").append(toIndentedString(leftOperands)).append("\n");
        sb.append("    assignees: ").append(toIndentedString(assignees)).append("\n");
        sb.append("    targets: ").append(toIndentedString(targets)).append("\n");
        sb.append("    constraints: ").append(toIndentedString(constraints)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Mappings} object that
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

        Mappings model = (Mappings) obj;

        return java.util.Objects.equals(actions, model.actions) &&
        java.util.Objects.equals(operators, model.operators) &&
        java.util.Objects.equals(operands, model.operands) &&
        java.util.Objects.equals(rightOperands, model.rightOperands) &&
        java.util.Objects.equals(leftOperands, model.leftOperands) &&
        java.util.Objects.equals(assignees, model.assignees) &&
        java.util.Objects.equals(targets, model.targets) &&
        java.util.Objects.equals(constraints, model.constraints);
    }

    /**
     * Returns a hash code for a {@code Mappings}.
     *
     * @return a hash code value for a {@code Mappings}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(actions,
        operators,
        operands,
        rightOperands,
        leftOperands,
        assignees,
        targets,
        constraints);
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
      * Mappings available to be used for building policies
     **/
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class MappingsQueryParam  {

        /**
          * Mappings available to be used for building policies
         **/
        @jakarta.ws.rs.QueryParam("actions")
        private List<Mapping> actions = null;
        /**
          * Mappings available to be used for building policies
         **/
        @jakarta.ws.rs.QueryParam("operators")
        private List<Mapping> operators = null;
        /**
          * Mappings available to be used for building policies
         **/
        @jakarta.ws.rs.QueryParam("operands")
        private List<Mapping> operands = null;
        /**
          * Mappings available to be used for building policies
         **/
        @jakarta.ws.rs.QueryParam("rightOperands")
        private List<Mapping> rightOperands = null;
        /**
          * Mappings available to be used for building policies
         **/
        @jakarta.ws.rs.QueryParam("leftOperands")
        private List<Mapping> leftOperands = null;
        /**
          * Mappings available to be used for building policies
         **/
        @jakarta.ws.rs.QueryParam("assignees")
        private List<Mapping> assignees = null;
        /**
          * Mappings available to be used for building policies
         **/
        @jakarta.ws.rs.QueryParam("targets")
        private List<Mapping> targets = null;
        /**
          * Mappings available to be used for building policies
         **/
        @jakarta.ws.rs.QueryParam("constraints")
        private List<Mapping> constraints = null;

        /**
        * Get actions
        * @return actions
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("actions")
        public List<Mapping> getActions() {
            return actions;
        }

        /**
         * Set actions
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("actions")
        public void setActions(List<Mapping> actions) {
            this.actions = actions;
        }

        public MappingsQueryParam actions(List<Mapping> actions) {
            this.actions = actions;
            return this;
        }
        public MappingsQueryParam addActionsItem(Mapping actionsItem) {
            this.actions.add(actionsItem);
            return this;
        }

        /**
        * Get operators
        * @return operators
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("operators")
        public List<Mapping> getOperators() {
            return operators;
        }

        /**
         * Set operators
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("operators")
        public void setOperators(List<Mapping> operators) {
            this.operators = operators;
        }

        public MappingsQueryParam operators(List<Mapping> operators) {
            this.operators = operators;
            return this;
        }
        public MappingsQueryParam addOperatorsItem(Mapping operatorsItem) {
            this.operators.add(operatorsItem);
            return this;
        }

        /**
        * Get operands
        * @return operands
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("operands")
        public List<Mapping> getOperands() {
            return operands;
        }

        /**
         * Set operands
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("operands")
        public void setOperands(List<Mapping> operands) {
            this.operands = operands;
        }

        public MappingsQueryParam operands(List<Mapping> operands) {
            this.operands = operands;
            return this;
        }
        public MappingsQueryParam addOperandsItem(Mapping operandsItem) {
            this.operands.add(operandsItem);
            return this;
        }

        /**
        * Get rightOperands
        * @return rightOperands
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("rightOperands")
        public List<Mapping> getRightOperands() {
            return rightOperands;
        }

        /**
         * Set rightOperands
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("rightOperands")
        public void setRightOperands(List<Mapping> rightOperands) {
            this.rightOperands = rightOperands;
        }

        public MappingsQueryParam rightOperands(List<Mapping> rightOperands) {
            this.rightOperands = rightOperands;
            return this;
        }
        public MappingsQueryParam addRightOperandsItem(Mapping rightOperandsItem) {
            this.rightOperands.add(rightOperandsItem);
            return this;
        }

        /**
        * Get leftOperands
        * @return leftOperands
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("leftOperands")
        public List<Mapping> getLeftOperands() {
            return leftOperands;
        }

        /**
         * Set leftOperands
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("leftOperands")
        public void setLeftOperands(List<Mapping> leftOperands) {
            this.leftOperands = leftOperands;
        }

        public MappingsQueryParam leftOperands(List<Mapping> leftOperands) {
            this.leftOperands = leftOperands;
            return this;
        }
        public MappingsQueryParam addLeftOperandsItem(Mapping leftOperandsItem) {
            this.leftOperands.add(leftOperandsItem);
            return this;
        }

        /**
        * Get assignees
        * @return assignees
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("assignees")
        public List<Mapping> getAssignees() {
            return assignees;
        }

        /**
         * Set assignees
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("assignees")
        public void setAssignees(List<Mapping> assignees) {
            this.assignees = assignees;
        }

        public MappingsQueryParam assignees(List<Mapping> assignees) {
            this.assignees = assignees;
            return this;
        }
        public MappingsQueryParam addAssigneesItem(Mapping assigneesItem) {
            this.assignees.add(assigneesItem);
            return this;
        }

        /**
        * Get targets
        * @return targets
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("targets")
        public List<Mapping> getTargets() {
            return targets;
        }

        /**
         * Set targets
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("targets")
        public void setTargets(List<Mapping> targets) {
            this.targets = targets;
        }

        public MappingsQueryParam targets(List<Mapping> targets) {
            this.targets = targets;
            return this;
        }
        public MappingsQueryParam addTargetsItem(Mapping targetsItem) {
            this.targets.add(targetsItem);
            return this;
        }

        /**
        * Get constraints
        * @return constraints
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("constraints")
        public List<Mapping> getConstraints() {
            return constraints;
        }

        /**
         * Set constraints
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("constraints")
        public void setConstraints(List<Mapping> constraints) {
            this.constraints = constraints;
        }

        public MappingsQueryParam constraints(List<Mapping> constraints) {
            this.constraints = constraints;
            return this;
        }
        public MappingsQueryParam addConstraintsItem(Mapping constraintsItem) {
            this.constraints.add(constraintsItem);
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class MappingsQueryParam {\n");

            sb.append("    actions: ").append(toIndentedString(actions)).append("\n");
            sb.append("    operators: ").append(toIndentedString(operators)).append("\n");
            sb.append("    operands: ").append(toIndentedString(operands)).append("\n");
            sb.append("    rightOperands: ").append(toIndentedString(rightOperands)).append("\n");
            sb.append("    leftOperands: ").append(toIndentedString(leftOperands)).append("\n");
            sb.append("    assignees: ").append(toIndentedString(assignees)).append("\n");
            sb.append("    targets: ").append(toIndentedString(targets)).append("\n");
            sb.append("    constraints: ").append(toIndentedString(constraints)).append("\n");
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
