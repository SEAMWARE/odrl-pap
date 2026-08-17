/**
 * Pure i18n string resolution helpers (no React), so they can be imported by
 * both the {@link I18nProvider} and by code that renders the provider itself
 * (e.g. the Web Component root) without tripping fast-refresh's
 * component-only-export rule.
 */
import { en, type I18nStrings } from './en';

/** Recursive partial type allowing consumers to override only specific keys. */
export type DeepPartial<T> = {
  [K in keyof T]?: T[K] extends object ? DeepPartial<T[K]> : T[K];
};

/**
 * Deep-merges a partial override object into a base object.
 *
 * Keys present in `override` replace those in `base`; missing keys fall back to
 * `base` values.
 */
function deepMerge<T extends Record<string, unknown>>(
  base: T,
  override: DeepPartial<T>,
): T {
  const result = { ...base };
  for (const key of Object.keys(override) as Array<keyof T>) {
    const overrideVal = override[key];
    if (
      overrideVal !== undefined &&
      typeof overrideVal === 'object' &&
      overrideVal !== null &&
      !Array.isArray(overrideVal) &&
      typeof base[key] === 'object' &&
      base[key] !== null
    ) {
      result[key] = deepMerge(
        base[key] as Record<string, unknown>,
        overrideVal as DeepPartial<Record<string, unknown>>,
      ) as T[keyof T];
    } else if (overrideVal !== undefined) {
      result[key] = overrideVal as T[keyof T];
    }
  }
  return result;
}

/**
 * Resolves the effective i18n strings by deep-merging overrides onto the
 * English defaults.
 *
 * @param overrides - Partial string overrides, or `undefined` for defaults.
 * @returns The fully resolved string tree.
 */
export function resolveStrings(overrides?: DeepPartial<I18nStrings>): I18nStrings {
  return overrides ? deepMerge(en, overrides) : en;
}
