/**
 * Unit tests for the ValidationResult component.
 *
 * Verifies visual allow/deny indicators, explanation list rendering,
 * raw response toggle, and policy view toggle.
 */
import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import ValidationResult from './ValidationResult';
import { I18nProvider } from '../i18n';
import type { ValidationResponse, OdrlPolicyJson } from '../services/api';

/** Wraps a component with required providers. */
const renderWithProviders = (ui: React.ReactElement) =>
  render(<I18nProvider>{ui}</I18nProvider>);

/** Mock policy for testing the policy view toggle. */
const MOCK_POLICY: OdrlPolicyJson = {
  '@context': 'http://www.w3.org/ns/odrl/2/',
  '@type': 'odrl:Policy',
  'odrl:permission': {
    'odrl:action': 'odrl:read',
  },
};

describe('ValidationResult', () => {
  describe('allow result', () => {
    const allowResult: ValidationResponse = {
      allow: true,
      explanation: [],
    };

    it('renders a green success indicator for allowed requests', () => {
      renderWithProviders(<ValidationResult result={allowResult} />);

      expect(screen.getByText('Request Allowed')).toBeInTheDocument();
      expect(screen.getByText('The policy permits this request.')).toBeInTheDocument();
    });

    it('renders a checkmark badge for allowed requests', () => {
      renderWithProviders(<ValidationResult result={allowResult} />);

      const badge = screen.getByTestId('validation-result-badge');
      expect(badge).toHaveTextContent('\u2713');
    });

    it('does not render explanation list when empty and allowed', () => {
      renderWithProviders(<ValidationResult result={allowResult} />);

      expect(screen.queryByTestId('explanation-list')).not.toBeInTheDocument();
    });
  });

  describe('deny result', () => {
    const denyResult: ValidationResponse = {
      allow: false,
      explanation: [
        'Policy evaluation failed: constraint not satisfied',
        'Required role "admin" not found in credential',
      ],
    };

    it('renders a red danger indicator for denied requests', () => {
      renderWithProviders(<ValidationResult result={denyResult} />);

      expect(screen.getByText('Request Denied')).toBeInTheDocument();
      expect(screen.getByText('The policy does not permit this request.')).toBeInTheDocument();
    });

    it('renders a cross badge for denied requests', () => {
      renderWithProviders(<ValidationResult result={denyResult} />);

      const badge = screen.getByTestId('validation-result-badge');
      expect(badge).toHaveTextContent('\u2717');
    });

    it('renders explanation list with all items', () => {
      renderWithProviders(<ValidationResult result={denyResult} />);

      const list = screen.getByTestId('explanation-list');
      expect(list).toBeInTheDocument();

      expect(screen.getByText('Policy evaluation failed: constraint not satisfied')).toBeInTheDocument();
      expect(screen.getByText('Required role "admin" not found in credential')).toBeInTheDocument();
    });

    it('renders numbered badges for each explanation item', () => {
      renderWithProviders(<ValidationResult result={denyResult} />);

      expect(screen.getByText('1')).toBeInTheDocument();
      expect(screen.getByText('2')).toBeInTheDocument();
    });
  });

  describe('deny result with no explanation', () => {
    const denyNoExplanation: ValidationResponse = {
      allow: false,
      explanation: [],
    };

    it('shows "no additional details" message when denied without explanations', () => {
      renderWithProviders(<ValidationResult result={denyNoExplanation} />);

      expect(screen.getByText('No additional details available.')).toBeInTheDocument();
    });
  });

  describe('raw response toggle', () => {
    const result: ValidationResponse = { allow: true, explanation: [] };

    it('renders the toggle button with correct initial label', () => {
      renderWithProviders(<ValidationResult result={result} />);

      const toggleBtn = screen.getByTestId('toggle-raw-response');
      expect(toggleBtn).toHaveTextContent('Show Raw Response');
      expect(toggleBtn).toHaveAttribute('aria-expanded', 'false');
    });

    it('changes button label and aria-expanded after clicking toggle', async () => {
      const user = userEvent.setup();
      renderWithProviders(<ValidationResult result={result} />);

      const toggleBtn = screen.getByTestId('toggle-raw-response');
      await user.click(toggleBtn);

      expect(toggleBtn).toHaveTextContent('Hide Raw Response');
      expect(toggleBtn).toHaveAttribute('aria-expanded', 'true');
    });

    it('displays JSON-formatted response in raw view', async () => {
      const user = userEvent.setup();
      const denyResult: ValidationResponse = {
        allow: false,
        explanation: ['test error'],
      };
      renderWithProviders(<ValidationResult result={denyResult} />);

      await user.click(screen.getByTestId('toggle-raw-response'));

      const raw = screen.getByTestId('raw-response');
      expect(raw).toHaveTextContent('"allow": false');
      expect(raw).toHaveTextContent('"test error"');
    });
  });

  describe('policy view toggle', () => {
    const result: ValidationResponse = { allow: true, explanation: [] };

    it('shows the policy view toggle when policy is provided', () => {
      renderWithProviders(<ValidationResult result={result} policy={MOCK_POLICY} />);

      expect(screen.getByTestId('toggle-policy-view')).toBeInTheDocument();
    });

    it('does not show the policy view toggle when no policy is provided', () => {
      renderWithProviders(<ValidationResult result={result} />);

      expect(screen.queryByTestId('toggle-policy-view')).not.toBeInTheDocument();
    });

    it('changes button label and aria-expanded after clicking toggle', async () => {
      const user = userEvent.setup();
      renderWithProviders(<ValidationResult result={result} policy={MOCK_POLICY} />);

      const toggleBtn = screen.getByTestId('toggle-policy-view');
      expect(toggleBtn).toHaveTextContent('Show Policy');
      expect(toggleBtn).toHaveAttribute('aria-expanded', 'false');

      await user.click(toggleBtn);

      expect(toggleBtn).toHaveTextContent('Hide Policy');
      expect(toggleBtn).toHaveAttribute('aria-expanded', 'true');
    });

    it('displays the policy JSON in the policy view', async () => {
      const user = userEvent.setup();
      renderWithProviders(<ValidationResult result={result} policy={MOCK_POLICY} />);

      await user.click(screen.getByTestId('toggle-policy-view'));

      const policyView = screen.getByTestId('policy-view');
      expect(policyView).toHaveTextContent('odrl:read');
    });
  });
});
