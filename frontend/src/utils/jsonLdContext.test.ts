/**
 * Unit tests for the JSON-LD `@context` helpers.
 */
import { describe, it, expect } from 'vitest';
import {
  isJsonLdContextObject,
  jsonLdContextEntryIri,
  addContextEntry,
  listContextEntries,
  removeContextPrefix,
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

describe('addContextEntry (P1-6)', () => {
  const ODRL = 'http://www.w3.org/ns/odrl/2/';

  it('creates an object context when there is none', () => {
    expect(addContextEntry(undefined, 'dome-op', 'https://x/')).toEqual({ 'dome-op': 'https://x/' });
  });

  it('adds the prefix to an object context, keeping its entries', () => {
    const ctx = { odrl: { '@id': ODRL, '@prefix': true } };
    expect(addContextEntry(ctx, 'dome-op', 'https://x/')).toEqual({ ...ctx, 'dome-op': 'https://x/' });
  });

  it('keeps a URL context as the first entry of an array instead of renaming it to odrl', () => {
    expect(addContextEntry(ODRL, 'dome-op', 'https://x/')).toEqual([ODRL, { 'dome-op': 'https://x/' }]);
  });

  it('appends to an array context without dropping any of its entries', () => {
    const ctx = [ODRL, 'https://other/context.jsonld'];
    expect(addContextEntry(ctx, 'dome-op', 'https://x/')).toEqual([...ctx, { 'dome-op': 'https://x/' }]);
  });

  it('adds the prefix to the last object of a mixed array', () => {
    const ctx = [ODRL, { odrl: ODRL }];
    expect(addContextEntry(ctx, 'dome-op', 'https://x/')).toEqual([ODRL, { odrl: ODRL, 'dome-op': 'https://x/' }]);
  });

  it('does not mutate its input', () => {
    const ctx = [ODRL, { odrl: ODRL }];
    addContextEntry(ctx, 'a', 'https://a/');
    expect(ctx).toEqual([ODRL, { odrl: ODRL }]);
  });
});

describe('listContextEntries and removeContextPrefix (P1-6)', () => {
  it('lists the prefixes of an object and of the objects in an array, and the remote URLs', () => {
    expect(listContextEntries({ a: 'https://a/' })).toEqual([{ prefix: 'a', uri: 'https://a/' }]);
    expect(listContextEntries(['https://r/', { a: 'https://a/', b: { '@id': 'https://b/' } }])).toEqual([
      { uri: 'https://r/' },
      { prefix: 'a', uri: 'https://a/' },
      { prefix: 'b', uri: 'https://b/' },
    ]);
    expect(listContextEntries('https://r/')).toEqual([{ uri: 'https://r/' }]);
    expect(listContextEntries(undefined)).toEqual([]);
  });

  it('removes a prefix from an object or from an object inside an array', () => {
    expect(removeContextPrefix({ a: 'x', b: 'y' }, 'a')).toEqual({ b: 'y' });
    expect(removeContextPrefix(['https://r/', { a: 'x', b: 'y' }], 'a')).toEqual(['https://r/', { b: 'y' }]);
  });

  it('drops an array object that ends up empty and leaves other contexts alone', () => {
    expect(removeContextPrefix(['https://r/', { a: 'x' }], 'a')).toEqual(['https://r/']);
    expect(removeContextPrefix('https://r/', 'a')).toBe('https://r/');
  });
});
