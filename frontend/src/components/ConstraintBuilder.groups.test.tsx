/**
 * Logical groups and blank constraints (P1-9): a loaded `odrl:and` keeps its shape unless the user
 * changes the grouping, and a constraint with nothing in it is never written to the policy (the
 * backend rejects it with "no left operand").
 */
import { useState } from 'react';
import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import ConstraintBuilder from './ConstraintBuilder';
import { I18nProvider } from '../i18n';
import type { Mappings } from '../services/api';

const MAPPINGS: Mappings = {
  actions: [],
  operators: [
    { name: 'odrl:eq', description: 'eq' },
    { name: 'odrl:neq', description: 'neq' },
  ],
  leftOperands: [{ name: 'dome-op:role', description: 'Role' }],
  rightOperands: [],
  assignees: [],
  targets: [],
  constraints: [],
};

const A = { 'odrl:leftOperand': { '@id': 'dome-op:role' }, 'odrl:operator': { '@id': 'odrl:eq' }, 'odrl:rightOperand': { '@value': 'a' } };
const B = { 'odrl:leftOperand': { '@id': 'dome-op:role' }, 'odrl:operator': { '@id': 'odrl:eq' }, 'odrl:rightOperand': { '@value': 'b' } };

const Stateful = ({ initial, onWrite }: { initial: Record<string, unknown>; onWrite: (v: Record<string, unknown>) => void }) => {
  const [parent, setParent] = useState(initial);
  return (
    <I18nProvider>
      <ConstraintBuilder
        parent={parent}
        setParent={(v) => {
          onWrite(v);
          setParent(v);
        }}
        mappings={MAPPINGS}
      />
    </I18nProvider>
  );
};

const lastConstraint = (onWrite: ReturnType<typeof vi.fn>) => onWrite.mock.calls.at(-1)![0]['odrl:constraint'];

describe('loaded logical groups', () => {
  it.each([
    ['with @type', { '@type': 'odrl:LogicalConstraint', 'odrl:and': [A, B] }],
    ['without @type', { 'odrl:and': [A, B] }],
  ])('an odrl:and %s is shown as a group of two', (_label, group) => {
    render(<Stateful initial={{ 'odrl:constraint': group }} onWrite={vi.fn()} />);
    expect(screen.getByRole('button', { name: /remove.*2/i })).toBeInTheDocument();
  });

  it.each([
    ['with @type', { '@type': 'odrl:LogicalConstraint', 'odrl:and': [A, B] }],
    ['without @type', { 'odrl:and': [A, B] }],
  ])('editing a constraint of an odrl:and %s keeps the odrl:and object', async (_label, group) => {
    const user = userEvent.setup();
    const onWrite = vi.fn();
    render(<Stateful initial={{ 'odrl:constraint': group }} onWrite={onWrite} />);
    await user.selectOptions(screen.getByLabelText(/select operator 2/i), 'odrl:neq');

    const written = lastConstraint(onWrite);
    expect(Array.isArray(written)).toBe(false);
    expect(Object.keys(written)).toEqual(Object.keys(group));
    expect(written['odrl:and'][1]['odrl:operator']).toEqual({ '@id': 'odrl:neq' });
    expect(written['odrl:and'][0]).toEqual(A);
  });

  it('changing the grouping does rewrite it', async () => {
    const user = userEvent.setup();
    const onWrite = vi.fn();
    render(<Stateful initial={{ 'odrl:constraint': { 'odrl:and': [A, B] } }} onWrite={onWrite} />);
    await user.selectOptions(screen.getByLabelText(/constraint logic/i), 'or');

    expect(lastConstraint(onWrite)).toEqual({ '@type': 'odrl:LogicalConstraint', 'odrl:or': [A, B] });
  });

  it('a plain array is still written as an array', async () => {
    const user = userEvent.setup();
    const onWrite = vi.fn();
    render(<Stateful initial={{ 'odrl:constraint': [A, B] }} onWrite={onWrite} />);
    await user.selectOptions(screen.getByLabelText(/select operator 1/i), 'odrl:neq');

    expect(Array.isArray(lastConstraint(onWrite))).toBe(true);
  });
});

describe('blank constraints', () => {
  it('adding a constraint shows a blank row but writes nothing', async () => {
    const user = userEvent.setup();
    const onWrite = vi.fn();
    render(<Stateful initial={{}} onWrite={onWrite} />);
    await user.click(screen.getByRole('button', { name: /add constraint/i }));

    expect(screen.getByLabelText(/select left operand 1/i)).toBeInTheDocument();
    expect(onWrite).not.toHaveBeenCalled();
  });

  it('writes the constraint once the user fills something in', async () => {
    const user = userEvent.setup();
    const onWrite = vi.fn();
    render(<Stateful initial={{}} onWrite={onWrite} />);
    await user.click(screen.getByRole('button', { name: /add constraint/i }));
    await user.selectOptions(screen.getByLabelText(/select left operand 1/i), 'dome-op:role');

    expect(lastConstraint(onWrite)).toEqual([{ 'odrl:leftOperand': { '@id': 'dome-op:role' } }]);
  });

  it('never writes an empty constraint, also next to existing ones', async () => {
    const user = userEvent.setup();
    const onWrite = vi.fn();
    render(<Stateful initial={{ 'odrl:constraint': [A] }} onWrite={onWrite} />);
    await user.click(screen.getByRole('button', { name: /add constraint/i }));
    await user.selectOptions(screen.getByLabelText(/select operator 1/i), 'odrl:neq');

    expect(lastConstraint(onWrite)).toHaveLength(1);
    expect(screen.getByLabelText(/select left operand 2/i)).toBeInTheDocument();
    onWrite.mock.calls.forEach(([value]) => {
      [value['odrl:constraint']].flat().forEach((c: Record<string, unknown>) => expect(Object.keys(c).length).toBeGreaterThan(0));
    });
  });

  it('allows a single blank row at a time', async () => {
    const user = userEvent.setup();
    render(<Stateful initial={{}} onWrite={vi.fn()} />);
    await user.click(screen.getByRole('button', { name: /add constraint/i }));

    expect(screen.getByRole('button', { name: /add constraint/i })).toBeDisabled();
  });

  it('removes the blank row without touching the policy', async () => {
    const user = userEvent.setup();
    render(<Stateful initial={{ 'odrl:constraint': [A] }} onWrite={vi.fn()} />);
    await user.click(screen.getByRole('button', { name: /add constraint/i }));
    await user.click(screen.getByRole('button', { name: /remove.*2/i }));

    expect(screen.queryByLabelText(/select left operand 2/i)).not.toBeInTheDocument();
    expect(screen.getByRole('button', { name: /add constraint/i })).toBeEnabled();
  });
});
