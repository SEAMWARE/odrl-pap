# Integrating `<odrl-policy-editor>` into the BAE Logic Proxy

This guide shows how to embed the ODRL Policy Editor Web Component in the
[BAE Logic Proxy](https://github.com/FIWARE-TMForum/business-ecosystem-logic-proxy),
a Node.js/Express application with an embedded web portal.

Two integration approaches are covered:

- **Option A — CDN (no build step):** Load the component from a CDN directly
  in HTML. Best for quick prototyping or portals without a frontend build
  pipeline.
- **Option B — npm bundle:** Install via npm and import in your build
  pipeline. Best when the portal already uses a bundler (Webpack, Vite, etc.).

---

## Table of Contents

- [Prerequisites](#prerequisites)
- [Option A: CDN Integration (No Build Step)](#option-a-cdn-integration-no-build-step)
- [Option B: npm Bundle Integration](#option-b-npm-bundle-integration)
- [Event Handling](#event-handling)
- [Authentication](#authentication)
- [API Proxy Configuration](#api-proxy-configuration)
- [Troubleshooting](#troubleshooting)

---

## Prerequisites

- A running ODRL PAP backend instance
- BAE Logic Proxy running locally or in a container
- Network access from the browser to both the BAE portal and the PAP backend

---

## Option A: CDN Integration (No Build Step)

This approach requires zero tooling changes. Add the following to the
portal page where you want the policy editor to appear.

### Complete HTML Example

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>ODRL Policy Editor — BAE Portal</title>

  <!-- Load the Web Component from unpkg CDN -->
  <script
    type="module"
    src="https://unpkg.com/@fiware/odrl-policy-editor@latest"
  ></script>

  <!-- Alternative: jsDelivr CDN -->
  <!--
  <script
    type="module"
    src="https://cdn.jsdelivr.net/npm/@fiware/odrl-policy-editor@latest"
  ></script>
  -->

  <style>
    /* Optional: give the editor a reasonable size on the page */
    odrl-policy-editor {
      display: block;
      max-width: 960px;
      margin: 2rem auto;
    }
  </style>
</head>
<body>
  <h1>Access Policy Management</h1>

  <odrl-policy-editor
    id="editor"
    api-base-url="/proxy/pap"
    mode="create"
    theme="light"
    locale="en"
  ></odrl-policy-editor>

  <script>
    const editor = document.getElementById('editor');

    // --- Authentication ---
    // Inject the auth token from the BAE session.
    // Adjust this to match how your portal exposes the token.
    function getAuthToken() {
      // Example: read from a meta tag set by the server template
      const meta = document.querySelector('meta[name="auth-token"]');
      if (meta) return meta.getAttribute('content');

      // Example: read from a cookie
      const match = document.cookie.match(/(?:^|;\s*)access_token=([^;]*)/);
      if (match) return decodeURIComponent(match[1]);

      return null;
    }

    const token = getAuthToken();
    if (token) {
      editor.setAttribute('auth-token', token);
    }

    // --- Event handling ---
    editor.addEventListener('policy-created', (e) => {
      console.log('Policy created:', e.detail.id);
      // Integrate with the BAE product/offering workflow
      alert('Policy ' + e.detail.id + ' created successfully!');
    });

    editor.addEventListener('policy-updated', (e) => {
      console.log('Policy updated:', e.detail.id);
    });

    editor.addEventListener('policy-validated', (e) => {
      console.log('Validation result:', e.detail.result);
    });

    editor.addEventListener('editor-cancelled', () => {
      // Navigate back to the product list or close the modal
      window.history.back();
    });
  </script>
</body>
</html>
```

### Adding to an Existing Portal Page

If the BAE portal already has a page template (e.g., EJS, Handlebars,
Pug), add the script tag in the `<head>` and the custom element wherever
you need it:

```html
<!-- In the <head> section -->
<script
  type="module"
  src="https://unpkg.com/@fiware/odrl-policy-editor@latest"
></script>

<!-- In the page body, where the editor should render -->
<odrl-policy-editor
  api-base-url="/proxy/pap"
  auth-token="<%= user.accessToken %>"
  mode="create"
  theme="light"
></odrl-policy-editor>
```

---

## Option B: npm Bundle Integration

If the BAE portal uses a JavaScript bundler, install the package and
import it:

### 1. Install

```bash
npm install @fiware/odrl-policy-editor
```

### 2. Import in the Portal Entry Point

```javascript
// In your portal's main JavaScript file (e.g., app.js or index.js)

// Side-effect import: registers the <odrl-policy-editor> custom element
import '@fiware/odrl-policy-editor';
```

The import registers the custom element globally. You can now use
`<odrl-policy-editor>` anywhere in your HTML.

### 3. Use in HTML

```html
<odrl-policy-editor
  api-base-url="/proxy/pap"
  mode="create"
  theme="light"
></odrl-policy-editor>
```

### 4. Configure Programmatically (Optional)

```javascript
const editor = document.querySelector('odrl-policy-editor');

// Set auth token from your session manager
editor.setAttribute('auth-token', sessionManager.getAccessToken());

// Listen for events
editor.addEventListener('policy-created', (e) => {
  console.log('New policy:', e.detail.id);
});
```

---

## Event Handling

The Web Component dispatches Custom Events to communicate with the host
page. Use these events to integrate the policy editor into the BAE's
product/offering workflow.

| Event | Detail | Typical Action |
|-------|--------|----------------|
| `policy-created` | `{ policy: object, id: string }` | Store the policy ID, associate it with a product offering |
| `policy-updated` | `{ policy: object, id: string }` | Refresh the offering's policy reference |
| `policy-validated` | `{ result: object }` | Display validation feedback to the user |
| `editor-cancelled` | `{}` | Close the editor modal or navigate back |

### Example: Associating a Policy with a Product Offering

```javascript
editor.addEventListener('policy-created', (event) => {
  const { id, policy } = event.detail;

  // Associate the new policy with the current product offering
  fetch('/api/offering/' + currentOfferingId + '/policy', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + getAuthToken(),
    },
    body: JSON.stringify({ policyId: id }),
  })
    .then((res) => {
      if (res.ok) {
        showNotification('Access policy attached to offering.');
      }
    })
    .catch((err) => console.error('Failed to attach policy:', err));
});
```

---

## Authentication

The BAE Logic Proxy manages user sessions via its own authentication
mechanism (typically OIDC/OAuth2 through an IdM like Keyrock). Pass the
access token to the Web Component so it can authenticate API calls to the
PAP backend.

**Server-rendered pages:** Inject the token via a template variable:

```html
<!-- EJS example -->
<odrl-policy-editor
  auth-token="<%= req.session.accessToken %>"
></odrl-policy-editor>
```

**Client-side session:** Read the token from a cookie or the session API:

```javascript
const token = document.cookie
  .split('; ')
  .find((c) => c.startsWith('access_token='))
  ?.split('=')[1];

if (token) {
  editor.setAttribute('auth-token', decodeURIComponent(token));
}
```

**Token refresh:** Update the attribute when the token is refreshed:

```javascript
sessionManager.onTokenRefresh((newToken) => {
  editor.setAttribute('auth-token', newToken);
});
```

---

## API Proxy Configuration

The PAP backend must be reachable from the browser. The recommended
approach is to proxy PAP API requests through the BAE Logic Proxy's
Express server to avoid CORS issues.

### Express Proxy Middleware

```javascript
// In the BAE Logic Proxy's Express app (e.g., server.js)
const { createProxyMiddleware } = require('http-proxy-middleware');

// Proxy /proxy/pap/* to the PAP backend
app.use(
  '/proxy/pap',
  createProxyMiddleware({
    target: process.env.PAP_BACKEND_URL || 'http://odrl-pap:8080',
    changeOrigin: true,
    pathRewrite: { '^/proxy/pap': '' },
  }),
);
```

Then set `api-base-url="/proxy/pap"` on the Web Component.

### Docker Compose Example

```yaml
services:
  bae-logic-proxy:
    image: fiware/biz-ecosystem-logic-proxy
    environment:
      PAP_BACKEND_URL: http://odrl-pap:8080
    depends_on:
      - odrl-pap

  odrl-pap:
    image: quay.io/fiware/odrl-pap:latest
    ports:
      - "8080:8080"
```

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Editor does not render | Ensure the `<script type="module">` tag is loaded; check the browser console for network errors |
| API calls return CORS errors | Set up the Express proxy (see above) so all requests go through the same origin |
| API calls return 401/403 | Verify the `auth-token` attribute contains a valid access token |
| Content Security Policy (CSP) blocks the CDN | Add `https://unpkg.com` (or `https://cdn.jsdelivr.net`) to the `script-src` directive |
| Editor styles conflict with portal CSS | The Web Component uses Shadow DOM — styles are isolated. If you see issues, ensure you are loading the correct package version |
