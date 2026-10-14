/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
/**
 * A concrete mapping.
 */
export type Mapping = {
    name?: string;
    description?: string;
    /**
     * Number of parameters the mapping requires from the policy. Mappings with a parameterCount > 0
     * - e.g. http:bodyValue, which needs the path of the body-element to read - have to be provided
     * with their parameter as the value of the namespaced key itself, for example
     * `"http:bodyValue": "$.my.property"`. Without the parameter, the policy will be rejected.
     *
     */
    parameterCount?: number;
};

