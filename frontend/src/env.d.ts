/**
 * Type declarations for runtime environment configuration.
 *
 * The env-config.js script sets window.__ENV__ at load time,
 * providing runtime-configurable environment variables for
 * Docker deployments without requiring a rebuild.
 */

/** Runtime environment variables injected via env-config.js. */
interface EnvConfig {
  /** Base URL for the PAP API (e.g., "https://pap.example.com/api"). */
  API_BASE_URL?: string;
}

declare global {
  interface Window {
    /** Runtime environment configuration, set by public/env-config.js. */
    __ENV__?: EnvConfig;
  }
}

export {};
