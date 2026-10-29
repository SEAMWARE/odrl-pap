/**
 * The mappings the PAP ships with, used by the embedded editor when it is not
 * allowed to fetch them (`mappings="none"`). Mappings the host sets through the
 * `mappings` property are layered over them with {@link mergeMappings}.
 *
 * `defaultMappings.json` is generated from the backend's `mapping.json` by
 * `npm run generate-default-mappings` (also run by `build:component`), so it
 * matches the PAP of the same release. It does not include mappings an
 * operator adds through `paths.mapping`: only a remote fetch sees those.
 */
import type { Mappings } from '../api';
import defaultMappingsJson from './defaultMappings.json';

export const DEFAULT_MAPPINGS: Mappings = defaultMappingsJson;

/**
 * Layers `overrides` over `base`, per attribute and by mapping `name`.
 *
 * A mapping in `overrides` replaces the one with the same name in place and a
 * new name is appended, so the host can extend or redefine the defaults
 * without restating them. This mirrors how the PAP merges its additional
 * mapping file into the built-in one.
 */
export function mergeMappings(base: Mappings, overrides?: Mappings): Mappings {
  if (!overrides) return base;
  const merged: Mappings = { ...base };
  for (const field of Object.keys(overrides) as (keyof Mappings)[]) {
    const byName = new Map((base[field] ?? []).map((mapping) => [mapping.name, mapping]));
    for (const mapping of overrides[field] ?? []) byName.set(mapping.name, mapping);
    merged[field] = [...byName.values()];
  }
  return merged;
}
