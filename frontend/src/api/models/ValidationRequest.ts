/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { GenericJsonInput } from './GenericJsonInput';
import type { OdrlPolicyJson } from './OdrlPolicyJson';
import type { TestRequest } from './TestRequest';
/**
 * Request to validate a policy. Provide either `testRequest` (for HTTP request evaluation) or `jsonInput` (for arbitrary JSON payload evaluation). The two are mutually exclusive — supply one or the other. The policy's JSON-LD context determines which evaluation path is used. Optionally, supply `additionalContexts` to override how URIs are compacted (e.g., compact `odrl:use` to `mcp:use` instead).
 *
 */
export type ValidationRequest = {
    policy?: OdrlPolicyJson;
    testRequest?: TestRequest;
    jsonInput?: GenericJsonInput;
    /**
     * Optional list of additional JSON-LD context objects for compaction. Each entry is a JSON-LD context object mapping namespace prefixes to IRIs (e.g., {"mcp": {"@id": "http://www.w3.org/ns/odrl/2/", "@prefix": true}}). These are merged with the default compaction context. If an additional context maps a prefix to an IRI that already exists in the base context under a different prefix, the base prefix is replaced — ensuring that the new prefix is used during compaction.
     *
     */
    additionalContexts?: Array<Record<string, any>>;
};

