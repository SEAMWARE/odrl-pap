/**
 * Placeholder editor component.
 *
 * Provides an editable list of template placeholder definitions with
 * controls to add, remove, and configure each placeholder's key,
 * display name, description, data type, and optional dropdown values.
 */
import { useState, useEffect } from 'react';
import { Form, Button, Row, Col, Card, CloseButton } from 'react-bootstrap';
import type { TemplatePlaceholder } from '../api/models/TemplatePlaceholder';
import { PLACEHOLDER_TYPES, createEmptyPlaceholder } from '../types/TemplateTypes';
import { useI18n } from '../i18n';

/** Separator used between dropdown option values in the options input. */
const OPTIONS_SEPARATOR = ',';

/**
 * Parses a separator-delimited string into a trimmed, non-empty options array.
 *
 * @param text - The raw options text (e.g. "ADMIN, DATA_ANALYST").
 * @returns The parsed list of option values.
 */
function parseOptions(text: string): string[] {
  return text
    .split(OPTIONS_SEPARATOR)
    .map((s) => s.trim())
    .filter((s) => s.length > 0);
}

/** Props for the {@link OptionsInput} component. */
interface OptionsInputProps {
  /** The committed option values for this placeholder. */
  options: string[];
  /** Index of the owning placeholder, used for the accessible label. */
  index: number;
  /** Called with the parsed options when editing is committed (on blur). */
  onCommit: (options: string[]) => void;
}

/**
 * Free-text input for a placeholder's dropdown options.
 *
 * Keeps the raw text in local state while the user types — so intermediate
 * states like a trailing comma are preserved — and only parses/commits the
 * value on blur. Parsing on every keystroke would drop the just-typed
 * separator and prevent entering more than one option by hand.
 *
 * @param props - Component properties.
 */
const OptionsInput: React.FC<OptionsInputProps> = ({ options, index, onCommit }) => {
  const { strings } = useI18n();
  const t = strings.placeholderEditor;
  const committed = options.join(`${OPTIONS_SEPARATOR} `);
  const [text, setText] = useState(committed);

  // Re-sync when the committed value changes by content (e.g. after blur
  // normalisation), without clobbering in-progress typing.
  useEffect(() => {
    setText(committed);
  }, [committed]);

  return (
    <Form.Control
      size="sm"
      type="text"
      value={text}
      onChange={(e) => setText(e.target.value)}
      onBlur={() => onCommit(parseOptions(text))}
      placeholder={t.optionsPlaceholder}
      aria-label={`${t.optionsLabel} ${index + 1}`}
    />
  );
};

/** Props for the {@link PlaceholderEditor} component. */
interface PlaceholderEditorProps {
  /** Current list of placeholder definitions. */
  placeholders: TemplatePlaceholder[];
  /** Callback to update the entire placeholders array. */
  onChange: (placeholders: TemplatePlaceholder[]) => void;
  /** Set of placeholder keys auto-detected from the ODRL skeleton / description. */
  detectedKeys?: Set<string>;
}

/**
 * Editable list of template placeholder definitions.
 *
 * Each placeholder row provides inputs for key, display name, description,
 * type selection, and optional comma-separated dropdown values. Keys that are
 * referenced by a `{{TOKEN}}` are flagged "auto-detected"; keys that are not
 * are flagged "unused" so the author can spot orphaned definitions without the
 * editor silently deleting them.
 *
 * @param props - Component properties.
 */
