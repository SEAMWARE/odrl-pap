/** Literal right operands (P1-7): an empty datatype is not written as `"@type": ""`. */
import { useState } from 'react';
import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import ConstraintBuilder from './ConstraintBuilder';
import { I18nProvider } from '../i18n';
import type { Mappings } from '../services/api';

const MAPPINGS: Mappings = {
  actions: [],
  operators: [{ name: 'odrl:eq', description: 'eq' }],
  leftOperands: [{ name: 'dome-op:role', description: 'Role' }],
  rightOperands: [],
  assignees: [],
  targets: [],
  constraints: [],
};

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

const constraintWith = (rightOperand: unknown) => ({
  'odrl:constraint': [
    { 'odrl:leftOperand': { '@id': 'dome-op:role' }, 'odrl:operator': { '@id': 'odrl:eq' }, 'odrl:rightOperand': rightOperand },
  ],
});

const lastRight = (onWrite: ReturnType<typeof vi.fn>) =>
  onWrite.mock.calls.at(-1)![0]['odrl:constraint'][0]['odrl:rightOperand'];

describe('right operand literals', () => {
  it('switching to a literal does not write an empty @type', async () => {
    const user = userEvent.setup();
    const onWrite = vi.fn();
    render(<Stateful initial={constraintWith({ '@id': '' })} onWrite={onWrite} />);
    await user.selectOptions(screen.getByLabelText(/right operand type for constraint 1/i), 'literal');

    expect(lastRight(onWrite)).toEqual({ '@value': '' });
    expect(lastRight(onWrite)).not.toHaveProperty('@type');
  });

  it('typing a value does not add an empty @type', async () => {
    const user = userEvent.setup();
    const onWrite = vi.fn();
    render(<Stateful initial={constraintWith({ '@value': '' })} onWrite={onWrite} />);
    await user.type(screen.getByLabelText(/^value for constraint 1$/i), 'admin');

    expect(lastRight(onWrite)).toEqual({ '@value': 'admin' });
  });

  it('keeps a datatype the user typed', async () => {
    const user = userEvent.setup();
    const onWrite = vi.fn();
    render(<Stateful initial={constraintWith({ '@value': '5' })} onWrite={onWrite} />);
    await user.type(screen.getByLabelText(/^type \(e\.g\., xsd:date\) for constraint 1$/i), 'xsd:integer');

    expect(lastRight(onWrite)).toEqual({ '@value': '5', '@type': 'xsd:integer' });
  });

  it('removes the @type again when the user clears it', async () => {
    const user = userEvent.setup();
    const onWrite = vi.fn();
    render(<Stateful initial={constraintWith({ '@value': '5', '@type': 'x' })} onWrite={onWrite} />);
    await user.clear(screen.getByLabelText(/^type \(e\.g\., xsd:date\) for constraint 1$/i));

    expect(lastRight(onWrite)).toEqual({ '@value': '5' });
  });

  it('drops a loaded empty @type when the value is edited', async () => {
    const user = userEvent.setup();
    const onWrite = vi.fn();
    render(<Stateful initial={constraintWith({ '@value': 'a', '@type': '' })} onWrite={onWrite} />);
    await user.type(screen.getByLabelText(/^value for constraint 1$/i), 'b');

    expect(lastRight(onWrite)).toEqual({ '@value': 'ab' });
  });
});
