/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { OdrlPolicyJson } from './OdrlPolicyJson';
import type { TemplatePlaceholder } from './TemplatePlaceholder';
/**
 * A stored policy template with its generated ID
 */
export type Template = {
    /**
     * The unique identifier of the template (auto-generated)
     */
    id: string;
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

