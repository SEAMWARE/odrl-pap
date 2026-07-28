/**
 * Policy editor page.
 *
 * Provides tabs for visual policy building ("Policy Builder") and
 * raw ODRL JSON editing, plus a validation modal for testing policies.
 */
import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Form, Button, Tabs, Tab, Modal, Alert } from 'react-bootstrap';
import { PapService } from '../api/services/PapService';
import { UiService } from '../api/services/UiService';
import type { OdrlPolicyJson, Policy, ValidationResponse } from '../services/api';
import { TestRequest } from '../api/models/TestRequest';
import PolicyBuilder from '../components/PolicyBuilder';
import ValidationEditor from '../components/ValidationEditor';
import { useI18n } from '../i18n';

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

  // Validation state
  const [showValidation, setShowValidation] = useState(false);
  const [testRequest, setTestRequest] = useState<TestRequest>(DEFAULT_TEST_REQUEST);
  const [validationResult, setValidationResult] = useState<ValidationResponse | null>(null);
  const [validationError, setValidationError] = useState('');

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

  /** Runs the policy validation against the test request. */
  const handleValidate = () => {
    try {
      const body =
        typeof testRequest.body === 'string'
          ? JSON.parse(testRequest.body)
          : testRequest.body;
      const finalTestRequest = { ...testRequest, body };

      const requestBody = { policy, testRequest: finalTestRequest };
      UiService.validatePolicy(requestBody)
        .then(setValidationResult)
        .catch((err: Error) => setValidationError(err.message));
    } catch {
      setValidationError('Invalid JSON in body');
    }
  };

  /** Closes the validation modal and resets results. */
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
      <Modal show={showValidation} onHide={handleCloseValidation} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>{strings.validationEditor.title}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <ValidationEditor testRequest={testRequest} setTestRequest={setTestRequest} />
          {validationResult && (
            <Alert variant={validationResult.allow ? 'success' : 'danger'} className="mt-3">
              <Alert.Heading>Validation Result</Alert.Heading>
              <pre>{JSON.stringify(validationResult, null, 2)}</pre>
            </Alert>
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
          <Button variant="primary" onClick={handleValidate}>
            {strings.validationEditor.runValidation}
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default PolicyEditor;
