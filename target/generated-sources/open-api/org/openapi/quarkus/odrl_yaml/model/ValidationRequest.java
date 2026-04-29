package org.openapi.quarkus.odrl_yaml.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import org.openapi.quarkus.odrl_yaml.model.TestRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationRequest  {

    private Map<String, Object> policy;
    private TestRequest testRequest;

    /**
    * Get policy
    * @return policy
    **/
    @JsonProperty("policy")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public Map<String, Object> getPolicy() {
        return policy;
    }

    /**
     * Set policy
     **/
    @JsonProperty("policy")
    public void setPolicy(Map<String, Object> policy) {
        this.policy = policy;
    }

    public ValidationRequest policy(Map<String, Object> policy) {
        this.policy = policy;
        return this;
    }
    public ValidationRequest putPolicyItem(String mapKey, Object policyItem) {
           if (this.policy == null){
                policy = new HashMap<>();
            }
        this.policy.put(mapKey, policyItem);
        return this;
    }

    /**
    * Get testRequest
    * @return testRequest
    **/
    @JsonProperty("testRequest")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public TestRequest getTestRequest() {
        return testRequest;
    }

    /**
     * Set testRequest
     **/
    @JsonProperty("testRequest")
    public void setTestRequest(TestRequest testRequest) {
        this.testRequest = testRequest;
    }

    public ValidationRequest testRequest(TestRequest testRequest) {
        this.testRequest = testRequest;
        return this;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ValidationRequest {\n");

        sb.append("    policy: ").append(toIndentedString(policy)).append("\n");
        sb.append("    testRequest: ").append(toIndentedString(testRequest)).append("\n");
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Compares this object to the specified object. The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code ValidationRequest} object that
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

        ValidationRequest model = (ValidationRequest) obj;

        return java.util.Objects.equals(policy, model.policy) &&
        java.util.Objects.equals(testRequest, model.testRequest);
    }

    /**
     * Returns a hash code for a {@code ValidationRequest}.
     *
     * @return a hash code value for a {@code ValidationRequest}.
     **/
    @Override
    public int hashCode() {
        return java.util.Objects.hash(policy,
        testRequest);
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
    public static class ValidationRequestQueryParam  {

        @jakarta.ws.rs.QueryParam("policy")
        private Map<String, Object> policy = null;
        @jakarta.ws.rs.QueryParam("testRequest")
        private TestRequest testRequest;

        /**
        * Get policy
        * @return policy
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("policy")
        public Map<String, Object> getPolicy() {
            return policy;
        }

        /**
         * Set policy
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("policy")
        public void setPolicy(Map<String, Object> policy) {
            this.policy = policy;
        }

        public ValidationRequestQueryParam policy(Map<String, Object> policy) {
            this.policy = policy;
            return this;
        }
        public ValidationRequestQueryParam putPolicyItem(String mapKey, Object policyItem) {
            this.policy.put(mapKey, policyItem);
            return this;
        }

        /**
        * Get testRequest
        * @return testRequest
        **/
        @com.fasterxml.jackson.annotation.JsonProperty("testRequest")
        public TestRequest getTestRequest() {
            return testRequest;
        }

        /**
         * Set testRequest
         **/
        @com.fasterxml.jackson.annotation.JsonProperty("testRequest")
        public void setTestRequest(TestRequest testRequest) {
            this.testRequest = testRequest;
        }

        public ValidationRequestQueryParam testRequest(TestRequest testRequest) {
            this.testRequest = testRequest;
            return this;
        }

        /**
         * Create a string representation of this pojo.
         **/
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class ValidationRequestQueryParam {\n");

            sb.append("    policy: ").append(toIndentedString(policy)).append("\n");
            sb.append("    testRequest: ").append(toIndentedString(testRequest)).append("\n");
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
