/**
 * Main visual policy builder component.
 *
 * Provides a guided, step-by-step form for constructing ODRL policies.
 * Each section (Target, Assignee, Action, Constraints) is numbered and
 * includes contextual help text. Dropdown items are grouped by namespace
 * via the shared NamespacedDropdown component.
 *
 * Supports an optional **template mode**: when a {@link PolicyTemplate}
 * is provided, the form is pre-filled from the template skeleton and
 * fields listed in `lockedFields` are visually disabled.
 *
 * Replaces the former "Baukasten" component with improved UX:
 * - Namespace-grouped action dropdown
 * - Contextual help on every section
 * - Loading spinner while mappings load
 * - Error alert with retry on fetch failure
 * - Numbered step indicators
 * - Template pre-fill and field locking
 */
import { useEffect, useRef } from 'react';
import { Form, Row, Col, Card, Spinner, Alert, Button, Badge } from 'react-bootstrap';
import { useMappings } from '../hooks/useMappings';
import { useTemplateMode } from '../hooks/useTemplateMode';
import { useI18n } from '../i18n';
import type { PolicyTemplate } from '../types';
import type { OdrlPolicyJson } from '../services/api';
import ConstraintBuilder from './ConstraintBuilder';
import TargetEditor from './TargetEditor';
import AssigneeEditor from './AssigneeEditor';
import PolicySummary from './PolicySummary';
import NamespacedDropdown from './NamespacedDropdown';

/** Step number constants for the guided workflow. */
const STEP_TARGET = 1;
const STEP_ASSIGNEE = 2;
const STEP_ACTION = 3;
const STEP_CONSTRAINTS = 4;

/** JSON path constants for template field locking. */
const PATH_TARGET = 'odrl:permission.odrl:target';
const PATH_ASSIGNEE = 'odrl:permission.odrl:assignee';
const PATH_ACTION = 'odrl:permission.odrl:action';
const PATH_CONSTRAINTS = 'odrl:permission.odrl:constraint';

interface PolicyBuilderProps {
  /** The current ODRL policy JSON being built. */
  policy: OdrlPolicyJson;
  /** Callback to update the policy when the user makes changes. */
  setPolicy: (policy: OdrlPolicyJson) => void;
  /** Optional template to pre-fill and constrain the policy form. */
  template?: PolicyTemplate;
}

/**
 * Renders a numbered section header with help text.
 * Optionally shows a lock icon and template field description.
 */
const SectionHeader = ({
  step,
  title,
  helpText,
  locked,
  templateHelpText,
  lockedBadgeLabel,
}: {
  step: number;
  title: string;
  helpText: string;
  locked?: boolean;
  templateHelpText?: string;
  lockedBadgeLabel?: string;
}) => (
  <Card.Header
    className="d-flex align-items-start gap-2"
    style={{ backgroundColor: 'var(--odrl-section-header-bg, #e9ecef)' }}
  >
    <Badge
      bg="primary"
      className="rounded-circle d-flex align-items-center justify-content-center flex-shrink-0"
      style={{ width: '1.75rem', height: '1.75rem', marginTop: '0.1rem' }}
    >
      {step}
    </Badge>
    <div className="flex-grow-1">
      <div className="d-flex align-items-center gap-2">
        <strong>{title}</strong>
        {locked && (
          <Badge bg="secondary" className="d-flex align-items-center gap-1" data-testid="locked-badge">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="12"
              height="12"
              viewBox="0 0 24 24"
              fill="currentColor"
              aria-hidden="true"
            >
              <path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zM12 17c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zM9 8V6c0-1.66 1.34-3 3-3s3 1.34 3 3v2H9z" />
            </svg>
            {lockedBadgeLabel ?? 'Locked'}
          </Badge>
        )}
      </div>
      <div className="text-muted small mt-1">
        {templateHelpText || helpText}
      </div>
    </div>
  </Card.Header>
);

/**
 * Visual policy builder with guided steps, namespace-grouped dropdowns,
 * contextual help, loading/error states, and optional template support.
 */
