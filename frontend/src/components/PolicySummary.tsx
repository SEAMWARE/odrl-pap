/**
 * Read-only policy summary component.
 *
 * Displays the current ODRL policy in a structured, human-readable
 * format with a toggle to show raw JSON. Includes copy-to-clipboard
 * for the JSON view and human-readable summaries.
 */
import { Card, ListGroup, Badge, Stack, Button, Collapse } from 'react-bootstrap';
import { useState, useCallback } from 'react';
import type { OdrlPolicyJson } from '../services/api';
import { useI18n } from '../i18n';

/** Delay in milliseconds before "Copied!" reverts to "Copy". */
const COPY_FEEDBACK_DELAY_MS = 2000;

interface PolicySummaryProps {
  /** The ODRL policy JSON to display. */
  policy: OdrlPolicyJson;
}

/**
 * Renders any kind of operand value as a display string.
 */
const renderOperand = (operand: unknown, notSet: string): string => {
  if (!operand) return notSet;
  if (typeof operand === 'string') return operand;
  const obj = operand as Record<string, string>;
  if (obj['@id']) return obj['@id'].replace('odrl:', '');
  if (obj['@value']) return `'${obj['@value']}' (${obj['@type']})`;
  return JSON.stringify(operand);
};

/**
 * Renders an ODRL action as a display string, tolerating both the plain
 * string form (`"odrl:read"`) and the object form (`{ "@id": "odrl:read" }`)
 * emitted by templates. The `odrl:` prefix is stripped for readability.
 */
const renderAction = (action: unknown, notSet: string): string => {
  if (!action) return notSet;
  return renderOperand(action, notSet).replace('odrl:', '');
};

/**
 * Generates a human-readable summary of the policy permission.
 */
function buildHumanSummary(
  action: unknown,
  target: unknown,
  assignee: unknown,
  notSet: string,
): string {
  const rawAction = renderAction(action, notSet);
  const actionStr = rawAction === notSet ? notSet : rawAction.toUpperCase();
  const targetStr = renderOperand(target, notSet);
  const assigneeStr = renderOperand(assignee, notSet);
  return `Allow ${actionStr} on ${targetStr} for ${assigneeStr}`;
}

/**
 * Policy summary card with structured display and JSON toggle.
 */
