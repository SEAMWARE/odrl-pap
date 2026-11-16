import { describe, it, expect } from 'vitest';
import { findTermKey, getTerm, removeTerm, setTerm, termKey } from './odrlTerms';

const IRI = 'http://www.w3.org/ns/odrl/2/';

describe('odrlTerms', () => {
  it('reads prefixed, bare and full-IRI spellings', () => {
    expect(getTerm({ 'odrl:target': 'a' }, 'target')).toBe('a');
    expect(getTerm({ target: 'b' }, 'target')).toBe('b');
    expect(getTerm({ [`${IRI}target`]: 'c' }, 'target')).toBe('c');
  });

  it('accepts the term with or without the odrl prefix', () => {
    expect(getTerm({ target: 'x' }, 'odrl:target')).toBe('x');
  });

  it('returns undefined when the term is missing or the object is absent', () => {
    expect(getTerm({}, 'target')).toBeUndefined();
    expect(getTerm(null, 'target')).toBeUndefined();
    expect(getTerm(undefined, 'target')).toBeUndefined();
  });

  it('writes to the key the object already uses', () => {
    expect(setTerm({ target: 'a' }, 'target', 'b')).toEqual({ target: 'b' });
    expect(setTerm({ 'odrl:target': 'a' }, 'target', 'b')).toEqual({ 'odrl:target': 'b' });
    expect(setTerm({ [`${IRI}target`]: 'a' }, 'target', 'b')).toEqual({ [`${IRI}target`]: 'b' });
  });

  it('writes the prefixed key when the term is new', () => {
    expect(setTerm({ other: 1 }, 'target', 'b')).toEqual({ other: 1, 'odrl:target': 'b' });
    expect(termKey({}, 'action')).toBe('odrl:action');
  });

  it('does not touch the other keys nor mutate its input', () => {
    const input = { target: 'a', action: 'read' };
    const output = setTerm(input, 'target', 'b');
    expect(output).toEqual({ target: 'b', action: 'read' });
    expect(input).toEqual({ target: 'a', action: 'read' });
  });

  it('finds the key in use, preferring the prefixed one when both exist', () => {
    expect(findTermKey({ target: 'a' }, 'target')).toBe('target');
    expect(findTermKey({ target: 'a', 'odrl:target': 'b' }, 'target')).toBe('odrl:target');
    expect(findTermKey({}, 'target')).toBeUndefined();
  });

  it('removes every spelling of a term', () => {
    expect(removeTerm({ 'odrl:target': 1, target: 2, keep: 3 }, 'target')).toEqual({ keep: 3 });
  });
});

import { spelledLike } from './odrlTerms';
describe('spelledLike', () => {
  it('follows the spelling of the sample key', () => {
    expect(spelledLike('odrl:constraint', 'or')).toBe('odrl:or');
    expect(spelledLike('constraint', 'or')).toBe('or');
    expect(spelledLike('http://www.w3.org/ns/odrl/2/refinement', 'xone')).toBe('http://www.w3.org/ns/odrl/2/xone');
  });
});
