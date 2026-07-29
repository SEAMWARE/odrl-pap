/**
 * Custom Element definition for `<odrl-policy-editor>`.
 *
 * Wraps the React-based policy editor inside a Web Component with
 * Shadow DOM for style isolation. The host page communicates via
 * HTML attributes and Custom Events — no React knowledge required.
 *
 * ## Observed attributes
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
 * ## Custom Events
 *
 * | Event              | Detail                                      |
 * |--------------------|---------------------------------------------|
 * | `policy-created`   | `{ policy: OdrlPolicyJson, id: string }`    |
 * | `policy-updated`   | `{ policy: OdrlPolicyJson, id: string }`    |
 * | `policy-validated` | `{ result: ValidationResponse }`            |
 * | `editor-cancelled` | `{}`                                        |
 *
 * ## JS Properties
 *
 * - `i18nStrings` — partial i18n override object (deep-merged with defaults)
 * - `themeConfig` — partial ThemeConfig override merged on top of the preset
 * - `template` — a {@link PolicyTemplate} object to pre-fill and constrain the editor
 * - `policyContext` — custom JSON-LD `@context` object/string for new policies
 *
 * @example
 * ```html
 * <odrl-policy-editor
 *   api-base-url="https://pap.example.com"
 *   auth-token="eyJhbG..."
 *   mode="create"
 *   theme="light"
 *   locale="en"
 * ></odrl-policy-editor>
 *
 * <script>
 *   const editor = document.querySelector('odrl-policy-editor');
 *   editor.addEventListener('policy-created', (e) => {
 *     console.log('Policy created:', e.detail);
 *   });
 * </script>
 * ```
 */
import { createElement, createRef, type RefObject } from 'react';
import { createRoot, type Root } from 'react-dom/client';
import type { EmbeddedConfig, EditorMode, EmbeddedThemePreset } from './EmbeddedContext';
import type { DeepPartial } from '../i18n/I18nContext';
import type { I18nStrings } from '../i18n/en';
import type { ThemeConfig } from '../theme/defaultTheme';
import type { PolicyTemplate } from '../types';

// Bootstrap CSS imported as a string for shadow-DOM injection.
// The `?inline` suffix tells Vite to return the CSS text, not inject it
// into the document head.
import bootstrapCss from 'bootstrap/dist/css/bootstrap.min.css?inline';
import themeCss from '../theme/theme.css?inline';

/** Tag name registered in the Custom Elements registry. */
export const TAG_NAME = 'odrl-policy-editor';

/** Default attribute values. */
const DEFAULT_API_BASE_URL = '';
const DEFAULT_MODE: EditorMode = 'create';
const DEFAULT_THEME: EmbeddedThemePreset = 'light';
const DEFAULT_LOCALE = 'en';

/**
 * `<odrl-policy-editor>` custom element.
 *
 * Lifecycle:
 * 1. `connectedCallback` — creates shadow root, injects CSS, mounts React.
 * 2. `attributeChangedCallback` — re-renders React with updated props.
 * 3. `disconnectedCallback` — unmounts React root.
 */
export class OdrlPolicyEditorElement extends HTMLElement {
  /** Attributes that trigger re-renders when changed. */
  static get observedAttributes(): string[] {
    return ['api-base-url', 'auth-token', 'mode', 'policy-id', 'theme', 'locale', 'policy-context'];
  }

  /** React root instance (created on connect, destroyed on disconnect). */
  private reactRoot: Root | null = null;

  /** Container div inside the shadow root where React mounts. */
  private container: HTMLDivElement | null = null;

  /** React ref to the container for theme CSS custom-property injection. */
  private containerRef: RefObject<HTMLDivElement | null> = createRef();

  /** Partial i18n overrides set via JS property. */
  private _i18nStrings: DeepPartial<I18nStrings> | undefined;

  /** Partial theme overrides set via JS property. */
  private _themeConfig: Partial<ThemeConfig> | undefined;

  /** Policy template set via JS property. */
  private _template: PolicyTemplate | undefined;

  /** Custom JSON-LD `@context` set via JS property. */
  private _policyContext: Record<string, string> | string | undefined;

  /**
   * Sets partial i18n string overrides (JS property, not HTML attribute).
   *
   * @example
   * ```js
   * document.querySelector('odrl-policy-editor').i18nStrings = {
   *   policyBuilder: { title: 'Richtlinien-Editor' },
   * };
   * ```
   */
  set i18nStrings(value: DeepPartial<I18nStrings> | undefined) {
    this._i18nStrings = value;
    this.renderReact();
  }

  /** Returns the current i18n overrides. */
  get i18nStrings(): DeepPartial<I18nStrings> | undefined {
    return this._i18nStrings;
  }

  /**
   * Sets partial theme config overrides (JS property, not HTML attribute).
   *
   * @example
   * ```js
   * document.querySelector('odrl-policy-editor').themeConfig = {
   *   'odrl-primary-color': '#1a73e8',
   * };
   * ```
   */
  set themeConfig(value: Partial<ThemeConfig> | undefined) {
    this._themeConfig = value;
    this.renderReact();
  }

  /** Returns the current theme overrides. */
  get themeConfig(): Partial<ThemeConfig> | undefined {
    return this._themeConfig;
  }

