/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { OdrlPolicyJson } from './OdrlPolicyJson';
import type { TemplatePlaceholder } from './TemplatePlaceholder';
/**
 * Request body for creating or updating a policy template. Contains the ODRL policy skeleton with {{PLACEHOLDER}} tokens, a human-readable natural language description, and placeholder definitions.
 *
 */
export type TemplateCreate = {
    /**
     * Human-readable name of the template
     */
    name: string;
    /**
     * Human-readable description of what the template does
     */
    description?: string;
    odrl: OdrlPolicyJson;
    /**
     * A human-readable sentence using {{PLACEHOLDER}} keys, e.g. "Allow read access to {{RESOURCE_ID}} for users in role {{ROLE}}"
     *
     */
    naturalLanguage?: string;
    /**
     * List of placeholder definitions used in the ODRL skeleton and natural language text
     */
    placeholders: Array<TemplatePlaceholder>;
};

