# Implementation Plan: Create policies under services

## Overview

Add frontend support for creating and managing policies at the service level, using `POST /service/{service-id}/policy` and related service endpoints that already exist in the backend. The work includes regenerating the TypeScript API client to expose `ServiceService`, building service management pages, updating `PolicyEditor` and `PolicyList` to work within a service context, and extending the web component with a `service-id` attribute.

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

### Step 2: Service Management UI — List, Create, Delete

**Goal:** Create a service management page where users can view all services, create new services, and delete existing services. Add routing and navigation so users can access services from the navbar.

**Files to create:**

- `frontend/src/pages/ServiceList.tsx` — New page component that:
  - Fetches all services via `ServiceService.getServices()` on mount
  - Displays services in a `react-bootstrap` `Table` with columns: Service ID, Policy Path, Actions
  - Provides a "New Service" inline form (input field + create button) that prompts for a service ID string and calls `ServiceService.createService({ id: serviceId })`
  - Each row has a "View Policies" link (`<Link to={`/services/${serviceId}`}>`) and a "Delete" button
  - Delete button calls `ServiceService.deleteService(serviceId)` with a confirmation (warn that all policies under the service will also be deleted)
  - Show an empty-state message when no services exist
  - Handle and display API errors (e.g., 409 for duplicate service ID)
  - Follow the same patterns as `PolicyList.tsx` (hooks, state management, error handling, table layout)
  - All user-facing strings must use the i18n system

**Files to modify:**

- `frontend/src/App.tsx` — Add new route inside the `<Route path="/" element={<Layout />}>` block:
  - `<Route path="services" element={<ServiceList />} />`
  - Import `ServiceList` at the top of the file

- `frontend/src/components/Layout.tsx` — Add a "Services" `Nav.Link` to the navbar, placed after the existing "Policies" link:
  - `<Nav.Link href="/services">Services</Nav.Link>`

- `frontend/src/i18n/en.ts` — Add a new `serviceList` section to the `en` object:
  ```
  serviceList: {
    title: 'Services',
    newService: 'New Service',
    columnId: 'Service ID',
    columnPolicyPath: 'Policy Path',
    columnActions: 'Actions',
    viewPolicies: 'View Policies',
    createService: 'Create Service',
    serviceIdLabel: 'Service ID',
    serviceIdPlaceholder: 'Enter a unique service identifier',
    confirmDelete: 'Are you sure you want to delete this service? All policies under it will also be deleted.',
    deleteSuccess: 'Service deleted successfully.',
    createSuccess: 'Service created successfully.',
    emptyState: 'No services found. Create one to organize policies.',
    createError: 'Failed to create service.',
    deleteError: 'Failed to delete service.',
  }
  ```

**Acceptance criteria:**
- Navigating to `/services` shows a table of all services from the backend
- Users can create a new service by entering an ID and clicking "Create"
- Creating a service with a duplicate ID shows an error (409 response handling)
- Users can delete a service with a confirmation warning about cascading policy deletion
- The navbar has a "Services" link that navigates to `/services`
- Empty state message displayed when no services exist
- `npm run build` and `npm run lint` succeed
- All visible text uses the i18n system

---

### Step 3: Service-Scoped Policy CRUD

**Goal:** Enable creating, listing, editing, and deleting policies within a specific service context. Reuse the existing `PolicyEditor` component and create a service detail page that shows a service's policies.

**Files to create:**

- `frontend/src/pages/ServiceDetail.tsx` — New page component that:
  - Reads `serviceId` from route params via `useParams()`
  - Fetches the service info via `ServiceService.getService(serviceId)` on mount
  - Displays service ID and policy path as a page header/breadcrumb
  - Lists the service's policies in a table (same format as `PolicyList.tsx`) fetched via `ServiceService.getServicePolicies(serviceId)`
  - Provides a "New Policy" button linking to `/services/${serviceId}/policies/new`
  - Each policy row has:
    - "Edit" link → `/services/${serviceId}/policies/edit/${policyId}`
    - "Delete" button → calls `ServiceService.deleteServicePolicyById(serviceId, policyId)`
  - Includes breadcrumb navigation: `Services > {serviceId}`
  - "Back to Services" link at the top
  - Handle 404 (service not found) with an appropriate message
  - Follow patterns from `PolicyList.tsx` for table rendering and state management
  - All user-facing strings must use the i18n system

