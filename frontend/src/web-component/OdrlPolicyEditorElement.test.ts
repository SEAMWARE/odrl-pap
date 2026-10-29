/**
 * Unit tests for the `<odrl-policy-editor>` custom element.
 *
 * Tests cover:
 * - Custom element registration and instantiation
 * - Shadow DOM creation
 * - Observed attribute propagation
 * - Custom event dispatching
 * - JS property setters (i18nStrings, themeConfig)
 * - Lifecycle callbacks (connect, disconnect, attribute change)
 */
import { describe, it, expect, vi, beforeAll, afterEach } from 'vitest';
import { TAG_NAME, OdrlPolicyEditorElement } from './OdrlPolicyEditorElement';

// ---------------------------------------------------------------
// Mock React rendering to avoid full React bootstrap in unit tests.
// We only test the Custom Element shell, not the React tree.
// ---------------------------------------------------------------
const mockRender = vi.fn();
const mockUnmount = vi.fn();

vi.mock('react-dom/client', () => ({
  createRoot: vi.fn(() => ({
    render: mockRender,
    unmount: mockUnmount,
  })),
}));

vi.mock('./EmbeddedApp', () => ({
  default: vi.fn(() => null),
}));

// Mock the shared API client configuration so we can assert it is invoked
// synchronously on connect — before the async EmbeddedApp import resolves.
const mockConfigureApi = vi.fn();
vi.mock('../services/api', () => ({
  configureApi: (...args: unknown[]) => mockConfigureApi(...args),
}));

// Mock CSS imports to avoid Vite-specific `?inline` handling in tests.
// Include `:root`/`body` selectors so the shadow-DOM rescoping can be asserted.
vi.mock('bootstrap/dist/css/bootstrap.min.css?inline', () => ({
  default: ':root{--bs-body-bg:#fff;}/* bootstrap-mock */',
}));

vi.mock('../theme/theme.css?inline', () => ({
  default: ':root{--odrl-bg-color:#FFFFFF;}body{color:var(--odrl-text-color);}/* theme-mock */',
}));

