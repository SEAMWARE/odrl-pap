/**
 * Unit tests for the {@link EmbeddedApp} root component.
 *
 * Focused on:
 * - the interplay between the template tab and the raw ODRL tab:
 *   react-bootstrap keeps every tab pane mounted, so a policy adopted from a
 *   template is rendered by the `@context` badge list straight away — even
 *   while the user is still on the template tab;
 * - the `persist` flag, which decides whether saving writes the policy to the
 *   PAP or only emits it to the host.
 */
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import EmbeddedApp from './EmbeddedApp';
import type { EmbeddedConfig } from './EmbeddedContext';
import { TemplateService } from '../api/services/TemplateService';
import { PolicyService } from '../api/services/PolicyService';
import { ServiceService } from '../api/services/ServiceService';
import type { Template } from '../api/models/Template';

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
  persist: true,
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

/** Policy id used by the edit-mode fixtures. */
const EXISTING_POLICY_ID = 'urn:policy:existing';

/** Stored policy returned by the mocked policy API in edit mode. */
const EXISTING_POLICY = {
  '@context': { odrl: ODRL_NAMESPACE },
  '@type': 'odrl:Policy',
  'odrl:uid': EXISTING_POLICY_ID,
};

/** Builds an embedded config for the persistence tests. */
function persistenceConfig(overrides: Partial<EmbeddedConfig>): EmbeddedConfig {
  return {
    ...CONFIG,
    // The template picker is irrelevant here and would steal the default tab.
    hiddenTabs: { ...CONFIG.hiddenTabs, hideTemplateTab: true },
    ...overrides,
  };
}

describe('EmbeddedApp policy persistence', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(TemplateService.getTemplates).mockResolvedValue([]);
    vi.mocked(PolicyService.createPolicyWithId).mockResolvedValue('');
    vi.mocked(PolicyService.getPolicyById).mockResolvedValue({
      id: EXISTING_POLICY_ID,
      odrl: JSON.stringify(EXISTING_POLICY),
    });
    vi.mocked(ServiceService.createServicePolicyWithId).mockResolvedValue('');
  });

  it.each([
    {
      label: 'stores and reports a new policy',
      persist: true,
      mode: 'create' as const,
      expectedEvent: 'policy-created',
      expectedWrites: 1,
    },
    {
      label: 'stores and reports an edited policy',
      persist: true,
      mode: 'edit' as const,
      expectedEvent: 'policy-updated',
      expectedWrites: 1,
    },
    {
      label: 'only reports a new policy',
      persist: false,
      mode: 'create' as const,
      expectedEvent: 'policy-created',
      expectedWrites: 0,
    },
    {
      label: 'only reports an edited policy',
      persist: false,
      mode: 'edit' as const,
      expectedEvent: 'policy-updated',
      expectedWrites: 0,
    },
  ])('$label (persist=$persist, mode=$mode)', async ({ persist, mode, expectedEvent, expectedWrites }) => {
    const onEvent = vi.fn();
    render(
      <EmbeddedApp
        config={persistenceConfig({
          persist,
          mode,
          policyId: mode === 'edit' ? EXISTING_POLICY_ID : null,
          onEvent,
        })}
      />,
    );

    await userEvent.click(await screen.findByRole('button', { name: 'Save' }));

    await waitFor(() => expect(onEvent).toHaveBeenCalled());

    expect(PolicyService.createPolicyWithId).toHaveBeenCalledTimes(expectedWrites);

    const [eventType, detail] = onEvent.mock.calls.at(-1)!;
    expect(eventType).toBe(expectedEvent);
    expect(detail.persisted).toBe(persist);
    expect(detail.policy).toBeDefined();
    expect(detail.id).toBeTruthy();
  });

  it('emits a generated uid so the host can store the policy under a stable id', async () => {
    const onEvent = vi.fn();
    render(<EmbeddedApp config={persistenceConfig({ persist: false, onEvent })} />);

    await userEvent.click(await screen.findByRole('button', { name: 'Save' }));
    await waitFor(() => expect(onEvent).toHaveBeenCalled());

    const [, detail] = onEvent.mock.calls.at(-1)!;
    expect(detail.policy['odrl:uid']).toBe(detail.id);
    expect(detail.id).not.toBe('');
  });

  it('does not write to the service-scoped endpoint either when persist is false', async () => {
    const onEvent = vi.fn();
    render(
      <EmbeddedApp config={persistenceConfig({ persist: false, serviceId: 'my-service', onEvent })} />,
    );

    await userEvent.click(await screen.findByRole('button', { name: 'Save' }));
    await waitFor(() => expect(onEvent).toHaveBeenCalled());

    expect(ServiceService.createServicePolicyWithId).not.toHaveBeenCalled();
    expect(PolicyService.createPolicyWithId).not.toHaveBeenCalled();
  });

  it('still loads an existing policy for editing when persist is false', async () => {
    render(
      <EmbeddedApp
        config={persistenceConfig({
          persist: false,
          mode: 'edit',
          policyId: EXISTING_POLICY_ID,
          onEvent: vi.fn(),
        })}
      />,
    );

    // Read access is unaffected by emit-only mode.
    await waitFor(() =>
      expect(PolicyService.getPolicyById).toHaveBeenCalledWith(EXISTING_POLICY_ID),
    );
  });

  it('hides the template management tab when persist is false', async () => {
    render(<EmbeddedApp config={persistenceConfig({ persist: false, onEvent: vi.fn() })} />);

    await screen.findByRole('button', { name: 'Save' });
    expect(screen.queryByRole('tab', { name: 'Manage Templates' })).not.toBeInTheDocument();
  });

  it('shows the template management tab when persist is true', async () => {
    render(<EmbeddedApp config={persistenceConfig({ persist: true, onEvent: vi.fn() })} />);

    expect(await screen.findByRole('tab', { name: 'Manage Templates' })).toBeInTheDocument();
  });
});
