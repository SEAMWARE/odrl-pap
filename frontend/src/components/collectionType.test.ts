/** Unit tests for the asset/party collection type helpers. */
import { describe, it, expect } from 'vitest';
import {
  ASSET_COLLECTION_SOURCE,
  isCollectionOfType,
  newAssetCollection,
  newPartyCollection,
} from './collectionType';

describe('isCollectionOfType', () => {
  it('accepts the prefixed and the bare type', () => {
    expect(isCollectionOfType({ '@type': 'odrl:AssetCollection' }, 'AssetCollection')).toBe(true);
    expect(isCollectionOfType({ '@type': 'AssetCollection' }, 'AssetCollection')).toBe(true);
    expect(isCollectionOfType({ '@type': 'odrl:PartyCollection' }, 'PartyCollection')).toBe(true);
    expect(isCollectionOfType({ '@type': 'PartyCollection' }, 'PartyCollection')).toBe(true);
  });

  it('rejects other types, other prefixes and non-objects', () => {
    expect(isCollectionOfType({ '@type': 'odrl:Asset' }, 'AssetCollection')).toBe(false);
    expect(isCollectionOfType({ '@type': 'odrl:PartyCollection' }, 'AssetCollection')).toBe(false);
    expect(isCollectionOfType({ '@type': 'other:AssetCollection' }, 'AssetCollection')).toBe(false);
    expect(isCollectionOfType({ '@type': ['odrl:AssetCollection'] }, 'AssetCollection')).toBe(false);
    expect(isCollectionOfType('https://example.com/x', 'AssetCollection')).toBe(false);
    expect(isCollectionOfType(null, 'AssetCollection')).toBe(false);
    expect(isCollectionOfType(undefined, 'AssetCollection')).toBe(false);
  });
});

describe('new collections', () => {
  it('creates a prefixed asset collection with the reference source', () => {
    expect(newAssetCollection()).toEqual({
      '@type': 'odrl:AssetCollection',
      'odrl:source': ASSET_COLLECTION_SOURCE,
      'odrl:refinement': [],
    });
    expect(ASSET_COLLECTION_SOURCE).toBe('urn:asset');
  });

  it('creates a prefixed party collection', () => {
    expect(newPartyCollection()).toEqual({ '@type': 'odrl:PartyCollection', 'odrl:refinement': [] });
  });
});
