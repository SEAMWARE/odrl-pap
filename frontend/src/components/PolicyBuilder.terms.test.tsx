/**
 * PolicyBuilder with ODRL terms written prefixed, bare or mixed (P1-5): the Builder shows the same
 * state for all of them and an edit keeps the keys the policy already uses.
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
    { name: 'odrl:read', description: 'read' },
    { name: 'odrl:use', description: 'use' },
  ],
  operators: [
    { name: 'odrl:eq', description: 'eq' },
    { name: 'odrl:neq', description: 'neq' },
  ],
  leftOperands: [{ name: 'dome-op:role', description: 'Role' }],
  rightOperands: [],
  assignees: [],
  targets: [],
  constraints: [],
};

const CONSTRAINT = (k: (t: string) => string) => ({
  [k('leftOperand')]: { '@id': 'dome-op:role' },
  [k('operator')]: { '@id': 'odrl:eq' },
  [k('rightOperand')]: { '@value': 'admin' },
});

const prefixed = (t: string) => `odrl:${t}`;
const bare = (t: string) => t;
/** Some terms prefixed, some bare, in the same policy. */
const mixed = (t: string) => (['permission', 'action', 'operator'].includes(t) ? t : `odrl:${t}`);

const build = (k: (t: string) => string): OdrlPolicyJson =>
  ({
    '@context': { odrl: 'http://www.w3.org/ns/odrl/2/' },
    '@type': 'odrl:Policy',
    [k('permission')]: {
      [k('target')]: 'https://a.example/t',
      [k('assignee')]: 'did:web:b.example',
      [k('action')]: 'odrl:read',
      [k('constraint')]: [CONSTRAINT(k)],
    },
  }) as OdrlPolicyJson;

const SPELLINGS: Array<[string, (t: string) => string]> = [
  ['prefixed', prefixed],
  ['bare', bare],
  ['mixed', mixed],
];

const renderBuilder = (policy: OdrlPolicyJson, setPolicy = vi.fn()) => {
  render(
    <I18nProvider>
      <PolicyBuilder policy={policy} setPolicy={setPolicy} showSummary={false} />
    </I18nProvider>,
  );
  return setPolicy;
};

describe('PolicyBuilder with ODRL terms spelled prefixed, bare or mixed', () => {
  beforeEach(() => {
    clearMappingsCache();
    vi.mocked(UiService.getMappings).mockReset();
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);
  });

  describe.each(SPELLINGS)('%s terms', (_name, k) => {
    it('shows target, assignee, action and constraint', async () => {
      renderBuilder(build(k));
      await waitFor(() => expect(screen.getByLabelText('Action')).toHaveValue('odrl:read'));
      expect(screen.getByDisplayValue('https://a.example/t')).toBeInTheDocument();
      expect(screen.getByDisplayValue('did:web:b.example')).toBeInTheDocument();
      expect(screen.getByLabelText(/select left operand 1/i)).toHaveValue('dome-op:role');
      expect(screen.getByLabelText(/select operator 1/i)).toHaveValue('odrl:eq');
    });

    it('keeps the keys when the action is edited', async () => {
      const user = userEvent.setup();
      const policy = build(k);
      const setPolicy = renderBuilder(policy);
      await waitFor(() => expect(screen.getByLabelText('Action')).toHaveValue('odrl:read'));
      await user.selectOptions(screen.getByLabelText('Action'), 'odrl:use');

      const written = setPolicy.mock.calls.at(-1)![0];
      expect(Object.keys(written)).toEqual(Object.keys(policy));
      const permission = written[k('permission')];
      expect(Object.keys(permission)).toEqual(Object.keys(policy[k('permission')] as object));
      expect(permission[k('action')]).toBe('odrl:use');
    });

    it('keeps the keys of the constraint when its operator is edited', async () => {
      const user = userEvent.setup();
      const policy = build(k);
      const setPolicy = renderBuilder(policy);
      await waitFor(() => expect(screen.getByLabelText(/select operator 1/i)).toBeInTheDocument());
      await user.selectOptions(screen.getByLabelText(/select operator 1/i), 'odrl:neq');

      const permission = setPolicy.mock.calls.at(-1)![0][k('permission')];
      const constraint = permission[k('constraint')][0];
      expect(Object.keys(constraint)).toEqual(Object.keys(CONSTRAINT(k)));
      expect(constraint[k('operator')]).toEqual({ '@id': 'odrl:neq' });
    });
  });

  it('does not add a second permission when a bare policy is edited', async () => {
    const user = userEvent.setup();
    const setPolicy = renderBuilder(build(bare));
    await waitFor(() => expect(screen.getByLabelText('Action')).toBeInTheDocument());
    await user.selectOptions(screen.getByLabelText('Action'), 'odrl:use');

    const written = setPolicy.mock.calls.at(-1)![0];
    expect(written['odrl:permission']).toBeUndefined();
    expect(written.permission).toBeDefined();
  });

  it('writes prefixed keys for a policy that has no permission yet', async () => {
    const user = userEvent.setup();
    const setPolicy = renderBuilder({ '@type': 'odrl:Policy' } as OdrlPolicyJson);
    await waitFor(() => expect(screen.getByLabelText('Action')).toBeInTheDocument());
    await user.selectOptions(screen.getByLabelText('Action'), 'odrl:use');

    expect(setPolicy.mock.calls.at(-1)![0]['odrl:permission']).toEqual({ 'odrl:action': 'odrl:use' });
  });
});
