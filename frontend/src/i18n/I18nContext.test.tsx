/**
 * Unit tests for the I18nContext provider and useI18n hook.
 *
 * Verifies default English strings, partial overrides via deep merge,
 * locale switching, and fallback behavior.
 */
import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import { renderHook } from '@testing-library/react';
import { I18nProvider, type DeepPartial } from './I18nContext';
import { useI18n } from './useI18n';
import { en, type I18nStrings } from './en';
import type { ReactNode } from 'react';

/** Helper to create a wrapper with a configured I18nProvider. */
const createWrapper =
  (props: { locale?: string; strings?: DeepPartial<I18nStrings> } = {}) =>
  ({ children }: { children: ReactNode }) => (
    <I18nProvider {...props}>{children}</I18nProvider>
  );

describe('useI18n', () => {
  it('returns default English strings when no provider is used', () => {
    const { result } = renderHook(() => useI18n());

    expect(result.current.locale).toBe('en');
    expect(result.current.strings).toBe(en);
  });

  it('returns default English strings with bare I18nProvider', () => {
    const { result } = renderHook(() => useI18n(), {
      wrapper: createWrapper(),
    });

    expect(result.current.locale).toBe('en');
    expect(result.current.strings.common.save).toBe('Save');
  });

  it('supports custom locale identifier', () => {
    const { result } = renderHook(() => useI18n(), {
      wrapper: createWrapper({ locale: 'de' }),
    });

    expect(result.current.locale).toBe('de');
  });

  it('deep-merges partial string overrides with English defaults', () => {
    const germanOverrides: DeepPartial<I18nStrings> = {
      common: {
        save: 'Speichern',
        cancel: 'Abbrechen',
      },
    };

    const { result } = renderHook(() => useI18n(), {
      wrapper: createWrapper({ locale: 'de', strings: germanOverrides }),
    });

    // Overridden keys
    expect(result.current.strings.common.save).toBe('Speichern');
    expect(result.current.strings.common.cancel).toBe('Abbrechen');
    // Non-overridden keys fall back to English
    expect(result.current.strings.common.delete).toBe('Delete');
    expect(result.current.strings.common.loading).toBe('Loading...');
  });

  it('preserves nested sections not included in overrides', () => {
    const partialOverrides: DeepPartial<I18nStrings> = {
      policyBuilder: {
        title: 'Richtlinien-Editor',
      },
    };

    const { result } = renderHook(() => useI18n(), {
      wrapper: createWrapper({ locale: 'de', strings: partialOverrides }),
    });

    // Overridden
    expect(result.current.strings.policyBuilder.title).toBe(
      'Richtlinien-Editor',
    );
    // Non-overridden in same section
    expect(result.current.strings.policyBuilder.stepTarget).toBe(
      en.policyBuilder.stepTarget,
    );
    // Entirely non-overridden section
    expect(result.current.strings.constraintBuilder.title).toBe(
      en.constraintBuilder.title,
    );
  });

  it('returns exact English defaults when overrides is undefined', () => {
    const { result } = renderHook(() => useI18n(), {
      wrapper: createWrapper({ locale: 'en', strings: undefined }),
    });

    expect(result.current.strings).toBe(en);
  });
});

describe('I18nProvider in component tree', () => {
  /** Simple component that consumes i18n strings. */
  const DisplayLabel = () => {
    const { strings, locale } = useI18n();
    return (
      <div>
        <span data-testid="locale">{locale}</span>
        <span data-testid="label">{strings.common.save}</span>
      </div>
    );
  };

  it('provides strings to child components', () => {
    render(
      <I18nProvider>
        <DisplayLabel />
      </I18nProvider>,
    );

    expect(screen.getByTestId('locale')).toHaveTextContent('en');
    expect(screen.getByTestId('label')).toHaveTextContent('Save');
  });

  it('provides overridden strings to child components', () => {
    render(
      <I18nProvider locale="fr" strings={{ common: { save: 'Enregistrer' } }}>
        <DisplayLabel />
      </I18nProvider>,
    );

    expect(screen.getByTestId('locale')).toHaveTextContent('fr');
    expect(screen.getByTestId('label')).toHaveTextContent('Enregistrer');
  });
});
