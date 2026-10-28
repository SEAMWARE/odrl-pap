/**
 * Unit tests for the policy id helpers.
 */
import { describe, it, expect } from 'vitest';
import { isValidPolicyId, toPolicyId } from './policyId';

describe('isValidPolicyId', () => {
  it.each(['_policy', 'EnergyReportRead', 'energy_report_read', 'policy1', 'rego'])(
    'accepts %s',
    (id) => {
      expect(isValidPolicyId(id)).toBe(true);
    },
  );

  it.each([
    'energy-report-read',
    '1policy',
    'my.policy',
    'urn:policy:1',
    'my policy',
    '',
    'if',
    'contains',
    'data',
    'input',
  ])('rejects %s', (id) => {
    expect(isValidPolicyId(id)).toBe(false);
  });
});

describe('toPolicyId', () => {
  it('keeps a uid that already is a valid policy id', () => {
    expect(toPolicyId('energyReportRead')).toBe('energyReportRead');
  });

  it('derives a valid id from a UUID', () => {
    const id = toPolicyId('3f2a9c1e-7b4d-4e8a-9c2f-1a2b3c4d5e6f');
    expect(id).toBe('p_3f2a9c1e_7b4d_4e8a_9c2f_1a2b3c4d5e6f');
    expect(isValidPolicyId(id)).toBe(true);
  });

  it('derives a valid id from an IRI', () => {
    const id = toPolicyId('https://marketplace.org/policy/common/_1003');
    expect(id).toBe('p_https___marketplace_org_policy_common__1003');
    expect(isValidPolicyId(id)).toBe(true);
  });

  it('derives a valid id from a rego keyword', () => {
    expect(toPolicyId('if')).toBe('p_if');
  });

  it('derives the same id for the same uid', () => {
    expect(toPolicyId('urn:uuid:abc-123')).toBe(toPolicyId('urn:uuid:abc-123'));
  });
});
