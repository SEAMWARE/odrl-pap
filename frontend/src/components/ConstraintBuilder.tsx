/**
 * Constraint builder component for ODRL policies.
 *
 * Allows users to add, remove, and configure policy constraints with
 * namespace-grouped operand/operator dropdowns, clear logical grouping
 * labels (AND/OR/XONE), and visual card styling for each constraint.
 */
import { Form, Row, Col, Button, Stack, Card, Badge } from 'react-bootstrap';
import type { Mappings } from '../services/api';
import type { Mapping } from '../api';
import { useI18n } from '../i18n';
import NamespacedDropdown from './NamespacedDropdown';
import { readConstraintGroup } from '../utils/constraints';
import { getTerm, removeTerm, setTerm, termKey } from '../utils/odrlTerms';

/** Default parent property that holds the constraint(s). */
const DEFAULT_PROPERTY_KEY = 'odrl:constraint';

interface ConstraintBuilderProps {
  /** Parent object containing the constraint field (see {@link ConstraintBuilderProps.propertyKey}). */
  parent: Record<string, unknown>;
  /** Callback to update the parent object. */
  setParent: (newParent: Record<string, unknown>) => void;
  /** Available mappings for populating dropdowns. */
  mappings: Mappings;
  /** When `true`, all constraint controls are disabled (set by template mode). */
  locked?: boolean;
  /**
   * The parent property that holds the constraint(s). Defaults to
   * `odrl:constraint`. Party/asset collections store their constraints under
   * `odrl:refinement`, so those editors pass that key instead.
   */
  propertyKey?: string;
}

/** Represents a single constraint's data shape. */
interface ConstraintData {
  'odrl:leftOperand'?: { '@id': string } | string;
  'odrl:operator'?: { '@id': string } | string;
  'odrl:rightOperand'?: { '@id'?: string; '@value'?: string; '@type'?: string } | string;
  [key: string]: unknown;
}

/**
 * Returns the number of parameters the given operand requires, as reported by the
 * `/mappings` endpoint. Unknown operands are treated as parameterless.
 */
function getParameterCount(operands: Mapping[], operandName: string): number {
  return operands.find((operand) => operand.name === operandName)?.parameterCount ?? 0;
}

/** Ordered AND: the builder has no grouping for it, so it is shown read-only instead of dropped. */
const ANDSEQUENCE_TYPE = 'andSequence';

/**
 * Reads the constraints held by a parent's constraint property, whatever shape they were written
 * in (see {@link readConstraintGroup}). Returns `undefined` when there is nothing to read.
 */
function readConstraints(
  constraint: unknown,
): { logicalType: string; constraints: ConstraintData[]; listWrapper: boolean } | undefined {
  return readConstraintGroup(constraint) as
    | { logicalType: string; constraints: ConstraintData[]; listWrapper: boolean }
    | undefined;
}
/**
 * Returns the left operand selected in the given constraint.
 *
 * A left operand is either referenced by name (`"odrl:leftOperand": {"@id": "http:path"}`) or - for
 * operands that require a parameter - written as the namespaced key itself, carrying the parameter as
 * its value (`"http:bodyValue": "$.my.property"`).
 */
function getLeftOperandId(constraint: ConstraintData, operands: Mapping[]): string {
  const referenced = getTerm(constraint, 'leftOperand') as ConstraintData['odrl:leftOperand'] | undefined;
  if (typeof referenced === 'string' && referenced) {
    return referenced;
  }
  if (referenced && typeof referenced === 'object' && referenced['@id']) {
    return referenced['@id'];
  }
  return Object.keys(constraint).find((key) => operands.some((operand) => operand.name === key)) ?? '';
}

/**
 * Returns the parameter of a parameterized left operand, which is stored as the value of the
 * operand's own key. Accepts both the plain and the JSON-LD value-object form.
 */
function getLeftOperandParameter(constraint: ConstraintData, operandName: string): string {
  const value = constraint[operandName];
  if (typeof value === 'string') {
    return value;
  }
  if (value && typeof value === 'object' && '@value' in value) {
    return String((value as { '@value'?: unknown })['@value'] ?? '');
  }
  return '';
}

