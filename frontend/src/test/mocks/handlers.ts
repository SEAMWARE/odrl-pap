/**
 * MSW (Mock Service Worker) request handlers for API mocking in tests.
 *
 * Provides realistic mock data for the `/mappings` and `/validate` endpoints,
 * matching the Mappings and ValidationResponse schemas from api/odrl.yaml.
 * Supports both HTTP Request (testRequest) and JSON Payload (jsonInput) modes.
 */
import { http, HttpResponse } from 'msw';
import type { Mappings, ValidationResponse } from '../../api';

/** Realistic mock mappings data matching the backend /mappings response. */
export const MOCK_MAPPINGS: Mappings = {
  actions: [
    { name: 'odrl:use', description: 'To use a resource' },
    { name: 'odrl:read', description: 'To read a resource' },
    { name: 'odrl:modify', description: 'To modify a resource' },
    { name: 'odrl:delete', description: 'To delete a resource' },
    { name: 'dome-op:access', description: 'To access a DOME resource' },
  ],
  operators: [
    { name: 'odrl:eq', description: 'Equal to' },
    { name: 'odrl:neq', description: 'Not equal to' },
    { name: 'odrl:gt', description: 'Greater than' },
    { name: 'odrl:lt', description: 'Less than' },
    { name: 'odrl:gteq', description: 'Greater than or equal to' },
    { name: 'odrl:lteq', description: 'Less than or equal to' },
    { name: 'odrl:isPartOf', description: 'Is part of' },
    { name: 'odrl:isAllOf', description: 'Is all of' },
    { name: 'odrl:isAnyOf', description: 'Is any of' },
    { name: 'http:path_operator', description: 'HTTP path matching operator' },
  ],
  operands: [
    { name: 'odrl:leftOperand', description: 'Left operand of a constraint' },
  ],
  rightOperands: [
    { name: 'odrl:rightOperand', description: 'Right operand value' },
  ],
  leftOperands: [
    { name: 'dome-op:role', description: 'Role of the requesting party' },
    { name: 'dome-op:serviceName', description: 'Name of the service' },
    {
      name: 'tmf:resource',
      description: 'TM Forum resource identifier',
    },
    {
      name: 'http:body_value',
      description: 'Value extracted from the HTTP request body',
    },
    {
      name: 'vc:role',
      description: 'Role from a Verifiable Credential',
    },
  ],
  assignees: [
    { name: 'odrl:assignee', description: 'Party receiving the permission' },
  ],
  targets: [
    { name: 'odrl:target', description: 'Target asset of the policy' },
  ],
  constraints: [
    { name: 'odrl:and', description: 'All constraints must be satisfied' },
    { name: 'odrl:or', description: 'At least one constraint must be satisfied' },
    { name: 'odrl:xone', description: 'Exactly one constraint must be satisfied' },
  ],
};

/** Mock validation response indicating an allowed request. */
export const MOCK_VALIDATION_ALLOW: ValidationResponse = {
  allow: true,
  explanation: [],
};

/** Mock validation response indicating a denied request. */
export const MOCK_VALIDATION_DENY: ValidationResponse = {
  allow: false,
  explanation: [
    'Policy evaluation failed: constraint not satisfied',
    'Required role "admin" not found in credential',
  ],
};

/**
 * Default MSW request handlers.
 *
 * - GET /mappings — returns realistic mock Mappings data
 * - POST /validate — returns allow/deny based on request body content.
 *   Supports both testRequest (HTTP mode) and jsonInput (JSON payload mode).
 */
export const handlers = [
  http.get('/mappings', () => {
    return HttpResponse.json(MOCK_MAPPINGS);
  }),

  http.post('/validate', async ({ request }) => {
    const body = await request.json() as Record<string, unknown>;
    // Return "allow" if a policy is present, "deny" otherwise (simple heuristic for tests)
    if (body?.policy) {
      return HttpResponse.json(MOCK_VALIDATION_ALLOW);
    }
    return HttpResponse.json(MOCK_VALIDATION_DENY);
  }),
];
