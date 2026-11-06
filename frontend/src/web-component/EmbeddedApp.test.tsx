/**
 * Unit tests for the {@link EmbeddedApp} root component.
 *
 * Focused on:
 * - the interplay between the template tab and the raw ODRL tab:
 *   react-bootstrap keeps every tab pane mounted, so a policy adopted from a
 *   template is rendered by the `@context` badge list straight away — even
 *   while the user is still on the template tab;
 * - the capability modes, in particular `save`, which decides whether saving
 *   writes the policy to the PAP, only emits it to the host, or is offered at
 *   all.
 */
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { act, render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import EmbeddedApp from './EmbeddedApp';
import type { EmbeddedConfig } from './EmbeddedContext';
import { TemplateService } from '../api/services/TemplateService';
import { PolicyService } from '../api/services/PolicyService';
import { ServiceService } from '../api/services/ServiceService';
import { UiService } from '../api/services/UiService';
import { DEFAULT_MAPPINGS } from './defaultMappings';
import { DEFAULT_CAPABILITIES, type CapabilityModes } from './capabilities';
import type { Template } from '../api/models/Template';
import { isValidPolicyId } from '../utils/policyId';

vi.mock('../api/services/TemplateService', () => ({
  TemplateService: {
    getTemplates: vi.fn(),
  },
}));

vi.mock('../api/services/PolicyService', () => ({
  PolicyService: {
    createPolicyWithId: vi.fn(),
    getPolicyById: vi.fn(),
  },
}));

vi.mock('../api/services/ServiceService', () => ({
  ServiceService: {
    createServicePolicyWithId: vi.fn(),
    getServicePolicyById: vi.fn(),
    getServiceTemplates: vi.fn(),
  },
}));

vi.mock('../api/services/UiService', () => ({
  UiService: {
    getMappings: vi.fn().mockResolvedValue({}),
    validatePolicy: vi.fn(),
  },
}));

/** ODRL namespace IRI used by the fixture context. */
const ODRL_NAMESPACE = 'http://www.w3.org/ns/odrl/2/';
/** XSD namespace IRI used by the fixture context. */
const XSD_NAMESPACE = 'http://www.w3.org/2001/XMLSchema#';

/**
 * Template following the DOME ODRL profile, whose `@context` maps every
 * prefix to a JSON-LD *expanded term definition*
 * (`{ "@id": ..., "@prefix": true }`) rather than a plain IRI string.
 */
const TERM_DEFINITION_TEMPLATE = {
  id: 'zhpanxnqdm',
  name: 'Role-based entity access',
  description: 'Allows access to entities of a certain type for a defineable role',
  odrl: {
    '@id': 'https://marketplace.org/policy/common/_1003',
    'odrl:uid': 'https://marketplace.org/policy/common/_1003',
    '@type': 'odrl:Policy',
    'odrl:profile': 'https://github.com/DOME-Marketplace/dome-odrl-profile/blob/main/dome-op.ttl',
    'odrl:permission': {
      'odrl:assigner': { '@id': 'https://marketplace.org/' },
      'odrl:target': {
        '@type': 'odrl:AssetCollection',
        'odrl:source': 'urn:asset',
        'odrl:refinement': {
          '@type': 'odrl:Constraint',
          'odrl:leftOperand': { '@id': 'ngsi-ld:entityType' },
          'odrl:operator': { '@id': 'odrl:eq' },
          'odrl:rightOperand': '{{ENTITY_TYPE}}',
        },
      },
      'odrl:assignee': {
        '@type': 'odrl:PartyCollection',
        'odrl:source': 'urn:user',
        'odrl:refinement': {
          '@type': 'odrl:Constraint',
          'odrl:leftOperand': { '@id': 'vc:role' },
          'odrl:operator': { '@id': 'odrl:hasPart' },
          'odrl:rightOperand': { '@value': '{{ROLE}}', '@type': 'xsd:string' },
        },
      },
      'odrl:action': { '@id': 'dome-op:read' },
    },
    '@context': {
      odrl: { '@id': ODRL_NAMESPACE, '@prefix': true },
      xsd: { '@id': XSD_NAMESPACE, '@prefix': true },
    },
  },
  naturalLanguage: 'Users in role {{ROLE}} are allowed to access entities of type {{ENTITY_TYPE}}.',
  placeholders: [
    { key: 'ENTITY_TYPE', name: 'Entity Type', description: 'Type of entities.', type: 'string' },
    { key: 'ROLE', name: 'Role', description: 'Roles to allow.', type: 'string', options: ['ADMIN'] },
  ],
} as unknown as Template;

/** Minimal embedded configuration for create mode with all tabs visible. */
const CONFIG: EmbeddedConfig = {
  apiBaseUrl: 'http://pap.example.com',
  authToken: null,
  mode: 'create',
  policyId: null,
  locale: 'en',
  theme: 'light',
  onEvent: vi.fn(),
  serviceId: null,
  hiddenTabs: {
    hideBuilderTab: false,
    hideRawTab: false,
    hideTemplateTab: false,
    hideTemplateCreateTab: false,
  },
  capabilities: DEFAULT_CAPABILITIES,
};

describe('EmbeddedApp template selection', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('adopts a template whose @context uses expanded term definitions', async () => {
    vi.mocked(TemplateService.getTemplates).mockResolvedValue([TERM_DEFINITION_TEMPLATE]);

    render(<EmbeddedApp config={CONFIG} />);

    const card = await screen.findByRole('button', { name: 'Template: Role-based entity access' });
    await userEvent.click(card);

    // The filler is rendered, i.e. the React tree survived adopting the policy.
    expect(await screen.findByLabelText('Entity Type')).toBeInTheDocument();

    // Both context entries are rendered with the IRI of their term definition.
    expect(screen.getByTitle(ODRL_NAMESPACE)).toHaveTextContent(`odrl: ${ODRL_NAMESPACE}`);
    expect(screen.getByTitle(XSD_NAMESPACE)).toHaveTextContent(`xsd: ${XSD_NAMESPACE}`);
  });
});

