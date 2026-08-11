/**
 * Template editor page.
 *
 * Provides a form for creating and editing policy templates with
 * sections for metadata, ODRL skeleton (raw JSON), placeholder
 * definitions, and a natural language description with live preview.
 *
 * Placeholders are auto-detected from the ODRL JSON and can also
 * be manually added. Templates can be scoped to a specific service.
 */
import { useState, useEffect, useCallback, useRef } from 'react';
import { useParams, useNavigate, useSearchParams } from 'react-router-dom';
import { Form, Button, Alert, Card, Row, Col } from 'react-bootstrap';
import { TemplateService } from '../api/services/TemplateService';
import { ServiceService } from '../api/services/ServiceService';
import type { TemplatePlaceholder } from '../api/models/TemplatePlaceholder';
import { TemplatePlaceholder as TemplatePlaceholderEnum } from '../api/models/TemplatePlaceholder';
import type { TemplateCreate } from '../api/models/TemplateCreate';
import type { OdrlPolicyJson } from '../api/models/OdrlPolicyJson';
import type { ServiceList } from '../api/models/ServiceList';
import PlaceholderEditor from '../components/PlaceholderEditor';
import NaturalLanguagePreview from '../components/NaturalLanguagePreview';
import { extractPlaceholderKeys, createEmptyPlaceholder } from '../types/TemplateTypes';
import { useI18n } from '../i18n';

/** Default number of rows for the ODRL JSON textarea. */
const ODRL_TEXTAREA_ROWS = 15;

/** Default number of rows for the natural language textarea. */
const NATURAL_LANGUAGE_TEXTAREA_ROWS = 3;

/** Debounce delay (ms) for placeholder extraction from text changes. */
const PLACEHOLDER_SYNC_DEBOUNCE_MS = 500;

/** Minimal ODRL skeleton for new templates. */
const DEFAULT_ODRL_SKELETON: Record<string, unknown> = {
  '@context': { odrl: 'http://www.w3.org/ns/odrl/2/' },
  '@type': 'odrl:Policy',
  'odrl:permission': {},
};

/**
 * Page component for creating and editing policy templates.
 *
 * Supports both general and service-scoped templates. Auto-detects
 * placeholder keys from the ODRL JSON and provides a live preview
 * of the natural language description with highlighted placeholders.
 */
