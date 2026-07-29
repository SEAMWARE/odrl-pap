# ODRL PAP Frontend

[![npm](https://img.shields.io/npm/v/@seamware/odrl-policy-editor)](https://www.npmjs.com/package/@seamware/odrl-policy-editor)

A React-based frontend for the ODRL Policy Administration Point (PAP). It
provides a visual, form-driven policy editor that guides users through creating
[ODRL](https://www.w3.org/TR/odrl-model/) policies step by step.

The editor can be used in two ways:

1. **Standalone SPA** — a full single-page application with page routing,
   served via Nginx or any static file server.
2. **Embeddable Web Component** — the `<odrl-policy-editor>` custom element
   can be dropped into any HTML page (e.g., BAE-Marketplace, FDSC-Dashboard)
   with Shadow DOM style isolation and zero framework dependencies on the
   host page.

---

## Table of Contents

- [Installation](#installation)
- [Quick Integration](#quick-integration)
- [Docker](#docker)
- [Integration Guides](#integration-guides)
- [Prerequisites](#prerequisites)
- [Quick Start (Development)](#quick-start-development)
- [Standalone SPA Usage](#standalone-spa-usage)
  - [Building for Production](#building-for-production)
  - [Docker Deployment](#docker-deployment)
  - [Configuration](#configuration)
- [Embedding as a Web Component](#embedding-as-a-web-component)
  - [Building the Web Component Bundle](#building-the-web-component-bundle)
  - [Basic Usage](#basic-usage)
  - [HTML Attributes](#html-attributes)
  - [JavaScript Properties](#javascript-properties)
  - [Custom Events](#custom-events)
  - [Full Embedding Example](#full-embedding-example)
- [Theming](#theming)
  - [Light and Dark Mode](#light-and-dark-mode)
  - [Custom Theme Properties](#custom-theme-properties)
  - [Theming in Standalone Mode](#theming-in-standalone-mode)
  - [Theming in Embedded Mode](#theming-in-embedded-mode)
- [Internationalization (i18n)](#internationalization-i18n)
  - [Changing the Locale](#changing-the-locale)
  - [Providing Custom Translations](#providing-custom-translations)
  - [Translation Keys Reference](#translation-keys-reference)
- [Authentication](#authentication)
  - [Standalone Mode Authentication](#standalone-mode-authentication)
  - [Embedded Mode Authentication](#embedded-mode-authentication)
  - [API Base URL Resolution](#api-base-url-resolution)
- [Testing](#testing)
- [Regenerating the API Client](#regenerating-the-api-client)

---

## Installation

Install the embeddable Web Component via npm:

```bash
npm install @seamware/odrl-policy-editor
```

Or load it directly from a CDN — no build step required:

```html
<!-- unpkg -->
<script type="module" src="https://unpkg.com/@seamware/odrl-policy-editor@latest"></script>

<!-- jsDelivr (alternative) -->
<script type="module" src="https://cdn.jsdelivr.net/npm/@seamware/odrl-policy-editor@latest"></script>
```

---

## Quick Integration

Drop this snippet into any HTML page to get a working policy editor:

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <script type="module" src="https://unpkg.com/@seamware/odrl-policy-editor@latest"></script>
</head>
<body>
  <odrl-policy-editor
    api-base-url="https://your-pap-backend.example.com"
    mode="create"
    theme="light"
    locale="en"
  ></odrl-policy-editor>
  <script>
    document.querySelector('odrl-policy-editor')
      .addEventListener('policy-created', (e) => console.log('Created:', e.detail.id));
  </script>
</body>
</html>
```

See [Embedding as a Web Component](#embedding-as-a-web-component) for full
attribute/property/event documentation.

---

## Docker

A pre-built frontend Docker image is available:

```bash
docker pull quay.io/fiware/odrl-pap-frontend:<version>
```

Run the container with the PAP backend URL:

```bash
docker run -p 8080:80 \
  -e PAP_BACKEND_URL=http://odrl-pap:8080 \
  quay.io/fiware/odrl-pap-frontend:<version>
```

See [Docker Deployment](#docker-deployment) for full configuration details.

---

## Integration Guides

Detailed, step-by-step guides for embedding the Web Component in specific
platforms:

- **[FDSC-Dashboard (Vue 3 + Vuetify)](docs/integration-fdsc-dashboard.md)** —
  Vue wrapper component with OIDC auth token and Vuetify theme bridging
- **[BAE Logic Proxy](docs/integration-bae-logic-proxy.md)** —
  CDN and npm integration options for the BAE marketplace portal
- **[Generic Integration](docs/integration-generic.md)** —
  Vanilla JS, React, Angular, Vue 2/3, Svelte, SSR, security (CSP), and
  performance optimization

Working examples are available in the [`examples/`](examples/) directory:

| Example | Description |
|---------|-------------|
| [`vanilla-integration/`](examples/vanilla-integration/) | Standalone HTML page with CDN loading and a config panel |
| [`vue-integration/`](examples/vue-integration/) | Copy-pasteable Vue 3 wrapper component |

---

## Prerequisites

- **Node.js** >= 18 and **npm** >= 9
- A running instance of the ODRL PAP backend (default: `http://localhost:8080`)

---

## Quick Start (Development)

The development server provides hot module replacement (HMR) so that changes to
source files are reflected in the browser immediately, and a built-in proxy that
forwards API requests to the backend.

```bash
# 1. Install dependencies
cd frontend
npm install

# 2. Copy the environment template and adjust if needed
cp .env.example .env
#    Edit .env to set VITE_API_PROXY_TARGET to your backend URL (default: http://localhost:8080)

# 3. Start the Vite dev server
npm run dev
```

The editor opens at **http://localhost:5173**. API calls to `/mappings`,
`/policy`, and `/validate` are proxied to the backend specified by
`VITE_API_PROXY_TARGET` in `.env`.

### Starting the Backend

If you do not already have a backend running, start one from the repository root:

```bash
# From the repository root
./mvnw quarkus:dev
```

This starts the PAP backend on `http://localhost:8080` with live-reload.

---

## Standalone SPA Usage

The standalone application renders three routes:

| Route        | Description                                 |
|-------------|---------------------------------------------|
| `/`         | Policy list — view, edit, and delete policies |
| `/new`      | Create a new policy via the guided builder   |
| `/edit/:id` | Edit an existing policy by its ID            |

### Building for Production

```bash
npm run build
```

This creates a `dist/` directory with optimized static assets ready to deploy
behind any HTTP server (Nginx, Apache, Caddy, a CDN, etc.). The server must be
configured for SPA routing (all unknown paths serve `index.html`).

### Docker Deployment

A multi-stage `Dockerfile` is provided. The container includes an Nginx reverse
proxy that forwards API requests to the PAP backend, so the browser never makes
cross-origin requests (no CORS issues).

```bash
# 1. Build the image
docker build -t odrl-pap-frontend .

# 2. Run the container — set PAP_BACKEND_URL to the backend's internal address
docker run -p 8080:80 \
  -e PAP_BACKEND_URL=http://odrl-pap:8080 \
  odrl-pap-frontend
```

The frontend is served at `http://localhost:8080`. API requests to `/policy`,
`/mappings`, and `/validate` are transparently proxied to the backend specified
by `PAP_BACKEND_URL`.

The container uses Nginx with:

- **API reverse proxy** — forwards `/policy`, `/mappings`, `/validate` to the
  backend, avoiding CORS issues
- **SPA fallback** — `try_files $uri $uri/ /index.html`
- **Gzip compression** — enabled for JS, CSS, JSON, SVG
- **Cache headers** — hashed assets cached for 1 year; `index.html` never cached
- **Health check** — `GET /healthz` returns `200 OK`

The `PAP_BACKEND_URL` environment variable is injected **at container startup**
via `envsubst` into the Nginx config, so a single image can be reused across
environments without rebuilding.

### Configuration

Environment variables are defined in a `.env` file (copy `.env.example` as a
starting point).

| Variable               | Purpose                                              | Default                |
|------------------------|------------------------------------------------------|------------------------|
| `VITE_API_PROXY_TARGET`| Backend URL used by the Vite dev-server proxy        | `http://localhost:8080` |
| `PAP_BACKEND_URL`      | Backend URL for the Nginx reverse proxy (Docker)     | _(required in Docker)_ |
| `VITE_API_BASE_URL`    | Override API base URL in the browser (rarely needed) | `""` (empty = proxy)   |
| `VITE_ODRL_CONTEXT`    | Default `@context` for new policies (JSON string)    | `{"odrl":"http://www.w3.org/ns/odrl/2/"}` |

- **Development mode** — the Vite dev server proxies `/mappings`, `/policy`,
  and `/validate` to the backend specified by `VITE_API_PROXY_TARGET`.
- **Docker / Production** — Nginx reverse-proxies API paths to `PAP_BACKEND_URL`.
  The frontend uses relative paths (empty base URL) so all requests go through
  the same origin, avoiding CORS issues entirely.
- **`VITE_API_BASE_URL`** — only needed if the frontend must call a different
  origin than the Nginx proxy (e.g., an external API gateway). In the normal
  proxy setup this variable should be left unset.

### Preview Mode

Preview mode builds the production bundle and serves it locally:

```bash
npm run build
npm run preview
```

The preview server starts at **http://localhost:4173**.

---

## Embedding as a Web Component

The policy editor can be embedded in any HTML page as a standard
[Web Component](https://developer.mozilla.org/en-US/docs/Web/API/Web_components).
The `<odrl-policy-editor>` custom element uses Shadow DOM for complete
style isolation — host-page CSS does not leak in, and editor styles do not
leak out.

### Building the Web Component Bundle

```bash
npm run build:component
```

This produces a single self-contained ES module at
`dist-component/odrl-policy-editor.js`. The bundle inlines all dependencies
(React, Bootstrap CSS, theme CSS) so there are **zero external dependencies**.

### Basic Usage

```html
<!-- Load the bundle -->
<script type="module" src="path/to/odrl-policy-editor.js"></script>

<!-- Use the element -->
<odrl-policy-editor
  api-base-url="https://pap-api.example.com"
  auth-token="eyJhbGciOiJSUzI1NiIs..."
  mode="create"
  theme="light"
  locale="en"
></odrl-policy-editor>
```

### HTML Attributes

All configuration is passed via standard HTML attributes. Changing an attribute
at runtime triggers a re-render automatically.

| Attribute       | Description                                   | Default    |
|-----------------|-----------------------------------------------|------------|
| `api-base-url`  | PAP API base URL                              | `""`       |
| `auth-token`    | Bearer token for API authentication           | `null`     |
| `mode`          | `"create"` or `"edit"`                        | `"create"` |
| `policy-id`     | Policy ID to load (required when `mode="edit"`) | `null`   |
| `theme`         | `"light"` or `"dark"`                         | `"light"`  |
| `locale`        | Language code (e.g., `"en"`, `"de"`)          | `"en"`     |
| `policy-context`| Default `@context` for new policies (JSON string) | `{"odrl":"http://www.w3.org/ns/odrl/2/"}` |

```javascript
// Attributes can be changed at runtime
const editor = document.querySelector('odrl-policy-editor');
editor.setAttribute('theme', 'dark');
editor.setAttribute('locale', 'de');
editor.setAttribute('api-base-url', 'https://new-api.example.com');
```

### JavaScript Properties

For richer configuration that cannot be expressed as string attributes, use
JavaScript properties on the element:

| Property      | Type                         | Description                                          |
|---------------|------------------------------|------------------------------------------------------|
| `i18nStrings` | `DeepPartial<I18nStrings>`   | Partial i18n overrides (deep-merged with defaults)   |
| `themeConfig` | `Partial<ThemeConfig>`       | Partial theme CSS custom property overrides           |
| `template`    | `PolicyTemplate`             | Pre-fills the form and optionally locks fields        |
| `policyContext` | `Record<string, string>`   | Default `@context` for new policies (takes precedence over attribute) |

```javascript
const editor = document.querySelector('odrl-policy-editor');

// Override specific translation strings
editor.i18nStrings = {
  policyBuilder: { title: 'Richtlinien-Editor' },
  common: { save: 'Speichern', cancel: 'Abbrechen' },
};

// Override specific theme properties
editor.themeConfig = {
  'odrl-primary-color': '#1a73e8',
  'odrl-secondary-color': '#e8710a',
};

// Pre-fill and constrain the editor with a template
editor.template = {
  id: 'dome-access',
  name: 'DOME Marketplace Access',
  description: 'Grants access to a DOME resource',
  category: 'DOME',
  skeleton: {
    '@context': 'http://www.w3.org/ns/odrl/2/',
    '@type': 'odrl:Policy',
    'odrl:permission': { 'odrl:action': 'odrl:use' },
  },
  editableFields: [
    { path: 'odrl:permission.odrl:target', label: 'Target Resource' },
  ],
  lockedFields: ['odrl:permission.odrl:action'],
};
```

### Custom Events

The Web Component communicates with the host page via
[Custom Events](https://developer.mozilla.org/en-US/docs/Web/API/CustomEvent).
Events bubble and cross Shadow DOM boundaries (`composed: true`).

| Event              | Detail payload                              | Fired when                       |
|--------------------|---------------------------------------------|----------------------------------|
| `policy-created`   | `{ policy: OdrlPolicyJson, id: string }`    | A new policy is saved            |
| `policy-updated`   | `{ policy: OdrlPolicyJson, id: string }`    | An existing policy is updated    |
| `policy-validated` | `{ result: ValidationResponse }`            | Policy validation completes      |
| `editor-cancelled` | `{}`                                        | The user clicks Cancel           |

```javascript
const editor = document.querySelector('odrl-policy-editor');

editor.addEventListener('policy-created', (e) => {
  console.log('New policy saved:', e.detail.id);
  console.log('Policy JSON:', e.detail.policy);
});

editor.addEventListener('policy-validated', (e) => {
  const allowed = e.detail.result.allowed;
  console.log('Validation result:', allowed ? 'ALLOWED' : 'DENIED');
});

editor.addEventListener('editor-cancelled', () => {
  // Hide the editor or navigate away
  document.querySelector('odrl-policy-editor').remove();
});
```

### Full Embedding Example

A complete working example is available at `examples/embed-example.html`. To run
it:

```bash
cd frontend
npm install
npm run build:component
npx serve .
# Open http://localhost:3000/examples/embed-example.html
```

The example demonstrates:

- Attribute-driven configuration (theme, locale, API URL)
- Runtime attribute changes via a control panel
- Custom event logging
- i18n string overrides via the `i18nStrings` JS property
- Theme overrides via the `themeConfig` JS property

---

## Theming

The frontend uses CSS custom properties (CSS variables) for all visual
styling. This allows full visual customization without modifying source code.

### Light and Dark Mode

Two built-in presets are available: **light** (default) and **dark**.

**Standalone mode** — the `ThemeProvider` component wraps the application and
applies CSS custom properties on `document.documentElement`.

**Embedded mode** — set the `theme` attribute on the custom element:

```html
<!-- Light mode (default) -->
<odrl-policy-editor theme="light"></odrl-policy-editor>

<!-- Dark mode -->
<odrl-policy-editor theme="dark"></odrl-policy-editor>
```

### Custom Theme Properties

Both presets define the following CSS custom properties. Override any of them
to match your brand:

| CSS Custom Property         | Description                       | Light Default        | Dark Default         |
|-----------------------------|-----------------------------------|----------------------|----------------------|
| `--odrl-primary-color`      | Primary brand color               | `#0B2B40`            | `#1a4a6b`            |
| `--odrl-secondary-color`    | Accent / highlight color          | `#F07D00`            | `#F07D00`            |
| `--odrl-bg-color`           | Page / container background       | `#FFFFFF`            | `#1a1a2e`            |
| `--odrl-text-color`         | Default text color                | `#333333`            | `#e0e0e0`            |
| `--odrl-text-muted`         | Muted / secondary text            | `#6c757d`            | `#adb5bd`            |
| `--odrl-card-bg`            | Card and panel background         | `#f8f9fa`            | `#16213e`            |
| `--odrl-border-color`       | Default border color              | `#dee2e6`            | `#2a2a4a`            |
| `--odrl-font-family`        | Base font family                  | `'Lato', sans-serif` | `'Lato', sans-serif` |
| `--odrl-border-radius`      | Default border radius             | `0.375rem`           | `0.375rem`           |
| `--odrl-section-header-bg`  | Section / step header background  | `#e9ecef`            | `#0f3460`            |
| `--odrl-success-color`      | Success indicator                 | `#198754`            | `#20c997`            |
| `--odrl-danger-color`       | Danger / error indicator          | `#dc3545`            | `#e74c3c`            |
| `--odrl-info-color`         | Info indicator                    | `#0dcaf0`            | `#17a2b8`            |
| `--odrl-card-shadow`        | Card drop shadow                  | `0 0.125rem 0.25rem rgba(0,0,0,0.075)` | `0 0.125rem 0.25rem rgba(0,0,0,0.3)` |

### Theming in Standalone Mode

In standalone mode the `ThemeProvider` applies CSS custom properties to the
document root. You can override them in your own stylesheet:

```css
:root {
  --odrl-primary-color: #1a73e8;
  --odrl-secondary-color: #e8710a;
  --odrl-font-family: 'Inter', sans-serif;
}
```

### Theming in Embedded Mode

In embedded mode, use the `themeConfig` JavaScript property to override
specific properties. Overrides are merged on top of the selected preset:

```javascript
const editor = document.querySelector('odrl-policy-editor');

// Start from dark preset, then override specific values
editor.setAttribute('theme', 'dark');
editor.themeConfig = {
  'odrl-primary-color': '#1a73e8',
  'odrl-bg-color': '#121212',
  'odrl-card-bg': '#1e1e1e',
};
```

Because the Web Component uses Shadow DOM, your overrides only affect the
editor — they will not interfere with the host page's styles.

---

## Internationalization (i18n)

The frontend ships with English (`en`) as the default and only built-in locale.
The i18n system supports runtime locale switching and partial string overrides,
making it easy to add new languages without rebuilding.

### Changing the Locale

**Standalone mode** — the `I18nProvider` accepts a `locale` prop. To add a new
language, pass the locale code and a partial or full set of translated strings:

```tsx
<I18nProvider locale="de" strings={germanStrings}>
  <App />
</I18nProvider>
```

**Embedded mode** — set the `locale` attribute on the custom element:

```html
<odrl-policy-editor locale="de"></odrl-policy-editor>
```

The `locale` attribute can be changed at runtime and the UI will re-render.

### Providing Custom Translations

Translations are supplied as a nested object matching the structure of the
English defaults. You only need to provide the keys you want to override —
missing keys fall back to English automatically (deep merge).

**Embedded mode** — use the `i18nStrings` JavaScript property:

```javascript
const editor = document.querySelector('odrl-policy-editor');
editor.setAttribute('locale', 'de');
editor.i18nStrings = {
  common: {
    save: 'Speichern',
    cancel: 'Abbrechen',
    delete: 'Loschen',
    loading: 'Wird geladen...',
  },
  policyBuilder: {
    title: 'Richtlinien-Editor',
    stepTarget: 'Ziel',
    stepTargetHelp: 'Definieren Sie, auf welche Ressource diese Richtlinie anwendbar ist.',
    stepAssignee: 'Berechtigter',
    stepAssigneeHelp: 'Geben Sie an, wem die Berechtigung erteilt oder verweigert wird.',
    stepAction: 'Aktion',
    stepActionHelp: 'Wahlen Sie die erlaubte oder verbotene Operation.',
    stepConstraints: 'Einschrankungen',
    stepConstraintsHelp: 'Fugen Sie Bedingungen hinzu, die erfullt sein mussen.',
  },
  policyList: {
    title: 'Richtlinien',
    newPolicy: 'Neue Richtlinie',
  },
};
```

To reset to English defaults, set `i18nStrings` to `undefined`:

```javascript
editor.i18nStrings = undefined;
```

### Translation Keys Reference

The i18n system organizes strings by UI section. Below are the top-level
sections with representative keys (see `src/i18n/en.ts` for the complete
reference):

| Section              | Description                        | Example Keys                                    |
|----------------------|------------------------------------|-------------------------------------------------|
| `common`             | Shared labels and buttons          | `save`, `cancel`, `delete`, `loading`, `search` |
| `policyBuilder`      | Policy builder wizard              | `title`, `stepTarget`, `stepAction`, `stepConstraints` |
| `targetEditor`       | Target selection UI                | `simpleTarget`, `enterTargetUrl`, `refinementsTitle` |
| `assigneeEditor`     | Assignee selection UI              | `simpleAssignee`, `enterAssigneeId`, `refinementsTitle` |
| `constraintBuilder`  | Constraint editing                 | `addConstraint`, `groupingAnd`, `groupingOr`    |
| `policySummary`      | Policy summary panel               | `title`, `showJson`, `humanSummary`             |
| `validationEditor`   | Validation request builder         | `method`, `host`, `path`, `runValidation`       |
| `validationResult`   | Validation result display          | `allowed`, `denied`, `explanationTitle`         |
| `policyList`         | Policy list page                   | `title`, `newPolicy`, `confirmDelete`           |
| `policyEditor`       | Editor page chrome                 | `editTitle`, `newTitle`, `tabBuilder`            |
| `namespacedDropdown` | Grouped dropdown component         | `noResults`, `filterPlaceholder`                |
| `templateMode`       | Template mode banner and labels    | `banner`, `lockedFieldTooltip`, `lockedBadge`   |

---

## Authentication

The frontend does **not** include a login page or user management UI.
Authentication is expected to be handled externally (e.g., by an API gateway,
an identity provider, or the embedding application). The frontend simply
attaches a Bearer token to every API request when one is available.

### Standalone Mode Authentication

In standalone mode, the frontend reads the token from the browser's
`localStorage`:

```javascript
// Set the token (e.g., after your external login flow completes)
localStorage.setItem('authToken', 'eyJhbGciOiJSUzI1NiIs...');

// Remove the token (logout)
localStorage.removeItem('authToken');
```

Once set, the token is included as an `Authorization: Bearer <token>` header
on every API request. If no token is present, requests are sent without an
authorization header.

A typical integration pattern:

1. The user authenticates with your identity provider (Keycloak, Auth0, etc.).
2. Your login callback stores the access token:
   `localStorage.setItem('authToken', accessToken)`.
3. Navigate to the ODRL PAP frontend — API requests are now authenticated.
4. On logout, remove the token: `localStorage.removeItem('authToken')`.

### Embedded Mode Authentication

When using the `<odrl-policy-editor>` Web Component, pass the token via the
`auth-token` HTML attribute:

```html
<odrl-policy-editor
  api-base-url="https://pap-api.example.com"
  auth-token="eyJhbGciOiJSUzI1NiIs..."
></odrl-policy-editor>
```

The attribute-supplied token takes precedence over any token in `localStorage`.
This allows the embedding application to manage authentication entirely on its
own and simply hand the token down.

```javascript
// Update the token at runtime (e.g., after a token refresh)
const editor = document.querySelector('odrl-policy-editor');
editor.setAttribute('auth-token', newAccessToken);
```

### API Base URL Resolution

The API base URL is resolved from multiple sources (highest priority first):

| Priority | Source                                     | Context               |
|----------|--------------------------------------------|-----------------------|
| 1        | `configureApi(baseUrl, token)` (programmatic) | Web Component mode |
| 2        | `window.__ENV__.API_BASE_URL` (runtime)    | Docker `envsubst`     |
| 3        | `import.meta.env.VITE_API_BASE_URL` (build-time) | Static builds    |
| 4        | Empty string (relative paths)              | Dev proxy / Nginx reverse proxy |

In the recommended Docker deployment, the base URL resolves to an empty string
(priority 4). API requests use relative paths (`/policy`, `/mappings`,
`/validate`) and are proxied by Nginx to the backend configured via
`PAP_BACKEND_URL`. This avoids CORS issues entirely because the browser sees
all traffic going to a single origin.

---

## Policy Context (`@context`) Configuration

New policies are created with a default JSON-LD `@context`. The PAP backend
expects the context as a **namespaced object**:

```json
{
  "@context": {
    "odrl": "http://www.w3.org/ns/odrl/2/"
  }
}
```

The default context can be overridden at multiple levels. Resolution order
(highest priority first):

| Priority | Source                                       | Context                          |
|----------|----------------------------------------------|----------------------------------|
| 1        | `policyContext` JS property on the Web Component | Embedded mode (programmatic) |
| 2        | `policy-context` HTML attribute (JSON string)    | Embedded mode (declarative)  |
| 3        | `window.__ENV__.ODRL_CONTEXT` (runtime JSON)     | Docker `envsubst`            |
| 4        | `import.meta.env.VITE_ODRL_CONTEXT` (build-time) | Static builds               |
| 5        | `{ "odrl": "http://www.w3.org/ns/odrl/2/" }`     | Built-in default             |

### Adding Contexts at Runtime

The **Raw ODRL** tab in the editor includes a context management panel that
lets users add or remove `@context` entries without editing JSON directly.
Click **Add Context**, enter a prefix (e.g., `dome-op`) and a namespace URI
(e.g., `https://dome-marketplace.eu/ns/`), and the entry is added to the
policy's `@context` object.

### Standalone Mode

Set the `VITE_ODRL_CONTEXT` environment variable (build-time) or
`window.__ENV__.ODRL_CONTEXT` (runtime injection in Docker):

```bash
# .env — build-time override
VITE_ODRL_CONTEXT='{"odrl":"http://www.w3.org/ns/odrl/2/","dome-op":"https://dome-marketplace.eu/ns/"}'
```

```javascript
// env-config.js — runtime injection (Docker envsubst)
window.__ENV__ = {
  ODRL_CONTEXT: '{"odrl":"http://www.w3.org/ns/odrl/2/","dome-op":"https://dome-marketplace.eu/ns/"}'
};
```

### Embedded Mode

Pass the context via the `policy-context` HTML attribute or the
`policyContext` JavaScript property:

```html
<!-- HTML attribute (JSON string) -->
<odrl-policy-editor
  policy-context='{"odrl":"http://www.w3.org/ns/odrl/2/","dome-op":"https://dome-marketplace.eu/ns/"}'
></odrl-policy-editor>
```

```javascript
// JavaScript property (object — takes precedence over the attribute)
const editor = document.querySelector('odrl-policy-editor');
editor.policyContext = {
  odrl: 'http://www.w3.org/ns/odrl/2/',
  'dome-op': 'https://dome-marketplace.eu/ns/',
};
```

---

## Testing

The project uses [Vitest](https://vitest.dev/) with React Testing Library and
[MSW](https://mswjs.io/) (Mock Service Worker) for API mocking.

```bash
# Run all tests once
npm test

# Run tests in watch mode (re-runs on file changes)
npm run test:watch

# Run tests with code coverage report
npm run test:coverage

# Run tests with verbose output (useful in CI)
npm run test:ci
```

---

## Regenerating the API Client

If you make changes to the `api/odrl.yaml` OpenAPI spec, regenerate the
TypeScript API client:

```bash
npm run generate-api
```

The generated code lives in `src/api/` and should be committed to the
repository.