describe('OdrlPolicyEditorElement', () => {
  beforeAll(() => {
    // Register the element if not already registered
    if (!customElements.get(TAG_NAME)) {
      customElements.define(TAG_NAME, OdrlPolicyEditorElement);
    }
  });

  afterEach(() => {
    // Remove any created elements from the DOM
    document.querySelectorAll(TAG_NAME).forEach((el) => el.remove());
    vi.clearAllMocks();
  });

  // -----------------------------------------------------------
  // Registration
  // -----------------------------------------------------------

  it('registers with the correct tag name', () => {
    expect(TAG_NAME).toBe('odrl-policy-editor');
    expect(customElements.get(TAG_NAME)).toBe(OdrlPolicyEditorElement);
  });

  it('can be instantiated via document.createElement', () => {
    const el = document.createElement(TAG_NAME);
    expect(el).toBeInstanceOf(OdrlPolicyEditorElement);
  });

  // -----------------------------------------------------------
  // Shadow DOM
  // -----------------------------------------------------------

  it('creates a shadow root when connected', () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);
    expect(el.shadowRoot).not.toBeNull();
    expect(el.shadowRoot!.mode).toBe('open');
  });

  it('injects a <style> element into the shadow root', () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);
    const styles = el.shadowRoot!.querySelectorAll('style');
    expect(styles.length).toBeGreaterThanOrEqual(1);
    // Style should contain our mocked CSS
    expect(styles[0].textContent).toContain('bootstrap-mock');
    expect(styles[0].textContent).toContain('theme-mock');
  });

  it('rescopes :root to :host so custom properties resolve inside the shadow tree', () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);
    const css = el.shadowRoot!.querySelector('style')!.textContent!;
    // :root would not match inside a shadow tree; it must be rescoped to :host.
    expect(css).not.toMatch(/:root\b/);
    expect(css).toContain(':host{--bs-body-bg:#fff;}');
    expect(css).toContain(':host{--odrl-bg-color:#FFFFFF;}');
    // A base surface rule is added for the mount container.
    expect(css).toContain('.odrl-wc-container');
  });

  it('creates a container div inside the shadow root', () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);
    const container = el.shadowRoot!.querySelector('.odrl-wc-container');
    expect(container).not.toBeNull();
    expect(container!.tagName).toBe('DIV');
  });

  // -----------------------------------------------------------
  // React mounting
  // -----------------------------------------------------------

  it('creates a React root and renders on connect', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);

    // Wait for the dynamic import in renderReact()
    await vi.dynamicImportSettled();

    expect(mockRender).toHaveBeenCalled();
  });

  it('configures the API client synchronously before React renders', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    el.setAttribute('api-base-url', 'https://pap.example.com');
    el.setAttribute('auth-token', 'tok-123');
    document.body.appendChild(el);

    // The API base URL must be set before any data fetch. React runs child
    // effects before parent effects, so the mappings fetch in a descendant
    // would otherwise race ahead of EmbeddedApp's own configuration effect.
    // configureApi therefore runs synchronously on connect — before the
    // dynamic EmbeddedApp import resolves and React renders.
    expect(mockConfigureApi).toHaveBeenCalledWith('https://pap.example.com', 'tok-123');
    expect(mockRender).not.toHaveBeenCalled();

    await vi.dynamicImportSettled();
    expect(mockRender).toHaveBeenCalled();
  });

  it('unmounts React root on disconnect', () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);
    el.remove();

    expect(mockUnmount).toHaveBeenCalled();
  });

  // -----------------------------------------------------------
  // Observed attributes
  // -----------------------------------------------------------

  it('declares the correct observed attributes', () => {
    expect(OdrlPolicyEditorElement.observedAttributes).toEqual([
      'api-base-url',
      'auth-token',
      'mode',
      'policy-id',
      'theme',
      'locale',
      'policy-context',
      'service-id',
      'hide-builder-tab',
      'hide-raw-tab',
      'hide-template-tab',
      'hide-template-create-tab',
      'summary',
      'hide-cancel-button',
      'load',
      'save',
      'validate',
      'templates',
      'mappings',
    ]);
  });

  it('includes service-id in observed attributes', () => {
    expect(OdrlPolicyEditorElement.observedAttributes).toContain('service-id');
  });

  it('re-renders when an observed attribute changes', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);

    // Wait for initial render
    await vi.dynamicImportSettled();
    mockRender.mockClear();

    // Change an observed attribute
    el.setAttribute('api-base-url', 'https://new-api.example.com');

    // Wait for the re-render triggered by attributeChangedCallback
    await vi.dynamicImportSettled();

    expect(mockRender).toHaveBeenCalled();
  });

  it('reads attribute values correctly', () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    el.setAttribute('api-base-url', 'https://pap.test');
    el.setAttribute('auth-token', 'tok123');
    el.setAttribute('mode', 'edit');
    el.setAttribute('policy-id', 'pol-42');
    el.setAttribute('theme', 'dark');
    el.setAttribute('locale', 'de');

    expect(el.getAttribute('api-base-url')).toBe('https://pap.test');
    expect(el.getAttribute('auth-token')).toBe('tok123');
    expect(el.getAttribute('mode')).toBe('edit');
    expect(el.getAttribute('policy-id')).toBe('pol-42');
    expect(el.getAttribute('theme')).toBe('dark');
    expect(el.getAttribute('locale')).toBe('de');
  });

  // -----------------------------------------------------------
  // JS properties
  // -----------------------------------------------------------

  it('accepts i18nStrings via JS property', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);

    await vi.dynamicImportSettled();
    mockRender.mockClear();

    const overrides = { policyBuilder: { title: 'Test Title' } };
    el.i18nStrings = overrides;
    expect(el.i18nStrings).toBe(overrides);

    // Should trigger a re-render
    await vi.dynamicImportSettled();
    expect(mockRender).toHaveBeenCalled();
  });

  it('accepts themeConfig via JS property', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);

    await vi.dynamicImportSettled();
    mockRender.mockClear();

    const themeOverrides = { 'odrl-primary-color': '#ff0000' };
    el.themeConfig = themeOverrides;
    expect(el.themeConfig).toBe(themeOverrides);

    await vi.dynamicImportSettled();
    expect(mockRender).toHaveBeenCalled();
  });

  // -----------------------------------------------------------
  // Custom Events
  // -----------------------------------------------------------

  it('dispatches custom events that bubble and are composed', () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);

    const handler = vi.fn();
    el.addEventListener('policy-created', handler);

    // Simulate the event dispatch that would come from EmbeddedApp
    el.dispatchEvent(
      new CustomEvent('policy-created', {
        detail: { policy: { '@type': 'test' }, id: 'pol-1' },
        bubbles: true,
        composed: true,
      }),
    );

    expect(handler).toHaveBeenCalledTimes(1);
    const event = handler.mock.calls[0][0] as CustomEvent;
    expect(event.detail.id).toBe('pol-1');
    expect(event.bubbles).toBe(true);
    expect(event.composed).toBe(true);
  });

  it('dispatches events that cross shadow DOM boundary', () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);

    // Listen on the host element (outside shadow DOM)
    const handler = vi.fn();
    el.addEventListener('policy-validated', handler);

    // Dispatch from inside
    el.dispatchEvent(
      new CustomEvent('policy-validated', {
        detail: { result: { decision: true } },
        bubbles: true,
        composed: true,
      }),
    );

    expect(handler).toHaveBeenCalledTimes(1);
  });

  // -----------------------------------------------------------
  // Default attribute values
  // -----------------------------------------------------------

  it('uses default values when attributes are not set', () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    // Without setting any attributes, defaults should apply
    expect(el.getAttribute('api-base-url')).toBeNull();
    expect(el.getAttribute('mode')).toBeNull();
    expect(el.getAttribute('theme')).toBeNull();
    expect(el.getAttribute('locale')).toBeNull();
  });

  // -----------------------------------------------------------
  // Multiple instances
  // -----------------------------------------------------------

  it('supports multiple instances on the same page', () => {
    const el1 = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    const el2 = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    el1.setAttribute('api-base-url', 'https://api1.example.com');
    el2.setAttribute('api-base-url', 'https://api2.example.com');

    document.body.appendChild(el1);
    document.body.appendChild(el2);

    expect(el1.shadowRoot).not.toBe(el2.shadowRoot);
    expect(el1.getAttribute('api-base-url')).toBe('https://api1.example.com');
    expect(el2.getAttribute('api-base-url')).toBe('https://api2.example.com');
  });

  // -----------------------------------------------------------
  // Template property
  // -----------------------------------------------------------

  it('accepts template via JS property', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);

    await vi.dynamicImportSettled();
    mockRender.mockClear();

    const template = {
      id: 'test-tmpl',
      name: 'Test Template',
      description: 'A test template',
      category: 'Testing',
      skeleton: { '@context': 'http://www.w3.org/ns/odrl/2/' },
      editableFields: [],
      lockedFields: [],
    };
    el.fieldTemplate = template;
    expect(el.fieldTemplate).toBe(template);

    // Should trigger a re-render
    await vi.dynamicImportSettled();
    expect(mockRender).toHaveBeenCalled();
  });

  it('returns undefined for template when none is set', () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    expect(el.fieldTemplate).toBeUndefined();
  });

  it('allows clearing the template by setting undefined', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);

    await vi.dynamicImportSettled();

    const template = {
      id: 'test-tmpl',
      name: 'Test',
      description: '',
      category: 'Test',
      skeleton: {},
      editableFields: [],
      lockedFields: [],
    };
    el.fieldTemplate = template;
    expect(el.fieldTemplate).toBe(template);

    el.fieldTemplate = undefined;
    expect(el.fieldTemplate).toBeUndefined();
  });

  // -----------------------------------------------------------
  // Service ID property
  // -----------------------------------------------------------

  it('accepts serviceId via JS property and triggers re-render', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);

    await vi.dynamicImportSettled();
    mockRender.mockClear();

    el.serviceId = 'my-service';
    expect(el.serviceId).toBe('my-service');

    // Should trigger a re-render
    await vi.dynamicImportSettled();
    expect(mockRender).toHaveBeenCalled();
  });

  it('returns undefined for serviceId when none is set', () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    expect(el.serviceId).toBeUndefined();
  });

  it('allows clearing serviceId by setting undefined', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);

    await vi.dynamicImportSettled();

    el.serviceId = 'test-service';
    expect(el.serviceId).toBe('test-service');

    el.serviceId = undefined;
    expect(el.serviceId).toBeUndefined();
  });

  it('reads service-id from HTML attribute', () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    el.setAttribute('service-id', 'attr-service');
    expect(el.getAttribute('service-id')).toBe('attr-service');
  });

  it('re-renders when service-id attribute changes', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);

    await vi.dynamicImportSettled();
    mockRender.mockClear();

    el.setAttribute('service-id', 'new-service');

    await vi.dynamicImportSettled();
    expect(mockRender).toHaveBeenCalled();
  });

  // -----------------------------------------------------------
  // Boolean hide-* attributes
  // -----------------------------------------------------------

  /** Reads the `hiddenTabs` config from the most recent React render. */
  const lastHiddenTabs = () => {
    const element = mockRender.mock.calls.at(-1)![0] as { props: { config: { hiddenTabs: Record<string, boolean> } } };
    return element.props.config.hiddenTabs;
  };

  it('treats a bare hide-*-tab attribute as true', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    el.setAttribute('hide-template-tab', '');
    document.body.appendChild(el);

    await vi.dynamicImportSettled();
    expect(lastHiddenTabs().hideTemplateTab).toBe(true);
  });

  it('treats hide-*-tab="false" and "0" as false (framework falsy bindings)', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    el.setAttribute('hide-template-tab', 'false');
    el.setAttribute('hide-builder-tab', '0');
    document.body.appendChild(el);

    await vi.dynamicImportSettled();
    const hidden = lastHiddenTabs();
    expect(hidden.hideTemplateTab).toBe(false);
    expect(hidden.hideBuilderTab).toBe(false);
  });

  it('treats hide-*-tab="true" as true', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    el.setAttribute('hide-raw-tab', 'true');
    document.body.appendChild(el);

    await vi.dynamicImportSettled();
    expect(lastHiddenTabs().hideRawTab).toBe(true);
  });

  it('treats an absent hide-*-tab attribute as false', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);

    await vi.dynamicImportSettled();
    const hidden = lastHiddenTabs();
    expect(hidden.hideTemplateTab).toBe(false);
    expect(hidden.hideBuilderTab).toBe(false);
    expect(hidden.hideRawTab).toBe(false);
    expect(hidden.hideTemplateCreateTab).toBe(false);
  });

  // -----------------------------------------------------------
  // Capabilities, policy and mappings
  // -----------------------------------------------------------
  const mockRenderConfig = (_el: unknown) =>
    (mockRender.mock.calls.at(-1)?.[0] as { props: { config: unknown } }).props.config;
  type RenderedProps = {
    config: { capabilities: Record<string, string> };
    policy?: Record<string, unknown>;
    mappings?: Record<string, unknown>;
  };
  const lastProps = () =>
    (mockRender.mock.calls.at(-1)![0] as { props: RenderedProps }).props;

  it('observes the capability attributes', () => {
    for (const name of ['load', 'save', 'validate', 'templates', 'mappings']) {
      expect(OdrlPolicyEditorElement.observedAttributes).toContain(name);
    }
  });

  it('serves every capability remotely by default', async () => {
    document.body.appendChild(document.createElement(TAG_NAME));

    await vi.dynamicImportSettled();
    expect(lastProps().config.capabilities).toEqual({
      load: 'remote', save: 'remote', validate: 'remote', templates: 'remote', mappings: 'remote',
    });
  });

  it('reads each capability independently from its attribute', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    el.setAttribute('save', 'none');
    el.setAttribute('validate', 'none');
    el.setAttribute('templates', 'bogus');
    document.body.appendChild(el);

    await vi.dynamicImportSettled();
    expect(lastProps().config.capabilities).toMatchObject({
      save: 'none', validate: 'none', templates: 'remote', load: 'remote', mappings: 'remote',
    });
  });

  it('passes the policy property to the editor and re-renders', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);
    await vi.dynamicImportSettled();
    mockRender.mockClear();

    const policy = { '@type': 'odrl:Policy' };
    el.policy = policy as never;

    await vi.dynamicImportSettled();
    expect(lastProps().policy).toBe(policy);
    expect(el.policy).toBe(policy);
  });

  it('parses a JSON string assigned to policy and ignores invalid text', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    const warn = vi.spyOn(console, 'warn').mockImplementation(() => {});
    document.body.appendChild(el);

    el.policy = '{"@type":"odrl:Policy"}';
    expect(el.policy).toEqual({ '@type': 'odrl:Policy' });

    el.policy = '{not json';
    expect(el.policy).toEqual({ '@type': 'odrl:Policy' });
    expect(warn).toHaveBeenCalled();
    warn.mockRestore();
  });

  it('keeps the policy property in sync with policy-changed without re-rendering', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);
    await vi.dynamicImportSettled();
    const { onEvent } = mockRenderConfig(el) as { onEvent: (t: string, d: unknown) => void };
    mockRender.mockClear();

    const onChange = vi.fn();
    el.addEventListener('policy-changed', onChange);
    const edited = { '@type': 'odrl:Agreement' };
    onEvent('policy-changed', { policy: edited });

    expect(onChange).toHaveBeenCalledTimes(1);
    expect(el.policy).toBe(edited);
    expect(mockRender).not.toHaveBeenCalled();
  });

  it('passes the mappings property to the editor', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);
    await vi.dynamicImportSettled();

    const mappings = { actions: [{ value: 'odrl:use', label: 'Use' }] };
    el.mappings = mappings as never;

    await vi.dynamicImportSettled();
    expect(lastProps().mappings).toBe(mappings);
  });

  // -----------------------------------------------------------
  // Summary placement, cancel button, stacking
  // -----------------------------------------------------------
  type LayoutConfig = { summaryPlacement: string; hiddenButtons: { hideCancelButton: boolean } };
  const lastLayout = () =>
    (mockRender.mock.calls.at(-1)![0] as { props: { config: LayoutConfig } }).props.config;

  it('places the summary at the side by default', async () => {
    document.body.appendChild(document.createElement(TAG_NAME));

    await vi.dynamicImportSettled();
    expect(lastLayout().summaryPlacement).toBe('side');
  });

  it('reads the summary placement and falls back to side for unknown values', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    el.setAttribute('summary', ' TAB ');
    document.body.appendChild(el);
    await vi.dynamicImportSettled();
    expect(lastLayout().summaryPlacement).toBe('tab');

    el.setAttribute('summary', 'none');
    await vi.dynamicImportSettled();
    expect(lastLayout().summaryPlacement).toBe('none');

    el.setAttribute('summary', 'bottom');
    await vi.dynamicImportSettled();
    expect(lastLayout().summaryPlacement).toBe('side');
  });

  it('reads hide-cancel-button as a boolean attribute', async () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);
    await vi.dynamicImportSettled();
    expect(lastLayout().hiddenButtons.hideCancelButton).toBe(false);

    el.setAttribute('hide-cancel-button', '');
    await vi.dynamicImportSettled();
    expect(lastLayout().hiddenButtons.hideCancelButton).toBe(true);

    el.setAttribute('hide-cancel-button', 'false');
    await vi.dynamicImportSettled();
    expect(lastLayout().hiddenButtons.hideCancelButton).toBe(false);
  });

  it('isolates its stacking context so internal z-indexes cannot cover the host page', () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    document.body.appendChild(el);

    const css = el.shadowRoot!.querySelector('style')!.textContent!;
    expect(css).toMatch(/:host\s*\{[^}]*isolation:\s*isolate/);
  });
});
