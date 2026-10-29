/**
 * Tests for the generated default mappings and for merging host mappings over them.
 */
import { describe, it, expect } from 'vitest';
import { DEFAULT_MAPPINGS, mergeMappings } from './defaultMappings';

describe('default mappings', () => {
  it('exposes every attribute the builder reads', () => {
    for (const field of ['actions', 'operators', 'leftOperands', 'rightOperands', 'targets', 'assignees'] as const) {
      expect(DEFAULT_MAPPINGS[field]?.length ?? 0).toBeGreaterThan(0);
    }
  });

  it('does not leak the rego implementation details', () => {
    expect(JSON.stringify(DEFAULT_MAPPINGS)).not.toMatch(/regoMethod|regoPackage/);
  });

  it('counts parameters like the backend', () => {
    const body = DEFAULT_MAPPINGS.leftOperands?.find((m) => m.name === 'http:bodyValue');
    expect(body?.parameterCount).toBe(1);
    const path = DEFAULT_MAPPINGS.leftOperands?.find((m) => m.name === 'http:path');
    expect(path?.parameterCount).toBe(0);
  });
});

describe('mergeMappings', () => {
  const base = {
    actions: [{ name: 'odrl:use', description: 'base', parameterCount: 0 }, { name: 'odrl:read', parameterCount: 0 }],
    operators: [{ name: 'odrl:eq', parameterCount: 2 }],
  };

  it('returns the base untouched without overrides', () => {
    expect(mergeMappings(base)).toBe(base);
  });

  it('replaces a mapping with the same name in place and keeps the rest', () => {
    const merged = mergeMappings(base, { actions: [{ name: 'odrl:use', description: 'host', parameterCount: 0 }] });

    expect(merged.actions).toEqual([
      { name: 'odrl:use', description: 'host', parameterCount: 0 },
      { name: 'odrl:read', parameterCount: 0 },
    ]);
    expect(merged.operators).toBe(base.operators);
  });

  it('appends mappings with a new name, including for attributes the base lacks', () => {
    const merged = mergeMappings(base, {
      actions: [{ name: 'dome-op:buy' }],
      targets: [{ name: 'odrl:uid' }],
    });

    expect(merged.actions?.map((m) => m.name)).toEqual(['odrl:use', 'odrl:read', 'dome-op:buy']);
    expect(merged.targets).toEqual([{ name: 'odrl:uid' }]);
  });

  it('does not mutate the base', () => {
    mergeMappings(base, { actions: [{ name: 'odrl:use', description: 'host' }] });

    expect(base.actions[0].description).toBe('base');
  });
});

