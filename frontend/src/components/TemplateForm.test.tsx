/**
 * Unit tests for the shared {@link TemplateForm} component.
 *
 * Verifies rendering of all sections, create/update persistence via the
 * general and service-scoped template APIs, client-side validation,
 * placeholder auto-detection, and cancel handling.
 */
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { I18nProvider } from '../i18n';
import TemplateForm from './TemplateForm';
import { TemplateService } from '../api/services/TemplateService';
import { ServiceService } from '../api/services/ServiceService';
import type { Template } from '../api/models/Template';

// Mock the template APIs so no network calls are made.
vi.mock('../api/services/TemplateService', () => ({
  TemplateService: {
    createTemplate: vi.fn(),
    updateTemplate: vi.fn(),
  },
}));

vi.mock('../api/services/ServiceService', () => ({
  ServiceService: {
    createServiceTemplate: vi.fn(),
    updateServiceTemplate: vi.fn(),
  },
}));

/** A stored template used to exercise the edit path. */
const EXISTING_TEMPLATE: Template = {
  id: 'tmpl-1',
  name: 'Existing Template',
  description: 'An existing template',
  odrl: { '@type': 'odrl:Policy', 'odrl:permission': {} } as unknown as Template['odrl'],
  naturalLanguage: 'Allow access',
  placeholders: [],
};

/** Renders the form wrapped in the required I18nProvider. */
const renderForm = (ui: React.ReactElement) =>
  render(<I18nProvider>{ui}</I18nProvider>);

describe('TemplateForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders all editor sections', () => {
    renderForm(<TemplateForm onSaved={vi.fn()} onCancel={vi.fn()} />);

    expect(screen.getByText('Template Metadata')).toBeInTheDocument();
    expect(screen.getByText('ODRL Policy Skeleton')).toBeInTheDocument();
    expect(screen.getByText('Placeholder Definitions')).toBeInTheDocument();
    expect(screen.getByText('Natural Language Description')).toBeInTheDocument();
  });

  it('creates a new general template and reports the result', async () => {
    const created: Template = { ...EXISTING_TEMPLATE, id: 'new-id', name: 'My Template' };
    vi.mocked(TemplateService.createTemplate).mockResolvedValue(created);
    const onSaved = vi.fn();

    renderForm(<TemplateForm onSaved={onSaved} onCancel={vi.fn()} />);

    await userEvent.type(screen.getByLabelText('Template Name'), 'My Template');
    await userEvent.click(screen.getByRole('button', { name: 'Save' }));

    await waitFor(() => expect(TemplateService.createTemplate).toHaveBeenCalledTimes(1));
    expect(vi.mocked(TemplateService.createTemplate).mock.calls[0][0]).toMatchObject({
      name: 'My Template',
    });
    await waitFor(() => expect(onSaved).toHaveBeenCalledWith(created, false));
  });

  it('updates an existing template when a templateId is provided', async () => {
    const updated: Template = { ...EXISTING_TEMPLATE, name: 'Existing Template' };
    vi.mocked(TemplateService.updateTemplate).mockResolvedValue(updated);
    const onSaved = vi.fn();

    renderForm(
      <TemplateForm
        templateId="tmpl-1"
        initialTemplate={EXISTING_TEMPLATE}
        onSaved={onSaved}
        onCancel={vi.fn()}
      />,
    );

    // The existing name should be pre-filled.
    expect(screen.getByLabelText('Template Name')).toHaveValue('Existing Template');

    await userEvent.click(screen.getByRole('button', { name: 'Save' }));

    await waitFor(() => expect(TemplateService.updateTemplate).toHaveBeenCalledWith(
      'tmpl-1',
      expect.objectContaining({ name: 'Existing Template' }),
    ));
    await waitFor(() => expect(onSaved).toHaveBeenCalledWith(updated, true));
  });

  it('uses service-scoped endpoints when a serviceId is set', async () => {
    const created: Template = { ...EXISTING_TEMPLATE, id: 'svc-tmpl' };
    vi.mocked(ServiceService.createServiceTemplate).mockResolvedValue(created);

    renderForm(<TemplateForm serviceId="svc-a" onSaved={vi.fn()} onCancel={vi.fn()} />);

    await userEvent.type(screen.getByLabelText('Template Name'), 'Scoped');
    await userEvent.click(screen.getByRole('button', { name: 'Save' }));

    await waitFor(() => expect(ServiceService.createServiceTemplate).toHaveBeenCalledTimes(1));
    expect(vi.mocked(ServiceService.createServiceTemplate).mock.calls[0][0]).toBe('svc-a');
    expect(TemplateService.createTemplate).not.toHaveBeenCalled();
  });

  it('blocks saving and shows an error when the name is empty', async () => {
    renderForm(<TemplateForm onSaved={vi.fn()} onCancel={vi.fn()} />);

    await userEvent.click(screen.getByRole('button', { name: 'Save' }));

    expect(await screen.findByText('Template name is required.')).toBeInTheDocument();
    expect(TemplateService.createTemplate).not.toHaveBeenCalled();
  });

  it('auto-detects placeholders from the ODRL skeleton', async () => {
    renderForm(<TemplateForm onSaved={vi.fn()} onCancel={vi.fn()} />);

    const odrlArea = screen.getByLabelText('ODRL Policy Skeleton');
    fireEvent.change(odrlArea, {
      target: {
        value: JSON.stringify({ '@type': 'odrl:Policy', 'odrl:target': '{{RESOURCE_ID}}' }),
      },
    });

    // Placeholder detection is debounced (500ms); wait for the entry to appear.
    await waitFor(
      () => expect(screen.getByDisplayValue('RESOURCE_ID')).toBeInTheDocument(),
      { timeout: 2000 },
    );
  });

  it('invokes onCancel when Cancel is clicked', async () => {
    const onCancel = vi.fn();
    renderForm(<TemplateForm onSaved={vi.fn()} onCancel={onCancel} />);

    await userEvent.click(screen.getByRole('button', { name: 'Cancel' }));

    expect(onCancel).toHaveBeenCalledTimes(1);
  });
});
