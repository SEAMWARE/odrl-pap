/**
 * Unit tests for the PolicySummary component.
 *
 * Focus: the summary must tolerate both the plain-string and the object
 * (`{ "@id": "odrl:read" }`) forms of `odrl:action` — the latter is produced
 * by template-based policies and previously crashed the component with
 * "action.replace is not a function".
 */
import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import PolicySummary from './PolicySummary';
import { I18nProvider } from '../i18n';
import type { OdrlPolicyJson } from '../api';

/** Renders the summary wrapped in the i18n provider. */
function renderSummary(policy: OdrlPolicyJson) {
  return render(
    <I18nProvider locale="en">
      <PolicySummary policy={policy} />
    </I18nProvider>,
  );
}

describe('PolicySummary action handling', () => {
  it('renders a string action without crashing', () => {
    const policy = {
      '@type': 'odrl:Policy',
      'odrl:permission': {
        'odrl:target': 'urn:asset:1',
        'odrl:assignee': 'did:web:other.org',
        'odrl:action': 'odrl:read',
      },
      'odrl:uid': 'p1',
    } as unknown as OdrlPolicyJson;

    expect(() => renderSummary(policy)).not.toThrow();
    expect(screen.getByText(/READ/)).toBeInTheDocument();
  });

  it('renders an object-form action ({ "@id": "odrl:read" }) without crashing', () => {
    const policy = {
      '@type': 'odrl:Policy',
      'odrl:permission': {
        'odrl:target': 'test',
        'odrl:assignee': 'did:web:other.org',
        'odrl:action': { '@id': 'odrl:read' },
      },
      'odrl:uid': 'p2',
    } as unknown as OdrlPolicyJson;

    expect(() => renderSummary(policy)).not.toThrow();
    // Human summary uppercases the stripped action name.
    expect(screen.getByText(/READ/)).toBeInTheDocument();
  });
});

describe('PolicySummary refinement handling', () => {
  /**
   * A PartyCollection assignee whose `odrl:refinement` is a single Constraint
   * object (not an array) — the shape produced by template-based policies.
   * This previously crashed the component with "refinements.map is not a
   * function" because the summary assumed refinements were always an array.
   */
  const singleRefinementAssignee = {
    '@type': 'odrl:PartyCollection',
    'odrl:source': 'urn:user',
    'odrl:refinement': {
      '@type': 'odrl:Constraint',
      'odrl:leftOperand': { '@id': 'vc:role' },
      'odrl:operator': { '@id': 'odrl:hasPart' },
      'odrl:rightOperand': { '@value': 'ADMIN', '@type': 'xsd:string' },
    },
  };

  it('renders a single-object refinement without crashing', () => {
    const policy = {
      '@type': 'odrl:Policy',
      'odrl:permission': {
        'odrl:target': 'urn:entity:1',
        'odrl:assignee': singleRefinementAssignee,
        'odrl:action': { '@id': 'odrl:read' },
      },
      'odrl:uid': 'p3',
    } as unknown as OdrlPolicyJson;

    expect(() => renderSummary(policy)).not.toThrow();
    // The refinement section renders with its heading and operand badges.
    expect(screen.getByText('Refinements:')).toBeInTheDocument();
    expect(screen.getAllByText(/vc:role/).length).toBeGreaterThan(0);
  });

  it('renders an array-form refinement without crashing', () => {
    const policy = {
      '@type': 'odrl:Policy',
      'odrl:permission': {
        'odrl:target': 'urn:entity:1',
        'odrl:assignee': {
          '@type': 'odrl:PartyCollection',
          'odrl:refinement': [singleRefinementAssignee['odrl:refinement']],
        },
        'odrl:action': { '@id': 'odrl:read' },
      },
      'odrl:uid': 'p4',
    } as unknown as OdrlPolicyJson;

    expect(() => renderSummary(policy)).not.toThrow();
    expect(screen.getByText('Refinements:')).toBeInTheDocument();
  });
});

describe('PolicySummary left operand handling', () => {
  /** Builds a policy with a single constraint. */
  function policyWithConstraint(constraint: Record<string, unknown>): OdrlPolicyJson {
    return {
      '@type': 'odrl:Policy',
      'odrl:permission': {
        'odrl:target': 'urn:asset:1',
        'odrl:assignee': 'did:web:other.org',
        'odrl:action': { '@id': 'odrl:read' },
        'odrl:constraint': [constraint],
      },
      'odrl:uid': 'p5',
    } as unknown as OdrlPolicyJson;
  }

  it('renders a left operand referenced via odrl:leftOperand', () => {
    renderSummary(policyWithConstraint({
      '@type': 'odrl:Constraint',
      'odrl:leftOperand': { '@id': 'vc:role' },
      'odrl:operator': { '@id': 'odrl:eq' },
      'odrl:rightOperand': { '@value': 'ADMIN', '@type': 'xsd:string' },
    }));

    expect(screen.getAllByText(/vc:role/).length).toBeGreaterThan(0);
  });

  it('renders a parameterized left operand written as its own key', () => {
    renderSummary(policyWithConstraint({
      '@type': 'odrl:Constraint',
      'http:bodyValue': '$.numNodes.value',
      'odrl:operator': { '@id': 'odrl:eq' },
      'odrl:rightOperand': { '@value': '3', '@type': 'xsd:string' },
    }));

    expect(screen.getByText('http:bodyValue: $.numNodes.value')).toBeInTheDocument();
  });
});
