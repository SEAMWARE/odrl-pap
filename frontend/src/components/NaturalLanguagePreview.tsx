/**
 * Natural language preview component.
 *
 * Renders a natural language string with `{{PLACEHOLDER}}` tokens
 * highlighted as colored badges, making template placeholders visually
 * distinct from surrounding text.
 */
import { Badge } from 'react-bootstrap';
import { PLACEHOLDER_REGEX } from '../types/TemplateTypes';

/** Props for the {@link NaturalLanguagePreview} component. */
interface NaturalLanguagePreviewProps {
  /** The natural language text containing `{{KEY}}` tokens. */
  text: string;
  /** Optional mapping of placeholder keys to their filled-in values. */
  values?: Record<string, string>;
}

/** CSS class name for the preview container. */
const PREVIEW_CONTAINER_CLASS = 'natural-language-preview';

/**
 * Renders a natural language sentence with placeholder tokens
 * displayed as colored Bootstrap badges.
 *
 * Filled placeholders show the value in a success badge;
 * unfilled placeholders show the key name in an info badge.
 *
 * @param props - Component properties.
 */
const NaturalLanguagePreview: React.FC<NaturalLanguagePreviewProps> = ({
  text,
  values = {},
}) => {
  if (!text) {
    return <span className="text-muted fst-italic">No natural language description provided.</span>;
  }

  const parts: React.ReactNode[] = [];
  let lastIndex = 0;
  let match: RegExpExecArray | null;
  const regex = new RegExp(PLACEHOLDER_REGEX.source, 'g');

  while ((match = regex.exec(text)) !== null) {
    // Add preceding text
    if (match.index > lastIndex) {
      parts.push(
        <span key={`text-${lastIndex}`}>
          {text.slice(lastIndex, match.index)}
        </span>,
      );
    }

    const key = match[1];
    const filledValue = values[key];

    if (filledValue !== undefined && filledValue !== '') {
      parts.push(
        <Badge
          key={`ph-${match.index}`}
          bg="success"
          className="mx-1"
          title={`${key}: ${filledValue}`}
        >
          {filledValue}
        </Badge>,
      );
    } else {
      parts.push(
        <Badge
          key={`ph-${match.index}`}
          bg="info"
          className="mx-1"
          title={key}
        >
          {`{{${key}}}`}
        </Badge>,
      );
    }

    lastIndex = match.index + match[0].length;
  }

  // Add trailing text
  if (lastIndex < text.length) {
    parts.push(
      <span key={`text-${lastIndex}`}>
        {text.slice(lastIndex)}
      </span>,
    );
  }

  return (
    <div className={PREVIEW_CONTAINER_CLASS} style={{ lineHeight: '2rem' }}>
      {parts}
    </div>
  );
};

export default NaturalLanguagePreview;
