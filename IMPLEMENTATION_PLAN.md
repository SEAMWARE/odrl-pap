# Implementation Plan: Easy definition of ODRL Policies by end users

## Overview

Create a polished, user-friendly frontend for the ODRL PAP that enables non-technical end users to build ODRL policies through guided dropdowns and form elements, test them against the validation endpoint, and that can be embedded as a Web Component into external applications (BAE-Marketplace, FDSC-Dashboard). The existing React 19 + TypeScript frontend in `frontend/` provides a solid foundation (~70% functional) and will be enhanced rather than replaced.

## Steps

### Step 1: Project Foundation - Testing Infrastructure & Build Configuration

**Goal:** Establish the testing and build infrastructure needed for all subsequent steps. Add Vitest + React Testing Library, configure Vite for dual output (standalone app + library/Web Component), and set up runtime environment variable injection for the Docker image.

**Files to create/modify:**
- `frontend/package.json` — Add devDependencies: `vitest`, `@testing-library/react`, `@testing-library/jest-dom`, `@testing-library/user-event`, `jsdom`, `msw` (Mock Service Worker for API mocking)
- `frontend/vite.config.ts` — Add `test` configuration block for Vitest (jsdom environment); add conditional library-mode build config (used in Step 4)
- `frontend/vitest.config.ts` — Vitest configuration if separate config is cleaner (jsdom env, setup files, coverage thresholds)
- `frontend/src/test/setup.ts` — Test setup file (import `@testing-library/jest-dom`, MSW server setup/teardown)
- `frontend/src/test/mocks/handlers.ts` — MSW request handlers for `/mappings` and `/validate` endpoints with realistic mock data
- `frontend/src/test/mocks/server.ts` — MSW server instance
- `frontend/Dockerfile` — Add runtime env var injection via `envsubst` on an `env-config.js` template so `VITE_API_BASE_URL` can be set at container start without rebuilding
- `frontend/public/env-config.js` — Runtime config template: `window.__ENV__ = { API_BASE_URL: "${VITE_API_BASE_URL}" }`
- `frontend/src/services/api.ts` — Read base URL from `window.__ENV__?.API_BASE_URL` at runtime, falling back to `import.meta.env.VITE_API_BASE_URL`

**Acceptance criteria:**
- `npm run test` runs Vitest and passes (with at least one placeholder test)
- `npm run build` produces a working `dist/` bundle
- Docker image serves the app and respects `VITE_API_BASE_URL` set at `docker run` time
- MSW handlers return realistic mock data matching the `Mappings` and `ValidationResponse` schemas from `api/odrl.yaml`

---

### Step 2: Policy Builder UX - Namespace-Grouped Dropdowns & User Guidance

**Goal:** Transform the policy builder into an intuitive experience for non-technical users. Group dropdown items by namespace (e.g., "odrl", "dome-op", "tmf") with human-readable labels and descriptions. Add contextual help, form validation, and loading/error states throughout.

**Files to modify:**
- `frontend/src/components/Baukasten.tsx` → **Rename to `PolicyBuilder.tsx`** — Major UX overhaul:
  - Group action dropdown items by namespace prefix (split on `:` — e.g., `odrl:read` becomes group "ODRL", item "read")
  - Show `description` from each `Mapping` as subtitle/tooltip in dropdown items
  - Add a guided step indicator or section numbering (1. Target, 2. Assignee, 3. Action, 4. Constraints) so users understand the flow
  - Add contextual help text explaining what each section does in plain language
  - Add loading spinner while mappings are being fetched
  - Add error alert if mappings fetch fails with retry button
  - Improve overall layout: make the form the primary focus, summary as collapsible sidebar

- `frontend/src/components/TargetEditor.tsx` — Enhance:
  - Group target dropdown items by namespace
  - Add placeholder text and help tooltips explaining "target" in plain language
  - Add input validation (URL format for custom targets)
  - Improve AssetCollection UX with better visual separation

- `frontend/src/components/AssigneeEditor.tsx` — Enhance:
  - Group assignee dropdown items by namespace
  - Add placeholder text and help tooltips
  - Improve PartyCollection UX

- `frontend/src/components/ConstraintBuilder.tsx` — Enhance:
  - Group left operand and operator dropdowns by namespace
  - Show descriptions in dropdown items
  - Add visual indicator explaining what AND/OR/XONE means in plain language (e.g., "ALL must match" / "ANY can match" / "Exactly ONE must match")
  - Add visual card/panel styling to each constraint for clearer separation
  - Better right operand type toggle with clearer labels ("Named value (URI)" vs "Literal value")

