/**
 * Normalisation of the constraint values of a policy.
 *
 * ODRL (as JSON-LD) lets the same constraints be written in several shapes: an array, a JSON-LD
 * `{"@list": [...]}`, a lone object, or a logical constraint (`odrl:or`, `odrl:xone`, `odrl:and`,
 * `odrl:andSequence`) whose operands may in turn be any of those. Both the Builder and the Summary
 * read them through {@link readConstraintGroup}, so they agree on what a value means.
 */
/** JSON-LD keyword for an ordered list. */
const LIST_KEY = '@list';
/** Logical grouping keys, by the name the editor uses for the group. */
const LOGICAL_KEYS: ReadonlyArray<readonly [string, string]> = [
  ['or', 'odrl:or'],
  ['xone', 'odrl:xone'],
  ['and', 'odrl:and'],
  ['andSequence', 'odrl:andSequence'],
];

export type ConstraintObject = Record<string, unknown>;

/** The constraints held by a value, and how they are grouped. */
export interface ConstraintGroup {
  /** `and` for an array, a list or a lone constraint; otherwise `or`, `xone` or `andSequence`. */
  logicalType: string;
  constraints: ConstraintObject[];
  /** `true` when the constraints are wrapped in an `@list`, so a write can keep that shape. */
  listWrapper: boolean;
}

function isObject(value: unknown): value is ConstraintObject {
  return typeof value === 'object' && value !== null && !Array.isArray(value);
}

/** Whether the value is a JSON-LD list object (`{"@list": ...}`). */
export function isJsonLdList(value: unknown): value is { '@list': unknown } {
  return isObject(value) && LIST_KEY in value;
}

/**
 * Turns an array, an `@list` or a lone object into an array of constraint objects. Anything that is
 * not an object (e.g. `null`) is dropped.
 */
export function toConstraintArray(value: unknown): ConstraintObject[] {
  if (value === undefined || value === null) return [];
  let items: unknown[];
  if (Array.isArray(value)) items = value;
  else if (isJsonLdList(value)) items = toArray(value[LIST_KEY]);
  else items = [value];
  return items.filter(isObject);
}

function toArray(value: unknown): unknown[] {
  if (value === undefined || value === null) return [];
  return Array.isArray(value) ? value : [value];
}

/**
 * Reads the constraints held by a value in any of the shapes above. Returns `undefined` when there
 * is nothing to read.
 */
export function readConstraintGroup(value: unknown): ConstraintGroup | undefined {
  if (value === undefined || value === null) return undefined;
  if (Array.isArray(value)) {
    return { logicalType: 'and', constraints: toConstraintArray(value), listWrapper: false };
  }
  if (!isObject(value)) return undefined;
  if (isJsonLdList(value)) {
    return { logicalType: 'and', constraints: toConstraintArray(value), listWrapper: true };
  }
  const logical = LOGICAL_KEYS.find(([, key]) => key in value);
  if (logical) {
    const [logicalType, key] = logical;
    return { logicalType, constraints: toConstraintArray(value[key]), listWrapper: isJsonLdList(value[key]) };
  }
  if (value['@type'] === 'odrl:LogicalConstraint') return undefined;
  // A lone constraint object rather than a list: load it as a one-element group.
  return { logicalType: 'and', constraints: [value], listWrapper: false };
}
