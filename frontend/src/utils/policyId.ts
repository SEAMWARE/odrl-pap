/**
 * Policy ids accepted by `PUT /policy/{id}`.
 *
 * The PAP uses a policy's id as its rego package and as the alias it is
 * imported under in the main policy, so the id has to be a valid rego
 * identifier that is not a rego keyword (mirrors `ApiResource.isValidPolicyId`
 * in the backend). An `odrl:uid` usually is not one: UUIDs contain hyphens
 * and IRIs contain `:` and `/`.
 */

const REGO_IDENTIFIER = /^[a-zA-Z_][a-zA-Z0-9_]*$/;

/** Rego keywords, plus the root documents, which cannot be used as an import alias. */
const REGO_RESERVED_WORDS = new Set([
  'as', 'contains', 'default', 'else', 'every', 'false', 'if', 'import', 'in', 'not', 'null',
  'package', 'some', 'true', 'with', 'data', 'input',
]);

/** Prefix of derived ids: starts them with a letter and keeps them clear of the keywords. */
const DERIVED_ID_PREFIX = 'p_';

/** Whether the PAP accepts `id` as a policy id. */
export function isValidPolicyId(id: string): boolean {
  return REGO_IDENTIFIER.test(id) && !REGO_RESERVED_WORDS.has(id);
}

/**
 * Derives the id a policy is stored under from its `odrl:uid`.
 *
 * A uid that already is a valid policy id is used as-is. Any other uid is
 * mapped deterministically, so saving the same policy again overwrites it
 * instead of creating a copy.
 */
export function toPolicyId(uid: string): string {
  if (isValidPolicyId(uid)) {
    return uid;
  }
  return DERIVED_ID_PREFIX + uid.replace(/[^a-zA-Z0-9_]/g, '_');
}
