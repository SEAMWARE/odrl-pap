/**
 * Hook to access the current locale and localized strings.
 *
 * Separated from I18nContext.tsx to satisfy react-refresh's
 * requirement that component files only export components.
 */
import { useContext } from 'react';
import { I18nContext, type I18nContextValue } from './context';

/**
 * Hook to access the current locale and localized strings.
 *
 * @returns Object with `locale` string and `strings` tree.
 *
 * @example
 * ```tsx
 * const { strings } = useI18n();
 * return <label>{strings.policyBuilder.stepTarget}</label>;
 * ```
 */
export const useI18n = (): I18nContextValue => useContext(I18nContext);
