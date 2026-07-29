/**
 * Unit tests for the ThemeContext provider and useTheme hook.
 *
 * Verifies default theme application, light/dark preset switching,
 * custom theme overrides, and CSS custom property injection on the
 * document root element.
 */
import { describe, it, expect, afterEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import { renderHook } from '@testing-library/react';
import { ThemeProvider } from './ThemeContext';
import { useTheme } from './useTheme';
import { lightTheme, darkTheme } from './defaultTheme';
import type { ThemeConfig } from './defaultTheme';
import type { ReactNode } from 'react';

/** Cleans up injected CSS custom properties after each test. */
afterEach(() => {
  const root = document.documentElement;
  const allProps = Object.keys(lightTheme).concat(Object.keys(darkTheme));
  for (const prop of allProps) {
    root.style.removeProperty(`--${prop}`);
  }
});

/** Helper to create a wrapper with a configured ThemeProvider. */
const createWrapper = (
  props: { preset?: 'light' | 'dark'; overrides?: Partial<ThemeConfig> } = {},
) =>
  function ThemeWrapper({ children }: { children: ReactNode }) {
    return <ThemeProvider {...props}>{children}</ThemeProvider>;
  };

describe('useTheme', () => {
  it('returns light theme by default when no provider is used', () => {
    const { result } = renderHook(() => useTheme());

    expect(result.current.preset).toBe('light');
    expect(result.current.theme).toEqual(lightTheme);
  });

  it('returns light theme with bare ThemeProvider', () => {
    const { result } = renderHook(() => useTheme(), {
      wrapper: createWrapper(),
    });

    expect(result.current.preset).toBe('light');
    expect(result.current.theme['odrl-primary-color']).toBe('#0B2B40');
  });

  it('returns dark theme when preset is "dark"', () => {
    const { result } = renderHook(() => useTheme(), {
      wrapper: createWrapper({ preset: 'dark' }),
    });

    expect(result.current.preset).toBe('dark');
    expect(result.current.theme['odrl-bg-color']).toBe('#1a1a2e');
    expect(result.current.theme['odrl-text-color']).toBe('#e0e0e0');
  });

  it('merges custom overrides on top of preset', () => {
    const overrides: Partial<ThemeConfig> = {
      'odrl-primary-color': '#FF0000',
      'odrl-secondary-color': '#00FF00',
    };

    const { result } = renderHook(() => useTheme(), {
      wrapper: createWrapper({ preset: 'light', overrides }),
    });

    // Overridden values
    expect(result.current.theme['odrl-primary-color']).toBe('#FF0000');
    expect(result.current.theme['odrl-secondary-color']).toBe('#00FF00');
    // Non-overridden values from light theme
    expect(result.current.theme['odrl-bg-color']).toBe(lightTheme['odrl-bg-color']);
  });

  it('sets preset to "custom" when overrides are provided', () => {
    const { result } = renderHook(() => useTheme(), {
      wrapper: createWrapper({
        preset: 'light',
        overrides: { 'odrl-primary-color': '#123456' },
      }),
    });

    expect(result.current.preset).toBe('custom');
  });
});

describe('ThemeProvider CSS injection', () => {
  it('injects CSS custom properties onto document root', () => {
    render(
      <ThemeProvider preset="light">
        <div>test</div>
      </ThemeProvider>,
    );

    const root = document.documentElement;
    expect(root.style.getPropertyValue('--odrl-primary-color')).toBe('#0B2B40');
    expect(root.style.getPropertyValue('--odrl-bg-color')).toBe('#FFFFFF');
  });

  it('injects dark theme CSS properties for dark preset', () => {
    render(
      <ThemeProvider preset="dark">
        <div>test</div>
      </ThemeProvider>,
    );

    const root = document.documentElement;
    expect(root.style.getPropertyValue('--odrl-bg-color')).toBe('#1a1a2e');
    expect(root.style.getPropertyValue('--odrl-text-color')).toBe('#e0e0e0');
  });

  it('injects overridden values alongside base theme properties', () => {
    render(
      <ThemeProvider
        preset="light"
        overrides={{ 'odrl-primary-color': '#ABCDEF' }}
      >
        <div>test</div>
      </ThemeProvider>,
    );

    const root = document.documentElement;
    // Overridden
    expect(root.style.getPropertyValue('--odrl-primary-color')).toBe('#ABCDEF');
    // Non-overridden base value
    expect(root.style.getPropertyValue('--odrl-secondary-color')).toBe('#F07D00');
  });

  it('removes CSS custom properties on unmount', () => {
    const { unmount } = render(
      <ThemeProvider preset="light">
        <div>test</div>
      </ThemeProvider>,
    );

    const root = document.documentElement;
    expect(root.style.getPropertyValue('--odrl-primary-color')).toBe('#0B2B40');

    unmount();

    expect(root.style.getPropertyValue('--odrl-primary-color')).toBe('');
  });
});

describe('ThemeProvider in component tree', () => {
  /** Simple component that consumes theme values. */
  const DisplayTheme = () => {
    const { theme, preset } = useTheme();
    return (
      <div>
        <span data-testid="preset">{preset}</span>
        <span data-testid="primary">{theme['odrl-primary-color']}</span>
        <span data-testid="bg">{theme['odrl-bg-color']}</span>
      </div>
    );
  };

  it('provides light theme values to child components', () => {
    render(
      <ThemeProvider>
        <DisplayTheme />
      </ThemeProvider>,
    );

    expect(screen.getByTestId('preset')).toHaveTextContent('light');
    expect(screen.getByTestId('primary')).toHaveTextContent('#0B2B40');
    expect(screen.getByTestId('bg')).toHaveTextContent('#FFFFFF');
  });

  it('provides dark theme values to child components', () => {
    render(
      <ThemeProvider preset="dark">
        <DisplayTheme />
      </ThemeProvider>,
    );

    expect(screen.getByTestId('preset')).toHaveTextContent('dark');
    expect(screen.getByTestId('bg')).toHaveTextContent('#1a1a2e');
  });

  it('provides custom-overridden values to child components', () => {
    render(
      <ThemeProvider
        preset="light"
        overrides={{ 'odrl-primary-color': '#CUSTOM1' }}
      >
        <DisplayTheme />
      </ThemeProvider>,
    );

    expect(screen.getByTestId('preset')).toHaveTextContent('custom');
    expect(screen.getByTestId('primary')).toHaveTextContent('#CUSTOM1');
    // Non-overridden stays at light default
    expect(screen.getByTestId('bg')).toHaveTextContent('#FFFFFF');
  });

  it('updates when preset changes', () => {
    const { rerender } = render(
      <ThemeProvider preset="light">
        <DisplayTheme />
      </ThemeProvider>,
    );

    expect(screen.getByTestId('bg')).toHaveTextContent('#FFFFFF');

    rerender(
      <ThemeProvider preset="dark">
        <DisplayTheme />
      </ThemeProvider>,
    );

    expect(screen.getByTestId('bg')).toHaveTextContent('#1a1a2e');
  });

  it('all light theme properties are defined', () => {
    const expectedProperties: Array<keyof ThemeConfig> = [
      'odrl-primary-color',
      'odrl-secondary-color',
      'odrl-bg-color',
      'odrl-text-color',
      'odrl-text-muted',
      'odrl-card-bg',
      'odrl-border-color',
      'odrl-font-family',
      'odrl-border-radius',
      'odrl-section-header-bg',
      'odrl-success-color',
      'odrl-danger-color',
      'odrl-info-color',
      'odrl-card-shadow',
    ];

    for (const prop of expectedProperties) {
      expect(lightTheme[prop]).toBeDefined();
      expect(lightTheme[prop]).not.toBe('');
    }
  });

  it('all dark theme properties are defined', () => {
    const lightKeys = Object.keys(lightTheme);
    const darkKeys = Object.keys(darkTheme);

    expect(darkKeys).toEqual(lightKeys);
  });
});
