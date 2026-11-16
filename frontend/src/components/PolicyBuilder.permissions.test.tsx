/**
 * PolicyBuilder with `odrl:permission` written as an array (P1-1): the Builder edits the first
 * permission, keeps the array shape and leaves the other permissions untouched.
 */
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import PolicyBuilder from './PolicyBuilder';
import { I18nProvider } from '../i18n';
import { clearMappingsCache } from '../hooks/useMappings';
import { UiService } from '../api/services/UiService';
import type { Mappings, OdrlPolicyJson } from '../api';

vi.mock('../api/services/UiService', () => ({
  UiService: { getMappings: vi.fn() },
}));

const MOCK_MAPPINGS: Mappings = {
  actions: [
    { name: 'odrl:read', description: 'To read a resource' },
    { name: 'odrl:use', description: 'To use a resource' },
  ],
  operators: [{ name: 'odrl:eq', description: 'Equal to' }],
  leftOperands: [{ name: 'dome-op:role', description: 'Role' }],
  rightOperands: [],
  assignees: [{ name: 'odrl:assignee', description: 'Assignee' }],
  targets: [{ name: 'odrl:target', description: 'Target' }],
  constraints: [],
};

const FIRST = { 'odrl:target': 'https://a.example/first', 'odrl:action': 'odrl:read' };
const SECOND = { 'odrl:target': 'https://a.example/second', 'odrl:action': 'odrl:use' };

const policyWith = (permission: unknown): OdrlPolicyJson =>
  ({ '@context': { odrl: 'http://www.w3.org/ns/odrl/2/' }, '@type': 'odrl:Policy', 'odrl:permission': permission }) as OdrlPolicyJson;

const renderBuilder = (policy: OdrlPolicyJson, setPolicy = vi.fn()) => {
  render(
    <I18nProvider>
      <PolicyBuilder policy={policy} setPolicy={setPolicy} showSummary={false} />
    </I18nProvider>,
  );
  return setPolicy;
};

describe('PolicyBuilder with an array of permissions', () => {
  beforeEach(() => {
    clearMappingsCache();
    vi.mocked(UiService.getMappings).mockReset();
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);
  });

  it('shows the content of the first permission', async () => {
    renderBuilder(policyWith([FIRST]));
    await waitFor(() => expect(screen.getByLabelText('Action')).toHaveValue('odrl:read'));
    expect(screen.getByDisplayValue('https://a.example/first')).toBeInTheDocument();
  });

  it('keeps the array shape when editing a one-element array', async () => {
    const user = userEvent.setup();
    const setPolicy = renderBuilder(policyWith([FIRST]));
    await waitFor(() => expect(screen.getByLabelText('Action')).toBeInTheDocument());
    await user.selectOptions(screen.getByLabelText('Action'), 'odrl:use');

    const written = setPolicy.mock.calls.at(-1)![0]['odrl:permission'];
    expect(Array.isArray(written)).toBe(true);
    expect(written).toEqual([{ ...FIRST, 'odrl:action': 'odrl:use' }]);
  });

  it('edits only the first permission of several and leaves the rest untouched', async () => {
    const user = userEvent.setup();
    const setPolicy = renderBuilder(policyWith([FIRST, SECOND]));
    await waitFor(() => expect(screen.getByLabelText('Action')).toBeInTheDocument());
    await user.selectOptions(screen.getByLabelText('Action'), 'odrl:use');

    const written = setPolicy.mock.calls.at(-1)![0]['odrl:permission'];
    expect(written).toEqual([{ ...FIRST, 'odrl:action': 'odrl:use' }, SECOND]);
    expect(written[1]).toBe(SECOND);
  });

  it('adds a constraint to the first permission without turning the array into an object', async () => {
    const user = userEvent.setup();
    const setPolicy = renderBuilder(policyWith([FIRST, SECOND]));
    await waitFor(() => expect(screen.getByRole('button', { name: /add constraint/i })).toBeInTheDocument());
    await user.click(screen.getByRole('button', { name: /add constraint/i }));
    await user.selectOptions(screen.getByLabelText(/select left operand 1/i), 'dome-op:role');

    const written = setPolicy.mock.calls.at(-1)![0]['odrl:permission'];
    expect(Array.isArray(written)).toBe(true);
    expect(written).toHaveLength(2);
    expect(written[0]['odrl:constraint']).toBeDefined();
    expect(written[1]).toBe(SECOND);
  });

  it('warns that only the first permission is editable when there are several', async () => {
    renderBuilder(policyWith([FIRST, SECOND]));
    await waitFor(() => expect(screen.getByTestId('multiple-permissions-notice')).toBeInTheDocument());
  });

  it('shows no notice for a single permission, as object or as one-element array', async () => {
    renderBuilder(policyWith([FIRST]));
    await waitFor(() => expect(screen.getByLabelText('Action')).toBeInTheDocument());
    expect(screen.queryByTestId('multiple-permissions-notice')).not.toBeInTheDocument();
  });

  it('still writes an object when the permission is an object', async () => {
    const user = userEvent.setup();
    const setPolicy = renderBuilder(policyWith(FIRST));
    await waitFor(() => expect(screen.getByLabelText('Action')).toBeInTheDocument());
    await user.selectOptions(screen.getByLabelText('Action'), 'odrl:use');

    expect(setPolicy.mock.calls.at(-1)![0]['odrl:permission']).toEqual({ ...FIRST, 'odrl:action': 'odrl:use' });
  });
});
