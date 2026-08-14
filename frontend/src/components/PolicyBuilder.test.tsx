/**
 * Unit tests for the PolicyBuilder component.
 *
 * Verifies loading state, error state with retry, section rendering
 * with numbered step headers, and action dropdown integration.
 */
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import PolicyBuilder from './PolicyBuilder';
import { I18nProvider } from '../i18n';
import { clearMappingsCache } from '../hooks/useMappings';
import { UiService } from '../api/services/UiService';
import type { Mappings, OdrlPolicyJson } from '../api';
import type { FieldTemplate } from '../types';

// Mock UiService so we control the mappings response
vi.mock('../api/services/UiService', () => ({
  UiService: {
    getMappings: vi.fn(),
  },
}));

/** Realistic mock mappings. */
const MOCK_MAPPINGS: Mappings = {
  actions: [
    { name: 'odrl:read', description: 'To read a resource' },
    { name: 'odrl:use', description: 'To use a resource' },
    { name: 'dome-op:access', description: 'To access a DOME resource' },
  ],
  operators: [
    { name: 'odrl:eq', description: 'Equal to' },
    { name: 'odrl:neq', description: 'Not equal to' },
  ],
  leftOperands: [
    { name: 'dome-op:role', description: 'Role of the requesting party' },
  ],
  rightOperands: [
    { name: 'odrl:rightOperand', description: 'Right operand value' },
  ],
  assignees: [
    { name: 'odrl:assignee', description: 'Party receiving the permission' },
  ],
  targets: [
    { name: 'odrl:target', description: 'Target asset of the policy' },
  ],
  constraints: [
    { name: 'odrl:and', description: 'All constraints must be satisfied' },
  ],
};

/** Empty ODRL policy for testing. */
const EMPTY_POLICY: OdrlPolicyJson = {
  '@context': 'http://www.w3.org/ns/odrl/2/',
  '@type': 'odrl:Policy',
  'odrl:permission': {},
};

/** Wraps a component with I18nProvider. */
const renderWithProviders = (ui: React.ReactElement) =>
  render(<I18nProvider>{ui}</I18nProvider>);

