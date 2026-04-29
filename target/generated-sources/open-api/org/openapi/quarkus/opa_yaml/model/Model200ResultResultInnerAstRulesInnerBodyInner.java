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
import org.openapi.quarkus.opa_yaml.model.Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200ResultResultInnerAstRulesInnerBodyInner  {

    /**
      * The location of this term in the list (starts at 0)
     **/
    private BigDecimal index;
    /**
      * The type/value pairing for this term
     **/
    private List<Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner> terms;

    /**
    * The location of this term in the list (starts at 0)
    * @return index
    **/
    @JsonProperty("index")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getIndex() {
        return index;
    }

    /**
     * Set index
     **/
    @JsonProperty("index")
    public void setIndex(BigDecimal index) {
        this.index = index;
    }

    public Model200ResultResultInnerAstRulesInnerBodyInner index(BigDecimal index) {
        this.index = index;
        return this;
    }

    /**
    * The type/value pairing for this term
    * @return terms
    **/
    @JsonProperty("terms")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public List<Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner> getTerms() {
        return terms;
    }

    /**
     * Set terms
     **/
    @JsonProperty("terms")
    public void setTerms(List<Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner> terms) {
        this.terms = terms;
    }

    public Model200ResultResultInnerAstRulesInnerBodyInner terms(List<Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner> terms) {
        this.terms = terms;
        return this;
    }
    public Model200ResultResultInnerAstRulesInnerBodyInner addTermsItem(Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner termsItem) {
        if (this.terms == null){
            terms = new ArrayList<>();
        }
        this.terms.add(termsItem);
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200ResultResultInnerAstRulesInnerBodyInner {\n");

        sb.append("    index: ").append(toIndentedString(index)).append("\n");
        sb.append("    terms: ").append(toIndentedString(terms)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200ResultResultInnerAstRulesInnerBodyInner} object that
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

        Model200ResultResultInnerAstRulesInnerBodyInner model = (Model200ResultResultInnerAstRulesInnerBodyInner) obj;

        return java.util.Objects.equals(index, model.index) &&
        java.util.Objects.equals(terms, model.terms);
    }

    /**
     * Returns a hash code for a {@code Model200ResultResultInnerAstRulesInnerBodyInner}.
     *
     * @return a hash code value for a {@code Model200ResultResultInnerAstRulesInnerBodyInner}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(index,
        terms);
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
    public static class Model200ResultResultInnerAstRulesInnerBodyInnerQueryParam  {

        @jakarta.ws.rs.QueryParam("index")
        private BigDecimal index;
        @jakarta.ws.rs.QueryParam("terms")
        private List<Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner> terms = null;

        /**
        * The location of this term in the list (starts at 0)
        * @return index
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("index")
        public BigDecimal getIndex() {
            return index;
        }

        /**
         * Set index
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("index")
        public void setIndex(BigDecimal index) {
            this.index = index;
        }

        public Model200ResultResultInnerAstRulesInnerBodyInnerQueryParam index(BigDecimal index) {
            this.index = index;
            return this;
        }

        /**
        * The type/value pairing for this term
        * @return terms
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("terms")
        public List<Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner> getTerms() {
            return terms;
        }

        /**
         * Set terms
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("terms")
        public void setTerms(List<Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner> terms) {
            this.terms = terms;
        }

        public Model200ResultResultInnerAstRulesInnerBodyInnerQueryParam terms(List<Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner> terms) {
            this.terms = terms;
            return this;
        }
        public Model200ResultResultInnerAstRulesInnerBodyInnerQueryParam addTermsItem(Model200ResultResultInnerAstRulesInnerBodyInnerTermsInner termsItem) {
            this.terms.add(termsItem);
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200ResultResultInnerAstRulesInnerBodyInnerQueryParam {\n");

            sb.append("    index: ").append(toIndentedString(index)).append("\n");
            sb.append("    terms: ").append(toIndentedString(terms)).append("\n");
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