- `frontend/src/components/PolicySummary.tsx` — Enhance:
  - Improve readability with better formatting
  - Add copy-to-clipboard for the raw JSON view
  - Show human-readable summaries (e.g., "Allow READ on target X when assignee is Y")

- Create `frontend/src/components/NamespacedDropdown.tsx` — Reusable dropdown component:
  - Accepts `items: Mapping[]` and groups by namespace prefix
  - Renders `<optgroup>` or Bootstrap equivalent for each namespace
  - Shows `description` as secondary text
  - Supports search/filter for large lists
  - Handles empty state gracefully

- Create `frontend/src/hooks/useMappings.ts` — Extract mappings fetch into a reusable React hook:
  - Caches mappings in memory (fetched once, shared across components)
  - Provides loading/error states
  - Returns typed mappings data
  - Used by PolicyBuilder, TargetEditor, AssigneeEditor, ConstraintBuilder

- Create `frontend/src/i18n/index.ts` — Localization infrastructure:
  - Define an `I18nStrings` interface covering all user-facing text (labels, tooltips, help text, error messages, placeholders)
  - Provide a default English translation (`en.ts`) as the built-in locale
  - Export a `useI18n()` hook that reads the current locale from an `I18nContext` React context
  - Support switching locale at runtime via context provider (e.g., `<I18nProvider locale="de" strings={germanStrings}>`)
  - Allow partial overrides: consumers pass only the keys they want to change, the rest falls back to English defaults

- Create `frontend/src/i18n/en.ts` — Default English translation file:
  - All UI strings organized by component/section (policyBuilder, constraintBuilder, validationEditor, etc.)
  - Includes labels, placeholders, help text, error messages, button text

- Create `frontend/src/i18n/I18nContext.tsx` — React context provider for localization:
  - `I18nProvider` component accepts `locale` and optional `strings` override
  - Deep-merges user-provided strings with English defaults
  - Exposes `t(key)` function and `locale` string via context

- Create `frontend/src/theme/ThemeContext.tsx` — Style customization infrastructure:
  - Define a `ThemeConfig` interface with CSS custom properties (colors, fonts, spacing, border-radius, etc.)
  - Provide a default theme matching the current Bootstrap-based look
  - `ThemeProvider` component injects CSS custom properties onto the root element
  - Support runtime theme switching (light/dark built-in, plus fully custom themes)
  - Components use CSS custom properties (e.g., `var(--odrl-primary-color)`) instead of hardcoded values

- Create `frontend/src/theme/defaultTheme.ts` — Default theme definition:
  - Colors, typography, spacing, border-radius, shadows
  - Light and dark mode presets
  - Maps to CSS custom properties applied at the container root

**Acceptance criteria:**
- All dropdowns group items by namespace with visible group headers
- Each dropdown item shows its description
- Contextual help text is present on every form section
- Loading states shown while data is being fetched
- Error states with retry shown on fetch failure
- Form sections are numbered and clearly labeled
- `NamespacedDropdown` is reusable and used consistently across all editors
- All user-facing strings are sourced from the i18n system (no hardcoded UI text in components)
- Language can be changed at runtime by providing a different locale to `I18nProvider`
- Custom themes can be applied via `ThemeProvider` — colors, fonts, and spacing are fully customizable
- CSS custom properties are used for all themeable values, enabling external style overrides
- Unit tests for `NamespacedDropdown`, `useMappings` hook, `useI18n` hook, and `PolicyBuilder` (renamed from Baukasten)

---

### Step 3: Validation & Testing UI Enhancement

**Goal:** Improve the policy validation/testing experience so users can easily test their policies against sample requests. Add support for the `GenericJsonInput` validation mode (JSON payload evaluation), improve result display, and add pre-populated example payloads.

