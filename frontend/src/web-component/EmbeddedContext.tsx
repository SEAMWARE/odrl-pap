/**
 * React context provider for embedded (Web Component) mode.
 *
 * When the policy editor runs inside a `<odrl-policy-editor>` custom
 * element, this context supplies configuration that would otherwise
 * come from environment variables, localStorage, or the URL router.
 *
 * Components can call `useEmbeddedContext()` to check whether they are
 * running in embedded mode and read the host-supplied configuration.
 */
import { createContext, useContext, type ReactNode } from 'react';

/** Editor operating mode: create a new policy or edit an existing one. */
export type EditorMode = 'create' | 'edit';

/** Theme preset names supported by the embedded component. */
export type EmbeddedThemePreset = 'light' | 'dark';

/**
 * Map of custom event names to their `detail` payload types.
 *
 * The Web Component host listens for these events on the custom element.
 */
export interface EmbeddedEventMap {
  /** Fired after a new policy is successfully saved. */
  'policy-created': { policy: Record<string, unknown>; id: string };
  /** Fired after an existing policy is successfully updated. */
  'policy-updated': { policy: Record<string, unknown>; id: string };
  /** Fired after a policy validation completes. */
  'policy-validated': { result: Record<string, unknown> };
  /** Fired when the user clicks Cancel. */
  'editor-cancelled': Record<string, never>;
}

/** Callback signature for dispatching custom events to the host. */
export type OnEventCallback = <K extends keyof EmbeddedEventMap>(
  type: K,
  detail: EmbeddedEventMap[K],
) => void;

/** Configuration supplied by the Web Component wrapper. */
export interface EmbeddedConfig {
  /** Base URL for all PAP API calls (e.g., "https://pap.example.com"). */
  apiBaseUrl: string;
  /** Optional Bearer token injected into every API request. */
  authToken: string | null;
  /** Whether to create a new policy or edit an existing one. */
  mode: EditorMode;
  /** Policy ID to load when `mode` is "edit". */
  policyId: string | null;
  /** UI locale (e.g., "en", "de"). */
  locale: string;
  /** Theme preset name. */
  theme: EmbeddedThemePreset;
  /** Callback to emit Custom Events on the host element. */
  onEvent: OnEventCallback;
  /**
   * Custom JSON-LD `@context` for new policies.
   *
   * When set, overrides the built-in default context
   * (`{ "odrl": "http://www.w3.org/ns/odrl/2/" }`).
   * Accepts an object (namespaced map) or a string (context URI).
   */
  policyContext?: Record<string, string> | string | null;
}

/** Full context value including an `isEmbedded` flag. */
export interface EmbeddedContextValue extends EmbeddedConfig {
  /** True when running inside the Web Component wrapper. */
  isEmbedded: true;
}

/** Sentinel value for standalone (non-embedded) mode. */
interface StandaloneContextValue {
  isEmbedded: false;
}

/** Union type for the context value. */
type ContextValue = EmbeddedContextValue | StandaloneContextValue;

/** Default context: not embedded. */
const defaultValue: StandaloneContextValue = { isEmbedded: false };

/**
 * React context that carries embedded-mode configuration.
 *
 * Outside of a `<odrl-policy-editor>` custom element the context
 * value is `{ isEmbedded: false }`, so components fall back to
 * their normal standalone behavior.
 */
// eslint-disable-next-line react-refresh/only-export-components
export const EmbeddedContext = createContext<ContextValue>(defaultValue);

/** Props for {@link EmbeddedProvider}. */
interface EmbeddedProviderProps {
  /** Configuration forwarded from the Custom Element wrapper. */
  config: EmbeddedConfig;
  children: ReactNode;
}

/**
 * Wraps the embedded React tree with configuration from the Web
 * Component host. All children can call `useEmbeddedContext()` to
 * read the injected config.
 */
export const EmbeddedProvider = ({ config, children }: EmbeddedProviderProps) => {
  const value: EmbeddedContextValue = { ...config, isEmbedded: true };
  return (
    <EmbeddedContext.Provider value={value}>
      {children}
    </EmbeddedContext.Provider>
  );
};

/**
 * Hook to access embedded-mode configuration.
 *
 * @returns The context value. Check `isEmbedded` before accessing
 *   config fields.
 *
 * @example
 * ```tsx
 * const ctx = useEmbeddedContext();
 * if (ctx.isEmbedded) {
 *   console.log(ctx.apiBaseUrl);
 * }
 * ```
 */
// eslint-disable-next-line react-refresh/only-export-components
export const useEmbeddedContext = (): ContextValue =>
  useContext(EmbeddedContext);
