/**
 * Unit tests for the PolicyEditor page component.
 *
 * Verifies create mode, edit mode, save behavior, tab switching,
 * validation modal interactions, and service dropdown integration.
 */
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import PolicyEditor from './PolicyEditor';
import { PolicyService } from '../api/services/PolicyService';
import { ServiceService } from '../api/services/ServiceService';
import { UiService } from '../api/services/UiService';
import { I18nProvider } from '../i18n';
import type { Mappings, Policy, ValidationResponse } from '../services/api';
import type { ServiceList } from '../api/models/ServiceList';

// Mock services
vi.mock('../api/services/PolicyService', () => ({
  PolicyService: {
    getPolicyById: vi.fn(),
    createPolicy: vi.fn(),
    createPolicyWithId: vi.fn(),
  },
}));

vi.mock('../api/services/ServiceService', () => ({
  ServiceService: {
    getServices: vi.fn(),
    getService: vi.fn(),
    createServicePolicy: vi.fn(),
    createServicePolicyWithId: vi.fn(),
    getServicePolicyById: vi.fn(),
    getServiceTemplates: vi.fn(),
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

/** Mock service list for service dropdown tests. */
const MOCK_SERVICES: ServiceList = [
  { id: 'service-alpha', policyPath: '/service/service-alpha/policy' },
  { id: 'service-beta', policyPath: '/service/service-beta/policy' },
];

/** Empty service list for testing graceful empty state. */
const MOCK_EMPTY_SERVICES: ServiceList = [];

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

/** Renders PolicyEditor in create mode with a pre-selected service via query param. */
const renderCreateModeWithService = (serviceId: string) =>
  render(
    <MemoryRouter initialEntries={[`/new?serviceId=${serviceId}`]}>
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

/** Renders PolicyEditor in edit mode with a service context via query param. */
const renderEditModeWithService = (id: string, serviceId: string) =>
  render(
    <MemoryRouter initialEntries={[`/edit/${id}?serviceId=${serviceId}`]}>
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
    vi.mocked(PolicyService.getPolicyById).mockReset();
    vi.mocked(PolicyService.createPolicy).mockReset();
    vi.mocked(PolicyService.createPolicyWithId).mockReset();
    vi.mocked(ServiceService.getServices).mockReset();
    vi.mocked(ServiceService.createServicePolicy).mockReset();
    vi.mocked(ServiceService.createServicePolicyWithId).mockReset();
    vi.mocked(ServiceService.getServicePolicyById).mockReset();
    vi.mocked(ServiceService.getServiceTemplates).mockReset();
    vi.mocked(ServiceService.getServiceTemplates).mockResolvedValue([]);
    vi.mocked(UiService.getMappings).mockReset();
    vi.mocked(UiService.validatePolicy).mockReset();
    // Default: mappings load successfully
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);
    // Default: services load successfully with two services
    vi.mocked(ServiceService.getServices).mockResolvedValue(MOCK_SERVICES);
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

    it('calls createPolicy on save in create mode (no service)', async () => {
      const user = userEvent.setup();
      vi.mocked(PolicyService.createPolicy).mockResolvedValue(undefined as never);

      renderCreateMode();

      await waitFor(() => {
        expect(screen.getByText('Save')).toBeInTheDocument();
      });

      await user.click(screen.getByText('Save'));

      expect(PolicyService.createPolicy).toHaveBeenCalled();
      expect(ServiceService.createServicePolicy).not.toHaveBeenCalled();
    });
  });

  describe('Edit mode', () => {
    it('renders with "Edit Policy" title', async () => {
      vi.mocked(PolicyService.getPolicyById).mockResolvedValue(MOCK_EXISTING_POLICY);

      renderEditMode();

      await waitFor(() => {
        expect(screen.getByText('Edit Policy')).toBeInTheDocument();
      });
    });

    it('loads the existing policy data', async () => {
      vi.mocked(PolicyService.getPolicyById).mockResolvedValue(MOCK_EXISTING_POLICY);

      renderEditMode();

      await waitFor(() => {
        expect(PolicyService.getPolicyById).toHaveBeenCalledWith('existing-policy-1');
      });
    });

    it('calls createPolicyWithId on save in edit mode (no service)', async () => {
      const user = userEvent.setup();
      vi.mocked(PolicyService.getPolicyById).mockResolvedValue(MOCK_EXISTING_POLICY);
      vi.mocked(PolicyService.createPolicyWithId).mockResolvedValue(undefined as never);

      renderEditMode();

      await waitFor(() => {
        expect(screen.getByText('Edit Policy')).toBeInTheDocument();
      });

      await user.click(screen.getByText('Save'));

      expect(PolicyService.createPolicyWithId).toHaveBeenCalledWith(
        'existing-policy-1',
        expect.any(Object),
      );
      expect(ServiceService.createServicePolicyWithId).not.toHaveBeenCalled();
    });
  });

  describe('Service dropdown', () => {
    it('renders the service dropdown with available services', async () => {
      renderCreateMode();

      await waitFor(() => {
        expect(screen.getByLabelText('Service')).toBeInTheDocument();
      });

      const dropdown = screen.getByLabelText('Service') as HTMLSelectElement;
      // Default "no service" option + 2 services = 3 options
      expect(dropdown.options).toHaveLength(3);
      expect(dropdown.options[0].textContent).toContain('standalone policy');
      expect(dropdown.options[1].textContent).toBe('service-alpha');
      expect(dropdown.options[2].textContent).toBe('service-beta');
    });

    it('defaults to no service selected', async () => {
      renderCreateMode();

      await waitFor(() => {
        expect(screen.getByLabelText('Service')).toBeInTheDocument();
      });

      const dropdown = screen.getByLabelText('Service') as HTMLSelectElement;
      expect(dropdown.value).toBe('');
    });

    it('handles empty service list gracefully', async () => {
      vi.mocked(ServiceService.getServices).mockResolvedValue(MOCK_EMPTY_SERVICES);

      renderCreateMode();

      await waitFor(() => {
        expect(screen.getByLabelText('Service')).toBeInTheDocument();
      });

      const dropdown = screen.getByLabelText('Service') as HTMLSelectElement;
      // Only the "no service" option should be present
      expect(dropdown.options).toHaveLength(1);
      expect(dropdown.options[0].textContent).toContain('standalone policy');
    });

    it('shows a warning when service list fetch fails', async () => {
      vi.mocked(ServiceService.getServices).mockRejectedValue(new Error('Network error'));

      renderCreateMode();

      await waitFor(() => {
        expect(screen.getByText('Could not load services.')).toBeInTheDocument();
      });

      // The dropdown should not be rendered
      expect(screen.queryByLabelText('Service')).not.toBeInTheDocument();
    });

    it('editor remains usable when service list fetch fails', async () => {
      vi.mocked(ServiceService.getServices).mockRejectedValue(new Error('Network error'));
      vi.mocked(PolicyService.createPolicy).mockResolvedValue(undefined as never);
      const user = userEvent.setup();

      renderCreateMode();

      await waitFor(() => {
        expect(screen.getByText('Could not load services.')).toBeInTheDocument();
      });

      // Save button should still work (creates standalone policy)
      await user.click(screen.getByText('Save'));
      expect(PolicyService.createPolicy).toHaveBeenCalled();
    });

    it('calls createServicePolicy when a service is selected in create mode', async () => {
      const user = userEvent.setup();
      vi.mocked(ServiceService.createServicePolicy).mockResolvedValue(undefined as never);

      renderCreateMode();

      await waitFor(() => {
        expect(screen.getByLabelText('Service')).toBeInTheDocument();
      });

      // Select a service
      await user.selectOptions(screen.getByLabelText('Service'), 'service-alpha');

      await user.click(screen.getByText('Save'));

      expect(ServiceService.createServicePolicy).toHaveBeenCalledWith(
        'service-alpha',
        expect.any(Object),
      );
      expect(PolicyService.createPolicy).not.toHaveBeenCalled();
    });

    it('calls createServicePolicyWithId when a service is selected in edit mode', async () => {
      const user = userEvent.setup();
      vi.mocked(PolicyService.getPolicyById).mockResolvedValue(MOCK_EXISTING_POLICY);
      vi.mocked(ServiceService.createServicePolicyWithId).mockResolvedValue(undefined as never);

      renderEditMode();

      await waitFor(() => {
        expect(screen.getByText('Edit Policy')).toBeInTheDocument();
      });

      await waitFor(() => {
        expect(screen.getByLabelText('Service')).toBeInTheDocument();
      });

      // Select a service
      await user.selectOptions(screen.getByLabelText('Service'), 'service-beta');

      await user.click(screen.getByText('Save'));

      expect(ServiceService.createServicePolicyWithId).toHaveBeenCalledWith(
        'service-beta',
        'existing-policy-1',
        expect.any(Object),
      );
      expect(PolicyService.createPolicyWithId).not.toHaveBeenCalled();
    });

    it('reverts to root-level save when service is deselected', async () => {
      const user = userEvent.setup();
      vi.mocked(PolicyService.createPolicy).mockResolvedValue(undefined as never);

      renderCreateMode();

      await waitFor(() => {
        expect(screen.getByLabelText('Service')).toBeInTheDocument();
      });

      // Select a service then deselect it
      await user.selectOptions(screen.getByLabelText('Service'), 'service-alpha');
      await user.selectOptions(screen.getByLabelText('Service'), '');

      await user.click(screen.getByText('Save'));

      expect(PolicyService.createPolicy).toHaveBeenCalled();
      expect(ServiceService.createServicePolicy).not.toHaveBeenCalled();
    });

    it('pre-selects the service from query param', async () => {
      renderCreateModeWithService('service-alpha');

      await waitFor(() => {
        expect(screen.getByLabelText('Service')).toBeInTheDocument();
      });

      const dropdown = screen.getByLabelText('Service') as HTMLSelectElement;
      expect(dropdown.value).toBe('service-alpha');
    });

    it('fetches templates scoped to the selected service', async () => {
      renderCreateModeWithService('service-alpha');

      // The service-scoped template endpoint must be used, not the general one.
      await waitFor(() => {
        expect(ServiceService.getServiceTemplates).toHaveBeenCalledWith('service-alpha');
      });
    });

    it('loads policy via service endpoint when serviceId query param is present in edit mode', async () => {
      vi.mocked(ServiceService.getServicePolicyById).mockResolvedValue(MOCK_EXISTING_POLICY);

      renderEditModeWithService('existing-policy-1', 'service-alpha');

      await waitFor(() => {
        expect(ServiceService.getServicePolicyById).toHaveBeenCalledWith(
          'service-alpha',
          'existing-policy-1',
        );
      });

      // Should not call the root-level endpoint
      expect(PolicyService.getPolicyById).not.toHaveBeenCalled();
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
