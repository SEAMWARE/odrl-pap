#!/usr/bin/env node
/**
 * Generates `src/web-component/defaultMappings.json` from the backend's
 * built-in `src/main/resources/mapping.json`.
 *
 * The PAP serves `GET /mappings` from that file (plus an optional,
 * operator-supplied one). The embedded editor uses the generated copy when it
 * has no backend to ask (`mappings="none"`), so a standalone editor offers the
 * same vocabulary as the PAP of the same release. The rego package/method
 * strings stay on the server: only name, description and parameter count are
 * exposed, exactly like the endpoint does.
 *
 * The file is not committed: it is generated on `npm install` and before
 * dev, test and build, so it always matches the `mapping.json` of the checkout
 * (and therefore of the PAP the package is published from).
 *
 * Usage:
 *   node scripts/generate-default-mappings.mjs                # (re)write the file
 *   node scripts/generate-default-mappings.mjs --if-present   # same, but do nothing
 *                                                             # when mapping.json is
 *                                                             # missing (frontend-only
 *                                                             # checkout / Docker build)
 */
import { existsSync, readFileSync, writeFileSync } from 'node:fs';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const root = resolve(dirname(fileURLToPath(import.meta.url)), '..');
export const SOURCE = resolve(root, '../src/main/resources/mapping.json');
export const TARGET = resolve(root, 'src/web-component/defaultMappings.json');

/** `OdrlAttribute` JSON value -> field of the `Mappings` API model. */
const FIELD_BY_ATTRIBUTE = {
  leftOperand: 'leftOperands',
  rightOperand: 'rightOperands',
  operator: 'operators',
  constraint: 'constraints',
  operand: 'operands',
  assignee: 'assignees',
  action: 'actions',
  target: 'targets',
};

// Same rules as RegoMethod#parameterCount in the backend.
const FORMAT_SPECIFIER = /%(\d+\$)?([-#+ 0,(]*)?(\d+)?(\.\d+)?([a-zA-Z])/g;

function parameterCount(regoMethod) {
  if (!regoMethod) return 0;
  return [...regoMethod.replaceAll('%%', '').matchAll(FORMAT_SPECIFIER)].length;
}

/** Builds the `Mappings` payload from the parsed `mapping.json`. */
export function buildMappings(mappingConfiguration) {
  const mappings = {};
  for (const [attribute, field] of Object.entries(FIELD_BY_ATTRIBUTE)) {
    mappings[field] = Object.entries(mappingConfiguration[attribute] ?? {}).flatMap(
      ([namespace, entries]) =>
        Object.entries(entries).map(([key, entry]) => ({
          name: `${namespace}:${key}`,
          description: entry.description,
          parameterCount: parameterCount(entry.regoMethod),
        })),
    );
  }
  return mappings;
}

function main() {
  if (!existsSync(SOURCE)) {
    if (process.argv.includes('--if-present')) return;
    console.error(`Cannot generate the default mappings: ${SOURCE} not found.`);
    process.exit(1);
  }
  writeFileSync(TARGET, `${JSON.stringify(buildMappings(JSON.parse(readFileSync(SOURCE, 'utf8'))), null, 2)}\n`);
  console.log(`Wrote ${TARGET}`);
}

if (process.argv[1] === fileURLToPath(import.meta.url)) main();
