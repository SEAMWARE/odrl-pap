/**
 * Helpers for reading and writing the permission of a policy.
 *
 * ODRL allows `odrl:permission` to be a single object or an array of permissions. The Builder
 * edits one permission - the first - and must write it back in the shape it found it, leaving
 * any other permission untouched.
 */
/** Key of the permission property written by the editor. */
export const PERMISSION_KEY = 'odrl:permission';

type PermissionObject = Record<string, unknown>;

function isObject(value: unknown): value is PermissionObject {
  return typeof value === 'object' && value !== null && !Array.isArray(value);
}

/** Number of permissions of the policy (0 when there is none). */
export function permissionCount(policy: Record<string, unknown>): number {
  const value = policy[PERMISSION_KEY];
  if (Array.isArray(value)) return value.length;
  return isObject(value) ? 1 : 0;
}

/** The permission the Builder edits: the object, or the first one of an array. Empty when there is none. */
export function readPermission(policy: Record<string, unknown>): PermissionObject {
  const value = policy[PERMISSION_KEY];
  const first = Array.isArray(value) ? value[0] : value;
  return isObject(first) ? first : {};
}

/**
 * Returns a copy of the policy with the edited permission written back. An array stays an array
 * (only its first entry changes); an object or a missing permission becomes an object.
 */
export function writePermission(
  policy: Record<string, unknown>,
  permission: unknown,
): Record<string, unknown> {
  const value = policy[PERMISSION_KEY];
  if (Array.isArray(value)) {
    return { ...policy, [PERMISSION_KEY]: [permission, ...value.slice(1)] };
  }
  return { ...policy, [PERMISSION_KEY]: permission };
}
