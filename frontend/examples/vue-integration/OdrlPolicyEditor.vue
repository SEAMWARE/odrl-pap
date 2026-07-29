<!--
  Vue 3 wrapper component for the <odrl-policy-editor> Web Component.

  Usage:
    <OdrlPolicyEditor
      api-base-url="/api/pap"
      :auth-token="accessToken"
      mode="create"
      @policy-created="handleCreated"
      @editor-cancelled="handleCancelled"
    />

  Requirements:
    npm install @seamware/odrl-policy-editor
    Configure isCustomElement in vite.config.ts (see README.md).
-->
<script setup lang="ts">
/**
 * Minimal Vue 3 wrapper for <odrl-policy-editor>.
 *
 * This component demonstrates:
 * - Binding HTML attributes reactively
 * - Listening for Custom Events
 * - Passing an auth token from the parent
 */
import { defineProps, defineEmits, withDefaults } from 'vue';

// Side-effect import: registers the <odrl-policy-editor> custom element
import '@seamware/odrl-policy-editor';

interface Props {
  /** PAP API base URL. */
  apiBaseUrl?: string;
  /** Bearer token for API authentication. */
  authToken?: string | null;
  /** Editor mode: "create" or "edit". */
  mode?: 'create' | 'edit';
  /** Policy ID to load when mode is "edit". */
  policyId?: string;
  /** Theme preset: "light" or "dark". */
  theme?: 'light' | 'dark';
  /** UI locale (e.g., "en", "de"). */
  locale?: string;
}

const props = withDefaults(defineProps<Props>(), {
  apiBaseUrl: '/api/pap',
  authToken: null,
  mode: 'create',
  policyId: undefined,
  theme: 'light',
  locale: 'en',
});

const emit = defineEmits<{
  (e: 'policy-created', detail: { policy: unknown; id: string }): void;
  (e: 'policy-updated', detail: { policy: unknown; id: string }): void;
  (e: 'editor-cancelled'): void;
}>();

function onPolicyCreated(event: Event) {
  emit('policy-created', (event as CustomEvent).detail);
}

function onPolicyUpdated(event: Event) {
  emit('policy-updated', (event as CustomEvent).detail);
}

function onEditorCancelled() {
  emit('editor-cancelled');
}
</script>

<template>
  <odrl-policy-editor
    :api-base-url="props.apiBaseUrl"
    :auth-token="props.authToken"
    :mode="props.mode"
    :policy-id="props.policyId"
    :theme="props.theme"
    :locale="props.locale"
    @policy-created="onPolicyCreated"
    @policy-updated="onPolicyUpdated"
    @editor-cancelled="onEditorCancelled"
  />
</template>
