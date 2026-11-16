/** The Action card shows an action written as a string or as `{"@id": ...}` (P1-8). */
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

const MAPPINGS: Mappings = {
  actions: [
    { name: 'odrl:read', description: 'read' },
    { name: 'odrl:use', description: 'use' },
  ],
  operators: [], leftOperands: [], rightOperands: [], assignees: [], targets: [], constraints: [],
};

const policyWith = (action: unknown) =>
  ({ '@type': 'odrl:Policy', 'odrl:permission': { 'odrl:target': 't', 'odrl:action': action } }) as OdrlPolicyJson;

const renderBuilder = (policy: OdrlPolicyJson, setPolicy = vi.fn()) => {
  render(
    <I18nProvider>
      <PolicyBuilder policy={policy} setPolicy={setPolicy} showSummary={false} />
    </I18nProvider>,
  );
  return setPolicy;
};

describe('Action card', () => {
  beforeEach(() => {
    clearMappingsCache();
    vi.mocked(UiService.getMappings).mockReset();
    vi.mocked(UiService.getMappings).mockResolvedValue(MAPPINGS);
  });

  it.each([
    ['a string', 'odrl:read'],
    ['an object with @id', { '@id': 'odrl:read' }],
  ])('shows an action written as %s', async (_label, action) => {
    renderBuilder(policyWith(action));
    await waitFor(() => expect(screen.getByLabelText('Action')).toHaveValue('odrl:read'));
  });

  it('keeps the @id form when another field is edited', async () => {
    const user = userEvent.setup();
    const setPolicy = renderBuilder(policyWith({ '@id': 'odrl:read' }));
    await waitFor(() => expect(screen.getByLabelText('Action')).toHaveValue('odrl:read'));
    await user.type(screen.getByRole('textbox', { name: 'Custom Target (URL)' }), 'x');

    expect(setPolicy.mock.calls.at(-1)![0]['odrl:permission']['odrl:action']).toEqual({ '@id': 'odrl:read' });
  });

  it('writes the chosen action as a string', async () => {
    const user = userEvent.setup();
    const setPolicy = renderBuilder(policyWith({ '@id': 'odrl:read' }));
    await waitFor(() => expect(screen.getByLabelText('Action')).toHaveValue('odrl:read'));
    await user.selectOptions(screen.getByLabelText('Action'), 'odrl:use');

    expect(setPolicy.mock.calls.at(-1)![0]['odrl:permission']['odrl:action']).toBe('odrl:use');
  });
});
