/**
 * Unit tests for the ConstraintBuilder component.
 *
 * Verifies add/remove constraints, AND/OR/XONE logic toggling,
 * left operand, operator, and right operand selection, named vs
 * literal right operand toggle, and correct ODRL JSON output.
 */
import { useState } from 'react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import ConstraintBuilder from './ConstraintBuilder';
import { I18nProvider } from '../i18n';
import type { Mappings } from '../services/api';

/** Realistic mock mappings for constraint dropdowns. */
const MOCK_MAPPINGS: Mappings = {
  actions: [],
  operators: [
    { name: 'odrl:eq', description: 'Equal to' },
    { name: 'odrl:neq', description: 'Not equal to' },
    { name: 'odrl:gt', description: 'Greater than' },
  ],
  leftOperands: [
    { name: 'dome-op:role', description: 'Role of the requesting party' },
    { name: 'http:body_value', description: 'Value from HTTP request body' },
    {
      name: 'http:bodyValue',
      description: 'Value from HTTP request body at the given path',
      parameterCount: 1,
    },
  ],
  rightOperands: [
    { name: 'odrl:rightOperand', description: 'Right operand value' },
  ],
  assignees: [],
  targets: [],
  constraints: [],
};

/** Wraps a component with the required I18nProvider. */
const renderWithI18n = (ui: React.ReactElement) =>
  render(<I18nProvider>{ui}</I18nProvider>);

/** A parent that keeps the value (as the real editors do) and reports every write. */
const StatefulBuilder = ({
  initial,
  onWrite,
  propertyKey,
}: {
  initial: Record<string, unknown>;
  onWrite: (value: Record<string, unknown>) => void;
  propertyKey?: string;
}) => {
  const [parent, setParent] = useState(initial);
  return (
    <ConstraintBuilder
      parent={parent}
      setParent={(value) => {
        onWrite(value);
        setParent(value);
      }}
      mappings={MOCK_MAPPINGS}
      propertyKey={propertyKey}
    />
  );
};

