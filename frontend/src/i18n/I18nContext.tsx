/**
 * React context provider for internationalization (i18n).
 *
 * Wraps the application (or a subtree) to provide localized strings
 * via the `useI18n()` hook. Supports runtime locale switching and
 * partial string overrides that fall back to English defaults.
 */
import { useMemo, type ReactNode } from 'react';
import { type I18nStrings } from './en';
import { I18nContext } from './context';
import { resolveStrings, type DeepPartial } from './resolve';

// Re-exported for backwards compatibility with modules that import the type
// from this file (the runtime helper now lives in ./resolve).
export type { DeepPartial } from './resolve';

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
    () => resolveStrings(overrides),
    [overrides],
  );

  const value = useMemo<I18nContextValue>(
    () => ({ locale, strings: merged }),
    [locale, merged],
  );

  return <I18nContext.Provider value={value}>{children}</I18nContext.Provider>;
};

