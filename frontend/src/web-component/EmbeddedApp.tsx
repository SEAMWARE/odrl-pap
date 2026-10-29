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
 * - Supports template-based policy creation via a template selection tab.
 * - Supports full template management (create/edit/delete) via a dedicated
 *   tab, which can be disabled with the `hide-template-create-tab` attribute.
 * - Allows per-tab visibility control via `hiddenTabs` configuration.
 */
import { useState, useEffect, useCallback, useRef, useMemo, type RefObject } from 'react';
import { Form, Button, Tabs, Tab, Alert, InputGroup, Badge, CloseButton } from 'react-bootstrap';
import { PolicyService } from '../api/services/PolicyService';
import { ServiceService } from '../api/services/ServiceService';
import { TemplateService } from '../api/services/TemplateService';
import { UiService } from '../api/services/UiService';
import type { OdrlPolicyJson, ValidationResponse, Template } from '../services/api';
import { configureApi } from '../services/api';
import type { Mappings } from '../api';
import type { FieldTemplate } from '../types';
import { TestRequest } from '../api/models/TestRequest';
import type { GenericJsonInput } from '../api/models/GenericJsonInput';
import type { ValidationMode } from '../components/ValidationEditor';
import PolicyBuilder from '../components/PolicyBuilder';
import ValidationEditor from '../components/ValidationEditor';
import ValidationResult from '../components/ValidationResult';
import TemplateSelector from '../components/TemplateSelector';
import TemplateFiller from '../components/TemplateFiller';
import EmbeddedTemplateManager from './EmbeddedTemplateManager';
import { I18nProvider, resolveStrings, type DeepPartial } from '../i18n';
import type { I18nStrings } from '../i18n';
import type { ThemeConfig } from '../theme/defaultTheme';
import {
  DEFAULT_SUMMARY_PLACEMENT,
  DEFAULT_VARIANT,
  EmbeddedProvider,
  type EmbeddedConfig,
} from './EmbeddedContext';
import { createNewPolicy } from '../constants/policyDefaults';
import {
  isJsonLdContextObject,
  jsonLdContextEntryIri,
  type JsonLdContextObject,
} from '../utils/jsonLdContext';
import { toPolicyId } from '../utils/policyId';
import { DEFAULT_CAPABILITIES, isRemote } from './capabilities';
import PolicySummary from '../components/PolicySummary';
import { DEFAULT_MAPPINGS, mergeMappings } from './defaultMappings';

/** Tab key for the template selection tab. */
const TAB_KEY_TEMPLATE = 'template';
/** Tab key for the visual policy builder tab. */
const TAB_KEY_BUILDER = 'builder';
/** Tab key for the raw ODRL JSON editor tab. */
const TAB_KEY_ODRL = 'odrl';
/** Tab key for the policy summary tab (summary="tab"). */
const TAB_KEY_SUMMARY = 'summary';
/** Tab key for the template management (create/edit/delete) tab. */
const TAB_KEY_MANAGE_TEMPLATES = 'manage-templates';

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

/**
 * Applies ODRL theme CSS custom properties onto a DOM element.
 *
 * Used to scope theme variables to the shadow-DOM container instead
 * of polluting `document.documentElement`.
 */
