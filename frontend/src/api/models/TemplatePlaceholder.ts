/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
/**
 * Defines a placeholder variable within a policy template. Each placeholder corresponds to a {{KEY}} token in the ODRL skeleton and natural language text.
 *
 */
export type TemplatePlaceholder = {
    /**
     * The placeholder key matching a {{KEY}} token in the ODRL skeleton and natural language text. Must follow the pattern [A-Z_][A-Z0-9_]*.
     *
     */
    key: string;
    /**
     * Display name for the input field shown to the user
     */
    name: string;
    /**
     * Help text shown to the user below the input field
     */
    description?: string;
    /**
     * The data type of the placeholder value
     */
    type: TemplatePlaceholder.type;
    /**
     * Optional list of allowed values. If provided, the placeholder is rendered as a dropdown select; if absent, a free-form input is shown.
     *
     */
    options?: Array<string>;
};
export namespace TemplatePlaceholder {
    /**
     * The data type of the placeholder value
     */
    export enum type {
        STRING = 'string',
        NUMBER = 'number',
        BOOLEAN = 'boolean',
        XSD_DATE = 'xsd:date',
    }
}

