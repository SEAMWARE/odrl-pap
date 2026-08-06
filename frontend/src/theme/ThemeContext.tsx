/**
 * React context provider for runtime theme customization.
 *
 * Injects CSS custom properties onto the container root element so
 * components can use `var(--odrl-primary-color)` etc. for fully
 * customizable styling. Supports light/dark presets and arbitrary
 * custom themes.
 */
import {
  useEffect,
  useMemo,
  type ReactNode,
} from 'react';
import {
  lightTheme,
  darkTheme,
  type ThemeConfig,
} from './defaultTheme';
import { ThemeContext, type ThemePreset } from './context';

/** Partial theme override — only supplied keys are changed. */
export type PartialThemeConfig = Partial<ThemeConfig>;

/** Resolves a preset name to the built-in ThemeConfig. */
function resolvePreset(preset: ThemePreset): ThemeConfig {
  return preset === 'dark' ? darkTheme : lightTheme;
}

/** Props for the ThemeProvider component. */
interface ThemeProviderProps {
  /** Built-in preset: "light" or "dark". Defaults to "light". */
  preset?: ThemePreset;
  /** Custom overrides merged on top of the selected preset. */
  overrides?: Partial<ThemeConfig>;
  children: ReactNode;
}

/**
 * Provides theme CSS custom properties to the component tree.
 *
 * Injects `--odrl-*` properties on `document.documentElement` so they
 * cascade to all elements. When unmounted, the properties are removed.
 *
 * @example
 * ```tsx
 * <ThemeProvider preset="dark">
 *   <App />
 * </ThemeProvider>
 * ```
 */
export const ThemeProvider = ({
  preset = 'light',
  overrides,
  children,
}: ThemeProviderProps) => {
  const resolved = useMemo<ThemeConfig>(() => {
    const base = resolvePreset(preset);
    if (!overrides) return base;
    return { ...base, ...overrides };
  }, [preset, overrides]);

  const contextValue = useMemo(
    () => ({ theme: resolved, preset: overrides ? 'custom' as const : preset }),
    [resolved, preset, overrides],
  );

  // Inject CSS custom properties onto the document root
  useEffect(() => {
    const root = document.documentElement;
    const entries = Object.entries(resolved) as Array<[string, string]>;
    for (const [prop, value] of entries) {
      root.style.setProperty(`--${prop}`, value);
    }
    return () => {
      for (const [prop] of entries) {
        root.style.removeProperty(`--${prop}`);
      }
    };
  }, [resolved]);

  return (
    <ThemeContext.Provider value={contextValue}>
      {children}
    </ThemeContext.Provider>
  );
};
