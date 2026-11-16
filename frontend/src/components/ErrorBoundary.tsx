/**
 * Error boundary for the parts of the editor that render a policy.
 *
 * A policy written by hand (or by another tool) can have a shape the Builder or the Summary do not
 * expect. Instead of unmounting the whole editor, the boundary shows the error in place and leaves
 * the rest of the page - in particular the raw ODRL editor, where the policy can be fixed - usable.
 */
import { Component, type ErrorInfo, type ReactNode } from 'react';
import { Alert } from 'react-bootstrap';
import { useI18n } from '../i18n';

interface ErrorBoundaryProps {
  children: ReactNode;
  /** When this value changes, the boundary forgets the error and renders its children again. */
  resetKey?: unknown;
}

interface ErrorBoundaryState {
  error: Error | null;
}

/** Fallback shown in place of the crashed children. */
const ErrorNotice = ({ error }: { error: Error }) => {
  const { strings } = useI18n();
  return (
    <Alert variant="danger" role="alert" className="mb-0">
      <strong>{strings.errorBoundary.title}</strong>
      <div className="small mt-1">{strings.errorBoundary.hint}</div>
      <code className="d-block small mt-2">{error.message}</code>
    </Alert>
  );
};

class ErrorBoundary extends Component<ErrorBoundaryProps, ErrorBoundaryState> {
  state: ErrorBoundaryState = { error: null };

  static getDerivedStateFromError(error: unknown): ErrorBoundaryState {
    return { error: error instanceof Error ? error : new Error(String(error)) };
  }

  componentDidCatch(error: Error, info: ErrorInfo) {
    console.error('Policy editor section failed to render', error, info.componentStack);
  }

  componentDidUpdate(previous: ErrorBoundaryProps) {
    if (this.state.error && previous.resetKey !== this.props.resetKey) {
      this.setState({ error: null });
    }
  }

  render() {
    return this.state.error ? <ErrorNotice error={this.state.error} /> : this.props.children;
  }
}

export default ErrorBoundary;
