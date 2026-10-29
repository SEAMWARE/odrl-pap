/**
 * The dark preset exists twice: as `darkTheme` (used by the standalone app) and
 * as the `:host([theme="dark"])` block of theme.css (used by the embedded
 * editor, so a page can override tokens with plain CSS). They must not drift.
 */
import { describe, it, expect } from 'vitest';
import { readFileSync } from 'node:fs';
import { resolve } from 'node:path';
import { darkTheme, lightTheme } from './defaultTheme';

const css = readFileSync(resolve(__dirname, 'theme.css'), 'utf8');

/** Declarations of the first rule whose selector is exactly `selector`. */
function declarations(selector: string): Record<string, string> {
  const start = css.indexOf(`${selector} {`);
  const body = css.slice(css.indexOf('{', start) + 1, css.indexOf('}', start));
  return Object.fromEntries(
    [...body.matchAll(/--([a-z-]+):\s*([^;]+);/g)].map(([, name, value]) => [name, value.trim()]),
  );
}

describe('theme.css presets', () => {
  it('declares the dark preset on the host exactly like darkTheme', () => {
    expect(declarations(':host([theme="dark"])')).toEqual(darkTheme);
  });

  it('declares the light preset like lightTheme', () => {
    expect(declarations(':root')).toEqual(lightTheme);
  });

  it('clips the horizontal overflow of grid rows so a plain editor gets no horizontal scrollbar', () => {
    const start = css.indexOf('.odrl-embedded-root {');
    const rule = css.slice(start, css.indexOf('}', start));
    expect(rule).toMatch(/overflow-x:\s*clip/);
  });
});