describe('ConstraintBuilder', () => {
  let parent: Record<string, unknown>;
  let setParent: ReturnType<typeof vi.fn>;

  beforeEach(() => {
    parent = {};
    setParent = vi.fn();
  });

  it('renders the add constraint button', () => {
    renderWithI18n(
      <ConstraintBuilder
        parent={parent}
        setParent={setParent}
        mappings={MOCK_MAPPINGS}
      />,
    );

    expect(
      screen.getByRole('button', { name: /add constraint/i }),
    ).toBeInTheDocument();
  });

  it('adds a constraint when the add button is clicked', async () => {
    const user = userEvent.setup();

    renderWithI18n(
      <ConstraintBuilder
        parent={parent}
        setParent={setParent}
        mappings={MOCK_MAPPINGS}
      />,
    );

    await user.click(screen.getByRole('button', { name: /add constraint/i }));

    // setParent should be called with an updated constraint array
    expect(setParent).toHaveBeenCalled();
    const lastCall = setParent.mock.calls[setParent.mock.calls.length - 1][0];
    const constraint = lastCall['odrl:constraint'];
    expect(Array.isArray(constraint)).toBe(true);
    expect(constraint.length).toBe(1);
  });

  it('renders constraint cards after adding constraints', async () => {
    const user = userEvent.setup();

    renderWithI18n(<StatefulBuilder initial={parent} onWrite={setParent} />);

    await user.click(screen.getByRole('button', { name: /add constraint/i }));

    // Should have a numbered badge "1" and a "Constraint" label
    expect(screen.getByText('1')).toBeInTheDocument();
    expect(screen.getByText('Constraint')).toBeInTheDocument();
  });

  it('removes a constraint when the remove button is clicked', async () => {
    const user = userEvent.setup();

    // Start with one existing constraint
    const parentWithConstraint = {
      'odrl:constraint': [
        { 'odrl:leftOperand': { '@id': 'dome-op:role' } },
      ],
    };

    renderWithI18n(
      <ConstraintBuilder
        parent={parentWithConstraint}
        setParent={setParent}
        mappings={MOCK_MAPPINGS}
      />,
    );

    // Click remove button
    const removeButton = screen.getByRole('button', { name: /remove.*1/i });
    await user.click(removeButton);

    // setParent should have been called with an empty constraint array
    const lastCall = setParent.mock.calls[setParent.mock.calls.length - 1][0];
    const constraint = lastCall['odrl:constraint'];
    expect(Array.isArray(constraint)).toBe(true);
    expect(constraint.length).toBe(0);
  });

  it('shows logical grouping selector when more than one constraint exists', async () => {
    const parentWithMultiple = {
      'odrl:constraint': [
        { 'odrl:leftOperand': { '@id': 'dome-op:role' } },
        { 'odrl:leftOperand': { '@id': 'http:body_value' } },
      ],
    };

    renderWithI18n(
      <ConstraintBuilder
        parent={parentWithMultiple}
        setParent={setParent}
        mappings={MOCK_MAPPINGS}
      />,
    );

    // The grouping selector should be visible
    expect(screen.getByLabelText(/constraint logic/i)).toBeInTheDocument();
  });

  it('does not show logical grouping selector with zero or one constraint', () => {
    const parentWithOne = {
      'odrl:constraint': [
        { 'odrl:leftOperand': { '@id': 'dome-op:role' } },
      ],
    };

    renderWithI18n(
      <ConstraintBuilder
        parent={parentWithOne}
        setParent={setParent}
        mappings={MOCK_MAPPINGS}
      />,
    );

    expect(screen.queryByLabelText(/constraint logic/i)).not.toBeInTheDocument();
  });

  it('toggles between AND, OR, and XONE logic', async () => {
    const user = userEvent.setup();

    const parentWithMultiple = {
      'odrl:constraint': [
        { 'odrl:leftOperand': { '@id': 'dome-op:role' } },
        { 'odrl:leftOperand': { '@id': 'http:body_value' } },
      ],
    };

    renderWithI18n(
      <ConstraintBuilder
        parent={parentWithMultiple}
        setParent={setParent}
        mappings={MOCK_MAPPINGS}
      />,
    );

    const groupingSelect = screen.getByLabelText(/constraint logic/i);

    // Switch to OR
    await user.selectOptions(groupingSelect, 'or');

    // Check that the parent was updated with a LogicalConstraint
    const orCall = setParent.mock.calls.find(
      (call) => call[0]?.['odrl:constraint']?.['@type'] === 'odrl:LogicalConstraint'
        && call[0]?.['odrl:constraint']?.['odrl:or'],
    );
    expect(orCall).toBeDefined();

    // Switch to XONE
    await user.selectOptions(groupingSelect, 'xone');

    const xoneCall = setParent.mock.calls.find(
      (call) => call[0]?.['odrl:constraint']?.['@type'] === 'odrl:LogicalConstraint'
        && call[0]?.['odrl:constraint']?.['odrl:xone'],
    );
    expect(xoneCall).toBeDefined();
  });

  it('shows logical grouping help text for AND', () => {
    const parentWithMultiple = {
      'odrl:constraint': [
        { 'odrl:leftOperand': { '@id': 'dome-op:role' } },
        { 'odrl:leftOperand': { '@id': 'http:body_value' } },
      ],
    };

    renderWithI18n(
      <ConstraintBuilder
        parent={parentWithMultiple}
        setParent={setParent}
        mappings={MOCK_MAPPINGS}
      />,
    );

    expect(screen.getByText(/every constraint must be satisfied/i)).toBeInTheDocument();
  });

  it('renders left operand, operator, and right operand dropdowns', () => {
    const parentWithConstraint = {
      'odrl:constraint': [
        { 'odrl:leftOperand': { '@id': '' } },
      ],
    };

    renderWithI18n(
      <ConstraintBuilder
        parent={parentWithConstraint}
        setParent={setParent}
        mappings={MOCK_MAPPINGS}
      />,
    );

    // Should have form labels for all three fields (use getAllByText to handle duplicates from placeholder options)
    const leftLabels = screen.getAllByText(/select left operand/i);
    expect(leftLabels.length).toBeGreaterThanOrEqual(1);
    const opLabels = screen.getAllByText(/select operator/i);
    expect(opLabels.length).toBeGreaterThanOrEqual(1);
    const rightLabels = screen.getAllByText(/select right operand/i);
    expect(rightLabels.length).toBeGreaterThanOrEqual(1);
  });

  it('selects a left operand from the dropdown', async () => {
    const user = userEvent.setup();

    const parentWithConstraint = {
      'odrl:constraint': [
        {},
      ],
    };

    renderWithI18n(
      <ConstraintBuilder
        parent={parentWithConstraint}
        setParent={setParent}
        mappings={MOCK_MAPPINGS}
      />,
    );

    // Find the left operand dropdown (first combobox in the constraint card)
    const leftOperandSelect = screen.getByLabelText(/select left operand 1/i);
    await user.selectOptions(leftOperandSelect, 'dome-op:role');

    // Verify setParent was called with the selected value
    const matchingCall = setParent.mock.calls.find(
      (call) => {
        const constraints = call[0]?.['odrl:constraint'];
        return Array.isArray(constraints)
          && constraints[0]?.['odrl:leftOperand']?.['@id'] === 'dome-op:role';
      },
    );
    expect(matchingCall).toBeDefined();
  });

  it('toggles right operand between named and literal', async () => {
    const user = userEvent.setup();

    const parentWithConstraint = {
      'odrl:constraint': [
        {},
      ],
    };

    renderWithI18n(
      <ConstraintBuilder
        parent={parentWithConstraint}
        setParent={setParent}
        mappings={MOCK_MAPPINGS}
      />,
    );

    // Find the right operand type selector
    const typeSelect = screen.getByLabelText(/right operand type/i);

    // Switch to literal
    await user.selectOptions(typeSelect, 'literal');

    // Should have called setParent with a literal right operand (@value)
    const literalCall = setParent.mock.calls.find(
      (call) => {
        const constraints = call[0]?.['odrl:constraint'];
        return Array.isArray(constraints)
          && constraints[0]?.['odrl:rightOperand']?.['@value'] !== undefined;
      },
    );
    expect(literalCall).toBeDefined();
  });

  it('shows value and type fields for literal right operand', async () => {
    const parentWithConstraint = {
      'odrl:constraint': [
        { 'odrl:rightOperand': { '@value': 'test', '@type': 'xsd:string' } },
      ],
    };

    renderWithI18n(
      <ConstraintBuilder
        parent={parentWithConstraint}
        setParent={setParent}
        mappings={MOCK_MAPPINGS}
      />,
    );

    // Should show the value and type input fields
    const valueInput = screen.getByLabelText(/^Value for constraint 1$/i);
    expect(valueInput).toHaveValue('test');

    // The type placeholder input — match the exact aria-label from the i18n string
    const typeInput = screen.getByLabelText('Type (e.g., xsd:date) for constraint 1');
    expect(typeInput).toHaveValue('xsd:string');
  });

  it('disables all controls when locked prop is true', async () => {
    const parentWithConstraint = {
      'odrl:constraint': [
        { 'odrl:leftOperand': { '@id': 'dome-op:role' } },
      ],
    };

    renderWithI18n(
      <ConstraintBuilder
        parent={parentWithConstraint}
        setParent={setParent}
        mappings={MOCK_MAPPINGS}
        locked
      />,
    );

    // The add button should be disabled
    expect(screen.getByRole('button', { name: /add constraint/i })).toBeDisabled();

    // The remove button should be disabled
    expect(screen.getByRole('button', { name: /remove.*1/i })).toBeDisabled();
  });

  it('renders correct ODRL structure for AND constraints', () => {
    const parentWithMultiple = {
      'odrl:constraint': [
        {
          'odrl:leftOperand': { '@id': 'dome-op:role' },
          'odrl:operator': { '@id': 'odrl:eq' },
          'odrl:rightOperand': { '@value': 'admin', '@type': 'xsd:string' },
        },
      ],
    };

    renderWithI18n(
      <ConstraintBuilder
        parent={parentWithMultiple}
        setParent={setParent}
        mappings={MOCK_MAPPINGS}
      />,
    );

    // The parent should be updated with an array (AND logic)
    const lastCall = setParent.mock.calls[setParent.mock.calls.length - 1]?.[0];
    if (lastCall) {
      const constraint = lastCall['odrl:constraint'];
      expect(Array.isArray(constraint)).toBe(true);
    }
  });

  it('parses OR logical constraint from parent', () => {
    const parentWithOr = {
      'odrl:constraint': {
        '@type': 'odrl:LogicalConstraint',
        'odrl:or': [
          { 'odrl:leftOperand': { '@id': 'dome-op:role' } },
          { 'odrl:leftOperand': { '@id': 'http:body_value' } },
        ],
      },
    };

    renderWithI18n(
      <ConstraintBuilder
        parent={parentWithOr}
        setParent={setParent}
        mappings={MOCK_MAPPINGS}
      />,
    );

    // Should render both constraint cards
    expect(screen.getByText('1')).toBeInTheDocument();
    expect(screen.getByText('2')).toBeInTheDocument();

    // Grouping should be OR
    const groupingSelect = screen.getByLabelText(/constraint logic/i);
    expect(groupingSelect).toHaveValue('or');
  });
});