describe('PolicyBuilder', () => {
  beforeEach(() => {
    clearMappingsCache();
    vi.mocked(UiService.getMappings).mockReset();
  });

  it('shows loading spinner while mappings are being fetched', () => {
    vi.mocked(UiService.getMappings).mockReturnValue(new Promise(() => {}));

    renderWithProviders(
      <PolicyBuilder policy={EMPTY_POLICY} setPolicy={() => {}} />,
    );

    expect(screen.getByRole('status')).toBeInTheDocument();
    expect(screen.getByText(/loading policy mappings/i)).toBeInTheDocument();
  });

  it('shows error alert with retry button on fetch failure', async () => {
    vi.mocked(UiService.getMappings).mockRejectedValue(
      new Error('Network error'),
    );

    renderWithProviders(
      <PolicyBuilder policy={EMPTY_POLICY} setPolicy={() => {}} />,
    );

    await waitFor(() => {
      expect(screen.getByText(/failed to load/i)).toBeInTheDocument();
    });

    expect(screen.getByRole('button', { name: /retry/i })).toBeInTheDocument();
  });

  it('retries fetching mappings when retry button is clicked', async () => {
    const user = userEvent.setup();

    // First call fails
    vi.mocked(UiService.getMappings).mockRejectedValueOnce(
      new Error('Network error'),
    );

    renderWithProviders(
      <PolicyBuilder policy={EMPTY_POLICY} setPolicy={() => {}} />,
    );

    await waitFor(() => {
      expect(screen.getByText(/failed to load/i)).toBeInTheDocument();
    });

    // Set up success for retry
    vi.mocked(UiService.getMappings).mockResolvedValueOnce(MOCK_MAPPINGS);

    await user.click(screen.getByRole('button', { name: /retry/i }));

    await waitFor(() => {
      expect(screen.getByText('Target')).toBeInTheDocument();
    });

    expect(UiService.getMappings).toHaveBeenCalledTimes(2);
  });

  it('renders all four numbered step sections after loading', async () => {
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);

    renderWithProviders(
      <PolicyBuilder policy={EMPTY_POLICY} setPolicy={() => {}} />,
    );

    await waitFor(() => {
      expect(screen.getByText('Target')).toBeInTheDocument();
    });

    // All four sections should be present
    expect(screen.getByText('Target')).toBeInTheDocument();
    expect(screen.getByText('Assignee')).toBeInTheDocument();
    expect(screen.getByText('Action')).toBeInTheDocument();
    expect(screen.getByText('Constraints')).toBeInTheDocument();
  });

  it('renders step numbers (1-4) as badges', async () => {
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);

    renderWithProviders(
      <PolicyBuilder policy={EMPTY_POLICY} setPolicy={() => {}} />,
    );

    await waitFor(() => {
      expect(screen.getByText('Target')).toBeInTheDocument();
    });

    // Step badges should contain numbers 1 through 4
    expect(screen.getByText('1')).toBeInTheDocument();
    expect(screen.getByText('2')).toBeInTheDocument();
    expect(screen.getByText('3')).toBeInTheDocument();
    expect(screen.getByText('4')).toBeInTheDocument();
  });

  it('displays contextual help text for each section', async () => {
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);

    renderWithProviders(
      <PolicyBuilder policy={EMPTY_POLICY} setPolicy={() => {}} />,
    );

    await waitFor(() => {
      expect(screen.getByText('Target')).toBeInTheDocument();
    });

    // Help text should be present for each section
    expect(screen.getByText(/define what resource/i)).toBeInTheDocument();
    expect(screen.getByText(/specify who is granted/i)).toBeInTheDocument();
    expect(screen.getByText(/choose what operation/i)).toBeInTheDocument();
    expect(screen.getByText(/add conditions/i)).toBeInTheDocument();
  });

  it('renders the action dropdown with namespace-grouped items', async () => {
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);

    const { container } = renderWithProviders(
      <PolicyBuilder policy={EMPTY_POLICY} setPolicy={() => {}} />,
    );

    await waitFor(() => {
      expect(screen.getByText('Action')).toBeInTheDocument();
    });

    // Check that optgroups exist in any dropdown
    const optgroups = container.querySelectorAll('optgroup');
    expect(optgroups.length).toBeGreaterThan(0);

    // Check action options are present
    expect(screen.getByText(/odrl:read/)).toBeInTheDocument();
    expect(screen.getByText(/dome-op:access/)).toBeInTheDocument();
  });

  it('calls setPolicy when action is selected', async () => {
    const user = userEvent.setup();
    const setPolicy = vi.fn();

    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);

    renderWithProviders(
      <PolicyBuilder policy={EMPTY_POLICY} setPolicy={setPolicy} />,
    );

    await waitFor(() => {
      expect(screen.getByText('Action')).toBeInTheDocument();
    });

    // Find the action select by its aria-label
    const actionSelect = screen.getByLabelText('Action');
    await user.selectOptions(actionSelect, 'odrl:read');

    // Find the call that set the action field
    const actionCall = setPolicy.mock.calls.find(
      (call) => call[0]?.['odrl:permission']?.['odrl:action'] === 'odrl:read',
    );
    expect(actionCall).toBeDefined();
    expect(actionCall![0]['odrl:permission']['odrl:action']).toBe('odrl:read');
  });

  it('renders PolicySummary sidebar', async () => {
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);

    renderWithProviders(
      <PolicyBuilder policy={EMPTY_POLICY} setPolicy={() => {}} />,
    );

    await waitFor(() => {
      expect(screen.getByText('Policy Summary')).toBeInTheDocument();
    });
  });

  it('does not show template banner when no template is provided', async () => {
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);

    renderWithProviders(
      <PolicyBuilder policy={EMPTY_POLICY} setPolicy={() => {}} />,
    );

    await waitFor(() => {
      expect(screen.getByText('Target')).toBeInTheDocument();
    });

    expect(screen.queryByTestId('template-banner')).not.toBeInTheDocument();
  });

  it('does not show locked badges when no template is provided', async () => {
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);

    renderWithProviders(
      <PolicyBuilder policy={EMPTY_POLICY} setPolicy={() => {}} />,
    );

    await waitFor(() => {
      expect(screen.getByText('Target')).toBeInTheDocument();
    });

    expect(screen.queryAllByTestId('locked-badge')).toHaveLength(0);
  });
});

