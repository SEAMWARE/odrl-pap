/**
 * Policy editor page.
 *
 * Provides tabs for template-based creation, visual policy building
 * ("Policy Builder"), and raw ODRL JSON editing, plus a validation
 * modal for testing policies.
 *
 * Tab order: Template | Policy Builder | Raw ODRL
 *
 * When a policy is created from a template, the Policy Builder and
 * Raw ODRL tabs are disabled — the policy is read-only.
 *
 * Supports two validation modes: HTTP Request and JSON Payload.
 * Test request data persists in session storage across modal open/close.
 */
import { useState, useEffect, useCallback, useRef } from 'react';
import { useParams, useNavigate, useSearchParams } from 'react-router-dom';
import { Form, Button, Tabs, Tab, Modal, Alert, InputGroup, Badge, CloseButton } from 'react-bootstrap';
import { PolicyService } from '../api/services/PolicyService';
import { ServiceService } from '../api/services/ServiceService';
import { TemplateService } from '../api/services/TemplateService';
import { UiService } from '../api/services/UiService';
import type { OdrlPolicyJson, Policy, ValidationResponse, Template } from '../services/api';
import type { ServiceList } from '../api/models/ServiceList';
import { TestRequest } from '../api/models/TestRequest';
import type { GenericJsonInput } from '../api/models/GenericJsonInput';
import type { ValidationMode } from '../components/ValidationEditor';
import PolicyBuilder from '../components/PolicyBuilder';
import ValidationEditor from '../components/ValidationEditor';
import ValidationResult from '../components/ValidationResult';
import TemplateSelector from '../components/TemplateSelector';
import TemplateFiller from '../components/TemplateFiller';
import { useI18n } from '../i18n';
import { createNewPolicy } from '../constants/policyDefaults';

/** Session storage key for persisting the last HTTP test request. */
const SESSION_KEY_TEST_REQUEST = 'odrl-pap-test-request';
/** Session storage key for persisting the last JSON input. */
const SESSION_KEY_JSON_INPUT = 'odrl-pap-json-input';
/** Session storage key for persisting the last validation mode. */
const SESSION_KEY_VALIDATION_MODE = 'odrl-pap-validation-mode';

/** Tab key for the template selection tab. */
const TAB_KEY_TEMPLATE = 'template';
/** Tab key for the visual policy builder tab. */
const TAB_KEY_BUILDER = 'builder';
/** Tab key for the raw ODRL JSON editor tab. */
const TAB_KEY_ODRL = 'odrl';

/** Default test request values for the validation modal. */
const DEFAULT_TEST_REQUEST: TestRequest = {
  method: TestRequest.method.GET,
  host: 'example.com',
  path: '/',
  headers: {
    'content-type': 'application/json',
  },
  body: {},
};

/** Default JSON input for the JSON payload validation mode. */
const DEFAULT_JSON_INPUT: GenericJsonInput = {
  payload: {},
};

/**
 * Loads persisted validation state from session storage, or returns defaults.
 *
 * @returns An object containing testRequest, jsonInput, and validation mode.
 */
const loadPersistedState = (): {
  testRequest: TestRequest;
  jsonInput: GenericJsonInput;
  mode: ValidationMode;
} => {
  let testRequest = DEFAULT_TEST_REQUEST;
  let jsonInput = DEFAULT_JSON_INPUT;
  let mode: ValidationMode = 'httpRequest';

  try {
    const stored = sessionStorage.getItem(SESSION_KEY_TEST_REQUEST);
    if (stored) testRequest = JSON.parse(stored);
  } catch { /* use default */ }

  try {
    const stored = sessionStorage.getItem(SESSION_KEY_JSON_INPUT);
    if (stored) jsonInput = JSON.parse(stored);
  } catch { /* use default */ }

  try {
    const stored = sessionStorage.getItem(SESSION_KEY_VALIDATION_MODE);
    if (stored === 'httpRequest' || stored === 'jsonPayload') mode = stored;
  } catch { /* use default */ }

  return { testRequest, jsonInput, mode };
};