/**
 * Determines whether a right operand uses a named (URI) or literal value.
 */
function getRightOperandType(constraint: ConstraintData): 'named' | 'literal' {
  const rightOperand = getTerm(constraint, 'rightOperand') as ConstraintData['odrl:rightOperand'];
  if (!rightOperand) return 'named';
  // A plain string is a literal value (e.g. "/items").
  if (typeof rightOperand === 'string') return 'literal';
  if (rightOperand['@value'] !== undefined) return 'literal';
  return 'named';
}

/** Reads the right operand as a value object; a plain string becomes `{ '@value': string }`. */
function getRightOperandObject(
  constraint: ConstraintData,
): { '@id'?: string; '@value'?: string; '@type'?: string } {
  const rightOperand = getTerm(constraint, 'rightOperand') as ConstraintData['odrl:rightOperand'];
  if (typeof rightOperand === 'string') return { '@value': rightOperand };
  return rightOperand ?? {};
}

/** Reads the `@id` of a reference property that may also be a plain string (e.g. the operator). */
function getReferenceId(value: { '@id'?: string } | string | undefined): string {
  if (typeof value === 'string') return value;
  return value?.['@id'] || '';
}

/**
 * Interactive constraint builder with namespace-grouped dropdowns and
 * clear logical-grouping indicators.
 */
