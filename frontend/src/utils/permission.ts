/**
 * Helpers for reading and writing the permission of a policy.
 *
 * ODRL allows `odrl:permission` to be a single object or an array of permissions. The Builder
 * edits one permission - the first - and must write it back in the shape it found it, leaving
 * any other permission untouched.
 */
import { getTerm, setTerm } from './odrlTerms';

/** The ODRL term of the permission property. */
const PERMISSION = 'permission';

type PermissionObject = Record<string, unknown>;

function isObject(value: unknown): value is PermissionObject {
  return typeof value === 'object' && value !== null && !Array.isArray(value);
}

/** Number of permissions of the policy (0 when there is none). */
export function permissionCount(policy: Record<string, unknown>): number {
  const value = getTerm(policy, PERMISSION);
  if (Array.isArray(value)) return value.length;
  return isObject(value) ? 1 : 0;
}

/** The permission the Builder edits: the object, or the first one of an array. Empty when there is none. */
export function readPermission(policy: Record<string, unknown>): PermissionObject {
  const value = getTerm(policy, PERMISSION);
  const first = Array.isArray(value) ? value[0] : value;
  return isObject(first) ? first : {};
}

/**
 * Returns a copy of the policy with the edited permission written back. An array stays an array
 * (only its first entry changes); an object or a missing permission becomes an object. The key the
 * policy already uses (`odrl:permission` or `permission`) is kept.
 */
export function writePermission(
  policy: Record<string, unknown>,
  permission: unknown,
): Record<string, unknown> {
  const value = getTerm(policy, PERMISSION);
  if (Array.isArray(value)) {
    return setTerm(policy, PERMISSION, [permission, ...value.slice(1)]);
  }
  return setTerm(policy, PERMISSION, permission);
}