const TemplateEditor = () => {
  const { id } = useParams();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { strings } = useI18n();
  const t = strings.templateEditor;

  // --- Metadata state ---
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');

  // --- ODRL skeleton state ---
  const [odrlText, setOdrlText] = useState(JSON.stringify(DEFAULT_ODRL_SKELETON, null, 2));
  const [odrlError, setOdrlError] = useState('');
  /** Parsed ODRL JSON (null when text is invalid). */
  const parsedOdrlRef = useRef<OdrlPolicyJson | null>(DEFAULT_ODRL_SKELETON as OdrlPolicyJson);

  // --- Placeholders state ---
  const [placeholders, setPlaceholders] = useState<TemplatePlaceholder[]>([]);
  /** Keys auto-detected from the ODRL skeleton. */
  const [detectedKeys, setDetectedKeys] = useState<Set<string>>(new Set());

  // --- Natural language state ---
  const [naturalLanguage, setNaturalLanguage] = useState('');

  // --- Service selection state ---
  const [services, setServices] = useState<ServiceList>([]);
  const [servicesLoading, setServicesLoading] = useState(true);
  const [selectedServiceId, setSelectedServiceId] = useState<string | null>(
    searchParams.get('serviceId'),
  );

  // --- Form state ---
  const [saving, setSaving] = useState(false);
  const [saveError, setSaveError] = useState('');
  const [loading, setLoading] = useState(!!id);

  // Fetch available services on mount
  useEffect(() => {
    setServicesLoading(true);
    ServiceService.getServices()
      .then(setServices)
      .catch(() => { /* services are optional */ })
      .finally(() => setServicesLoading(false));
  }, []);

  // Load existing template when editing
  useEffect(() => {
    if (!id) return;

    setLoading(true);
    const serviceIdParam = searchParams.get('serviceId');

    const promise = serviceIdParam
      ? ServiceService.getServiceTemplateById(serviceIdParam, id)
      : TemplateService.getTemplateById(id);

    promise
      .then((template) => {
        setName(template.name);
        setDescription(template.description ?? '');
        setNaturalLanguage(template.naturalLanguage ?? '');
        setPlaceholders(template.placeholders);
        const odrlStr = typeof template.odrl === 'string'
          ? template.odrl
          : JSON.stringify(template.odrl, null, 2);
        setOdrlText(odrlStr);
        try {
          parsedOdrlRef.current = (typeof template.odrl === 'string'
            ? JSON.parse(template.odrl)
            : template.odrl) as OdrlPolicyJson;
        } catch {
          parsedOdrlRef.current = null;
        }
        if (serviceIdParam) {
          setSelectedServiceId(serviceIdParam);
        }
      })
      .catch((err: Error) => setSaveError(err.message))
      .finally(() => setLoading(false));
  }, [id, searchParams]);

  /**
   * Handles changes in the ODRL JSON textarea.
   *
   * Validates JSON syntax. Placeholder synchronization (adding new
   * keys, removing stale ones) is handled by the combined effect
   * that watches both `odrlText` and `naturalLanguage`.
   *
   * @param text - The raw ODRL JSON text.
   */
  const handleOdrlChange = useCallback(
    (text: string) => {
      setOdrlText(text);
      try {
        const parsed = JSON.parse(text) as OdrlPolicyJson;
        parsedOdrlRef.current = parsed;
        setOdrlError('');
      } catch (err: unknown) {
        parsedOdrlRef.current = null;
        setOdrlError(err instanceof Error ? err.message : t.jsonInvalid);
      }
    },
    [t.jsonInvalid],
  );

  // Synchronize placeholders with detected keys from ODRL + NL text.
  // Debounced so that intermediate keystrokes (e.g. while typing
  // {{PLACEHOLDER}}) do not create and immediately destroy entries.
  // Adds entries for newly detected keys and removes entries whose
  // keys no longer appear in either source.
  useEffect(() => {
    const timer = setTimeout(() => {
      const nlKeys = extractPlaceholderKeys(naturalLanguage);
      const odrlKeys = extractPlaceholderKeys(odrlText);
      const allKeys = new Set([...odrlKeys, ...nlKeys]);
      setDetectedKeys(allKeys);

      setPlaceholders((prev) => {
        const existingKeys = new Set(prev.map((p) => p.key));
        const newKeys = [...allKeys].filter((k) => !existingKeys.has(k));

        // Remove placeholders whose keys are no longer detected
        const filtered = prev.filter((p) => allKeys.has(p.key));

        if (newKeys.length === 0 && filtered.length === prev.length) return prev;
        return [
          ...filtered,
          ...newKeys.map((k) => createEmptyPlaceholder(k)),
        ];
      });
    }, PLACEHOLDER_SYNC_DEBOUNCE_MS);

    return () => clearTimeout(timer);
  }, [naturalLanguage, odrlText]);

  /**
   * Saves the template (create or update).
   *
   * Validates the form before submission and navigates back to
   * the template list on success.
   */
  const handleSave = useCallback(() => {
    // Validate required fields
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

    let promise;
    if (selectedServiceId) {
      promise = id
        ? ServiceService.updateServiceTemplate(selectedServiceId, id, requestBody)
        : ServiceService.createServiceTemplate(selectedServiceId, requestBody);
    } else {
      promise = id
        ? TemplateService.updateTemplate(id, requestBody)
        : TemplateService.createTemplate(requestBody);
    }

    promise
      .then(() => navigate('/templates'))
      .catch((err: Error) => setSaveError(err.message))
      .finally(() => setSaving(false));
  }, [name, description, naturalLanguage, placeholders, selectedServiceId, id, navigate, t.nameRequired, t.jsonInvalid]);

  if (loading) {
    return <p>{strings.common.loading}</p>;
  }

  return (
    <>
      <h1>{id ? t.editTitle : t.newTitle}</h1>

      {saveError && <Alert variant="danger" className="mb-3">{saveError}</Alert>}

      {/* --- Service selection --- */}
      {!servicesLoading && services.length > 0 && (
        <Form.Group className="mb-3" controlId="template-service-select">
          <Form.Label>{t.serviceLabel}</Form.Label>
          <Form.Select
            value={selectedServiceId ?? ''}
            onChange={(e) => setSelectedServiceId(e.target.value || null)}
            aria-label={t.serviceLabel}
          >
            <option value="">{t.serviceNone}</option>
            {services.map((svc) => (
              <option key={svc.id} value={svc.id}>
                {svc.id}
              </option>
            ))}
          </Form.Select>
          <Form.Text className="text-muted">{t.serviceTooltip}</Form.Text>
        </Form.Group>
      )}

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
          <Form.Text className="text-muted d-block mb-2">
            {t.odrlHelp}
          </Form.Text>
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
          {odrlError && (
            <Form.Text className="text-danger">{odrlError}</Form.Text>
          )}
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
          <Form.Text className="text-muted d-block mb-2">
            {t.placeholdersHelp}
          </Form.Text>
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
            <Form.Text className="text-muted d-block mb-2">
              {t.naturalLanguageHelp}
            </Form.Text>
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
      <Button variant="secondary" className="ms-2" onClick={() => navigate('/templates')}>
        {strings.common.cancel}
      </Button>
    </>
  );
};

export default TemplateEditor;