describe('EmbeddedApp save', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(TemplateService.getTemplates).mockResolvedValue([]);
    vi.mocked(PolicyService.createPolicyWithId).mockResolvedValue('');
  });

  it('stores a new policy under a valid policy id derived from its odrl:uid', async () => {
    const onEvent = vi.fn();
    render(<EmbeddedApp config={{ ...CONFIG, onEvent }} />);

    await userEvent.click(screen.getByRole('button', { name: 'Save' }));

    // A new policy gets a UUID as odrl:uid, which has hyphens: the PAP would
    // reject it as a policy id, since it becomes the rego package.
    expect(PolicyService.createPolicyWithId).toHaveBeenCalledTimes(1);
    const [id, policy] = vi.mocked(PolicyService.createPolicyWithId).mock.calls[0];
    expect(isValidPolicyId(id)).toBe(true);
    expect((policy as Record<string, unknown>)['odrl:uid']).toMatch(/-/);
    expect(onEvent).toHaveBeenCalledWith('policy-created', { policy, id, persisted: true });
  });
});

/** Policy id used by the edit-mode fixtures of the emit-only tests. */
const EXISTING_POLICY_ID = 'existingpolicy';

/** Stored policy returned by the mocked policy API in edit mode. */
const EXISTING_POLICY = {
  '@context': { odrl: ODRL_NAMESPACE },
  '@type': 'odrl:Policy',
  'odrl:uid': EXISTING_POLICY_ID,
};

/** Builds a config with the `save` capability set to the given mode. */
function withSaveMode(
  mode: CapabilityModes['save'],
  overrides: Partial<EmbeddedConfig> = {},
): EmbeddedConfig {
  return {
    ...CONFIG,
    capabilities: { ...DEFAULT_CAPABILITIES, save: mode },
    // The template picker is irrelevant here and would steal the default tab.
    hiddenTabs: { ...CONFIG.hiddenTabs, hideTemplateTab: true },
    ...overrides,
  };
}

