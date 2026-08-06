/**
 * ValidationEditor component.
 *
 * Provides two validation modes:
 * - **HTTP Request mode:** Build an HTTP test request with method, host, path,
 *   protocol, headers (including custom headers), JWT helper, and body.
 * - **JSON Payload mode:** Evaluate a policy against an arbitrary JSON payload
 *   with optional subject/credential context.
 *
 * Both modes support pre-populated example data and JSON syntax validation
 * before submission.
 */
import { Form, Row, Col, Stack, Button, Alert, Tab, Tabs, InputGroup, Badge } from 'react-bootstrap';
import { useState, useCallback } from 'react';
import { useI18n } from '../i18n';
import type { TestRequest } from '../services/api';
import type { GenericJsonInput } from '../api/models/GenericJsonInput';

/** Supported validation input modes. */
export type ValidationMode = 'httpRequest' | 'jsonPayload';

/** Props for the ValidationEditor component. */
interface ValidationEditorProps {
  /** Current HTTP test request state. */
  testRequest: TestRequest;
  /** Callback to update the HTTP test request. */
  setTestRequest: (testRequest: TestRequest) => void;
  /** Current JSON input state (payload mode). */
  jsonInput: GenericJsonInput;
  /** Callback to update the JSON input. */
  setJsonInput: (jsonInput: GenericJsonInput) => void;
  /** Currently active validation mode. */
  mode: ValidationMode;
  /** Callback to change the active validation mode. */
  setMode: (mode: ValidationMode) => void;
}

/** Represents a single custom header key-value pair. */
interface CustomHeader {
  /** Unique identifier for React key prop. */
  id: string;
  /** Header field name. */
  name: string;
  /** Header field value. */
  value: string;
}

/** Authorization type options for HTTP request mode. */
type AuthType = 'none' | 'manual' | 'jwt';

/** Available HTTP methods for the method dropdown. */
const HTTP_METHODS = ['GET', 'POST', 'PUT', 'PATCH', 'DELETE'] as const;

/** Available HTTP protocols. */
const HTTP_PROTOCOLS = ['https', 'http'] as const;

/** Example HTTP test request pre-filled for quick testing. */
const EXAMPLE_HTTP_REQUEST: TestRequest = {
  method: 'GET' as TestRequest.method,
  host: 'api.example.com',
  path: '/ngsi-ld/v1/entities/urn:example:product:123',
  protocol: 'https' as TestRequest.protocol,
  headers: {
    'content-type': 'application/json',
  },
  body: {},
};

/** Example JSON payload pre-filled for quick testing. */
const EXAMPLE_JSON_INPUT: GenericJsonInput = {
  payload: {
    type: 'Product',
    id: 'urn:example:product:123',
    name: 'Example Product',
    category: 'electronics',
  },
  subject: {
    type: 'VerifiableCredential',
    credentialSubject: {
      id: 'did:example:user:456',
      role: 'admin',
    },
  },
};

/**
 * Generates a unique ID for custom header tracking.
 *
 * @returns A short unique string suitable for React keys.
 */
const generateHeaderId = (): string => Math.random().toString(36).substring(2, 9);

/**
 * Encodes a string to Base64-URL format (no padding).
 *
 * @param str - The raw string to encode.
 * @returns The Base64-URL encoded string.
 */
