/** PolicySummary with constraints written as a JSON-LD `@list` (P1-2). */
import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import PolicySummary from './PolicySummary';
import { I18nProvider } from '../i18n';
import type { OdrlPolicyJson } from '../api';

const C = (value: string) => ({
  'odrl:leftOperand': { '@id': 'dome-op:role' },
  'odrl:operator': { '@id': 'odrl:eq' },
  'odrl:rightOperand': { '@value': value },
});

const policyWith = (constraint: unknown) =>
  ({
    '@type': 'odrl:Policy',
    'odrl:permission': {
      'odrl:target': 'urn:asset:1',
      'odrl:assignee': 'did:web:other.org',
      'odrl:action': 'odrl:read',
      'odrl:constraint': constraint,
    },
  }) as unknown as OdrlPolicyJson;

const renderSummary = (policy: OdrlPolicyJson) =>
  render(
    <I18nProvider locale="en">
      <PolicySummary policy={policy} />
    </I18nProvider>,
  );

describe('PolicySummary with @list constraints', () => {
  it('lists the constraints of an @list as an AND group', () => {
    renderSummary(policyWith({ '@list': [C('a'), C('b')] }));
    expect(screen.getByText('AND')).toBeInTheDocument();
    expect(screen.getAllByText('Constraint')).toHaveLength(2);
  });

  it('lists the constraints of an @list inside a logical constraint', () => {
    renderSummary(policyWith({ '@type': 'odrl:LogicalConstraint', 'odrl:or': { '@list': [C('a'), C('b')] } }));
    expect(screen.getByText('OR')).toBeInTheDocument();
    expect(screen.getAllByText('Constraint')).toHaveLength(2);
  });

  it('tolerates an empty list and null entries', () => {
    expect(() => renderSummary(policyWith({ '@list': [] }))).not.toThrow();
  });

  it('tolerates null entries in a list', () => {
    expect(() => renderSummary(policyWith({ '@list': [C('a'), null] }))).not.toThrow();
  });
});