describe('EmbeddedApp emit-only save', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(TemplateService.getTemplates).mockResolvedValue([]);
    vi.mocked(UiService.getMappings).mockResolvedValue({});
    vi.mocked(PolicyService.createPolicyWithId).mockResolvedValue('');
    vi.mocked(PolicyService.getPolicyById).mockResolvedValue({
      id: EXISTING_POLICY_ID,
      odrl: JSON.stringify(EXISTING_POLICY),
    });
    vi.mocked(ServiceService.createServicePolicyWithId).mockResolvedValue('');
    // The template manager lists templates on mount; with a serviceId set it
    // goes through the service-scoped endpoint.
    vi.mocked(ServiceService.getServiceTemplates).mockResolvedValue([]);
  });

  it.each([
    {
      label: 'stores and reports a new policy',
      saveMode: 'remote' as const,
      mode: 'create' as const,
      expectedEvent: 'policy-created',
      expectedWrites: 1,
      persisted: true,
    },
    {
      label: 'stores and reports an edited policy',
      saveMode: 'remote' as const,
      mode: 'edit' as const,
      expectedEvent: 'policy-updated',
      expectedWrites: 1,
      persisted: true,
    },
    {
      label: 'only reports a new policy',
      saveMode: 'event' as const,
      mode: 'create' as const,
      expectedEvent: 'policy-created',
      expectedWrites: 0,
      persisted: false,
    },
    {
      label: 'only reports an edited policy',
      saveMode: 'event' as const,
      mode: 'edit' as const,
      expectedEvent: 'policy-updated',
      expectedWrites: 0,
      persisted: false,
    },
  ])('$label (save=$saveMode, mode=$mode)', async ({ saveMode, mode, expectedEvent, expectedWrites, persisted }) => {
    const onEvent = vi.fn();
    render(
      <EmbeddedApp
        config={withSaveMode(saveMode, {
          mode,
          policyId: mode === 'edit' ? EXISTING_POLICY_ID : null,
          onEvent,
        })}
      />,
    );

    await userEvent.click(await screen.findByRole('button', { name: 'Save' }));

    await waitFor(() =>
      expect(onEvent.mock.calls.some(([type]) => type === expectedEvent)).toBe(true),
    );

    expect(PolicyService.createPolicyWithId).toHaveBeenCalledTimes(expectedWrites);

    const [, detail] = onEvent.mock.calls.findLast(([type]) => type === expectedEvent)!;
    expect(detail.persisted).toBe(persisted);
    expect(detail.policy).toBeDefined();
    expect(detail.id).toBeTruthy();
  });

  it('emits a valid policy id so the host can store the policy under it', async () => {
    const onEvent = vi.fn();
    render(<EmbeddedApp config={withSaveMode('event', { onEvent })} />);

    await userEvent.click(await screen.findByRole('button', { name: 'Save' }));
    await waitFor(() =>
      expect(onEvent.mock.calls.some(([type]) => type === 'policy-created')).toBe(true),
    );

    const [, detail] = onEvent.mock.calls.findLast(([type]) => type === 'policy-created')!;
    expect(isValidPolicyId(detail.id)).toBe(true);
    expect(detail.policy['odrl:uid']).toBeTruthy();
  });

  it('does not write to the service-scoped endpoint either', async () => {
    const onEvent = vi.fn();
    render(<EmbeddedApp config={withSaveMode('event', { serviceId: 'my-service', onEvent })} />);

    await userEvent.click(await screen.findByRole('button', { name: 'Save' }));
    await waitFor(() =>
      expect(onEvent.mock.calls.some(([type]) => type === 'policy-created')).toBe(true),
    );

    expect(ServiceService.createServicePolicyWithId).not.toHaveBeenCalled();
    expect(PolicyService.createPolicyWithId).not.toHaveBeenCalled();
  });

  it('still loads an existing policy for editing, which the load capability governs', async () => {
    render(
      <EmbeddedApp
        config={withSaveMode('event', { mode: 'edit', policyId: EXISTING_POLICY_ID })}
      />,
    );

    // Read access is independent of how saving is served.
    await waitFor(() =>
      expect(PolicyService.getPolicyById).toHaveBeenCalledWith(EXISTING_POLICY_ID),
    );
  });

  it('reports a template-based policy without storing it', async () => {
    vi.mocked(TemplateService.getTemplates).mockResolvedValue([TERM_DEFINITION_TEMPLATE]);
    const onEvent = vi.fn();
    render(
      <EmbeddedApp
        config={{
          ...withSaveMode('event', { onEvent }),
          // The template picker is what this test drives.
          hiddenTabs: { ...CONFIG.hiddenTabs, hideTemplateTab: false },
        }}
      />,
    );

    await userEvent.click(
      await screen.findByRole('button', { name: 'Template: Role-based entity access' }),
    );

    // The Create button stays disabled until every placeholder is filled in.
    await userEvent.type(await screen.findByLabelText('Entity Type'), 'Product');
    await userEvent.selectOptions(await screen.findByLabelText('Role'), 'ADMIN');

    await userEvent.click(
      await screen.findByRole('button', { name: 'Create Policy from Template' }),
    );

    await waitFor(() =>
      expect(onEvent.mock.calls.some(([type]) => type === 'policy-created')).toBe(true),
    );
    expect(PolicyService.createPolicyWithId).not.toHaveBeenCalled();

    const [, detail] = onEvent.mock.calls.findLast(([type]) => type === 'policy-created')!;
    expect(detail.persisted).toBe(false);
    expect(isValidPolicyId(detail.id)).toBe(true);
  });

  it('offers Save, unlike save="none"', async () => {
    render(<EmbeddedApp config={withSaveMode('event')} />);

    expect(await screen.findByRole('button', { name: 'Save' })).toBeInTheDocument();
  });
});