**Files to modify:**
- `frontend/src/components/ValidationEditor.tsx` — Major enhancement:
  - Add a mode toggle: "HTTP Request" (existing TestRequest) vs "JSON Payload" (GenericJsonInput)
  - **HTTP Request mode** (existing, enhanced):
    - Add protocol field (currently hardcoded; make explicit)
    - Better header management: add/remove custom headers (not just Content-Type and Authorization)
    - Improve JWT helper UX: add field descriptions, show decoded JWT preview
    - Add "Example" button that pre-fills a realistic sample HTTP request
  - **JSON Payload mode** (new):
    - `payload` JSON textarea with syntax highlighting or at least validation
    - Optional `subject` JSON textarea for identity/credential context
    - Add "Example" button with sample JSON payload
  - Shared:
    - Add a "Copy as cURL" button for the HTTP request mode
    - Better JSON syntax validation with clear error messages before submission

- `frontend/src/pages/PolicyEditor.tsx` — Enhance validation modal:
  - Larger modal for better readability
  - Show validation result prominently: green checkmark for allow, red X for deny
  - Display `explanation` array as a readable list with icons
  - Add "Test Again" button to modify and re-run without closing modal
  - Show the policy that was validated (collapsible)
  - Persist last test request in session storage so users don't lose their test data

- `frontend/src/api/models/GenericJsonInput.ts` — Verify this model exists in generated API client; if not, regenerate client from the latest `api/odrl.yaml`

- Create `frontend/src/components/ValidationResult.tsx` — Extract result display into its own component:
  - Visual allow/deny indicator (icon + color)
  - Expandable explanation list
  - Raw response toggle for advanced users

**Acceptance criteria:**
- Users can toggle between HTTP Request and JSON Payload validation modes
- JSON Payload mode sends `{ policy, jsonInput: { payload, subject } }` to POST /validate
- HTTP Request mode continues to work as before
- Pre-filled example buttons work for both modes
- Validation results display clearly with visual allow/deny indicator
- Test request data persists across modal open/close within a session
- Unit tests for `ValidationResult` component
- Unit tests for mode toggle behavior

---

### Step 4: Embeddable Web Component Wrapper

**Goal:** Make the policy builder embeddable in external applications (BAE-Marketplace, FDSC-Dashboard) as a standard Web Component (`<odrl-policy-editor>`). This uses the Custom Elements API — no framework required by the host application. The component communicates via HTML attributes and Custom Events.

**Files to create/modify:**
- Create `frontend/src/web-component/OdrlPolicyEditorElement.ts` — Custom Element definition:
  - Extends `HTMLElement`, registers as `<odrl-policy-editor>`
  - Observed attributes: `api-base-url`, `auth-token`, `mode` (create/edit), `policy-id`, `theme` (light/dark), `locale` (language code, e.g., "en", "de")
  - Creates a Shadow DOM root and mounts the React app inside it
  - Injects Bootstrap CSS into shadow root for style isolation
  - Attribute change callbacks update React context/props

- Create `frontend/src/web-component/EmbeddedApp.tsx` — Stripped-down React root for embedded mode:
  - No router (single-page policy editor only, no list view)
  - Receives config via React Context from the Web Component wrapper
  - Emits Custom Events via a callback bridge:
    - `policy-created` — detail: `{ policy: OdrlPolicyJson, id: string }`
    - `policy-updated` — detail: `{ policy: OdrlPolicyJson, id: string }`
    - `policy-validated` — detail: `{ result: ValidationResponse }`
    - `editor-cancelled` — detail: `{}`
  - Uses the same PolicyBuilder, ConstraintBuilder, ValidationEditor components

- Create `frontend/src/web-component/EmbeddedContext.tsx` — React Context provider:
  - Provides: `apiBaseUrl`, `authToken`, `mode`, `policyId`, `locale`, `theme`, `i18nStrings`, `onEvent` callback
  - Components read config from this context instead of env vars / localStorage when in embedded mode
  - Wraps children in `I18nProvider` and `ThemeProvider` so embedded consumers can customize language and styling

- Modify `frontend/src/services/api.ts` — Make API configuration injectable:
  - Accept base URL and auth token from EmbeddedContext when present
  - Fall back to env var / localStorage in standalone mode

- Create `frontend/src/web-component/index.ts` — Entry point for Web Component build:
  - Registers the custom element
  - Exports for direct ES module import

- Modify `frontend/vite.config.ts` — Add a library build target:
  - New npm script `build:component` that builds only the Web Component entry point
  - Output: `dist/odrl-policy-editor.js` (single bundled file with CSS inlined)
  - Standard `build` continues to produce the standalone app

- Modify `frontend/package.json`:
  - Add `build:component` script
  - Add `main`, `module`, `exports` fields pointing to the Web Component bundle
  - Add `files` array for npm publishing