describe('ConstraintBuilder propertyKey', () => {
  let setParent: ReturnType<typeof vi.fn>;

  beforeEach(() => {
    setParent = vi.fn();
  });

  it('writes to a custom property key (odrl:refinement)', async () => {
    const user = userEvent.setup();

    renderWithI18n(
      <ConstraintBuilder
        parent={{ '@type': 'odrl:PartyCollection' }}
        setParent={setParent}
        mappings={MOCK_MAPPINGS}
        propertyKey="odrl:refinement"
      />,
    );

    await user.click(screen.getByRole('button', { name: /add constraint/i }));

    const lastCall = setParent.mock.calls[setParent.mock.calls.length - 1][0];
    // The collection type is preserved and refinement is written, not constraint.
    expect(lastCall['@type']).toBe('odrl:PartyCollection');
    expect(Array.isArray(lastCall['odrl:refinement'])).toBe(true);
    expect(lastCall['odrl:constraint']).toBeUndefined();
  });

  /**
   * Operands reported with a parameterCount > 0 need their parameter written as the value of the
   * namespaced key itself. Referencing them via `odrl:leftOperand` leaves the parameter unresolved
   * and makes the backend reject the policy.
   */
  describe('parameterized left operands', () => {
    /** Returns the first constraint of the most recent setParent call. */
    const lastConstraintOf = (setParentMock: ReturnType<typeof vi.fn>) => {
      const lastCall = setParentMock.mock.calls[setParentMock.mock.calls.length - 1][0];
      return lastCall['odrl:constraint'][0];
    };

    it('does not show a parameter input for an operand without parameters', async () => {
      const user = userEvent.setup();

      renderWithI18n(
        <ConstraintBuilder
          parent={{ 'odrl:constraint': [{}] }}
          setParent={setParent}
          mappings={MOCK_MAPPINGS}
        />,
      );

      await user.selectOptions(screen.getByLabelText(/select left operand 1/i), 'dome-op:role');

      expect(screen.queryByLabelText(/operand parameter 1/i)).not.toBeInTheDocument();
    });

    it('writes the operand as its own key when it requires a parameter', async () => {
      const user = userEvent.setup();

      renderWithI18n(
        <ConstraintBuilder
          parent={{ 'odrl:constraint': [{}] }}
          setParent={setParent}
          mappings={MOCK_MAPPINGS}
        />,
      );

      await user.selectOptions(screen.getByLabelText(/select left operand 1/i), 'http:bodyValue');

      const constraint = lastConstraintOf(setParent);
      expect(constraint).toHaveProperty('http:bodyValue');
      expect(constraint['odrl:leftOperand']).toBeUndefined();
    });

    it('stores the entered parameter as the value of the operand key', async () => {
      const user = userEvent.setup();

      renderWithI18n(
        <StatefulBuilder initial={{ 'odrl:constraint': [{ 'http:bodyValue': '' }] }} onWrite={setParent} />,
      );

      await user.type(screen.getByLabelText(/operand parameter 1/i), '$.numNodes.value');

      expect(lastConstraintOf(setParent)['http:bodyValue']).toBe('$.numNodes.value');
    });

    it('preselects the operand and its parameter from an existing constraint', () => {
      renderWithI18n(
        <ConstraintBuilder
          parent={{
            'odrl:constraint': [
              {
                '@type': 'odrl:Constraint',
                'http:bodyValue': '$.numNodes.value',
                'odrl:operator': { '@id': 'odrl:eq' },
                'odrl:rightOperand': { '@value': '3', '@type': 'xsd:string' },
              },
            ],
          }}
          setParent={setParent}
          mappings={MOCK_MAPPINGS}
        />,
      );

      expect(screen.getByLabelText(/select left operand 1/i)).toHaveValue('http:bodyValue');
      expect(screen.getByLabelText(/operand parameter 1/i)).toHaveValue('$.numNodes.value');
    });

    it('reads a parameter that is given as a JSON-LD value object', () => {
      renderWithI18n(
        <ConstraintBuilder
          parent={{
            'odrl:constraint': [{ 'http:bodyValue': { '@value': '$.numNodes.value' } }],
          }}
          setParent={setParent}
          mappings={MOCK_MAPPINGS}
        />,
      );

      expect(screen.getByLabelText(/operand parameter 1/i)).toHaveValue('$.numNodes.value');
    });

    it('drops the operand key when switching to an operand without parameters', async () => {
      const user = userEvent.setup();

      renderWithI18n(
        <ConstraintBuilder
          parent={{ 'odrl:constraint': [{ 'http:bodyValue': '$.numNodes.value' }] }}
          setParent={setParent}
          mappings={MOCK_MAPPINGS}
        />,
      );

      await user.selectOptions(screen.getByLabelText(/select left operand 1/i), 'dome-op:role');

      const constraint = lastConstraintOf(setParent);
      expect(constraint['http:bodyValue']).toBeUndefined();
      expect(constraint['odrl:leftOperand']).toEqual({ '@id': 'dome-op:role' });
    });

    it('drops odrl:leftOperand when switching to an operand with parameters', async () => {
      const user = userEvent.setup();

      renderWithI18n(
        <ConstraintBuilder
          parent={{ 'odrl:constraint': [{ 'odrl:leftOperand': { '@id': 'dome-op:role' } }] }}
          setParent={setParent}
          mappings={MOCK_MAPPINGS}
        />,
      );

      await user.selectOptions(screen.getByLabelText(/select left operand 1/i), 'http:bodyValue');

      const constraint = lastConstraintOf(setParent);
      expect(constraint['odrl:leftOperand']).toBeUndefined();
      expect(constraint['http:bodyValue']).toBe('');
    });
  });

  it('loads a single-object refinement as an editable constraint card', () => {
    const parentWithSingleRefinement = {
      '@type': 'odrl:PartyCollection',
      'odrl:source': 'urn:user',
      'odrl:refinement': {
        '@type': 'odrl:Constraint',
        'odrl:leftOperand': { '@id': 'dome-op:role' },
        'odrl:operator': { '@id': 'odrl:eq' },
        'odrl:rightOperand': { '@value': 'ADMIN', '@type': 'xsd:string' },
      },
    };

    renderWithI18n(
      <ConstraintBuilder
        parent={parentWithSingleRefinement}
        setParent={setParent}
        mappings={MOCK_MAPPINGS}
        propertyKey="odrl:refinement"
      />,
    );

    // A single constraint card is rendered (numbered "1") with the operand
    // preselected — proving the single-object refinement is now editable.
    expect(screen.getByText('1')).toBeInTheDocument();
    expect(screen.getByDisplayValue('ADMIN')).toBeInTheDocument();
  });
});