/** Config with the given capabilities switched off. */
function withNone(...names: (keyof CapabilityModes)[]): EmbeddedConfig {
  const capabilities = { ...DEFAULT_CAPABILITIES };
  for (const name of names) capabilities[name] = 'none';
  return { ...CONFIG, onEvent: vi.fn(), capabilities };
}

describe('EmbeddedApp capabilities', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(TemplateService.getTemplates).mockResolvedValue([]);
    vi.mocked(UiService.getMappings).mockResolvedValue({});
  });

  it('shows Save and Validate when they are remote', () => {
    render(<EmbeddedApp config={CONFIG} />);

    expect(screen.getByRole('button', { name: 'Save' })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Validate' })).toBeInTheDocument();
  });

  it('hides Save when save is none', () => {
    render(<EmbeddedApp config={withNone('save')} />);

    expect(screen.queryByRole('button', { name: 'Save' })).not.toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Validate' })).toBeInTheDocument();
  });

  it('hides Validate when validate is none', () => {
    render(<EmbeddedApp config={withNone('validate')} />);

    expect(screen.queryByRole('button', { name: 'Validate' })).not.toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Save' })).toBeInTheDocument();
  });

  it('does not list templates when templates is none', () => {
    render(<EmbeddedApp config={withNone('templates')} />);

    expect(TemplateService.getTemplates).not.toHaveBeenCalled();
  });

  it('does not fetch the policy in edit mode when load is none', () => {
    const config = { ...withNone('load'), mode: 'edit' as const, policyId: 'p1' };
    render(<EmbeddedApp config={config} />);

    expect(PolicyService.getPolicyById).not.toHaveBeenCalled();
  });

  it('fetches the policy in edit mode when load is remote', () => {
    vi.mocked(PolicyService.getPolicyById).mockResolvedValue({ odrl: '{}' } as never);
    render(<EmbeddedApp config={{ ...CONFIG, mode: 'edit', policyId: 'p1' }} />);

    expect(PolicyService.getPolicyById).toHaveBeenCalledWith('p1');
  });

  it('does not fetch mappings when mappings is none', () => {
    render(<EmbeddedApp config={withNone('mappings')} />);

    expect(UiService.getMappings).not.toHaveBeenCalled();
  });

  it('uses host-supplied mappings instead of fetching them', () => {
    render(<EmbeddedApp config={CONFIG} mappings={{ actions: [] }} />);

    expect(UiService.getMappings).not.toHaveBeenCalled();
  });

  it('falls back to the built-in mappings when mappings is none and none are supplied', async () => {
    render(<EmbeddedApp config={withNone('mappings')} />);

    // The builder renders (no "failed to load mappings" alert) with the
    // default vocabulary, without touching the backend.
    expect(UiService.getMappings).not.toHaveBeenCalled();
    expect(screen.queryByRole('button', { name: 'Retry' })).not.toBeInTheDocument();
    expect(DEFAULT_MAPPINGS.actions?.map((m) => m.name)).toContain('odrl:use');
  });

  it('makes no backend call at all with every capability off', () => {
    const everything = Object.keys(DEFAULT_CAPABILITIES) as (keyof CapabilityModes)[];
    render(<EmbeddedApp config={{ ...withNone(...everything), mode: 'edit', policyId: 'p1' }} />);

    expect(TemplateService.getTemplates).not.toHaveBeenCalled();
    expect(PolicyService.getPolicyById).not.toHaveBeenCalled();
    expect(UiService.getMappings).not.toHaveBeenCalled();
    expect(screen.queryByRole('button', { name: 'Save' })).not.toBeInTheDocument();
    expect(screen.queryByRole('button', { name: 'Validate' })).not.toBeInTheDocument();
  });
});