const PlaceholderEditor: React.FC<PlaceholderEditorProps> = ({
  placeholders,
  onChange,
  detectedKeys = new Set(),
}) => {
  const { strings } = useI18n();
  const t = strings.placeholderEditor;

  /**
   * Updates a single field of a placeholder at the given index.
   *
   * @param index - Index into the placeholders array.
   * @param field - The field name to update.
   * @param value - The new value.
   */
  const handleFieldChange = (
    index: number,
    field: keyof TemplatePlaceholder,
    value: string | string[],
  ) => {
    const updated = [...placeholders];
    if (field === 'options') {
      updated[index] = { ...updated[index], options: value as string[] };
    } else if (field === 'type') {
      updated[index] = {
        ...updated[index],
        type: value as TemplatePlaceholder['type'],
      };
    } else {
      updated[index] = { ...updated[index], [field]: value };
    }
    onChange(updated);
  };

  /** Adds a new empty placeholder to the list. */
  const handleAdd = () => {
    const newKey = `PLACEHOLDER_${placeholders.length + 1}`;
    onChange([...placeholders, createEmptyPlaceholder(newKey)]);
  };

  /**
   * Removes a placeholder at the given index.
   *
   * @param index - Index of the placeholder to remove.
   */
  const handleRemove = (index: number) => {
    onChange(placeholders.filter((_, i) => i !== index));
  };

  return (
    <div>
      {placeholders.map((placeholder, index) => {
        const isDetected = detectedKeys.has(placeholder.key);
        const isUnused = placeholder.key.length > 0 && !isDetected;
        return (
          // Keyed by index (not `placeholder.key`) so editing the Key field does
          // not unmount/remount the card and steal focus after each keystroke.
          <Card key={index} className="mb-2">
            <Card.Body className="py-2 px-3">
              <div className="d-flex justify-content-between align-items-start mb-2">
                <strong className="text-muted small">
                  {t.cardLabel} #{index + 1}
                  {isDetected && (
                    <span className="badge bg-success ms-2" title={t.autoDetectedTooltip}>
                      {t.autoDetectedBadge}
                    </span>
                  )}
                  {isUnused && (
                    <span className="badge bg-warning text-dark ms-2" title={t.unusedTooltip}>
                      {t.unusedBadge}
                    </span>
                  )}
                </strong>
                <CloseButton
                  onClick={() => handleRemove(index)}
                  aria-label={`${t.removePlaceholder} ${placeholder.key}`}
                />
              </div>

              <Row className="g-2 mb-2">
                <Col md={4}>
                  <Form.Group controlId={`ph-key-${index}`}>
                    <Form.Label className="small mb-0">{t.keyLabel}</Form.Label>
                    <Form.Control
                      size="sm"
                      type="text"
                      value={placeholder.key}
                      onChange={(e) =>
                        handleFieldChange(index, 'key', e.target.value.toUpperCase().replace(/[^A-Z0-9_]/g, ''))
                      }
                      placeholder={t.keyPlaceholder}
                      aria-label={`${t.keyLabel} ${index + 1}`}
                    />
                  </Form.Group>
                </Col>
                <Col md={4}>
                  <Form.Group controlId={`ph-name-${index}`}>
                    <Form.Label className="small mb-0">{t.displayNameLabel}</Form.Label>
                    <Form.Control
                      size="sm"
                      type="text"
                      value={placeholder.name}
                      onChange={(e) => handleFieldChange(index, 'name', e.target.value)}
                      placeholder={t.displayNamePlaceholder}
                      aria-label={`${t.displayNameLabel} ${index + 1}`}
                    />
                  </Form.Group>
                </Col>
                <Col md={4}>
                  <Form.Group controlId={`ph-type-${index}`}>
                    <Form.Label className="small mb-0">{t.typeLabel}</Form.Label>
                    <Form.Select
                      size="sm"
                      value={placeholder.type}
                      onChange={(e) => handleFieldChange(index, 'type', e.target.value)}
                      aria-label={`${t.typeLabel} ${index + 1}`}
                    >
                      {PLACEHOLDER_TYPES.map((type) => (
                        <option key={type} value={type}>
                          {type}
                        </option>
                      ))}
                    </Form.Select>
                  </Form.Group>
                </Col>
              </Row>

              <Row className="g-2">
                <Col md={6}>
                  <Form.Group controlId={`ph-desc-${index}`}>
                    <Form.Label className="small mb-0">{t.descriptionLabel}</Form.Label>
                    <Form.Control
                      size="sm"
                      type="text"
                      value={placeholder.description ?? ''}
                      onChange={(e) => handleFieldChange(index, 'description', e.target.value)}
                      placeholder={t.descriptionPlaceholder}
                      aria-label={`${t.descriptionLabel} ${index + 1}`}
                    />
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group controlId={`ph-options-${index}`}>
                    <Form.Label className="small mb-0">{t.optionsLabel}</Form.Label>
                    <OptionsInput
                      options={placeholder.options ?? []}
                      index={index}
                      onCommit={(opts) => handleFieldChange(index, 'options', opts)}
                    />
                    <Form.Text className="text-muted" style={{ fontSize: '0.75rem' }}>
                      {t.optionsHelp}
                    </Form.Text>
                  </Form.Group>
                </Col>
              </Row>
            </Card.Body>
          </Card>
        );
      })}

      <Button variant="outline-primary" size="sm" onClick={handleAdd}>
        {t.addPlaceholder}
      </Button>
    </div>
  );
};

export default PlaceholderEditor;
