/**
 * Unit tests for the capability mode helpers.
 */
import { describe, it, expect } from 'vitest';
import {
  CAPABILITIES,
  DEFAULT_CAPABILITIES,
  isRemote,
  parseCapabilityMode,
  resolveCapabilities,
} from './capabilities';

describe('parseCapabilityMode', () => {
  it('defaults to remote when the attribute is absent', () => {
    expect(parseCapabilityMode(null)).toBe('remote');
  });

  it('accepts remote and none, ignoring case and whitespace', () => {
    expect(parseCapabilityMode('remote')).toBe('remote');
    expect(parseCapabilityMode('none')).toBe('none');
    expect(parseCapabilityMode('  NONE ')).toBe('none');
  });

  it('falls back to remote for unknown values so a typo never disables a call', () => {
    expect(parseCapabilityMode('nope')).toBe('remote');
    expect(parseCapabilityMode('')).toBe('remote');
  });
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
