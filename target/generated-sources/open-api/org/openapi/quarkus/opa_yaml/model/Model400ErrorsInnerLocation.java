package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
  * Where the error occurred
 **/
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model400ErrorsInnerLocation  {

    /**
      * The policy module name that generated the error
     **/
    private String _file;
    /**
      * The line number in the policy module where the error occurred
     **/
    private BigDecimal row;
    /**
      * The column in the policy module where the error occurred
     **/
    private BigDecimal col;

    /**
    * The policy module name that generated the error
    * @return _file
    **/
    @JsonProperty("file")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public String getFile() {
        return _file;
    }

    /**
     * Set _file
     **/
    @JsonProperty("file")
    public void setFile(String _file) {
        this._file = _file;
    }

    public Model400ErrorsInnerLocation _file(String _file) {
        this._file = _file;
        return this;
    }

    /**
    * The line number in the policy module where the error occurred
    * @return row
    **/
    @JsonProperty("row")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getRow() {
        return row;
    }

    /**
     * Set row
     **/
    @JsonProperty("row")
    public void setRow(BigDecimal row) {
        this.row = row;
    }

    public Model400ErrorsInnerLocation row(BigDecimal row) {
        this.row = row;
        return this;
    }

    /**
    * The column in the policy module where the error occurred
    * @return col
    **/
    @JsonProperty("col")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getCol() {
        return col;
    }

    /**
     * Set col
     **/
    @JsonProperty("col")
    public void setCol(BigDecimal col) {
        this.col = col;
    }

    public Model400ErrorsInnerLocation col(BigDecimal col) {
        this.col = col;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model400ErrorsInnerLocation {\n");

        sb.append("    _file: ").append(toIndentedString(_file)).append("\n");
        sb.append("    row: ").append(toIndentedString(row)).append("\n");
        sb.append("    col: ").append(toIndentedString(col)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model400ErrorsInnerLocation} object that
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

        Model400ErrorsInnerLocation model = (Model400ErrorsInnerLocation) obj;

        return java.util.Objects.equals(_file, model._file) &&
        java.util.Objects.equals(row, model.row) &&
        java.util.Objects.equals(col, model.col);
    }

    /**
     * Returns a hash code for a {@code Model400ErrorsInnerLocation}.
     *
     * @return a hash code value for a {@code Model400ErrorsInnerLocation}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(_file,
        row,
        col);
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
      * Where the error occurred
     **/
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class Model400ErrorsInnerLocationQueryParam  {

        /**
          * Where the error occurred
         **/
        @jakarta.ws.rs.QueryParam("_file")
        private String _file;
        /**
          * Where the error occurred
         **/
        @jakarta.ws.rs.QueryParam("row")
        private BigDecimal row;
        /**
          * Where the error occurred
         **/
        @jakarta.ws.rs.QueryParam("col")
        private BigDecimal col;

        /**
        * The policy module name that generated the error
        * @return _file
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("file")
        public String getFile() {
            return _file;
        }

        /**
         * Set _file
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("file")
        public void setFile(String _file) {
            this._file = _file;
        }

        public Model400ErrorsInnerLocationQueryParam _file(String _file) {
            this._file = _file;
            return this;
        }

        /**
        * The line number in the policy module where the error occurred
        * @return row
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("row")
        public BigDecimal getRow() {
            return row;
        }

        /**
         * Set row
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("row")
        public void setRow(BigDecimal row) {
            this.row = row;
        }

        public Model400ErrorsInnerLocationQueryParam row(BigDecimal row) {
            this.row = row;
            return this;
        }

        /**
        * The column in the policy module where the error occurred
        * @return col
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("col")
        public BigDecimal getCol() {
            return col;
        }

        /**
         * Set col
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("col")
        public void setCol(BigDecimal col) {
            this.col = col;
        }

        public Model400ErrorsInnerLocationQueryParam col(BigDecimal col) {
            this.col = col;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model400ErrorsInnerLocationQueryParam {\n");

            sb.append("    _file: ").append(toIndentedString(_file)).append("\n");
            sb.append("    row: ").append(toIndentedString(row)).append("\n");
            sb.append("    col: ").append(toIndentedString(col)).append("\n");
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