- Create `frontend/examples/embed-example.html` — Standalone HTML demo showing:
  - `<odrl-policy-editor api-base-url="http://localhost:8080" locale="en" theme="light"></odrl-policy-editor>`
  - Event listeners logging policy-created, policy-validated events
  - Demonstrates attribute configuration including locale and theme switching
  - Shows how to pass custom i18n strings and theme via JS properties

**Acceptance criteria:**
- `<odrl-policy-editor>` custom element renders a fully functional policy editor
- Changing `api-base-url` attribute reconfigures the API client
- Setting `auth-token` attribute injects the bearer token into API calls
- Setting `policy-id` and `mode="edit"` loads an existing policy for editing
- Setting `locale` attribute changes the UI language (e.g., `locale="de"` switches to German if translations are provided)
- Setting `theme` attribute switches between light/dark modes; custom theme objects can be passed via JS property
- Custom i18n strings can be passed via JS property for full translation override
- Custom Events fire correctly on policy create/update/validate/cancel
- Shadow DOM isolates styles — no CSS leaks in or out
- `npm run build:component` produces a single JS file that can be loaded via `<script>`
- The standalone app (`npm run build`) is not affected by these changes
- `examples/embed-example.html` loads and renders correctly in a browser
- Unit tests for the Web Component lifecycle (mount, attribute changes, event dispatch)

---

### Step 5: Template Extensibility Architecture

**Goal:** Design and implement the hooks and interfaces needed for future policy template support, without implementing actual templates. The architecture should make it straightforward for a future iteration to add pre-defined templates that users fill out.

**Files to create/modify:**
- Create `frontend/src/types/PolicyTemplate.ts` — Template type definitions:
  ```typescript
  interface PolicyTemplate {
    id: string;
    name: string;
    description: string;
    category: string;
    /** Pre-filled ODRL policy structure with placeholder markers */
    skeleton: Record<string, unknown>;
    /** Which fields the user must fill in */
    editableFields: TemplateField[];
    /** Which fields are locked and cannot be changed */
    lockedFields: string[];
  }
  
  interface TemplateField {
    path: string;           // JSON path in the skeleton (e.g., "odrl:permission.odrl:target")
    label: string;          // Human-readable label
    description: string;    // Help text
    type: 'dropdown' | 'text' | 'constraint';
    required: boolean;
  }
  ```

- Create `frontend/src/hooks/useTemplateMode.ts` — Hook for template-aware editing:
  - Accepts optional `PolicyTemplate`
  - Returns: `isTemplateMode`, `isFieldLocked(path)`, `isFieldEditable(path)`, `getFieldMeta(path)`
  - When no template is provided, all fields are editable (current behavior)

- Modify `frontend/src/components/PolicyBuilder.tsx` (renamed from Baukasten) — Add template awareness:
  - Accept optional `template?: PolicyTemplate` prop
  - When template is set, pre-fill form from `template.skeleton`
  - Disable/lock fields listed in `template.lockedFields` (visual lock icon + disabled state)
  - Show `TemplateField.description` as help text for editable fields
  - Add a visual banner at the top: "Creating policy from template: {name}"

- Modify `frontend/src/components/TargetEditor.tsx` — Accept `locked?: boolean` prop to disable editing
- Modify `frontend/src/components/AssigneeEditor.tsx` — Accept `locked?: boolean` prop
- Modify `frontend/src/components/ConstraintBuilder.tsx` — Accept `lockedFields?: string[]` prop to selectively lock individual constraints

