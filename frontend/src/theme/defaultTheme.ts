/**
 * Default theme definitions for the ODRL Policy Builder.
 *
 * Themes are expressed as maps of CSS custom-property names to values.
 * The ThemeProvider injects these onto the container root element so
 * all components can reference them via `var(--odrl-*)`.
 */

/** Map of CSS custom-property names (without `--` prefix) to values. */
export interface ThemeConfig {
  /** Primary brand color (used for navbar, primary buttons). */
  'odrl-primary-color': string;
  /** Secondary / accent color (used for highlights, links). */
  'odrl-secondary-color': string;
  /** Page / container background. */
  'odrl-bg-color': string;
  /** Default text color. */
  'odrl-text-color': string;
  /** Muted / secondary text color. */
  'odrl-text-muted': string;
  /** Card and panel background. */
  'odrl-card-bg': string;
  /** Default border color. */
  'odrl-border-color': string;
  /** Base font family. */
  'odrl-font-family': string;
  /** Default border radius. */
  'odrl-border-radius': string;
  /** Section / step header background. */
  'odrl-section-header-bg': string;
  /** Success indicator color. */
  'odrl-success-color': string;
  /** Danger / error indicator color. */
  'odrl-danger-color': string;
  /** Info indicator color. */
  'odrl-info-color': string;
  /** Box shadow for cards. */
  'odrl-card-shadow': string;
}

/** Light theme (default) matching the existing Bootstrap-based look. */
export const lightTheme: ThemeConfig = {
  'odrl-primary-color': '#0B2B40',
  'odrl-secondary-color': '#F07D00',
  'odrl-bg-color': '#FFFFFF',
  'odrl-text-color': '#333333',
  'odrl-text-muted': '#6c757d',
  'odrl-card-bg': '#f8f9fa',
  'odrl-border-color': '#dee2e6',
  'odrl-font-family': "'Lato', sans-serif",
  'odrl-border-radius': '0.375rem',
  'odrl-section-header-bg': '#e9ecef',
  'odrl-success-color': '#198754',
  'odrl-danger-color': '#dc3545',
  'odrl-info-color': '#0dcaf0',
  'odrl-card-shadow': '0 0.125rem 0.25rem rgba(0,0,0,0.075)',
};

/** Dark theme preset. */
export const darkTheme: ThemeConfig = {
  'odrl-primary-color': '#1a4a6b',
  'odrl-secondary-color': '#F07D00',
  'odrl-bg-color': '#1a1a2e',
  'odrl-text-color': '#e0e0e0',
  'odrl-text-muted': '#adb5bd',
  'odrl-card-bg': '#16213e',
  'odrl-border-color': '#2a2a4a',
  'odrl-font-family': "'Lato', sans-serif",
  'odrl-border-radius': '0.375rem',
  'odrl-section-header-bg': '#0f3460',
  'odrl-success-color': '#20c997',
  'odrl-danger-color': '#e74c3c',
  'odrl-info-color': '#17a2b8',
  'odrl-card-shadow': '0 0.125rem 0.25rem rgba(0,0,0,0.3)',
};

/** Default theme used when no ThemeProvider is present. */
export const defaultTheme = lightTheme;
