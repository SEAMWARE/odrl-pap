/**
 * Type definitions for the policy template extensibility architecture.
 *
 * These interfaces define the contract for future policy templates —
 * pre-defined policy skeletons that users can fill in rather than
 * building from scratch. No actual templates are provided here; only
 * the structural types and helpers needed so that a future iteration
 * can plug templates in seamlessly.
 *
 * @example
 * ```ts
 * const template: PolicyTemplate = {
 *   id: 'dome-marketplace-access',
 *   name: 'DOME Marketplace Access',
 *   description: 'Grants access to a DOME marketplace resource',
 *   category: 'DOME',
 *   skeleton: {
 *     '@context': 'http://www.w3.org/ns/odrl/2/',
 *     '@type': 'odrl:Policy',
 *     'odrl:permission': {
 *       'odrl:action': 'dome-op:access',
 *       'odrl:target': '',          // user fills this in
 *     },
 *   },
 *   editableFields: [
 *     {
 *       path: 'odrl:permission.odrl:target',
 *       label: 'Target Resource',
 *       description: 'The DOME resource URL to grant access to',
 *       type: 'text',
 *       required: true,
 *     },
 *   ],
 *   lockedFields: ['odrl:permission.odrl:action'],
 * };
 * ```
 */

/**
 * Input control types for template editable fields.
 *
 * - `dropdown` — rendered as a namespace-grouped dropdown from mappings
 * - `text` — rendered as a free-text input
 * - `constraint` — rendered as the full constraint builder
 */
export type TemplateFieldType = 'dropdown' | 'text' | 'constraint';

/**
 * Describes a single user-editable field within a policy template.
 *
 * Each field maps to a location in the template skeleton via a
 * dot-separated JSON path.
 */
export interface TemplateField {
  /** Dot-separated JSON path in the skeleton (e.g., "odrl:permission.odrl:target"). */
  path: string;
  /** Human-readable label displayed above the field. */
  label: string;
  /** Help text shown below the field to guide the user. */
  description: string;
  /** The type of input control to render for this field. */
  type: TemplateFieldType;
  /** Whether the user must provide a value before saving. */
  required: boolean;
}

/**
 * A pre-defined policy template that users can fill in.
 *
 * Templates provide a skeleton ODRL policy with some fields
 * pre-populated and locked, while others are left for the user
 * to complete. This reduces errors and speeds up policy creation
 * for common use cases.
 */
export interface PolicyTemplate {
  /** Unique identifier for the template. */
  id: string;
  /** Human-readable template name (displayed in the UI). */
  name: string;
  /** Brief description of what this template does. */
  description: string;
  /** Grouping category (e.g., "DOME", "NGSI-LD", "General"). */
  category: string;
  /** Pre-filled ODRL policy structure with placeholder markers. */
  skeleton: Record<string, unknown>;
  /** Fields the user must fill in or can edit. */
  editableFields: TemplateField[];
  /** Dot-separated JSON paths of fields that are locked and cannot be changed. */
  lockedFields: string[];
}
