/**
 * Template selector component.
 *
 * Displays a list of available policy templates as selectable cards.
 * Each card shows the template name and description. When a template
 * is selected, the parent component is notified via the `onSelect` callback.
 */
import { Card, Row, Col, Form, Badge } from 'react-bootstrap';
import type { Template } from '../types/TemplateTypes';
import { useI18n } from '../i18n';

/** Props for the {@link TemplateSelector} component. */
interface TemplateSelectorProps {
  /** List of available templates to choose from. */
  templates: Template[];
  /** The currently selected template, if any. */
  selectedTemplate: Template | null;
  /** Callback invoked when a template is selected. */
  onSelect: (template: Template) => void;
}

/** CSS class applied to the selected template card. */
const SELECTED_CARD_CLASS = 'border-primary';

/**
 * Renders a searchable list of template cards for selection.
 *
 * Users can filter templates by name or description using a search input.
 * Clicking a card selects the template and notifies the parent via `onSelect`.
 *
 * @param props - Component properties.
 */
const TemplateSelector: React.FC<TemplateSelectorProps> = ({
  templates,
  selectedTemplate,
  onSelect,
}) => {
  const { strings } = useI18n();
  const t = strings.templateSelector;

  if (templates.length === 0) {
    return (
      <p className="text-muted fst-italic">{t.noTemplates}</p>
    );
  }

  return (
    <div>
      <Form.Label className="fw-bold mb-2">{t.selectLabel}</Form.Label>
      <Row xs={1} md={2} className="g-3">
        {templates.map((template) => {
          const isSelected = selectedTemplate?.id === template.id;
          return (
            <Col key={template.id}>
              <Card
                className={`h-100 cursor-pointer ${isSelected ? SELECTED_CARD_CLASS : ''}`}
                style={{
                  cursor: 'pointer',
                  boxShadow: isSelected ? '0 0 0 2px var(--bs-primary)' : undefined,
                }}
                onClick={() => onSelect(template)}
                role="button"
                tabIndex={0}
                onKeyDown={(e) => {
                  if (e.key === 'Enter' || e.key === ' ') {
                    e.preventDefault();
                    onSelect(template);
                  }
                }}
                aria-pressed={isSelected}
                aria-label={`${t.templateCardLabel}: ${template.name}`}
              >
                <Card.Body>
                  <Card.Title className="d-flex align-items-center gap-2">
                    {template.name}
                    {isSelected && (
                      <Badge bg="primary" pill>{t.selectedBadge}</Badge>
                    )}
                  </Card.Title>
                  {template.description && (
                    <Card.Text className="text-muted small">
                      {template.description}
                    </Card.Text>
                  )}
                  <div className="mt-2">
                    {template.placeholders.map((ph) => (
                      <Badge
                        key={ph.key}
                        bg="info"
                        className="me-1 mb-1"
                        title={ph.description || ph.name}
                      >
                        {ph.name}
                      </Badge>
                    ))}
                  </div>
                </Card.Body>
              </Card>
            </Col>
          );
        })}
      </Row>
    </div>
  );
};

export default TemplateSelector;
