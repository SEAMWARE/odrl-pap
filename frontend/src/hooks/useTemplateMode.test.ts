/**
 * Unit tests for the useTemplateMode hook.
 *
 * Verifies freeform (no template) behaviour, field locking,
 * field editability, and metadata lookup when a template is active.
 */
import { describe, it, expect } from 'vitest';
import { renderHook } from '@testing-library/react';
import { useTemplateMode } from './useTemplateMode';
import type { FieldTemplate, TemplateField } from '../types';

/** Reusable editable field fixture. */
const EDITABLE_TARGET: TemplateField = {
  path: 'odrl:permission.odrl:target',
  label: 'Target Resource',
  description: 'The resource URL to grant access to',
  type: 'text',
  required: true,
};

/** Reusable editable field fixture for constraints. */
const EDITABLE_CONSTRAINTS: TemplateField = {
  path: 'odrl:permission.odrl:constraint',
  label: 'Constraints',
  description: 'Add conditions to the permission',
  type: 'constraint',
  required: false,
};

/** Path that is locked in the test template. */
const LOCKED_ACTION_PATH = 'odrl:permission.odrl:action';

/** Path that is locked in the test template. */
const LOCKED_ASSIGNEE_PATH = 'odrl:permission.odrl:assignee';

/** A sample template used across multiple tests. */
const SAMPLE_TEMPLATE: FieldTemplate = {
  id: 'test-template',
  name: 'Test Template',
  description: 'A template for unit testing',
  category: 'Testing',
  skeleton: {
    '@context': 'http://www.w3.org/ns/odrl/2/',
    '@type': 'odrl:Policy',
    'odrl:permission': {
      'odrl:action': 'odrl:read',
      'odrl:assignee': 'test:party',
    },
  },
  editableFields: [EDITABLE_TARGET, EDITABLE_CONSTRAINTS],
  lockedFields: [LOCKED_ACTION_PATH, LOCKED_ASSIGNEE_PATH],
};

describe('useTemplateMode', () => {
  describe('freeform mode (no template)', () => {
    it('returns isTemplateMode as false', () => {
      const { result } = renderHook(() => useTemplateMode(undefined));
      expect(result.current.isTemplateMode).toBe(false);
    });

    it('returns template as undefined', () => {
      const { result } = renderHook(() => useTemplateMode(undefined));
      expect(result.current.fieldTemplate).toBeUndefined();
    });

    it('isFieldLocked returns false for any path', () => {
      const { result } = renderHook(() => useTemplateMode(undefined));
      expect(result.current.isFieldLocked(LOCKED_ACTION_PATH)).toBe(false);
      expect(result.current.isFieldLocked('any.random.path')).toBe(false);
    });

    it('isFieldEditable returns true for any path', () => {
      const { result } = renderHook(() => useTemplateMode(undefined));
      expect(result.current.isFieldEditable(LOCKED_ACTION_PATH)).toBe(true);
      expect(result.current.isFieldEditable('any.random.path')).toBe(true);
    });

    it('getFieldMeta returns undefined for any path', () => {
      const { result } = renderHook(() => useTemplateMode(undefined));
      expect(result.current.getFieldMeta(LOCKED_ACTION_PATH)).toBeUndefined();
      expect(result.current.getFieldMeta('any.random.path')).toBeUndefined();
    });
  });

  describe('template mode', () => {
    it('returns isTemplateMode as true', () => {
      const { result } = renderHook(() => useTemplateMode(SAMPLE_TEMPLATE));
      expect(result.current.isTemplateMode).toBe(true);
    });

    it('returns the provided template', () => {
      const { result } = renderHook(() => useTemplateMode(SAMPLE_TEMPLATE));
      expect(result.current.fieldTemplate).toBe(SAMPLE_TEMPLATE);
    });

    it('isFieldLocked returns true for locked paths', () => {
      const { result } = renderHook(() => useTemplateMode(SAMPLE_TEMPLATE));
      expect(result.current.isFieldLocked(LOCKED_ACTION_PATH)).toBe(true);
      expect(result.current.isFieldLocked(LOCKED_ASSIGNEE_PATH)).toBe(true);
    });

    it('isFieldLocked returns false for non-locked paths', () => {
      const { result } = renderHook(() => useTemplateMode(SAMPLE_TEMPLATE));
      expect(result.current.isFieldLocked('odrl:permission.odrl:target')).toBe(false);
      expect(result.current.isFieldLocked('unknown.path')).toBe(false);
    });

    it('isFieldEditable returns true for editable paths', () => {
      const { result } = renderHook(() => useTemplateMode(SAMPLE_TEMPLATE));
      expect(result.current.isFieldEditable('odrl:permission.odrl:target')).toBe(true);
      expect(result.current.isFieldEditable('odrl:permission.odrl:constraint')).toBe(true);
    });

    it('isFieldEditable returns false for non-editable paths', () => {
      const { result } = renderHook(() => useTemplateMode(SAMPLE_TEMPLATE));
      expect(result.current.isFieldEditable(LOCKED_ACTION_PATH)).toBe(false);
      expect(result.current.isFieldEditable('unknown.path')).toBe(false);
    });

    it('getFieldMeta returns metadata for editable fields', () => {
      const { result } = renderHook(() => useTemplateMode(SAMPLE_TEMPLATE));
      const meta = result.current.getFieldMeta('odrl:permission.odrl:target');
      expect(meta).toEqual(EDITABLE_TARGET);
    });

    it('getFieldMeta returns undefined for locked fields', () => {
      const { result } = renderHook(() => useTemplateMode(SAMPLE_TEMPLATE));
      expect(result.current.getFieldMeta(LOCKED_ACTION_PATH)).toBeUndefined();
    });

    it('getFieldMeta returns undefined for unknown paths', () => {
      const { result } = renderHook(() => useTemplateMode(SAMPLE_TEMPLATE));
      expect(result.current.getFieldMeta('unknown.path')).toBeUndefined();
    });
  });

  describe('template with empty arrays', () => {
    const EMPTY_TEMPLATE: FieldTemplate = {
      id: 'empty',
      name: 'Empty',
      description: '',
      category: 'Test',
      skeleton: {},
      editableFields: [],
      lockedFields: [],
    };

    it('isFieldLocked returns false when lockedFields is empty', () => {
      const { result } = renderHook(() => useTemplateMode(EMPTY_TEMPLATE));
      expect(result.current.isFieldLocked('any.path')).toBe(false);
    });

    it('isFieldEditable returns false when editableFields is empty', () => {
      const { result } = renderHook(() => useTemplateMode(EMPTY_TEMPLATE));
      expect(result.current.isFieldEditable('any.path')).toBe(false);
    });

    it('isTemplateMode is still true', () => {
      const { result } = renderHook(() => useTemplateMode(EMPTY_TEMPLATE));
      expect(result.current.isTemplateMode).toBe(true);
    });
  });
});