const base64UrlEncode = (str: string): string =>
  btoa(str).replace(/\+/g, '-').replace(/\//g, '_').replace(/=/g, '');

/**
 * Validates that a string is parseable JSON.
 *
 * @param str - The string to validate.
 * @returns `true` if the string is valid JSON, `false` otherwise.
 */
const isValidJson = (str: string): boolean => {
  try {
    JSON.parse(str);
    return true;
  } catch {
    return false;
  }
};

/**
 * Builds a cURL command string from the current test request state.
 *
 * @param testRequest - The HTTP test request to convert.
 * @param customHeaders - Additional custom headers to include.
 * @returns The cURL command as a string.
 */
const buildCurlCommand = (testRequest: TestRequest, customHeaders: CustomHeader[]): string => {
  const protocol = testRequest.protocol || 'https';
  const url = `${protocol}://${testRequest.host || 'example.com'}${testRequest.path || '/'}`;
  const method = testRequest.method || 'GET';

  const parts = [`curl -X ${method}`];

  // Standard headers
  if (testRequest.headers?.['content-type']) {
    parts.push(`-H "Content-Type: ${testRequest.headers['content-type']}"`);
  }
  if (testRequest.headers?.authorization) {
    parts.push(`-H "Authorization: ${testRequest.headers.authorization}"`);
  }

  // Custom headers
  for (const header of customHeaders) {
    if (header.name && header.value) {
      parts.push(`-H "${header.name}: ${header.value}"`);
    }
  }

  // Body
  if (testRequest.body && Object.keys(testRequest.body).length > 0) {
    const bodyStr = typeof testRequest.body === 'string'
      ? testRequest.body
      : JSON.stringify(testRequest.body);
    parts.push(`-d '${bodyStr}'`);
  }

  parts.push(`"${url}"`);

  return parts.join(' \\\n  ');
};

/**
 * Editor component for configuring policy validation requests.
 *
 * Supports two modes: HTTP Request (for testing API gateway policies)
 * and JSON Payload (for evaluating policies against arbitrary JSON data).
 */
const ValidationEditor = ({
  testRequest,
  setTestRequest,
  jsonInput,
  setJsonInput,
  mode,
  setMode,
}: ValidationEditorProps) => {
  const { strings } = useI18n();
  const t = strings.validationEditor;

  // HTTP Request mode state
  const [jwtPayloadInput, setJwtPayloadInput] = useState('{\n  "sub": "did:example:user:456",\n  "iss": "did:example:org:789",\n  "vc": {\n    "credentialSubject": {\n      "role": "admin"\n    }\n  }\n}');
  const [jwtError, setJwtError] = useState('');
  const [authType, setAuthType] = useState<AuthType>('none');
  const [customHeaders, setCustomHeaders] = useState<CustomHeader[]>([]);
  const [curlCopied, setCurlCopied] = useState(false);

  // JSON Payload mode state
  const [payloadText, setPayloadText] = useState(JSON.stringify(jsonInput.payload, null, 2));
  const [subjectText, setSubjectText] = useState(
    jsonInput.subject ? JSON.stringify(jsonInput.subject, null, 2) : '',
  );
  const [payloadError, setPayloadError] = useState('');
  const [subjectError, setSubjectError] = useState('');

  /** Updates a single field on the test request. */
  const handleChange = useCallback(
    (field: keyof TestRequest, value: unknown) => {
      setTestRequest({ ...testRequest, [field]: value });
    },
    [testRequest, setTestRequest],
  );

  /** Updates a standard header field (content-type or authorization). */
  const handleHeaderChange = useCallback(
    (field: string, value: string) => {
      const newHeaders = { ...(testRequest.headers || {}), [field]: value };
      handleChange('headers', newHeaders);
    },
    [testRequest, handleChange],
  );

  /** Generates an unsigned JWT from the payload input. */
  const generateJwt = () => {
    try {
      const header = { alg: 'none', typ: 'JWT' };
      const payload = JSON.parse(jwtPayloadInput);
      const encodedHeader = base64UrlEncode(JSON.stringify(header));
      const encodedPayload = base64UrlEncode(JSON.stringify(payload));
      const jwt = `${encodedHeader}.${encodedPayload}.`;
      handleHeaderChange('authorization', `Bearer ${jwt}`);
      setJwtError('');
    } catch {
      setJwtError(t.invalidJson);
    }
  };

  /** Adds an empty custom header row. */
  const addCustomHeader = () => {
    setCustomHeaders([...customHeaders, { id: generateHeaderId(), name: '', value: '' }]);
  };

  /** Removes a custom header by its ID. */
  const removeCustomHeader = (id: string) => {
    setCustomHeaders(customHeaders.filter((h) => h.id !== id));
  };

  /** Updates a custom header field by ID. */
  const updateCustomHeader = (id: string, field: 'name' | 'value', value: string) => {
    setCustomHeaders(
      customHeaders.map((h) => (h.id === id ? { ...h, [field]: value } : h)),
    );
  };

  /** Pre-fills the HTTP request with example data. */
  const loadHttpExample = () => {
    setTestRequest(EXAMPLE_HTTP_REQUEST);
  };

  /** Pre-fills the JSON payload with example data. */
  const loadJsonExample = () => {
    const example = EXAMPLE_JSON_INPUT;
    setJsonInput(example);
    setPayloadText(JSON.stringify(example.payload, null, 2));
    setSubjectText(example.subject ? JSON.stringify(example.subject, null, 2) : '');
    setPayloadError('');
    setSubjectError('');
  };

  /** Copies the constructed cURL command to the clipboard. */
  const copyCurl = async () => {
    const curl = buildCurlCommand(testRequest, customHeaders);
    await navigator.clipboard.writeText(curl);
    setCurlCopied(true);
    setTimeout(() => setCurlCopied(false), 2000);
  };

  /** Handles changes to the JSON payload textarea. */
  const handlePayloadChange = (value: string) => {
    setPayloadText(value);
    if (value.trim() === '') {
      setPayloadError('');
      return;
    }
    if (isValidJson(value)) {
      setPayloadError('');
      setJsonInput({ ...jsonInput, payload: JSON.parse(value) });
    } else {
      setPayloadError(t.invalidJson);
    }
  };

  /** Handles changes to the JSON subject textarea. */
  const handleSubjectChange = (value: string) => {
    setSubjectText(value);
    if (value.trim() === '') {
      setSubjectError('');
      setJsonInput({ ...jsonInput, subject: undefined });
      return;
    }
    if (isValidJson(value)) {
      setSubjectError('');
      setJsonInput({ ...jsonInput, subject: JSON.parse(value) });
    } else {
      setSubjectError(t.invalidJson);
    }
  };

  /** Decodes a JWT token for preview display. */
  const getJwtPreview = (): string | null => {
    const authHeader = testRequest.headers?.authorization || '';
    if (!authHeader.startsWith('Bearer ')) return null;
    const token = authHeader.substring(7);
    const parts = token.split('.');
    if (parts.length < 2) return null;
    try {
      const header = JSON.parse(atob(parts[0].replace(/-/g, '+').replace(/_/g, '/')));
      const payload = JSON.parse(atob(parts[1].replace(/-/g, '+').replace(/_/g, '/')));
      return JSON.stringify({ header, payload }, null, 2);
    } catch {
      return null;
    }
  };

  const jwtPreview = getJwtPreview();

  return (
    <Stack gap={3}>
      {/* Mode toggle tabs */}
      <Tabs
        id="validation-mode-tabs"
        activeKey={mode}
        onSelect={(k) => setMode(k as ValidationMode)}
        className="mb-2"
        data-testid="validation-mode-tabs"
      >
        <Tab eventKey="httpRequest" title={t.modeHttpRequest}>
          <Stack gap={3} className="pt-3">
            {/* Method, Protocol, Host */}
            <Row>
              <Col sm={3}>
                <Form.Group>
                  <Form.Label>{t.method}</Form.Label>
                  <Form.Select
                    value={testRequest.method || ''}
                    onChange={(e) => handleChange('method', e.target.value)}
                    aria-label={t.method}
                  >
                    <option>{t.selectMethod}</option>
                    {HTTP_METHODS.map((m) => (
                      <option key={m} value={m}>{m}</option>
                    ))}
                  </Form.Select>
                </Form.Group>
              </Col>
              <Col sm={2}>
                <Form.Group>
                  <Form.Label>{t.protocol}</Form.Label>
                  <Form.Select
                    value={(testRequest as Record<string, unknown>).protocol as string || 'https'}
                    onChange={(e) => handleChange('protocol', e.target.value)}
                    aria-label={t.protocol}
                  >
                    {HTTP_PROTOCOLS.map((p) => (
                      <option key={p} value={p}>{p}</option>
                    ))}
                  </Form.Select>
                </Form.Group>
              </Col>
              <Col sm={7}>
                <Form.Group>
                  <Form.Label>{t.host}</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder={t.hostPlaceholder}
                    value={testRequest.host || ''}
                    onChange={(e) => handleChange('host', e.target.value)}
                    aria-label={t.host}
                  />
                </Form.Group>
              </Col>
            </Row>

            {/* Path */}
            <Form.Group>
              <Form.Label>{t.path}</Form.Label>
              <Form.Control
                type="text"
                placeholder={t.pathPlaceholder}
                value={testRequest.path || ''}
                onChange={(e) => handleChange('path', e.target.value)}
                aria-label={t.path}
              />
            </Form.Group>

            <hr />

            {/* Headers section */}
            <h6>{t.headers}</h6>
            <Row>
              <Col>
                <Form.Group>
                  <Form.Label>{t.contentType}</Form.Label>
                  <Form.Control
                    type="text"
                    value={testRequest.headers?.['content-type'] || 'application/json'}
                    onChange={(e) => handleHeaderChange('content-type', e.target.value)}
                    aria-label={t.contentType}
                  />
                </Form.Group>
              </Col>
              <Col>
                <Form.Group>
                  <Form.Label>{t.authType}</Form.Label>
                  <Form.Select
                    value={authType}
                    onChange={(e) => {
                      const newType = e.target.value as AuthType;
                      setAuthType(newType);
                      if (newType === 'none') handleHeaderChange('authorization', '');
                    }}
                    aria-label={t.authType}
                  >
                    <option value="none">{t.authNone}</option>
                    <option value="manual">{t.authManual}</option>
                    <option value="jwt">{t.authJwt}</option>
                  </Form.Select>
                </Form.Group>
              </Col>
            </Row>

            {/* Manual auth input */}
            {authType === 'manual' && (
              <Form.Group>
                <Form.Label>{t.authHeader}</Form.Label>
                <Form.Control
                  type="text"
                  placeholder={t.authPlaceholder}
                  value={testRequest.headers?.authorization || ''}
                  onChange={(e) => handleHeaderChange('authorization', e.target.value)}
                  aria-label={t.authHeader}
                />
              </Form.Group>
            )}

            {/* JWT helper */}
            {authType === 'jwt' && (
              <Stack gap={2}>
                <h6>{t.jwtHelper}</h6>
                <Form.Text className="text-muted mb-2">
                  {t.jwtHelperDescription}
                </Form.Text>
                <Form.Group>
                  <Form.Label>{t.jwtPayload}</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={5}
                    value={jwtPayloadInput}
                    onChange={(e) => setJwtPayloadInput(e.target.value)}
                    isInvalid={!!jwtError}
                    className="font-monospace"
                    aria-label={t.jwtPayload}
                  />
                  <Form.Control.Feedback type="invalid">
                    {jwtError}
                  </Form.Control.Feedback>
                </Form.Group>
                <Button variant="secondary" onClick={generateJwt} size="sm">
                  {t.generateJwt}
                </Button>
                {jwtPreview && (
                  <div>
                    <Form.Label className="text-muted">{t.jwtPreview}</Form.Label>
                    <pre className="bg-light p-2 rounded border small font-monospace" data-testid="jwt-preview">
                      {jwtPreview}
                    </pre>
                  </div>
                )}
              </Stack>
            )}

            {/* Custom headers */}
            <div>
              <div className="d-flex align-items-center gap-2 mb-2">
                <h6 className="mb-0">{t.customHeaders}</h6>
                <Button variant="outline-primary" size="sm" onClick={addCustomHeader}>
                  {t.addHeader}
                </Button>
              </div>
              {customHeaders.map((header) => (
                <InputGroup key={header.id} className="mb-2">
                  <Form.Control
                    type="text"
                    placeholder={t.headerName}
                    value={header.name}
                    onChange={(e) => updateCustomHeader(header.id, 'name', e.target.value)}
                    aria-label={t.headerName}
                  />
                  <Form.Control
                    type="text"
                    placeholder={t.headerValue}
                    value={header.value}
                    onChange={(e) => updateCustomHeader(header.id, 'value', e.target.value)}
                    aria-label={t.headerValue}
                  />
                  <Button
                    variant="outline-danger"
                    onClick={() => removeCustomHeader(header.id)}
                    aria-label={t.removeHeader}
                  >
                    {t.removeHeader}
                  </Button>
                </InputGroup>
              ))}
            </div>

            <hr />

            {/* Request body */}
            <Form.Group>
              <Form.Label>{t.body}</Form.Label>
              <Form.Control
                as="textarea"
                rows={6}
                placeholder={t.bodyPlaceholder}
                value={typeof testRequest.body === 'string' ? testRequest.body : JSON.stringify(testRequest.body, null, 2)}
                onChange={(e) => handleChange('body', e.target.value)}
                className="font-monospace"
                aria-label={t.body}
              />
            </Form.Group>

            {/* HTTP mode action buttons */}
            <div className="d-flex gap-2">
              <Button variant="outline-secondary" size="sm" onClick={loadHttpExample} data-testid="http-load-example">
                {t.loadExample}
              </Button>
              <Button variant="outline-secondary" size="sm" onClick={copyCurl}>
                {curlCopied ? (
                  <>{t.copiedCurl}</>
                ) : (
                  <>{t.copyAsCurl}</>
                )}
              </Button>
            </div>
          </Stack>
        </Tab>

        <Tab eventKey="jsonPayload" title={t.modeJsonPayload}>
          <Stack gap={3} className="pt-3">
            {/* Payload textarea */}
            <Form.Group>
              <Form.Label>
                {t.jsonPayload}{' '}
                <Badge bg="info" className="ms-1">required</Badge>
              </Form.Label>
              <Form.Text className="d-block mb-2 text-muted">
                {t.jsonPayloadHelp}
              </Form.Text>
              <Form.Control
                as="textarea"
                rows={8}
                placeholder={t.jsonPayloadPlaceholder}
                value={payloadText}
                onChange={(e) => handlePayloadChange(e.target.value)}
                isInvalid={!!payloadError}
                className="font-monospace"
                aria-label={t.jsonPayload}
                data-testid="json-payload-input"
              />
              <Form.Control.Feedback type="invalid">
                {payloadError}
              </Form.Control.Feedback>
            </Form.Group>

            {/* Subject textarea */}
            <Form.Group>
              <Form.Label>{t.jsonSubject}</Form.Label>
              <Form.Text className="d-block mb-2 text-muted">
                {t.jsonSubjectHelp}
              </Form.Text>
              <Form.Control
                as="textarea"
                rows={6}
                placeholder={t.jsonSubjectPlaceholder}
                value={subjectText}
                onChange={(e) => handleSubjectChange(e.target.value)}
                isInvalid={!!subjectError}
                className="font-monospace"
                aria-label={t.jsonSubject}
                data-testid="json-subject-input"
              />
              <Form.Control.Feedback type="invalid">
                {subjectError}
              </Form.Control.Feedback>
            </Form.Group>

            {/* JSON mode action buttons */}
            <div className="d-flex gap-2">
              <Button variant="outline-secondary" size="sm" onClick={loadJsonExample} data-testid="json-load-example">
                {t.loadExample}
              </Button>
            </div>

            {/* JSON validation error */}
            {(payloadError || subjectError) && (
              <Alert variant="warning" className="mt-2">
                {t.invalidJsonDetail}
              </Alert>
            )}
          </Stack>
        </Tab>
      </Tabs>
    </Stack>
  );
};

export default ValidationEditor;
export type { ValidationEditorProps, CustomHeader };
