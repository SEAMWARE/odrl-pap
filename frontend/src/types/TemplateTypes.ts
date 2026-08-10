/**
 * Frontend-specific template type definitions and constants.
 *
 * Re-exports generated API types and defines helpers for
 * placeholder detection and type validation in the template editor.
 */

// Re-export generated API types for convenience
export type { Template } from '../api/models/Template';
export type { TemplateCreate } from '../api/models/TemplateCreate';
export type { TemplatePlaceholder } from '../api/models/TemplatePlaceholder';
export type { TemplateList } from '../api/models/TemplateList';

/**
 * Union of allowed placeholder data types.
 * Mirrors the `TemplatePlaceholder.type` enum from the generated API.
 */
export type PlaceholderType = 'string' | 'number' | 'boolean' | 'xsd:date';

/**
 * Regex pattern to detect `{{PLACEHOLDER_NAME}}` tokens in strings.
 * Matches uppercase letters, digits, and underscores between double curly braces.
 *
 * @example
 * ```ts
 * const matches = someText.match(PLACEHOLDER_REGEX);
 * // ["{{RESOURCE_ID}}", "{{ROLE}}"]
 * ```
 */
export const PLACEHOLDER_REGEX = /\{\{([A-Z_][A-Z0-9_]*)\}\}/g;

/**
 * Ordered list of supported placeholder types for use in dropdown selectors.
 */
export const PLACEHOLDER_TYPES: readonly PlaceholderType[] = [
  'string',
  'number',
  'boolean',
  'xsd:date',
] as const;

/**
 * Extracts all unique placeholder keys from a text string.
 *
 * @param text - The input string containing `{{KEY}}` tokens.
 * @returns An array of unique placeholder key strings (without braces).
 */
/**
 * Extracts all unique placeholder keys from a text string.
 *
 * @param text - The input string containing `{{KEY}}` tokens.
 * @returns An array of unique placeholder key strings (without braces).
 */
export function extractPlaceholderKeys(text: string): string[] {
  const keys = new Set<string>();
  let match: RegExpExecArray | null;
  const regex = new RegExp(PLACEHOLDER_REGEX.source, 'g');
  while ((match = regex.exec(text)) !== null) {
    keys.add(match[1]);
  }
  return Array.from(keys);
}

/**
 * Creates a new empty placeholder definition with the given key.
 *
 * Auto-generates a human-readable display name from the key by
 * replacing underscores with spaces and title-casing the result.
 *
 * @param key - The placeholder key (e.g., "RESOURCE_ID").
 * @returns A new TemplatePlaceholder with default values.
 */
export function createEmptyPlaceholder(key: string): import('../api/models/TemplatePlaceholder').TemplatePlaceholder {
  return {
    key,
    name: key.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, (c) => c.toUpperCase()),
    description: '',
    type: 'string' as import('../api/models/TemplatePlaceholder').TemplatePlaceholder.type,
    options: [],
  };
}
