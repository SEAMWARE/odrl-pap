/**
 * Unit tests for the {@link EmbeddedTemplateManager} component.
 *
 * Verifies list rendering, navigation between list and form views,
 * event emission on save, template deletion, and service-scoped API
 * selection. The shared {@link TemplateForm} is stubbed so these tests
 * focus on the manager's own orchestration logic.
 */
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { I18nProvider } from '../i18n';
import EmbeddedTemplateManager from './EmbeddedTemplateManager';
import { TemplateService } from '../api/services/TemplateService';
import { ServiceService } from '../api/services/ServiceService';
import type { Template } from '../api/models/Template';

vi.mock('../api/services/TemplateService', () => ({
  TemplateService: {
    getTemplates: vi.fn(),
    getTemplateById: vi.fn(),
    deleteTemplateById: vi.fn(),
  },
}));

vi.mock('../api/services/ServiceService', () => ({
  ServiceService: {
    getServiceTemplates: vi.fn(),
    getServiceTemplateById: vi.fn(),
    deleteServiceTemplateById: vi.fn(),
  },
}));

// Stub the shared form so we can drive onSaved / onCancel deterministically.
vi.mock('../components/TemplateForm', () => ({
  default: (props: {
    templateId?: string | null;
    onSaved: (template: Template, isUpdate: boolean) => void;
    onCancel: () => void;
  }) => (
    <div data-testid="template-form-stub">
      <span>form:{props.templateId ?? 'new'}</span>
      <button
        onClick={() =>
          props.onSaved(
            { id: props.templateId ?? 'created-id', name: 'Saved', odrl: {}, placeholders: [] } as unknown as Template,
            !!props.templateId,
          )
        }
      >
        stub-save
      </button>
      <button onClick={props.onCancel}>stub-cancel</button>
    </div>
  ),
}));

/** Sample templates returned by the list endpoints. */
const MOCK_TEMPLATES: Template[] = [
  { id: 'tmpl-1', name: 'Access Policy', description: 'Grants access', odrl: {}, placeholders: [] } as unknown as Template,
  { id: 'tmpl-2', name: 'Rate Limit', description: '', odrl: {}, placeholders: [] } as unknown as Template,
];

/** Renders the manager wrapped in the required I18nProvider. */
const renderManager = (serviceId: string | null = null, onEvent = vi.fn()) => {
  render(
    <I18nProvider>
      <EmbeddedTemplateManager serviceId={serviceId} onEvent={onEvent} />
    </I18nProvider>,
  );
  return onEvent;
};

describe('EmbeddedTemplateManager', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.stubGlobal('confirm', vi.fn(() => true));
  });

  it('lists general templates from the template API', async () => {
    vi.mocked(TemplateService.getTemplates).mockResolvedValue(MOCK_TEMPLATES);

    renderManager();

    expect(await screen.findByText('Access Policy')).toBeInTheDocument();
    expect(screen.getByText('Rate Limit')).toBeInTheDocument();
    expect(TemplateService.getTemplates).toHaveBeenCalledTimes(1);
  });

  it('uses service-scoped endpoints when a serviceId is set', async () => {
    vi.mocked(ServiceService.getServiceTemplates).mockResolvedValue(MOCK_TEMPLATES);

    renderManager('svc-a');

    await waitFor(() => expect(ServiceService.getServiceTemplates).toHaveBeenCalledWith('svc-a'));
    expect(TemplateService.getTemplates).not.toHaveBeenCalled();
  });

  it('opens a blank form for a new template and emits template-created on save', async () => {
    vi.mocked(TemplateService.getTemplates).mockResolvedValue([]);
    const onEvent = renderManager();

    await screen.findByText('No templates found. Create one to get started.');
    await userEvent.click(screen.getByRole('button', { name: 'New Template' }));

    expect(screen.getByText('form:new')).toBeInTheDocument();

    await userEvent.click(screen.getByRole('button', { name: 'stub-save' }));

    expect(onEvent).toHaveBeenCalledWith('template-created', {
      template: expect.objectContaining({ id: 'created-id' }),
      id: 'created-id',
    });
  });

  it('loads a template for editing and emits template-updated on save', async () => {
    vi.mocked(TemplateService.getTemplates).mockResolvedValue(MOCK_TEMPLATES);
    vi.mocked(TemplateService.getTemplateById).mockResolvedValue(MOCK_TEMPLATES[0]);
    const onEvent = renderManager();

    await screen.findByText('Access Policy');
    await userEvent.click(screen.getByRole('button', { name: 'Edit Access Policy' }));

    await waitFor(() => expect(TemplateService.getTemplateById).toHaveBeenCalledWith('tmpl-1'));
    expect(await screen.findByText('form:tmpl-1')).toBeInTheDocument();

    await userEvent.click(screen.getByRole('button', { name: 'stub-save' }));

    expect(onEvent).toHaveBeenCalledWith('template-updated', {
      template: expect.objectContaining({ id: 'tmpl-1' }),
      id: 'tmpl-1',
    });
  });

  it('deletes a template after confirmation', async () => {
    vi.mocked(TemplateService.getTemplates).mockResolvedValue(MOCK_TEMPLATES);
    vi.mocked(TemplateService.deleteTemplateById).mockResolvedValue(undefined);

    renderManager();

    await screen.findByText('Access Policy');
    await userEvent.click(screen.getByRole('button', { name: 'Delete Access Policy' }));

    await waitFor(() => expect(TemplateService.deleteTemplateById).toHaveBeenCalledWith('tmpl-1'));
    await waitFor(() => expect(screen.queryByText('Access Policy')).not.toBeInTheDocument());
  });

  it('returns to the list view when the form is cancelled', async () => {
    vi.mocked(TemplateService.getTemplates).mockResolvedValue(MOCK_TEMPLATES);
    renderManager();

    await screen.findByText('Access Policy');
    await userEvent.click(screen.getByRole('button', { name: 'New Template' }));
    expect(screen.getByTestId('template-form-stub')).toBeInTheDocument();

    await userEvent.click(screen.getByRole('button', { name: 'stub-cancel' }));

    expect(await screen.findByText('Access Policy')).toBeInTheDocument();
    expect(screen.queryByTestId('template-form-stub')).not.toBeInTheDocument();
  });
});