const PolicyBuilder = ({ policy, setPolicy, template }: PolicyBuilderProps) => {
  const { mappings, loading, error, retry } = useMappings();
  const { strings } = useI18n();
  const t = strings.policyBuilder;
  const tt = strings.templateMode;
  const { isTemplateMode, isFieldLocked, getFieldMeta } = useTemplateMode(template);

  /**
   * Tracks whether the template skeleton has been applied to prevent
   * re-applying it on every render.
   */
  const skeletonAppliedRef = useRef(false);

  // Pre-fill the policy from the template skeleton on first mount
  useEffect(() => {
    if (isTemplateMode && template && !skeletonAppliedRef.current) {
      skeletonAppliedRef.current = true;
      const skeletonPolicy: OdrlPolicyJson = {
        ...template.skeleton,
        'odrl:uid': (policy as Record<string, unknown>)['odrl:uid'] as string ?? crypto.randomUUID(),
      } as OdrlPolicyJson;
      setPolicy(skeletonPolicy);
    }
  }, [isTemplateMode, template, setPolicy, policy]);

  /** Updates a single field inside `odrl:permission`. */
  const handlePermissionChange = (field: string, value: unknown) => {
    const newPolicy = { ...policy };
    if (!newPolicy['odrl:permission']) {
      newPolicy['odrl:permission'] = {};
    }
    newPolicy['odrl:permission'] = {
      ...newPolicy['odrl:permission'],
      [field]: value,
    };
    setPolicy(newPolicy);
  };

  /** Replaces the entire permission object. */
  const setPermission = (newPermission: unknown) => {
    setPolicy({ ...policy, 'odrl:permission': newPermission });
  };

  // --- Loading state ---
  if (loading) {
    return (
      <div className="d-flex align-items-center gap-2 p-4" role="status">
        <Spinner animation="border" size="sm" />
        <span>{t.loadingMappings}</span>
      </div>
    );
  }

  // --- Error state ---
  if (error || !mappings) {
    return (
      <Alert variant="danger" className="d-flex align-items-center justify-content-between">
        <span>{t.errorLoadingMappings}</span>
        <Button variant="outline-danger" size="sm" onClick={retry}>
          {strings.common.retry}
        </Button>
      </Alert>
    );
  }

  const permission = policy['odrl:permission'] || {};

  const targetLocked = isFieldLocked(PATH_TARGET);
  const assigneeLocked = isFieldLocked(PATH_ASSIGNEE);
  const actionLocked = isFieldLocked(PATH_ACTION);
  const constraintsLocked = isFieldLocked(PATH_CONSTRAINTS);

  const targetMeta = getFieldMeta(PATH_TARGET);
  const assigneeMeta = getFieldMeta(PATH_ASSIGNEE);
  const actionMeta = getFieldMeta(PATH_ACTION);
  const constraintsMeta = getFieldMeta(PATH_CONSTRAINTS);

  return (
    <Row>
      <Col lg={8}>
        <div className="d-flex flex-column gap-3">
          {/* Template banner */}
          {isTemplateMode && template && (
            <Alert variant="info" className="d-flex align-items-start gap-2 mb-0" data-testid="template-banner">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="20"
                height="20"
                viewBox="0 0 24 24"
                fill="currentColor"
                className="flex-shrink-0 mt-1"
                aria-hidden="true"
              >
                <path d="M14 2H6c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V8l-6-6zM6 20V4h7v5h5v11H6z" />
              </svg>
              <div>
                <strong>{tt.banner.replace('{name}', template.name)}</strong>
                {template.description && (
                  <div className="text-muted small mt-1">
                    {tt.bannerDescription.replace('{description}', template.description)}
                  </div>
                )}
              </div>
            </Alert>
          )}

          {/* Step 1: Target */}
          <Card style={{ boxShadow: 'var(--odrl-card-shadow)' }}>
            <SectionHeader
              step={STEP_TARGET}
              title={t.stepTarget}
              helpText={t.stepTargetHelp}
              locked={targetLocked}
              templateHelpText={targetMeta?.description}
              lockedBadgeLabel={tt.lockedBadge}
            />
            <Card.Body>
              <TargetEditor
                target={permission['odrl:target']}
                setTarget={(target) => handlePermissionChange('odrl:target', target)}
                mappings={mappings}
                locked={targetLocked}
              />
            </Card.Body>
          </Card>

          {/* Step 2: Assignee */}
          <Card style={{ boxShadow: 'var(--odrl-card-shadow)' }}>
            <SectionHeader
              step={STEP_ASSIGNEE}
              title={t.stepAssignee}
              helpText={t.stepAssigneeHelp}
              locked={assigneeLocked}
              templateHelpText={assigneeMeta?.description}
              lockedBadgeLabel={tt.lockedBadge}
            />
            <Card.Body>
              <AssigneeEditor
                assignee={permission['odrl:assignee']}
                setAssignee={(assignee) => handlePermissionChange('odrl:assignee', assignee)}
                mappings={mappings}
                locked={assigneeLocked}
              />
            </Card.Body>
          </Card>

          {/* Step 3: Action */}
          <Card style={{ boxShadow: 'var(--odrl-card-shadow)' }}>
            <SectionHeader
              step={STEP_ACTION}
              title={t.stepAction}
              helpText={t.stepActionHelp}
              locked={actionLocked}
              templateHelpText={actionMeta?.description}
              lockedBadgeLabel={tt.lockedBadge}
            />
            <Card.Body>
              <Form.Group>
                <NamespacedDropdown
                  items={mappings.actions ?? []}
                  value={permission['odrl:action'] || ''}
                  onChange={(val) => handlePermissionChange('odrl:action', val)}
                  placeholder={t.selectAction}
                  ariaLabel={t.stepAction}
                  disabled={actionLocked}
                />
                {actionLocked && (
                  <Form.Text className="text-muted" data-testid="action-locked-hint">
                    {tt.lockedFieldTooltip}
                  </Form.Text>
                )}
              </Form.Group>
            </Card.Body>
          </Card>

          {/* Step 4: Constraints */}
          <Card style={{ boxShadow: 'var(--odrl-card-shadow)' }}>
            <SectionHeader
              step={STEP_CONSTRAINTS}
              title={t.stepConstraints}
              helpText={t.stepConstraintsHelp}
              locked={constraintsLocked}
              templateHelpText={constraintsMeta?.description}
              lockedBadgeLabel={tt.lockedBadge}
            />
            <Card.Body>
              <ConstraintBuilder
                parent={permission}
                setParent={setPermission}
                mappings={mappings}
                locked={constraintsLocked}
              />
            </Card.Body>
          </Card>
        </div>
      </Col>

      <Col lg={4}>
        <div className="sticky-top" style={{ top: '1rem' }}>
          <PolicySummary policy={policy} />
        </div>
      </Col>
    </Row>
  );
};

export default PolicyBuilder;