  /**
   * Sets a policy template (JS property, not HTML attribute).
   *
   * When set, the editor pre-fills the form from the template skeleton
   * and locks fields listed in `lockedFields`.
   *
   * @example
   * ```js
   * const editor = document.querySelector('odrl-policy-editor');
   * editor.template = {
   *   id: 'dome-access',
   *   name: 'DOME Marketplace Access',
   *   description: 'Grants access to a DOME resource',
   *   category: 'DOME',
   *   skeleton: { '@context': 'http://www.w3.org/ns/odrl/2/', ... },
   *   editableFields: [{ path: 'odrl:permission.odrl:target', ... }],
   *   lockedFields: ['odrl:permission.odrl:action'],
   * };
   * ```
   */
  set template(value: PolicyTemplate | undefined) {
    this._template = value;
    this.renderReact();
  }

  /** Returns the current policy template, or `undefined` if none is set. */
  get template(): PolicyTemplate | undefined {
    return this._template;
  }

  /**
   * Sets a custom JSON-LD `@context` for new policies (JS property).
   *
   * When set, overrides the built-in default context
   * (`{ "odrl": "http://www.w3.org/ns/odrl/2/" }`).
   *
   * Can also be set via the `policy-context` HTML attribute (as a JSON
   * string), but the JS property takes precedence.
   *
   * @example
   * ```js
   * const editor = document.querySelector('odrl-policy-editor');
   * editor.policyContext = {
   *   odrl: 'http://www.w3.org/ns/odrl/2/',
   *   'dome-op': 'https://github.com/DOME-Marketplace/dome-odrl-profile#',
   * };
   * ```
   */
  set policyContext(value: Record<string, string> | string | undefined) {
    this._policyContext = value;
    this.renderReact();
  }

  /** Returns the current custom policy context, or `undefined` if using the default. */
  get policyContext(): Record<string, string> | string | undefined {
    return this._policyContext;
  }

  // ------------------------------------------------------------------
  // Lifecycle
  // ------------------------------------------------------------------

  /** Called when the element is inserted into the DOM. */
  connectedCallback(): void {
    // Create shadow root if not already present
    if (!this.shadowRoot) {
      this.attachShadow({ mode: 'open' });
    }

    // Inject Bootstrap + theme CSS into the shadow root
    const style = document.createElement('style');
    style.textContent = `${bootstrapCss}\n${themeCss}`;
    this.shadowRoot!.appendChild(style);

    // Create the React mount container
    this.container = document.createElement('div');
    this.container.setAttribute('class', 'odrl-wc-container');
    this.shadowRoot!.appendChild(this.container);

    // Create React root and render
    this.reactRoot = createRoot(this.container);
    this.renderReact();
  }

  /** Called when the element is removed from the DOM. */
  disconnectedCallback(): void {
    if (this.reactRoot) {
      this.reactRoot.unmount();
      this.reactRoot = null;
    }
    this.container = null;
  }

  /** Called when an observed attribute changes. Re-renders with updated props. */
  attributeChangedCallback(): void {
    this.renderReact();
  }

  // ------------------------------------------------------------------
  // Internal helpers
  // ------------------------------------------------------------------

  /**
   * Resolves the policy context from the JS property or the HTML attribute.
   *
   * The JS property (`_policyContext`) takes precedence. If not set,
   * falls back to parsing the `policy-context` HTML attribute as JSON.
   *
   * @returns The resolved context, or `undefined` to use the default.
   */
  private resolvePolicyContextAttr(): Record<string, string> | string | undefined {
    if (this._policyContext !== undefined) {
      return this._policyContext;
    }
    const attr = this.getAttribute('policy-context');
    if (attr) {
      try {
        return JSON.parse(attr) as Record<string, string>;
      } catch {
        return attr;
      }
    }
    return undefined;
  }

  /** Builds the {@link EmbeddedConfig} from the current attribute values. */
  private buildConfig(): EmbeddedConfig {
    const policyContext = this.resolvePolicyContextAttr();
    return {
      apiBaseUrl: this.getAttribute('api-base-url') ?? DEFAULT_API_BASE_URL,
      authToken: this.getAttribute('auth-token'),
      mode: (this.getAttribute('mode') as EditorMode) ?? DEFAULT_MODE,
      policyId: this.getAttribute('policy-id'),
      locale: this.getAttribute('locale') ?? DEFAULT_LOCALE,
      theme: (this.getAttribute('theme') as EmbeddedThemePreset) ?? DEFAULT_THEME,
      onEvent: this.dispatchComponentEvent.bind(this),
      ...(policyContext !== undefined && { policyContext }),
    };
  }

  /**
   * Dispatches a Custom Event on this element so the host page can listen.
   *
   * Events bubble and are composed (cross shadow-DOM boundary).
   */
  private dispatchComponentEvent<K extends string>(
    type: K,
    detail: unknown,
  ): void {
    this.dispatchEvent(
      new CustomEvent(type, {
        detail,
        bubbles: true,
        composed: true,
      }),
    );
  }

  /**
   * (Re-)renders the React tree inside the shadow-DOM container.
   *
   * Safe to call before the React root exists (e.g., during
   * `attributeChangedCallback` before `connectedCallback`).
   */
  private renderReact(): void {
    if (!this.reactRoot || !this.container) return;

    // Assign the ref to the container so EmbeddedApp can apply theme vars
    (this.containerRef as { current: HTMLDivElement | null }).current =
      this.container;

    // Lazy-import EmbeddedApp to avoid circular dependency at module parse time
    // and to allow tree-shaking in the standalone app build.
    import('./EmbeddedApp').then(({ default: EmbeddedApp }) => {
      this.reactRoot?.render(
        createElement(EmbeddedApp, {
          config: this.buildConfig(),
          i18nOverrides: this._i18nStrings,
          themeOverrides: this._themeConfig,
          containerRef: this.containerRef,
          template: this._template,
        }),
      );
    });
  }
}
