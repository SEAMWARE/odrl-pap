/**
 * Unit tests for the useMappings hook.
 *
 * Verifies fetch lifecycle (loading, success, error), caching behavior,
 * and the retry mechanism.
 */
import { describe, it, expect, beforeEach, vi } from 'vitest';
import { renderHook, waitFor, act } from '@testing-library/react';
import { useMappings, clearMappingsCache } from './useMappings';
import { UiService } from '../api/services/UiService';
import type { Mappings } from '../api';

// Mock the UiService module
vi.mock('../api/services/UiService', () => ({
  UiService: {
    getMappings: vi.fn(),
  },
}));

/** Realistic mock mappings response. */
const MOCK_MAPPINGS: Mappings = {
  actions: [
    { name: 'odrl:read', description: 'To read a resource' },
    { name: 'odrl:use', description: 'To use a resource' },
  ],
  operators: [{ name: 'odrl:eq', description: 'Equal to' }],
  leftOperands: [{ name: 'dome-op:role', description: 'Role' }],
};

/** Error message for failed fetch tests. */
const FETCH_ERROR_MESSAGE = 'Network error';

describe('useMappings', () => {
  beforeEach(() => {
    clearMappingsCache();
    vi.mocked(UiService.getMappings).mockReset();
  });

  it('starts in loading state', () => {
    vi.mocked(UiService.getMappings).mockReturnValue(
      new Promise(() => {}), // never resolves
    );

    const { result } = renderHook(() => useMappings());

    expect(result.current.loading).toBe(true);
    expect(result.current.mappings).toBeNull();
    expect(result.current.error).toBeNull();
  });

  it('provides mappings after successful fetch', async () => {
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);

    const { result } = renderHook(() => useMappings());

    await waitFor(() => {
      expect(result.current.loading).toBe(false);
    });

    expect(result.current.mappings).toEqual(MOCK_MAPPINGS);
    expect(result.current.error).toBeNull();
  });

  it('provides error on fetch failure', async () => {
    vi.mocked(UiService.getMappings).mockRejectedValue(
      new Error(FETCH_ERROR_MESSAGE),
    );

    const { result } = renderHook(() => useMappings());

    await waitFor(() => {
      expect(result.current.loading).toBe(false);
    });

    expect(result.current.mappings).toBeNull();
    expect(result.current.error).toBe(FETCH_ERROR_MESSAGE);
  });

  it('caches mappings across multiple hook instances', async () => {
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);

    // First hook call triggers fetch
    const { result: result1 } = renderHook(() => useMappings());
    await waitFor(() => {
      expect(result1.current.loading).toBe(false);
    });

    // Second hook call should use cached data
    const { result: result2 } = renderHook(() => useMappings());

    // Should already have data (no loading state)
    expect(result2.current.mappings).toEqual(MOCK_MAPPINGS);
    expect(result2.current.loading).toBe(false);

    // UiService.getMappings should have been called only once
    expect(UiService.getMappings).toHaveBeenCalledTimes(1);
  });

  it('retry clears cache and re-fetches', async () => {
    // First call fails
    vi.mocked(UiService.getMappings).mockRejectedValueOnce(
      new Error(FETCH_ERROR_MESSAGE),
    );

    const { result } = renderHook(() => useMappings());

    await waitFor(() => {
      expect(result.current.error).toBe(FETCH_ERROR_MESSAGE);
    });

    // Now set up success for retry
    vi.mocked(UiService.getMappings).mockResolvedValueOnce(MOCK_MAPPINGS);

    act(() => {
      result.current.retry();
    });

    await waitFor(() => {
      expect(result.current.loading).toBe(false);
    });

    expect(result.current.mappings).toEqual(MOCK_MAPPINGS);
    expect(result.current.error).toBeNull();
    expect(UiService.getMappings).toHaveBeenCalledTimes(2);
  });

  it('clearMappingsCache resets the module-level cache', async () => {
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);

    // First call populates cache
    const { result: result1 } = renderHook(() => useMappings());
    await waitFor(() => {
      expect(result1.current.mappings).toEqual(MOCK_MAPPINGS);
    });

    // Clear cache
    clearMappingsCache();

    // Next call should re-fetch
    const { result: result2 } = renderHook(() => useMappings());

    await waitFor(() => {
      expect(result2.current.mappings).toEqual(MOCK_MAPPINGS);
    });

    expect(UiService.getMappings).toHaveBeenCalledTimes(2);
  });

  it('provides a fallback error message when error has no message', async () => {
    vi.mocked(UiService.getMappings).mockRejectedValue(new Error(''));

    const { result } = renderHook(() => useMappings());

    await waitFor(() => {
      expect(result.current.loading).toBe(false);
    });

    expect(result.current.error).toBe('Failed to load mappings');
  });
});
