/**
 * Standalone theme context definition.
 *
 * Separated from the ThemeProvider component file to satisfy
 * react-refresh's requirement that component files only export
 * components. This file exports only the React context and its
 * value type.
 */
import { createContext } from 'react';
import { lightTheme, type ThemeConfig } from './defaultTheme';

/** Named presets that map to built-in themes. */
export type ThemePreset = 'light' | 'dark';

/** Value exposed by the theme context. */
export interface ThemeContextValue {
  /** The fully resolved theme configuration. */
  theme: ThemeConfig;
  /** The active preset name, or "custom" for user-supplied themes. */
  preset: ThemePreset | 'custom';
}

/** The theme React context. */
export const ThemeContext = createContext<ThemeContextValue>({
  theme: lightTheme,
  preset: 'light',
});
