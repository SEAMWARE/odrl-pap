/** ErrorBoundary (P1-2/P1-3): a crash in a child shows the error instead of blanking the editor. */
import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import ErrorBoundary from './ErrorBoundary';
import PolicyBuilder from './PolicyBuilder';
import { I18nProvider } from '../i18n';
import { clearMappingsCache } from '../hooks/useMappings';
import type { Mappings, OdrlPolicyJson } from '../api';

const MAPPINGS: Mappings = {
  actions: [], operators: [], leftOperands: [], rightOperands: [], assignees: [], targets: [], constraints: [],
};

const Boom = (): never => {
  throw new Error('kaboom');
};

describe('ErrorBoundary', () => {
  beforeEach(() => {
    clearMappingsCache();
    vi.spyOn(console, 'error').mockImplementation(() => {});
  });
  afterEach(() => vi.restoreAllMocks());

  it('renders its children when nothing fails', () => {
    render(
      <I18nProvider>
        <ErrorBoundary><span>fine</span></ErrorBoundary>
      </I18nProvider>,
    );
    expect(screen.getByText('fine')).toBeInTheDocument();
  });

  it('shows the error message when a child throws', () => {
    render(
      <I18nProvider>
        <ErrorBoundary><Boom /></ErrorBoundary>
      </I18nProvider>,
    );
    expect(screen.getByRole('alert')).toHaveTextContent('kaboom');
  });

  it('contains the crash of the Builder given a policy it cannot read', () => {
    const unreadable = {
      get 'odrl:permission'(): never {
        throw new Error('unreadable policy');
      },
    };
    render(
      <I18nProvider>
        <ErrorBoundary>
          <PolicyBuilder policy={unreadable as unknown as OdrlPolicyJson} setPolicy={() => {}} mappings={MAPPINGS} />
        </ErrorBoundary>
        <span>raw tab still here</span>
      </I18nProvider>,
    );
    expect(screen.getByRole('alert')).toBeInTheDocument();
    expect(screen.getByText('raw tab still here')).toBeInTheDocument();
  });

  it('recovers when the reset key changes', () => {
    const { rerender } = render(
      <I18nProvider>
        <ErrorBoundary resetKey="a"><Boom /></ErrorBoundary>
      </I18nProvider>,
    );
    expect(screen.getByRole('alert')).toBeInTheDocument();
    rerender(
      <I18nProvider>
        <ErrorBoundary resetKey="b"><span>recovered</span></ErrorBoundary>
      </I18nProvider>,
    );
    expect(screen.getByText('recovered')).toBeInTheDocument();
  });
});
