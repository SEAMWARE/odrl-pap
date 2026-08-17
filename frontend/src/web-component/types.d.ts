/**
 * Public TypeScript declarations for `@seamware/odrl-policy-editor`.
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
// Tab visibility
// ---------------------------------------------------------------------------

/**
 * Controls the visibility of individual editor tabs in the web component.
 *
 * Each property, when `true`, hides the corresponding tab.
 * By default all tabs are visible (all values are `false`).
 */
export interface TabVisibility {
  /** When `true`, hides the visual policy builder tab. */
  hideBuilderTab: boolean;
  /** When `true`, hides the raw ODRL JSON editor tab. */
  hideRawTab: boolean;
  /** When `true`, hides the template selection tab. */
  hideTemplateTab: boolean;
  /** When `true`, hides the template creation/management tab. */
  hideTemplateCreateTab: boolean;
}

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
  /** Fired after a new template is created via the template editor. */
  'template-created': { template: Record<string, unknown>; id: string };
  /** Fired after an existing template is updated via the template editor. */
  'template-updated': { template: Record<string, unknown>; id: string };
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
  /**
   * Optional service ID to scope policy operations under a specific service.
   *
   * When set, the editor uses service-scoped API endpoints
   * (e.g., `POST /service/{serviceId}/policy`) instead of root-level
   * endpoints (`POST /policy`).
   */
  serviceId: string | null;
  /**
   * Controls which editor tabs are hidden.
   *
   * Each property, when `true`, hides the corresponding tab.
   * Defaults to all tabs visible.
   */
  hiddenTabs: TabVisibility;
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
 * | `service-id`     | Service ID for service-scoped policy ops       | `null`     |
 * | `hide-builder-tab`         | Boolean — hides the visual policy builder tab   | absent |
 * | `hide-raw-tab`             | Boolean — hides the raw ODRL JSON editor tab    | absent |
 * | `hide-template-tab`        | Boolean — hides the template selection tab      | absent |
 * | `hide-template-create-tab` | Boolean — hides the template management tab     | absent |
 *
 * ## JS Properties
 *
 * - `i18nStrings` — partial i18n override object (deep-merged with defaults)
 * - `themeConfig` — partial ThemeConfig override merged on top of the preset
 * - `fieldTemplate` — a FieldTemplate object to pre-fill and constrain the editor
 * - `policyContext` — custom JSON-LD `@context` object/string for new policies
 * - `serviceId` — service ID string for service-scoped policy operations
 *
 * ## Custom Events
 *
 * | Event              | Detail                                      |
 * |--------------------|---------------------------------------------|
 * | `policy-created`   | `{ policy: object, id: string }`            |
 * | `policy-updated`   | `{ policy: object, id: string }`            |
 * | `policy-validated` | `{ result: object }`                        |
 * | `editor-cancelled` | `{}`                                        |
 * | `template-created` | `{ template: object, id: string }`          |
 * | `template-updated` | `{ template: object, id: string }`          |
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
   * Field template to pre-fill and constrain the builder form.
   *
   * A client-side skeleton with editable/locked fields — distinct from the
   * stored, placeholder-based templates managed through the template tabs.
   *
   * @example
   * ```js
   * editor.fieldTemplate = {
   *   id: 'dome-access',
   *   name: 'DOME Marketplace Access',
   *   skeleton: { '@context': '...', '@type': 'odrl:Agreement', ... },
   *   editableFields: [{ path: 'odrl:permission.odrl:target', ... }],
   *   lockedFields: ['odrl:permission.odrl:action'],
   * };
   * ```
   */
  fieldTemplate: Record<string, unknown> | undefined;

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

  /**
   * Service ID for service-scoped policy operations.
   *
   * When set, the editor uses service-scoped API endpoints instead of
   * root-level endpoints. The JS property takes precedence over the
   * `service-id` HTML attribute.
   *
   * @example
   * ```js
   * editor.serviceId = 'my-service';
   * ```
   */
  serviceId: string | undefined;
}

// ---------------------------------------------------------------------------
// HTMLElementTagNameMap augmentation
// ---------------------------------------------------------------------------

declare global {
  interface HTMLElementTagNameMap {
    'odrl-policy-editor': OdrlPolicyEditorElement;
  }
}
