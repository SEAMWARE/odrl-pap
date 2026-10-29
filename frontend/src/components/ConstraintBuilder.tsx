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

/** Default parent property that holds the constraint(s). */
const DEFAULT_PROPERTY_KEY = 'odrl:constraint';

/** Key under which a left operand is referenced by name. */
const LEFT_OPERAND_KEY = 'odrl:leftOperand';

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
  'odrl:leftOperand'?: { '@id': string };
  'odrl:operator'?: { '@id': string };
  'odrl:rightOperand'?: { '@id'?: string; '@value'?: string; '@type'?: string };
  [key: string]: unknown;
}

/**
 * Returns the number of parameters the given operand requires, as reported by the
 * `/mappings` endpoint. Unknown operands are treated as parameterless.
 */
function getParameterCount(operands: Mapping[], operandName: string): number {
  return operands.find((operand) => operand.name === operandName)?.parameterCount ?? 0;
}

/**
 * Reads the constraints held by a parent's constraint property.
 *
 * Handles the three shapes ODRL allows: a logical constraint (`odrl:or` /
 * `odrl:xone`), a list (an implicit AND) and a lone constraint object, which
 * templates commonly emit. Returns `undefined` when there is nothing to read.
 */
function readConstraints(
  constraint: Record<string, unknown> | ConstraintData[] | undefined,
): { logicalType: string; constraints: ConstraintData[] } | undefined {
  if (!constraint) return undefined;
  if (Array.isArray(constraint)) {
    return { logicalType: 'and', constraints: constraint };
  }
  if (typeof constraint !== 'object') return undefined;
  if (constraint['@type'] === 'odrl:LogicalConstraint') {
    if (constraint['odrl:or']) {
      return { logicalType: 'or', constraints: constraint['odrl:or'] as ConstraintData[] };
    }
    if (constraint['odrl:xone']) {
      return { logicalType: 'xone', constraints: constraint['odrl:xone'] as ConstraintData[] };
    }
    return undefined;
  }
  // A single constraint object (a lone constraint/refinement rather than an array):
  // load it as a one-element list so it becomes editable instead of being dropped.
  return { logicalType: 'and', constraints: [constraint as ConstraintData] };
}

/**
 * Returns the left operand selected in the given constraint.
 *
 * A left operand is either referenced by name (`"odrl:leftOperand": {"@id": "http:path"}`) or - for
 * operands that require a parameter - written as the namespaced key itself, carrying the parameter as
 * its value (`"http:bodyValue": "$.my.property"`).
 */
function getLeftOperandId(constraint: ConstraintData, operands: Mapping[]): string {
  const referenced = constraint[LEFT_OPERAND_KEY] as { '@id'?: string } | undefined;
  if (referenced?.['@id']) {
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
  const rightOperand = constraint['odrl:rightOperand'];
  if (!rightOperand) return 'named';
  if (rightOperand['@value'] !== undefined) return 'literal';
  return 'named';
}

/**
 * Interactive constraint builder with namespace-grouped dropdowns and
 * clear logical-grouping indicators.
 */
const ConstraintBuilder = ({
  parent,
  setParent,
  mappings,
  locked = false,
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
  const parentConstraint = parent[propertyKey] as Record<string, unknown> | ConstraintData[] | undefined;
  const held = readConstraints(parentConstraint);
  const internalConstraints: ConstraintData[] = held?.constraints ?? [];
  const logicalType = held?.logicalType ?? 'and';

  /** Writes a list (and, optionally, a new logical grouping) into the parent. */
  const write = (constraints: ConstraintData[], type: string = logicalType) => {
    setParent({
      ...parent,
      [propertyKey]:
        type === 'and'
          ? constraints
          : { '@type': 'odrl:LogicalConstraint', [`odrl:${type}`]: constraints },
    });
  };

  const handleConstraintChange = (index: number, field: string, value: unknown) => {
    const newConstraints = [...internalConstraints];
    newConstraints[index] = { ...newConstraints[index], [field]: value };
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

    const updated: ConstraintData = { ...current };
    delete updated[LEFT_OPERAND_KEY];
    leftOperands.forEach((operand) => {
      if (operand.name) {
        delete updated[operand.name];
      }
    });

    if (operandName) {
      if (getParameterCount(leftOperands, operandName) > 0) {
        updated[operandName] = previousParameter;
      } else {
        updated[LEFT_OPERAND_KEY] = { '@id': operandName };
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
                  value={constraint['odrl:operator']?.['@id'] || ''}
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
                        value={constraint['odrl:rightOperand']?.['@id'] || ''}
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
                          value={constraint['odrl:rightOperand']?.['@value'] || ''}
                          onChange={(e) =>
                            handleConstraintChange(index, 'odrl:rightOperand', {
                              ...constraint['odrl:rightOperand'],
                              '@value': e.target.value,
                            })
                          }
                          aria-label={`${t.valuePlaceholder} for constraint ${index + 1}`}
                          disabled={locked}
                        />
                        <Form.Control
                          type="text"
                          placeholder={t.typePlaceholder}
                          value={constraint['odrl:rightOperand']?.['@type'] || ''}
                          onChange={(e) =>
                            handleConstraintChange(index, 'odrl:rightOperand', {
                              ...constraint['odrl:rightOperand'],
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
