/**
 * Helpers for reading JSON-LD `@context` values of ODRL policies.
 *
 * JSON-LD allows a prefix to be mapped either to a plain IRI string:
 * ```json
 * { "odrl": "http://www.w3.org/ns/odrl/2/" }
 * ```
 * or to an *expanded term definition* object, which is what the DOME ODRL
 * profile and the PAP's own compaction context use:
 * ```json
 * { "odrl": { "@id": "http://www.w3.org/ns/odrl/2/", "@prefix": true } }
 * ```
 * Both notations are valid and must be handled wherever a context entry is
 * displayed — rendering the raw object would crash React.
 */

/** JSON-LD keyword holding the IRI of an expanded term definition. */
const JSON_LD_ID_KEY = '@id';

/**
 * A single `@context` entry: either a plain IRI string or an expanded term
 * definition object (e.g. `{ "@id": "...", "@prefix": true }`).
 */
export type JsonLdContextEntry = string | Record<string, unknown>;

/** A JSON-LD `@context` given as a prefix-to-entry map. */
export type JsonLdContextObject = Record<string, JsonLdContextEntry>;

/**
 * Narrows an arbitrary `@context` value to a prefix-to-entry map.
 *
 * Returns `false` for string contexts (a single remote context IRI) and for
 * array contexts (a list of contexts), neither of which is a prefix map.
 *
 * @param value - The raw `@context` value taken from a policy.
 * @returns `true` when the value is a prefix-to-entry map.
 */
export function isJsonLdContextObject(value: unknown): value is JsonLdContextObject {
  return typeof value === 'object' && value !== null && !Array.isArray(value);
}

/**
 * Resolves the namespace IRI a `@context` entry maps to.
 *
 * @param entry - A plain IRI string or an expanded term definition.
 * @returns The IRI for both notations; for an unrecognised shape, its JSON
 *   representation, so the entry stays visible instead of rendering as blank.
 */
export function jsonLdContextEntryIri(entry: JsonLdContextEntry): string {
  if (typeof entry === 'string') {
    return entry;
  }
  const id = entry[JSON_LD_ID_KEY];
  return typeof id === 'string' ? id : JSON.stringify(entry);
}

/** An entry shown for a `@context`: a prefix with its namespace IRI, or a remote context IRI (no prefix). */
export interface ContextEntry {
  prefix?: string;
  uri: string;
}

/**
 * Lists what a `@context` defines, whichever shape it has: a prefix map, a remote context IRI, or
 * an array mixing both.
 */
export function listContextEntries(context: unknown): ContextEntry[] {
  if (typeof context === 'string') return [{ uri: context }];
  if (isJsonLdContextObject(context)) {
    return Object.entries(context).map(([prefix, entry]) => ({ prefix, uri: jsonLdContextEntryIri(entry) }));
  }
  if (Array.isArray(context)) return context.flatMap(listContextEntries);
  return [];
}

/**
 * Adds a `prefix` -> `uri` entry to a `@context` without losing what it already holds.
 *
 * A prefix map gets the prefix. A remote context IRI becomes the first entry of an array, followed
 * by the new prefix map. An array gets the prefix in its last prefix map, or a new one at its end.
 */
export function addContextEntry(context: unknown, prefix: string, uri: string): unknown {
  if (Array.isArray(context)) {
    const lastMap = context.map(isJsonLdContextObject).lastIndexOf(true);
    if (lastMap === -1) return [...context, { [prefix]: uri }];
    return context.map((entry, index) => (index === lastMap ? { ...(entry as JsonLdContextObject), [prefix]: uri } : entry));
  }
  if (isJsonLdContextObject(context)) return { ...context, [prefix]: uri };
  if (typeof context === 'string') return [context, { [prefix]: uri }];
  return { [prefix]: uri };
}

/** Removes a prefix from a `@context` (an object, or the objects of an array); other shapes are returned as they are. */
export function removeContextPrefix(context: unknown, prefix: string): unknown {
  if (isJsonLdContextObject(context)) {
    const copy = { ...context };
    delete copy[prefix];
    return copy;
  }
  if (Array.isArray(context)) {
    return context
      .map((entry) => (isJsonLdContextObject(entry) ? removeContextPrefix(entry, prefix) : entry))
      .filter((entry) => !(isJsonLdContextObject(entry) && Object.keys(entry).length === 0));
  }
  return context;
}
