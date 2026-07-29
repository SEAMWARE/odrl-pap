/**
 * Vite configuration for building the ODRL Policy Editor Web Component.
 *
 * Produces a single self-contained ES module (`odrl-policy-editor.js`)
 * that registers the `<odrl-policy-editor>` custom element. Bootstrap
 * and theme CSS are inlined via `?inline` imports so the bundle has
 * zero external dependencies.
 *
 * Usage:
 * ```bash
 * npm run build:component
 * # or directly:
 * vite build --config vite.component.config.ts
 * ```
 *
 * The standard `npm run build` is NOT affected by this file.
 */
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  build: {
    lib: {
      entry: 'src/web-component/index.ts',
      formats: ['es'],
      fileName: () => 'odrl-policy-editor.js',
    },
    outDir: 'dist-component',
    emptyOutDir: true,
    // Inline everything — no external dependencies for the Web Component
    rollupOptions: {
      output: {
        // Single chunk — no code-splitting for easier consumption
        inlineDynamicImports: true,
      },
    },
    // Do not extract CSS — it is imported as strings via `?inline`
    cssCodeSplit: false,
  },
});