/** Template fixture for template-mode tests. */
const TEMPLATE_WITH_LOCKED_ACTION: FieldTemplate = {
  id: 'test-locked-action',
  name: 'Locked Action Template',
  description: 'A test template with the action field locked',
  category: 'Testing',
  skeleton: {
    '@context': 'http://www.w3.org/ns/odrl/2/',
    '@type': 'odrl:Policy',
    'odrl:permission': {
      'odrl:action': 'odrl:read',
    },
  },
  editableFields: [
    {
      path: 'odrl:permission.odrl:target',
      label: 'Target',
      description: 'Choose the target resource',
      type: 'dropdown',
      required: true,
    },
  ],
  lockedFields: [
    'odrl:permission.odrl:action',
    'odrl:permission.odrl:assignee',
  ],
};

describe('PolicyBuilder with template', () => {
  beforeEach(() => {
    clearMappingsCache();
    vi.mocked(UiService.getMappings).mockReset();
  });

  it('shows the template banner with template name', async () => {
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);

    renderWithProviders(
      <PolicyBuilder
        policy={EMPTY_POLICY}
        setPolicy={() => {}}
        fieldTemplate={TEMPLATE_WITH_LOCKED_ACTION}
      />,
    );

    await waitFor(() => {
      expect(screen.getByTestId('template-banner')).toBeInTheDocument();
    });

    expect(
      screen.getByText(/Locked Action Template/),
    ).toBeInTheDocument();
  });

  it('shows locked badges on locked sections', async () => {
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);

    renderWithProviders(
      <PolicyBuilder
        policy={EMPTY_POLICY}
        setPolicy={() => {}}
        fieldTemplate={TEMPLATE_WITH_LOCKED_ACTION}
      />,
    );

    await waitFor(() => {
      expect(screen.getByText('Target')).toBeInTheDocument();
    });

    const lockedBadges = screen.getAllByTestId('locked-badge');
    // Action and Assignee are locked
    expect(lockedBadges.length).toBe(2);
  });

  it('disables the action dropdown when action is locked', async () => {
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);

    renderWithProviders(
      <PolicyBuilder
        policy={EMPTY_POLICY}
        setPolicy={() => {}}
        fieldTemplate={TEMPLATE_WITH_LOCKED_ACTION}
      />,
    );

    await waitFor(() => {
      expect(screen.getByText('Action')).toBeInTheDocument();
    });

    const actionSelect = screen.getByLabelText('Action');
    expect(actionSelect).toBeDisabled();
  });

  it('shows the locked field tooltip on the action section', async () => {
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);

    renderWithProviders(
      <PolicyBuilder
        policy={EMPTY_POLICY}
        setPolicy={() => {}}
        fieldTemplate={TEMPLATE_WITH_LOCKED_ACTION}
      />,
    );

    await waitFor(() => {
      expect(screen.getByText('Action')).toBeInTheDocument();
    });

    expect(screen.getByTestId('action-locked-hint')).toBeInTheDocument();
    expect(
      screen.getByText(/locked by the template/i),
    ).toBeInTheDocument();
  });

  it('renders all four step sections even with template', async () => {
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);

    renderWithProviders(
      <PolicyBuilder
        policy={EMPTY_POLICY}
        setPolicy={() => {}}
        fieldTemplate={TEMPLATE_WITH_LOCKED_ACTION}
      />,
    );

    await waitFor(() => {
      expect(screen.getByText('Target')).toBeInTheDocument();
    });

    expect(screen.getByText('Assignee')).toBeInTheDocument();
    expect(screen.getByText('Action')).toBeInTheDocument();
    expect(screen.getByText('Constraints')).toBeInTheDocument();
  });

  it('calls setPolicy with skeleton data on mount', async () => {
    const setPolicy = vi.fn();
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);

    renderWithProviders(
      <PolicyBuilder
        policy={EMPTY_POLICY}
        setPolicy={setPolicy}
        fieldTemplate={TEMPLATE_WITH_LOCKED_ACTION}
      />,
    );

    await waitFor(() => {
      expect(setPolicy).toHaveBeenCalled();
    });

    // The first call should apply the skeleton
    const skeletonCall = setPolicy.mock.calls[0][0];
    expect(skeletonCall['odrl:permission']['odrl:action']).toBe('odrl:read');
  });
});
