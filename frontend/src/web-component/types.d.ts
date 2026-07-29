/**
 * Public TypeScript declarations for `@fiware/odrl-policy-editor`.
 *
 * This hand-crafted declaration file is copied into `dist-component/`
 * during the `build:component` script so npm consumers get type-checking
 * out of the box.
 *
 * @packageDocumentation
 */

// ---------------------------------------------------------------------------
// Editor mode & theme preset
// ---------------------------------------------------------------------------

/** Editor operating mode: create a new policy or edit an existing one. */
export type EditorMode = 'create' | 'edit';

/** Theme preset names supported by the embedded component. */
export type EmbeddedThemePreset = 'light' | 'dark';

// ---------------------------------------------------------------------------
// Event map
// ---------------------------------------------------------------------------

/**
 * Map of custom event names to their `detail` payload types.
 *
 * The Web Component host listens for these events on the custom element:
 *
 * ```js
 * editor.addEventListener('policy-created', (e) => {
 *   console.log(e.detail.id, e.detail.policy);
 * });
 * ```
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

// ---------------------------------------------------------------------------
// Configuration
// ---------------------------------------------------------------------------

/** Configuration supplied by the Web Component wrapper. */
export interface EmbeddedConfig {
  /** Base URL for all PAP API calls (e.g., `"https://pap.example.com"`). */
  apiBaseUrl: string;
  /** Optional Bearer token injected into every API request. */
  authToken: string | null;
  /** Whether to create a new policy or edit an existing one. */
  mode: EditorMode;
  /** Policy ID to load when `mode` is `"edit"`. */
  policyId: string | null;
  /** UI locale (e.g., `"en"`, `"de"`). */
  locale: string;
  /** Theme preset name. */
  theme: EmbeddedThemePreset;
  /** Callback to emit Custom Events on the host element. */
  onEvent: OnEventCallback;
  /**
   * Custom JSON-LD `@context` for new policies.
   *
   * Accepts an object (namespaced map) or a string (context URI).
   * When set, overrides the built-in default context.
   */
  policyContext?: Record<string, string> | string | null;
}

// ---------------------------------------------------------------------------
// Custom Element class
// ---------------------------------------------------------------------------

/** Tag name registered in the Custom Elements registry. */
export const TAG_NAME: 'odrl-policy-editor';

/**
 * `<odrl-policy-editor>` custom element class.
 *
 * ## Observed HTML attributes
 *
 * | Attribute        | Description                                   | Default    |
 * |------------------|-----------------------------------------------|------------|
 * | `api-base-url`   | PAP API base URL                              | `""`       |
 * | `auth-token`     | Bearer token for API authentication           | `null`     |
 * | `mode`           | `"create"` or `"edit"`                        | `"create"` |
 * | `policy-id`      | Policy ID to load (when `mode="edit"`)        | `null`     |
 * | `theme`          | `"light"` or `"dark"`                         | `"light"`  |
 * | `locale`         | Language code (e.g., `"en"`, `"de"`)          | `"en"`     |
 * | `policy-context` | JSON-LD `@context` for new policies (JSON)    | `null`     |
 *
 * ## JS Properties
 *
 * - `i18nStrings` — partial i18n override object (deep-merged with defaults)
 * - `themeConfig` — partial ThemeConfig override merged on top of the preset
 * - `template` — a PolicyTemplate object to pre-fill and constrain the editor
 * - `policyContext` — custom JSON-LD `@context` object/string for new policies
 *
 * ## Custom Events
 *
 * | Event              | Detail                                      |
 * |--------------------|---------------------------------------------|
 * | `policy-created`   | `{ policy: object, id: string }`            |
 * | `policy-updated`   | `{ policy: object, id: string }`            |
 * | `policy-validated` | `{ result: object }`                        |
 * | `editor-cancelled` | `{}`                                        |
 */
export declare class OdrlPolicyEditorElement extends HTMLElement {
  /** Attributes that trigger re-renders when changed. */
  static readonly observedAttributes: string[];

  /**
   * Partial i18n string overrides.
   *
   * @example
   * ```js
   * editor.i18nStrings = { policyBuilder: { title: 'Richtlinien-Editor' } };
   * ```
   */
  i18nStrings: Record<string, unknown> | undefined;

  /**
   * Partial theme config overrides.
   *
   * @example
   * ```js
   * editor.themeConfig = { 'odrl-primary-color': '#1a73e8' };
   * ```
   */
  themeConfig: Record<string, string> | undefined;

  /**
   * Policy template to pre-fill and constrain the editor.
   *
   * @example
   * ```js
   * editor.template = {
   *   id: 'dome-access',
   *   name: 'DOME Marketplace Access',
   *   skeleton: { '@context': '...', '@type': 'odrl:Agreement', ... },
   *   editableFields: [{ path: 'odrl:permission.odrl:target', ... }],
   *   lockedFields: ['odrl:permission.odrl:action'],
   * };
   * ```
   */
  template: Record<string, unknown> | undefined;

  /**
   * Custom JSON-LD `@context` for new policies.
   *
   * Overrides the built-in default context. The JS property takes
   * precedence over the `policy-context` HTML attribute.
   *
   * @example
   * ```js
   * editor.policyContext = {
   *   odrl: 'http://www.w3.org/ns/odrl/2/',
   *   'dome-op': 'https://github.com/DOME-Marketplace/dome-odrl-profile#',
   * };
   * ```
   */
  policyContext: Record<string, string> | string | undefined;
}

// ---------------------------------------------------------------------------
// HTMLElementTagNameMap augmentation
// ---------------------------------------------------------------------------

declare global {
  interface HTMLElementTagNameMap {
    'odrl-policy-editor': OdrlPolicyEditorElement;
  }
}
