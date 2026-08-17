/**
 * Unit tests for the TemplateFiller component.
 *
 * Focus: the policy derived from a template must always carry an `odrl:uid`
 * so it is valid for validation (which happens before the policy is created)
 * and for the eventual create request.
 */
import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import TemplateFiller from './TemplateFiller';
import { I18nProvider } from '../i18n';
import type { Template } from '../types/TemplateTypes';

/** A minimal template whose skeleton does NOT define an odrl:uid. */
const TEMPLATE: Template = {
  id: 't1',
  name: 'Test',
  description: 'Test template',
  odrl: {
    '@type': 'odrl:Policy',
    'odrl:permission': {
      'odrl:target': '{{TARGET}}',
      'odrl:action': { '@id': 'odrl:read' },
    },
  } as unknown as Template['odrl'],
  placeholders: [
    { key: 'TARGET', name: 'Target', type: 'string' as Template['placeholders'][number]['type'] },
  ],
};

function renderFiller(props: Partial<React.ComponentProps<typeof TemplateFiller>> = {}) {
  const onCreatePolicy = vi.fn();
  const onPolicyChange = vi.fn();
  render(
    <I18nProvider locale="en">
      <TemplateFiller
        template={TEMPLATE}
        onCreatePolicy={onCreatePolicy}
        onPolicyChange={onPolicyChange}
        {...props}
      />
    </I18nProvider>,
  );
  return { onCreatePolicy, onPolicyChange };
}

describe('TemplateFiller UID handling', () => {
  it('reports a policy with an odrl:uid via onPolicyChange (used for validation)', () => {
    const { onPolicyChange } = renderFiller();
    expect(onPolicyChange).toHaveBeenCalled();
    const lastPolicy = onPolicyChange.mock.calls.at(-1)![0] as Record<string, unknown>;
    expect(typeof lastPolicy['odrl:uid']).toBe('string');
    expect(lastPolicy['odrl:uid']).not.toBe('');
  });

  it('creates a policy that carries the same odrl:uid and filled values', async () => {
    const user = userEvent.setup();
    const { onCreatePolicy, onPolicyChange } = renderFiller();

    const uidBefore = (onPolicyChange.mock.calls.at(-1)![0] as Record<string, unknown>)['odrl:uid'];

    await user.type(screen.getByLabelText('Target'), 'urn:asset:1');
    await user.click(screen.getByRole('button', { name: /create/i }));

    expect(onCreatePolicy).toHaveBeenCalledTimes(1);
    const created = onCreatePolicy.mock.calls[0][0] as Record<string, unknown>;
    expect(created['odrl:uid']).toBe(uidBefore);
    const permission = created['odrl:permission'] as Record<string, unknown>;
    expect(permission['odrl:target']).toBe('urn:asset:1');
  });
});

/** A template whose only placeholder is an untouched boolean. */
const BOOLEAN_TEMPLATE: Template = {
  id: 't-bool',
  name: 'Bool',
  description: 'Boolean template',
  odrl: {
    '@type': 'odrl:Policy',
    'odrl:permission': {
      'odrl:target': 'urn:asset:fixed',
      'odrl:constraint': {
        'odrl:leftOperand': { '@id': 'ex:active' },
        'odrl:operator': { '@id': 'odrl:eq' },
        'odrl:rightOperand': { '@value': '{{IS_ACTIVE}}', '@type': 'xsd:boolean' },
      },
      'odrl:action': { '@id': 'odrl:read' },
    },
  } as unknown as Template['odrl'],
  placeholders: [
    { key: 'IS_ACTIVE', name: 'Is Active', type: 'boolean' as Template['placeholders'][number]['type'] },
  ],
};

function renderBooleanFiller() {
  const onCreatePolicy = vi.fn();
  const onPolicyChange = vi.fn();
  render(
    <I18nProvider locale="en">
      <TemplateFiller
        template={BOOLEAN_TEMPLATE}
        onCreatePolicy={onCreatePolicy}
        onPolicyChange={onPolicyChange}
      />
    </I18nProvider>,
  );
  return { onCreatePolicy, onPolicyChange };
}

describe('TemplateFiller boolean and placeholder-residue handling', () => {
  it('seeds an untouched boolean placeholder with false instead of leaving {{KEY}}', () => {
    const { onPolicyChange } = renderBooleanFiller();

    const lastPolicy = onPolicyChange.mock.calls.at(-1)![0] as Record<string, unknown>;
    const rightOperand =
      ((lastPolicy['odrl:permission'] as Record<string, unknown>)['odrl:constraint'] as Record<string, unknown>)[
        'odrl:rightOperand'
      ] as Record<string, unknown>;
    // The unresolved token must be gone, replaced by a real boolean.
    expect(rightOperand['@value']).toBe(false);
    expect(JSON.stringify(lastPolicy)).not.toContain('{{');
  });

  it('creates the policy for a boolean-only template without clicking the switch', async () => {
    const user = userEvent.setup();
    const { onCreatePolicy } = renderBooleanFiller();

    const button = screen.getByRole('button', { name: /create/i });
    expect(button).toBeEnabled();
    await user.click(button);

    expect(onCreatePolicy).toHaveBeenCalledTimes(1);
    expect(JSON.stringify(onCreatePolicy.mock.calls[0][0])).not.toContain('{{');
  });
});
