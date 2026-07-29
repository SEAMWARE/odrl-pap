/**
 * Unit tests for the PolicyEditor page component.
 *
 * Verifies create mode, edit mode, save behavior, tab switching,
 * and validation modal interactions.
 */
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import PolicyEditor from './PolicyEditor';
import { PapService } from '../api/services/PapService';
import { UiService } from '../api/services/UiService';
import { I18nProvider } from '../i18n';
import type { Mappings, Policy, ValidationResponse } from '../services/api';

// Mock services
vi.mock('../api/services/PapService', () => ({
  PapService: {
    getPolicyById: vi.fn(),
    createPolicy: vi.fn(),
    createPolicyWithId: vi.fn(),
  },
}));

vi.mock('../api/services/UiService', () => ({
  UiService: {
    getMappings: vi.fn(),
    validatePolicy: vi.fn(),
  },
}));

// Mock crypto.randomUUID for deterministic test output
const MOCK_UUID = '00000000-1111-2222-3333-444444444444';
vi.stubGlobal('crypto', {
  ...globalThis.crypto,
  randomUUID: () => MOCK_UUID,
});

/** Mock mappings for the PolicyBuilder sub-component. */
const MOCK_MAPPINGS: Mappings = {
  actions: [
    { name: 'odrl:read', description: 'To read a resource' },
  ],
  operators: [
    { name: 'odrl:eq', description: 'Equal to' },
  ],
  leftOperands: [
    { name: 'dome-op:role', description: 'Role' },
  ],
  rightOperands: [],
  assignees: [],
  targets: [],
  constraints: [],
};

/** Mock existing policy for edit mode. */
const MOCK_EXISTING_POLICY: Policy = {
  id: 'existing-policy-1',
  odrl: JSON.stringify({
    '@context': 'http://www.w3.org/ns/odrl/2/',
    '@type': 'odrl:Policy',
    'odrl:uid': 'urn:uuid:existing',
    'odrl:permission': {
      'odrl:action': 'odrl:read',
    },
  }),
} as unknown as Policy;

/** Mock validation responses. */
const MOCK_ALLOW_RESULT: ValidationResponse = {
  allow: true,
  explanation: [],
};

/** Renders PolicyEditor in create mode with providers. */
const renderCreateMode = () =>
  render(
    <MemoryRouter initialEntries={['/new']}>
      <I18nProvider>
        <Routes>
          <Route path="/new" element={<PolicyEditor />} />
          <Route path="/" element={<div>Policy List</div>} />
        </Routes>
      </I18nProvider>
    </MemoryRouter>,
  );

/** Renders PolicyEditor in edit mode with providers. */
const renderEditMode = (id = 'existing-policy-1') =>
  render(
    <MemoryRouter initialEntries={[`/edit/${id}`]}>
      <I18nProvider>
        <Routes>
          <Route path="/edit/:id" element={<PolicyEditor />} />
          <Route path="/" element={<div>Policy List</div>} />
        </Routes>
      </I18nProvider>
    </MemoryRouter>,
  );

