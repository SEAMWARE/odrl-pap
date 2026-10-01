/**
 * Unit tests for the JSON-LD `@context` helpers.
 */
import { describe, it, expect } from 'vitest';
import {
  isJsonLdContextObject,
  jsonLdContextEntryIri,
  type JsonLdContextEntry,
} from './jsonLdContext';

describe('isJsonLdContextObject', () => {
  it.each([
    ['prefix map', { odrl: 'http://www.w3.org/ns/odrl/2/' }, true],
    ['empty object', {}, true],
    ['term definition map', { odrl: { '@id': 'http://x/', '@prefix': true } }, true],
    ['remote context IRI', 'http://www.w3.org/ns/odrl.jsonld', false],
    ['array of contexts', ['http://a/', { b: 'http://b/' }], false],
    ['null', null, false],
    ['undefined', undefined, false],
  ])('returns %s → %s', (_name, value, expected) => {
    expect(isJsonLdContextObject(value)).toBe(expected);
  });
});

describe('jsonLdContextEntryIri', () => {
  it.each<[string, JsonLdContextEntry, string]>([
    ['plain IRI string', 'http://www.w3.org/ns/odrl/2/', 'http://www.w3.org/ns/odrl/2/'],
    [
      'expanded term definition',
      { '@id': 'http://www.w3.org/ns/odrl/2/', '@prefix': true },
      'http://www.w3.org/ns/odrl/2/',
    ],
    ['term definition without @id', { '@type': '@id' }, '{"@type":"@id"}'],
  ])('resolves a %s', (_name, entry, expected) => {
    expect(jsonLdContextEntryIri(entry)).toBe(expected);
  });
});
