/**
 * API client configuration module.
 *
 * Configures the auto-generated OpenAPI client with the correct base URL
 * and authentication headers. Supports three configuration sources
 * (highest priority first):
 *
 * 1. Runtime injection via `window.__ENV__.API_BASE_URL` (Docker deployments)
 * 2. Build-time env via `import.meta.env.VITE_API_BASE_URL` (static builds)
 * 3. Empty string fallback (dev-server proxy handles relative paths)
 */
import { OpenAPI } from '../api';
import type { Mappings, OdrlPolicyJson, Policy, TestRequest, ValidationResponse } from '../api';

/**
 * Resolves the API base URL from available configuration sources.
 *
 * @returns The base URL string, or empty string for relative/proxy mode.
 */
function resolveApiBaseUrl(): string {
  // 1. Runtime injection (Docker envsubst at container start)
  const runtimeUrl = window.__ENV__?.API_BASE_URL;
  if (runtimeUrl && !runtimeUrl.startsWith('$')) {
    return runtimeUrl;
  }
  // 2. Build-time Vite env variable
  if (typeof import.meta !== 'undefined' && import.meta.env?.VITE_API_BASE_URL) {
    return import.meta.env.VITE_API_BASE_URL;
  }
  // 3. Fallback: relative paths (handled by dev proxy or same-origin deployment)
  return '';
}

/** Retrieves the stored authentication token, if any. */
const getAuthToken = (): string | null => {
  return localStorage.getItem('authToken');
};

OpenAPI.BASE = resolveApiBaseUrl();
OpenAPI.WITH_CREDENTIALS = true;

/**
 * Use the HEADERS resolver to inject Accept and Authorization headers
 * into every API request. The generated request pipeline in request.ts
 * calls `resolve(options, config.HEADERS)` and merges the result.
 */
OpenAPI.HEADERS = async () => {
  const token = getAuthToken();
  const headers: Record<string, string> = {
    'Accept': 'application/json, text/plain, */*',
  };
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  return headers;
};

export { OpenAPI, resolveApiBaseUrl };
export type { Mappings, OdrlPolicyJson, Policy, TestRequest, ValidationResponse };
