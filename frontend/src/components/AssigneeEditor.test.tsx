/** Unit tests for the AssigneeEditor component, focused on party collections. */
import { useState } from 'react';
import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import AssigneeEditor from './AssigneeEditor';
import { I18nProvider } from '../i18n';
import type { Mappings } from '../services/api';

const MAPPINGS: Mappings = {
  actions: [],
  operators: [{ name: 'odrl:eq', description: 'Equal to' }],
  leftOperands: [{ name: 'dome-op:role', description: 'Role' }],
  rightOperands: [],
  assignees: [],
  targets: [],
  constraints: [],
};

const Stateful = ({ initial, onWrite }: { initial: unknown; onWrite: (v: unknown) => void }) => {
  const [assignee, setAssignee] = useState(initial);
  return (
    <I18nProvider>
      <AssigneeEditor
        assignee={assignee}
        setAssignee={(v) => {
          onWrite(v);
          setAssignee(v);
        }}
        mappings={MAPPINGS}
      />
    </I18nProvider>
  );
};

const last = (fn: ReturnType<typeof vi.fn>) => fn.mock.calls[fn.mock.calls.length - 1][0];

describe('AssigneeEditor party collections', () => {
  it.each(['odrl:PartyCollection', 'PartyCollection'])('shows the collection builder for %s', (type) => {
    render(<Stateful initial={{ '@type': type, 'odrl:refinement': [] }} onWrite={vi.fn()} />);
    expect(screen.getByRole('radio', { name: 'Party Collection' })).toBeChecked();
    expect(screen.getByRole('button', { name: /add constraint/i })).toBeInTheDocument();
  });

  it('writes a prefixed type when creating a collection', async () => {
    const onWrite = vi.fn();
    render(<Stateful initial="" onWrite={onWrite} />);
    await userEvent.setup().click(screen.getByRole('radio', { name: 'Party Collection' }));
    expect(last(onWrite)).toEqual({ '@type': 'odrl:PartyCollection', 'odrl:refinement': [] });
  });

  it('keeps the loaded type and unknown keys when adding a refinement', async () => {
    const onWrite = vi.fn();
    render(
      <Stateful
        initial={{ '@type': 'PartyCollection', 'odrl:source': 'urn:party', 'odrl:refinement': [] }}
        onWrite={onWrite}
      />,
    );
    const user = userEvent.setup();
    await user.click(screen.getByRole('button', { name: /add constraint/i }));
    await user.selectOptions(screen.getByLabelText(/select left operand 1/i), 'dome-op:role');
    expect(last(onWrite)).toEqual({
      '@type': 'PartyCollection',
      'odrl:source': 'urn:party',
      'odrl:refinement': [{ 'odrl:leftOperand': { '@id': 'dome-op:role' } }],
    });
  });
});