**Files to modify:**

- `frontend/src/pages/PolicyEditor.tsx` — Make service-aware:
  - Read an optional `serviceId` from route params: update destructuring to `const { id, serviceId } = useParams()`
  - In the policy load effect (lines 132-140):
    - When `serviceId` is present and `id` is present: call `ServiceService.getServicePolicyById(serviceId, id)` instead of `PapService.getPolicyById(id)`
    - When `serviceId` is absent: keep existing `PapService.getPolicyById(id)` behavior
  - In `handleSave()` (lines 226-237):
    - When `serviceId` is present and creating (no `id`): call `ServiceService.createServicePolicy(serviceId, requestBody)`
    - When `serviceId` is present and editing (has `id`): call `ServiceService.createServicePolicyWithId(serviceId, id, requestBody)`
    - When `serviceId` is absent: keep existing `PapService` calls
  - Update all `navigate('/')` calls: when `serviceId` is present, navigate to `/services/${serviceId}` instead of `/`
  - Display service context: when `serviceId` is present, show breadcrumb `Services > {serviceId} > New Policy / Edit Policy` and update the page title (e.g., "New Policy — Service: {serviceId}")
  - Import `ServiceService` from the generated API client

- `frontend/src/App.tsx` — Add the service-scoped routes inside the `<Route path="/" element={<Layout />}>` block:
  - `<Route path="services/:serviceId" element={<ServiceDetail />} />`
  - `<Route path="services/:serviceId/policies/new" element={<PolicyEditor />} />`
  - `<Route path="services/:serviceId/policies/edit/:id" element={<PolicyEditor />} />`
  - Import `ServiceDetail` at the top of the file

- `frontend/src/i18n/en.ts` — Add a `serviceDetail` section to the `en` object:
  ```
  serviceDetail: {
    title: 'Service: {serviceId}',
    policyPath: 'Policy Path',
    policiesTitle: 'Policies',
    newPolicy: 'New Policy',
    backToServices: 'Back to Services',
    notFound: 'Service not found.',
    noPolicies: 'No policies in this service. Create one to get started.',
    breadcrumbServices: 'Services',
    breadcrumbNewPolicy: 'New Policy',
    breadcrumbEditPolicy: 'Edit Policy',
  }
  ```

**Acceptance criteria:**
- Navigating to `/services/my-service` shows the service detail with its policies listed
- Clicking "New Policy" on the service detail page navigates to `/services/my-service/policies/new`
- Creating a policy from that page calls `POST /service/my-service/policy`
- Editing a service-scoped policy loads via `GET /service/my-service/policy/{id}` and saves via `PUT /service/my-service/policy/{id}`
- Deleting a service-scoped policy calls `DELETE /service/my-service/policy/{id}`
- Root-level policy CRUD (`/`, `/new`, `/edit/:id`) continues to work unchanged (no regressions)
- Cancel in service-scoped PolicyEditor navigates back to `/services/my-service` (not `/`)
- Breadcrumb navigation shows the service context when in service-scoped routes
- 404 handling when service does not exist
- `npm run build` and `npm run lint` succeed

---

### Step 4: Web Component Service Support

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
  - Extract `serviceId` from `config` alongside `apiBaseUrl`, `authToken`, etc. (line 98)
  - Import `ServiceService` from `'../api/services/ServiceService'`
  - In the policy load effect (lines ~122-127): when `serviceId` is present and `mode === 'edit'` and `policyId` is present, use `ServiceService.getServicePolicyById(serviceId, policyId)` instead of `PapService.getPolicyById(policyId)`
  - In `handleSave()` (lines ~229-244):
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
