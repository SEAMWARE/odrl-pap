/**
 * Placeholder editor component.
 *
 * Provides an editable list of template placeholder definitions with
 * controls to add, remove, and configure each placeholder's key,
 * display name, description, data type, and optional dropdown values.
 */
import { Form, Button, Row, Col, Card, CloseButton } from 'react-bootstrap';
import type { TemplatePlaceholder } from '../api/models/TemplatePlaceholder';
import { PLACEHOLDER_TYPES, createEmptyPlaceholder } from '../types/TemplateTypes';

/** Props for the {@link PlaceholderEditor} component. */
interface PlaceholderEditorProps {
  /** Current list of placeholder definitions. */
  placeholders: TemplatePlaceholder[];
  /** Callback to update the entire placeholders array. */
  onChange: (placeholders: TemplatePlaceholder[]) => void;
  /** Set of placeholder keys auto-detected from the ODRL skeleton. */
  detectedKeys?: Set<string>;
}

/**
 * Editable list of template placeholder definitions.
 *
 * Each placeholder row provides inputs for key, display name,
 * description, type selection, and optional comma-separated
 * dropdown values. Auto-detected keys from the ODRL skeleton
 * are shown with a visual indicator.
 *
 * @param props - Component properties.
 */
const PlaceholderEditor: React.FC<PlaceholderEditorProps> = ({
  placeholders,
  onChange,
  detectedKeys = new Set(),
}) => {
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

  /**
   * Parses a comma-separated string into an options array.
   *
   * @param index - Index into the placeholders array.
   * @param text - Comma-separated text input.
   */
  const handleOptionsChange = (index: number, text: string) => {
    const options = text
      .split(',')
      .map((s) => s.trim())
      .filter((s) => s.length > 0);
    handleFieldChange(index, 'options', options);
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
      {placeholders.map((placeholder, index) => (
        <Card key={placeholder.key} className="mb-2">
          <Card.Body className="py-2 px-3">
            <div className="d-flex justify-content-between align-items-start mb-2">
              <strong className="text-muted small">
                Placeholder #{index + 1}
                {detectedKeys.has(placeholder.key) && (
                  <span className="badge bg-success ms-2" title="Auto-detected from ODRL skeleton">
                    auto-detected
                  </span>
                )}
              </strong>
              <CloseButton
                onClick={() => handleRemove(index)}
                aria-label={`Remove placeholder ${placeholder.key}`}
              />
            </div>

            <Row className="g-2 mb-2">
              <Col md={4}>
                <Form.Group controlId={`ph-key-${index}`}>
                  <Form.Label className="small mb-0">Key</Form.Label>
                  <Form.Control
                    size="sm"
                    type="text"
                    value={placeholder.key}
                    onChange={(e) =>
                      handleFieldChange(index, 'key', e.target.value.toUpperCase().replace(/[^A-Z0-9_]/g, ''))
                    }
                    placeholder="e.g., RESOURCE_ID"
                    aria-label={`Placeholder key ${index + 1}`}
                  />
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group controlId={`ph-name-${index}`}>
                  <Form.Label className="small mb-0">Display Name</Form.Label>
                  <Form.Control
                    size="sm"
                    type="text"
                    value={placeholder.name}
                    onChange={(e) => handleFieldChange(index, 'name', e.target.value)}
                    placeholder="e.g., Resource ID"
                    aria-label={`Placeholder name ${index + 1}`}
                  />
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group controlId={`ph-type-${index}`}>
                  <Form.Label className="small mb-0">Type</Form.Label>
                  <Form.Select
                    size="sm"
                    value={placeholder.type}
                    onChange={(e) => handleFieldChange(index, 'type', e.target.value)}
                    aria-label={`Placeholder type ${index + 1}`}
                  >
                    {PLACEHOLDER_TYPES.map((t) => (
                      <option key={t} value={t}>
                        {t}
                      </option>
                    ))}
                  </Form.Select>
                </Form.Group>
              </Col>
            </Row>

            <Row className="g-2">
              <Col md={6}>
                <Form.Group controlId={`ph-desc-${index}`}>
                  <Form.Label className="small mb-0">Description</Form.Label>
                  <Form.Control
                    size="sm"
                    type="text"
                    value={placeholder.description ?? ''}
                    onChange={(e) => handleFieldChange(index, 'description', e.target.value)}
                    placeholder="Help text for the user"
                    aria-label={`Placeholder description ${index + 1}`}
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group controlId={`ph-options-${index}`}>
                  <Form.Label className="small mb-0">Options (comma-separated, optional)</Form.Label>
                  <Form.Control
                    size="sm"
                    type="text"
                    value={(placeholder.options ?? []).join(', ')}
                    onChange={(e) => handleOptionsChange(index, e.target.value)}
                    placeholder="e.g., value1, value2, value3"
                    aria-label={`Placeholder options ${index + 1}`}
                  />
                  <Form.Text className="text-muted" style={{ fontSize: '0.75rem' }}>
                    If provided, renders as a dropdown. Leave empty for free-form input.
                  </Form.Text>
                </Form.Group>
              </Col>
            </Row>
          </Card.Body>
        </Card>
      ))}

      <Button variant="outline-primary" size="sm" onClick={handleAdd}>
        + Add Placeholder
      </Button>
    </div>
  );
};

export default PlaceholderEditor;
