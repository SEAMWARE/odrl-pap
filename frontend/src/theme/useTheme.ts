/**
 * Hook to access the current theme configuration.
 *
 * Separated from ThemeContext.tsx to satisfy react-refresh's
 * requirement that component files only export components.
 */
import { useContext } from 'react';
import { ThemeContext, type ThemeContextValue } from './context';

/**
 * Hook to access the current theme configuration.
 *
 * @returns Object with `theme` (full ThemeConfig) and `preset` name.
 *
 * @example
 * ```tsx
 * const { theme, preset } = useTheme();
 * ```
 */
export const useTheme = (): ThemeContextValue => useContext(ThemeContext);
