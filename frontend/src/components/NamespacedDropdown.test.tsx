/**
 * Unit tests for the NamespacedDropdown component.
 *
 * Verifies namespace grouping, search/filter behavior, empty states,
 * and callback handling for the reusable dropdown.
 */
import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import NamespacedDropdown from './NamespacedDropdown';
import { splitNamespace, formatGroupLabel } from './namespaceUtils';
import { I18nProvider } from '../i18n';
import type { Mapping } from '../api';

/** Wraps a component with the required I18nProvider. */
const renderWithI18n = (ui: React.ReactElement) =>
  render(<I18nProvider>{ui}</I18nProvider>);

/** Sample mappings spanning multiple namespaces. */
const SAMPLE_ITEMS: Mapping[] = [
  { name: 'odrl:read', description: 'To read a resource' },
  { name: 'odrl:use', description: 'To use a resource' },
  { name: 'dome-op:access', description: 'To access a DOME resource' },
  { name: 'tmf:resource', description: 'TM Forum resource' },
];

/** Minimal items list (below default search threshold). */
const FEW_ITEMS: Mapping[] = [
  { name: 'odrl:read', description: 'To read a resource' },
  { name: 'odrl:use', description: 'To use a resource' },
];

describe('splitNamespace', () => {
  it.each([
    ['odrl:read', ['odrl', 'read']],
    ['dome-op:access', ['dome-op', 'access']],
    ['http:body_value', ['http', 'body_value']],
    ['nonamespace', ['other', 'nonamespace']],
    ['a:b:c', ['a', 'b:c']],
  ])('splits "%s" into %j', (input, expected) => {
    expect(splitNamespace(input)).toEqual(expected);
  });
});

describe('formatGroupLabel', () => {
  it.each([
    ['odrl', 'ODRL'],
    ['dome-op', 'DOME-OP'],
    ['http', 'HTTP'],
    ['tmf', 'TMF'],
  ])('formats "%s" as "%s"', (input, expected) => {
    expect(formatGroupLabel(input)).toBe(expected);
  });
});

describe('NamespacedDropdown', () => {
  it('renders a placeholder option', () => {
    renderWithI18n(
      <NamespacedDropdown
        items={SAMPLE_ITEMS}
        value=""
        onChange={() => {}}
        placeholder="Pick one"
      />,
    );
    expect(screen.getByText('Pick one')).toBeInTheDocument();
  });

  it('groups items by namespace with optgroup labels', () => {
    const { container } = renderWithI18n(
      <NamespacedDropdown items={SAMPLE_ITEMS} value="" onChange={() => {}} />,
    );
    const optgroups = container.querySelectorAll('optgroup');
    const labels = Array.from(optgroups).map((og) => og.getAttribute('label'));
    // Should have DOME-OP, ODRL, TMF (sorted alphabetically)
    expect(labels).toEqual(['DOME-OP', 'ODRL', 'TMF']);
  });

  it('shows description alongside name in options', () => {
    renderWithI18n(
      <NamespacedDropdown items={SAMPLE_ITEMS} value="" onChange={() => {}} />,
    );
    const option = screen.getByText(/odrl:read/);
    expect(option.textContent).toContain('To read a resource');
  });

  it('calls onChange with the selected value', async () => {
    const user = userEvent.setup();
    const handleChange = vi.fn();

    renderWithI18n(
      <NamespacedDropdown
        items={SAMPLE_ITEMS}
        value=""
        onChange={handleChange}
        placeholder="Select"
      />,
    );

    const select = screen.getByRole('combobox');
    await user.selectOptions(select, 'odrl:read');
    expect(handleChange).toHaveBeenCalledWith('odrl:read');
  });

  it('does not show search input when item count is below threshold', () => {
    renderWithI18n(
      <NamespacedDropdown items={FEW_ITEMS} value="" onChange={() => {}} />,
    );
    expect(screen.queryByPlaceholderText(/filter/i)).not.toBeInTheDocument();
  });

  it('shows search input when item count meets threshold', () => {
    renderWithI18n(
      <NamespacedDropdown
        items={SAMPLE_ITEMS}
        value=""
        onChange={() => {}}
        searchThreshold={3}
      />,
    );
    expect(screen.getByPlaceholderText(/filter/i)).toBeInTheDocument();
  });

  it('filters items based on search input', async () => {
    const user = userEvent.setup();

    renderWithI18n(
      <NamespacedDropdown
        items={SAMPLE_ITEMS}
        value=""
        onChange={() => {}}
        searchThreshold={1}
      />,
    );

    const searchInput = screen.getByPlaceholderText(/filter/i);
    await user.type(searchInput, 'dome');

    // Should only show dome-op:access
    expect(screen.getByText(/dome-op:access/)).toBeInTheDocument();
    expect(screen.queryByText(/odrl:read/)).not.toBeInTheDocument();
  });

  it('filters items by description text', async () => {
    const user = userEvent.setup();

    renderWithI18n(
      <NamespacedDropdown
        items={SAMPLE_ITEMS}
        value=""
        onChange={() => {}}
        searchThreshold={1}
      />,
    );

    const searchInput = screen.getByPlaceholderText(/filter/i);
    await user.type(searchInput, 'Forum');

    expect(screen.getByText(/tmf:resource/)).toBeInTheDocument();
    expect(screen.queryByText(/odrl:read/)).not.toBeInTheDocument();
  });

  it('shows "no results" when filter matches nothing', async () => {
    const user = userEvent.setup();

    renderWithI18n(
      <NamespacedDropdown
        items={SAMPLE_ITEMS}
        value=""
        onChange={() => {}}
        searchThreshold={1}
      />,
    );

    const searchInput = screen.getByPlaceholderText(/filter/i);
    await user.type(searchInput, 'zzzznotfound');

    expect(screen.getByText(/no matching/i)).toBeInTheDocument();
  });

  it('renders empty state gracefully when items is empty', () => {
    const { container } = renderWithI18n(
      <NamespacedDropdown items={[]} value="" onChange={() => {}} />,
    );
    const optgroups = container.querySelectorAll('optgroup');
    expect(optgroups.length).toBe(0);
  });

  it('disables the select when disabled prop is true', () => {
    renderWithI18n(
      <NamespacedDropdown
        items={SAMPLE_ITEMS}
        value=""
        onChange={() => {}}
        disabled
      />,
    );
    const select = screen.getByRole('combobox');
    expect(select).toBeDisabled();
  });

  it('applies custom ariaLabel', () => {
    renderWithI18n(
      <NamespacedDropdown
        items={SAMPLE_ITEMS}
        value=""
        onChange={() => {}}
        ariaLabel="Choose action"
      />,
    );
    expect(screen.getByLabelText('Choose action')).toBeInTheDocument();
  });
});
