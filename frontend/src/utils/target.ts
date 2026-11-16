/**
 * The target of a permission and the key it is written under.
 *
 * The backend does not read the target from `odrl:target` alone: it looks, in the permission, for
 * any key of the `target` mapping (`odrl:uid`, `odrl:target`, `json:uid`, `json:target`,
 * `json:payloadTarget`) and takes the rego rule from the key that is present, with the key's value
 * as the target id. So the key carries meaning and the editor has to write the one the user chose.
 */
import { findTermKey, removeTerm, termKey } from './odrlTerms';

type Obj = Record<string, unknown>;

/**
 * Target keys that are not a spelling of the plain `odrl:target` term, as the default mapping
 * defines them. The backend reads them from its `target` mappings, which can be extended, so
 * callers that have the `/mappings` response pass its target names as `extraKeys`.
 */
const NAMESPACED_TARGET_KEYS = ['odrl:uid', 'json:uid', 'json:target', 'json:payloadTarget'];

/** The plain target term, as the editor's dropdown names it. */
export const PLAIN_TARGET = 'odrl:target';

/** The key the permission holds its target under, or `undefined` when it has none. */
export function findTargetKey(permission: Obj, extraKeys: readonly string[] = []): string | undefined {
  return (
    findTermKey(permission, 'target') ??
    namespacedKeys(extraKeys).find((key) => Object.prototype.hasOwnProperty.call(permission, key))
  );
}

/** The default namespaced target keys plus the ones the mappings add. */
function namespacedKeys(extraKeys: readonly string[]): string[] {
  return [...new Set([...NAMESPACED_TARGET_KEYS, ...extraKeys])].filter((key) => !isPlainTargetKey(key));
}

/** The names of the target mappings, to pass as `extraKeys`. */
export function targetKeysOf(mappings: { targets?: ReadonlyArray<{ name?: string }> } | undefined): string[] {
  return (mappings?.targets ?? []).map((m) => m.name).filter((n): n is string => !!n);
}

/** Whether the key is a spelling of the plain `target` term (`odrl:target`, `target`, full IRI). */
export function isPlainTargetKey(key: string): boolean {
  return findTermKey({ [key]: true }, 'target') === key;
}

/**
 * A copy of the permission with the target written.
 *
 * @param key - The key to write under, as the dropdown names it (`json:uid`, `odrl:target`, ...). A
 *   plain `odrl:target` goes to the spelling the permission already uses. When omitted, the key
 *   already in use stays. Any other target key is removed, so the permission holds one target.
 */
export function writeTarget(
  permission: Obj,
  value: unknown,
  key?: string,
  extraKeys: readonly string[] = [],
): Obj {
  const current = findTargetKey(permission, extraKeys);
  let nextKey: string;
  if (key === undefined) nextKey = current ?? termKey(permission, 'target');
  else if (isPlainTargetKey(key)) nextKey = termKey(permission, 'target');
  else nextKey = key;

  if (nextKey === current) return { ...permission, [nextKey]: value };

  const rest: Obj = removeTerm(permission, 'target');
  namespacedKeys(extraKeys).forEach((targetKey) => delete rest[targetKey]);
  return { ...rest, [nextKey]: value };
}
