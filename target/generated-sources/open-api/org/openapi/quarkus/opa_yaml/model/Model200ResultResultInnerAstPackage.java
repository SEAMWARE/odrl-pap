package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapi.quarkus.opa_yaml.model.Model200ResultResultInnerAstPackagePathInner;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200ResultResultInnerAstPackage  {

    /**
      * The path to the package
     **/
    private List<Model200ResultResultInnerAstPackagePathInner> path;

    /**
    * The path to the package
    * @return path
    **/
    @JsonProperty("path")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public List<Model200ResultResultInnerAstPackagePathInner> getPath() {
        return path;
    }

    /**
     * Set path
     **/
    @JsonProperty("path")
    public void setPath(List<Model200ResultResultInnerAstPackagePathInner> path) {
        this.path = path;
    }

    public Model200ResultResultInnerAstPackage path(List<Model200ResultResultInnerAstPackagePathInner> path) {
        this.path = path;
        return this;
    }
    public Model200ResultResultInnerAstPackage addPathItem(Model200ResultResultInnerAstPackagePathInner pathItem) {
        if (this.path == null){
            path = new ArrayList<>();
        }
        this.path.add(pathItem);
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200ResultResultInnerAstPackage {\n");

        sb.append("    path: ").append(toIndentedString(path)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200ResultResultInnerAstPackage} object that
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

        Model200ResultResultInnerAstPackage model = (Model200ResultResultInnerAstPackage) obj;

        return java.util.Objects.equals(path, model.path);
    }

    /**
     * Returns a hash code for a {@code Model200ResultResultInnerAstPackage}.
     *
     * @return a hash code value for a {@code Model200ResultResultInnerAstPackage}.
     **/
    @Override
    public int hashCode() {
    return java.util.Objects.hash(path);
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
    public static class Model200ResultResultInnerAstPackageQueryParam  {

        @jakarta.ws.rs.QueryParam("path")
        private List<Model200ResultResultInnerAstPackagePathInner> path = null;

        /**
        * The path to the package
        * @return path
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("path")
        public List<Model200ResultResultInnerAstPackagePathInner> getPath() {
            return path;
        }

        /**
         * Set path
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("path")
        public void setPath(List<Model200ResultResultInnerAstPackagePathInner> path) {
            this.path = path;
        }

        public Model200ResultResultInnerAstPackageQueryParam path(List<Model200ResultResultInnerAstPackagePathInner> path) {
            this.path = path;
            return this;
        }
        public Model200ResultResultInnerAstPackageQueryParam addPathItem(Model200ResultResultInnerAstPackagePathInner pathItem) {
            this.path.add(pathItem);
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200ResultResultInnerAstPackageQueryParam {\n");

            sb.append("    path: ").append(toIndentedString(path)).append("\n");
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
