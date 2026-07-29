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
  /**
   * JSON-LD `@context` for new ODRL policies (JSON string).
   *
   * When set, overrides the built-in default context. The value must be
   * a valid JSON string representing either an object or a string.
   *
   * @example '{"odrl":"http://www.w3.org/ns/odrl/2/","dome-op":"https://github.com/DOME-Marketplace/dome-odrl-profile#"}'
   */
  ODRL_CONTEXT?: string;
}

declare global {
  interface Window {
    /** Runtime environment configuration, set by public/env-config.js. */
    __ENV__?: EnvConfig;
  }
}

export {};
