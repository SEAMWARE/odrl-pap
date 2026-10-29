/**
 * Unit tests for the {@link EmbeddedApp} root component.
 *
 * Focused on the interplay between the template tab and the raw ODRL tab:
 * react-bootstrap keeps every tab pane mounted, so a policy adopted from a
 * template is rendered by the `@context` badge list straight away — even
 * while the user is still on the template tab.
 */
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import EmbeddedApp from './EmbeddedApp';
import type { EmbeddedConfig } from './EmbeddedContext';
import { TemplateService } from '../api/services/TemplateService';
import { PolicyService } from '../api/services/PolicyService';
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
    expect(onEvent).toHaveBeenCalledWith('policy-created', { policy, id });
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

