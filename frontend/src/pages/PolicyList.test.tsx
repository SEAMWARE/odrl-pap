/**
 * Unit tests for the PolicyList page component.
 *
 * Verifies policy table rendering, delete functionality,
 * and navigation to create new policies.
 */
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import PolicyList from './PolicyList';
import { PolicyService } from '../api/services/PolicyService';
import type { Policy } from '../services/api';

// Mock the PolicyService
vi.mock('../api/services/PolicyService', () => ({
  PolicyService: {
    getPolicies: vi.fn(),
    deletePolicyById: vi.fn(),
  },
}));

/** Sample policies for testing. */
const MOCK_POLICIES: Policy[] = [
  { id: 'policy-1', odrl: '{"odrl:uid":"urn:uuid:1111"}', 'odrl:uid': 'urn:uuid:1111' },
  { id: 'policy-2', odrl: '{"odrl:uid":"urn:uuid:2222"}', 'odrl:uid': 'urn:uuid:2222' },
  { id: 'policy-3', odrl: '{"odrl:uid":"urn:uuid:3333"}', 'odrl:uid': 'urn:uuid:3333' },
] as unknown as Policy[];

/** Renders the component inside a MemoryRouter for react-router. */
const renderWithRouter = (ui: React.ReactElement) =>
  render(<MemoryRouter>{ui}</MemoryRouter>);

describe('PolicyList', () => {
  beforeEach(() => {
    vi.mocked(PolicyService.getPolicies).mockReset();
    vi.mocked(PolicyService.deletePolicyById).mockReset();
  });

  it('renders the policy table with all policies', async () => {
    vi.mocked(PolicyService.getPolicies).mockResolvedValue(MOCK_POLICIES);

    renderWithRouter(<PolicyList />);

    await waitFor(() => {
      expect(screen.getByText('policy-1')).toBeInTheDocument();
    });

    // All three policies should be rendered
    expect(screen.getByText('policy-1')).toBeInTheDocument();
    expect(screen.getByText('policy-2')).toBeInTheDocument();
    expect(screen.getByText('policy-3')).toBeInTheDocument();
  });

  it('renders column headers', async () => {
    vi.mocked(PolicyService.getPolicies).mockResolvedValue(MOCK_POLICIES);

    renderWithRouter(<PolicyList />);

    await waitFor(() => {
      expect(screen.getByText('policy-1')).toBeInTheDocument();
    });

    expect(screen.getByText('ID')).toBeInTheDocument();
    expect(screen.getByText('ODRL UID')).toBeInTheDocument();
    expect(screen.getByText('Actions')).toBeInTheDocument();
  });

  it('renders ODRL UIDs in the table', async () => {
    vi.mocked(PolicyService.getPolicies).mockResolvedValue(MOCK_POLICIES);

    renderWithRouter(<PolicyList />);

    await waitFor(() => {
      expect(screen.getByText('urn:uuid:1111')).toBeInTheDocument();
    });

    expect(screen.getByText('urn:uuid:2222')).toBeInTheDocument();
    expect(screen.getByText('urn:uuid:3333')).toBeInTheDocument();
  });

  it('renders a "New Policy" link that navigates to /new', async () => {
    vi.mocked(PolicyService.getPolicies).mockResolvedValue([]);

    renderWithRouter(<PolicyList />);

    const newPolicyLink = screen.getByText('New Policy');
    expect(newPolicyLink).toBeInTheDocument();
    expect(newPolicyLink.closest('a')).toHaveAttribute('href', '/new');
  });

  it('renders Edit links for each policy', async () => {
    vi.mocked(PolicyService.getPolicies).mockResolvedValue(MOCK_POLICIES);

    renderWithRouter(<PolicyList />);

    await waitFor(() => {
      expect(screen.getByText('policy-1')).toBeInTheDocument();
    });

    const editLinks = screen.getAllByText('Edit');
    expect(editLinks).toHaveLength(3);
    expect(editLinks[0].closest('a')).toHaveAttribute('href', '/edit/policy-1');
  });

  it('deletes a policy when delete button is clicked', async () => {
    const user = userEvent.setup();
    vi.mocked(PolicyService.getPolicies).mockResolvedValue([...MOCK_POLICIES]);
    vi.mocked(PolicyService.deletePolicyById).mockResolvedValue(undefined as never);

    renderWithRouter(<PolicyList />);

    await waitFor(() => {
      expect(screen.getByText('policy-1')).toBeInTheDocument();
    });

    // Click the first Delete button
    const deleteButtons = screen.getAllByText('Delete');
    await user.click(deleteButtons[0]);

    // deletePolicyById should have been called with the correct ID
    expect(PolicyService.deletePolicyById).toHaveBeenCalledWith('policy-1');

    // The policy should be removed from the table
    await waitFor(() => {
      expect(screen.queryByText('policy-1')).not.toBeInTheDocument();
    });
  });

  it('handles empty policy list gracefully', async () => {
    vi.mocked(PolicyService.getPolicies).mockResolvedValue([]);

    renderWithRouter(<PolicyList />);

    await waitFor(() => {
      expect(screen.getByText('Policies')).toBeInTheDocument();
    });

    // Table should render but with no data rows
    const rows = screen.queryAllByRole('row');
    // Only the header row should exist
    expect(rows.length).toBe(1);
  });

  it('renders the page title', async () => {
    vi.mocked(PolicyService.getPolicies).mockResolvedValue([]);

    renderWithRouter(<PolicyList />);

    expect(screen.getByText('Policies')).toBeInTheDocument();
  });
});
