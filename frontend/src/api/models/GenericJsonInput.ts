/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
/**
 * Input for evaluating policies against arbitrary JSON payloads (non-HTTP). The `payload` contains the data to evaluate, and the optional `subject` carries identity or credential information about the requesting party.
 *
 */
export type GenericJsonInput = {
    /**
     * Arbitrary JSON payload to evaluate the policy against. This can represent any data structure — e.g., a resource, an event, or a document — that the policy's rules will inspect.
     *
     */
    payload: Record<string, any>;
    /**
     * Optional identity or credential information about the party requesting access. Used by assignee and subject-based policy constraints (e.g., verifiable credentials, roles, organization).
     *
     */
    subject?: Record<string, any>;
};

