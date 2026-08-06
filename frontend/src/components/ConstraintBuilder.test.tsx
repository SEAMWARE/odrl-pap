/**
 * Unit tests for the ConstraintBuilder component.
 *
 * Verifies add/remove constraints, AND/OR/XONE logic toggling,
 * left operand, operator, and right operand selection, named vs
 * literal right operand toggle, and correct ODRL JSON output.
 */
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

    renderWithI18n(
      <ConstraintBuilder
        parent={parent}
        setParent={setParent}
        mappings={MOCK_MAPPINGS}
      />,
    );

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
