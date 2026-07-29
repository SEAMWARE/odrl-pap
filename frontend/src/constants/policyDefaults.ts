/**
 * Default ODRL policy constants.
 *
 * Centralises the default `@context` object and new-policy template so
 * that every entry point (standalone editor, embedded web component,
 * tests) uses the same baseline.
 *
 * The PAP backend expects the context as a **namespaced object**:
 * ```json
 * { "odrl": "http://www.w3.org/ns/odrl/2/" }
 * ```
 * rather than a plain string `"http://www.w3.org/ns/odrl/2/"`.
 *
 * The context can be overridden at runtime via:
 * - **Standalone mode**: `window.__ENV__.ODRL_CONTEXT` (JSON string) or
 *   `import.meta.env.VITE_ODRL_CONTEXT` (build-time).
 * - **Embedded mode**: the `policy-context` HTML attribute (JSON string)
 *   or the `policyContext` JavaScript property (object) on the
 *   `<odrl-policy-editor>` custom element.
 */

/** ODRL namespace URI used as the default context value. */
const ODRL_NAMESPACE_URI = 'http://www.w3.org/ns/odrl/2/';

/**
 * Default JSON-LD `@context` for new ODRL policies.
 *
 * Uses the namespaced object format expected by the PAP backend's
 * JSON-LD compaction pipeline.
 */
export const DEFAULT_POLICY_CONTEXT: Record<string, string> = {
  odrl: ODRL_NAMESPACE_URI,
};

/** Default `@type` value for new ODRL policies. */
export const DEFAULT_POLICY_TYPE = 'odrl:Policy';

/**
 * Resolves the ODRL policy `@context` from available configuration sources.
 *
 * Resolution order (highest priority first):
 * 1. Explicit override passed as `override` parameter (embedded mode).
 * 2. Runtime injection via `window.__ENV__.ODRL_CONTEXT` (Docker deployments).
 * 3. Build-time env via `import.meta.env.VITE_ODRL_CONTEXT` (static builds).
 * 4. {@link DEFAULT_POLICY_CONTEXT} fallback.
 *
 * @param override - An explicit context value, typically supplied by the
 *   Web Component host via the `policyContext` property.
 * @returns The resolved `@context` value (object or string).
 */
export function resolvePolicyContext(
  override?: Record<string, string> | string | null,
): Record<string, string> | string {
  // 1. Explicit programmatic override (embedded / Web Component mode)
  if (override !== undefined && override !== null) {
    return override;
  }

  // 2. Runtime injection (Docker envsubst at container start)
  const runtimeCtx = window.__ENV__?.ODRL_CONTEXT;
  if (runtimeCtx && !runtimeCtx.startsWith('$')) {
    try {
      return JSON.parse(runtimeCtx) as Record<string, string>;
    } catch {
      // If it's not valid JSON, return it as a plain string
      return runtimeCtx;
    }
  }

  // 3. Build-time Vite env variable
  if (typeof import.meta !== 'undefined' && import.meta.env?.VITE_ODRL_CONTEXT) {
    try {
      return JSON.parse(import.meta.env.VITE_ODRL_CONTEXT) as Record<string, string>;
    } catch {
      return import.meta.env.VITE_ODRL_CONTEXT;
    }
  }

  // 4. Default
  return { ...DEFAULT_POLICY_CONTEXT };
}

/**
 * Creates a new empty ODRL policy template with the resolved context.
 *
 * @param contextOverride - Optional explicit `@context` override.
 * @returns A new ODRL policy object with a fresh UUID.
 */
export function createNewPolicy(
  contextOverride?: Record<string, string> | string | null,
): Record<string, unknown> {
  return {
    '@context': resolvePolicyContext(contextOverride),
    '@type': DEFAULT_POLICY_TYPE,
    'odrl:permission': {},
    'odrl:uid': crypto.randomUUID(),
  };
}
