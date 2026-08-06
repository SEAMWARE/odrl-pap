/**
 * MSW mock server instance for use in Vitest tests.
 *
 * This server intercepts HTTP requests and responds with the handlers
 * defined in ./handlers.ts. It runs in-process (no network), making
 * tests fast and deterministic.
 */
import { setupServer } from 'msw/node';
import { handlers } from './handlers';

/** MSW server configured with the default request handlers. */
export const server = setupServer(...handlers);
