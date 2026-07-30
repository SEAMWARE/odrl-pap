# Implementation Plan: Create policies under services

## Overview

Add frontend support for creating policies under existing services. The backend already exposes service CRUD and service-scoped policy endpoints (`POST /service/{service-id}/policy`, etc.). The frontend needs to: (1) regenerate its API client to include `ServiceService`, (2) add a service dropdown to the existing `PolicyEditor` so users can optionally assign a policy to a service, and (3) extend the web component with a `service-id` attribute.

No dedicated Service Management UI is needed. Instead, a dropdown listing existing services is added to the `PolicyEditor`. When a service is selected, the policy is created/saved via `POST /service/{service-id}/policy` (or `PUT /service/{service-id}/policy/{id}`). When no service is selected, the plain `/policy` endpoint is used, preserving the current behavior.

## Steps

### Step 1: Regenerate API Client & Configure Service Proxy

**Goal:** Regenerate the TypeScript OpenAPI client so it includes `ServiceService` with all service and service-policy endpoints, and add `/service` proxy configuration to the Vite dev server and Nginx production config.

**Why first:** Every subsequent step depends on `ServiceService` being available and `/service` API calls being routed correctly.

**Files to modify:**

- `frontend/src/api/` — Regenerate by running `npm run generate-api` in the `frontend/` directory. This reads `api/odrl.yaml` (which already defines all `/service/*` endpoints) and produces a new `ServiceService.ts` alongside the existing `PapService.ts` and `UiService.ts`. The generated `ServiceService` should include:
  - `createService(requestBody: ServiceCreate)` — POST `/service`
  - `getServices(page?, pageSize?)` — GET `/service`
  - `getService(serviceId)` — GET `/service/{service-id}`
  - `deleteService(serviceId)` — DELETE `/service/{service-id}`
  - `createServicePolicy(serviceId, requestBody)` — POST `/service/{service-id}/policy`
  - `getServicePolicies(serviceId, page?, pageSize?)` — GET `/service/{service-id}/policy`
  - `createServicePolicyWithId(serviceId, id, requestBody)` — PUT `/service/{service-id}/policy/{id}`
  - `getServicePolicyById(serviceId, id)` — GET `/service/{service-id}/policy/{id}`
  - `deleteServicePolicyById(serviceId, id)` — DELETE `/service/{service-id}/policy/{id}`
  - `getServicePolicyByUid(serviceId, id)` — GET `/service/{service-id}/policy/odrl/{id}`
  - `deleteServicePolicyByUid(serviceId, id)` — DELETE `/service/{service-id}/policy/odrl/{id}`
- Verify the generated models include `ServiceCreate` (with `id: string`), `Service` (with `id`, `policyPath`, `policies` fields), and `ServiceList` types.

- `frontend/vite.config.ts` — Add `'/service'` to the proxy configuration object alongside the existing `'/mappings'`, `'/policy'`, and `'/validate'` entries. Use the same `proxyConfig` object already defined (lines 12-34).

- `frontend/nginx.conf` — Add a `location /service` block with the same proxy settings as the existing `/policy`, `/mappings`, and `/validate` blocks (proxy_pass to `$PAP_BACKEND_URL`, proxy_set_header for Host, X-Real-IP, X-Forwarded-For, X-Forwarded-Proto, strip Origin/Referer, HTTP/1.1).

**Acceptance criteria:**
- `frontend/src/api/services/ServiceService.ts` exists and contains all service-level API methods listed above
- Generated models include `ServiceCreate`, `Service`, and `ServiceList` types
- `npm run build` succeeds with the regenerated client (no TypeScript errors)
- `npm run lint` passes
- Dev server proxies `/service/*` requests to the backend
- Nginx config proxies `/service/*` requests to the backend in production
- Existing `PapService` and `UiService` clients remain unchanged in behavior

---

### Step 2: Service Dropdown in PolicyEditor

**Goal:** Add a service selection dropdown to the existing `PolicyEditor` page so users can optionally assign a policy to a service. When a service is selected, use the service-scoped policy endpoints; when none is selected, use the plain `/policy` endpoint.

**Files to modify:**