const PolicySummary = ({ policy }: PolicySummaryProps) => {
  const { strings } = useI18n();
  const t = strings.policySummary;
  const notSet = strings.common.notSet;

  const [showJson, setShowJson] = useState(false);
  const [copied, setCopied] = useState(false);

  const handleCopy = useCallback(async () => {
    try {
      await navigator.clipboard.writeText(JSON.stringify(policy, null, 2));
      setCopied(true);
      setTimeout(() => setCopied(false), COPY_FEEDBACK_DELAY_MS);
    } catch {
      // Fallback: select text for manual copy
    }
  }, [policy]);

  if (!policy) return null;

  const permission = (policy['odrl:permission'] || {}) as Record<string, unknown>;
  const target = permission['odrl:target'];
  const assignee = permission['odrl:assignee'];
  const action = permission['odrl:action'];
  const constraint = permission['odrl:constraint'] as Record<string, unknown> | unknown[] | undefined;

  const renderConstraintItem = (c: Record<string, unknown>, index: number) => (
    <ListGroup.Item key={index} as="li" className="d-flex justify-content-between align-items-start">
      <div className="ms-2 me-auto">
        <div className="fw-bold">{t.constraint}</div>
        <Stack direction="horizontal" gap={2}>
          <Badge bg="secondary">{renderOperand(c['odrl:leftOperand'], notSet)}</Badge>
          <span className="fw-bold text-primary">{renderOperand(c['odrl:operator'], notSet)}</span>
          <Badge bg="secondary">{renderOperand(c['odrl:rightOperand'], notSet)}</Badge>
        </Stack>
      </div>
    </ListGroup.Item>
  );

  /**
   * Normalizes an ODRL `odrl:refinement` value into an array of constraint
   * items. ODRL allows a refinement to be either a single constraint object
   * or an array of them; templates commonly emit the single-object form, so
   * both must be handled to avoid calling `.map` on a non-array.
   */
  const toRefinementArray = (refinement: unknown): Record<string, unknown>[] => {
    if (!refinement) return [];
    if (Array.isArray(refinement)) return refinement as Record<string, unknown>[];
    return [refinement as Record<string, unknown>];
  };

  const renderRefinements = (refinement: unknown) => {
    const refinements = toRefinementArray(refinement);
    if (refinements.length === 0) return null;
    return (
      <div className="mt-2 ms-4">
        <h6>{t.refinements}:</h6>
        <ListGroup as="ol" numbered>
          {refinements.map(renderConstraintItem)}
        </ListGroup>
      </div>
    );
  };

  const renderEntity = (entity: unknown, name: string) => {
    if (!entity) return <>{name}: {notSet}</>;
    if (typeof entity === 'string') return <>{name}: {entity}</>;
    const obj = entity as Record<string, unknown>;
    if (obj['@type']) {
      return (
        <>
          {name}: {obj['@type'] as string}
          {renderRefinements(obj['odrl:refinement'])}
        </>
      );
    }
    if (obj['@id']) return <>{name}: {obj['@id'] as string}</>;
    return <>{name}: {JSON.stringify(entity)}</>;
  };

  const renderConstraints = () => {
    if (!constraint) return null;

    // Logical Constraint
    if (
      typeof constraint === 'object' &&
      !Array.isArray(constraint) &&
      constraint['@type'] === 'odrl:LogicalConstraint'
    ) {
      const operator = Object.keys(constraint).find((k) => k.startsWith('odrl:'));
      const constraints = operator ? (constraint[operator] as Record<string, unknown>[]) : [];
      return (
        <>
          <div className="fw-bold mt-3">
            {t.constraints}{' '}
            <Badge bg="info">{operator?.replace('odrl:', '').toUpperCase()}</Badge>
          </div>
          <ListGroup as="ol" numbered>
            {constraints.map(renderConstraintItem)}
          </ListGroup>
        </>
      );
    }

    // Array of constraints (implicit AND)
    if (Array.isArray(constraint)) {
      return (
        <>
          <div className="fw-bold mt-3">
            {t.constraints} <Badge bg="info">AND</Badge>
          </div>
          <ListGroup as="ol" numbered>
            {constraint.map((c, i) => renderConstraintItem(c as Record<string, unknown>, i))}
          </ListGroup>
        </>
      );
    }

    // Single constraint
    return (
      <>
        <div className="fw-bold mt-3">{t.constraint}</div>
        <ListGroup as="ol">
          {renderConstraintItem(constraint as Record<string, unknown>, 0)}
        </ListGroup>
      </>
    );
  };

  const humanSummary = buildHumanSummary(action, target, assignee, notSet);

  return (
    <Card style={{ backgroundColor: 'var(--odrl-card-bg, #f8f9fa)' }}>
      <Card.Header as="h5" className="d-flex justify-content-between align-items-center">
        {t.title}
        <Button
          variant="outline-secondary"
          size="sm"
          onClick={() => setShowJson(!showJson)}
        >
          {showJson ? t.hideJson : t.showJson}
        </Button>
      </Card.Header>
      <Card.Body>
        {/* Human-readable one-line summary */}
        <div className="mb-2 text-muted fst-italic small">{humanSummary}</div>

        <Collapse in={!showJson}>
          <div>
            <Card.Text>
              <strong>{t.uid}:</strong> {policy['odrl:uid'] || notSet}
            </Card.Text>
            <hr />
            <h6>{t.permission}</h6>
            <ListGroup variant="flush">
              <ListGroup.Item>{renderEntity(target, t.target)}</ListGroup.Item>
              <ListGroup.Item>{renderEntity(assignee, t.assignee)}</ListGroup.Item>
              <ListGroup.Item>
                {t.action}: {renderAction(action, notSet)}
              </ListGroup.Item>
            </ListGroup>
            {renderConstraints()}
          </div>
        </Collapse>
        <Collapse in={showJson}>
          <div>
            <div className="d-flex justify-content-end mb-2">
              <Button
                variant="outline-secondary"
                size="sm"
                onClick={handleCopy}
                aria-label={strings.common.copyToClipboard}
              >
                {copied ? strings.common.copied : strings.common.copyToClipboard}
              </Button>
            </div>
            <pre className="bg-dark text-light p-3 rounded" style={{ fontSize: '0.85rem' }}>
              <code>{JSON.stringify(policy, null, 2)}</code>
            </pre>
          </div>
        </Collapse>
      </Card.Body>
    </Card>
  );
};

export default PolicySummary;
