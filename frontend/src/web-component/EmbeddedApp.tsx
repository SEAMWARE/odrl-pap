/**
 * Stripped-down React root for embedded (Web Component) mode.
 *
 * Unlike the standalone {@link App}, this component:
 * - Has no router — it renders a single policy editor view.
 * - Receives configuration via {@link EmbeddedProvider} instead of
 *   environment variables or the URL.
 * - Emits Custom Events via the `onEvent` callback instead of
 *   navigating between pages.
 * - Wraps children in `I18nProvider` and applies theme CSS custom
 *   properties on its own container (not `document.documentElement`).
 */
import { useState, useEffect, useCallback, useRef, type RefObject } from 'react';
import { Form, Button, Tabs, Tab, Alert, InputGroup, Badge, CloseButton } from 'react-bootstrap';
import { PolicyService } from '../api/services/PolicyService';
import { UiService } from '../api/services/UiService';
import type { OdrlPolicyJson, ValidationResponse } from '../services/api';
import { configureApi } from '../services/api';
import type { PolicyTemplate } from '../types';
import { TestRequest } from '../api/models/TestRequest';
import type { GenericJsonInput } from '../api/models/GenericJsonInput';
import type { ValidationMode } from '../components/ValidationEditor';
import PolicyBuilder from '../components/PolicyBuilder';
import ValidationEditor from '../components/ValidationEditor';
import ValidationResult from '../components/ValidationResult';
import { I18nProvider, type DeepPartial } from '../i18n';
import type { I18nStrings } from '../i18n';
import { lightTheme, darkTheme, type ThemeConfig } from '../theme/defaultTheme';
import {
  EmbeddedProvider,
  type EmbeddedConfig,
  type EmbeddedThemePreset,
} from './EmbeddedContext';
import { createNewPolicy } from '../constants/policyDefaults';

/** Default test request values for validation. */
const DEFAULT_TEST_REQUEST: TestRequest = {
  method: TestRequest.method.GET,
  host: 'example.com',
  path: '/',
  headers: { 'content-type': 'application/json' },
  body: {},
};

/** Default JSON input for JSON payload validation mode. */
const DEFAULT_JSON_INPUT: GenericJsonInput = {
  payload: {},
};

/** Resolves a theme preset name to a {@link ThemeConfig} object. */
function resolveThemePreset(preset: EmbeddedThemePreset): ThemeConfig {
  return preset === 'dark' ? darkTheme : lightTheme;
}

/**
 * Applies ODRL theme CSS custom properties onto a DOM element.
 *
 * Used to scope theme variables to the shadow-DOM container instead
 * of polluting `document.documentElement`.
 */
function applyThemeToElement(el: HTMLElement, theme: ThemeConfig): void {
  for (const [prop, value] of Object.entries(theme)) {
    el.style.setProperty(`--${prop}`, value);
  }
}

/** Props accepted by the top-level {@link EmbeddedApp} component. */
export interface EmbeddedAppProps {
  /** Configuration forwarded from the Custom Element wrapper. */
  config: EmbeddedConfig;
  /** Optional partial i18n string overrides. */
  i18nOverrides?: DeepPartial<I18nStrings>;
  /** Optional custom theme overrides merged on top of the preset. */
  themeOverrides?: Partial<ThemeConfig>;
  /**
   * Ref to the container element where theme CSS custom properties
   * should be applied (typically the shadow-DOM container div).
   */
  containerRef?: RefObject<HTMLDivElement | null>;
  /** Optional policy template for pre-filling and constraining the editor. */
  template?: PolicyTemplate;
}

/**
 * Embedded policy editor application.
 *
 * Renders the full policy builder UI (visual builder + raw JSON +
 * validation) without any routing. Communicates with the host page
 * exclusively through the `onEvent` callback.
 */
