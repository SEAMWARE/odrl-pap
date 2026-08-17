/**
 * Embedded template management view.
 *
 * Provides full template CRUD inside the `<odrl-policy-editor>` Web Component:
 * lists existing templates and lets the user create, edit, and delete them.
 * Unlike the standalone SPA pages, it is router-free, scopes all operations
 * to the configured service (if any), and reports changes to the host page
 * through the `template-created` / `template-updated` Custom Events.
 *
 * The tab hosting this view can be disabled entirely via the
 * `hide-template-create-tab` attribute on the custom element.
 */
import { useCallback, useEffect, useState } from 'react';
import { Table, Button, Alert, Spinner } from 'react-bootstrap';
import { TemplateService } from '../api/services/TemplateService';
import { ServiceService } from '../api/services/ServiceService';
import type { Template } from '../api/models/Template';
import TemplateForm from '../components/TemplateForm';
import { useI18n } from '../i18n';
import type { OnEventCallback } from './EmbeddedContext';

/** The two sub-views of the manager: the template list or the editor form. */
type ManagerView = 'list' | 'form';

/** Props for the {@link EmbeddedTemplateManager} component. */
export interface EmbeddedTemplateManagerProps {
  /**
   * Service scope for all template operations. When set, service-scoped
   * endpoints are used; otherwise general (root-level) templates are managed.
   */
  serviceId: string | null;
  /** Callback used to emit `template-created` / `template-updated` events. */
  onEvent: OnEventCallback;
}

/**
 * Template management UI embedded in the Web Component.
 *
 * Fetches the (service-scoped) template list and toggles between a list
 * view and the shared {@link TemplateForm}. On save it refreshes the list,
 * returns to the list view, and dispatches the appropriate Custom Event.
 *
 * @param props - Component properties.
 */
const EmbeddedTemplateManager: React.FC<EmbeddedTemplateManagerProps> = ({
  serviceId,
  onEvent,
}) => {
  const { strings } = useI18n();
  const tList = strings.templateList;
  const tEditor = strings.templateEditor;

  const [templates, setTemplates] = useState<Template[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const [view, setView] = useState<ManagerView>('list');
  /** ID of the template currently being edited (`null` when creating). */
  const [editingId, setEditingId] = useState<string | null>(null);
  /** Loaded template data for the editor (`null` when creating). */
  const [editingTemplate, setEditingTemplate] = useState<Template | null>(null);
  /** Error surfaced while loading a template for editing. */
  const [editLoadError, setEditLoadError] = useState('');

  /** Fetches the template list for the current service scope. */
  const loadTemplates = useCallback(() => {
    setLoading(true);
    setError('');

    const promise = serviceId
      ? ServiceService.getServiceTemplates(serviceId)
      : TemplateService.getTemplates();

    promise
      .then(setTemplates)
      .catch(() => setError(tList.loadError))
      .finally(() => setLoading(false));
  }, [serviceId, tList.loadError]);

  useEffect(() => {
    loadTemplates();
  }, [loadTemplates]);

  /** Opens the editor for a brand-new template. */
  const handleNew = useCallback(() => {
    setEditingId(null);
    setEditingTemplate(null);
    setEditLoadError('');
    setView('form');
  }, []);

  /**
   * Loads the given template and opens the editor for it.
   *
   * @param template - The template row selected for editing.
   */
  const handleEdit = useCallback(
    (template: Template) => {
      setEditingId(template.id);
      setEditLoadError('');

      const promise = serviceId
        ? ServiceService.getServiceTemplateById(serviceId, template.id)
        : TemplateService.getTemplateById(template.id);

      promise
        .then((full) => {
          setEditingTemplate(full);
          setView('form');
        })
        .catch((err: Error) => setEditLoadError(err.message));
    },
    [serviceId],
  );

  /**
   * Deletes a template after confirmation and refreshes the list.
   *
   * @param id - The template ID to delete.
   */
  const handleDelete = useCallback(
    (id: string) => {
      if (!confirm(tList.confirmDelete)) return;

      const promise = serviceId
        ? ServiceService.deleteServiceTemplateById(serviceId, id)
        : TemplateService.deleteTemplateById(id);

      promise
        .then(() => setTemplates((prev) => prev.filter((tmpl) => tmpl.id !== id)))
        .catch((err: Error) => setError(err.message));
    },
    [serviceId, tList.confirmDelete],
  );

  /**
   * Handles a successful save from the form: emits the matching Custom
   * Event, refreshes the list, and returns to the list view.
   */
  const handleSaved = useCallback(
    (template: Template, isUpdate: boolean) => {
      onEvent(isUpdate ? 'template-updated' : 'template-created', {
        template: template as unknown as Record<string, unknown>,
        id: template.id,
      });
      loadTemplates();
      setView('list');
    },
    [onEvent, loadTemplates],
  );

  if (view === 'form') {
    return (
      <div>
        <h5 className="mb-3">{editingId ? tEditor.editTitle : tEditor.newTitle}</h5>
        {editLoadError && <Alert variant="danger">{editLoadError}</Alert>}
        <TemplateForm
          key={editingId ?? 'new'}
          templateId={editingId}
          serviceId={serviceId}
          initialTemplate={editingTemplate}
          onSaved={handleSaved}
          onCancel={() => setView('list')}
        />
      </div>
    );
  }

  return (
    <div>
      <Button
        variant="primary"
        className="mb-3"
        onClick={handleNew}
        aria-label={tList.newTemplate}
      >
        {tList.newTemplate}
      </Button>

      {error && <Alert variant="danger">{error}</Alert>}
      {editLoadError && <Alert variant="danger">{editLoadError}</Alert>}

      {loading ? (
        <div className="text-center py-4">
          <Spinner animation="border" role="status">
            <span className="visually-hidden">{strings.common.loading}</span>
          </Spinner>
        </div>
      ) : templates.length === 0 ? (
        <Alert variant="info">{tList.noTemplates}</Alert>
      ) : (
        <Table striped bordered hover aria-label={tList.title}>
          <thead>
            <tr>
              <th scope="col">{tList.columnName}</th>
              <th scope="col">{tList.columnDescription}</th>
              <th scope="col">{tList.columnPlaceholders}</th>
              <th scope="col">{tList.columnActions}</th>
            </tr>
          </thead>
          <tbody>
            {templates.map((template) => (
              <tr key={template.id}>
                <td>{template.name}</td>
                <td className="text-truncate" style={{ maxWidth: '300px' }}>
                  {template.description || (
                    <span className="text-muted">{strings.common.notSet}</span>
                  )}
                </td>
                <td>
                  <span className="badge bg-secondary">{template.placeholders.length}</span>
                </td>
                <td>
                  <Button
                    variant="primary"
                    size="sm"
                    className="me-2"
                    onClick={() => handleEdit(template)}
                    aria-label={`${strings.common.edit} ${template.name}`}
                  >
                    {strings.common.edit}
                  </Button>
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
    </div>
  );
};

export default EmbeddedTemplateManager;
