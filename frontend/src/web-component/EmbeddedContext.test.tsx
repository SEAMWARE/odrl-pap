/**
 * Unit tests for the EmbeddedContext provider and hook.
 *
 * Verifies that components can detect embedded mode, read configuration
 * from the context, and that the standalone default works correctly.
 */
import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import {
  EmbeddedProvider,
  useEmbeddedContext,
  type EmbeddedConfig,
} from './EmbeddedContext';

/** No-op event callback for tests. */
const noopOnEvent = vi.fn();

/** Builds a minimal valid EmbeddedConfig for testing. */
function makeConfig(overrides: Partial<EmbeddedConfig> = {}): EmbeddedConfig {
  return {
    apiBaseUrl: 'https://pap.test',
    authToken: null,
    mode: 'create',
    policyId: null,
    locale: 'en',
    theme: 'light',
    onEvent: noopOnEvent,
    ...overrides,
  };
}

/**
 * Test component that renders context values so assertions can
 * query the DOM.
 */
const ContextConsumer = () => {
  const ctx = useEmbeddedContext();

  if (!ctx.isEmbedded) {
    return <div data-testid="not-embedded">standalone</div>;
  }

  return (
    <div data-testid="embedded">
      <span data-testid="api-base-url">{ctx.apiBaseUrl}</span>
      <span data-testid="auth-token">{ctx.authToken ?? 'none'}</span>
      <span data-testid="mode">{ctx.mode}</span>
      <span data-testid="policy-id">{ctx.policyId ?? 'none'}</span>
      <span data-testid="locale">{ctx.locale}</span>
      <span data-testid="theme">{ctx.theme}</span>
    </div>
  );
};

describe('EmbeddedContext', () => {
  // -----------------------------------------------------------------
  // Standalone mode (no provider)
  // -----------------------------------------------------------------

  it('returns isEmbedded=false when no EmbeddedProvider is present', () => {
    render(<ContextConsumer />);
    expect(screen.getByTestId('not-embedded')).toHaveTextContent('standalone');
  });

  // -----------------------------------------------------------------
  // Embedded mode
  // -----------------------------------------------------------------

  it('returns isEmbedded=true when inside EmbeddedProvider', () => {
    render(
      <EmbeddedProvider config={makeConfig()}>
        <ContextConsumer />
      </EmbeddedProvider>,
    );
    expect(screen.getByTestId('embedded')).toBeInTheDocument();
  });

  it('provides the correct apiBaseUrl', () => {
    render(
      <EmbeddedProvider config={makeConfig({ apiBaseUrl: 'https://custom.api' })}>
        <ContextConsumer />
      </EmbeddedProvider>,
    );
    expect(screen.getByTestId('api-base-url')).toHaveTextContent('https://custom.api');
  });

  it('provides the correct authToken', () => {
    render(
      <EmbeddedProvider config={makeConfig({ authToken: 'my-secret-token' })}>
        <ContextConsumer />
      </EmbeddedProvider>,
    );
    expect(screen.getByTestId('auth-token')).toHaveTextContent('my-secret-token');
  });

  it('provides the correct mode', () => {
    render(
      <EmbeddedProvider config={makeConfig({ mode: 'edit' })}>
        <ContextConsumer />
      </EmbeddedProvider>,
    );
    expect(screen.getByTestId('mode')).toHaveTextContent('edit');
  });

  it('provides the correct policyId', () => {
    render(
      <EmbeddedProvider config={makeConfig({ policyId: 'pol-42' })}>
        <ContextConsumer />
      </EmbeddedProvider>,
    );
    expect(screen.getByTestId('policy-id')).toHaveTextContent('pol-42');
  });

  it('provides the correct locale', () => {
    render(
      <EmbeddedProvider config={makeConfig({ locale: 'de' })}>
        <ContextConsumer />
      </EmbeddedProvider>,
    );
    expect(screen.getByTestId('locale')).toHaveTextContent('de');
  });

  it('provides the correct theme', () => {
    render(
      <EmbeddedProvider config={makeConfig({ theme: 'dark' })}>
        <ContextConsumer />
      </EmbeddedProvider>,
    );
    expect(screen.getByTestId('theme')).toHaveTextContent('dark');
  });

  it('makes onEvent callable from the context', () => {
    const onEvent = vi.fn();
    const TestEventEmitter = () => {
      const ctx = useEmbeddedContext();
      if (ctx.isEmbedded) {
        ctx.onEvent('policy-created', { policy: {}, id: 'test-id' });
      }
      return <div data-testid="emitter">done</div>;
    };

    render(
      <EmbeddedProvider config={makeConfig({ onEvent })}>
        <TestEventEmitter />
      </EmbeddedProvider>,
    );

    expect(onEvent).toHaveBeenCalledWith('policy-created', {
      policy: {},
      id: 'test-id',
    });
  });
});
