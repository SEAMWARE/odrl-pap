/**
 * Constraint builder component for ODRL policies.
 *
 * Allows users to add, remove, and configure policy constraints with
 * namespace-grouped operand/operator dropdowns, clear logical grouping
 * labels (AND/OR/XONE), and visual card styling for each constraint.
 */
import { Form, Row, Col, Button, Stack, Card, Badge } from 'react-bootstrap';
import type { Mappings } from '../services/api';
import { useState, useEffect } from 'react';
import { useI18n } from '../i18n';
import NamespacedDropdown from './NamespacedDropdown';

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
  'odrl:leftOperand'?: { '@id': string };
  'odrl:operator'?: { '@id': string };
  'odrl:rightOperand'?: { '@id'?: string; '@value'?: string; '@type'?: string };
  [key: string]: unknown;
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

  const [internalConstraints, setInternalConstraints] = useState<ConstraintData[]>([]);
  const [logicalType, setLogicalType] = useState('and');

  /** The parent's raw constraint value, extracted for dependency tracking. */
  const parentConstraint = parent[propertyKey];

  // Sync internal state from parent constraint
  useEffect(() => {
    const constraint = parentConstraint as Record<string, unknown> | ConstraintData[] | undefined;
    if (constraint) {
      if (
        typeof constraint === 'object' &&
        !Array.isArray(constraint) &&
        constraint['@type'] === 'odrl:LogicalConstraint'
      ) {
        if (constraint['odrl:or']) {
          setLogicalType('or');
          setInternalConstraints(constraint['odrl:or'] as ConstraintData[]);
        } else if (constraint['odrl:xone']) {
          setLogicalType('xone');
          setInternalConstraints(constraint['odrl:xone'] as ConstraintData[]);
        }
      } else if (Array.isArray(constraint)) {
        setLogicalType('and');
        setInternalConstraints(constraint);
      } else if (typeof constraint === 'object') {
        // A single constraint object (ODRL allows a lone constraint/refinement
        // rather than an array). Templates commonly emit this form; load it as
        // a one-element list so it becomes editable instead of being dropped.
        setLogicalType('and');
        setInternalConstraints([constraint as ConstraintData]);
      }
    }
  }, [parentConstraint]);

  // Propagate changes back to parent
  useEffect(() => {
    if (logicalType === 'and') {
      setParent({ ...parent, [propertyKey]: internalConstraints });
    } else {
      const logicalKey = `odrl:${logicalType}`;
      const logicalConstraint = {
        '@type': 'odrl:LogicalConstraint',
        [logicalKey]: internalConstraints,
      };
      setParent({ ...parent, [propertyKey]: logicalConstraint });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [internalConstraints, logicalType]);

  const handleConstraintChange = (index: number, field: string, value: unknown) => {
    const newConstraints = [...internalConstraints];
    newConstraints[index] = { ...newConstraints[index], [field]: value };
    setInternalConstraints(newConstraints);
  };

  const addConstraint = () => {
    setInternalConstraints([...internalConstraints, {} as ConstraintData]);
  };

  const removeConstraint = (index: number) => {
    const newConstraints = [...internalConstraints];
    newConstraints.splice(index, 1);
    setInternalConstraints(newConstraints);
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
                    onChange={(e) => setLogicalType(e.target.value)}
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
                  value={constraint['odrl:leftOperand']?.['@id'] || ''}
                  onChange={(val) =>
                    handleConstraintChange(index, 'odrl:leftOperand', { '@id': val })
                  }
                  placeholder={t.selectLeftOperand}
                  ariaLabel={`${t.selectLeftOperand} ${index + 1}`}
                  disabled={locked}
                />
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
        <Button variant="success" onClick={addConstraint} disabled={locked}>
          + {t.addConstraint}
        </Button>
      </div>
    </Stack>
  );
};

export default ConstraintBuilder;
