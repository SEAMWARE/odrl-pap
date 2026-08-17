/**
 * Shared, router-free policy template form.
 *
 * Renders the metadata, ODRL skeleton, placeholder, and natural-language
 * sections used to create or edit a policy template, along with the
 * placeholder auto-detection logic and save handling.
 *
 * This component is deliberately free of any routing dependency so it can
 * be reused both by the standalone SPA (`TemplateEditor` page) and by the
 * embedded Web Component (`EmbeddedTemplateManager`). Loading of an existing
 * template and any surrounding chrome (page titles, service selectors,
 * navigation) are the responsibility of the parent.
 */
import { useState, useEffect, useCallback, useRef, useMemo } from 'react';
import { Form, Button, Alert, Card, Row, Col } from 'react-bootstrap';
import { TemplateService } from '../api/services/TemplateService';
import { ServiceService } from '../api/services/ServiceService';
import type { Template } from '../api/models/Template';
import type { TemplatePlaceholder } from '../api/models/TemplatePlaceholder';
import { TemplatePlaceholder as TemplatePlaceholderEnum } from '../api/models/TemplatePlaceholder';
import type { TemplateCreate } from '../api/models/TemplateCreate';
import type { OdrlPolicyJson } from '../api/models/OdrlPolicyJson';
import PlaceholderEditor from './PlaceholderEditor';
import NaturalLanguagePreview from './NaturalLanguagePreview';
import { extractPlaceholderKeys, createEmptyPlaceholder } from '../types/TemplateTypes';
import { useI18n } from '../i18n';

/** Default number of rows for the ODRL JSON textarea. */
const ODRL_TEXTAREA_ROWS = 15;

/** Default number of rows for the natural language textarea. */
const NATURAL_LANGUAGE_TEXTAREA_ROWS = 3;

/** Debounce delay (ms) for placeholder extraction from text changes. */
const PLACEHOLDER_SYNC_DEBOUNCE_MS = 500;

/** Minimal ODRL skeleton used to seed a brand-new template. */
const DEFAULT_ODRL_SKELETON: Record<string, unknown> = {
  '@context': { odrl: 'http://www.w3.org/ns/odrl/2/' },
  '@type': 'odrl:Policy',
  'odrl:permission': {},
};

/** Props for the {@link TemplateForm} component. */
export interface TemplateFormProps {
  /**
   * Template ID being edited. When `null`/`undefined`, the form creates a
   * new template; otherwise it updates the template with this ID.
   */
  templateId?: string | null;
  /**
   * Service scope for the save operation. When set, the template is stored
   * under the service (`POST/PUT /service/{serviceId}/template`); otherwise
   * it is stored as a general template.
   */
  serviceId?: string | null;
  /**
   * Existing template data to pre-fill the form with when editing.
   *
   * The parent is responsible for loading it (this component does not fetch).
   * Because the initial form state is derived from this prop only once, the
   * parent should re-mount the form (e.g. via a `key`) when switching between
   * different templates.
   */
  initialTemplate?: Template | null;
  /**
   * Called after a successful save.
   *
   * @param template - The created or updated template as returned by the API.
   * @param isUpdate - `true` when an existing template was updated.
   */
  onSaved: (template: Template, isUpdate: boolean) => void;
  /** Called when the user cancels editing. */
  onCancel: () => void;
}

/**
 * Serializes an ODRL skeleton (object or string) to pretty-printed JSON text.
 *
 * @param odrl - The ODRL skeleton, either a parsed object or a JSON string.
 * @returns Formatted JSON text suitable for the editor textarea.
 */
function odrlToText(odrl: Template['odrl'] | string | undefined): string {
  if (odrl === undefined) {
    return JSON.stringify(DEFAULT_ODRL_SKELETON, null, 2);
  }
  if (typeof odrl === 'string') {
    return odrl;
  }
  return JSON.stringify(odrl, null, 2);
}

/**
 * Reusable create/edit form for policy templates.
 *
 * Auto-detects `{{PLACEHOLDER}}` keys from the ODRL skeleton and the natural
 * language description, keeping the placeholder definition list in sync, and
 * persists via the general or service-scoped template API depending on
 * {@link TemplateFormProps.serviceId}.
 *
 * @param props - Component properties.
 */
