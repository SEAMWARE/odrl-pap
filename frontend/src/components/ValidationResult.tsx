/**
 * ValidationResult component.
 *
 * Displays the outcome of a policy validation request with a clear
 * visual allow/deny indicator, expandable explanation list, optional
 * raw response toggle, and collapsible view of the validated policy.
 */
import { useState } from 'react';
import { Alert, Badge, Button, Collapse, ListGroup } from 'react-bootstrap';
import { useI18n } from '../i18n';
import type { ValidationResponse, OdrlPolicyJson } from '../services/api';

/** Props for the ValidationResult component. */
interface ValidationResultProps {
  /** The validation response from the server. */
  result: ValidationResponse;
  /** The policy that was validated (shown in a collapsible section). */
  policy?: OdrlPolicyJson;
}

/** CSS class suffix for the allow/deny visual state. */
const ALLOW_VARIANT = 'success';
/** CSS class suffix for the deny visual state. */
const DENY_VARIANT = 'danger';

/**
 * Renders the validation result with a visual allow/deny indicator,
 * explanation list, and optional raw response and policy views.
 */
const ValidationResult = ({ result, policy }: ValidationResultProps) => {
  const { strings } = useI18n();
  const t = strings.validationResult;

  const [showRaw, setShowRaw] = useState(false);
  const [showPolicy, setShowPolicy] = useState(false);

  const isAllowed = result.allow === true;
  const variant = isAllowed ? ALLOW_VARIANT : DENY_VARIANT;
  const explanations = result.explanation ?? [];

  return (
    <div className="validation-result mt-3" data-testid="validation-result">
      {/* Allow/Deny indicator */}
      <Alert variant={variant} className="d-flex align-items-center gap-2">
        <Badge
          bg={variant}
          className="fs-5 p-2"
          aria-label={isAllowed ? t.allowed : t.denied}
          data-testid="validation-result-badge"
        >
          {isAllowed ? '\u2713' : '\u2717'}
        </Badge>
        <div>
          <Alert.Heading className="mb-1">
            {isAllowed ? t.allowed : t.denied}
          </Alert.Heading>
          <p className="mb-0">
            {isAllowed ? t.allowedDescription : t.deniedDescription}
          </p>
        </div>
      </Alert>

      {/* Explanation list */}
      {explanations.length > 0 && (
        <div className="mb-3">
          <h6>{t.explanationTitle}</h6>
          <ListGroup variant="flush" data-testid="explanation-list">
            {explanations.map((explanation, index) => (
              <ListGroup.Item
                key={index}
                className="d-flex align-items-start gap-2"
              >
                <Badge bg="secondary" className="mt-1">
                  {index + 1}
                </Badge>
                <span>{explanation}</span>
              </ListGroup.Item>
            ))}
          </ListGroup>
        </div>
      )}

      {explanations.length === 0 && !isAllowed && (
        <p className="text-muted">{t.noExplanation}</p>
      )}

      {/* Raw response toggle */}
      <div className="mb-2">
        <Button
          variant="outline-secondary"
          size="sm"
          onClick={() => setShowRaw(!showRaw)}
          aria-expanded={showRaw}
          data-testid="toggle-raw-response"
        >
          {showRaw ? t.hideRawResponse : t.showRawResponse}
        </Button>
      </div>
      <Collapse in={showRaw}>
        <div>
          <pre className="bg-light p-3 rounded border" data-testid="raw-response">
            {JSON.stringify(result, null, 2)}
          </pre>
        </div>
      </Collapse>

      {/* Validated policy toggle */}
      {policy && (
        <>
          <div className="mb-2">
            <Button
              variant="outline-secondary"
              size="sm"
              onClick={() => setShowPolicy(!showPolicy)}
              aria-expanded={showPolicy}
              data-testid="toggle-policy-view"
            >
              {showPolicy ? t.hidePolicy : t.showPolicy}
            </Button>
          </div>
          <Collapse in={showPolicy}>
            <div>
              <pre className="bg-light p-3 rounded border" data-testid="policy-view">
                {JSON.stringify(policy, null, 2)}
              </pre>
            </div>
          </Collapse>
        </>
      )}
    </div>
  );
};

export default ValidationResult;
