/**
 * Unit tests for the ValidationEditor component.
 *
 * Verifies mode toggle between HTTP Request and JSON Payload modes,
 * HTTP request field rendering, JSON payload field rendering,
 * example loading buttons, and JWT helper functionality.
 */
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import ValidationEditor from './ValidationEditor';
import type { ValidationMode } from './ValidationEditor';
import { I18nProvider } from '../i18n';
import { TestRequest } from '../api/models/TestRequest';
import type { GenericJsonInput } from '../api/models/GenericJsonInput';

/** Default test request for tests. */
const DEFAULT_TEST_REQUEST: TestRequest = {
  method: TestRequest.method.GET,
  host: 'example.com',
  path: '/',
  headers: {
    'content-type': 'application/json',
  },
  body: {},
};

/** Default JSON input for tests. */
const DEFAULT_JSON_INPUT: GenericJsonInput = {
  payload: {},
};

/** Wraps a component with required providers. */
const renderWithProviders = (ui: React.ReactElement) =>
  render(<I18nProvider>{ui}</I18nProvider>);

/** Creates a default set of props for the ValidationEditor. */
const createDefaultProps = (overrides: Partial<{
  testRequest: TestRequest;
  setTestRequest: (tr: TestRequest) => void;
  jsonInput: GenericJsonInput;
  setJsonInput: (ji: GenericJsonInput) => void;
  mode: ValidationMode;
  setMode: (m: ValidationMode) => void;
}> = {}) => ({
  testRequest: DEFAULT_TEST_REQUEST,
  setTestRequest: vi.fn(),
  jsonInput: DEFAULT_JSON_INPUT,
  setJsonInput: vi.fn(),
  mode: 'httpRequest' as ValidationMode,
  setMode: vi.fn(),
  ...overrides,
});

