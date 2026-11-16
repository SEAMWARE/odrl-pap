/**
 * Parts of a policy the PAP backend ignores (P1-10): the Builder says so and keeps them untouched.
 * The backend maps `odrl:permission` only: `prohibition`, `obligation`, `assigner` and `profile`
 * are never read by the mapper.
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

const MAPPINGS: Mappings = {
  actions: [
    { name: 'odrl:read', description: 'read' },
    { name: 'odrl:use', description: 'use' },
  ],
  operators: [], leftOperands: [], rightOperands: [], assignees: [], targets: [], constraints: [],
};

const PERMISSION = { 'odrl:target': 't', 'odrl:action': 'odrl:read' };
const PROHIBITION = { 'odrl:action': 'odrl:distribute', 'odrl:target': 't' };
const OBLIGATION = { 'odrl:action': 'odrl:delete' };

const renderBuilder = (policy: Record<string, unknown>, setPolicy = vi.fn()) => {
  render(
    <I18nProvider>
      <PolicyBuilder policy={policy as OdrlPolicyJson} setPolicy={setPolicy} showSummary={false} />
    </I18nProvider>,
  );
  return setPolicy;
};

describe('unsupported parts of a policy', () => {
  beforeEach(() => {
    clearMappingsCache();
    vi.mocked(UiService.getMappings).mockReset();
    vi.mocked(UiService.getMappings).mockResolvedValue(MAPPINGS);
  });

  it('shows no notice for a policy with only a permission', async () => {
    renderBuilder({ 'odrl:permission': PERMISSION });
    await waitFor(() => expect(screen.getByLabelText('Action')).toBeInTheDocument());
    expect(screen.queryByTestId('unsupported-parts-notice')).not.toBeInTheDocument();
  });

  it.each([
    ['prohibition', { 'odrl:prohibition': PROHIBITION }],
    ['obligation', { 'odrl:obligation': OBLIGATION }],
    ['assigner', { 'odrl:assigner': 'did:web:a' }],
    ['profile', { 'odrl:profile': 'http://example.com/profile' }],
    ['prohibition', { prohibition: PROHIBITION }],
  ])('names the %s that the PAP ignores', async (part, extra) => {
    renderBuilder({ 'odrl:permission': PERMISSION, ...extra });
    const notice = await screen.findByTestId('unsupported-parts-notice');
    expect(notice).toHaveTextContent(part);
    expect(notice).toHaveTextContent(/ignored/i);
  });

  it('lists every unsupported part present', async () => {
    renderBuilder({ 'odrl:permission': PERMISSION, 'odrl:prohibition': PROHIBITION, 'odrl:obligation': OBLIGATION });
    const notice = await screen.findByTestId('unsupported-parts-notice');
    expect(notice).toHaveTextContent('prohibition');
    expect(notice).toHaveTextContent('obligation');
  });

  it('notes an assigner written inside the permission', async () => {
    renderBuilder({ 'odrl:permission': { ...PERMISSION, 'odrl:assigner': 'did:web:a' } });
    expect(await screen.findByTestId('unsupported-parts-notice')).toHaveTextContent('assigner');
  });

  it('leaves those parts untouched when the permission is edited', async () => {
    const user = userEvent.setup();
    const policy = {
      '@type': 'odrl:Policy',
      'odrl:uid': 'urn:p',
      'odrl:profile': 'http://example.com/profile',
      'odrl:assigner': 'did:web:a',
      'odrl:permission': PERMISSION,
      'odrl:prohibition': PROHIBITION,
      'odrl:obligation': OBLIGATION,
    };
    const setPolicy = renderBuilder(policy);
    await waitFor(() => expect(screen.getByLabelText('Action')).toBeInTheDocument());
    await user.selectOptions(screen.getByLabelText('Action'), 'odrl:use');

    const written = setPolicy.mock.calls.at(-1)![0];
    expect(Object.keys(written)).toEqual(Object.keys(policy));
    expect(written['odrl:prohibition']).toBe(PROHIBITION);
    expect(written['odrl:obligation']).toBe(OBLIGATION);
    expect(written['odrl:assigner']).toBe('did:web:a');
    expect(written['odrl:profile']).toBe('http://example.com/profile');
    expect(written['odrl:permission']['odrl:action']).toBe('odrl:use');
  });
});
