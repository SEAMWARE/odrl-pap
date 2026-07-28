/**
 * Tests for MSW mock handlers.
 *
 * Verifies that the mock /mappings and /validate endpoints return
 * realistic data matching the API schemas from odrl.yaml.
 */
import { describe, it, expect } from 'vitest';
import { MOCK_MAPPINGS } from './handlers';

describe('MOCK_MAPPINGS', () => {
  it('contains all expected mapping categories', () => {
    expect(MOCK_MAPPINGS.actions).toBeDefined();
    expect(MOCK_MAPPINGS.operators).toBeDefined();
    expect(MOCK_MAPPINGS.leftOperands).toBeDefined();
    expect(MOCK_MAPPINGS.rightOperands).toBeDefined();
    expect(MOCK_MAPPINGS.assignees).toBeDefined();
    expect(MOCK_MAPPINGS.targets).toBeDefined();
    expect(MOCK_MAPPINGS.constraints).toBeDefined();
    expect(MOCK_MAPPINGS.operands).toBeDefined();
  });

  it('each mapping has a name and description', () => {
    const allMappings = [
      ...(MOCK_MAPPINGS.actions ?? []),
      ...(MOCK_MAPPINGS.operators ?? []),
      ...(MOCK_MAPPINGS.leftOperands ?? []),
      ...(MOCK_MAPPINGS.rightOperands ?? []),
      ...(MOCK_MAPPINGS.assignees ?? []),
      ...(MOCK_MAPPINGS.targets ?? []),
      ...(MOCK_MAPPINGS.constraints ?? []),
    ];

    for (const mapping of allMappings) {
      expect(mapping.name).toBeDefined();
      expect(typeof mapping.name).toBe('string');
      expect(mapping.description).toBeDefined();
      expect(typeof mapping.description).toBe('string');
    }
  });

  it('action names follow namespace:value pattern', () => {
    for (const action of MOCK_MAPPINGS.actions ?? []) {
      expect(action.name).toMatch(/^[\w-]+:[\w-]+$/);
    }
  });

  it('contains actions from multiple namespaces', () => {
    const namespaces = new Set(
      (MOCK_MAPPINGS.actions ?? []).map((a) => a.name?.split(':')[0])
    );
    expect(namespaces.size).toBeGreaterThanOrEqual(2);
  });
});

describe('Mock /mappings endpoint', () => {
  it('returns mappings data via MSW', async () => {
    const response = await fetch('/mappings');
    expect(response.ok).toBe(true);

    const data = await response.json();
    expect(data.actions).toBeDefined();
    expect(data.actions.length).toBeGreaterThan(0);
    expect(data.operators).toBeDefined();
  });
});

describe('Mock /validate endpoint', () => {
  it('returns allow when policy is present', async () => {
    const response = await fetch('/validate', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ policy: { '@type': 'odrl:Set' } }),
    });
    expect(response.ok).toBe(true);

    const data = await response.json();
    expect(data.allow).toBe(true);
    expect(data.explanation).toEqual([]);
  });

  it('returns deny when no policy is present', async () => {
    const response = await fetch('/validate', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({}),
    });
    expect(response.ok).toBe(true);

    const data = await response.json();
    expect(data.allow).toBe(false);
    expect(data.explanation.length).toBeGreaterThan(0);
  });
});