describe('EmbeddedApp policy value', () => {
  const POLICY = { '@type': 'odrl:Policy', 'odrl:uid': 'urn:p:1' } as never;

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(TemplateService.getTemplates).mockResolvedValue([]);
    vi.mocked(UiService.getMappings).mockResolvedValue({});
  });

  it('does not emit policy-changed for the initial or host-supplied policy', () => {
    // Builder tab hidden: the builder normalises the policy it mounts with
    // (e.g. adds an empty constraint list), which is reported as an edit.
    const config = {
      ...withNone('mappings'),
      hiddenTabs: { ...CONFIG.hiddenTabs, hideBuilderTab: true },
    };
    const { rerender } = render(<EmbeddedApp config={config} policy={POLICY} />);
    rerender(<EmbeddedApp config={config} policy={{ ...(POLICY as object), '@type': 'odrl:Set' } as never} />);

    expect(config.onEvent).not.toHaveBeenCalledWith('policy-changed', expect.anything());
  });

  it('does not emit policy-changed for a policy loaded remotely in edit mode', async () => {
    vi.mocked(PolicyService.getPolicyById).mockResolvedValue({
      odrl: '{"@type":"odrl:Set"}',
    } as never);
    const config = {
      ...CONFIG,
      mode: 'edit' as const,
      policyId: 'p1',
      capabilities: { ...DEFAULT_CAPABILITIES, mappings: 'none' as const },
      hiddenTabs: { ...CONFIG.hiddenTabs, hideBuilderTab: true },
    };
    render(<EmbeddedApp config={config} />);

    await waitFor(() => expect(PolicyService.getPolicyById).toHaveBeenCalled());
    // Let the resolved load be applied to the state.
    await act(async () => {});

    expect(config.onEvent).not.toHaveBeenCalledWith('policy-changed', expect.anything());
  });

  it('emits policy-changed when the user edits the raw policy', async () => {
    const config = withNone('mappings');
    render(<EmbeddedApp config={config} policy={POLICY} />);
    (config.onEvent as ReturnType<typeof vi.fn>).mockClear();

    await userEvent.click(screen.getByRole('tab', { name: /raw/i }));
    const textarea = screen.getByRole('textbox', { name: '' });
    await userEvent.clear(textarea);
    await userEvent.click(textarea);
    await userEvent.paste('{"@type":"odrl:Agreement"}');

    expect(config.onEvent).toHaveBeenCalledWith('policy-changed', {
      policy: { '@type': 'odrl:Agreement' },
    });
  });
});

