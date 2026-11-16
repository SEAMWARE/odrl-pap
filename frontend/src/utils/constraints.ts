/**
 * Normalisation of the constraint values of a policy.
 *
 * ODRL (as JSON-LD) lets the same constraints be written in several shapes: an array, a JSON-LD
 * `{"@list": [...]}`, a lone object, or a logical constraint (`odrl:or`, `odrl:xone`, `odrl:and`,
 * `odrl:andSequence`) whose operands may in turn be any of those. Both the Builder and the Summary
 * read them through {@link readConstraintGroup}, so they agree on what a value means.
 */
import { findTermKey } from './odrlTerms';

/** JSON-LD keyword for an ordered list. */
const LIST_KEY = '@list';
/** Logical grouping keys, by the name the editor uses for the group. */
const LOGICAL_TERMS = ['or', 'xone', 'and', 'andSequence'] as const;

export type ConstraintObject = Record<string, unknown>;

/** The constraints held by a value, and how they are grouped. */
export interface ConstraintGroup {
  /** `and` for an array, a list or a lone constraint; otherwise `or`, `xone` or `andSequence`. */
  logicalType: string;
  constraints: ConstraintObject[];
  /** `true` when the constraints are wrapped in an `@list`, so a write can keep that shape. */
  listWrapper: boolean;
  /**
   * For a logical constraint, the object as it was written and the key that holds the operands, so
   * that an edit which does not change the grouping can write it back unchanged around them.
   */
  logical?: { original: ConstraintObject; key: string };
}

/** Whether an `@type` names `LogicalConstraint`, prefixed, bare or as the full IRI. */
function isLogicalConstraintType(type: unknown): boolean {
  const types = Array.isArray(type) ? type : [type];
  return types.some((t) => typeof t === 'string' && termOfType(t) === 'LogicalConstraint');
}

function termOfType(type: string): string {
  return type.replace(/^odrl:/, '').replace(/^http:\/\/www\.w3\.org\/ns\/odrl\/2\//, '');
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
  // The operand may be prefixed (`odrl:or`), bare (`or`) or the full IRI.
  for (const logicalType of LOGICAL_TERMS) {
    const key = findTermKey(value, logicalType);
    if (key !== undefined) {
      return {
        logicalType,
        constraints: toConstraintArray(value[key]),
        listWrapper: isJsonLdList(value[key]),
        logical: { original: value, key },
      };
    }
  }
  if (isLogicalConstraintType(value['@type'])) return undefined;
  // A lone constraint object rather than a list: load it as a one-element group.
  return { logicalType: 'and', constraints: [value], listWrapper: false };
}
