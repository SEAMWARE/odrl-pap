/**
 * Template filler component.
 *
 * Renders input fields for each placeholder defined in a template,
 * displays a live natural language preview, and provides a button
 * to create a concrete ODRL policy from the filled template.
 *
 * Input field types are determined by the placeholder `type`:
 * - `string`: text input (or dropdown if `options` are defined)
 * - `number`: number input (or dropdown if `options` are defined)
 * - `boolean`: checkbox (or dropdown if `options` are defined)
 * - `xsd:date`: date picker
 *
 * Type coercion is applied when generating the final ODRL JSON:
 * - Numbers become JSON numbers
 * - Booleans become JSON booleans
 * - Dates and strings remain JSON strings
 */
import { useState, useCallback, useMemo } from 'react';
import { Form, Button, Card, Alert } from 'react-bootstrap';
import type { Template } from '../types/TemplateTypes';
import { PLACEHOLDER_REGEX } from '../types/TemplateTypes';
import NaturalLanguagePreview from './NaturalLanguagePreview';
import { useI18n } from '../i18n';

/** Props for the {@link TemplateFiller} component. */
interface TemplateFillerProps {
  /** The template to fill in. */
  template: Template;
  /** Callback invoked with the generated ODRL policy JSON when the user clicks "Create Policy". */
  onCreatePolicy: (odrl: Record<string, unknown>) => void;
  /** Whether the creation request is currently in progress. */
  isCreating?: boolean;
}

/** String representation of the boolean true value. */
const BOOLEAN_TRUE = 'true';
/** String representation of the boolean false value. */
const BOOLEAN_FALSE = 'false';

/**
 * Replaces all `{{PLACEHOLDER}}` tokens in a JSON object with concrete values.
 *
 * Performs a deep recursive walk of the object. String values containing
 * placeholder tokens are replaced. Type coercion is applied based on the
 * placeholder type definition:
 * - `number`: value is parsed as a float; if the entire string is a single
 *   placeholder, the result is a JSON number (not wrapped in a string)
 * - `boolean`: converted to a JSON boolean
 * - `string` and `xsd:date`: remain as JSON strings
 *
 * @param obj - The object to process (deep-cloned internally).
 * @param values - Mapping from placeholder key to user-entered string value.
 * @param placeholderTypes - Mapping from placeholder key to its declared type.
 * @returns A new object with all placeholders replaced by concrete values.
 */
function replacePlaceholders(
  obj: unknown,
  values: Record<string, string>,
  placeholderTypes: Record<string, string>,
): unknown {
  if (typeof obj === 'string') {
    // Check if the entire string is a single placeholder (for type coercion)
    const singleMatch = obj.match(/^\{\{([A-Z_][A-Z0-9_]*)\}\}$/);
    if (singleMatch) {
      const key = singleMatch[1];
      const value = values[key] ?? obj;
      const type = placeholderTypes[key];

      if (value === obj) return obj; // Not filled, keep placeholder

      switch (type) {
        case 'number': {
          const num = parseFloat(value);
          return isNaN(num) ? value : num;
        }
        case 'boolean':
          return value === BOOLEAN_TRUE;
        default:
          return value;
      }
    }

    // Replace embedded placeholders (always string result)
    const regex = new RegExp(PLACEHOLDER_REGEX.source, 'g');
    return obj.replace(regex, (_match: string, key: string) => {
      return values[key] ?? _match;
    });
  }

  if (Array.isArray(obj)) {
    return obj.map((item) => replacePlaceholders(item, values, placeholderTypes));
  }

  if (typeof obj === 'object' && obj !== null) {
    const result: Record<string, unknown> = {};
    for (const [k, v] of Object.entries(obj)) {
      result[k] = replacePlaceholders(v, values, placeholderTypes);
    }
    return result;
  }

  return obj;
}

/**
 * Renders a form for filling in template placeholders and creating a policy.
 *
 * @param props - Component properties.
 */