describe('ValidationEditor', () => {
  beforeEach(() => {
    vi.restoreAllMocks();
  });

  describe('mode toggle', () => {
    it('renders both mode tabs', () => {
      const props = createDefaultProps();
      renderWithProviders(<ValidationEditor {...props} />);

      expect(screen.getByText('HTTP Request')).toBeInTheDocument();
      expect(screen.getByText('JSON Payload')).toBeInTheDocument();
    });

    it('shows HTTP Request fields by default', () => {
      const props = createDefaultProps({ mode: 'httpRequest' });
      renderWithProviders(<ValidationEditor {...props} />);

      expect(screen.getByLabelText('Method')).toBeInTheDocument();
      expect(screen.getByLabelText('Host')).toBeInTheDocument();
      expect(screen.getByLabelText('Path')).toBeInTheDocument();
    });

    it('calls setMode when JSON Payload tab is clicked', async () => {
      const user = userEvent.setup();
      const setMode = vi.fn();
      const props = createDefaultProps({ setMode });
      renderWithProviders(<ValidationEditor {...props} />);

      await user.click(screen.getByText('JSON Payload'));

      expect(setMode).toHaveBeenCalledWith('jsonPayload');
    });

    it('shows JSON Payload fields when in jsonPayload mode', () => {
      const props = createDefaultProps({ mode: 'jsonPayload' });
      renderWithProviders(<ValidationEditor {...props} />);

      expect(screen.getByTestId('json-payload-input')).toBeInTheDocument();
      expect(screen.getByTestId('json-subject-input')).toBeInTheDocument();
    });
  });

  describe('HTTP Request mode', () => {
    it('renders the protocol dropdown', () => {
      const props = createDefaultProps();
      renderWithProviders(<ValidationEditor {...props} />);

      expect(screen.getByLabelText('Protocol')).toBeInTheDocument();
    });

    it('renders authorization type dropdown', () => {
      const props = createDefaultProps();
      renderWithProviders(<ValidationEditor {...props} />);

      expect(screen.getByLabelText('Authorization Type')).toBeInTheDocument();
    });

    it('renders the body textarea', () => {
      const props = createDefaultProps();
      renderWithProviders(<ValidationEditor {...props} />);

      expect(screen.getByLabelText('Body')).toBeInTheDocument();
    });

    it('renders Load Example and Copy as cURL buttons', () => {
      const props = createDefaultProps();
      renderWithProviders(<ValidationEditor {...props} />);

      expect(screen.getByTestId('http-load-example')).toBeInTheDocument();
      expect(screen.getByText('Copy as cURL')).toBeInTheDocument();
    });

    it('pre-fills example data when Load Example is clicked', async () => {
      const user = userEvent.setup();
      const setTestRequest = vi.fn();
      const props = createDefaultProps({ setTestRequest });
      renderWithProviders(<ValidationEditor {...props} />);

      await user.click(screen.getByTestId('http-load-example'));

      expect(setTestRequest).toHaveBeenCalledWith(
        expect.objectContaining({
          host: 'api.example.com',
          path: '/ngsi-ld/v1/entities/urn:example:product:123',
        }),
      );
    });

    it('shows custom header management', () => {
      const props = createDefaultProps();
      renderWithProviders(<ValidationEditor {...props} />);

      expect(screen.getByText('Custom Headers')).toBeInTheDocument();
      expect(screen.getByText('Add Header')).toBeInTheDocument();
    });

    it('adds a custom header row when Add Header is clicked', async () => {
      const user = userEvent.setup();
      const props = createDefaultProps();
      renderWithProviders(<ValidationEditor {...props} />);

      await user.click(screen.getByText('Add Header'));

      // Should show header name and value inputs
      expect(screen.getByPlaceholderText('Header Name')).toBeInTheDocument();
      expect(screen.getByPlaceholderText('Header Value')).toBeInTheDocument();
    });

    it('removes a custom header when Remove is clicked', async () => {
      const user = userEvent.setup();
      const props = createDefaultProps();
      renderWithProviders(<ValidationEditor {...props} />);

      // Add a header
      await user.click(screen.getByText('Add Header'));
      expect(screen.getByPlaceholderText('Header Name')).toBeInTheDocument();

      // Remove the header
      await user.click(screen.getByText('Remove'));
      expect(screen.queryByPlaceholderText('Header Name')).not.toBeInTheDocument();
    });
  });

  describe('JWT helper', () => {
    it('shows JWT helper when JWT auth type is selected', async () => {
      const user = userEvent.setup();
      const props = createDefaultProps();
      renderWithProviders(<ValidationEditor {...props} />);

      await user.selectOptions(screen.getByLabelText('Authorization Type'), 'jwt');

      expect(screen.getByLabelText('JWT Payload (JSON)')).toBeInTheDocument();
      expect(screen.getByText('Generate Unsigned JWT')).toBeInTheDocument();
    });

    it('generates a valid unsigned JWT and sets authorization header', async () => {
      const user = userEvent.setup();
      const setTestRequest = vi.fn();
      const props = createDefaultProps({ setTestRequest });
      renderWithProviders(<ValidationEditor {...props} />);

      await user.selectOptions(screen.getByLabelText('Authorization Type'), 'jwt');

      // Use fireEvent.change to avoid userEvent interpreting braces as keyboard modifiers
      const textarea = screen.getByLabelText('JWT Payload (JSON)');
      fireEvent.change(textarea, { target: { value: '{"sub":"test"}' } });

      await user.click(screen.getByText('Generate Unsigned JWT'));

      // Verify setTestRequest was called with Bearer token in authorization header
      expect(setTestRequest).toHaveBeenCalledWith(
        expect.objectContaining({
          headers: expect.objectContaining({
            authorization: expect.stringMatching(/^Bearer [A-Za-z0-9_-]+\.[A-Za-z0-9_-]+\.$/),
          }),
        }),
      );
    });

    it('shows JWT preview after generating a token', async () => {
      const user = userEvent.setup();

      // Set up with an already-generated JWT in headers
      const header = btoa(JSON.stringify({ alg: 'none', typ: 'JWT' })).replace(/=/g, '');
      const payload = btoa(JSON.stringify({ sub: 'test' })).replace(/=/g, '');
      const jwt = `${header}.${payload}.`;

      const testRequestWithJwt: TestRequest = {
        ...DEFAULT_TEST_REQUEST,
        headers: {
          'content-type': 'application/json',
          authorization: `Bearer ${jwt}`,
        },
      };

      const props = createDefaultProps({ testRequest: testRequestWithJwt });
      renderWithProviders(<ValidationEditor {...props} />);

      // Select JWT auth type to see the preview
      await user.selectOptions(screen.getByLabelText('Authorization Type'), 'jwt');

      expect(screen.getByTestId('jwt-preview')).toBeInTheDocument();
    });
  });

  describe('JSON Payload mode', () => {
    it('renders payload and subject textareas', () => {
      const props = createDefaultProps({ mode: 'jsonPayload' });
      renderWithProviders(<ValidationEditor {...props} />);

      expect(screen.getByTestId('json-payload-input')).toBeInTheDocument();
      expect(screen.getByTestId('json-subject-input')).toBeInTheDocument();
    });

    it('shows help text for payload and subject fields', () => {
      const props = createDefaultProps({ mode: 'jsonPayload' });
      renderWithProviders(<ValidationEditor {...props} />);

      expect(screen.getByText(/JSON data to evaluate/i)).toBeInTheDocument();
      expect(screen.getByText(/identity or credential information/i)).toBeInTheDocument();
    });

    it('renders Load Example button in JSON mode', () => {
      const props = createDefaultProps({ mode: 'jsonPayload' });
      renderWithProviders(<ValidationEditor {...props} />);

      expect(screen.getByTestId('json-load-example')).toBeInTheDocument();
    });

    it('pre-fills example JSON data when Load Example is clicked', async () => {
      const user = userEvent.setup();
      const setJsonInput = vi.fn();
      const props = createDefaultProps({ mode: 'jsonPayload', setJsonInput });
      renderWithProviders(<ValidationEditor {...props} />);

      await user.click(screen.getByTestId('json-load-example'));

      expect(setJsonInput).toHaveBeenCalledWith(
        expect.objectContaining({
          payload: expect.objectContaining({
            type: 'Product',
          }),
          subject: expect.objectContaining({
            type: 'VerifiableCredential',
          }),
        }),
      );
    });

    it('shows validation error for invalid JSON in payload', async () => {
      const props = createDefaultProps({ mode: 'jsonPayload' });
      renderWithProviders(<ValidationEditor {...props} />);

      const payloadInput = screen.getByTestId('json-payload-input');
      // Use fireEvent.change to avoid userEvent interpreting braces as keyboard modifiers
      fireEvent.change(payloadInput, { target: { value: '{invalid json' } });

      await waitFor(() => {
        expect(screen.getByText('Invalid JSON syntax')).toBeInTheDocument();
      });
    });

    it('calls setJsonInput with parsed JSON when valid payload is entered', async () => {
      const setJsonInput = vi.fn();
      const props = createDefaultProps({ mode: 'jsonPayload', setJsonInput });
      renderWithProviders(<ValidationEditor {...props} />);

      const payloadInput = screen.getByTestId('json-payload-input');
      // Use fireEvent.change to avoid userEvent interpreting braces as keyboard modifiers
      fireEvent.change(payloadInput, { target: { value: '{"key":"value"}' } });

      await waitFor(() => {
        // Find the most recent call with the correct payload
        const calls = setJsonInput.mock.calls;
        const lastCall = calls[calls.length - 1];
        expect(lastCall[0].payload).toEqual({ key: 'value' });
      });
    });
  });
});