function applyThemeToElement(el: HTMLElement, theme: Partial<ThemeConfig>): void {
  for (const [prop, value] of Object.entries(theme)) {
    if (value !== undefined) el.style.setProperty(`--${prop}`, value);
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
  /** Optional field template for pre-filling and constraining the builder form. */
  fieldTemplate?: FieldTemplate;
  /**
   * Policy supplied by the host (JS property `policy`). Whenever this
   * reference changes the editor adopts it as its working policy.
   */
  policy?: OdrlPolicyJson;
  /**
   * Mappings supplied by the host (JS property `mappings`). When set they are
   * layered over the built-in defaults (same `name` wins) and `/mappings` is
   * not fetched; with the `mappings` capability off and nothing supplied, the
   * built-in defaults are used.
   */
  mappings?: Mappings;
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
  fieldTemplate,
  policy: policyProp,
  mappings: mappingsProp,
}: EmbeddedAppProps) => {
  const { apiBaseUrl, authToken, mode, policyId, locale, onEvent, policyContext, serviceId, hiddenTabs } = config;
  const capabilities = config.capabilities ?? DEFAULT_CAPABILITIES;
  const summaryPlacement = config.summaryPlacement ?? DEFAULT_SUMMARY_PLACEMENT;
  const hideCancelButton = config.hiddenButtons?.hideCancelButton ?? false;
  const variant = config.variant ?? DEFAULT_VARIANT;
  const remoteLoad = isRemote(capabilities, 'load');
  const remoteSave = isRemote(capabilities, 'save');
  const remoteValidate = isRemote(capabilities, 'validate');
  const remoteTemplates = isRemote(capabilities, 'templates');
  const remoteMappings = isRemote(capabilities, 'mappings');

  // EmbeddedApp renders the I18nProvider itself, so it cannot read strings via
  // useI18n(); it resolves them directly for the (translatable) tab titles.
  const tabStrings = useMemo(() => resolveStrings(i18nOverrides).policyEditor, [i18nOverrides]);

  // --- API configuration ---
  useEffect(() => {
    configureApi(apiBaseUrl, authToken);
  }, [apiBaseUrl, authToken]);

  // --- Theme overrides on the shadow-DOM container ---
  // The light and dark presets live in theme.css (`:host` / `:host([theme="dark"])`),
  // so a page can restyle the editor with plain CSS. Only the tokens the host
  // passes as `themeConfig` are set inline, and removed again when they change.
  useEffect(() => {
    const el = containerRef?.current;
    if (!el || !themeOverrides) return;
    applyThemeToElement(el, themeOverrides);
    return () => {
      for (const prop of Object.keys(themeOverrides)) el.style.removeProperty(`--${prop}`);
    };
  }, [themeOverrides, containerRef]);

  // --- Policy state ---
  const [policy, setPolicy] = useState<OdrlPolicyJson>(
    () => policyProp ?? (createNewPolicy(policyContext) as OdrlPolicyJson),
  );
  const [saveError, setSaveError] = useState('');

  // Host mappings are layered over the built-in ones; with neither a host
  // value nor a remote capability the built-ins alone are used.
  const builderMappings = useMemo(
    () => (mappingsProp ? mergeMappings(DEFAULT_MAPPINGS, mappingsProp) : remoteMappings ? undefined : DEFAULT_MAPPINGS),
    [mappingsProp, remoteMappings],
  );

  // The last policy the host knows about: the one it supplied or the last one
  // we reported through `policy-changed`. Used to avoid echoing it back.
  const hostPolicyRef = useRef<OdrlPolicyJson | undefined>(policy);

  // Adopt a policy supplied (or replaced) by the host.
  useEffect(() => {
    if (policyProp && policyProp !== hostPolicyRef.current) {
      hostPolicyRef.current = policyProp;
      setPolicy(policyProp);
    }
  }, [policyProp]);

  // Report every edit to the host, except the ones that originate from it.
  useEffect(() => {
    if (policy === hostPolicyRef.current) return;
    hostPolicyRef.current = policy;
    onEvent('policy-changed', { policy: policy as Record<string, unknown> });
  }, [policy, onEvent]);

  // Load existing policy in edit mode (uses service-scoped endpoint when serviceId is set)
  useEffect(() => {
    if (remoteLoad && mode === 'edit' && policyId) {
      const loadPromise = serviceId
        ? ServiceService.getServicePolicyById(serviceId, policyId)
        : PolicyService.getPolicyById(policyId);

      loadPromise
        .then((p) => setPolicy(JSON.parse(p.odrl!)))
        .catch((err: Error) => setSaveError(err.message));
    }
  }, [remoteLoad, mode, policyId, serviceId]);

  // --- Template state ---
  /** Available templates fetched from the backend. */
  const [templates, setTemplates] = useState<Template[]>([]);
  /** Whether templates are currently loading. */
  const [templatesLoading, setTemplatesLoading] = useState(true);
  /** The currently selected template in the Template tab. */
  const [selectedTemplate, setSelectedTemplate] = useState<Template | null>(null);
  /** Whether the policy was created from a template (disables other tabs). */
  const [createdFromTemplate, setCreatedFromTemplate] = useState(false);
  /** Whether a template-based policy creation is in progress. */
  const [isCreatingFromTemplate, setIsCreatingFromTemplate] = useState(false);

  // Fetch available templates for create mode when the template tab is visible.
  // Scoped to the configured service (if any) and re-run when it changes, so a
  // service-scoped template can be used to create a policy.
  useEffect(() => {
    if (!remoteTemplates || mode === 'edit' || hiddenTabs.hideTemplateTab) {
      setTemplatesLoading(false);
      return;
    }

    setTemplatesLoading(true);
    setSelectedTemplate(null);

    const fetchPromise = serviceId
      ? ServiceService.getServiceTemplates(serviceId)
      : TemplateService.getTemplates();

    fetchPromise
      .then((list) => {
        setTemplates(list);
      })
      .catch(() => {
        // Silently fail — templates are optional
        setTemplates([]);
      })
      .finally(() => {
        setTemplatesLoading(false);
      });
  }, [remoteTemplates, mode, hiddenTabs.hideTemplateTab, serviceId]);

  /** Whether the template selection tab should be visible. */
  const showTemplateTab = mode === 'create' && !hiddenTabs.hideTemplateTab && !templatesLoading && templates.length > 0;

  /**
   * Whether the template management tab should be visible.
   *
   * Independent of the editor `mode`: template authoring is orthogonal to
   * whether a policy is being created or edited. Controlled by the
   * `hide-template-create-tab` attribute.
   */
  const showManageTemplatesTab = remoteTemplates && !hiddenTabs.hideTemplateCreateTab;

  // --- Determine the default active tab based on visibility ---
  /**
   * Computes the default tab key, choosing only among tabs that are actually
   * rendered. Priority: template (when available) > builder > raw > manage.
   */
  const defaultTab = useMemo(() => {
    if (mode === 'create' && !hiddenTabs.hideTemplateTab && templates.length > 0) {
      return TAB_KEY_TEMPLATE;
    }
    if (!hiddenTabs.hideBuilderTab) return TAB_KEY_BUILDER;
    if (!hiddenTabs.hideRawTab) return TAB_KEY_ODRL;
    if (showManageTemplatesTab) return TAB_KEY_MANAGE_TEMPLATES;
    // Nothing is visible (all tabs hidden) — fall back to the builder key.
    return TAB_KEY_BUILDER;
  }, [hiddenTabs, mode, templates.length, showManageTemplatesTab]);

  /** Returns whether the tab with the given key is currently rendered. */
  const isTabVisible = useCallback(
    (key: string): boolean => {
      switch (key) {
        case TAB_KEY_TEMPLATE:
          return showTemplateTab;
        case TAB_KEY_BUILDER:
          return !hiddenTabs.hideBuilderTab;
        case TAB_KEY_ODRL:
          return !hiddenTabs.hideRawTab;
        case TAB_KEY_MANAGE_TEMPLATES:
          return showManageTemplatesTab;
        case TAB_KEY_SUMMARY:
          return summaryPlacement === 'tab';
        default:
          return false;
      }
    },
    [showTemplateTab, showManageTemplatesTab, summaryPlacement, hiddenTabs.hideBuilderTab, hiddenTabs.hideRawTab],
  );

  // --- Editor tab state ---
  const [activeTab, setActiveTab] = useState(TAB_KEY_BUILDER);
  /** True once the user has manually chosen a tab. */
  const userSelectedTabRef = useRef(false);

  // Follow the computed default until the user picks a tab; afterwards only move
  // them if their current tab has become hidden. This stops the async template
  // fetch (which changes `defaultTab` once templates resolve) from yanking the
  // user off a tab — e.g. an in-progress Manage Templates form — mid-edit.
  useEffect(() => {
    setActiveTab((current) =>
      userSelectedTabRef.current && isTabVisible(current) ? current : defaultTab,
    );
  }, [defaultTab, isTabVisible]);

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
    if (activeTab === TAB_KEY_ODRL && prevTabRef.current !== TAB_KEY_ODRL) {
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
    let contextObj: JsonLdContextObject;

    if (isJsonLdContextObject(currentCtx)) {
      contextObj = { ...currentCtx };
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
    if (isJsonLdContextObject(currentCtx)) {
      const copy = { ...currentCtx };
      delete copy[prefix];
      const updatedPolicy = { ...policy, '@context': copy } as OdrlPolicyJson;
      setPolicy(updatedPolicy);
      setRawText(JSON.stringify(updatedPolicy, null, 2));
      setJsonError('');
    }
  }, [policy]);

  /**
   * Handles policy creation from a filled template.
   *
   * Saves the generated ODRL JSON as a new policy, locks the editor
   * to prevent further modifications, and emits the `policy-created` event.
   *
   * @param odrl - The ODRL policy JSON with placeholders replaced.
   */
  const handleCreateFromTemplate = useCallback((odrl: Record<string, unknown>) => {
    // Without a remote save there is nothing to create: hand the filled-in
    // policy to the editor (and, through `policy-changed`, to the host).
    if (!remoteSave) {
      setPolicy(odrl as OdrlPolicyJson);
      userSelectedTabRef.current = true;
      setActiveTab(hiddenTabs.hideBuilderTab ? TAB_KEY_ODRL : TAB_KEY_BUILDER);
      return;
    }

    setIsCreatingFromTemplate(true);

    // Ensure the policy has a UID; generate one if the template did not include it.
    if (!odrl['odrl:uid']) {
      odrl['odrl:uid'] = crypto.randomUUID();
    }

    const requestBody = odrl as OdrlPolicyJson;
    const effectiveId = toPolicyId(odrl['odrl:uid'] as string);

    // Store the policy under an id derived from its `odrl:uid` (PUT /policy/{id})
    // rather than via POST /policy, which would assign a server-generated id the
    // host cannot resolve. This keeps the emitted `id` retrievable via
    // GET /policy/{id}. The uid itself is usually not a valid policy id (see
    // toPolicyId), and an invalid one is rejected by the PAP.
    const savePromise = serviceId
      ? ServiceService.createServicePolicyWithId(serviceId, effectiveId, requestBody)
      : PolicyService.createPolicyWithId(effectiveId, requestBody);

    savePromise
      .then(() => {
        setPolicy(requestBody);
        setCreatedFromTemplate(true);
        onEvent('policy-created', { policy: requestBody, id: effectiveId });
      })
      .catch((err: Error) => setSaveError(err.message))
      .finally(() => {
        setIsCreatingFromTemplate(false);
      });
  }, [remoteSave, hiddenTabs.hideBuilderTab, serviceId, onEvent]);

  /**
   * Saves the policy and emits the appropriate event.
   *
   * When `serviceId` is set, uses service-scoped endpoints
   * (`POST /service/{serviceId}/policy` or `PUT /service/{serviceId}/policy/{id}`).
   * Otherwise, falls back to root-level endpoints.
   */
  const handleSave = useCallback(() => {
    setSaveError('');

    // Ensure the policy carries a UID so it can be stored under — and later
    // retrieved by — that id. Creating via plain POST /policy would let the
    // backend assign a generated id that the host cannot resolve through
    // GET /policy/{id}, producing a 404 on the follow-up fetch.
    const body = { ...(policy as Record<string, unknown>) };
    if (!body['odrl:uid']) {
      body['odrl:uid'] = crypto.randomUUID();
    }
    const policyToSave = body as OdrlPolicyJson;
    const effectiveId = policyId ?? toPolicyId(body['odrl:uid'] as string);

    // Both create and edit use the id-scoped endpoint (PUT /policy/{id}),
    // which "creates or overwrites", so the stored id always matches the
    // emitted id.
    const savePromise = serviceId
      ? ServiceService.createServicePolicyWithId(serviceId, effectiveId, policyToSave)
      : PolicyService.createPolicyWithId(effectiveId, policyToSave);

    savePromise
      .then(() => {
        const eventType = mode === 'edit' ? 'policy-updated' : 'policy-created';
        onEvent(eventType, { policy: policyToSave, id: effectiveId });
      })
      .catch((err: Error) => setSaveError(err.message));
  }, [policy, mode, policyId, onEvent, serviceId]);

  /**
   * Keeps the working policy in sync with the template being filled in, so
   * that validation and other actions operate on the template-derived policy
   * rather than the default empty one.
   */
  const handleTemplatePolicyChange = useCallback((odrl: Record<string, unknown>) => {
    setPolicy(odrl as OdrlPolicyJson);
  }, []);

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
        <div
          ref={effectiveRef}
          className={`odrl-embedded-root${variant === 'card' ? ' p-3' : ''}`}
          data-variant={variant}
        >
          {/* Editor Tabs */}
          <Tabs
            id="embedded-editor-tabs"
            activeKey={activeTab}
            onSelect={(k) => {
              if (!k) return;
              // The template and management tabs stay switchable even when the
              // policy editor is locked in read-only (template-created) mode.
              if (!createdFromTemplate || k === TAB_KEY_TEMPLATE || k === TAB_KEY_MANAGE_TEMPLATES) {
                userSelectedTabRef.current = true;
                setActiveTab(k);
              }
            }}
            className="mb-3"
          >
            {/* Template tab — shown only for create mode when templates exist and tab is not hidden */}
            {showTemplateTab && (
              <Tab eventKey={TAB_KEY_TEMPLATE} title={tabStrings.tabTemplate}>
                <TemplateSelector
                  templates={templates}
                  selectedTemplate={selectedTemplate}
                  onSelect={setSelectedTemplate}
                />
                {selectedTemplate && (
                  <div className="mt-4">
                    <hr />
                    <TemplateFiller
                      key={selectedTemplate.id}
                      template={selectedTemplate}
                      onCreatePolicy={handleCreateFromTemplate}
                      onPolicyChange={handleTemplatePolicyChange}
                      isCreating={isCreatingFromTemplate}
                    />
                  </div>
                )}
              </Tab>
            )}

            {!hiddenTabs.hideBuilderTab && (
              <Tab
                eventKey={TAB_KEY_BUILDER}
                title={tabStrings.tabBuilder}
                disabled={createdFromTemplate}
              >
                <PolicyBuilder
                  policy={policy}
                  setPolicy={setPolicy}
                  fieldTemplate={fieldTemplate}
                  mappings={builderMappings}
                  showSummary={summaryPlacement === 'side'}
                />
              </Tab>
            )}
            {!hiddenTabs.hideRawTab && (
              <Tab
                eventKey={TAB_KEY_ODRL}
                title={tabStrings.tabRawOdrl}
                disabled={createdFromTemplate}
              >
                {/* --- Context management --- */}
                <div className="mb-3 p-3 border rounded" style={{ backgroundColor: 'var(--odrl-card-bg, #f8f9fa)' }}>
                  <h6 className="mb-2">JSON-LD Context (@context)</h6>
                  {(() => {
                    const ctx = (policy as Record<string, unknown>)['@context'];
                    if (isJsonLdContextObject(ctx)) {
                      return (
                        <div className="mb-2 d-flex flex-wrap gap-1">
                          {Object.entries(ctx).map(([prefix, entry]) => {
                            const uri = jsonLdContextEntryIri(entry);
                            return (
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
                            );
                          })}
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
            )}

            {/* Policy summary tab — only when the summary is placed as a tab */}
            {summaryPlacement === 'tab' && (
              <Tab eventKey={TAB_KEY_SUMMARY} title={tabStrings.tabSummary}>
                <PolicySummary policy={policy} />
              </Tab>
            )}

            {/* Template management tab — create/edit/delete templates */}
            {showManageTemplatesTab && (
              <Tab eventKey={TAB_KEY_MANAGE_TEMPLATES} title={tabStrings.tabManageTemplates}>
                <EmbeddedTemplateManager serviceId={serviceId} onEvent={onEvent} />
              </Tab>
            )}
          </Tabs>

          {/* Policy action buttons — shown only while editing a policy.
              Hidden in template-created read-only mode and on the template
              management tab, where policy save/validate is not applicable. */}
          {!createdFromTemplate && activeTab !== TAB_KEY_MANAGE_TEMPLATES && (remoteSave || remoteValidate || !hideCancelButton) && (
            <div className="d-flex gap-2 mt-3 mb-3">
              {remoteSave && (
                <Button variant="primary" onClick={handleSave}>
                  Save
                </Button>
              )}
              {!hideCancelButton && (
                <Button variant="secondary" onClick={handleCancel}>
                  Cancel
                </Button>
              )}
              {remoteValidate && (
                <Button
                  variant="info"
                  onClick={() => setShowValidation(!showValidation)}
                >
                  {showValidation ? 'Hide Validation' : 'Validate'}
                </Button>
              )}
            </div>
          )}

          {saveError && (
            <Alert variant="danger" className="mt-2" dismissible onClose={() => setSaveError('')}>
              {saveError}
            </Alert>
          )}

          {/* Inline validation panel (no modal — avoids portal issues in Shadow DOM) */}
          {remoteValidate && showValidation && activeTab !== TAB_KEY_MANAGE_TEMPLATES && (
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
