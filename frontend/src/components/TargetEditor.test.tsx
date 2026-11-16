/**
 * Unit tests for the TargetEditor component, focused on asset collections:
 * prefixed/bare detection, creation, and refinement editing that must keep
 * the loaded type, `odrl:source`, unknown keys and the refinement shape.
 */
import { useState } from 'react';
import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import TargetEditor from './TargetEditor';
import { I18nProvider } from '../i18n';
import type { Mappings } from '../services/api';

const MAPPINGS: Mappings = {
  actions: [],
  operators: [{ name: 'odrl:eq', description: 'Equal to' }],
  leftOperands: [{ name: 'http:path', description: 'Request path' }],
  rightOperands: [],
  assignees: [],
  targets: [],
  constraints: [],
};

const SINGLE_REFINEMENT = {
  '@type': 'odrl:Constraint',
  'odrl:leftOperand': 'http:path',
  'odrl:operator': { '@id': 'odrl:eq' },
  'odrl:rightOperand': '/items',
};

/** Keeps the target like the real editor and reports every write. */
const Stateful = ({ initial, onWrite }: { initial: unknown; onWrite: (v: unknown) => void }) => {
  const [target, setTarget] = useState(initial);
  return (
    <I18nProvider>
      <TargetEditor
        target={target}
        setTarget={(v) => {
          onWrite(v);
          setTarget(v);
        }}
        mappings={MAPPINGS}
      />
    </I18nProvider>
  );
};

const last = (fn: ReturnType<typeof vi.fn>) => fn.mock.calls[fn.mock.calls.length - 1][0];

describe('TargetEditor asset collections', () => {
  it.each(['odrl:AssetCollection', 'AssetCollection'])('shows the collection builder for %s', (type) => {
    render(
      <Stateful initial={{ '@type': type, 'odrl:refinement': [] }} onWrite={vi.fn()} />,
    );
    expect(screen.getByRole('radio', { name: 'Asset Collection' })).toBeChecked();
    expect(screen.getByRole('button', { name: /add constraint/i })).toBeInTheDocument();
    expect(screen.queryByDisplayValue('[object Object]')).not.toBeInTheDocument();
  });

  it('shows a prefixed collection with a single-object refinement without "[object Object]"', () => {
    render(
      <Stateful
        initial={{
          '@type': 'odrl:AssetCollection',
          'odrl:source': 'urn:asset',
          'odrl:refinement': SINGLE_REFINEMENT,
        }}
        onWrite={vi.fn()}
      />,
    );
    expect(screen.getByRole('radio', { name: 'Asset Collection' })).toBeChecked();
    expect(screen.getByRole('radio', { name: 'Simple Target' })).not.toBeChecked();
    expect(screen.queryByDisplayValue('[object Object]')).not.toBeInTheDocument();
    // the plain-string right operand is shown, not lost
    expect(screen.getByDisplayValue('/items')).toBeInTheDocument();
  });

  it('writes a prefixed type and the reference source when creating a collection', async () => {
    const onWrite = vi.fn();
    render(<Stateful initial="" onWrite={onWrite} />);
    await userEvent.setup().click(screen.getByRole('radio', { name: 'Asset Collection' }));
    expect(last(onWrite)).toEqual({
      '@type': 'odrl:AssetCollection',
      'odrl:source': 'urn:asset',
      'odrl:refinement': [],
    });
  });

  it('does not touch a loaded policy until the user edits it', () => {
    const onWrite = vi.fn();
    render(
      <Stateful
        initial={{ '@type': 'odrl:AssetCollection', 'odrl:refinement': SINGLE_REFINEMENT }}
        onWrite={onWrite}
      />,
    );
    expect(onWrite).not.toHaveBeenCalled();
  });

  it('keeps the loaded type, source and unknown keys when adding a refinement', async () => {
    const onWrite = vi.fn();
    render(
      <Stateful
        initial={{
          '@type': 'AssetCollection',
          'odrl:source': 'urn:custom',
          'odrl:uid': 'urn:uid:1',
          'odrl:refinement': SINGLE_REFINEMENT,
        }}
        onWrite={onWrite}
      />,
    );
    const user = userEvent.setup();
    await user.click(screen.getByRole('button', { name: /add constraint/i }));
    await user.selectOptions(screen.getByLabelText(/select left operand 2/i), 'http:path');
    const written = last(onWrite) as Record<string, unknown>;
    expect(written['@type']).toBe('AssetCollection');
    expect(written['odrl:source']).toBe('urn:custom');
    expect(written['odrl:uid']).toBe('urn:uid:1');
    // the single refinement is normalised to the list the backend accepts, nothing lost
    expect(written['odrl:refinement']).toEqual([SINGLE_REFINEMENT, { 'odrl:leftOperand': { '@id': 'http:path' } }]);
  });

  it('keeps plain-string operands and the single constraint when editing its value', async () => {
    const onWrite = vi.fn();
    render(
      <Stateful
        initial={{
          '@type': 'odrl:AssetCollection',
          'odrl:source': 'urn:asset',
          'odrl:refinement': SINGLE_REFINEMENT,
        }}
        onWrite={onWrite}
      />,
    );
    const user = userEvent.setup();
    await user.type(screen.getByDisplayValue('/items'), 'x');
    const written = last(onWrite) as Record<string, unknown>;
    expect(written['@type']).toBe('odrl:AssetCollection');
    expect(written['odrl:source']).toBe('urn:asset');
    expect(written['odrl:refinement']).toEqual([
      {
        ...SINGLE_REFINEMENT,
        'odrl:rightOperand': { '@value': '/itemsx' },
      },
    ]);
  });
});