- Modify `frontend/src/web-component/OdrlPolicyEditorElement.ts` — Add `template` attribute/property:
  - Accepts serialized JSON template via property (not attribute, since it's complex data)
  - Passes template to EmbeddedApp → PolicyBuilder

- Create `frontend/src/types/index.ts` — Barrel export for all type definitions

**Acceptance criteria:**
- `PolicyTemplate` and `TemplateField` types are defined and exported
- `useTemplateMode` hook works correctly: returns unlocked when no template, locked fields when template provided
- PolicyBuilder renders normally when no template is passed (no regression)
- When a template is passed, skeleton data pre-fills the form and locked fields are visually disabled
- Web Component accepts a `template` property (JS property, not HTML attribute)
- Unit tests for `useTemplateMode` hook
- Unit tests verifying PolicyBuilder renders correctly with and without a template
- No actual template data/catalog is created — only the extensibility hooks

---

### Step 6: Comprehensive Testing & Production Polish

**Goal:** Ensure production readiness with comprehensive tests, polished UI, accessibility basics, and a clean build pipeline. Verify the entire frontend works end-to-end in both standalone and embedded modes.

**Files to create/modify:**
- Create `frontend/src/test/components/PolicyBuilder.test.tsx` — Unit tests:
  - Renders loading state while mappings load
  - Renders error state on mappings fetch failure
  - Populates action dropdown from mappings data
  - Groups dropdown items by namespace
  - Assembles correct ODRL JSON structure on form submission
  - Template mode: pre-fills and locks fields

- Create `frontend/src/test/components/ConstraintBuilder.test.tsx` — Unit tests:
  - Add/remove constraints
  - Toggle AND/OR/XONE logic
  - Select left operand, operator, right operand
  - Toggle named vs literal right operand
  - Outputs correct ODRL constraint JSON

- Create `frontend/src/test/components/ValidationEditor.test.tsx` — Unit tests:
  - HTTP Request mode fields
  - JSON Payload mode fields
  - Mode toggle
  - JWT helper generates valid unsigned JWT
  - Example button pre-fills form

- Create `frontend/src/test/components/ValidationResult.test.tsx` — Unit tests:
  - Allow result renders green indicator
  - Deny result renders red indicator
  - Explanation list renders correctly

- Create `frontend/src/test/components/NamespacedDropdown.test.tsx` — Unit tests:
  - Groups items by namespace prefix
  - Renders descriptions
  - Filter/search works
  - Handles empty items array

- Create `frontend/src/test/hooks/useMappings.test.ts` — Hook tests:
  - Returns loading state initially
  - Returns data after fetch
  - Returns error on failure
  - Caches data across multiple hook instances

- Create `frontend/src/test/hooks/useTemplateMode.test.ts` — Hook tests:
  - No template: all fields unlocked
  - With template: correct fields locked/unlocked

- Create `frontend/src/test/pages/PolicyList.test.tsx` — Page tests:
  - Renders policy table
  - Delete button removes policy
  - New policy button navigates

- Create `frontend/src/test/pages/PolicyEditor.test.tsx` — Page tests:
  - Create mode: empty form
  - Edit mode: loads existing policy
  - Save submits correct data
  - Validation modal opens and displays results

- Create `frontend/src/test/web-component/OdrlPolicyEditorElement.test.ts` — Web Component tests:
  - Custom element registers
  - Renders shadow DOM content
  - Attribute changes propagate
  - Custom events fire

- Modify `frontend/src/components/*.tsx` — Accessibility improvements:
  - Add `aria-label` and `aria-describedby` to all form controls
  - Ensure keyboard navigation works in dropdowns
  - Add `role` attributes where needed
  - Ensure color contrast meets WCAG AA

- Modify `frontend/Dockerfile` — Final production polish:
  - Add nginx.conf with proper SPA fallback (try_files), gzip, cache headers
  - Health check endpoint

- Modify `frontend/package.json` — Add scripts:
  - `test:coverage` — Run tests with coverage report
  - `test:ci` — Run tests in CI mode (no watch)

- Modify `frontend/eslint.config.js` — Add accessibility linting:
  - Add `eslint-plugin-jsx-a11y` for accessibility checks

- Create `frontend/src/test/i18n/useI18n.test.ts` — Localization tests:
  - Default locale returns English strings
  - Switching locale returns translated strings
  - Partial override merges with defaults
  - Missing keys fall back to English

- Create `frontend/src/test/theme/ThemeContext.test.tsx` — Theme tests:
  - Default theme applies CSS custom properties
  - Custom theme overrides specific properties
  - Light/dark mode switching works
  - Theme changes propagate to all components

**Acceptance criteria:**
- All unit tests pass (`npm run test`)
- Test coverage is >=70% for components and hooks
- ESLint passes with no errors (`npm run lint`)
- Production build succeeds (`npm run build`)
- Web Component build succeeds (`npm run build:component`)
- Docker image builds and serves correctly
- No accessibility warnings from jsx-a11y ESLint plugin
- All form controls are keyboard-navigable
- Loading and error states work throughout the app
- Localization system works end-to-end: UI language can be switched at runtime
- Theme customization works: colors, fonts, and spacing respond to theme changes
- No hardcoded user-facing strings remain in components (all sourced from i18n)
