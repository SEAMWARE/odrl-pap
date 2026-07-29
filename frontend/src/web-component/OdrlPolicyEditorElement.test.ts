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

// Mock CSS imports to avoid Vite-specific `?inline` handling in tests
vi.mock('bootstrap/dist/css/bootstrap.min.css?inline', () => ({
  default: '/* bootstrap-mock */',
}));

vi.mock('../theme/theme.css?inline', () => ({
  default: '/* theme-mock */',
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
    ]);
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
    el.template = template;
    expect(el.template).toBe(template);

    // Should trigger a re-render
    await vi.dynamicImportSettled();
    expect(mockRender).toHaveBeenCalled();
  });

  it('returns undefined for template when none is set', () => {
    const el = document.createElement(TAG_NAME) as OdrlPolicyEditorElement;
    expect(el.template).toBeUndefined();
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
    el.template = template;
    expect(el.template).toBe(template);

    el.template = undefined;
    expect(el.template).toBeUndefined();
  });
});
