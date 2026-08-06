/**
 * Runtime environment configuration.
 *
 * This file is served as a static asset and loaded before the application
 * bundle. In production Docker deployments, `envsubst` replaces the
 * placeholder values at container start time, allowing configuration
 * changes without rebuilding the image.
 *
 * In development, the values are empty strings and the application
 * falls back to Vite's import.meta.env or sensible defaults.
 */
window.__ENV__ = {
  API_BASE_URL: "$VITE_API_BASE_URL",
  ODRL_CONTEXT: "$VITE_ODRL_CONTEXT",
};
