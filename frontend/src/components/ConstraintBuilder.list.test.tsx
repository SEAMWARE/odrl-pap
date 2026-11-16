/**
 * ConstraintBuilder with constraints written as a JSON-LD `@list` (P1-2): the list is read as the
 * group of constraints it is, and a list written by the user is kept as a list on edit.
 */
import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import ConstraintBuilder from './ConstraintBuilder';
import { I18nProvider } from '../i18n';
import type { Mappings } from '../services/api';

const MOCK_MAPPINGS: Mappings = {
  actions: [],
  operators: [{ name: 'odrl:eq', description: 'Equal to' }],
  leftOperands: [{ name: 'dome-op:role', description: 'Role' }],
  rightOperands: [],
  assignees: [],
  targets: [],
  constraints: [],
};

const A = { 'odrl:leftOperand': { '@id': 'dome-op:role' }, 'odrl:operator': { '@id': 'odrl:eq' }, 'odrl:rightOperand': { '@value': 'a' } };
const B = { 'odrl:leftOperand': { '@id': 'dome-op:role' }, 'odrl:operator': { '@id': 'odrl:eq' }, 'odrl:rightOperand': { '@value': 'b' } };

const renderBuilder = (parent: Record<string, unknown>, setParent = vi.fn()) => {
  render(
    <I18nProvider>
      <ConstraintBuilder parent={parent} setParent={setParent} mappings={MOCK_MAPPINGS} />
    </I18nProvider>,
  );
  return setParent;
};

describe('ConstraintBuilder with @list constraints', () => {
  it('shows each constraint of an @list as its own card', () => {
    renderBuilder({ 'odrl:constraint': { '@list': [A, B] } });
    expect(screen.getByRole('button', { name: /remove.*1/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /remove.*2/i })).toBeInTheDocument();
    expect(screen.getByLabelText(/constraint logic/i)).toBeInTheDocument();
  });

  it('reads an @list inside a logical constraint', () => {
    renderBuilder({
      'odrl:constraint': { '@type': 'odrl:LogicalConstraint', 'odrl:or': { '@list': [A, B] } },
    });
    expect(screen.getByRole('button', { name: /remove.*2/i })).toBeInTheDocument();
  });

  it('keeps the @list wrapper when a constraint is removed', async () => {
    const user = userEvent.setup();
    const setParent = renderBuilder({ 'odrl:constraint': { '@list': [A, B] } });
    await user.click(screen.getByRole('button', { name: /remove.*1/i }));

    expect(setParent.mock.calls.at(-1)![0]['odrl:constraint']).toEqual({ '@list': [B] });
  });

  it('does not show garbage for empty or invalid constraint values', () => {
    expect(() => renderBuilder({ 'odrl:constraint': { '@list': [] } })).not.toThrow();
    expect(screen.queryByRole('button', { name: /remove.*1/i })).not.toBeInTheDocument();
  });

  it('ignores null entries inside the list instead of crashing', () => {
    expect(() => renderBuilder({ 'odrl:constraint': { '@list': [A, null] } })).not.toThrow();
    expect(screen.getByRole('button', { name: /remove.*1/i })).toBeInTheDocument();
  });
});
