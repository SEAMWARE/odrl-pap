/**
 * Standalone i18n context definition.
 *
 * Separated from the I18nProvider component file to satisfy
 * react-refresh's requirement that component files only export
 * components. This file exports only the React context and its
 * value type.
 */
import { createContext } from 'react';
import { en, type I18nStrings } from './en';

/** Value exposed by the i18n context. */
export interface I18nContextValue {
  /** Current locale identifier (e.g., "en", "de"). */
  locale: string;
  /** Fully resolved string tree (overrides merged with English defaults). */
  strings: I18nStrings;
}

/** The i18n React context. */
export const I18nContext = createContext<I18nContextValue>({
  locale: 'en',
  strings: en,
});
