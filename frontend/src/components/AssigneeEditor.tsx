/**
 * Assignee editor component for ODRL policies.
 *
 * Supports two modes:
 * - **Simple Assignee:** A custom identifier or a selection from
 *   available assignee mappings (via namespace-grouped dropdown).
 * - **Party Collection:** A typed collection with refinement constraints.
 *
 * Includes contextual help text and improved visual separation for
 * the PartyCollection mode.
 */
import { Form, Row, Col, Stack, Card } from 'react-bootstrap';
import { useState } from 'react';
import type { Mappings } from '../services/api';
import { useI18n } from '../i18n';
import ConstraintBuilder from './ConstraintBuilder';
import NamespacedDropdown from './NamespacedDropdown';

interface AssigneeEditorProps {
  /** Current assignee value (string identifier or PartyCollection object). */
  assignee: unknown;
  /** Callback to update the assignee value. */
  setAssignee: (assignee: unknown) => void;
  /** Available mappings for populating dropdowns. */
  mappings: Mappings;
  /** When `true`, all controls are disabled (set by template mode). */
  locked?: boolean;
}

/** Checks whether the assignee is a PartyCollection object. */
function isPartyCollection(assignee: unknown): boolean {
  return (
    typeof assignee === 'object' &&
    assignee !== null &&
    (assignee as Record<string, unknown>)['@type'] === 'PartyCollection'
  );
}

/**
 * Assignee editor with namespace-grouped dropdowns, help text,
 * and improved Party Collection UX.
 */
const AssigneeEditor = ({ assignee, setAssignee, mappings, locked = false }: AssigneeEditorProps) => {
  const { strings } = useI18n();
  const t = strings.assigneeEditor;

  const [simpleAssigneeType, setSimpleAssigneeType] = useState<'text' | 'dropdown'>('text');
  /** Tracks the dropdown selection separately from the assignee value. */
  const [dropdownSelection, setDropdownSelection] = useState('');

  const isCollection = isPartyCollection(assignee);

  /** Switches between simple and collection assignee modes. */
  const setType = (type: 'simple' | 'collection') => {
    if (type === 'simple') {
      setAssignee('');
      setSimpleAssigneeType('text');
    } else {
      setAssignee({ '@type': 'PartyCollection', 'odrl:refinement': [] });
    }
  };

  const availableAssignees = mappings.assignees ?? [];

  return (
    <Stack gap={3}>
      <Row>
        <Col>
          <Form.Check
            type="radio"
            id="simple-assignee"
            label={t.simpleAssignee}
            checked={!isCollection}
            onChange={() => setType('simple')}
            disabled={locked}
          />
          <Form.Check
            type="radio"
            id="collection-assignee"
            label={t.partyCollection}
            checked={isCollection}
            onChange={() => setType('collection')}
            disabled={locked}
          />
        </Col>
      </Row>

      {!isCollection ? (
        <Card body style={{ backgroundColor: 'var(--odrl-card-bg, #f8f9fa)' }}>
          <Stack gap={2}>
            <Row>
              <Col>
                <Form.Check
                  type="radio"
                  id="simple-assignee-text"
                  label={t.customAssignee}
                  checked={simpleAssigneeType === 'text'}
                  onChange={() => {
                    setSimpleAssigneeType('text');
                    setAssignee('');
                  }}
                  disabled={locked}
                />
                <Form.Check
                  type="radio"
                  id="simple-assignee-dropdown"
                  label={t.selectFromOptions}
                  checked={simpleAssigneeType === 'dropdown'}
                  onChange={() => {
                    setSimpleAssigneeType('dropdown');
                    setDropdownSelection('');
                    setAssignee('');
                  }}
                  disabled={locked || availableAssignees.length === 0}
                />
              </Col>
            </Row>
            <Row>
              <Col>
                {simpleAssigneeType === 'text' ? (
                  <Form.Control
                    type="text"
                    placeholder={t.enterAssigneeId}
                    value={(assignee as string) || ''}
                    onChange={(e) => setAssignee(e.target.value)}
                    aria-label={t.customAssignee}
                    disabled={locked}
                  />
                ) : (
                  <Stack gap={2}>
                    <NamespacedDropdown
                      items={availableAssignees}
                      value={dropdownSelection}
                      onChange={(val) => {
                        setDropdownSelection(val);
                        setAssignee(val);
                      }}
                      placeholder={t.selectAssignee}
                      ariaLabel={t.selectAssignee}
                      disabled={locked}
                    />
                    <Form.Control
                      type="text"
                      placeholder={t.enterAssigneeValue}
                      value={typeof assignee === 'string' ? assignee : ''}
                      onChange={(e) => setAssignee(e.target.value)}
                      aria-label={t.enterAssigneeValue}
                      disabled={locked}
                    />
                  </Stack>
                )}
              </Col>
            </Row>
          </Stack>
        </Card>
      ) : (
        <Card style={{ backgroundColor: 'var(--odrl-card-bg, #f8f9fa)' }}>
          <Card.Header>
            <strong>{t.partyCollection}</strong>
          </Card.Header>
          <Card.Body>
            <h6>{t.refinementsTitle}</h6>
            <div className="text-muted small mb-3">{t.refinementsHelp}</div>
            <ConstraintBuilder
              parent={assignee as Record<string, unknown>}
              setParent={setAssignee as (v: Record<string, unknown>) => void}
              mappings={mappings}
              propertyKey="odrl:refinement"
            />
          </Card.Body>
        </Card>
      )}
    </Stack>
  );
};

export default AssigneeEditor;
