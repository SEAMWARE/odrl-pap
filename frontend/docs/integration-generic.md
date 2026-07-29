# Generic Integration Guide for `<odrl-policy-editor>`

This guide covers how to embed the ODRL Policy Editor Web Component in
any web application, regardless of framework. Pick the section that matches
your stack.

---

## Table of Contents

- [Vanilla HTML / JavaScript](#vanilla-html--javascript)
- [React](#react)
- [Angular](#angular)
- [Vue 2](#vue-2)
- [Vue 3](#vue-3)
- [Svelte](#svelte)
- [Server-Side Rendering (SSR)](#server-side-rendering-ssr)
- [Security Considerations](#security-considerations)
- [Performance Optimization](#performance-optimization)
- [Attributes & Events Reference](#attributes--events-reference)

---

## Vanilla HTML / JavaScript

The simplest integration path — no build tools required.

### Via CDN

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>ODRL Policy Editor</title>

  <!-- Option 1: unpkg -->
  <script
    type="module"
    src="https://unpkg.com/@seamware/odrl-policy-editor@latest"
  ></script>

  <!-- Option 2: jsDelivr -->
  <!--
  <script
    type="module"
    src="https://cdn.jsdelivr.net/npm/@seamware/odrl-policy-editor@latest"
  ></script>
  -->
</head>
<body>
  <odrl-policy-editor
    api-base-url="https://pap-api.example.com"
    auth-token="your-bearer-token"
    mode="create"
    theme="light"
    locale="en"
  ></odrl-policy-editor>

  <script>
    const editor = document.querySelector('odrl-policy-editor');

    editor.addEventListener('policy-created', (e) => {
      console.log('Policy created:', e.detail.id);
    });

    editor.addEventListener('editor-cancelled', () => {
      console.log('User cancelled');
    });
  </script>
</body>
</html>
```

### Via npm (Self-Hosted)

```bash
npm install @seamware/odrl-policy-editor
```

```javascript
// main.js
import '@seamware/odrl-policy-editor';
```

Then use `<odrl-policy-editor>` in your HTML as shown above.

---

## React

React (17+) supports custom elements in JSX. Use a `ref` to set
JavaScript properties and listen for Custom Events.

### Installation

```bash
npm install @seamware/odrl-policy-editor
```

### Component Wrapper

```tsx
// OdrlPolicyEditor.tsx
import { useEffect, useRef } from 'react';

// Side-effect import: registers the custom element
import '@seamware/odrl-policy-editor';

interface OdrlPolicyEditorProps {
  apiBaseUrl: string;
  authToken?: string | null;
  mode?: 'create' | 'edit';
  policyId?: string | null;
  theme?: 'light' | 'dark';
  locale?: string;
  onPolicyCreated?: (detail: { policy: unknown; id: string }) => void;
  onPolicyUpdated?: (detail: { policy: unknown; id: string }) => void;
  onEditorCancelled?: () => void;
}

export function OdrlPolicyEditor({
  apiBaseUrl,
  authToken,
  mode = 'create',
  policyId,
  theme = 'light',
  locale = 'en',
  onPolicyCreated,
  onPolicyUpdated,
  onEditorCancelled,
}: OdrlPolicyEditorProps) {
  const ref = useRef<HTMLElement>(null);

  useEffect(() => {
    const el = ref.current;
    if (!el) return;

    const handleCreated = (e: Event) =>
      onPolicyCreated?.((e as CustomEvent).detail);
    const handleUpdated = (e: Event) =>
      onPolicyUpdated?.((e as CustomEvent).detail);
    const handleCancelled = () => onEditorCancelled?.();

    el.addEventListener('policy-created', handleCreated);
    el.addEventListener('policy-updated', handleUpdated);
    el.addEventListener('editor-cancelled', handleCancelled);

    return () => {
      el.removeEventListener('policy-created', handleCreated);
      el.removeEventListener('policy-updated', handleUpdated);
      el.removeEventListener('editor-cancelled', handleCancelled);
    };
  }, [onPolicyCreated, onPolicyUpdated, onEditorCancelled]);

  return (
    <odrl-policy-editor
      ref={ref}
      api-base-url={apiBaseUrl}
      auth-token={authToken ?? undefined}
      mode={mode}
      policy-id={policyId ?? undefined}
      theme={theme}
      locale={locale}
    />
  );
}
```

### TypeScript: Declare the Custom Element

Add a type declaration so TypeScript recognizes the custom element in JSX:

```ts
// src/types/custom-elements.d.ts
declare namespace JSX {
  interface IntrinsicElements {
    'odrl-policy-editor': React.DetailedHTMLProps<
      React.HTMLAttributes<HTMLElement> & {
        'api-base-url'?: string;
        'auth-token'?: string;
        mode?: 'create' | 'edit';
        'policy-id'?: string;
        theme?: 'light' | 'dark';
        locale?: string;
        'policy-context'?: string;
      },
      HTMLElement
    >;
  }
}
```

### Usage

```tsx
import { OdrlPolicyEditor } from './OdrlPolicyEditor';

function App() {
  return (
    <OdrlPolicyEditor
      apiBaseUrl="/api/pap"
      authToken={user.accessToken}
      onPolicyCreated={(detail) => console.log('Created:', detail.id)}
      onEditorCancelled={() => navigate('/policies')}
    />
  );
}
```

---

## Angular

Angular requires explicit opt-in for custom elements via
`CUSTOM_ELEMENTS_SCHEMA`.

### Installation

```bash
npm install @seamware/odrl-policy-editor
```

### Module Configuration

```typescript
// app.module.ts
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';

@NgModule({
  declarations: [AppComponent],
  imports: [BrowserModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  bootstrap: [AppComponent],
})
export class AppModule {}
```

For **standalone components** (Angular 14+):

```typescript
// policy-editor.component.ts
import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

@Component({
  selector: 'app-policy-editor',
  standalone: true,
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  template: `
    <odrl-policy-editor
      [attr.api-base-url]="apiBaseUrl"
      [attr.auth-token]="authToken"
      [attr.mode]="mode"
      [attr.theme]="theme"
      (policy-created)="onPolicyCreated($event)"
      (editor-cancelled)="onEditorCancelled()"
    ></odrl-policy-editor>
  `,
})
export class PolicyEditorComponent {
  apiBaseUrl = '/api/pap';
  authToken: string | null = null;
  mode: 'create' | 'edit' = 'create';
  theme: 'light' | 'dark' = 'light';

  onPolicyCreated(event: CustomEvent) {
    console.log('Policy created:', event.detail.id);
  }

  onEditorCancelled() {
    // Navigate back
  }
}
```

### Register the Custom Element

Import the package in your `main.ts` (before bootstrapping):

```typescript
// main.ts
import '@seamware/odrl-policy-editor';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { AppModule } from './app/app.module';

platformBrowserDynamic().bootstrapModule(AppModule);
```

---

## Vue 2

Vue 2 does not have built-in custom element support. Tell Vue to ignore
the tag:

### Configuration

```javascript
// main.js
import Vue from 'vue';
import '@seamware/odrl-policy-editor';

// Tell Vue 2 to treat <odrl-policy-editor> as a native element
Vue.config.ignoredElements = ['odrl-policy-editor'];
```

### Template

```vue
<template>
  <odrl-policy-editor
    :api-base-url="apiBaseUrl"
    :auth-token="authToken"
    mode="create"
    theme="light"
    @policy-created="onPolicyCreated"
    @editor-cancelled="onEditorCancelled"
  />
</template>

<script>
export default {
  data() {
    return {
      apiBaseUrl: '/api/pap',
      authToken: null,
    };
  },
  methods: {
    onPolicyCreated(event) {
      console.log('Policy created:', event.detail.id);
    },
    onEditorCancelled() {
      this.$router.push('/policies');
    },
  },
};
</script>
```

---

## Vue 3

Vue 3 uses the `compilerOptions.isCustomElement` option to recognize
custom elements.

### Vite Configuration

```typescript
// vite.config.ts
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [
    vue({
      template: {
        compilerOptions: {
          isCustomElement: (tag) => tag === 'odrl-policy-editor',
        },
      },
    }),
  ],
});
```

### Usage

```vue
<script setup lang="ts">
import { ref } from 'vue';
import '@seamware/odrl-policy-editor';

const apiBaseUrl = '/api/pap';
const authToken = ref<string | null>(null);

function onPolicyCreated(event: Event) {
  const detail = (event as CustomEvent).detail;
  console.log('Policy created:', detail.id);
}

function onEditorCancelled() {
  // Navigate back
}
</script>

<template>
  <odrl-policy-editor
    :api-base-url="apiBaseUrl"
    :auth-token="authToken"
    mode="create"
    theme="light"
    @policy-created="onPolicyCreated"
    @editor-cancelled="onEditorCancelled"
  />
</template>
```

---

## Svelte

Svelte recognizes custom elements automatically. No special configuration
is needed.

```svelte
<!-- PolicyEditor.svelte -->
<script>
  import '@seamware/odrl-policy-editor';

  export let apiBaseUrl = '/api/pap';
  export let authToken = null;

  function handlePolicyCreated(event) {
    console.log('Policy created:', event.detail.id);
  }

  function handleEditorCancelled() {
    // Navigate back
  }
</script>

<odrl-policy-editor
  api-base-url={apiBaseUrl}
  auth-token={authToken}
  mode="create"
  theme="light"
  on:policy-created={handlePolicyCreated}
  on:editor-cancelled={handleEditorCancelled}
/>
```

---

## Server-Side Rendering (SSR)

The `<odrl-policy-editor>` custom element requires a browser DOM
(`HTMLElement`, `customElements.define`, `attachShadow`). It cannot
render on the server. Use these patterns to defer loading to the client:

### Next.js (React)

```tsx
// components/PolicyEditor.tsx
import dynamic from 'next/dynamic';

const OdrlPolicyEditor = dynamic(
  () => import('./OdrlPolicyEditorWrapper'),
  { ssr: false },
);

export default function PolicyEditorPage() {
  return <OdrlPolicyEditor apiBaseUrl="/api/pap" />;
}
```

### Nuxt 3 (Vue)

```vue
<!-- pages/policies.vue -->
<template>
  <ClientOnly>
    <odrl-policy-editor
      api-base-url="/api/pap"
      mode="create"
      theme="light"
    />
  </ClientOnly>
</template>

<script setup>
// Import only on the client
if (import.meta.client) {
  await import('@seamware/odrl-policy-editor');
}
</script>
```

### SvelteKit

```svelte
<!-- +page.svelte -->
<script>
  import { onMount } from 'svelte';
  import { browser } from '$app/environment';

  let loaded = false;

  onMount(async () => {
    if (browser) {
      await import('@seamware/odrl-policy-editor');
      loaded = true;
    }
  });
</script>

{#if loaded}
  <odrl-policy-editor
    api-base-url="/api/pap"
    mode="create"
    theme="light"
  />
{/if}
```

### General Pattern

For any SSR framework, the rule is the same: **defer the import to the
client** using dynamic imports, `onMount` hooks, or framework-provided
wrappers like `<ClientOnly>`.

---

## Security Considerations

### Content Security Policy (CSP)

If your application enforces a CSP, update the `script-src` directive to
allow the CDN source:

```
Content-Security-Policy:
  script-src 'self' https://unpkg.com https://cdn.jsdelivr.net;
```

If you self-host the bundle (Option B / npm), `'self'` is sufficient.

### CORS

The Web Component makes API calls to the URL specified in `api-base-url`.
If this URL is a different origin than the host page, the PAP backend must
return appropriate CORS headers:

```
Access-Control-Allow-Origin: https://your-app.example.com
Access-Control-Allow-Headers: Authorization, Content-Type
Access-Control-Allow-Methods: GET, POST, PUT, DELETE
```

**Recommended:** Proxy API requests through the same origin as the host
page (see the FDSC-Dashboard and BAE Logic Proxy guides for examples).
This eliminates CORS requirements entirely.

### Token Security

- Never embed long-lived tokens in HTML source. Prefer short-lived JWTs
  injected at runtime.
- The `auth-token` attribute is visible in the DOM. If this is a concern,
  use the JS property instead: `editor.setAttribute('auth-token', token)`.
- Rotate tokens regularly and update the attribute when they refresh.

---

## Performance Optimization

### Lazy Loading

Load the Web Component only when the user navigates to the policy editor
page:

```javascript
// Lazy-load on demand
async function showEditor() {
  await import('@seamware/odrl-policy-editor');
  document.getElementById('editor-container').innerHTML =
    '<odrl-policy-editor api-base-url="/api/pap" mode="create"></odrl-policy-editor>';
}
```

### Preconnect to the API Host

If the PAP backend is on a different origin, add a preconnect hint so the
browser establishes the connection early:

```html
<link rel="preconnect" href="https://pap-api.example.com" />
```

### Caching

The Web Component bundle is a single immutable file. Serve it with
long-lived cache headers:

```
Cache-Control: public, max-age=31536000, immutable
```

When using a CDN (unpkg or jsDelivr), caching is handled automatically
via versioned URLs.

---

## Attributes & Events Reference

### HTML Attributes

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `api-base-url` | `string` | `""` | PAP API base URL |
| `auth-token` | `string` | `null` | Bearer token for API authentication |
| `mode` | `"create" \| "edit"` | `"create"` | Editor mode |
| `policy-id` | `string` | `null` | Policy ID to load (required when `mode="edit"`) |
| `theme` | `"light" \| "dark"` | `"light"` | Theme preset |
| `locale` | `string` | `"en"` | Language code |
| `policy-context` | `string` (JSON) | `null` | Default JSON-LD `@context` for new policies |

### JavaScript Properties

| Property | Type | Description |
|----------|------|-------------|
| `i18nStrings` | `object \| undefined` | Partial i18n overrides (deep-merged with defaults) |
| `themeConfig` | `object \| undefined` | CSS custom property overrides |
| `template` | `object \| undefined` | Policy template to pre-fill and constrain the editor |
| `policyContext` | `object \| string \| undefined` | JSON-LD `@context` (JS property takes precedence over attribute) |

### Custom Events

| Event | Detail | Description |
|-------|--------|-------------|
| `policy-created` | `{ policy: object, id: string }` | A new policy was saved |
| `policy-updated` | `{ policy: object, id: string }` | An existing policy was updated |
| `policy-validated` | `{ result: object }` | Policy validation completed |
| `editor-cancelled` | `{}` | The user clicked Cancel |

For complete type definitions, see the
[TypeScript declarations](https://www.npmjs.com/package/@seamware/odrl-policy-editor)
included in the npm package.