const TemplateForm: React.FC<TemplateFormProps> = ({
  templateId,
  serviceId,
  initialTemplate,
  onSaved,
  onCancel,
}) => {
  const { strings } = useI18n();
  const t = strings.templateEditor;

  // --- Metadata state ---
  const [name, setName] = useState(initialTemplate?.name ?? '');
  const [description, setDescription] = useState(initialTemplate?.description ?? '');

  // --- ODRL skeleton state ---
  const [odrlText, setOdrlText] = useState(() => odrlToText(initialTemplate?.odrl));
  const [odrlError, setOdrlError] = useState('');
  /** Parsed ODRL JSON (null when the text is invalid). */
  const parsedOdrlRef = useRef<OdrlPolicyJson | null>(null);

  // Seed the parsed-ODRL ref from the initial text.
  if (parsedOdrlRef.current === null && !odrlError) {
    try {
      parsedOdrlRef.current = JSON.parse(odrlText) as OdrlPolicyJson;
    } catch {
      parsedOdrlRef.current = null;
    }
  }

  // --- Placeholders state ---
  const [placeholders, setPlaceholders] = useState<TemplatePlaceholder[]>(
    initialTemplate?.placeholders ?? [],
  );
  // --- Natural language state (declared early so detectedKeys can read it) ---
  const [naturalLanguage, setNaturalLanguage] = useState(initialTemplate?.naturalLanguage ?? '');

  /**
   * Keys referenced by a `{{TOKEN}}` in the ODRL skeleton or natural language.
   *
   * Derived synchronously (not via the debounced effect) so the editor's
   * "auto-detected" / "unused" badges are always accurate on the current text —
   * including immediately on load — while the debounce below governs only the
   * *creation* of new placeholder entries.
   */
  const detectedKeys = useMemo(
    () => new Set([...extractPlaceholderKeys(odrlText), ...extractPlaceholderKeys(naturalLanguage)]),
    [odrlText, naturalLanguage],
  );

  // --- Form state ---
  const [saving, setSaving] = useState(false);
  const [saveError, setSaveError] = useState('');

  /**
   * Handles changes in the ODRL JSON textarea.
   *
   * Validates JSON syntax. Placeholder synchronization (adding new keys,
   * removing stale ones) is handled by the debounced effect below.
   *
   * @param text - The raw ODRL JSON text.
   */
  const handleOdrlChange = useCallback(
    (text: string) => {
      setOdrlText(text);
      try {
        parsedOdrlRef.current = JSON.parse(text) as OdrlPolicyJson;
        setOdrlError('');
      } catch (err: unknown) {
        parsedOdrlRef.current = null;
        setOdrlError(err instanceof Error ? err.message : t.jsonInvalid);
      }
    },
    [t.jsonInvalid],
  );

  // Create a placeholder entry for each newly detected key.
  //
  // Debounced so that the intermediate keystrokes of typing a `{{PLACEHOLDER}}`
  // token do not spawn entries — only the completed token, once, after the user
  // pauses. It ONLY adds; it never removes. Removing on each change would delete
  // a placeholder (with its name, type and options) the instant its key stops
  // matching a token — e.g. one added via "+ Add Placeholder", or mid-rename.
  // Keys not currently detected are instead flagged "unused" in the editor,
  // leaving the user in control of deletion.
  useEffect(() => {
    const timer = setTimeout(() => {
      setPlaceholders((prev) => {
        const existingKeys = new Set(prev.map((p) => p.key));
        const newKeys = [...detectedKeys].filter((k) => !existingKeys.has(k));
        if (newKeys.length === 0) return prev;
        return [...prev, ...newKeys.map((k) => createEmptyPlaceholder(k))];
      });
    }, PLACEHOLDER_SYNC_DEBOUNCE_MS);

    return () => clearTimeout(timer);
  }, [detectedKeys]);

  /**
   * Validates and saves the template (create or update), then notifies the
   * parent through {@link TemplateFormProps.onSaved}.
   */
  const handleSave = useCallback(() => {
    if (!name.trim()) {
      setSaveError(t.nameRequired);
      return;
    }

    if (parsedOdrlRef.current === null) {
      setSaveError(t.jsonInvalid);
      return;
    }

    setSaving(true);
    setSaveError('');

    const requestBody: TemplateCreate = {
      name: name.trim(),
      description: description.trim() || undefined,
      odrl: parsedOdrlRef.current,
      naturalLanguage: naturalLanguage.trim() || undefined,
      placeholders: placeholders.map((p) => ({
        ...p,
        type: p.type || TemplatePlaceholderEnum.type.STRING,
        options: p.options && p.options.length > 0 ? p.options : undefined,
      })),
    };

    const isUpdate = !!templateId;
    let promise;
    if (serviceId) {
      promise =
        isUpdate && templateId
          ? ServiceService.updateServiceTemplate(serviceId, templateId, requestBody)
          : ServiceService.createServiceTemplate(serviceId, requestBody);
    } else {
      promise =
        isUpdate && templateId
          ? TemplateService.updateTemplate(templateId, requestBody)
          : TemplateService.createTemplate(requestBody);
    }

    promise
      .then((saved) => onSaved(saved, isUpdate))
      .catch((err: Error) => setSaveError(err.message))
      .finally(() => setSaving(false));
  }, [
    name,
    description,
    naturalLanguage,
    placeholders,
    serviceId,
    templateId,
    onSaved,
    t.nameRequired,
    t.jsonInvalid,
  ]);

  return (
    <>
      {saveError && <Alert variant="danger" className="mb-3">{saveError}</Alert>}

      {/* --- Metadata section --- */}
      <Card className="mb-3">
        <Card.Header>{t.metadataSection}</Card.Header>
        <Card.Body>
          <Row className="g-3">
            <Col md={6}>
              <Form.Group controlId="template-name">
                <Form.Label>{t.nameLabel}</Form.Label>
                <Form.Control
                  type="text"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  placeholder={t.namePlaceholder}
                  required
                  aria-label={t.nameLabel}
                />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group controlId="template-description">
                <Form.Label>{t.descriptionLabel}</Form.Label>
                <Form.Control
                  type="text"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  placeholder={t.descriptionPlaceholder}
                  aria-label={t.descriptionLabel}
                />
              </Form.Group>
            </Col>
          </Row>
        </Card.Body>
      </Card>

      {/* --- ODRL skeleton section --- */}
      <Card className="mb-3">
        <Card.Header>{t.odrlSection}</Card.Header>
        <Card.Body>
          <Form.Text className="text-muted d-block mb-2">{t.odrlHelp}</Form.Text>
          <Form.Control
            as="textarea"
            rows={ODRL_TEXTAREA_ROWS}
            value={odrlText}
            onChange={(e) => handleOdrlChange(e.target.value)}
            isInvalid={!!odrlError}
            isValid={!odrlError && odrlText.length > 0}
            className="font-monospace"
            aria-label={t.odrlSection}
          />
          {odrlError && <Form.Text className="text-danger">{odrlError}</Form.Text>}
        </Card.Body>
      </Card>

      {/* --- Placeholders section --- */}
      <Card className="mb-3">
        <Card.Header>
          {t.placeholdersSection}
          {placeholders.length > 0 && (
            <span className="badge bg-secondary ms-2">{placeholders.length}</span>
          )}
        </Card.Header>
        <Card.Body>
          <Form.Text className="text-muted d-block mb-2">{t.placeholdersHelp}</Form.Text>
          <PlaceholderEditor
            placeholders={placeholders}
            onChange={setPlaceholders}
            detectedKeys={detectedKeys}
          />
        </Card.Body>
      </Card>

      {/* --- Natural language section --- */}
      <Card className="mb-3">
        <Card.Header>{t.naturalLanguageSection}</Card.Header>
        <Card.Body>
          <Form.Group controlId="template-natural-language">
            <Form.Text className="text-muted d-block mb-2">{t.naturalLanguageHelp}</Form.Text>
            <Form.Control
              as="textarea"
              rows={NATURAL_LANGUAGE_TEXTAREA_ROWS}
              value={naturalLanguage}
              onChange={(e) => setNaturalLanguage(e.target.value)}
              placeholder={t.naturalLanguagePlaceholder}
              aria-label={t.naturalLanguageSection}
            />
          </Form.Group>
          {naturalLanguage && (
            <div className="mt-2">
              <strong className="small text-muted">{t.preview}:</strong>
              <div className="p-2 border rounded bg-light mt-1">
                <NaturalLanguagePreview text={naturalLanguage} />
              </div>
            </div>
          )}
        </Card.Body>
      </Card>

      {/* --- Action buttons --- */}
      <hr />
      <Button variant="primary" onClick={handleSave} disabled={saving}>
        {saving ? strings.common.loading : strings.common.save}
      </Button>
      <Button variant="secondary" className="ms-2" onClick={onCancel}>
        {strings.common.cancel}
      </Button>
    </>
  );
};

export default TemplateForm;
