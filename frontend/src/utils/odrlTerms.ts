/**
 * Access to ODRL terms that may be written prefixed (`odrl:permission`), bare (`permission`, as the
 * ODRL JSON-LD context allows) or as the full IRI. Reads accept all three; writes go to the key the
 * object already uses, so editing never adds a second spelling of the same term. Only an object that
 * does not have the term yet gets the prefixed form, which the editor's default `@context` defines.
 */
const ODRL_PREFIX = 'odrl:';
const ODRL_NAMESPACE = 'http://www.w3.org/ns/odrl/2/';

type Obj = Record<string, unknown>;

/** The spellings of an ODRL term, in the order they are looked up. */
function aliases(term: string): string[] {
  return [`${ODRL_PREFIX}${term}`, term, `${ODRL_NAMESPACE}${term}`];
}

/** The ODRL term a key stands for (`odrl:target` and `target` -> `target`), or the key itself. */
function termOf(key: string): string {
  if (key.startsWith(ODRL_PREFIX)) return key.slice(ODRL_PREFIX.length);
  if (key.startsWith(ODRL_NAMESPACE)) return key.slice(ODRL_NAMESPACE.length);
  return key;
}

/** The key under which the object holds the term, or `undefined` when it does not. */
export function findTermKey(obj: Obj, term: string): string | undefined {
  return aliases(termOf(term)).find((key) => Object.prototype.hasOwnProperty.call(obj, key));
}

/** The key to write the term to: the one already in use, else the prefixed form. */
export function termKey(obj: Obj, term: string): string {
  return findTermKey(obj, term) ?? `${ODRL_PREFIX}${termOf(term)}`;
}

/** The value of the term, whichever way it is spelled. */
export function getTerm(obj: Obj | undefined | null, term: string): unknown {
  if (!obj) return undefined;
  const key = findTermKey(obj, term);
  return key === undefined ? undefined : obj[key];
}

/** A copy of the object with the term set, on the key it already uses. */
export function setTerm(obj: Obj, term: string, value: unknown): Obj {
  return { ...obj, [termKey(obj, term)]: value };
}

/** A copy of the object without the term, under any of its spellings. */
export function removeTerm(obj: Obj, term: string): Obj {
  const copy = { ...obj };
  aliases(termOf(term)).forEach((key) => delete copy[key]);
  return copy;
}

/**
 * The term spelled like a key that is already in use: `odrl:` prefixed, bare or as the full IRI.
 * Lets a new sibling term follow the spelling the object already uses.
 */
export function spelledLike(sampleKey: string, term: string): string {
  if (sampleKey.startsWith(ODRL_NAMESPACE)) return `${ODRL_NAMESPACE}${term}`;
  if (sampleKey.startsWith(ODRL_PREFIX)) return `${ODRL_PREFIX}${term}`;
  return term;
}