const ConstraintBuilder = ({
  parent,
  setParent,
  mappings,
  locked: lockedProp = false,
  propertyKey = DEFAULT_PROPERTY_KEY,
}: ConstraintBuilderProps) => {
  const { strings } = useI18n();
  const t = strings.constraintBuilder;

  // The parent is the single source of truth: the constraints and their logical grouping are read
  // from it on every render and every edit is written straight back. An earlier version kept its own
  // copy of the list in state and synced it with the parent through two effects; when both copies
  // changed in the same render the effects swapped them back and forth forever ("Maximum update
  // depth exceeded", the value flickering), whenever the editor opened a policy that had
  // constraints or the user typed quickly.
  // ODRL terms may be spelled prefixed or bare; the key the parent already uses is the one written.
  const constraintKey = termKey(parent, propertyKey);
  const parentConstraint = parent[constraintKey];
  const held = readConstraints(parentConstraint);
  const internalConstraints: ConstraintData[] = held?.constraints ?? [];
  const logicalType = held?.logicalType ?? 'and';
  const locked = lockedProp || logicalType === ANDSEQUENCE_TYPE;

  /** Writes a list (and, optionally, a new logical grouping) into the parent. */
  const write = (constraints: ConstraintData[], type: string = logicalType) => {
    // A list written as a JSON-LD `@list` stays one.
    const operands = held?.listWrapper ? { '@list': constraints } : constraints;
    setParent({
      ...parent,
      [constraintKey]:
        type === 'and'
          ? operands
          : { '@type': 'odrl:LogicalConstraint', [`odrl:${type}`]: operands },
    });
  };

  const handleConstraintChange = (index: number, field: string, value: unknown) => {
    const newConstraints = [...internalConstraints];
    newConstraints[index] = setTerm(newConstraints[index], field, value);
    write(newConstraints);
  };

  const replaceConstraint = (index: number, constraint: ConstraintData) => {
    const newConstraints = [...internalConstraints];
    newConstraints[index] = constraint;
    write(newConstraints);
  };

  /**
   * Selects the left operand of a constraint.
   *
   * Operands that require a parameter (e.g. `http:bodyValue`) have to be written as the namespaced key
   * itself, carrying the parameter as its value, since the backend takes the parameter from there.
   * Parameterless operands are referenced by name via `odrl:leftOperand`. Only one of both forms may be
   * present, so any previous representation is removed first.
   */
  const handleLeftOperandChange = (index: number, operandName: string) => {
    const current = internalConstraints[index];
    const previousOperand = getLeftOperandId(current, leftOperands);
    const previousParameter = getLeftOperandParameter(current, previousOperand);

    const leftOperandKey = termKey(current, 'leftOperand');
    const updated: ConstraintData = removeTerm(current, 'leftOperand');
    leftOperands.forEach((operand) => {
      if (operand.name) {
        delete updated[operand.name];
      }
    });

    if (operandName) {
      if (getParameterCount(leftOperands, operandName) > 0) {
        updated[operandName] = previousParameter;
      } else {
        updated[leftOperandKey] = { '@id': operandName };
      }
    }
    replaceConstraint(index, updated);
  };

  const addConstraint = () => {
    write([...internalConstraints, {} as ConstraintData]);
  };

  const removeConstraint = (index: number) => {
    const newConstraints = [...internalConstraints];
    newConstraints.splice(index, 1);
    write(newConstraints);
  };

  const handleRightOperandTypeChange = (index: number, type: 'named' | 'literal') => {
    if (type === 'named') {
      handleConstraintChange(index, 'odrl:rightOperand', { '@id': '' });
    } else {
      handleConstraintChange(index, 'odrl:rightOperand', { '@value': '', '@type': '' });
    }
  };

  const leftOperands = mappings.leftOperands ?? mappings.operands ?? [];
  const rightOperands = mappings.rightOperands ?? mappings.operands ?? [];
  const operators = mappings.operators ?? [];

  /** Returns a description for the current logical type. */
  const getLogicalHelp = (): string => {
    switch (logicalType) {
      case 'or':
        return t.groupingOrHelp;
      case 'xone':
        return t.groupingXoneHelp;
      default:
        return t.groupingAndHelp;
    }
  };

  return (
    <Stack gap={3}>
      {/* Logical grouping selector (shown when >1 constraint) */}
      {internalConstraints.length > 1 && (
        <Card
          border="info"
          style={{ backgroundColor: 'var(--odrl-card-bg, #f8f9fa)' }}
        >
          <Card.Body className="py-2">
            <Row className="align-items-center">
              <Col md={5}>
                <Form.Group>
                  <Form.Label className="fw-bold mb-1">{t.groupingLabel}</Form.Label>
                  <Form.Select
                    value={logicalType}
                    onChange={(e) => write(internalConstraints, e.target.value)}
                    aria-label={t.groupingLabel}
                    disabled={locked}
                  >
                    <option value="and">{t.groupingAnd}</option>
                    <option value="or">{t.groupingOr}</option>
                    <option value="xone">{t.groupingXone}</option>
                    {logicalType === ANDSEQUENCE_TYPE && <option value={ANDSEQUENCE_TYPE}>andSequence</option>}
                  </Form.Select>
                </Form.Group>
              </Col>
              <Col md={7}>
                <div className="text-muted small mt-2 mt-md-0">
                  {getLogicalHelp()}
                </div>
              </Col>
            </Row>
          </Card.Body>
        </Card>
      )}

      {/* Individual constraint cards */}
      {internalConstraints.map((constraint, index) => (
        <Card
          key={index}
          style={{ boxShadow: 'var(--odrl-card-shadow)' }}
        >
          <Card.Header className="d-flex justify-content-between align-items-center py-2">
            <span>
              <Badge bg="secondary" className="me-2">
                {index + 1}
              </Badge>
              {t.constraintLabel}
            </span>
            <Button
              variant="outline-danger"
              size="sm"
              onClick={() => removeConstraint(index)}
              aria-label={`${t.removeConstraint} ${index + 1}`}
              disabled={locked}
            >
              {t.removeConstraint}
            </Button>
          </Card.Header>
          <Card.Body>
            <Row className="g-3">
              {/* Left operand */}
              <Col md={4}>
                <Form.Label className="small fw-bold">{t.selectLeftOperand}</Form.Label>
                <NamespacedDropdown
                  items={leftOperands}
                  value={getLeftOperandId(constraint, leftOperands)}
                  onChange={(val) => handleLeftOperandChange(index, val)}
                  placeholder={t.selectLeftOperand}
                  ariaLabel={`${t.selectLeftOperand} ${index + 1}`}
                  disabled={locked}
                />
                {getParameterCount(leftOperands, getLeftOperandId(constraint, leftOperands)) > 0 && (
                  <div className="mt-2">
                    <Form.Label className="small fw-bold">{t.leftOperandParameter}</Form.Label>
                    <Form.Control
                      type="text"
                      placeholder={t.leftOperandParameterPlaceholder}
                      value={getLeftOperandParameter(
                        constraint,
                        getLeftOperandId(constraint, leftOperands),
                      )}
                      onChange={(e) =>
                        handleConstraintChange(
                          index,
                          getLeftOperandId(constraint, leftOperands),
                          e.target.value,
                        )
                      }
                      aria-label={`${t.leftOperandParameter} ${index + 1}`}
                      disabled={locked}
                    />
                    <Form.Text className="text-muted">{t.leftOperandParameterHelp}</Form.Text>
                  </div>
                )}
              </Col>

              {/* Operator */}
              <Col md={3}>
                <Form.Label className="small fw-bold">{t.selectOperator}</Form.Label>
                <NamespacedDropdown
                  items={operators}
                  value={getReferenceId(getTerm(constraint, 'operator') as ConstraintData['odrl:operator'])}
                  onChange={(val) =>
                    handleConstraintChange(index, 'odrl:operator', { '@id': val })
                  }
                  placeholder={t.selectOperator}
                  ariaLabel={`${t.selectOperator} ${index + 1}`}
                  disabled={locked}
                />
              </Col>

              {/* Right operand */}
              <Col md={5}>
                <Form.Label className="small fw-bold">{t.selectRightOperand}</Form.Label>
                <Row className="g-2">
                  <Col sm={5}>
                    <Form.Select
                      value={getRightOperandType(constraint)}
                      onChange={(e) =>
                        handleRightOperandTypeChange(index, e.target.value as 'named' | 'literal')
                      }
                      aria-label={`Right operand type for constraint ${index + 1}`}
                      disabled={locked}
                    >
                      <option value="named">{t.rightOperandNamed}</option>
                      <option value="literal">{t.rightOperandLiteral}</option>
                    </Form.Select>
                  </Col>
                  <Col sm={7}>
                    {getRightOperandType(constraint) === 'named' ? (
                      <NamespacedDropdown
                        items={rightOperands}
                        value={getRightOperandObject(constraint)['@id'] || ''}
                        onChange={(val) =>
                          handleConstraintChange(index, 'odrl:rightOperand', { '@id': val })
                        }
                        placeholder={t.selectRightOperand}
                        ariaLabel={`${t.selectRightOperand} ${index + 1}`}
                        disabled={locked}
                      />
                    ) : (
                      <Stack direction="horizontal" gap={2}>
                        <Form.Control
                          type="text"
                          placeholder={t.valuePlaceholder}
                          value={getRightOperandObject(constraint)['@value'] || ''}
                          onChange={(e) =>
                            handleConstraintChange(index, 'odrl:rightOperand', {
                              ...getRightOperandObject(constraint),
                              '@value': e.target.value,
                            })
                          }
                          aria-label={`${t.valuePlaceholder} for constraint ${index + 1}`}
                          disabled={locked}
                        />
                        <Form.Control
                          type="text"
                          placeholder={t.typePlaceholder}
                          value={getRightOperandObject(constraint)['@type'] || ''}
                          onChange={(e) =>
                            handleConstraintChange(index, 'odrl:rightOperand', {
                              ...getRightOperandObject(constraint),
                              '@type': e.target.value,
                            })
                          }
                          aria-label={`${t.typePlaceholder} for constraint ${index + 1}`}
                          disabled={locked}
                        />
                      </Stack>
                    )}
                  </Col>
                </Row>
              </Col>
            </Row>
          </Card.Body>
        </Card>
      ))}

      {/* Add constraint button */}
      <div>
        <Button variant="outline-primary" onClick={addConstraint} disabled={locked}>
          + {t.addConstraint}
        </Button>
      </div>
    </Stack>
  );
};

export default ConstraintBuilder;
