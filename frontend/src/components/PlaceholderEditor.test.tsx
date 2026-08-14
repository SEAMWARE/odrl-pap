/**
 * Unit tests for the PlaceholderEditor component.
 *
 * Focus: the dropdown-options input must let the user type a
 * comma-separated list by hand. Previously the value was re-parsed on every
 * keystroke, which dropped the just-typed comma and merged options together.
 */
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import PlaceholderEditor from './PlaceholderEditor';
import type { TemplatePlaceholder } from '../api/models/TemplatePlaceholder';

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
    render(<PlaceholderEditor placeholders={[PLACEHOLDER]} onChange={onChange} />);

    const optionsInput = screen.getByLabelText('Placeholder options 1');
    await user.type(optionsInput, 'ADMIN, DATA_ANALYST');

    // The comma and both option names survive keystroke-by-keystroke.
    expect(optionsInput).toHaveValue('ADMIN, DATA_ANALYST');
  });

  it('commits a parsed options array on blur', async () => {
    const user = userEvent.setup();
    render(<PlaceholderEditor placeholders={[PLACEHOLDER]} onChange={onChange} />);

    const optionsInput = screen.getByLabelText('Placeholder options 1');
    await user.type(optionsInput, 'ADMIN, DATA_ANALYST');
    await user.tab(); // blur

    expect(onChange).toHaveBeenCalled();
    const updated = onChange.mock.calls.at(-1)![0] as TemplatePlaceholder[];
    expect(updated[0].options).toEqual(['ADMIN', 'DATA_ANALYST']);
  });

  it('does not merge options together while typing a separator', async () => {
    const user = userEvent.setup();
    render(<PlaceholderEditor placeholders={[PLACEHOLDER]} onChange={onChange} />);

    const optionsInput = screen.getByLabelText('Placeholder options 1');
    // Type a value, a comma, then a second value — the comma must not be dropped.
    await user.type(optionsInput, 'ADMIN,USER');
    expect(optionsInput).toHaveValue('ADMIN,USER');

    await user.tab();
    const updated = onChange.mock.calls.at(-1)![0] as TemplatePlaceholder[];
    expect(updated[0].options).toEqual(['ADMIN', 'USER']);
  });
});
