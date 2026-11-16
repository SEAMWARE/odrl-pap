/**
 * Helpers for recognising and creating ODRL asset/party collections.
 *
 * Policies in the wild write the collection type both prefixed (`odrl:AssetCollection`, what the
 * PAP backend and every reference policy use) and bare (`AssetCollection`, as the ODRL JSON-LD
 * context allows), so detection accepts both. New collections are written prefixed, since the
 * editor's default `@context` only defines the `odrl` prefix and does not carry the ODRL term
 * definitions that would expand a bare type.
 */

/** Optional namespace prefix of ODRL types. */
const ODRL_PREFIX = 'odrl:';

/** `odrl:source` value that every reference policy uses for asset collections. */
export const ASSET_COLLECTION_SOURCE = 'urn:asset';

/** Checks whether the value is an object whose `@type` is `name` or `odrl:name`. */
export function isCollectionOfType(value: unknown, name: 'AssetCollection' | 'PartyCollection'): boolean {
  if (typeof value !== 'object' || value === null) return false;
  const type = (value as Record<string, unknown>)['@type'];
  if (typeof type !== 'string') return false;
  return (type.startsWith(ODRL_PREFIX) ? type.slice(ODRL_PREFIX.length) : type) === name;
}

/** A new, empty asset collection. */
export function newAssetCollection(): Record<string, unknown> {
  return {
    '@type': `${ODRL_PREFIX}AssetCollection`,
    'odrl:source': ASSET_COLLECTION_SOURCE,
    'odrl:refinement': [],
  };
}

/** A new, empty party collection. */
export function newPartyCollection(): Record<string, unknown> {
  return { '@type': `${ODRL_PREFIX}PartyCollection`, 'odrl:refinement': [] };
}
