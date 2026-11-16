import { describe, it, expect } from 'vitest';
import { findTargetKey, isPlainTargetKey, writeTarget } from './target';

describe('target', () => {
  it('finds the key the target is stored under', () => {
    expect(findTargetKey({ 'odrl:target': 'a' })).toBe('odrl:target');
    expect(findTargetKey({ target: 'a' })).toBe('target');
    expect(findTargetKey({ 'json:uid': 'a' })).toBe('json:uid');
    expect(findTargetKey({ 'json:payloadTarget': 'a' })).toBe('json:payloadTarget');
    expect(findTargetKey({ 'odrl:action': 'a' })).toBeUndefined();
  });

  it('tells the plain target term from the namespaced options', () => {
    expect(isPlainTargetKey('odrl:target')).toBe(true);
    expect(isPlainTargetKey('target')).toBe(true);
    expect(isPlainTargetKey('json:target')).toBe(false);
    expect(isPlainTargetKey('odrl:uid')).toBe(false);
  });

  it('writes under the chosen key and drops the previous one', () => {
    expect(writeTarget({ 'odrl:target': 'a', x: 1 }, 'b', 'json:uid')).toEqual({ x: 1, 'json:uid': 'b' });
    expect(writeTarget({ 'json:uid': 'a' }, 'b', 'odrl:target')).toEqual({ 'odrl:target': 'b' });
  });

  it('keeps the key order when the key does not change', () => {
    const out = writeTarget({ 'odrl:target': 'a', 'odrl:action': 'x' }, 'b');
    expect(Object.keys(out)).toEqual(['odrl:target', 'odrl:action']);
    expect(out['odrl:target']).toBe('b');
  });

  it('keeps a bare target spelling for a plain target', () => {
    expect(writeTarget({ target: 'a' }, 'b', 'odrl:target')).toEqual({ target: 'b' });
  });

  it('does not mutate its input', () => {
    const input = { 'json:uid': 'a' };
    writeTarget(input, 'b', 'odrl:target');
    expect(input).toEqual({ 'json:uid': 'a' });
  });
});

describe('custom target mappings', () => {
  it('finds and replaces a target key that only the mappings define', () => {
    expect(findTargetKey({ 'custom:target': 'a' })).toBeUndefined();
    expect(findTargetKey({ 'custom:target': 'a' }, ['custom:target'])).toBe('custom:target');
    expect(writeTarget({ 'custom:target': 'a' }, 'b', 'json:uid', ['custom:target'])).toEqual({ 'json:uid': 'b' });
  });
});
