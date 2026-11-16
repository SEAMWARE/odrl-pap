import { describe, expect, it } from 'vitest';
import { readConstraintGroup } from './constraints';

describe('bare and IRI logical operands', () => {
  it('reads a bare or and an IRI xone group', () => {
    const bare = readConstraintGroup({ or: [{ a: 1 }, { a: 2 }] });
    expect(bare?.logicalType).toBe('or');
    expect(bare?.constraints).toHaveLength(2);
    const iri = readConstraintGroup({ 'http://www.w3.org/ns/odrl/2/xone': [{ a: 1 }] });
    expect(iri?.logicalType).toBe('xone');
  });
});
