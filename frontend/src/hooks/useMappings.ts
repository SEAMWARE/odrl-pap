/**
 * React hook for fetching and caching ODRL policy mappings.
 *
 * Fetches mappings from the `/mappings` endpoint once and caches
 * them in a module-level variable so all consumers share the same
 * data without redundant network requests.
 */
import { useState, useEffect, useCallback } from 'react';
import { UiService } from '../api/services/UiService';
import type { Mappings } from '../api';

/** Possible states for the mappings fetch lifecycle. */
interface UseMappingsResult {
  /** The fetched mappings data, or null while loading / on error. */
  mappings: Mappings | null;
  /** True while the initial fetch is in progress. */
  loading: boolean;
  /** Error message if the fetch failed, or null on success. */
  error: string | null;
  /** Clears the cache and re-fetches mappings from the server. */
  retry: () => void;
}

/** Module-level cache shared across all hook instances. */
let cachedMappings: Mappings | null = null;
/** Module-level promise to prevent duplicate concurrent fetches. */
let fetchPromise: Promise<Mappings> | null = null;

/**
 * Clears the in-memory mappings cache.
 * Exposed for testing purposes.
 */
export function clearMappingsCache(): void {
  cachedMappings = null;
  fetchPromise = null;
}

/**
 * Fetches and caches ODRL policy mappings.
 *
 * On first call, issues a GET /mappings request and caches the result.
 * Subsequent calls (from any component) return the cached data
 * immediately without a network request.
 *
 * @returns Loading state, cached data, error message, and a retry function.
 *
 * @example
 * ```tsx
 * const { mappings, loading, error, retry } = useMappings();
 * if (loading) return <Spinner />;
 * if (error) return <ErrorAlert message={error} onRetry={retry} />;
 * // use mappings...
 * ```
 */
export function useMappings(): UseMappingsResult {
  const [mappings, setMappings] = useState<Mappings | null>(cachedMappings);
  const [loading, setLoading] = useState<boolean>(!cachedMappings);
  const [error, setError] = useState<string | null>(null);

  const fetchMappings = useCallback(() => {
    // If already cached, use it immediately
    if (cachedMappings) {
      setMappings(cachedMappings);
      setLoading(false);
      setError(null);
      return;
    }

    setLoading(true);
    setError(null);

    // Reuse in-flight promise to avoid duplicate requests
    if (!fetchPromise) {
      fetchPromise = UiService.getMappings();
    }

    fetchPromise
      .then((data: Mappings) => {
        cachedMappings = data;
        fetchPromise = null;
        setMappings(data);
        setLoading(false);
      })
      .catch((err: Error) => {
        fetchPromise = null;
        setError(err.message || 'Failed to load mappings');
        setLoading(false);
      });
  }, []);

  const retry = useCallback(() => {
    cachedMappings = null;
    fetchPromise = null;
    fetchMappings();
  }, [fetchMappings]);

  useEffect(() => {
    fetchMappings();
  }, [fetchMappings]);

  return { mappings, loading, error, retry };
}
