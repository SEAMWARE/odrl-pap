/**
 * Hook for template-aware policy editing.
 *
 * When a {@link PolicyTemplate} is provided, this hook exposes helpers
 * that the policy builder uses to determine which fields are locked,
 * which are editable, and what metadata (label, description) to show
 * for each editable field.
 *
 * When no template is provided the hook returns a "passthrough" state
 * where every field is editable and nothing is locked — preserving
 * the existing freeform editing experience.
 *
 * @example
 * ```tsx
 * const { isTemplateMode, isFieldLocked, getFieldMeta } = useTemplateMode(template);
 *
 * if (isFieldLocked('odrl:permission.odrl:action')) {
 *   // render a read-only badge instead of a dropdown
 * }
 * ```
 */
import { useMemo } from 'react';
import type { PolicyTemplate, TemplateField } from '../types';

/** Return value of the {@link useTemplateMode} hook. */
export interface TemplateModeResult {
  /** `true` when a template is active, `false` in freeform mode. */
  isTemplateMode: boolean;

  /** The active template, or `undefined` in freeform mode. */
  template: PolicyTemplate | undefined;

  /**
   * Returns `true` if the field at the given dot-separated path is
   * locked (i.e., cannot be edited by the user).
   *
   * Always returns `false` when no template is active.
   */
  isFieldLocked: (path: string) => boolean;

  /**
   * Returns `true` if the field at the given dot-separated path is
   * explicitly listed as editable in the template.
   *
   * Always returns `true` when no template is active.
   */
  isFieldEditable: (path: string) => boolean;

  /**
   * Returns the {@link TemplateField} metadata for the given path,
   * or `undefined` if the path is not an editable template field
   * (or if no template is active).
   */
  getFieldMeta: (path: string) => TemplateField | undefined;
}

/**
 * Provides template-mode awareness for the policy builder.
 *
 * @param template - The active policy template, or `undefined` for
 *   freeform editing.
 * @returns Helpers for checking field lock state and reading field metadata.
 */
export function useTemplateMode(
  template: PolicyTemplate | undefined,
): TemplateModeResult {
  const isTemplateMode = template !== undefined;

  /** Set of locked field paths for O(1) lookup. */
  const lockedSet = useMemo<Set<string>>(
    () => new Set(template?.lockedFields ?? []),
    [template?.lockedFields],
  );

  /** Map of editable field paths to their metadata for O(1) lookup. */
  const editableMap = useMemo<Map<string, TemplateField>>(() => {
    const map = new Map<string, TemplateField>();
    for (const field of template?.editableFields ?? []) {
      map.set(field.path, field);
    }
    return map;
  }, [template?.editableFields]);

  const isFieldLocked = useMemo(
    () => (path: string): boolean => {
      if (!isTemplateMode) return false;
      return lockedSet.has(path);
    },
    [isTemplateMode, lockedSet],
  );

  const isFieldEditable = useMemo(
    () => (path: string): boolean => {
      if (!isTemplateMode) return true;
      return editableMap.has(path);
    },
    [isTemplateMode, editableMap],
  );

  const getFieldMeta = useMemo(
    () => (path: string): TemplateField | undefined => {
      if (!isTemplateMode) return undefined;
      return editableMap.get(path);
    },
    [isTemplateMode, editableMap],
  );

  return {
    isTemplateMode,
    template,
    isFieldLocked,
    isFieldEditable,
    getFieldMeta,
  };
}
