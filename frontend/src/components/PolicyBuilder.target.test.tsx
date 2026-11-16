/**
 * "Select from available targets" (P1-4): the option names the key the target is written under
 * (`json:uid`, `odrl:uid`, ...) and the value is what the user typed - never the option's name.
 * The backend picks the rego rule from that key, so each option must emit its own key.
 */
import React from 'react';
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

/** The target options of the backend's mapping.json. */
const TARGET_OPTIONS = ['odrl:uid', 'odrl:target', 'json:uid', 'json:target', 'json:payloadTarget'];

const MOCK_MAPPINGS: Mappings = {
  actions: [{ name: 'odrl:read', description: 'read' }],
  operators: [],
  leftOperands: [],
  rightOperands: [],
  assignees: [],
  targets: TARGET_OPTIONS.map((name) => ({ name, description: `option ${name}` })),
  constraints: [],
};

const policyWith = (permission: unknown) =>
  ({ '@type': 'odrl:Policy', 'odrl:permission': permission }) as OdrlPolicyJson;

const renderBuilder = (policy: OdrlPolicyJson, setPolicy = vi.fn()) => {
  render(
    <I18nProvider>
      <PolicyBuilder policy={policy} setPolicy={setPolicy} showSummary={false} />
    </I18nProvider>,
  );
  return setPolicy;
};

const lastPermission = (setPolicy: ReturnType<typeof vi.fn>) =>
  setPolicy.mock.calls.at(-1)![0]['odrl:permission'];

/** Stateful host, so that a sequence of edits builds on the previous one. */
const Host = ({ onWrite, initial }: { onWrite: (p: OdrlPolicyJson) => void; initial: OdrlPolicyJson }) => {
  const [policy, setPolicy] = React.useState(initial);
  return (
    <I18nProvider>
      <PolicyBuilder
        policy={policy}
        setPolicy={(p) => {
          onWrite(p);
          setPolicy(p);
        }}
        showSummary={false}
      />
    </I18nProvider>
  );
};
describe('Target "select from available targets"', () => {
  beforeEach(() => {
    clearMappingsCache();
    vi.mocked(UiService.getMappings).mockReset();
    vi.mocked(UiService.getMappings).mockResolvedValue(MOCK_MAPPINGS);
  });

  it.each(TARGET_OPTIONS)('writes the value under %s, not under odrl:target with the option name', async (option) => {
    const user = userEvent.setup();
    const onWrite = vi.fn();
    render(<Host onWrite={onWrite} initial={policyWith({})} />);
    await waitFor(() => expect(screen.getByLabelText('Action')).toBeInTheDocument());

    await user.click(screen.getByRole('radio', { name: 'Select from available targets' }));
    await user.selectOptions(screen.getByLabelText('Select a target'), option);
    await user.type(screen.getByLabelText(/enter the target value/i), 'abc');

    const permission = onWrite.mock.calls.at(-1)![0]['odrl:permission'];
    expect(permission).toEqual({ [option]: 'abc' });
  });

  it('moves the typed value to the new key when the option changes', async () => {
    const user = userEvent.setup();
    const onWrite = vi.fn();
    render(<Host onWrite={onWrite} initial={policyWith({})} />);
    await waitFor(() => expect(screen.getByLabelText('Action')).toBeInTheDocument());

    await user.click(screen.getByRole('radio', { name: 'Select from available targets' }));
    await user.selectOptions(screen.getByLabelText('Select a target'), 'json:uid');
    await user.type(screen.getByLabelText(/enter the target value/i), 'abc');
    await user.selectOptions(screen.getByLabelText('Select a target'), 'json:target');

    expect(onWrite.mock.calls.at(-1)![0]['odrl:permission']).toEqual({ 'json:target': 'abc' });
  });

  it('loads a permission written with json:uid in the select mode, showing option and value', async () => {
    renderBuilder(policyWith({ 'json:uid': 'abc', 'odrl:action': 'odrl:read' }));
    await waitFor(() => expect(screen.getByLabelText('Select a target')).toHaveValue('json:uid'));
    expect(screen.getByLabelText(/enter the target value/i)).toHaveValue('abc');
  });

  it('keeps json:uid and its value when another field is edited', async () => {
    const user = userEvent.setup();
    const setPolicy = renderBuilder(policyWith({ 'json:uid': 'abc', 'odrl:action': 'odrl:read' }));
    await waitFor(() => expect(screen.getByLabelText('Action')).toBeInTheDocument());
    await user.selectOptions(screen.getByLabelText('Action'), '');

    expect(lastPermission(setPolicy)).toEqual({ 'json:uid': 'abc', 'odrl:action': '' });
  });

  it('writes odrl:target again when switching back to a custom target', async () => {
    const user = userEvent.setup();
    const onWrite = vi.fn();
    render(<Host onWrite={onWrite} initial={policyWith({ 'json:uid': 'abc' })} />);
    await waitFor(() => expect(screen.getByLabelText('Select a target')).toBeInTheDocument());

    await user.click(screen.getByRole('radio', { name: 'Custom Target (URL)' }));
    await user.type(screen.getByRole('textbox', { name: 'Custom Target (URL)' }), 'https://a.example/x');

    expect(onWrite.mock.calls.at(-1)![0]['odrl:permission']).toEqual({ 'odrl:target': 'https://a.example/x' });
  });

  it('keeps a bare target key when the custom URL is edited', async () => {
    const user = userEvent.setup();
    const onWrite = vi.fn();
    render(<Host onWrite={onWrite} initial={{ '@type': 'odrl:Policy', permission: { target: 'https://a.example/x' } } as OdrlPolicyJson} />);
    await waitFor(() => expect(screen.getByRole('textbox', { name: 'Custom Target (URL)' })).toBeInTheDocument());
    await user.type(screen.getByRole('textbox', { name: 'Custom Target (URL)' }), 'y');

    expect(onWrite.mock.calls.at(-1)![0].permission).toEqual({ target: 'https://a.example/xy' });
  });
});
