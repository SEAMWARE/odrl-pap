/**
 * Unit tests for the PlaceholderEditor component.
 *
 * Focus: the dropdown-options input must let the user type a
 * comma-separated list by hand. Previously the value was re-parsed on every
 * keystroke, which dropped the just-typed comma and merged options together.
 */
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { useState } from 'react';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import PlaceholderEditor from './PlaceholderEditor';
import { I18nProvider } from '../i18n';
import type { TemplatePlaceholder } from '../api/models/TemplatePlaceholder';

/** The accessible label of the first placeholder's options input. */
const OPTIONS_LABEL = 'Options (comma-separated, optional) 1';

/** Renders the editor wrapped in the required I18nProvider. */
const renderEditor = (ui: React.ReactElement) =>
  render(<I18nProvider locale="en">{ui}</I18nProvider>);

/** A single string placeholder with no options yet. */
const PLACEHOLDER: TemplatePlaceholder = {
  key: 'USER_ROLE',
  name: 'User Role',
  description: '',
  type: 'string' as TemplatePlaceholder['type'],
  options: [],
};

describe('PlaceholderEditor options input', () => {
  let onChange: ReturnType<typeof vi.fn>;

  beforeEach(() => {
    onChange = vi.fn();
  });

  it('preserves the raw text (including commas) while typing', async () => {
    const user = userEvent.setup();
    renderEditor(<PlaceholderEditor placeholders={[PLACEHOLDER]} onChange={onChange} />);

    const optionsInput = screen.getByLabelText(OPTIONS_LABEL);
    await user.type(optionsInput, 'ADMIN, DATA_ANALYST');

    // The comma and both option names survive keystroke-by-keystroke.
    expect(optionsInput).toHaveValue('ADMIN, DATA_ANALYST');
  });

  it('commits a parsed options array on blur', async () => {
    const user = userEvent.setup();
    renderEditor(<PlaceholderEditor placeholders={[PLACEHOLDER]} onChange={onChange} />);

    const optionsInput = screen.getByLabelText(OPTIONS_LABEL);
    await user.type(optionsInput, 'ADMIN, DATA_ANALYST');
    await user.tab(); // blur

    expect(onChange).toHaveBeenCalled();
    const updated = onChange.mock.calls.at(-1)![0] as TemplatePlaceholder[];
    expect(updated[0].options).toEqual(['ADMIN', 'DATA_ANALYST']);
  });

  it('does not merge options together while typing a separator', async () => {
    const user = userEvent.setup();
    renderEditor(<PlaceholderEditor placeholders={[PLACEHOLDER]} onChange={onChange} />);

    const optionsInput = screen.getByLabelText(OPTIONS_LABEL);
    // Type a value, a comma, then a second value — the comma must not be dropped.
    await user.type(optionsInput, 'ADMIN,USER');
    expect(optionsInput).toHaveValue('ADMIN,USER');

    await user.tab();
    const updated = onChange.mock.calls.at(-1)![0] as TemplatePlaceholder[];
    expect(updated[0].options).toEqual(['ADMIN', 'USER']);
  });

  it('keeps focus in the Key field while typing (card is not remounted per keystroke)', async () => {
    const user = userEvent.setup();

    // Stateful harness so the placeholder key actually changes on each keystroke,
    // reproducing the condition that previously remounted the card and stole focus.
    const Harness = () => {
      const [phs, setPhs] = useState<TemplatePlaceholder[]>([
        { key: 'AB', name: '', description: '', type: 'string' as TemplatePlaceholder['type'], options: [] },
      ]);
      return <PlaceholderEditor placeholders={phs} onChange={setPhs} />;
    };
    renderEditor(<Harness />);

    const keyInput = screen.getByLabelText('Key 1');
    keyInput.focus();
    await user.keyboard('CD');

    expect(keyInput).toHaveFocus();
    expect(keyInput).toHaveValue('ABCD');
  });
});

describe('PlaceholderEditor detection badges', () => {
  const ph = (key: string): TemplatePlaceholder => ({
    key,
    name: key,
    description: '',
    type: 'string' as TemplatePlaceholder['type'],
    options: [],
  });

  it('flags an unreferenced placeholder as "unused"', () => {
    renderEditor(
      <PlaceholderEditor placeholders={[ph('ORPHAN')]} onChange={vi.fn()} detectedKeys={new Set()} />,
    );
    expect(screen.getByText('unused')).toBeInTheDocument();
    expect(screen.queryByText('auto-detected')).not.toBeInTheDocument();
  });

  it('flags a referenced placeholder as "auto-detected", not "unused"', () => {
    renderEditor(
      <PlaceholderEditor placeholders={[ph('USED')]} onChange={vi.fn()} detectedKeys={new Set(['USED'])} />,
    );
    expect(screen.getByText('auto-detected')).toBeInTheDocument();
    expect(screen.queryByText('unused')).not.toBeInTheDocument();
  });
});
