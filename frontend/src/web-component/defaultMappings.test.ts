/**
 * Keeps the generated default mappings in step with the backend's mapping.json.
 */
import { describe, it, expect } from 'vitest';
import { execFileSync } from 'node:child_process';
import { existsSync } from 'node:fs';
import { resolve } from 'node:path';
import { DEFAULT_MAPPINGS } from './defaultMappings';

const SCRIPT = resolve(__dirname, '../../scripts/generate-default-mappings.mjs');
const BACKEND_MAPPING = resolve(__dirname, '../../../src/main/resources/mapping.json');

describe('default mappings', () => {
  it('exposes every attribute the builder reads', () => {
    for (const field of ['actions', 'operators', 'leftOperands', 'rightOperands', 'targets', 'assignees'] as const) {
      expect(DEFAULT_MAPPINGS[field]?.length ?? 0).toBeGreaterThan(0);
    }
  });

  it('does not leak the rego implementation details', () => {
    expect(JSON.stringify(DEFAULT_MAPPINGS)).not.toMatch(/regoMethod|regoPackage/);
  });

  it('counts parameters like the backend', () => {
    const body = DEFAULT_MAPPINGS.leftOperands?.find((m) => m.name === 'http:bodyValue');
    expect(body?.parameterCount).toBe(1);
    const path = DEFAULT_MAPPINGS.leftOperands?.find((m) => m.name === 'http:path');
    expect(path?.parameterCount).toBe(0);
  });

  // The backend file is only there when the whole repository is checked out.
  it.skipIf(!existsSync(BACKEND_MAPPING))(
    'is up to date with the backend mapping.json (run `npm run generate-default-mappings`)',
    () => {
      expect(() => execFileSync('node', [SCRIPT, '--check'], { stdio: 'pipe' })).not.toThrow();
    },
  );
});
