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
