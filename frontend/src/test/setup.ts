/**
 * Global test setup for Vitest.
 *
 * - Registers @testing-library/jest-dom matchers (e.g., toBeInTheDocument).
 * - Starts / resets / stops the MSW mock server around each test.
 */
import '@testing-library/jest-dom/vitest';
import { server } from './mocks/server';

beforeAll(() => {
  server.listen({ onUnhandledRequest: 'warn' });
});

afterEach(() => {
  server.resetHandlers();
});

afterAll(() => {
  server.close();
});
