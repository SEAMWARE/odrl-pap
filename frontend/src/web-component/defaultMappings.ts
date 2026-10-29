/**
 * The mappings the PAP ships with, used by the embedded editor when it is not
 * allowed to fetch them (`mappings="none"`) and the host did not set the
 * `mappings` property.
 *
 * `defaultMappings.json` is generated from the backend's `mapping.json` by
 * `npm run generate-default-mappings` (also run by `build:component`), so it
 * matches the PAP of the same release. It does not include mappings an
 * operator adds through `paths.mapping`: only a remote fetch sees those.
 */
import type { Mappings } from '../api';
import defaultMappingsJson from './defaultMappings.json';

export const DEFAULT_MAPPINGS: Mappings = defaultMappingsJson;