- `frontend/src/pages/PolicyEditor.tsx` — Add service selection:
  - On mount, fetch the list of available services via `ServiceService.getServices()` and store in component state
  - Add a `<Form.Select>` dropdown at the top of the editor (before the existing tabs) with:
    - A default option: "(No service — create standalone policy)" or similar
    - One `<option>` per service, showing the service ID as both value and label
  - Track the selected service ID in component state (e.g., `const [selectedServiceId, setSelectedServiceId] = useState<string | null>(null)`)
  - In the policy load effect (when editing an existing policy):
    - If the policy was loaded via a service context (e.g., from a `serviceId` route param or query param), pre-select that service in the dropdown
  - In `handleSave()`:
    - When `selectedServiceId` is present and creating (no `id`): call `ServiceService.createServicePolicy(selectedServiceId, requestBody)`
    - When `selectedServiceId` is present and editing (has `id`): call `ServiceService.createServicePolicyWithId(selectedServiceId, id, requestBody)`
    - When `selectedServiceId` is null: keep existing `PapService.createPolicy(requestBody)` / `PapService.createPolicyWithId(id, requestBody)` calls
  - Import `ServiceService` from the generated API client
  - Handle errors from the service list fetch gracefully (show the editor without the dropdown if services can't be loaded, or show an inline error)

- `frontend/src/pages/PolicyList.tsx` — (Optional enhancement) Show the service each policy belongs to, if applicable:
  - This is optional and depends on whether the existing `GET /policy` response includes service association info
  - If the response includes service info, add a "Service" column to the policy table

- `frontend/src/i18n/en.ts` — Add i18n strings for the service dropdown:
  ```
  policyEditor: {
    ...existing keys,
    serviceLabel: 'Service',
    serviceNone: '(No service — standalone policy)',
    serviceLoadError: 'Could not load services.',
    serviceTooltip: 'Optionally assign this policy to an existing service. If no service is selected, the policy is created as a standalone policy.',
  }
  ```

**Acceptance criteria:**
- The PolicyEditor page shows a service dropdown populated with existing services from the backend
- Selecting a service and saving a new policy calls `POST /service/{service-id}/policy`
- Selecting a service and saving an existing policy calls `PUT /service/{service-id}/policy/{id}`
- Leaving the dropdown on the default (no service) uses the plain `POST /policy` or `PUT /policy/{id}`
- The dropdown handles empty service lists gracefully (shows only the "no service" option)
- The dropdown handles API errors gracefully (editor still usable without service selection)
- All user-facing strings use the i18n system
- `npm run build` and `npm run lint` succeed
- Root-level policy CRUD continues to work unchanged (no regressions)

---

### Step 3: Web Component Service Support

**Goal:** Extend the `<odrl-policy-editor>` web component with a `service-id` attribute so that host pages can create/edit policies under a specific service without needing the full SPA routing.

**Files to modify:**

- `frontend/src/web-component/EmbeddedContext.tsx`:
  - Add `serviceId: string | null` field to the `EmbeddedConfig` interface (default `null`)
  - The field flows through `EmbeddedProvider` to any component that calls `useEmbeddedContext()`
  - Document the new field with a JSDoc comment

- `frontend/src/web-component/OdrlPolicyEditorElement.ts`:
  - Add `'service-id'` to the `observedAttributes` array (line 88)
  - In `buildConfig()` (line 278), read `this.getAttribute('service-id')` and include as `serviceId` in the returned config
  - Add a private `_serviceId: string | undefined` field
  - Add a JS property `serviceId` with getter/setter (following the pattern of existing `_policyContext` property): setter calls `this.renderReact()` to trigger re-render
  - Update `buildConfig()` to prefer the JS property `_serviceId` over the HTML attribute (like `resolvePolicyContextAttr`)
  - Update the JSDoc table at the top of the file to document the new `service-id` attribute
  - Update the `@example` HTML snippet to show `service-id` usage

- `frontend/src/web-component/EmbeddedApp.tsx`:
  - Extract `serviceId` from `config` alongside `apiBaseUrl`, `authToken`, etc.
  - Import `ServiceService` from `'../api/services/ServiceService'`
  - In the policy load effect: when `serviceId` is present and `mode === 'edit'` and `policyId` is present, use `ServiceService.getServicePolicyById(serviceId, policyId)` instead of `PapService.getPolicyById(policyId)`
  - In `handleSave()`:
    - When `serviceId` is present and `mode === 'create'`: use `ServiceService.createServicePolicy(serviceId, policy)`
    - When `serviceId` is present and `mode === 'edit'`: use `ServiceService.createServicePolicyWithId(serviceId, policyId, policy)`
    - When `serviceId` is absent: keep existing `PapService` calls (backward compatible)

- `frontend/src/web-component/types.d.ts` (exists from ticket-46):
  - Add `service-id` to the attribute documentation in the `OdrlPolicyEditorElement` class definition
  - Add `serviceId: string | undefined` JS property type

- `frontend/src/web-component/OdrlPolicyEditorElement.test.ts`:
  - Add test case verifying `'service-id'` is in `OdrlPolicyEditorElement.observedAttributes`
  - Add test case verifying the `serviceId` JS property setter triggers a re-render

**Acceptance criteria:**
- `<odrl-policy-editor service-id="my-service" mode="create">` creates policies via `POST /service/my-service/policy`
- `<odrl-policy-editor service-id="my-service" mode="edit" policy-id="abc123">` loads via `GET /service/my-service/policy/abc123` and saves via `PUT /service/my-service/policy/abc123`
- Omitting `service-id` preserves existing root-level behavior (fully backward compatible)
- The `serviceId` JS property works as an alternative to the HTML attribute
- `npm run build:component` succeeds (Web Component library build)
- `npm run build` succeeds (SPA build)
- `npm run lint` passes
- Tests pass and cover the new attribute and property
