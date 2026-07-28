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
import { useState } from 'react';
import type { Mappings } from '../services/api';
import { useI18n } from '../i18n';
import ConstraintBuilder from './ConstraintBuilder';
import NamespacedDropdown from './NamespacedDropdown';

interface TargetEditorProps {
  /** Current target value (string URL, `{@id}` object, or AssetCollection). */
  target: unknown;
  /** Callback to update the target value. */
  setTarget: (target: unknown) => void;
  /** Available mappings for populating dropdowns. */
  mappings: Mappings;
}

/** Checks whether the target is an AssetCollection object. */
function isAssetCollection(target: unknown): boolean {
  return (
    typeof target === 'object' &&
    target !== null &&
    (target as Record<string, unknown>)['@type'] === 'AssetCollection'
  );
}

/**
 * Target editor with namespace-grouped dropdowns, help text,
 * and URL validation for custom targets.
 */
const TargetEditor = ({ target, setTarget, mappings }: TargetEditorProps) => {
  const { strings } = useI18n();
  const t = strings.targetEditor;

  const [simpleTargetType, setSimpleTargetType] = useState<'text' | 'dropdown'>('text');
  const [urlError, setUrlError] = useState('');

  const isCollection = isAssetCollection(target);

  /** Switches between simple and collection target modes. */
  const setType = (type: 'simple' | 'collection') => {
    setUrlError('');
    if (type === 'simple') {
      setTarget('');
      setSimpleTargetType('text');
    } else {
      setTarget({ '@type': 'AssetCollection', 'odrl:refinement': [] });
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
          />
          <Form.Check
            type="radio"
            id="collection-target"
            label={t.assetCollection}
            checked={isCollection}
            onChange={() => setType('collection')}
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
                    setTarget('');
                    setUrlError('');
                  }}
                />
                <Form.Check
                  type="radio"
                  id="simple-target-dropdown"
                  label={t.selectFromOptions}
                  checked={simpleTargetType === 'dropdown'}
                  onChange={() => {
                    setSimpleTargetType('dropdown');
                    setTarget({ '@id': '' });
                    setUrlError('');
                  }}
                  disabled={availableTargets.length === 0}
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
                    />
                    <Form.Control.Feedback type="invalid">
                      {urlError}
                    </Form.Control.Feedback>
                  </Form.Group>
                ) : (
                  <NamespacedDropdown
                    items={availableTargets}
                    value={(target as Record<string, string>)?.['@id'] || ''}
                    onChange={(val) => setTarget({ '@id': val })}
                    placeholder={t.selectTarget}
                    ariaLabel={t.selectTarget}
                  />
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
            />
          </Card.Body>
        </Card>
      )}
    </Stack>
  );
};

export default TargetEditor;
