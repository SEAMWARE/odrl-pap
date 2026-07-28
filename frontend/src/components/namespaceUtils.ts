/**
 * Utility functions for namespace-based grouping of mapping items.
 *
 * Extracted to a separate file to avoid react-refresh lint warnings
 * when co-exporting non-component functions alongside React components.
 */

/** Separator used to split namespace from local name. */
const NAMESPACE_SEPARATOR = ':';

/**
 * Splits a mapping name into namespace prefix and local name.
 *
 * @param name - Full qualified name (e.g., "odrl:read").
 * @returns Tuple of [prefix, localName]. If no separator is found,
 *          prefix is "other" and localName is the full name.
 */
export function splitNamespace(name: string): [string, string] {
  const idx = name.indexOf(NAMESPACE_SEPARATOR);
  if (idx === -1) return ['other', name];
  return [name.substring(0, idx), name.substring(idx + 1)];
}

/**
 * Formats a namespace prefix into a human-readable group label.
 *
 * @param prefix - Raw prefix (e.g., "dome-op", "odrl").
 * @returns Display label (e.g., "DOME-OP", "ODRL").
 */
export function formatGroupLabel(prefix: string): string {
  return prefix.toUpperCase();
}
