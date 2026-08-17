/**
 * Localization (i18n) module.
 *
 * Re-exports the context provider, hook, types, and default English
 * strings so consumers can import everything from `@/i18n`.
 *
 * @example
 * ```tsx
 * import { I18nProvider, useI18n } from '../i18n';
 * ```
 */
export { en, type I18nStrings } from './en';
export { I18nProvider } from './I18nContext';
export { resolveStrings, type DeepPartial } from './resolve';
export { useI18n } from './useI18n';
