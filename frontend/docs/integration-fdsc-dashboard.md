# Integrating `<odrl-policy-editor>` into the FDSC-Dashboard

This guide shows how to embed the ODRL Policy Editor Web Component in the
[FDSC-Dashboard](https://github.com/SEAMWARE/fdsc-dashboard), a Vue 3 +
Vuetify 3 + TypeScript single-page application that uses `oidc-client-ts`
for authentication.

---

## Table of Contents

- [Prerequisites](#prerequisites)
- [1. Install the Package](#1-install-the-package)
- [2. Configure Vite](#2-configure-vite)
- [3. Create a Vue Wrapper Component](#3-create-a-vue-wrapper-component)
- [4. Register the Route](#4-register-the-route)
- [5. Add Navigation](#5-add-navigation)
- [6. Proxy the PAP API (Development)](#6-proxy-the-pap-api-development)
- [7. Production Configuration](#7-production-configuration)

---

## Prerequisites

- FDSC-Dashboard running locally (`npm run dev`)
- A running ODRL PAP backend instance
- Node.js >= 18

---

## 1. Install the Package

```bash
cd fdsc-dashboard
npm install @seamware/odrl-policy-editor
```

---

## 2. Configure Vite

Vue 3 tries to resolve unknown HTML tags as Vue components. Tell the
compiler to treat `odrl-policy-editor` as a native custom element:

```ts
// vite.config.ts
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [
    vue({
      template: {
        compilerOptions: {
          // Treat <odrl-policy-editor> as a native custom element
          isCustomElement: (tag) => tag === 'odrl-policy-editor',
        },
      },
    }),
  ],
  // ... rest of your config
});
```

---

## 3. Create a Vue Wrapper Component

Create `src/components/OdrlPolicyEditor.vue`:

```vue
<script setup lang="ts">
/**
 * Vue 3 wrapper for the <odrl-policy-editor> Web Component.
 *
 * Bridges the FDSC-Dashboard's OIDC auth token and Vuetify theme
 * into the embedded editor.
 */
import { ref, computed, onMounted, watch } from 'vue';
import { useTheme } from 'vuetify';
import { useRouter } from 'vue-router';

// Side-effect import: registers the <odrl-policy-editor> custom element
import '@seamware/odrl-policy-editor';

// Import your OIDC user store (adjust the path to match your project)
import { useAuthStore } from '@/stores/auth';

// ---------------------------------------------------------------------------
// Props & emits
// ---------------------------------------------------------------------------

interface Props {
  /** Editor mode: "create" (default) or "edit". */
  mode?: 'create' | 'edit';
  /** Policy ID to load when mode is "edit". */
  policyId?: string;
}

const props = withDefaults(defineProps<Props>(), {
  mode: 'create',
  policyId: undefined,
});

const emit = defineEmits<{
  (e: 'policy-created', detail: { policy: unknown; id: string }): void;
  (e: 'policy-updated', detail: { policy: unknown; id: string }): void;
}>();

// ---------------------------------------------------------------------------
// Auth token from oidc-client-ts
// ---------------------------------------------------------------------------

const authStore = useAuthStore();
const accessToken = computed(() => authStore.user?.access_token ?? null);

// ---------------------------------------------------------------------------
// Theme bridge: Vuetify -> Web Component
// ---------------------------------------------------------------------------

const vuetifyTheme = useTheme();
const editorRef = ref<HTMLElement | null>(null);

/** Map Vuetify theme colors to the Web Component's themeConfig. */
function applyVuetifyTheme() {
  const el = editorRef.value as HTMLElement & {
    themeConfig?: Record<string, string>;
  };
  if (!el) return;

  const colors = vuetifyTheme.current.value.colors;
  el.themeConfig = {
    'odrl-primary-color': colors.primary,
    'odrl-secondary-color': colors.secondary,
    'odrl-bg-color': colors.background,
    'odrl-text-color': colors['on-background'] ?? colors['on-surface'],
    'odrl-card-bg': colors.surface,
  };
}

onMounted(applyVuetifyTheme);
watch(() => vuetifyTheme.current.value, applyVuetifyTheme);

// ---------------------------------------------------------------------------
// Detect light/dark mode from Vuetify
// ---------------------------------------------------------------------------

const themePreset = computed(() =>
  vuetifyTheme.current.value.dark ? 'dark' : 'light',
);

// ---------------------------------------------------------------------------
// Event handlers
// ---------------------------------------------------------------------------

const router = useRouter();

function onPolicyCreated(event: Event) {
  const detail = (event as CustomEvent).detail;
  emit('policy-created', detail);
  // Navigate to the policy list after creation
  router.push({ name: 'policies' });
}

function onPolicyUpdated(event: Event) {
  const detail = (event as CustomEvent).detail;
  emit('policy-updated', detail);
  router.push({ name: 'policies' });
}

function onEditorCancelled() {
  router.push({ name: 'policies' });
}

// ---------------------------------------------------------------------------
// PAP API base URL
// ---------------------------------------------------------------------------

/** Adjust this to your BFF proxy or direct PAP URL. */
const API_BASE_URL = '/api/pap';
</script>

<template>
  <odrl-policy-editor
    ref="editorRef"
    :api-base-url="API_BASE_URL"
    :auth-token="accessToken"
    :mode="props.mode"
    :policy-id="props.policyId"
    :theme="themePreset"
    locale="en"
    @policy-created="onPolicyCreated"
    @policy-updated="onPolicyUpdated"
    @editor-cancelled="onEditorCancelled"
  />
</template>
```

> **Note:** Adjust the `useAuthStore` import path and `API_BASE_URL` to
> match your project's structure. The `useAuthStore` should expose the
> OIDC user object from `oidc-client-ts`.

---

## 4. Register the Route

Add the policy editor routes to your Vue Router configuration:

```ts
// src/router/index.ts
import OdrlPolicyEditor from '@/components/OdrlPolicyEditor.vue';

const routes = [
  // ... existing routes ...
  {
    path: '/policies',
    name: 'policies',
    component: () => import('@/views/PoliciesView.vue'),
  },
  {
    path: '/policies/new',
    name: 'policy-create',
    component: OdrlPolicyEditor,
    props: { mode: 'create' },
  },
  {
    path: '/policies/:id/edit',
    name: 'policy-edit',
    component: OdrlPolicyEditor,
    props: (route) => ({
      mode: 'edit',
      policyId: route.params.id as string,
    }),
  },
];
```

---

## 5. Add Navigation

Add a sidebar or navigation item that links to the policy editor:

```vue
<!-- In your navigation component (e.g., src/components/AppNavigation.vue) -->
<template>
  <v-list-item
    :to="{ name: 'policy-create' }"
    prepend-icon="mdi-shield-edit"
    title="Policy Editor"
  />
</template>
```

---

## 6. Proxy the PAP API (Development)

During development, add a Vite proxy rule so API calls reach the PAP
backend without CORS issues:

```ts
// vite.config.ts
export default defineConfig({
  server: {
    proxy: {
      '/api/pap': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/pap/, ''),
      },
    },
  },
  // ... other config
});
```

This forwards `/api/pap/policy` to `http://localhost:8080/policy`, etc.

---

## 7. Production Configuration

In production, configure your reverse proxy (Nginx, Traefik, etc.) to
forward PAP API requests to the backend:

```nginx
# Nginx example
location /api/pap/ {
    proxy_pass http://odrl-pap:8080/;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
}
```

The Web Component's `api-base-url="/api/pap"` attribute ensures all API
calls go through this proxy, avoiding CORS issues entirely.

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| `[Vue warn]: Failed to resolve component: odrl-policy-editor` | Add `isCustomElement` to `vite.config.ts` (see [Step 2](#2-configure-vite)) |
| API calls return 401 | Verify the OIDC token is being passed correctly; check `accessToken` in Vue DevTools |
| Editor styles look broken | Ensure you are importing `@seamware/odrl-policy-editor` (the side-effect import registers the custom element and its Shadow DOM styles) |
| Theme colors do not match Vuetify | Check that `applyVuetifyTheme()` runs after the element is mounted; inspect the `themeConfig` property in DevTools |