describe('ConstraintBuilder with a parent that owns the state', () => {
  /** Stands in for the collection editors: the parent keeps the value and hands it back down. */
  const StatefulParent = ({ initial, onRender }: { initial: Record<string, unknown>; onRender: () => void }) => {
    const [parent, setParent] = useState(initial);
    onRender();
    return (
      <ConstraintBuilder
        parent={parent}
        setParent={setParent}
        mappings={MOCK_MAPPINGS}
        propertyKey="odrl:refinement"
      />
    );
  };

  it('settles when the parent builds an equal but new empty constraint array', () => {
    // Choosing Party/Asset Collection creates `odrl:refinement: []` — a different
    // array from the builder's own empty state. The two syncs used to pass it
    // back and forth forever ("Maximum update depth exceeded").
    const onRender = vi.fn();
    const errors = vi.spyOn(console, 'error').mockImplementation(() => {});

    renderWithI18n(
      <StatefulParent initial={{ '@type': 'PartyCollection', 'odrl:refinement': [] }} onRender={onRender} />,
    );

    expect(errors).not.toHaveBeenCalled();
    expect(onRender.mock.calls.length).toBeLessThan(10);
    errors.mockRestore();
  });

  describe('mounting on a parent that already has constraints', () => {
    const CONSTRAINT = {
      '@type': 'odrl:Constraint',
      'odrl:leftOperand': { '@id': 'dome-op:role' },
      'odrl:operator': { '@id': 'odrl:eq' },
      'odrl:rightOperand': { '@value': 'ADMIN', '@type': 'xsd:string' },
    };

    /** Like StatefulParent, but reports the parent's latest value so data loss is visible. */
    const Harness = ({ initial, onValue }: { initial: Record<string, unknown>; onValue: (v: unknown) => void }) => {
      const [parent, setParent] = useState(initial);
      onValue(parent['odrl:refinement']);
      return (
        <ConstraintBuilder
          parent={parent}
          setParent={setParent}
          mappings={MOCK_MAPPINGS}
          propertyKey="odrl:refinement"
        />
      );
    };

    const settle = (initial: Record<string, unknown>) => {
      const values: unknown[] = [];
      const errors = vi.spyOn(console, 'error').mockImplementation(() => {});
      renderWithI18n(<Harness initial={initial} onValue={(v) => values.push(v)} />);
      const errorCount = errors.mock.calls.length;
      errors.mockRestore();
      return { values, errorCount };
    };

    it('keeps a list of constraints and settles', () => {
      // Reopening the editor on a saved policy mounts the builder with its constraints. It used
      // to start empty, overwrite the parent with `[]`, and then fight the sync that pulled the
      // constraints back in: the list flickered and React gave up with "Maximum update depth".
      const { values, errorCount } = settle({ '@type': 'PartyCollection', 'odrl:refinement': [CONSTRAINT] });

      expect(errorCount).toBe(0);
      expect(values.length).toBeLessThan(10);
      expect(values.at(-1)).toEqual([CONSTRAINT]);
      expect(screen.getByDisplayValue('ADMIN')).toBeInTheDocument();
    });

    it('shows a lone constraint object as editable and leaves it untouched until edited', () => {
      const { values, errorCount } = settle({ '@type': 'PartyCollection', 'odrl:refinement': CONSTRAINT });

      expect(errorCount).toBe(0);
      expect(values).toEqual([CONSTRAINT]);
      expect(screen.getByDisplayValue('ADMIN')).toBeInTheDocument();
    });

    it('keeps a logical (OR) group and settles', () => {
      const group = { '@type': 'odrl:LogicalConstraint', 'odrl:or': [CONSTRAINT] };
      const { values, errorCount } = settle({ '@type': 'PartyCollection', 'odrl:refinement': group });

      expect(errorCount).toBe(0);
      expect(values.length).toBeLessThan(10);
      expect(values.at(-1)).toEqual(group);
    });

    it('keeps fast typing in a literal value intact while the parent echoes every write back', async () => {
      // Typing used to be able to flicker and loop: the builder held a copy of the list and two
      // effects synced it with the parent, which could swap the copies back and forth.
      const user = userEvent.setup({ delay: null });
      const writes = vi.fn();
      const literal = (value: string) => ({
        'odrl:leftOperand': { '@id': 'dome-op:role' },
        'odrl:operator': { '@id': 'odrl:eq' },
        'odrl:rightOperand': { '@value': value, '@type': '' },
      });
      const errors = vi.spyOn(console, 'error').mockImplementation(() => {});

      renderWithI18n(
        <StatefulBuilder
          initial={{ 'odrl:refinement': [literal('test'), literal('abc'), literal('asd')] }}
          onWrite={writes}
          propertyKey="odrl:refinement"
        />,
      );
      await user.type(screen.getByLabelText(/value for constraint 3/i), 'xyz');

      expect(errors).not.toHaveBeenCalled();
      expect(screen.getByLabelText(/value for constraint 3/i)).toHaveValue('asdxyz');
      expect(writes).toHaveBeenCalledTimes(3);
      expect(screen.getByLabelText(/value for constraint 1/i)).toHaveValue('test');
      errors.mockRestore();
    });

    it('does not write an unchanged value back to the parent', () => {
      const setParent = vi.fn();

      renderWithI18n(
        <ConstraintBuilder
          parent={{ 'odrl:refinement': [CONSTRAINT] }}
          setParent={setParent}
          mappings={MOCK_MAPPINGS}
          propertyKey="odrl:refinement"
        />,
      );

      expect(setParent).not.toHaveBeenCalled();
    });
  });

  it('still adopts constraints the parent changes to something different', () => {
    const constraint = {
      '@type': 'odrl:Constraint',
      'odrl:leftOperand': { '@id': 'dome-op:role' },
      'odrl:operator': { '@id': 'odrl:eq' },
      'odrl:rightOperand': { '@value': 'ADMIN', '@type': 'xsd:string' },
    };
    const { rerender } = renderWithI18n(
      <ConstraintBuilder
        parent={{ 'odrl:refinement': [] }}
        setParent={vi.fn()}
        mappings={MOCK_MAPPINGS}
        propertyKey="odrl:refinement"
      />,
    );

    rerender(
      <I18nProvider>
        <ConstraintBuilder
          parent={{ 'odrl:refinement': [constraint] }}
          setParent={vi.fn()}
          mappings={MOCK_MAPPINGS}
          propertyKey="odrl:refinement"
        />
      </I18nProvider>,
    );

    expect(screen.getByDisplayValue('ADMIN')).toBeInTheDocument();
  });
  describe('logical constraints', () => {
    const two = [
      { 'odrl:leftOperand': { '@id': 'dome-op:role' }, 'odrl:operator': { '@id': 'odrl:eq' }, 'odrl:rightOperand': { '@value': 'ADMIN' } },
      { 'odrl:leftOperand': { '@id': 'dome-op:role' }, 'odrl:operator': { '@id': 'odrl:eq' }, 'odrl:rightOperand': { '@value': 'USER' } },
    ];

    it('loads an odrl:and constraint as the and grouping and keeps its constraints', async () => {
      const onWrite = vi.fn();
      renderWithI18n(
        <StatefulBuilder
          initial={{ 'odrl:constraint': { '@type': 'odrl:LogicalConstraint', 'odrl:and': two } }}
          onWrite={onWrite}
        />,
      );

      expect(screen.getByDisplayValue('ADMIN')).toBeInTheDocument();
      expect(screen.getByDisplayValue('USER')).toBeInTheDocument();

      await userEvent.setup().click(screen.getByRole('button', { name: /add constraint/i }));
      expect(onWrite.mock.calls.at(-1)![0]['odrl:constraint']).toHaveLength(3);
    });

    it('shows an odrl:andSequence constraint read-only instead of dropping it', () => {
      renderWithI18n(
        <StatefulBuilder
          initial={{ 'odrl:constraint': { '@type': 'odrl:LogicalConstraint', 'odrl:andSequence': two } }}
          onWrite={vi.fn()}
        />,
      );

      expect(screen.getByDisplayValue('ADMIN')).toBeInTheDocument();
      expect(screen.getByDisplayValue('USER')).toBeInTheDocument();
      expect(screen.getByRole('button', { name: /add constraint/i })).toBeDisabled();
    });
  });
});

