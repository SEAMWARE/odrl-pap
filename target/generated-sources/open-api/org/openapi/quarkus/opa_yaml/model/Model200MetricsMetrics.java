package org.openapi.quarkus.opa_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Model200MetricsMetrics  {

    /**
      * Time taken (in nanonseconds) to parse the input
     **/
    private BigDecimal timerRegoInputParseNs;
    /**
      * Time taken (in nanoseconds) to parse the query
     **/
    private BigDecimal timerRegoQueryParseNs;
    /**
      * Time taken (in nanoseconds) to compile the query
     **/
    private BigDecimal timerRegoQueryCompileNs;
    /**
      * Time taken (in nanonseconds) to evaluate the query
     **/
    private BigDecimal timerRegoQueryEvalNs;
    /**
      * Time taken (in nanoseconds) to parse the input policy module
     **/
    private BigDecimal timerRegoModuleParseNs;
    /**
      * Time taken (in nanonseconds) to compile the loaded policy modules
     **/
    private BigDecimal timerRegoModuleCompileNs;
    /**
      * Time taken (in nanoseconds) to handle the API request
     **/
    private BigDecimal timerServerHandlerNs;
    /**
      * *Description is forthcoming*
     **/
    private BigDecimal timerServerReadBytesNs;
    /**
      * *Description is forthcoming*
     **/
    private BigDecimal counterServerQueryCacheHit;
    /**
      * (Only returned if `instrument` is true.) *Description is forthcoming*
     **/
    private BigDecimal timerQueryCompileStageBuildComprehensionIndexNs;
    /**
      * (Only returned if `instrument` is true.) *Description is forthcoming*
     **/
    private BigDecimal timerQueryCompileStageCheckSafetyNs;
    /**
      * (Only returned if `instrument` is true.) *Description is forthcoming*
     **/
    private BigDecimal timerQueryCompileStageCheckTypesNs;
    /**
      * (Only returned if `instrument` is true.) *Description is forthcoming*
     **/
    private BigDecimal timerQueryCompileStageCheckUndefinedFuncsNs;
    /**
      * (Only returned if `instrument` is true.) *Description is forthcoming*
     **/
    private BigDecimal timerQueryCompileStageCheckUnsafeBuiltinsNs;
    /**
      * (Only returned if `instrument` is true.) *Description is forthcoming*
     **/
    private BigDecimal timerQueryCompileStageResolveRefsNs;
    /**
      * (Only returned if `instrument` is true.) *Description is forthcoming*
     **/
    private BigDecimal timerQueryCompileStageRewriteComprehensionTermsNs;
    /**
      * (Only returned if `instrument` is true.) *Description is forthcoming*
     **/
    private BigDecimal timerQueryCompileStageRewriteDynamicTermsNs;
    /**
      * (Only returned if `instrument` is true.) *Description is forthcoming*
     **/
    private BigDecimal timerQueryCompileStageRewriteExprTermsNs;
    /**
      * (Only returned if `instrument` is true.) *Description is forthcoming*
     **/
    private BigDecimal timerQueryCompileStageRewriteLocalVarsNs;
    /**
      * (Only returned if `instrument` is true.) *Description is forthcoming*
     **/
    private BigDecimal timerQueryCompileStageRewriteToCaptureValueNs;
    /**
      * (Only returned if `instrument` is true.) *Description is forthcoming*
     **/
    private BigDecimal timerQueryCompileStageRewriteWithValuesNs;

    /**
    * Time taken (in nanonseconds) to parse the input
    * @return timerRegoInputParseNs
    **/
    @JsonProperty("timer_rego_input_parse_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerRegoInputParseNs() {
        return timerRegoInputParseNs;
    }

    /**
     * Set timerRegoInputParseNs
     **/
    @JsonProperty("timer_rego_input_parse_ns")
    public void setTimerRegoInputParseNs(BigDecimal timerRegoInputParseNs) {
        this.timerRegoInputParseNs = timerRegoInputParseNs;
    }

    public Model200MetricsMetrics timerRegoInputParseNs(BigDecimal timerRegoInputParseNs) {
        this.timerRegoInputParseNs = timerRegoInputParseNs;
        return this;
    }

    /**
    * Time taken (in nanoseconds) to parse the query
    * @return timerRegoQueryParseNs
    **/
    @JsonProperty("timer_rego_query_parse_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerRegoQueryParseNs() {
        return timerRegoQueryParseNs;
    }

    /**
     * Set timerRegoQueryParseNs
     **/
    @JsonProperty("timer_rego_query_parse_ns")
    public void setTimerRegoQueryParseNs(BigDecimal timerRegoQueryParseNs) {
        this.timerRegoQueryParseNs = timerRegoQueryParseNs;
    }

    public Model200MetricsMetrics timerRegoQueryParseNs(BigDecimal timerRegoQueryParseNs) {
        this.timerRegoQueryParseNs = timerRegoQueryParseNs;
        return this;
    }

    /**
    * Time taken (in nanoseconds) to compile the query
    * @return timerRegoQueryCompileNs
    **/
    @JsonProperty("timer_rego_query_compile_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerRegoQueryCompileNs() {
        return timerRegoQueryCompileNs;
    }

    /**
     * Set timerRegoQueryCompileNs
     **/
    @JsonProperty("timer_rego_query_compile_ns")
    public void setTimerRegoQueryCompileNs(BigDecimal timerRegoQueryCompileNs) {
        this.timerRegoQueryCompileNs = timerRegoQueryCompileNs;
    }

    public Model200MetricsMetrics timerRegoQueryCompileNs(BigDecimal timerRegoQueryCompileNs) {
        this.timerRegoQueryCompileNs = timerRegoQueryCompileNs;
        return this;
    }

    /**
    * Time taken (in nanonseconds) to evaluate the query
    * @return timerRegoQueryEvalNs
    **/
    @JsonProperty("timer_rego_query_eval_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerRegoQueryEvalNs() {
        return timerRegoQueryEvalNs;
    }

    /**
     * Set timerRegoQueryEvalNs
     **/
    @JsonProperty("timer_rego_query_eval_ns")
    public void setTimerRegoQueryEvalNs(BigDecimal timerRegoQueryEvalNs) {
        this.timerRegoQueryEvalNs = timerRegoQueryEvalNs;
    }

    public Model200MetricsMetrics timerRegoQueryEvalNs(BigDecimal timerRegoQueryEvalNs) {
        this.timerRegoQueryEvalNs = timerRegoQueryEvalNs;
        return this;
    }

    /**
    * Time taken (in nanoseconds) to parse the input policy module
    * @return timerRegoModuleParseNs
    **/
    @JsonProperty("timer_rego_module_parse_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerRegoModuleParseNs() {
        return timerRegoModuleParseNs;
    }

    /**
     * Set timerRegoModuleParseNs
     **/
    @JsonProperty("timer_rego_module_parse_ns")
    public void setTimerRegoModuleParseNs(BigDecimal timerRegoModuleParseNs) {
        this.timerRegoModuleParseNs = timerRegoModuleParseNs;
    }

    public Model200MetricsMetrics timerRegoModuleParseNs(BigDecimal timerRegoModuleParseNs) {
        this.timerRegoModuleParseNs = timerRegoModuleParseNs;
        return this;
    }

    /**
    * Time taken (in nanonseconds) to compile the loaded policy modules
    * @return timerRegoModuleCompileNs
    **/
    @JsonProperty("timer_rego_module_compile_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerRegoModuleCompileNs() {
        return timerRegoModuleCompileNs;
    }

    /**
     * Set timerRegoModuleCompileNs
     **/
    @JsonProperty("timer_rego_module_compile_ns")
    public void setTimerRegoModuleCompileNs(BigDecimal timerRegoModuleCompileNs) {
        this.timerRegoModuleCompileNs = timerRegoModuleCompileNs;
    }

    public Model200MetricsMetrics timerRegoModuleCompileNs(BigDecimal timerRegoModuleCompileNs) {
        this.timerRegoModuleCompileNs = timerRegoModuleCompileNs;
        return this;
    }

    /**
    * Time taken (in nanoseconds) to handle the API request
    * @return timerServerHandlerNs
    **/
    @JsonProperty("timer_server_handler_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerServerHandlerNs() {
        return timerServerHandlerNs;
    }

    /**
     * Set timerServerHandlerNs
     **/
    @JsonProperty("timer_server_handler_ns")
    public void setTimerServerHandlerNs(BigDecimal timerServerHandlerNs) {
        this.timerServerHandlerNs = timerServerHandlerNs;
    }

    public Model200MetricsMetrics timerServerHandlerNs(BigDecimal timerServerHandlerNs) {
        this.timerServerHandlerNs = timerServerHandlerNs;
        return this;
    }

    /**
    * *Description is forthcoming*
    * @return timerServerReadBytesNs
    **/
    @JsonProperty("timer_server_read_bytes_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerServerReadBytesNs() {
        return timerServerReadBytesNs;
    }

    /**
     * Set timerServerReadBytesNs
     **/
    @JsonProperty("timer_server_read_bytes_ns")
    public void setTimerServerReadBytesNs(BigDecimal timerServerReadBytesNs) {
        this.timerServerReadBytesNs = timerServerReadBytesNs;
    }

    public Model200MetricsMetrics timerServerReadBytesNs(BigDecimal timerServerReadBytesNs) {
        this.timerServerReadBytesNs = timerServerReadBytesNs;
        return this;
    }

    /**
    * *Description is forthcoming*
    * @return counterServerQueryCacheHit
    **/
    @JsonProperty("counter_server_query_cache_hit")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getCounterServerQueryCacheHit() {
        return counterServerQueryCacheHit;
    }

    /**
     * Set counterServerQueryCacheHit
     **/
    @JsonProperty("counter_server_query_cache_hit")
    public void setCounterServerQueryCacheHit(BigDecimal counterServerQueryCacheHit) {
        this.counterServerQueryCacheHit = counterServerQueryCacheHit;
    }

    public Model200MetricsMetrics counterServerQueryCacheHit(BigDecimal counterServerQueryCacheHit) {
        this.counterServerQueryCacheHit = counterServerQueryCacheHit;
        return this;
    }

    /**
    * (Only returned if `instrument` is true.) *Description is forthcoming*
    * @return timerQueryCompileStageBuildComprehensionIndexNs
    **/
    @JsonProperty("timer_query_compile_stage_build_comprehension_index_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerQueryCompileStageBuildComprehensionIndexNs() {
        return timerQueryCompileStageBuildComprehensionIndexNs;
    }

    /**
     * Set timerQueryCompileStageBuildComprehensionIndexNs
     **/
    @JsonProperty("timer_query_compile_stage_build_comprehension_index_ns")
    public void setTimerQueryCompileStageBuildComprehensionIndexNs(BigDecimal timerQueryCompileStageBuildComprehensionIndexNs) {
        this.timerQueryCompileStageBuildComprehensionIndexNs = timerQueryCompileStageBuildComprehensionIndexNs;
    }

    public Model200MetricsMetrics timerQueryCompileStageBuildComprehensionIndexNs(BigDecimal timerQueryCompileStageBuildComprehensionIndexNs) {
        this.timerQueryCompileStageBuildComprehensionIndexNs = timerQueryCompileStageBuildComprehensionIndexNs;
        return this;
    }

    /**
    * (Only returned if `instrument` is true.) *Description is forthcoming*
    * @return timerQueryCompileStageCheckSafetyNs
    **/
    @JsonProperty("timer_query_compile_stage_check_safety_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerQueryCompileStageCheckSafetyNs() {
        return timerQueryCompileStageCheckSafetyNs;
    }

    /**
     * Set timerQueryCompileStageCheckSafetyNs
     **/
    @JsonProperty("timer_query_compile_stage_check_safety_ns")
    public void setTimerQueryCompileStageCheckSafetyNs(BigDecimal timerQueryCompileStageCheckSafetyNs) {
        this.timerQueryCompileStageCheckSafetyNs = timerQueryCompileStageCheckSafetyNs;
    }

    public Model200MetricsMetrics timerQueryCompileStageCheckSafetyNs(BigDecimal timerQueryCompileStageCheckSafetyNs) {
        this.timerQueryCompileStageCheckSafetyNs = timerQueryCompileStageCheckSafetyNs;
        return this;
    }

    /**
    * (Only returned if `instrument` is true.) *Description is forthcoming*
    * @return timerQueryCompileStageCheckTypesNs
    **/
    @JsonProperty("timer_query_compile_stage_check_types_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerQueryCompileStageCheckTypesNs() {
        return timerQueryCompileStageCheckTypesNs;
    }

    /**
     * Set timerQueryCompileStageCheckTypesNs
     **/
    @JsonProperty("timer_query_compile_stage_check_types_ns")
    public void setTimerQueryCompileStageCheckTypesNs(BigDecimal timerQueryCompileStageCheckTypesNs) {
        this.timerQueryCompileStageCheckTypesNs = timerQueryCompileStageCheckTypesNs;
    }

    public Model200MetricsMetrics timerQueryCompileStageCheckTypesNs(BigDecimal timerQueryCompileStageCheckTypesNs) {
        this.timerQueryCompileStageCheckTypesNs = timerQueryCompileStageCheckTypesNs;
        return this;
    }

    /**
    * (Only returned if `instrument` is true.) *Description is forthcoming*
    * @return timerQueryCompileStageCheckUndefinedFuncsNs
    **/
    @JsonProperty("timer_query_compile_stage_check_undefined_funcs_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerQueryCompileStageCheckUndefinedFuncsNs() {
        return timerQueryCompileStageCheckUndefinedFuncsNs;
    }

    /**
     * Set timerQueryCompileStageCheckUndefinedFuncsNs
     **/
    @JsonProperty("timer_query_compile_stage_check_undefined_funcs_ns")
    public void setTimerQueryCompileStageCheckUndefinedFuncsNs(BigDecimal timerQueryCompileStageCheckUndefinedFuncsNs) {
        this.timerQueryCompileStageCheckUndefinedFuncsNs = timerQueryCompileStageCheckUndefinedFuncsNs;
    }

    public Model200MetricsMetrics timerQueryCompileStageCheckUndefinedFuncsNs(BigDecimal timerQueryCompileStageCheckUndefinedFuncsNs) {
        this.timerQueryCompileStageCheckUndefinedFuncsNs = timerQueryCompileStageCheckUndefinedFuncsNs;
        return this;
    }

    /**
    * (Only returned if `instrument` is true.) *Description is forthcoming*
    * @return timerQueryCompileStageCheckUnsafeBuiltinsNs
    **/
    @JsonProperty("timer_query_compile_stage_check_unsafe_builtins_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerQueryCompileStageCheckUnsafeBuiltinsNs() {
        return timerQueryCompileStageCheckUnsafeBuiltinsNs;
    }

    /**
     * Set timerQueryCompileStageCheckUnsafeBuiltinsNs
     **/
    @JsonProperty("timer_query_compile_stage_check_unsafe_builtins_ns")
    public void setTimerQueryCompileStageCheckUnsafeBuiltinsNs(BigDecimal timerQueryCompileStageCheckUnsafeBuiltinsNs) {
        this.timerQueryCompileStageCheckUnsafeBuiltinsNs = timerQueryCompileStageCheckUnsafeBuiltinsNs;
    }

    public Model200MetricsMetrics timerQueryCompileStageCheckUnsafeBuiltinsNs(BigDecimal timerQueryCompileStageCheckUnsafeBuiltinsNs) {
        this.timerQueryCompileStageCheckUnsafeBuiltinsNs = timerQueryCompileStageCheckUnsafeBuiltinsNs;
        return this;
    }

    /**
    * (Only returned if `instrument` is true.) *Description is forthcoming*
    * @return timerQueryCompileStageResolveRefsNs
    **/
    @JsonProperty("timer_query_compile_stage_resolve_refs_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerQueryCompileStageResolveRefsNs() {
        return timerQueryCompileStageResolveRefsNs;
    }

    /**
     * Set timerQueryCompileStageResolveRefsNs
     **/
    @JsonProperty("timer_query_compile_stage_resolve_refs_ns")
    public void setTimerQueryCompileStageResolveRefsNs(BigDecimal timerQueryCompileStageResolveRefsNs) {
        this.timerQueryCompileStageResolveRefsNs = timerQueryCompileStageResolveRefsNs;
    }

    public Model200MetricsMetrics timerQueryCompileStageResolveRefsNs(BigDecimal timerQueryCompileStageResolveRefsNs) {
        this.timerQueryCompileStageResolveRefsNs = timerQueryCompileStageResolveRefsNs;
        return this;
    }

    /**
    * (Only returned if `instrument` is true.) *Description is forthcoming*
    * @return timerQueryCompileStageRewriteComprehensionTermsNs
    **/
    @JsonProperty("timer_query_compile_stage_rewrite_comprehension_terms_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerQueryCompileStageRewriteComprehensionTermsNs() {
        return timerQueryCompileStageRewriteComprehensionTermsNs;
    }

    /**
     * Set timerQueryCompileStageRewriteComprehensionTermsNs
     **/
    @JsonProperty("timer_query_compile_stage_rewrite_comprehension_terms_ns")
    public void setTimerQueryCompileStageRewriteComprehensionTermsNs(BigDecimal timerQueryCompileStageRewriteComprehensionTermsNs) {
        this.timerQueryCompileStageRewriteComprehensionTermsNs = timerQueryCompileStageRewriteComprehensionTermsNs;
    }

    public Model200MetricsMetrics timerQueryCompileStageRewriteComprehensionTermsNs(BigDecimal timerQueryCompileStageRewriteComprehensionTermsNs) {
        this.timerQueryCompileStageRewriteComprehensionTermsNs = timerQueryCompileStageRewriteComprehensionTermsNs;
        return this;
    }

    /**
    * (Only returned if `instrument` is true.) *Description is forthcoming*
    * @return timerQueryCompileStageRewriteDynamicTermsNs
    **/
    @JsonProperty("timer_query_compile_stage_rewrite_dynamic_terms_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerQueryCompileStageRewriteDynamicTermsNs() {
        return timerQueryCompileStageRewriteDynamicTermsNs;
    }

    /**
     * Set timerQueryCompileStageRewriteDynamicTermsNs
     **/
    @JsonProperty("timer_query_compile_stage_rewrite_dynamic_terms_ns")
    public void setTimerQueryCompileStageRewriteDynamicTermsNs(BigDecimal timerQueryCompileStageRewriteDynamicTermsNs) {
        this.timerQueryCompileStageRewriteDynamicTermsNs = timerQueryCompileStageRewriteDynamicTermsNs;
    }

    public Model200MetricsMetrics timerQueryCompileStageRewriteDynamicTermsNs(BigDecimal timerQueryCompileStageRewriteDynamicTermsNs) {
        this.timerQueryCompileStageRewriteDynamicTermsNs = timerQueryCompileStageRewriteDynamicTermsNs;
        return this;
    }

    /**
    * (Only returned if `instrument` is true.) *Description is forthcoming*
    * @return timerQueryCompileStageRewriteExprTermsNs
    **/
    @JsonProperty("timer_query_compile_stage_rewrite_expr_terms_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerQueryCompileStageRewriteExprTermsNs() {
        return timerQueryCompileStageRewriteExprTermsNs;
    }

    /**
     * Set timerQueryCompileStageRewriteExprTermsNs
     **/
    @JsonProperty("timer_query_compile_stage_rewrite_expr_terms_ns")
    public void setTimerQueryCompileStageRewriteExprTermsNs(BigDecimal timerQueryCompileStageRewriteExprTermsNs) {
        this.timerQueryCompileStageRewriteExprTermsNs = timerQueryCompileStageRewriteExprTermsNs;
    }

    public Model200MetricsMetrics timerQueryCompileStageRewriteExprTermsNs(BigDecimal timerQueryCompileStageRewriteExprTermsNs) {
        this.timerQueryCompileStageRewriteExprTermsNs = timerQueryCompileStageRewriteExprTermsNs;
        return this;
    }

    /**
    * (Only returned if `instrument` is true.) *Description is forthcoming*
    * @return timerQueryCompileStageRewriteLocalVarsNs
    **/
    @JsonProperty("timer_query_compile_stage_rewrite_local_vars_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerQueryCompileStageRewriteLocalVarsNs() {
        return timerQueryCompileStageRewriteLocalVarsNs;
    }

    /**
     * Set timerQueryCompileStageRewriteLocalVarsNs
     **/
    @JsonProperty("timer_query_compile_stage_rewrite_local_vars_ns")
    public void setTimerQueryCompileStageRewriteLocalVarsNs(BigDecimal timerQueryCompileStageRewriteLocalVarsNs) {
        this.timerQueryCompileStageRewriteLocalVarsNs = timerQueryCompileStageRewriteLocalVarsNs;
    }

    public Model200MetricsMetrics timerQueryCompileStageRewriteLocalVarsNs(BigDecimal timerQueryCompileStageRewriteLocalVarsNs) {
        this.timerQueryCompileStageRewriteLocalVarsNs = timerQueryCompileStageRewriteLocalVarsNs;
        return this;
    }

    /**
    * (Only returned if `instrument` is true.) *Description is forthcoming*
    * @return timerQueryCompileStageRewriteToCaptureValueNs
    **/
    @JsonProperty("timer_query_compile_stage_rewrite_to_capture_value_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerQueryCompileStageRewriteToCaptureValueNs() {
        return timerQueryCompileStageRewriteToCaptureValueNs;
    }

    /**
     * Set timerQueryCompileStageRewriteToCaptureValueNs
     **/
    @JsonProperty("timer_query_compile_stage_rewrite_to_capture_value_ns")
    public void setTimerQueryCompileStageRewriteToCaptureValueNs(BigDecimal timerQueryCompileStageRewriteToCaptureValueNs) {
        this.timerQueryCompileStageRewriteToCaptureValueNs = timerQueryCompileStageRewriteToCaptureValueNs;
    }

    public Model200MetricsMetrics timerQueryCompileStageRewriteToCaptureValueNs(BigDecimal timerQueryCompileStageRewriteToCaptureValueNs) {
        this.timerQueryCompileStageRewriteToCaptureValueNs = timerQueryCompileStageRewriteToCaptureValueNs;
        return this;
    }

    /**
    * (Only returned if `instrument` is true.) *Description is forthcoming*
    * @return timerQueryCompileStageRewriteWithValuesNs
    **/
    @JsonProperty("timer_query_compile_stage_rewrite_with_values_ns")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public BigDecimal getTimerQueryCompileStageRewriteWithValuesNs() {
        return timerQueryCompileStageRewriteWithValuesNs;
    }

    /**
     * Set timerQueryCompileStageRewriteWithValuesNs
     **/
    @JsonProperty("timer_query_compile_stage_rewrite_with_values_ns")
    public void setTimerQueryCompileStageRewriteWithValuesNs(BigDecimal timerQueryCompileStageRewriteWithValuesNs) {
        this.timerQueryCompileStageRewriteWithValuesNs = timerQueryCompileStageRewriteWithValuesNs;
    }

    public Model200MetricsMetrics timerQueryCompileStageRewriteWithValuesNs(BigDecimal timerQueryCompileStageRewriteWithValuesNs) {
        this.timerQueryCompileStageRewriteWithValuesNs = timerQueryCompileStageRewriteWithValuesNs;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Model200MetricsMetrics {\n");

        sb.append("    timerRegoInputParseNs: ").append(toIndentedString(timerRegoInputParseNs)).append("\n");
        sb.append("    timerRegoQueryParseNs: ").append(toIndentedString(timerRegoQueryParseNs)).append("\n");
        sb.append("    timerRegoQueryCompileNs: ").append(toIndentedString(timerRegoQueryCompileNs)).append("\n");
        sb.append("    timerRegoQueryEvalNs: ").append(toIndentedString(timerRegoQueryEvalNs)).append("\n");
        sb.append("    timerRegoModuleParseNs: ").append(toIndentedString(timerRegoModuleParseNs)).append("\n");
        sb.append("    timerRegoModuleCompileNs: ").append(toIndentedString(timerRegoModuleCompileNs)).append("\n");
        sb.append("    timerServerHandlerNs: ").append(toIndentedString(timerServerHandlerNs)).append("\n");
        sb.append("    timerServerReadBytesNs: ").append(toIndentedString(timerServerReadBytesNs)).append("\n");
        sb.append("    counterServerQueryCacheHit: ").append(toIndentedString(counterServerQueryCacheHit)).append("\n");
        sb.append("    timerQueryCompileStageBuildComprehensionIndexNs: ").append(toIndentedString(timerQueryCompileStageBuildComprehensionIndexNs)).append("\n");
        sb.append("    timerQueryCompileStageCheckSafetyNs: ").append(toIndentedString(timerQueryCompileStageCheckSafetyNs)).append("\n");
        sb.append("    timerQueryCompileStageCheckTypesNs: ").append(toIndentedString(timerQueryCompileStageCheckTypesNs)).append("\n");
        sb.append("    timerQueryCompileStageCheckUndefinedFuncsNs: ").append(toIndentedString(timerQueryCompileStageCheckUndefinedFuncsNs)).append("\n");
        sb.append("    timerQueryCompileStageCheckUnsafeBuiltinsNs: ").append(toIndentedString(timerQueryCompileStageCheckUnsafeBuiltinsNs)).append("\n");
        sb.append("    timerQueryCompileStageResolveRefsNs: ").append(toIndentedString(timerQueryCompileStageResolveRefsNs)).append("\n");
        sb.append("    timerQueryCompileStageRewriteComprehensionTermsNs: ").append(toIndentedString(timerQueryCompileStageRewriteComprehensionTermsNs)).append("\n");
        sb.append("    timerQueryCompileStageRewriteDynamicTermsNs: ").append(toIndentedString(timerQueryCompileStageRewriteDynamicTermsNs)).append("\n");
        sb.append("    timerQueryCompileStageRewriteExprTermsNs: ").append(toIndentedString(timerQueryCompileStageRewriteExprTermsNs)).append("\n");
        sb.append("    timerQueryCompileStageRewriteLocalVarsNs: ").append(toIndentedString(timerQueryCompileStageRewriteLocalVarsNs)).append("\n");
        sb.append("    timerQueryCompileStageRewriteToCaptureValueNs: ").append(toIndentedString(timerQueryCompileStageRewriteToCaptureValueNs)).append("\n");
        sb.append("    timerQueryCompileStageRewriteWithValuesNs: ").append(toIndentedString(timerQueryCompileStageRewriteWithValuesNs)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Model200MetricsMetrics} object that
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

        Model200MetricsMetrics model = (Model200MetricsMetrics) obj;

        return java.util.Objects.equals(timerRegoInputParseNs, model.timerRegoInputParseNs) &&
        java.util.Objects.equals(timerRegoQueryParseNs, model.timerRegoQueryParseNs) &&
        java.util.Objects.equals(timerRegoQueryCompileNs, model.timerRegoQueryCompileNs) &&
        java.util.Objects.equals(timerRegoQueryEvalNs, model.timerRegoQueryEvalNs) &&
        java.util.Objects.equals(timerRegoModuleParseNs, model.timerRegoModuleParseNs) &&
        java.util.Objects.equals(timerRegoModuleCompileNs, model.timerRegoModuleCompileNs) &&
        java.util.Objects.equals(timerServerHandlerNs, model.timerServerHandlerNs) &&
        java.util.Objects.equals(timerServerReadBytesNs, model.timerServerReadBytesNs) &&
        java.util.Objects.equals(counterServerQueryCacheHit, model.counterServerQueryCacheHit) &&
        java.util.Objects.equals(timerQueryCompileStageBuildComprehensionIndexNs, model.timerQueryCompileStageBuildComprehensionIndexNs) &&
        java.util.Objects.equals(timerQueryCompileStageCheckSafetyNs, model.timerQueryCompileStageCheckSafetyNs) &&
        java.util.Objects.equals(timerQueryCompileStageCheckTypesNs, model.timerQueryCompileStageCheckTypesNs) &&
        java.util.Objects.equals(timerQueryCompileStageCheckUndefinedFuncsNs, model.timerQueryCompileStageCheckUndefinedFuncsNs) &&
        java.util.Objects.equals(timerQueryCompileStageCheckUnsafeBuiltinsNs, model.timerQueryCompileStageCheckUnsafeBuiltinsNs) &&
        java.util.Objects.equals(timerQueryCompileStageResolveRefsNs, model.timerQueryCompileStageResolveRefsNs) &&
        java.util.Objects.equals(timerQueryCompileStageRewriteComprehensionTermsNs, model.timerQueryCompileStageRewriteComprehensionTermsNs) &&
        java.util.Objects.equals(timerQueryCompileStageRewriteDynamicTermsNs, model.timerQueryCompileStageRewriteDynamicTermsNs) &&
        java.util.Objects.equals(timerQueryCompileStageRewriteExprTermsNs, model.timerQueryCompileStageRewriteExprTermsNs) &&
        java.util.Objects.equals(timerQueryCompileStageRewriteLocalVarsNs, model.timerQueryCompileStageRewriteLocalVarsNs) &&
        java.util.Objects.equals(timerQueryCompileStageRewriteToCaptureValueNs, model.timerQueryCompileStageRewriteToCaptureValueNs) &&
        java.util.Objects.equals(timerQueryCompileStageRewriteWithValuesNs, model.timerQueryCompileStageRewriteWithValuesNs);
    }

    /**
     * Returns a hash code for a {@code Model200MetricsMetrics}.
     *
     * @return a hash code value for a {@code Model200MetricsMetrics}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(timerRegoInputParseNs,
        timerRegoQueryParseNs,
        timerRegoQueryCompileNs,
        timerRegoQueryEvalNs,
        timerRegoModuleParseNs,
        timerRegoModuleCompileNs,
        timerServerHandlerNs,
        timerServerReadBytesNs,
        counterServerQueryCacheHit,
        timerQueryCompileStageBuildComprehensionIndexNs,
        timerQueryCompileStageCheckSafetyNs,
        timerQueryCompileStageCheckTypesNs,
        timerQueryCompileStageCheckUndefinedFuncsNs,
        timerQueryCompileStageCheckUnsafeBuiltinsNs,
        timerQueryCompileStageResolveRefsNs,
        timerQueryCompileStageRewriteComprehensionTermsNs,
        timerQueryCompileStageRewriteDynamicTermsNs,
        timerQueryCompileStageRewriteExprTermsNs,
        timerQueryCompileStageRewriteLocalVarsNs,
        timerQueryCompileStageRewriteToCaptureValueNs,
        timerQueryCompileStageRewriteWithValuesNs);
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
    public static class Model200MetricsMetricsQueryParam  {

        @jakarta.ws.rs.QueryParam("timerRegoInputParseNs")
        private BigDecimal timerRegoInputParseNs;
        @jakarta.ws.rs.QueryParam("timerRegoQueryParseNs")
        private BigDecimal timerRegoQueryParseNs;
        @jakarta.ws.rs.QueryParam("timerRegoQueryCompileNs")
        private BigDecimal timerRegoQueryCompileNs;
        @jakarta.ws.rs.QueryParam("timerRegoQueryEvalNs")
        private BigDecimal timerRegoQueryEvalNs;
        @jakarta.ws.rs.QueryParam("timerRegoModuleParseNs")
        private BigDecimal timerRegoModuleParseNs;
        @jakarta.ws.rs.QueryParam("timerRegoModuleCompileNs")
        private BigDecimal timerRegoModuleCompileNs;
        @jakarta.ws.rs.QueryParam("timerServerHandlerNs")
        private BigDecimal timerServerHandlerNs;
        @jakarta.ws.rs.QueryParam("timerServerReadBytesNs")
        private BigDecimal timerServerReadBytesNs;
        @jakarta.ws.rs.QueryParam("counterServerQueryCacheHit")
        private BigDecimal counterServerQueryCacheHit;
        @jakarta.ws.rs.QueryParam("timerQueryCompileStageBuildComprehensionIndexNs")
        private BigDecimal timerQueryCompileStageBuildComprehensionIndexNs;
        @jakarta.ws.rs.QueryParam("timerQueryCompileStageCheckSafetyNs")
        private BigDecimal timerQueryCompileStageCheckSafetyNs;
        @jakarta.ws.rs.QueryParam("timerQueryCompileStageCheckTypesNs")
        private BigDecimal timerQueryCompileStageCheckTypesNs;
        @jakarta.ws.rs.QueryParam("timerQueryCompileStageCheckUndefinedFuncsNs")
        private BigDecimal timerQueryCompileStageCheckUndefinedFuncsNs;
        @jakarta.ws.rs.QueryParam("timerQueryCompileStageCheckUnsafeBuiltinsNs")
        private BigDecimal timerQueryCompileStageCheckUnsafeBuiltinsNs;
        @jakarta.ws.rs.QueryParam("timerQueryCompileStageResolveRefsNs")
        private BigDecimal timerQueryCompileStageResolveRefsNs;
        @jakarta.ws.rs.QueryParam("timerQueryCompileStageRewriteComprehensionTermsNs")
        private BigDecimal timerQueryCompileStageRewriteComprehensionTermsNs;
        @jakarta.ws.rs.QueryParam("timerQueryCompileStageRewriteDynamicTermsNs")
        private BigDecimal timerQueryCompileStageRewriteDynamicTermsNs;
        @jakarta.ws.rs.QueryParam("timerQueryCompileStageRewriteExprTermsNs")
        private BigDecimal timerQueryCompileStageRewriteExprTermsNs;
        @jakarta.ws.rs.QueryParam("timerQueryCompileStageRewriteLocalVarsNs")
        private BigDecimal timerQueryCompileStageRewriteLocalVarsNs;
        @jakarta.ws.rs.QueryParam("timerQueryCompileStageRewriteToCaptureValueNs")
        private BigDecimal timerQueryCompileStageRewriteToCaptureValueNs;
        @jakarta.ws.rs.QueryParam("timerQueryCompileStageRewriteWithValuesNs")
        private BigDecimal timerQueryCompileStageRewriteWithValuesNs;

        /**
        * Time taken (in nanonseconds) to parse the input
        * @return timerRegoInputParseNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_rego_input_parse_ns")
        public BigDecimal getTimerRegoInputParseNs() {
            return timerRegoInputParseNs;
        }

        /**
         * Set timerRegoInputParseNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_rego_input_parse_ns")
        public void setTimerRegoInputParseNs(BigDecimal timerRegoInputParseNs) {
            this.timerRegoInputParseNs = timerRegoInputParseNs;
        }

        public Model200MetricsMetricsQueryParam timerRegoInputParseNs(BigDecimal timerRegoInputParseNs) {
            this.timerRegoInputParseNs = timerRegoInputParseNs;
            return this;
        }

        /**
        * Time taken (in nanoseconds) to parse the query
        * @return timerRegoQueryParseNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_rego_query_parse_ns")
        public BigDecimal getTimerRegoQueryParseNs() {
            return timerRegoQueryParseNs;
        }

        /**
         * Set timerRegoQueryParseNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_rego_query_parse_ns")
        public void setTimerRegoQueryParseNs(BigDecimal timerRegoQueryParseNs) {
            this.timerRegoQueryParseNs = timerRegoQueryParseNs;
        }

        public Model200MetricsMetricsQueryParam timerRegoQueryParseNs(BigDecimal timerRegoQueryParseNs) {
            this.timerRegoQueryParseNs = timerRegoQueryParseNs;
            return this;
        }

        /**
        * Time taken (in nanoseconds) to compile the query
        * @return timerRegoQueryCompileNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_rego_query_compile_ns")
        public BigDecimal getTimerRegoQueryCompileNs() {
            return timerRegoQueryCompileNs;
        }

        /**
         * Set timerRegoQueryCompileNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_rego_query_compile_ns")
        public void setTimerRegoQueryCompileNs(BigDecimal timerRegoQueryCompileNs) {
            this.timerRegoQueryCompileNs = timerRegoQueryCompileNs;
        }

        public Model200MetricsMetricsQueryParam timerRegoQueryCompileNs(BigDecimal timerRegoQueryCompileNs) {
            this.timerRegoQueryCompileNs = timerRegoQueryCompileNs;
            return this;
        }

        /**
        * Time taken (in nanonseconds) to evaluate the query
        * @return timerRegoQueryEvalNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_rego_query_eval_ns")
        public BigDecimal getTimerRegoQueryEvalNs() {
            return timerRegoQueryEvalNs;
        }

        /**
         * Set timerRegoQueryEvalNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_rego_query_eval_ns")
        public void setTimerRegoQueryEvalNs(BigDecimal timerRegoQueryEvalNs) {
            this.timerRegoQueryEvalNs = timerRegoQueryEvalNs;
        }

        public Model200MetricsMetricsQueryParam timerRegoQueryEvalNs(BigDecimal timerRegoQueryEvalNs) {
            this.timerRegoQueryEvalNs = timerRegoQueryEvalNs;
            return this;
        }

        /**
        * Time taken (in nanoseconds) to parse the input policy module
        * @return timerRegoModuleParseNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_rego_module_parse_ns")
        public BigDecimal getTimerRegoModuleParseNs() {
            return timerRegoModuleParseNs;
        }

        /**
         * Set timerRegoModuleParseNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_rego_module_parse_ns")
        public void setTimerRegoModuleParseNs(BigDecimal timerRegoModuleParseNs) {
            this.timerRegoModuleParseNs = timerRegoModuleParseNs;
        }

        public Model200MetricsMetricsQueryParam timerRegoModuleParseNs(BigDecimal timerRegoModuleParseNs) {
            this.timerRegoModuleParseNs = timerRegoModuleParseNs;
            return this;
        }

        /**
        * Time taken (in nanonseconds) to compile the loaded policy modules
        * @return timerRegoModuleCompileNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_rego_module_compile_ns")
        public BigDecimal getTimerRegoModuleCompileNs() {
            return timerRegoModuleCompileNs;
        }

        /**
         * Set timerRegoModuleCompileNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_rego_module_compile_ns")
        public void setTimerRegoModuleCompileNs(BigDecimal timerRegoModuleCompileNs) {
            this.timerRegoModuleCompileNs = timerRegoModuleCompileNs;
        }

        public Model200MetricsMetricsQueryParam timerRegoModuleCompileNs(BigDecimal timerRegoModuleCompileNs) {
            this.timerRegoModuleCompileNs = timerRegoModuleCompileNs;
            return this;
        }

        /**
        * Time taken (in nanoseconds) to handle the API request
        * @return timerServerHandlerNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_server_handler_ns")
        public BigDecimal getTimerServerHandlerNs() {
            return timerServerHandlerNs;
        }

        /**
         * Set timerServerHandlerNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_server_handler_ns")
        public void setTimerServerHandlerNs(BigDecimal timerServerHandlerNs) {
            this.timerServerHandlerNs = timerServerHandlerNs;
        }

        public Model200MetricsMetricsQueryParam timerServerHandlerNs(BigDecimal timerServerHandlerNs) {
            this.timerServerHandlerNs = timerServerHandlerNs;
            return this;
        }

        /**
        * *Description is forthcoming*
        * @return timerServerReadBytesNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_server_read_bytes_ns")
        public BigDecimal getTimerServerReadBytesNs() {
            return timerServerReadBytesNs;
        }

        /**
         * Set timerServerReadBytesNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_server_read_bytes_ns")
        public void setTimerServerReadBytesNs(BigDecimal timerServerReadBytesNs) {
            this.timerServerReadBytesNs = timerServerReadBytesNs;
        }

        public Model200MetricsMetricsQueryParam timerServerReadBytesNs(BigDecimal timerServerReadBytesNs) {
            this.timerServerReadBytesNs = timerServerReadBytesNs;
            return this;
        }

        /**
        * *Description is forthcoming*
        * @return counterServerQueryCacheHit
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("counter_server_query_cache_hit")
        public BigDecimal getCounterServerQueryCacheHit() {
            return counterServerQueryCacheHit;
        }

        /**
         * Set counterServerQueryCacheHit
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("counter_server_query_cache_hit")
        public void setCounterServerQueryCacheHit(BigDecimal counterServerQueryCacheHit) {
            this.counterServerQueryCacheHit = counterServerQueryCacheHit;
        }

        public Model200MetricsMetricsQueryParam counterServerQueryCacheHit(BigDecimal counterServerQueryCacheHit) {
            this.counterServerQueryCacheHit = counterServerQueryCacheHit;
            return this;
        }

        /**
        * (Only returned if `instrument` is true.) *Description is forthcoming*
        * @return timerQueryCompileStageBuildComprehensionIndexNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_build_comprehension_index_ns")
        public BigDecimal getTimerQueryCompileStageBuildComprehensionIndexNs() {
            return timerQueryCompileStageBuildComprehensionIndexNs;
        }

        /**
         * Set timerQueryCompileStageBuildComprehensionIndexNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_build_comprehension_index_ns")
        public void setTimerQueryCompileStageBuildComprehensionIndexNs(BigDecimal timerQueryCompileStageBuildComprehensionIndexNs) {
            this.timerQueryCompileStageBuildComprehensionIndexNs = timerQueryCompileStageBuildComprehensionIndexNs;
        }

        public Model200MetricsMetricsQueryParam timerQueryCompileStageBuildComprehensionIndexNs(BigDecimal timerQueryCompileStageBuildComprehensionIndexNs) {
            this.timerQueryCompileStageBuildComprehensionIndexNs = timerQueryCompileStageBuildComprehensionIndexNs;
            return this;
        }

        /**
        * (Only returned if `instrument` is true.) *Description is forthcoming*
        * @return timerQueryCompileStageCheckSafetyNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_check_safety_ns")
        public BigDecimal getTimerQueryCompileStageCheckSafetyNs() {
            return timerQueryCompileStageCheckSafetyNs;
        }

        /**
         * Set timerQueryCompileStageCheckSafetyNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_check_safety_ns")
        public void setTimerQueryCompileStageCheckSafetyNs(BigDecimal timerQueryCompileStageCheckSafetyNs) {
            this.timerQueryCompileStageCheckSafetyNs = timerQueryCompileStageCheckSafetyNs;
        }

        public Model200MetricsMetricsQueryParam timerQueryCompileStageCheckSafetyNs(BigDecimal timerQueryCompileStageCheckSafetyNs) {
            this.timerQueryCompileStageCheckSafetyNs = timerQueryCompileStageCheckSafetyNs;
            return this;
        }

        /**
        * (Only returned if `instrument` is true.) *Description is forthcoming*
        * @return timerQueryCompileStageCheckTypesNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_check_types_ns")
        public BigDecimal getTimerQueryCompileStageCheckTypesNs() {
            return timerQueryCompileStageCheckTypesNs;
        }

        /**
         * Set timerQueryCompileStageCheckTypesNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_check_types_ns")
        public void setTimerQueryCompileStageCheckTypesNs(BigDecimal timerQueryCompileStageCheckTypesNs) {
            this.timerQueryCompileStageCheckTypesNs = timerQueryCompileStageCheckTypesNs;
        }

        public Model200MetricsMetricsQueryParam timerQueryCompileStageCheckTypesNs(BigDecimal timerQueryCompileStageCheckTypesNs) {
            this.timerQueryCompileStageCheckTypesNs = timerQueryCompileStageCheckTypesNs;
            return this;
        }

        /**
        * (Only returned if `instrument` is true.) *Description is forthcoming*
        * @return timerQueryCompileStageCheckUndefinedFuncsNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_check_undefined_funcs_ns")
        public BigDecimal getTimerQueryCompileStageCheckUndefinedFuncsNs() {
            return timerQueryCompileStageCheckUndefinedFuncsNs;
        }

        /**
         * Set timerQueryCompileStageCheckUndefinedFuncsNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_check_undefined_funcs_ns")
        public void setTimerQueryCompileStageCheckUndefinedFuncsNs(BigDecimal timerQueryCompileStageCheckUndefinedFuncsNs) {
            this.timerQueryCompileStageCheckUndefinedFuncsNs = timerQueryCompileStageCheckUndefinedFuncsNs;
        }

        public Model200MetricsMetricsQueryParam timerQueryCompileStageCheckUndefinedFuncsNs(BigDecimal timerQueryCompileStageCheckUndefinedFuncsNs) {
            this.timerQueryCompileStageCheckUndefinedFuncsNs = timerQueryCompileStageCheckUndefinedFuncsNs;
            return this;
        }

        /**
        * (Only returned if `instrument` is true.) *Description is forthcoming*
        * @return timerQueryCompileStageCheckUnsafeBuiltinsNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_check_unsafe_builtins_ns")
        public BigDecimal getTimerQueryCompileStageCheckUnsafeBuiltinsNs() {
            return timerQueryCompileStageCheckUnsafeBuiltinsNs;
        }

        /**
         * Set timerQueryCompileStageCheckUnsafeBuiltinsNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_check_unsafe_builtins_ns")
        public void setTimerQueryCompileStageCheckUnsafeBuiltinsNs(BigDecimal timerQueryCompileStageCheckUnsafeBuiltinsNs) {
            this.timerQueryCompileStageCheckUnsafeBuiltinsNs = timerQueryCompileStageCheckUnsafeBuiltinsNs;
        }

        public Model200MetricsMetricsQueryParam timerQueryCompileStageCheckUnsafeBuiltinsNs(BigDecimal timerQueryCompileStageCheckUnsafeBuiltinsNs) {
            this.timerQueryCompileStageCheckUnsafeBuiltinsNs = timerQueryCompileStageCheckUnsafeBuiltinsNs;
            return this;
        }

        /**
        * (Only returned if `instrument` is true.) *Description is forthcoming*
        * @return timerQueryCompileStageResolveRefsNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_resolve_refs_ns")
        public BigDecimal getTimerQueryCompileStageResolveRefsNs() {
            return timerQueryCompileStageResolveRefsNs;
        }

        /**
         * Set timerQueryCompileStageResolveRefsNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_resolve_refs_ns")
        public void setTimerQueryCompileStageResolveRefsNs(BigDecimal timerQueryCompileStageResolveRefsNs) {
            this.timerQueryCompileStageResolveRefsNs = timerQueryCompileStageResolveRefsNs;
        }

        public Model200MetricsMetricsQueryParam timerQueryCompileStageResolveRefsNs(BigDecimal timerQueryCompileStageResolveRefsNs) {
            this.timerQueryCompileStageResolveRefsNs = timerQueryCompileStageResolveRefsNs;
            return this;
        }

        /**
        * (Only returned if `instrument` is true.) *Description is forthcoming*
        * @return timerQueryCompileStageRewriteComprehensionTermsNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_rewrite_comprehension_terms_ns")
        public BigDecimal getTimerQueryCompileStageRewriteComprehensionTermsNs() {
            return timerQueryCompileStageRewriteComprehensionTermsNs;
        }

        /**
         * Set timerQueryCompileStageRewriteComprehensionTermsNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_rewrite_comprehension_terms_ns")
        public void setTimerQueryCompileStageRewriteComprehensionTermsNs(BigDecimal timerQueryCompileStageRewriteComprehensionTermsNs) {
            this.timerQueryCompileStageRewriteComprehensionTermsNs = timerQueryCompileStageRewriteComprehensionTermsNs;
        }

        public Model200MetricsMetricsQueryParam timerQueryCompileStageRewriteComprehensionTermsNs(BigDecimal timerQueryCompileStageRewriteComprehensionTermsNs) {
            this.timerQueryCompileStageRewriteComprehensionTermsNs = timerQueryCompileStageRewriteComprehensionTermsNs;
            return this;
        }

        /**
        * (Only returned if `instrument` is true.) *Description is forthcoming*
        * @return timerQueryCompileStageRewriteDynamicTermsNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_rewrite_dynamic_terms_ns")
        public BigDecimal getTimerQueryCompileStageRewriteDynamicTermsNs() {
            return timerQueryCompileStageRewriteDynamicTermsNs;
        }

        /**
         * Set timerQueryCompileStageRewriteDynamicTermsNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_rewrite_dynamic_terms_ns")
        public void setTimerQueryCompileStageRewriteDynamicTermsNs(BigDecimal timerQueryCompileStageRewriteDynamicTermsNs) {
            this.timerQueryCompileStageRewriteDynamicTermsNs = timerQueryCompileStageRewriteDynamicTermsNs;
        }

        public Model200MetricsMetricsQueryParam timerQueryCompileStageRewriteDynamicTermsNs(BigDecimal timerQueryCompileStageRewriteDynamicTermsNs) {
            this.timerQueryCompileStageRewriteDynamicTermsNs = timerQueryCompileStageRewriteDynamicTermsNs;
            return this;
        }

        /**
        * (Only returned if `instrument` is true.) *Description is forthcoming*
        * @return timerQueryCompileStageRewriteExprTermsNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_rewrite_expr_terms_ns")
        public BigDecimal getTimerQueryCompileStageRewriteExprTermsNs() {
            return timerQueryCompileStageRewriteExprTermsNs;
        }

        /**
         * Set timerQueryCompileStageRewriteExprTermsNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_rewrite_expr_terms_ns")
        public void setTimerQueryCompileStageRewriteExprTermsNs(BigDecimal timerQueryCompileStageRewriteExprTermsNs) {
            this.timerQueryCompileStageRewriteExprTermsNs = timerQueryCompileStageRewriteExprTermsNs;
        }

        public Model200MetricsMetricsQueryParam timerQueryCompileStageRewriteExprTermsNs(BigDecimal timerQueryCompileStageRewriteExprTermsNs) {
            this.timerQueryCompileStageRewriteExprTermsNs = timerQueryCompileStageRewriteExprTermsNs;
            return this;
        }

        /**
        * (Only returned if `instrument` is true.) *Description is forthcoming*
        * @return timerQueryCompileStageRewriteLocalVarsNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_rewrite_local_vars_ns")
        public BigDecimal getTimerQueryCompileStageRewriteLocalVarsNs() {
            return timerQueryCompileStageRewriteLocalVarsNs;
        }

        /**
         * Set timerQueryCompileStageRewriteLocalVarsNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_rewrite_local_vars_ns")
        public void setTimerQueryCompileStageRewriteLocalVarsNs(BigDecimal timerQueryCompileStageRewriteLocalVarsNs) {
            this.timerQueryCompileStageRewriteLocalVarsNs = timerQueryCompileStageRewriteLocalVarsNs;
        }

        public Model200MetricsMetricsQueryParam timerQueryCompileStageRewriteLocalVarsNs(BigDecimal timerQueryCompileStageRewriteLocalVarsNs) {
            this.timerQueryCompileStageRewriteLocalVarsNs = timerQueryCompileStageRewriteLocalVarsNs;
            return this;
        }

        /**
        * (Only returned if `instrument` is true.) *Description is forthcoming*
        * @return timerQueryCompileStageRewriteToCaptureValueNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_rewrite_to_capture_value_ns")
        public BigDecimal getTimerQueryCompileStageRewriteToCaptureValueNs() {
            return timerQueryCompileStageRewriteToCaptureValueNs;
        }

        /**
         * Set timerQueryCompileStageRewriteToCaptureValueNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_rewrite_to_capture_value_ns")
        public void setTimerQueryCompileStageRewriteToCaptureValueNs(BigDecimal timerQueryCompileStageRewriteToCaptureValueNs) {
            this.timerQueryCompileStageRewriteToCaptureValueNs = timerQueryCompileStageRewriteToCaptureValueNs;
        }

        public Model200MetricsMetricsQueryParam timerQueryCompileStageRewriteToCaptureValueNs(BigDecimal timerQueryCompileStageRewriteToCaptureValueNs) {
            this.timerQueryCompileStageRewriteToCaptureValueNs = timerQueryCompileStageRewriteToCaptureValueNs;
            return this;
        }

        /**
        * (Only returned if `instrument` is true.) *Description is forthcoming*
        * @return timerQueryCompileStageRewriteWithValuesNs
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_rewrite_with_values_ns")
        public BigDecimal getTimerQueryCompileStageRewriteWithValuesNs() {
            return timerQueryCompileStageRewriteWithValuesNs;
        }

        /**
         * Set timerQueryCompileStageRewriteWithValuesNs
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("timer_query_compile_stage_rewrite_with_values_ns")
        public void setTimerQueryCompileStageRewriteWithValuesNs(BigDecimal timerQueryCompileStageRewriteWithValuesNs) {
            this.timerQueryCompileStageRewriteWithValuesNs = timerQueryCompileStageRewriteWithValuesNs;
        }

        public Model200MetricsMetricsQueryParam timerQueryCompileStageRewriteWithValuesNs(BigDecimal timerQueryCompileStageRewriteWithValuesNs) {
            this.timerQueryCompileStageRewriteWithValuesNs = timerQueryCompileStageRewriteWithValuesNs;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Model200MetricsMetricsQueryParam {\n");

            sb.append("    timerRegoInputParseNs: ").append(toIndentedString(timerRegoInputParseNs)).append("\n");
            sb.append("    timerRegoQueryParseNs: ").append(toIndentedString(timerRegoQueryParseNs)).append("\n");
            sb.append("    timerRegoQueryCompileNs: ").append(toIndentedString(timerRegoQueryCompileNs)).append("\n");
            sb.append("    timerRegoQueryEvalNs: ").append(toIndentedString(timerRegoQueryEvalNs)).append("\n");
            sb.append("    timerRegoModuleParseNs: ").append(toIndentedString(timerRegoModuleParseNs)).append("\n");
            sb.append("    timerRegoModuleCompileNs: ").append(toIndentedString(timerRegoModuleCompileNs)).append("\n");
            sb.append("    timerServerHandlerNs: ").append(toIndentedString(timerServerHandlerNs)).append("\n");
            sb.append("    timerServerReadBytesNs: ").append(toIndentedString(timerServerReadBytesNs)).append("\n");
            sb.append("    counterServerQueryCacheHit: ").append(toIndentedString(counterServerQueryCacheHit)).append("\n");
            sb.append("    timerQueryCompileStageBuildComprehensionIndexNs: ").append(toIndentedString(timerQueryCompileStageBuildComprehensionIndexNs)).append("\n");
            sb.append("    timerQueryCompileStageCheckSafetyNs: ").append(toIndentedString(timerQueryCompileStageCheckSafetyNs)).append("\n");
            sb.append("    timerQueryCompileStageCheckTypesNs: ").append(toIndentedString(timerQueryCompileStageCheckTypesNs)).append("\n");
            sb.append("    timerQueryCompileStageCheckUndefinedFuncsNs: ").append(toIndentedString(timerQueryCompileStageCheckUndefinedFuncsNs)).append("\n");
            sb.append("    timerQueryCompileStageCheckUnsafeBuiltinsNs: ").append(toIndentedString(timerQueryCompileStageCheckUnsafeBuiltinsNs)).append("\n");
            sb.append("    timerQueryCompileStageResolveRefsNs: ").append(toIndentedString(timerQueryCompileStageResolveRefsNs)).append("\n");
            sb.append("    timerQueryCompileStageRewriteComprehensionTermsNs: ").append(toIndentedString(timerQueryCompileStageRewriteComprehensionTermsNs)).append("\n");
            sb.append("    timerQueryCompileStageRewriteDynamicTermsNs: ").append(toIndentedString(timerQueryCompileStageRewriteDynamicTermsNs)).append("\n");
            sb.append("    timerQueryCompileStageRewriteExprTermsNs: ").append(toIndentedString(timerQueryCompileStageRewriteExprTermsNs)).append("\n");
            sb.append("    timerQueryCompileStageRewriteLocalVarsNs: ").append(toIndentedString(timerQueryCompileStageRewriteLocalVarsNs)).append("\n");
            sb.append("    timerQueryCompileStageRewriteToCaptureValueNs: ").append(toIndentedString(timerQueryCompileStageRewriteToCaptureValueNs)).append("\n");
            sb.append("    timerQueryCompileStageRewriteWithValuesNs: ").append(toIndentedString(timerQueryCompileStageRewriteWithValuesNs)).append("\n");
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