/**
 * Persists validation state to session storage for recovery.
 *
 * @param testRequest - The current HTTP test request state.
 * @param jsonInput - The current JSON input state.
 * @param mode - The current validation mode.
 */
const persistState = (
  testRequest: TestRequest,
  jsonInput: GenericJsonInput,
  mode: ValidationMode,
): void => {
  try {
    sessionStorage.setItem(SESSION_KEY_TEST_REQUEST, JSON.stringify(testRequest));
    sessionStorage.setItem(SESSION_KEY_JSON_INPUT, JSON.stringify(jsonInput));
    sessionStorage.setItem(SESSION_KEY_VALIDATION_MODE, mode);
  } catch { /* session storage may be unavailable */ }
};

/**
 * Page component for creating and editing ODRL policies.
 *
 * When templates are available, the Template tab is shown first and
 * becomes the default active tab for new policies. Editing existing
 * policies defaults to the Policy Builder tab.
 */
const PolicyEditor = () => {
  const { id } = useParams();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { strings } = useI18n();
  const t = strings.policyEditor;

  const [policy, setPolicy] = useState<OdrlPolicyJson>({});
  const [activeTab, setActiveTab] = useState(TAB_KEY_BUILDER);

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
  /** Name of the template used to create the policy (for display). */
  const [templateName, setTemplateName] = useState<string>('');

  // --- Service selection state ---
  /** List of available services fetched from the backend. */
  const [services, setServices] = useState<ServiceList>([]);
  /** Whether the service list is currently loading. */
  const [servicesLoading, setServicesLoading] = useState(true);
  /** Error message if the service list fetch failed. */
  const [servicesError, setServicesError] = useState('');
  /** The currently selected service ID (null = standalone/root-level policy). */
  const [selectedServiceId, setSelectedServiceId] = useState<string | null>(
    searchParams.get('serviceId'),
  );

  // --- Raw ODRL tab state ---
  /** Raw JSON text decoupled from the policy object so the user can type freely. */
  const [rawText, setRawText] = useState('');
  /** JSON parse error message (empty when valid). */
  const [jsonError, setJsonError] = useState('');
  /** Tracks the previous active tab to detect tab switches. */
  const prevTabRef = useRef(activeTab);
  /** Prefix input for adding a new @context entry. */
  const [newCtxPrefix, setNewCtxPrefix] = useState('');
  /** URI input for adding a new @context entry. */
  const [newCtxUri, setNewCtxUri] = useState('');

  // Validation state — initialized from session storage
  const [showValidation, setShowValidation] = useState(false);
  const persisted = loadPersistedState();
  const [testRequest, setTestRequest] = useState<TestRequest>(persisted.testRequest);
  const [jsonInput, setJsonInput] = useState<GenericJsonInput>(persisted.jsonInput);
  const [validationMode, setValidationMode] = useState<ValidationMode>(persisted.mode);
  const [validationResult, setValidationResult] = useState<ValidationResponse | null>(null);
  const [validationError, setValidationError] = useState('');
  const [isValidating, setIsValidating] = useState(false);

  // Fetch available services on mount
  useEffect(() => {
    setServicesLoading(true);
    ServiceService.getServices()
      .then((list: ServiceList) => {
        setServices(list);
        setServicesError('');
      })
      .catch(() => {
        setServicesError(t.serviceLoadError);
      })
      .finally(() => {
        setServicesLoading(false);
      });
  }, [t.serviceLoadError]);

  // Fetch available templates on mount (only for new policies)
  useEffect(() => {
    if (id) {
      // Editing an existing policy — no template tab
      setTemplatesLoading(false);
      return;
    }

    setTemplatesLoading(true);
    TemplateService.getTemplates()
      .then((list) => {
        setTemplates(list);
        // Default to template tab if templates are available
        if (list.length > 0) {
          setActiveTab(TAB_KEY_TEMPLATE);
        }
      })
      .catch(() => {
        // Silently fail — templates are optional
        setTemplates([]);
      })
      .finally(() => {
        setTemplatesLoading(false);
      });
  }, [id]);

  // Load existing policy or initialize a new one
  useEffect(() => {
    if (id) {
      const serviceIdParam = searchParams.get('serviceId');
      if (serviceIdParam) {
        ServiceService.getServicePolicyById(serviceIdParam, id)
          .then((p: Policy) => setPolicy(JSON.parse(p.odrl!)))
          .catch(console.error);
      } else {
        PolicyService.getPolicyById(id)
          .then((p: Policy) => setPolicy(JSON.parse(p.odrl!)))
          .catch(console.error);
      }
    } else {
      setPolicy(createNewPolicy() as OdrlPolicyJson);
    }
  }, [id, searchParams]);

  // Sync rawText from the current policy when switching TO the raw tab.
  // Intentionally does NOT depend on `policy` so edits in the textarea
  // are never overwritten while the user is typing.
  useEffect(() => {
    if (activeTab === TAB_KEY_ODRL && prevTabRef.current !== TAB_KEY_ODRL) {
      setRawText(JSON.stringify(policy, null, 2));
      setJsonError('');
    }
    prevTabRef.current = activeTab;
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activeTab]);

  // Persist validation state on change
  useEffect(() => {
    persistState(testRequest, jsonInput, validationMode);
  }, [testRequest, jsonInput, validationMode]);

  /**
   * Handles changes in the raw ODRL textarea.
   *
   * Updates the raw text immediately (so the user can type freely)
   * and only syncs to the policy state when the text is valid JSON.
   */
  const handleRawTextChange = useCallback((text: string) => {
    setRawText(text);
    try {
      const parsed = JSON.parse(text) as OdrlPolicyJson;
      setPolicy(parsed);
      setJsonError('');
    } catch (err: unknown) {
      setJsonError(err instanceof Error ? err.message : t.jsonInvalid);
    }
  }, [t.jsonInvalid]);

  /**
   * Adds a new prefix:URI entry to the policy's `@context`.
   *
   * If the current `@context` is a plain string it is converted to an
   * object first. After mutation both the policy state and the raw text
   * are updated so the textarea reflects the change.
   */
  const handleAddContext = useCallback(() => {
    if (!newCtxPrefix.trim() || !newCtxUri.trim()) return;

    const currentCtx = (policy as Record<string, unknown>)['@context'];
    let contextObj: Record<string, string>;

    if (typeof currentCtx === 'object' && currentCtx !== null && !Array.isArray(currentCtx)) {
      contextObj = { ...(currentCtx as Record<string, string>) };
    } else if (typeof currentCtx === 'string') {
      // Convert a plain-string context to a namespaced object
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
   * @param prefix - The key to remove.
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

  /**
   * Handles policy creation from a filled template.
   *
   * Saves the generated ODRL JSON as a new policy and locks the editor
   * to prevent further modifications (template-created policies are read-only).
   *
   * @param odrl - The ODRL policy JSON with placeholders replaced.
   */
  const handleCreateFromTemplate = useCallback((odrl: Record<string, unknown>) => {
    setIsCreatingFromTemplate(true);

    // Ensure the policy has a UID; generate one if the template did not include it.
    if (!odrl['odrl:uid']) {
      odrl['odrl:uid'] = crypto.randomUUID();
    }

    const requestBody = odrl as OdrlPolicyJson;

    const savePromise = selectedServiceId
      ? ServiceService.createServicePolicy(selectedServiceId, requestBody)
      : PolicyService.createPolicy(requestBody);

    savePromise
      .then(() => {
        setPolicy(requestBody);
        setCreatedFromTemplate(true);
        setTemplateName(selectedTemplate?.name ?? '');
        navigate('/');
      })
      .catch(console.error)
      .finally(() => {
        setIsCreatingFromTemplate(false);
      });
  }, [selectedServiceId, selectedTemplate, navigate]);

  /**
   * Saves the policy (create or update).
   *
   * When a service is selected, uses the service-scoped policy endpoints.
   * When no service is selected, uses the root-level policy endpoints.
   */
  const handleSave = () => {
    const requestBody = policy;
    if (selectedServiceId) {
      if (id) {
        ServiceService.createServicePolicyWithId(selectedServiceId, id, requestBody)
          .then(() => navigate('/'))
          .catch(console.error);
      } else {
        ServiceService.createServicePolicy(selectedServiceId, requestBody)
          .then(() => navigate('/'))
          .catch(console.error);
      }
    } else {
      if (id) {
        PolicyService.createPolicyWithId(id, requestBody)
          .then(() => navigate('/'))
          .catch(console.error);
      } else {
        PolicyService.createPolicy(requestBody)
          .then(() => navigate('/'))
          .catch(console.error);
      }
    }
  };

  /** Runs the policy validation against the test request or JSON input. */
  const handleValidate = useCallback(() => {
    setIsValidating(true);
    setValidationError('');
    setValidationResult(null);

    try {
      if (validationMode === 'httpRequest') {
        const body =
          typeof testRequest.body === 'string'
            ? JSON.parse(testRequest.body)
            : testRequest.body;
        const finalTestRequest = { ...testRequest, body };
        const requestBody = { policy, testRequest: finalTestRequest };

        UiService.validatePolicy(requestBody)
          .then(setValidationResult)
          .catch((err: Error) => setValidationError(err.message))
          .finally(() => setIsValidating(false));
      } else {
        const requestBody = { policy, jsonInput };

        UiService.validatePolicy(requestBody)
          .then(setValidationResult)
          .catch((err: Error) => setValidationError(err.message))
          .finally(() => setIsValidating(false));
      }
    } catch {
      setValidationError(strings.validationEditor.invalidJson);
      setIsValidating(false);
    }
  }, [validationMode, testRequest, jsonInput, policy, strings]);

  /** Resets the validation result to allow re-testing without closing modal. */
  const handleTestAgain = () => {
    setValidationResult(null);
    setValidationError('');
  };

  /** Closes the validation modal (preserves test data via session storage). */
  const handleCloseValidation = () => {
    setShowValidation(false);
    setValidationResult(null);
    setValidationError('');
  };

  /**
   * Handles service dropdown selection changes.
   *
   * @param value - The selected option value (empty string for no service).
   */
  const handleServiceChange = (value: string) => {
    setSelectedServiceId(value || null);
  };

  /** Whether the template tab should be visible. */
  const showTemplateTab = !id && !templatesLoading && templates.length > 0;

  return (
    <>
      <h1>{id ? t.editTitle : t.newTitle}</h1>

      {/* Read-only banner for template-created policies */}
      {createdFromTemplate && (
        <Alert variant="info" className="d-flex align-items-center gap-2">
          <Badge bg="info">{strings.templateFiller.createdFromTemplate}</Badge>
          {templateName && <span>{templateName}</span>}
          <span className="text-muted">&mdash; {t.templateCreated}</span>
        </Alert>
      )}

      {/* Service selection dropdown */}
      {!servicesLoading && !servicesError && (
        <Form.Group className="mb-3" controlId="service-select">
          <Form.Label>{t.serviceLabel}</Form.Label>
          <Form.Select
            value={selectedServiceId ?? ''}
            onChange={(e) => handleServiceChange(e.target.value)}
            aria-label={t.serviceLabel}
            title={t.serviceTooltip}
          >
            <option value="">{t.serviceNone}</option>
            {services.map((svc) => (
              <option key={svc.id} value={svc.id}>
                {svc.id}
              </option>
            ))}
          </Form.Select>
          <Form.Text className="text-muted">{t.serviceTooltip}</Form.Text>
        </Form.Group>
      )}
      {servicesError && (
        <Alert variant="warning" className="mb-3">
          {servicesError}
        </Alert>
      )}

      <Tabs
        id="policy-editor-tabs"
        activeKey={activeTab}
        onSelect={(k) => {
          if (k && !createdFromTemplate) {
            setActiveTab(k);
          } else if (k === TAB_KEY_TEMPLATE) {
            // Always allow switching to template tab even in read-only mode
            setActiveTab(k);
          }
        }}
        className="mb-3"
      >
        {/* Template tab — shown only for new policies when templates exist */}
        {showTemplateTab && (
          <Tab eventKey={TAB_KEY_TEMPLATE} title={t.tabTemplate}>
            <TemplateSelector
              templates={templates}
              selectedTemplate={selectedTemplate}
              onSelect={setSelectedTemplate}
            />
            {selectedTemplate && (
              <div className="mt-4">
                <hr />
                <TemplateFiller
                  template={selectedTemplate}
                  onCreatePolicy={handleCreateFromTemplate}
                  isCreating={isCreatingFromTemplate}
                />
              </div>
            )}
          </Tab>
        )}

        <Tab
          eventKey={TAB_KEY_BUILDER}
          title={t.tabBuilder}
          disabled={createdFromTemplate}
        >
          <PolicyBuilder policy={policy} setPolicy={setPolicy} />
        </Tab>
        <Tab
          eventKey={TAB_KEY_ODRL}
          title={t.tabRawOdrl}
          disabled={createdFromTemplate}
        >
          {/* --- Context management --- */}
          <div className="mb-3 p-3 border rounded" style={{ backgroundColor: 'var(--odrl-card-bg, #f8f9fa)' }}>
            <h6 className="mb-2">{t.contextTitle}</h6>
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
                          aria-label={`${t.removeContext} ${prefix}`}
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
                placeholder={t.contextPrefixPlaceholder}
                value={newCtxPrefix}
                onChange={(e) => setNewCtxPrefix(e.target.value)}
                aria-label={t.contextPrefix}
              />
              <Form.Control
                placeholder={t.contextUriPlaceholder}
                value={newCtxUri}
                onChange={(e) => setNewCtxUri(e.target.value)}
                aria-label={t.contextUri}
              />
              <Button
                variant="outline-primary"
                onClick={handleAddContext}
                disabled={!newCtxPrefix.trim() || !newCtxUri.trim()}
              >
                {t.addContext}
              </Button>
            </InputGroup>
          </div>

          {/* --- Raw JSON textarea --- */}
          <Form.Control
            as="textarea"
            rows={20}
            value={rawText}
            onChange={(e) => handleRawTextChange(e.target.value)}
            isInvalid={!!jsonError}
            isValid={!jsonError && rawText.length > 0}
            aria-label={t.tabRawOdrl}
            className="font-monospace"
          />
          {jsonError && (
            <Form.Text className="text-danger">{jsonError}</Form.Text>
          )}
        </Tab>
      </Tabs>

      {/* Action buttons — hidden when in template-created read-only mode */}
      {!createdFromTemplate && (
        <>
          <hr />
          <Button variant="primary" onClick={handleSave}>
            {strings.common.save}
          </Button>
          <Button variant="secondary" className="ms-2" onClick={() => navigate('/')}>
            {strings.common.cancel}
          </Button>
          <Button variant="info" className="ms-2" onClick={() => setShowValidation(true)}>
            {t.validate}
          </Button>
        </>
      )}

      {/* Validation Modal */}
      <Modal
        show={showValidation}
        onHide={handleCloseValidation}
        size="xl"
        fullscreen="lg-down"
        data-testid="validation-modal"
      >
        <Modal.Header closeButton>
          <Modal.Title>{strings.validationEditor.title}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {/* Show editor when no result; show result when available */}
          {!validationResult ? (
            <ValidationEditor
              testRequest={testRequest}
              setTestRequest={setTestRequest}
              jsonInput={jsonInput}
              setJsonInput={setJsonInput}
              mode={validationMode}
              setMode={setValidationMode}
            />
          ) : (
            <ValidationResult result={validationResult} policy={policy} />
          )}

          {validationError && (
            <Alert variant="danger" className="mt-3">
              {validationError}
            </Alert>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseValidation}>
            {strings.common.close}
          </Button>
          {validationResult ? (
            <Button variant="outline-primary" onClick={handleTestAgain}>
              {strings.validationEditor.testAgain}
            </Button>
          ) : (
            <Button
              variant="primary"
              onClick={handleValidate}
              disabled={isValidating}
            >
              {isValidating ? strings.common.loading : strings.validationEditor.runValidation}
            </Button>
          )}
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default PolicyEditor;
