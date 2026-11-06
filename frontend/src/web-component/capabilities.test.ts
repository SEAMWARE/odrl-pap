/**
 * Unit tests for the capability mode helpers.
 */
import { describe, it, expect } from 'vitest';
import {
  CAPABILITIES,
  DEFAULT_CAPABILITIES,
  isEnabled,
  isEvent,
  isRemote,
  parseCapabilityMode,
  resolveCapabilities,
  type Capability,
} from './capabilities';

describe('parseCapabilityMode', () => {
  it.each(CAPABILITIES)('defaults %s to remote when the attribute is absent', (capability) => {
    expect(parseCapabilityMode(capability, null)).toBe('remote');
  });

  it.each([
    { raw: 'remote', expected: 'remote' },
    { raw: 'none', expected: 'none' },
    { raw: '  NONE ', expected: 'none' },
    { raw: 'Remote', expected: 'remote' },
  ])('reads $raw as $expected, ignoring case and whitespace', ({ raw, expected }) => {
    expect(parseCapabilityMode('save', raw)).toBe(expected);
  });

  it.each([
    { label: 'a typo', raw: 'nope' },
    { label: 'an empty value', raw: '' },
    { label: 'a boolean-looking value', raw: 'false' },
  ])('falls back to remote for $label so a call is never silently disabled', ({ raw }) => {
    expect(parseCapabilityMode('save', raw)).toBe('remote');
  });

  it('accepts the event mode for save', () => {
    expect(parseCapabilityMode('save', 'event')).toBe('event');
    expect(parseCapabilityMode('save', ' EVENT ')).toBe('event');
  });

  it.each(CAPABILITIES.filter((capability) => capability !== 'save'))(
    'rejects the event mode for %s, which has no handling for it',
    (capability) => {
      expect(parseCapabilityMode(capability, 'event')).toBe('remote');
    },
  );
});

describe('resolveCapabilities', () => {
  it('resolves every capability to remote by default', () => {
    expect(resolveCapabilities(() => null)).toEqual(DEFAULT_CAPABILITIES);
    expect(Object.keys(DEFAULT_CAPABILITIES)).toEqual([...CAPABILITIES]);
  });

  it('reads each capability from its own attribute', () => {
    const attrs: Record<string, string> = { save: 'none', validate: 'none' };
    const modes = resolveCapabilities((name) => attrs[name] ?? null);

    expect(isRemote(modes, 'save')).toBe(false);
    expect(isRemote(modes, 'validate')).toBe(false);
    expect(isRemote(modes, 'load')).toBe(true);
    expect(isRemote(modes, 'templates')).toBe(true);
    expect(isRemote(modes, 'mappings')).toBe(true);
  });
});

describe('capability predicates', () => {
  /** Builds capability modes with a single capability overridden. */
  const modesWith = (capability: Capability, raw: string) =>
    resolveCapabilities((name) => (name === capability ? raw : null));

  it.each([
    { mode: 'remote', remote: true, event: false, enabled: true },
    { mode: 'none', remote: false, event: false, enabled: false },
    { mode: 'event', remote: false, event: true, enabled: true },
  ])('classifies save=$mode', ({ mode, remote, event, enabled }) => {
    const modes = modesWith('save', mode);

    expect(isRemote(modes, 'save')).toBe(remote);
    expect(isEvent(modes, 'save')).toBe(event);
    expect(isEnabled(modes, 'save')).toBe(enabled);
  });
});
