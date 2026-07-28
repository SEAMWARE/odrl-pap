/**
 * Policy editor page.
 *
 * Provides tabs for visual policy building ("Policy Builder") and
 * raw ODRL JSON editing, plus a validation modal for testing policies.
 * Supports two validation modes: HTTP Request and JSON Payload.
 * Test request data persists in session storage across modal open/close.
 */
import { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Form, Button, Tabs, Tab, Modal, Alert } from 'react-bootstrap';
import { PapService } from '../api/services/PapService';
import { UiService } from '../api/services/UiService';
import type { OdrlPolicyJson, Policy, ValidationResponse } from '../services/api';
import { TestRequest } from '../api/models/TestRequest';
import type { GenericJsonInput } from '../api/models/GenericJsonInput';
import type { ValidationMode } from '../components/ValidationEditor';
import PolicyBuilder from '../components/PolicyBuilder';
import ValidationEditor from '../components/ValidationEditor';
import ValidationResult from '../components/ValidationResult';
import { useI18n } from '../i18n';

/** Session storage key for persisting the last HTTP test request. */
const SESSION_KEY_TEST_REQUEST = 'odrl-pap-test-request';
/** Session storage key for persisting the last JSON input. */
const SESSION_KEY_JSON_INPUT = 'odrl-pap-json-input';
/** Session storage key for persisting the last validation mode. */
const SESSION_KEY_VALIDATION_MODE = 'odrl-pap-validation-mode';

/** Template for a new, empty ODRL policy. */
const NEW_POLICY_TEMPLATE = {
  '@context': 'http://www.w3.org/ns/odrl/2/',
  '@type': 'odrl:Policy',
  'odrl:permission': {},
};

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
 */
const PolicyEditor = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { strings } = useI18n();
  const t = strings.policyEditor;

  const [policy, setPolicy] = useState<OdrlPolicyJson>({});
  const [activeTab, setActiveTab] = useState('builder');

  // Validation state — initialized from session storage
  const [showValidation, setShowValidation] = useState(false);
  const persisted = loadPersistedState();
  const [testRequest, setTestRequest] = useState<TestRequest>(persisted.testRequest);
  const [jsonInput, setJsonInput] = useState<GenericJsonInput>(persisted.jsonInput);
  const [validationMode, setValidationMode] = useState<ValidationMode>(persisted.mode);
  const [validationResult, setValidationResult] = useState<ValidationResponse | null>(null);
  const [validationError, setValidationError] = useState('');
  const [isValidating, setIsValidating] = useState(false);

  useEffect(() => {
    if (id) {
      PapService.getPolicyById(id)
        .then((p: Policy) => setPolicy(JSON.parse(p.odrl!)))
        .catch(console.error);
    } else {
      const newPolicy = {
        ...NEW_POLICY_TEMPLATE,
        'odrl:uid': crypto.randomUUID(),
      };
      setPolicy(newPolicy);
    }
  }, [id]);

  // Persist validation state on change
  useEffect(() => {
    persistState(testRequest, jsonInput, validationMode);
  }, [testRequest, jsonInput, validationMode]);

  /** Saves the policy (create or update). */
  const handleSave = () => {
    const requestBody = policy;
    if (id) {
      PapService.createPolicyWithId(id, requestBody)
        .then(() => navigate('/'))
        .catch(console.error);
    } else {
      PapService.createPolicy(requestBody)
        .then(() => navigate('/'))
        .catch(console.error);
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

  return (
    <>
      <h1>{id ? t.editTitle : t.newTitle}</h1>
      <Tabs
        id="policy-editor-tabs"
        activeKey={activeTab}
        onSelect={(k) => setActiveTab(k!)}
        className="mb-3"
      >
        <Tab eventKey="builder" title={t.tabBuilder}>
          <PolicyBuilder policy={policy} setPolicy={setPolicy} />
        </Tab>
        <Tab eventKey="odrl" title={t.tabRawOdrl}>
          <Form.Control
            as="textarea"
            rows={20}
            value={JSON.stringify(policy, null, 2)}
            onChange={(e) => setPolicy(JSON.parse(e.target.value))}
          />
        </Tab>
      </Tabs>
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
