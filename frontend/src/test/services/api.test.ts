/**
 * Unit tests for the API configuration module.
 *
 * Verifies that the base URL resolution logic correctly prioritizes
 * runtime env injection over build-time env vars, and falls back
 * to the build-time value or empty string when runtime is unavailable.
 */
import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import { resolveApiBaseUrl } from '../../services/api';

/** The value of VITE_API_BASE_URL from the .env file, used as build-time fallback. */
const BUILD_TIME_BASE_URL = '/api';

describe('resolveApiBaseUrl', () => {
  const originalEnv = window.__ENV__;

  beforeEach(() => {
    // Reset window.__ENV__ before each test
    delete window.__ENV__;
  });

  afterEach(() => {
    // Restore original state
    window.__ENV__ = originalEnv;
    vi.unstubAllEnvs();
  });

  it('returns runtime URL when window.__ENV__.API_BASE_URL is set', () => {
    window.__ENV__ = { API_BASE_URL: 'https://pap.example.com/api' };
    expect(resolveApiBaseUrl()).toBe('https://pap.example.com/api');
  });

  it('prioritizes runtime URL over build-time env var', () => {
    window.__ENV__ = { API_BASE_URL: 'https://runtime.example.com' };
    // Build-time env is also set (from .env), but runtime should win
    expect(resolveApiBaseUrl()).toBe('https://runtime.example.com');
  });

  it('ignores un-substituted template placeholder (starts with $)', () => {
    window.__ENV__ = { API_BASE_URL: '$VITE_API_BASE_URL' };
    // Should fall through to build-time env var from .env
    const result = resolveApiBaseUrl();
    expect(result).toBe(BUILD_TIME_BASE_URL);
  });

  it('falls back to build-time VITE_API_BASE_URL when runtime is unavailable', () => {
    window.__ENV__ = undefined;
    expect(resolveApiBaseUrl()).toBe(BUILD_TIME_BASE_URL);
  });

  it('returns empty string when both runtime and build-time are unavailable', () => {
    window.__ENV__ = undefined;
    vi.stubEnv('VITE_API_BASE_URL', '');
    expect(resolveApiBaseUrl()).toBe('');
  });
});