const TemplateFiller: React.FC<TemplateFillerProps> = ({
  template,
  onCreatePolicy,
  isCreating = false,
}) => {
  const { strings } = useI18n();
  const t = strings.templateFiller;

  /** Map of placeholder key to the user-entered value string. */
  const [values, setValues] = useState<Record<string, string>>({});

  /** Mapping from placeholder key to its declared type, for coercion. */
  const placeholderTypes = useMemo(() => {
    const types: Record<string, string> = {};
    for (const ph of template.placeholders) {
      types[ph.key] = ph.type;
    }
    return types;
  }, [template.placeholders]);

  /**
   * Updates a single placeholder value.
   *
   * @param key - The placeholder key.
   * @param value - The new value string.
   */
  const handleValueChange = useCallback((key: string, value: string) => {
    setValues((prev) => ({ ...prev, [key]: value }));
  }, []);

  /** Whether all required placeholders have been filled in. */
  const allFilled = useMemo(() => {
    return template.placeholders.every((ph) => {
      const val = values[ph.key];
      if (ph.type === 'boolean') return true; // Booleans always have a value (false by default)
      return val !== undefined && val !== '';
    });
  }, [template.placeholders, values]);

  /**
   * Handles the "Create Policy" button click.
   *
   * Replaces all placeholder tokens in the template ODRL with the
   * user-entered values (with type coercion) and invokes the callback.
   */
  const handleCreate = useCallback(() => {
    const odrl = replacePlaceholders(
      template.odrl,
      values,
      placeholderTypes,
    ) as Record<string, unknown>;
    onCreatePolicy(odrl);
  }, [template.odrl, values, placeholderTypes, onCreatePolicy]);

  /**
   * Renders the appropriate input field for a placeholder based on its type.
   *
   * @param ph - The placeholder definition.
   */
  const renderInputField = (ph: { key: string; name: string; description?: string; type: string; options?: string[] }) => {
    const currentValue = values[ph.key] ?? '';
    const hasOptions = ph.options && ph.options.length > 0;

    // If options are defined, always render as dropdown regardless of type
    if (hasOptions) {
      return (
        <Form.Select
          value={currentValue}
          onChange={(e) => handleValueChange(ph.key, e.target.value)}
          aria-label={ph.name}
        >
          <option value="">{t.selectPlaceholder}</option>
          {ph.options!.map((opt) => (
            <option key={opt} value={opt}>
              {opt}
            </option>
          ))}
        </Form.Select>
      );
    }

    switch (ph.type) {
      case 'boolean':
        return (
          <Form.Check
            type="switch"
            id={`ph-${ph.key}`}
            label={currentValue === BOOLEAN_TRUE ? t.boolTrue : t.boolFalse}
            checked={currentValue === BOOLEAN_TRUE}
            onChange={(e) => handleValueChange(ph.key, e.target.checked ? BOOLEAN_TRUE : BOOLEAN_FALSE)}
          />
        );

      case 'number':
        return (
          <Form.Control
            type="number"
            value={currentValue}
            onChange={(e) => handleValueChange(ph.key, e.target.value)}
            placeholder={t.numberPlaceholder}
            aria-label={ph.name}
          />
        );

      case 'xsd:date':
        return (
          <Form.Control
            type="date"
            value={currentValue}
            onChange={(e) => handleValueChange(ph.key, e.target.value)}
            aria-label={ph.name}
          />
        );

      default: // 'string'
        return (
          <Form.Control
            type="text"
            value={currentValue}
            onChange={(e) => handleValueChange(ph.key, e.target.value)}
            placeholder={t.stringPlaceholder}
            aria-label={ph.name}
          />
        );
    }
  };

  return (
    <div>
      {/* Natural language preview */}
      {template.naturalLanguage && (
        <Card className="mb-4" style={{ backgroundColor: 'var(--odrl-card-bg, #f8f9fa)' }}>
          <Card.Body>
            <Card.Title className="h6">{t.previewTitle}</Card.Title>
            <NaturalLanguagePreview
              text={template.naturalLanguage}
              values={values}
            />
          </Card.Body>
        </Card>
      )}

      {/* Placeholder input fields */}
      <h6 className="mb-3">{t.fillPlaceholders}</h6>
      {template.placeholders.length === 0 && (
        <Alert variant="info">{t.noPlaceholders}</Alert>
      )}
      {template.placeholders.map((ph) => (
        <Form.Group key={ph.key} className="mb-3" controlId={`placeholder-${ph.key}`}>
          <Form.Label className="fw-bold">
            {ph.name}
          </Form.Label>
          {renderInputField(ph)}
          {ph.description && (
            <Form.Text className="text-muted">{ph.description}</Form.Text>
          )}
        </Form.Group>
      ))}

      {/* Create Policy button */}
      <Button
        variant="primary"
        onClick={handleCreate}
        disabled={!allFilled || isCreating}
        className="mt-3"
      >
        {isCreating ? strings.common.loading : t.createPolicy}
      </Button>
    </div>
  );
};

export default TemplateFiller;
