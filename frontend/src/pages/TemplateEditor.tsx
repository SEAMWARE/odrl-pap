/**
 * Template editor page.
 *
 * Thin routing wrapper around the shared {@link TemplateForm} component.
 * Handles URL parameters (template ID, service scope), loads the template
 * being edited, renders the service selector, and navigates back to the
 * template list after a successful save or cancel.
 */
import { useState, useEffect } from 'react';
import { useParams, useNavigate, useSearchParams } from 'react-router-dom';
import { Form, Alert } from 'react-bootstrap';
import { TemplateService } from '../api/services/TemplateService';
import { ServiceService } from '../api/services/ServiceService';
import type { Template } from '../api/models/Template';
import type { ServiceList } from '../api/models/ServiceList';
import TemplateForm from '../components/TemplateForm';
import { useI18n } from '../i18n';

/**
 * Page component for creating and editing policy templates.
 *
 * Supports both general and service-scoped templates. Delegates the actual
 * form rendering, placeholder auto-detection, and persistence to
 * {@link TemplateForm}.
 */
const TemplateEditor = () => {
  const { id } = useParams();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { strings } = useI18n();
  const t = strings.templateEditor;

  // --- Service selection state ---
  const [services, setServices] = useState<ServiceList>([]);
  const [servicesLoading, setServicesLoading] = useState(true);
  const [selectedServiceId, setSelectedServiceId] = useState<string | null>(
    searchParams.get('serviceId'),
  );

  // --- Loaded template (when editing) ---
  const [initialTemplate, setInitialTemplate] = useState<Template | null>(null);
  const [loading, setLoading] = useState(!!id);
  const [loadError, setLoadError] = useState('');

  // Fetch available services on mount.
  useEffect(() => {
    setServicesLoading(true);
    ServiceService.getServices()
      .then(setServices)
      .catch(() => { /* services are optional */ })
      .finally(() => setServicesLoading(false));
  }, []);

  // Load the existing template when editing.
  useEffect(() => {
    if (!id) return;

    setLoading(true);
    const serviceIdParam = searchParams.get('serviceId');

    const promise = serviceIdParam
      ? ServiceService.getServiceTemplateById(serviceIdParam, id)
      : TemplateService.getTemplateById(id);

    promise
      .then((template) => {
        setInitialTemplate(template);
        if (serviceIdParam) {
          setSelectedServiceId(serviceIdParam);
        }
      })
      .catch((err: Error) => setLoadError(err.message))
      .finally(() => setLoading(false));
  }, [id, searchParams]);

  if (loading) {
    return <p>{strings.common.loading}</p>;
  }

  return (
    <>
      <h1>{id ? t.editTitle : t.newTitle}</h1>

      {loadError && <Alert variant="danger" className="mb-3">{loadError}</Alert>}

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

      <TemplateForm
        key={id ?? 'new'}
        templateId={id ?? null}
        serviceId={selectedServiceId}
        initialTemplate={initialTemplate}
        onSaved={() => navigate('/templates')}
        onCancel={() => navigate('/templates')}
      />
    </>
  );
};

export default TemplateEditor;
