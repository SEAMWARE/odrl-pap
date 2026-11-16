/**
 * Target editor component for ODRL policies.
 *
 * Supports two modes:
 * - **Simple Target:** A custom URL or a selection from available target mappings
 *   (via namespace-grouped dropdown).
 * - **Asset Collection:** A typed collection with refinement constraints.
 *
 * Includes contextual help text, input validation for URLs, and
 * improved visual separation for the AssetCollection mode.
 */
import { Form, Row, Col, Stack, Card } from 'react-bootstrap';
import { useEffect, useState } from 'react';
import type { Mappings } from '../services/api';
import { useI18n } from '../i18n';
import ConstraintBuilder from './ConstraintBuilder';
import NamespacedDropdown from './NamespacedDropdown';
import { isCollectionOfType, newAssetCollection } from './collectionType';
import { isPlainTargetKey, PLAIN_TARGET } from '../utils/target';

interface TargetEditorProps {
  /** Current target value (string URL/identifier or AssetCollection object). */
  target: unknown;
  /**
   * Key the target is stored under (`odrl:target`, `json:uid`, ...). It selects the rego rule, so
   * the "select from available targets" mode shows and changes it.
   */
  targetKey?: string;
  /**
   * Callback to update the target. `key` is the mapping name the target is to be written under
   * (e.g. `json:uid`); when omitted, the key already in use stays.
   */
  setTarget: (target: unknown, key?: string) => void;
  /** Available mappings for populating dropdowns. */
  mappings: Mappings;
  /** When `true`, all controls are disabled (set by template mode). */
  locked?: boolean;
}

/** Checks whether the target is an AssetCollection object (`AssetCollection` or `odrl:AssetCollection`). */
function isAssetCollection(target: unknown): boolean {
  return isCollectionOfType(target, 'AssetCollection');
}

/**
 * Target editor with namespace-grouped dropdowns, help text,
 * and URL validation for custom targets.
 */
const TargetEditor = ({ target, targetKey, setTarget, mappings, locked = false }: TargetEditorProps) => {
  const { strings } = useI18n();
  const t = strings.targetEditor;

  /** A target stored under a mapping key other than the plain `odrl:target` is one of the options. */
  const optionKey = targetKey && !isPlainTargetKey(targetKey) ? targetKey : '';
  const [simpleTargetType, setSimpleTargetType] = useState<'text' | 'dropdown'>(optionKey ? 'dropdown' : 'text');
  const [urlError, setUrlError] = useState('');
  /** Tracks the dropdown selection separately from the target value. */
  const [dropdownSelection, setDropdownSelection] = useState(optionKey);

  // The policy can be replaced after mount (loaded from the backend, edited in the Raw tab), so
  // the mode and the selection follow the key the target is stored under.
  useEffect(() => {
    // A plain `odrl:target` is itself a dropdown option, so a selection of it is kept rather than
    // cleared when the stored key becomes `odrl:target`.
    setDropdownSelection((current) => optionKey || (isPlainTargetKey(current) ? current : ''));
    // A mapping key shows the dropdown. When the key stops being one (the policy was replaced) and
    // the selection was a mapping key, the target is a plain one now and shows as text.
    if (optionKey) setSimpleTargetType('dropdown');
    else if (dropdownSelection && !isPlainTargetKey(dropdownSelection)) setSimpleTargetType('text');
    // eslint-disable-next-line react-hooks/exhaustive-deps -- the effect follows the stored key only
  }, [optionKey]);

  const isCollection = isAssetCollection(target);

  /** Switches between simple and collection target modes. */
  const setType = (type: 'simple' | 'collection') => {
    setUrlError('');
    if (type === 'simple') {
      setTarget('', PLAIN_TARGET);
      setSimpleTargetType('text');
    } else {
      setTarget(newAssetCollection(), PLAIN_TARGET);
    }
  };

  const availableTargets = mappings.targets ?? [];

  /** Validates and sets a custom target URL. */
  const handleUrlChange = (value: string) => {
    setTarget(value);
    if (value && !value.match(/^https?:\/\/.+/i) && value.length > 0) {
      setUrlError(t.invalidUrl);
    } else {
      setUrlError('');
    }
  };

  return (
    <Stack gap={3}>
      <Row>
        <Col>
          <Form.Check
            type="radio"
            id="simple-target"
            label={t.simpleTarget}
            checked={!isCollection}
            onChange={() => setType('simple')}
            disabled={locked}
          />
          <Form.Check
            type="radio"
            id="collection-target"
            label={t.assetCollection}
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
                  id="simple-target-text"
                  label={t.customTarget}
                  checked={simpleTargetType === 'text'}
                  onChange={() => {
                    setSimpleTargetType('text');
                    setTarget('', PLAIN_TARGET);
                    setUrlError('');
                  }}
                  disabled={locked}
                />
                <Form.Check
                  type="radio"
                  id="simple-target-dropdown"
                  label={t.selectFromOptions}
                  checked={simpleTargetType === 'dropdown'}
                  onChange={() => {
                    setSimpleTargetType('dropdown');
                    setDropdownSelection('');
                    setTarget('', PLAIN_TARGET);
                    setUrlError('');
                  }}
                  disabled={locked || availableTargets.length === 0}
                />
              </Col>
            </Row>
            <Row>
              <Col>
                {simpleTargetType === 'text' ? (
                  <Form.Group>
                    <Form.Control
                      type="text"
                      placeholder={t.enterTargetUrl}
                      value={(target as string) || ''}
                      onChange={(e) => handleUrlChange(e.target.value)}
                      isInvalid={!!urlError}
                      aria-label={t.customTarget}
                      disabled={locked}
                    />
                    <Form.Control.Feedback type="invalid">
                      {urlError}
                    </Form.Control.Feedback>
                  </Form.Group>
                ) : (
                  <Stack gap={2}>
                    <NamespacedDropdown
                      items={availableTargets}
                      value={dropdownSelection}
                      onChange={(val) => {
                        // The option is the key the target is written under; the value the user
                        // already typed moves with it.
                        setDropdownSelection(val);
                        setTarget(typeof target === 'string' ? target : '', val || PLAIN_TARGET);
                      }}
                      placeholder={t.selectTarget}
                      ariaLabel={t.selectTarget}
                      disabled={locked}
                    />
                    <Form.Control
                      type="text"
                      placeholder={t.enterTargetValue}
                      value={typeof target === 'string' ? target : ''}
                      onChange={(e) => setTarget(e.target.value, dropdownSelection || undefined)}
                      aria-label={t.enterTargetValue}
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
            <strong>{t.assetCollection}</strong>
          </Card.Header>
          <Card.Body>
            <h6>{t.refinementsTitle}</h6>
            <div className="text-muted small mb-3">{t.refinementsHelp}</div>
            <ConstraintBuilder
              parent={target as Record<string, unknown>}
              setParent={setTarget as (v: Record<string, unknown>) => void}
              mappings={mappings}
              propertyKey="odrl:refinement"
            />
          </Card.Body>
        </Card>
      )}
    </Stack>
  );
};

export default TargetEditor;
