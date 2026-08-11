/**
 * Template list page component.
 *
 * Displays all stored policy templates in a table with edit and delete
 * actions. Provides a link to create new templates. Supports both
 * general (root-level) and service-scoped template views.
 */
import { useEffect, useState } from 'react';
import { Table, Button, Alert, Form, Spinner } from 'react-bootstrap';
import { TemplateService } from '../api/services/TemplateService';
import { ServiceService } from '../api/services/ServiceService';
import type { Template } from '../api/models/Template';
import type { ServiceList } from '../api/models/ServiceList';
import { Link } from 'react-router-dom';
import { useI18n } from '../i18n';

/**
 * Renders a table of all policy templates with CRUD actions.
 *
 * When a service is selected, displays service-scoped templates;
 * otherwise displays general (root-level) templates.
 */
const TemplateList = () => {
  const { strings } = useI18n();
  const t = strings.templateList;

  const [templates, setTemplates] = useState<Template[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // Service filter state
  const [services, setServices] = useState<ServiceList>([]);
  const [selectedServiceId, setSelectedServiceId] = useState<string>('');
  const [servicesLoading, setServicesLoading] = useState(true);

  // Fetch services on mount
  useEffect(() => {
    setServicesLoading(true);
    ServiceService.getServices()
      .then(setServices)
      .catch(() => { /* services are optional */ })
      .finally(() => setServicesLoading(false));
  }, []);

  // Fetch templates when service selection changes
  useEffect(() => {
    setLoading(true);
    setError('');

    const promise = selectedServiceId
      ? ServiceService.getServiceTemplates(selectedServiceId)
      : TemplateService.getTemplates();

    promise
      .then(setTemplates)
      .catch(() => setError(t.loadError))
      .finally(() => setLoading(false));
  }, [selectedServiceId, t.loadError]);

  /**
   * Deletes a template by ID and removes it from the displayed list.
   *
   * @param id - The template ID to delete.
   */
  const handleDelete = (id: string) => {
    if (!confirm(t.confirmDelete)) return;

    const promise = selectedServiceId
      ? ServiceService.deleteServiceTemplateById(selectedServiceId, id)
      : TemplateService.deleteTemplateById(id);

    promise
      .then(() => {
        setTemplates(templates.filter((tmpl) => tmpl.id !== id));
      })
      .catch(console.error);
  };

  /**
   * Handles service dropdown selection changes.
   *
   * @param value - The selected option value (empty string for general templates).
   */
  const handleServiceChange = (value: string) => {
    setSelectedServiceId(value);
  };

  return (
    <>
      <h1>{t.title}</h1>

      {/* Service filter */}
      {!servicesLoading && services.length > 0 && (
        <Form.Group className="mb-3" controlId="template-service-filter">
          <Form.Label>{t.serviceFilter}</Form.Label>
          <Form.Select
            value={selectedServiceId}
            onChange={(e) => handleServiceChange(e.target.value)}
            aria-label={t.serviceFilter}
          >
            <option value="">{t.allGeneral}</option>
            {services.map((svc) => (
              <option key={svc.id} value={svc.id}>
                {svc.id}
              </option>
            ))}
          </Form.Select>
        </Form.Group>
      )}

      <Link
        to={selectedServiceId ? `/templates/new?serviceId=${selectedServiceId}` : '/templates/new'}
        className="btn btn-primary mb-3"
        aria-label={t.newTemplate}
      >
        {t.newTemplate}
      </Link>

      {error && <Alert variant="danger">{error}</Alert>}

      {loading ? (
        <div className="text-center py-4">
          <Spinner animation="border" role="status">
            <span className="visually-hidden">{strings.common.loading}</span>
          </Spinner>
        </div>
      ) : templates.length === 0 ? (
        <Alert variant="info">{t.noTemplates}</Alert>
      ) : (
        <Table striped bordered hover aria-label={t.title}>
          <thead>
            <tr>
              <th scope="col">{t.columnName}</th>
              <th scope="col">{t.columnDescription}</th>
              <th scope="col">{t.columnPlaceholders}</th>
              <th scope="col">{t.columnActions}</th>
            </tr>
          </thead>
          <tbody>
            {templates.map((template) => (
              <tr key={template.id}>
                <td>{template.name}</td>
                <td className="text-truncate" style={{ maxWidth: '300px' }}>
                  {template.description || <span className="text-muted">{strings.common.notSet}</span>}
                </td>
                <td>
                  <span className="badge bg-secondary">
                    {template.placeholders.length}
                  </span>
                </td>
                <td>
                  <Link
                    to={
                      selectedServiceId
                        ? `/templates/edit/${template.id}?serviceId=${selectedServiceId}`
                        : `/templates/edit/${template.id}`
                    }
                    className="btn btn-sm btn-primary me-2"
                    aria-label={`${strings.common.edit} ${template.name}`}
                  >
                    {strings.common.edit}
                  </Link>
                  <Button
                    variant="danger"
                    size="sm"
                    onClick={() => handleDelete(template.id)}
                    aria-label={`${strings.common.delete} ${template.name}`}
                  >
                    {strings.common.delete}
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </>
  );
};

export default TemplateList;