describe('PolicyEditor', () => {
  beforeEach(() => {
    vi.mocked(PapService.getPolicyById).mockReset();
    vi.mocked(PapService.createPolicy).mockReset();
    vi.mocked(PapService.createPolicyWithId).mockReset();
    vi.mocked(UiService.getMappings).mockReset();
    vi.mocked(UiService.validatePolicy).mockReset();
    // Default: mappings load successfully
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);
    // Clear session storage between tests
    sessionStorage.clear();
  });

  describe('Create mode', () => {
    it('renders with "New Policy" title', async () => {
      renderCreateMode();

      await waitFor(() => {
        expect(screen.getByText('New Policy')).toBeInTheDocument();
      });
    });

    it('renders Policy Builder and Raw ODRL tabs', async () => {
      renderCreateMode();

      await waitFor(() => {
        expect(screen.getByText('Policy Builder')).toBeInTheDocument();
      });

      expect(screen.getByText('Raw ODRL')).toBeInTheDocument();
    });

    it('renders Save, Cancel, and Validate buttons', async () => {
      renderCreateMode();

      await waitFor(() => {
        expect(screen.getByText('Save')).toBeInTheDocument();
      });

      expect(screen.getByText('Cancel')).toBeInTheDocument();
      expect(screen.getByText('Validate')).toBeInTheDocument();
    });

    it('shows the raw ODRL JSON when switching tabs', async () => {
      const user = userEvent.setup();
      renderCreateMode();

      await waitFor(() => {
        expect(screen.getByText('Raw ODRL')).toBeInTheDocument();
      });

      await user.click(screen.getByText('Raw ODRL'));

      // The ODRL tab renders a large textarea; find it among all textboxes
      const textareas = screen.getAllByRole('textbox');
      // Find the textarea that contains ODRL JSON (the raw editor)
      const odrlTextarea = textareas.find(
        (el) => el.tagName === 'TEXTAREA' && (el as HTMLTextAreaElement).value.includes('odrl:Policy'),
      );
      expect(odrlTextarea).toBeDefined();
      const parsed = JSON.parse((odrlTextarea as HTMLTextAreaElement).value);
      expect(parsed['@type']).toBe('odrl:Policy');
    });

    it('calls createPolicy on save in create mode', async () => {
      const user = userEvent.setup();
      vi.mocked(PapService.createPolicy).mockResolvedValue(undefined as never);

      renderCreateMode();

      await waitFor(() => {
        expect(screen.getByText('Save')).toBeInTheDocument();
      });

      await user.click(screen.getByText('Save'));

      expect(PapService.createPolicy).toHaveBeenCalled();
    });
  });

  describe('Edit mode', () => {
    it('renders with "Edit Policy" title', async () => {
      vi.mocked(PapService.getPolicyById).mockResolvedValue(MOCK_EXISTING_POLICY);

      renderEditMode();

      await waitFor(() => {
        expect(screen.getByText('Edit Policy')).toBeInTheDocument();
      });
    });

    it('loads the existing policy data', async () => {
      vi.mocked(PapService.getPolicyById).mockResolvedValue(MOCK_EXISTING_POLICY);

      renderEditMode();

      await waitFor(() => {
        expect(PapService.getPolicyById).toHaveBeenCalledWith('existing-policy-1');
      });
    });

    it('calls createPolicyWithId on save in edit mode', async () => {
      const user = userEvent.setup();
      vi.mocked(PapService.getPolicyById).mockResolvedValue(MOCK_EXISTING_POLICY);
      vi.mocked(PapService.createPolicyWithId).mockResolvedValue(undefined as never);

      renderEditMode();

      await waitFor(() => {
        expect(screen.getByText('Edit Policy')).toBeInTheDocument();
      });

      await user.click(screen.getByText('Save'));

      expect(PapService.createPolicyWithId).toHaveBeenCalledWith(
        'existing-policy-1',
        expect.any(Object),
      );
    });
  });

  describe('Validation modal', () => {
    it('opens the validation modal when Validate is clicked', async () => {
      const user = userEvent.setup();
      renderCreateMode();

      await waitFor(() => {
        expect(screen.getByText('Validate')).toBeInTheDocument();
      });

      await user.click(screen.getByText('Validate'));

      await waitFor(() => {
        expect(screen.getByTestId('validation-modal')).toBeInTheDocument();
      });
    });

    it('shows "Validate Policy" as the modal title', async () => {
      const user = userEvent.setup();
      renderCreateMode();

      await waitFor(() => {
        expect(screen.getByText('Validate')).toBeInTheDocument();
      });

      await user.click(screen.getByText('Validate'));

      await waitFor(() => {
        expect(screen.getByText('Validate Policy')).toBeInTheDocument();
      });
    });

    it('runs validation and displays results', async () => {
      const user = userEvent.setup();
      vi.mocked(UiService.validatePolicy).mockResolvedValue(MOCK_ALLOW_RESULT);

      renderCreateMode();

      await waitFor(() => {
        expect(screen.getByText('Validate')).toBeInTheDocument();
      });

      // Open modal
      await user.click(screen.getByText('Validate'));

      await waitFor(() => {
        expect(screen.getByText('Run Validation')).toBeInTheDocument();
      });

      // Click Run Validation
      await user.click(screen.getByText('Run Validation'));

      // Wait for result display
      await waitFor(() => {
        expect(screen.getByText(/request allowed/i)).toBeInTheDocument();
      });
    });

    it('shows Test Again button after validation result', async () => {
      const user = userEvent.setup();
      vi.mocked(UiService.validatePolicy).mockResolvedValue(MOCK_ALLOW_RESULT);

      renderCreateMode();

      await waitFor(() => {
        expect(screen.getByText('Validate')).toBeInTheDocument();
      });

      await user.click(screen.getByText('Validate'));

      await waitFor(() => {
        expect(screen.getByText('Run Validation')).toBeInTheDocument();
      });

      await user.click(screen.getByText('Run Validation'));

      await waitFor(() => {
        expect(screen.getByText('Test Again')).toBeInTheDocument();
      });
    });

    it('closes the validation modal when Close is clicked', async () => {
      const user = userEvent.setup();
      renderCreateMode();

      await waitFor(() => {
        expect(screen.getByText('Validate')).toBeInTheDocument();
      });

      await user.click(screen.getByText('Validate'));

      await waitFor(() => {
        expect(screen.getByTestId('validation-modal')).toBeInTheDocument();
      });

      await user.click(screen.getByText('Close'));

      await waitFor(() => {
        expect(screen.queryByTestId('validation-modal')).not.toBeInTheDocument();
      });
    });
  });
});