const EmbeddedApp = ({
  config,
  i18nOverrides,
  themeOverrides,
  containerRef,
  template,
}: EmbeddedAppProps) => {
  const { apiBaseUrl, authToken, mode, policyId, locale, theme, onEvent, policyContext } = config;

  // --- API configuration ---
  useEffect(() => {
    configureApi(apiBaseUrl, authToken);
  }, [apiBaseUrl, authToken]);

  // --- Theme application on shadow-DOM container ---
  useEffect(() => {
    const el = containerRef?.current;
    if (!el) return;
    const resolved = resolveThemePreset(theme);
    const merged = themeOverrides ? { ...resolved, ...themeOverrides } : resolved;
    applyThemeToElement(el, merged);
  }, [theme, themeOverrides, containerRef]);

  // --- Policy state ---
  const [policy, setPolicy] = useState<OdrlPolicyJson>(
    () => createNewPolicy(policyContext) as OdrlPolicyJson,
  );
  const [saveError, setSaveError] = useState('');

  // Load existing policy in edit mode
  useEffect(() => {
    if (mode === 'edit' && policyId) {
      PolicyService.getPolicyById(policyId)
        .then((p) => setPolicy(JSON.parse(p.odrl!)))
        .catch((err: Error) => setSaveError(err.message));
    }
  }, [mode, policyId]);

  // --- Editor tab state ---
  const [activeTab, setActiveTab] = useState('builder');

  // --- Raw ODRL tab state ---
  /** Raw JSON text decoupled from the policy state for free-form editing. */
  const [rawText, setRawText] = useState('');
  /** JSON parse error message (empty when the text is valid JSON). */
  const [jsonError, setJsonError] = useState('');
  /** Tracks previous tab to detect switches. */
  const prevTabRef = useRef(activeTab);
  /** Prefix input for a new @context entry. */
  const [newCtxPrefix, setNewCtxPrefix] = useState('');
  /** URI input for a new @context entry. */
  const [newCtxUri, setNewCtxUri] = useState('');

  // --- Validation state ---
  const [showValidation, setShowValidation] = useState(false);
  const [testRequest, setTestRequest] = useState<TestRequest>(DEFAULT_TEST_REQUEST);
  const [jsonInput, setJsonInput] = useState<GenericJsonInput>(DEFAULT_JSON_INPUT);
  const [validationMode, setValidationMode] = useState<ValidationMode>('httpRequest');
  const [validationResult, setValidationResult] = useState<ValidationResponse | null>(null);
  const [validationError, setValidationError] = useState('');
  const [isValidating, setIsValidating] = useState(false);

  // --- Internal ref for theme fallback ---
  const internalRef = useRef<HTMLDivElement>(null);
  const effectiveRef = containerRef ?? internalRef;

  // Sync rawText from the current policy when switching TO the raw tab.
  useEffect(() => {
    if (activeTab === 'odrl' && prevTabRef.current !== 'odrl') {
      setRawText(JSON.stringify(policy, null, 2));
      setJsonError('');
    }
    prevTabRef.current = activeTab;
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activeTab]);

  /**
   * Handles changes in the raw ODRL textarea.
   *
   * Updates rawText immediately so the user can type freely, and syncs
   * to the policy state only when the JSON is valid.
   */
  const handleRawTextChange = useCallback((text: string) => {
    setRawText(text);
    try {
      const parsed = JSON.parse(text) as OdrlPolicyJson;
      setPolicy(parsed);
      setJsonError('');
    } catch (err: unknown) {
      setJsonError(err instanceof Error ? err.message : 'Invalid JSON');
    }
  }, []);

  /**
   * Adds a new prefix:URI entry to the policy's `@context`.
   */
  const handleAddContext = useCallback(() => {
    if (!newCtxPrefix.trim() || !newCtxUri.trim()) return;

    const currentCtx = (policy as Record<string, unknown>)['@context'];
    let contextObj: Record<string, string>;

    if (typeof currentCtx === 'object' && currentCtx !== null && !Array.isArray(currentCtx)) {
      contextObj = { ...(currentCtx as Record<string, string>) };
    } else if (typeof currentCtx === 'string') {
      contextObj = { odrl: currentCtx };
    } else {
      contextObj = {};
    }

    contextObj[newCtxPrefix.trim()] = newCtxUri.trim();

    const updatedPolicy = { ...policy, '@context': contextObj } as OdrlPolicyJson;
    setPolicy(updatedPolicy);
    setRawText(JSON.stringify(updatedPolicy, null, 2));
    setJsonError('');
    setNewCtxPrefix('');
    setNewCtxUri('');
  }, [newCtxPrefix, newCtxUri, policy]);

  /**
   * Removes a prefix entry from the policy's `@context`.
   *
   * @param prefix - The context key to remove.
   */
  const handleRemoveContext = useCallback((prefix: string) => {
    const currentCtx = (policy as Record<string, unknown>)['@context'];
    if (typeof currentCtx === 'object' && currentCtx !== null && !Array.isArray(currentCtx)) {
      const copy = { ...(currentCtx as Record<string, string>) };
      delete copy[prefix];
      const updatedPolicy = { ...policy, '@context': copy } as OdrlPolicyJson;
      setPolicy(updatedPolicy);
      setRawText(JSON.stringify(updatedPolicy, null, 2));
      setJsonError('');
    }
  }, [policy]);

  /** Saves the policy and emits the appropriate event. */
  const handleSave = useCallback(() => {
    setSaveError('');
    const uid = (policy as Record<string, unknown>)['odrl:uid'] as string | undefined;
    const effectiveId = policyId ?? uid ?? '';

    const savePromise =
      mode === 'edit' && policyId
        ? PolicyService.createPolicyWithId(policyId, policy)
        : PolicyService.createPolicy(policy);

    savePromise
      .then(() => {
        const eventType = mode === 'edit' ? 'policy-updated' : 'policy-created';
        onEvent(eventType, { policy, id: effectiveId });
      })
      .catch((err: Error) => setSaveError(err.message));
  }, [policy, mode, policyId, onEvent]);

  /** Cancels editing and notifies the host. */
  const handleCancel = useCallback(() => {
    onEvent('editor-cancelled', {} as Record<string, never>);
  }, [onEvent]);

  /** Runs policy validation. */
  const handleValidate = useCallback(() => {
    setIsValidating(true);
    setValidationError('');
    setValidationResult(null);

    try {
      const requestBody =
        validationMode === 'httpRequest'
          ? {
              policy,
              testRequest: {
                ...testRequest,
                body:
                  typeof testRequest.body === 'string'
                    ? JSON.parse(testRequest.body as string)
                    : testRequest.body,
              },
            }
          : { policy, jsonInput };

      UiService.validatePolicy(requestBody)
        .then((result) => {
          setValidationResult(result);
          onEvent('policy-validated', { result: result as unknown as Record<string, unknown> });
        })
        .catch((err: Error) => setValidationError(err.message))
        .finally(() => setIsValidating(false));
    } catch {
      setValidationError('Invalid JSON syntax');
      setIsValidating(false);
    }
  }, [validationMode, testRequest, jsonInput, policy, onEvent]);

  /** Resets the validation result to allow re-testing. */
  const handleTestAgain = () => {
    setValidationResult(null);
    setValidationError('');
  };

  return (
    <EmbeddedProvider config={config}>
      <I18nProvider locale={locale} strings={i18nOverrides}>
        <div ref={effectiveRef} className="odrl-embedded-root p-3">
          {/* Editor Tabs */}
          <Tabs
            id="embedded-editor-tabs"
            activeKey={activeTab}
            onSelect={(k) => setActiveTab(k!)}
            className="mb-3"
          >
            <Tab eventKey="builder" title="Policy Builder">
              <PolicyBuilder policy={policy} setPolicy={setPolicy} template={template} />
            </Tab>
            <Tab eventKey="odrl" title="Raw ODRL">
              {/* --- Context management --- */}
              <div className="mb-3 p-3 border rounded" style={{ backgroundColor: 'var(--odrl-card-bg, #f8f9fa)' }}>
                <h6 className="mb-2">JSON-LD Context (@context)</h6>
                {(() => {
                  const ctx = (policy as Record<string, unknown>)['@context'];
                  if (typeof ctx === 'object' && ctx !== null && !Array.isArray(ctx)) {
                    return (
                      <div className="mb-2 d-flex flex-wrap gap-1">
                        {Object.entries(ctx as Record<string, string>).map(([prefix, uri]) => (
                          <Badge
                            key={prefix}
                            bg="secondary"
                            className="d-inline-flex align-items-center gap-1 px-2 py-1"
                            title={uri}
                          >
                            <strong>{prefix}</strong>: {uri}
                            <CloseButton
                              variant="white"
                              style={{ fontSize: '0.5rem' }}
                              onClick={() => handleRemoveContext(prefix)}
                              aria-label={`Remove ${prefix}`}
                            />
                          </Badge>
                        ))}
                      </div>
                    );
                  }
                  if (typeof ctx === 'string') {
                    return <p className="text-muted small mb-2">{ctx}</p>;
                  }
                  return null;
                })()}
                <InputGroup size="sm">
                  <Form.Control
                    placeholder="e.g., dome-op"
                    value={newCtxPrefix}
                    onChange={(e) => setNewCtxPrefix(e.target.value)}
                    aria-label="Context prefix"
                  />
                  <Form.Control
                    placeholder="e.g., https://example.com/ns/"
                    value={newCtxUri}
                    onChange={(e) => setNewCtxUri(e.target.value)}
                    aria-label="Context namespace URI"
                  />
                  <Button
                    variant="outline-primary"
                    onClick={handleAddContext}
                    disabled={!newCtxPrefix.trim() || !newCtxUri.trim()}
                  >
                    Add Context
                  </Button>
                </InputGroup>
              </div>

              {/* --- Raw JSON textarea --- */}
              <Form.Control
                as="textarea"
                rows={16}
                value={rawText}
                onChange={(e) => handleRawTextChange(e.target.value)}
                isInvalid={!!jsonError}
                isValid={!jsonError && rawText.length > 0}
                className="font-monospace"
              />
              {jsonError && (
                <Form.Text className="text-danger">{jsonError}</Form.Text>
              )}
            </Tab>
          </Tabs>

          {/* Action buttons */}
          <div className="d-flex gap-2 mt-3 mb-3">
            <Button variant="primary" onClick={handleSave}>
              Save
            </Button>
            <Button variant="secondary" onClick={handleCancel}>
              Cancel
            </Button>
            <Button
              variant="info"
              onClick={() => setShowValidation(!showValidation)}
            >
              {showValidation ? 'Hide Validation' : 'Validate'}
            </Button>
          </div>

          {saveError && (
            <Alert variant="danger" className="mt-2" dismissible onClose={() => setSaveError('')}>
              {saveError}
            </Alert>
          )}

          {/* Inline validation panel (no modal — avoids portal issues in Shadow DOM) */}
          {showValidation && (
            <div className="border rounded p-3 mt-3" data-testid="validation-panel">
              <h5>Validate Policy</h5>

              {!validationResult ? (
                <>
                  <ValidationEditor
                    testRequest={testRequest}
                    setTestRequest={setTestRequest}
                    jsonInput={jsonInput}
                    setJsonInput={setJsonInput}
                    mode={validationMode}
                    setMode={setValidationMode}
                  />
                  <Button
                    variant="primary"
                    className="mt-3"
                    onClick={handleValidate}
                    disabled={isValidating}
                  >
                    {isValidating ? 'Validating...' : 'Run Validation'}
                  </Button>
                </>
              ) : (
                <>
                  <ValidationResult result={validationResult} policy={policy} />
                  <Button
                    variant="outline-primary"
                    className="mt-3"
                    onClick={handleTestAgain}
                  >
                    Test Again
                  </Button>
                </>
              )}

              {validationError && (
                <Alert variant="danger" className="mt-3">
                  {validationError}
                </Alert>
              )}
            </div>
          )}
        </div>
      </I18nProvider>
    </EmbeddedProvider>
  );
};

export default EmbeddedApp;
