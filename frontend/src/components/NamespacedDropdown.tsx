/**
 * Reusable dropdown component that groups items by namespace prefix.
 *
 * Accepts an array of `Mapping` items (with `name` like "odrl:read")
 * and renders them grouped by the namespace prefix (e.g., "ODRL",
 * "DOME-OP"). Each item displays its description as secondary text.
 * Supports search/filter for large lists and handles empty states.
 */
import { useState, useMemo, useCallback } from 'react';
import { Form } from 'react-bootstrap';
import type { Mapping } from '../api';
import { useI18n } from '../i18n';
import { splitNamespace, formatGroupLabel } from './namespaceUtils';

/** A group of items sharing the same namespace prefix. */
interface NamespaceGroup {
  /** Display label for the group (e.g., "ODRL", "DOME-OP"). */
  label: string;
  /** Raw namespace prefix (e.g., "odrl", "dome-op"). */
  prefix: string;
  /** Items belonging to this namespace. */
  items: Mapping[];
}

interface NamespacedDropdownProps {
  /** Available items to display, each with `name` and `description`. */
  items: Mapping[];
  /** Currently selected value (the full `name`, e.g., "odrl:read"). */
  value: string;
  /** Callback when a new value is selected. */
  onChange: (value: string) => void;
  /** Placeholder text shown as the first disabled option. */
  placeholder?: string;
  /** Whether to show the filter input for large lists. */
  searchable?: boolean;
  /** Minimum number of items before the search input appears. */
  searchThreshold?: number;
  /** HTML id attribute for the select element. */
  id?: string;
  /** Additional CSS class names. */
  className?: string;
  /** Whether the control is disabled. */
  disabled?: boolean;
  /** aria-label for accessibility. */
  ariaLabel?: string;
}

/** Default minimum items before the search filter appears. */
const DEFAULT_SEARCH_THRESHOLD = 8;

/**
 * Dropdown that groups items by namespace prefix with descriptions.
 *
 * @example
 * ```tsx
 * <NamespacedDropdown
 *   items={mappings.actions ?? []}
 *   value={selectedAction}
 *   onChange={setSelectedAction}
 *   placeholder="Select an action"
 * />
 * ```
 */
const NamespacedDropdown = ({
  items,
  value,
  onChange,
  placeholder,
  searchable = true,
  searchThreshold = DEFAULT_SEARCH_THRESHOLD,
  id,
  className,
  disabled = false,
  ariaLabel,
}: NamespacedDropdownProps) => {
  const { strings } = useI18n();
  const [filter, setFilter] = useState('');

  const effectivePlaceholder = placeholder ?? strings.common.selectOption;

  /** Group items by namespace, applying the current filter. */
  const groups = useMemo<NamespaceGroup[]>(() => {
    const filtered = filter
      ? items.filter(
          (item) =>
            item.name?.toLowerCase().includes(filter.toLowerCase()) ||
            item.description?.toLowerCase().includes(filter.toLowerCase()),
        )
      : items;

    const groupMap = new Map<string, Mapping[]>();
    for (const item of filtered) {
      const [prefix] = splitNamespace(item.name ?? '');
      if (!groupMap.has(prefix)) {
        groupMap.set(prefix, []);
      }
      groupMap.get(prefix)!.push(item);
    }

    // Sort groups alphabetically by prefix
    return Array.from(groupMap.entries())
      .sort(([a], [b]) => a.localeCompare(b))
      .map(([prefix, groupItems]) => ({
        label: formatGroupLabel(prefix),
        prefix,
        items: groupItems,
      }));
  }, [items, filter]);

  const showSearch = searchable && items.length >= searchThreshold;

  const handleChange = useCallback(
    (e: React.ChangeEvent<HTMLSelectElement>) => {
      onChange(e.target.value);
    },
    [onChange],
  );

  const totalFiltered = groups.reduce((sum, g) => sum + g.items.length, 0);

  return (
    <div className={className}>
      {showSearch && (
        <Form.Control
          type="text"
          size="sm"
          placeholder={strings.namespacedDropdown.filterPlaceholder}
          value={filter}
          onChange={(e) => setFilter(e.target.value)}
          className="mb-1"
          aria-label={strings.common.search}
        />
      )}
      <Form.Select
        id={id}
        value={value}
        onChange={handleChange}
        disabled={disabled}
        aria-label={ariaLabel ?? effectivePlaceholder}
      >
        <option value="">{effectivePlaceholder}</option>
        {totalFiltered === 0 && filter && (
          <option disabled>{strings.namespacedDropdown.noResults}</option>
        )}
        {groups.map((group) => (
          <optgroup key={group.prefix} label={group.label}>
            {group.items.map((item) => (
              <option key={item.name} value={item.name} title={item.description}>
                {item.name} {item.description ? `\u2014 ${item.description}` : ''}
              </option>
            ))}
          </optgroup>
        ))}
      </Form.Select>
    </div>
  );
};

export default NamespacedDropdown;
