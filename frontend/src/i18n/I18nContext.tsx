/**
 * React context provider for internationalization (i18n).
 *
 * Wraps the application (or a subtree) to provide localized strings
 * via the `useI18n()` hook. Supports runtime locale switching and
 * partial string overrides that fall back to English defaults.
 */
import { useMemo, type ReactNode } from 'react';
import { en, type I18nStrings } from './en';
import { I18nContext } from './context';

/** Recursive partial type allowing consumers to override only specific keys. */
export type DeepPartial<T> = {
  [K in keyof T]?: T[K] extends object ? DeepPartial<T[K]> : T[K];
};

/**
 * Deep-merges a partial override object into a base object.
 * Keys present in `override` replace those in `base`; missing keys
 * fall back to `base` values.
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

/** Value exposed by the i18n context (re-imported from context.ts). */
type I18nContextValue = import('./context').I18nContextValue;

/** Props for the I18nProvider component. */
interface I18nProviderProps {
  /** Locale identifier (e.g., "en", "de"). Defaults to "en". */
  locale?: string;
  /** Partial string overrides merged on top of English defaults. */
  strings?: DeepPartial<I18nStrings>;
  children: ReactNode;
}

/**
 * Provides localized strings to the component tree.
 *
 * @example
 * ```tsx
 * <I18nProvider locale="de" strings={germanStrings}>
 *   <App />
 * </I18nProvider>
 * ```
 */
export const I18nProvider = ({
  locale = 'en',
  strings: overrides,
  children,
}: I18nProviderProps) => {
  const merged = useMemo<I18nStrings>(
    () => (overrides ? deepMerge(en, overrides) : en),
    [overrides],
  );

  const value = useMemo<I18nContextValue>(
    () => ({ locale, strings: merged }),
    [locale, merged],
  );

  return <I18nContext.Provider value={value}>{children}</I18nContext.Provider>;
};