describe('EmbeddedApp layout flags', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(TemplateService.getTemplates).mockResolvedValue([]);
    vi.mocked(UiService.getMappings).mockResolvedValue({});
  });

  const NO_REMOTE_ACTIONS = withNone('save', 'validate', 'mappings', 'templates');

  it('shows the summary beside the builder and no summary tab by default', () => {
    render(<EmbeddedApp config={{ ...NO_REMOTE_ACTIONS }} />);

    expect(screen.queryByRole('tab', { name: 'Summary' })).not.toBeInTheDocument();
    expect(screen.getAllByText('Policy Summary').length).toBeGreaterThan(0);
  });

  it('moves the summary to its own tab when summary is tab', () => {
    render(<EmbeddedApp config={{ ...NO_REMOTE_ACTIONS, summaryPlacement: 'tab' }} />);

    expect(screen.getByRole('tab', { name: 'Summary' })).toBeInTheDocument();
    // Only the tab renders it now, not the builder's side column.
    expect(screen.getAllByText('Policy Summary')).toHaveLength(1);
  });

  it('shows no summary at all when summary is none', () => {
    render(<EmbeddedApp config={{ ...NO_REMOTE_ACTIONS, summaryPlacement: 'none' }} />);

    expect(screen.queryByRole('tab', { name: 'Summary' })).not.toBeInTheDocument();
    expect(screen.queryByText('Policy Summary')).not.toBeInTheDocument();
  });

  it('hides the Cancel button with hide-cancel-button', () => {
    render(<EmbeddedApp config={{ ...CONFIG, hiddenButtons: { hideCancelButton: true } }} />);

    expect(screen.queryByRole('button', { name: 'Cancel' })).not.toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Save' })).toBeInTheDocument();
  });

  it('renders no action row when every button is off', () => {
    render(
      <EmbeddedApp config={{ ...NO_REMOTE_ACTIONS, hiddenButtons: { hideCancelButton: true } }} />,
    );

    expect(screen.queryByRole('button', { name: 'Cancel' })).not.toBeInTheDocument();
    expect(screen.queryByRole('button', { name: 'Save' })).not.toBeInTheDocument();
    expect(screen.queryByRole('button', { name: 'Validate' })).not.toBeInTheDocument();
  });

  it('keeps Cancel by default', () => {
    render(<EmbeddedApp config={NO_REMOTE_ACTIONS} />);

    expect(screen.getByRole('button', { name: 'Cancel' })).toBeInTheDocument();
  });
});

describe('EmbeddedApp variant', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(TemplateService.getTemplates).mockResolvedValue([]);
    vi.mocked(UiService.getMappings).mockResolvedValue({});
  });

  const root = (container: HTMLElement) => container.querySelector('.odrl-embedded-root') as HTMLElement;

  it('is a padded card by default', () => {
    const { container } = render(<EmbeddedApp config={CONFIG} />);

    expect(root(container).dataset.variant).toBe('card');
    expect(root(container).classList).toContain('p-3');
  });

  it('adds no chrome or padding when plain', () => {
    const { container } = render(<EmbeddedApp config={{ ...CONFIG, variant: 'plain' }} />);

    expect(root(container).dataset.variant).toBe('plain');
    expect(root(container).classList).not.toContain('p-3');
  });
});

