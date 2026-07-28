/**
 * Main visual policy builder component.
 *
 * Provides a guided, step-by-step form for constructing ODRL policies.
 * Each section (Target, Assignee, Action, Constraints) is numbered and
 * includes contextual help text. Dropdown items are grouped by namespace
 * via the shared NamespacedDropdown component.
 *
 * Replaces the former "Baukasten" component with improved UX:
 * - Namespace-grouped action dropdown
 * - Contextual help on every section
 * - Loading spinner while mappings load
 * - Error alert with retry on fetch failure
 * - Numbered step indicators
 */
import { Form, Row, Col, Card, Spinner, Alert, Button, Badge } from 'react-bootstrap';
import { useMappings } from '../hooks/useMappings';
import { useI18n } from '../i18n';
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

interface PolicyBuilderProps {
  /** The current ODRL policy JSON being built. */
  policy: OdrlPolicyJson;
  /** Callback to update the policy when the user makes changes. */
  setPolicy: (policy: OdrlPolicyJson) => void;
}

/**
 * Renders a numbered section header with help text.
 */
const SectionHeader = ({
  step,
  title,
  helpText,
}: {
  step: number;
  title: string;
  helpText: string;
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
    <div>
      <strong>{title}</strong>
      <div className="text-muted small mt-1">{helpText}</div>
    </div>
  </Card.Header>
);

/**
 * Visual policy builder with guided steps, namespace-grouped dropdowns,
 * contextual help, and loading/error states.
 */
const PolicyBuilder = ({ policy, setPolicy }: PolicyBuilderProps) => {
  const { mappings, loading, error, retry } = useMappings();
  const { strings } = useI18n();
  const t = strings.policyBuilder;

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

  return (
    <Row>
      <Col lg={8}>
        <div className="d-flex flex-column gap-3">
          {/* Step 1: Target */}
          <Card style={{ boxShadow: 'var(--odrl-card-shadow)' }}>
            <SectionHeader
              step={STEP_TARGET}
              title={t.stepTarget}
              helpText={t.stepTargetHelp}
            />
            <Card.Body>
              <TargetEditor
                target={permission['odrl:target']}
                setTarget={(target) => handlePermissionChange('odrl:target', target)}
                mappings={mappings}
              />
            </Card.Body>
          </Card>

          {/* Step 2: Assignee */}
          <Card style={{ boxShadow: 'var(--odrl-card-shadow)' }}>
            <SectionHeader
              step={STEP_ASSIGNEE}
              title={t.stepAssignee}
              helpText={t.stepAssigneeHelp}
            />
            <Card.Body>
              <AssigneeEditor
                assignee={permission['odrl:assignee']}
                setAssignee={(assignee) => handlePermissionChange('odrl:assignee', assignee)}
                mappings={mappings}
              />
            </Card.Body>
          </Card>

          {/* Step 3: Action */}
          <Card style={{ boxShadow: 'var(--odrl-card-shadow)' }}>
            <SectionHeader
              step={STEP_ACTION}
              title={t.stepAction}
              helpText={t.stepActionHelp}
            />
            <Card.Body>
              <Form.Group>
                <NamespacedDropdown
                  items={mappings.actions ?? []}
                  value={permission['odrl:action'] || ''}
                  onChange={(val) => handlePermissionChange('odrl:action', val)}
                  placeholder={t.selectAction}
                  ariaLabel={t.stepAction}
                />
              </Form.Group>
            </Card.Body>
          </Card>

          {/* Step 4: Constraints */}
          <Card style={{ boxShadow: 'var(--odrl-card-shadow)' }}>
            <SectionHeader
              step={STEP_CONSTRAINTS}
              title={t.stepConstraints}
              helpText={t.stepConstraintsHelp}
            />
            <Card.Body>
              <ConstraintBuilder
                parent={permission}
                setParent={setPermission}
                mappings={mappings}
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
