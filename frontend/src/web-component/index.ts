/**
 * Entry point for the ODRL Policy Editor Web Component.
 *
 * Importing this module registers the `<odrl-policy-editor>` custom
 * element in the browser's Custom Elements registry. The element can
 * then be used in any HTML page without a build step:
 *
 * ```html
 * <script type="module" src="odrl-policy-editor.js"></script>
 * <odrl-policy-editor api-base-url="https://pap.example.com"></odrl-policy-editor>
 * ```
 *
 * For programmatic use (ES module import):
 *
 * ```ts
 * import { OdrlPolicyEditorElement, TAG_NAME } from 'odrl-policy-editor';
 * ```
 */
import { OdrlPolicyEditorElement, TAG_NAME } from './OdrlPolicyEditorElement';

// Register the custom element only if it hasn't been registered already.
// This prevents errors when the script is loaded multiple times.
if (!customElements.get(TAG_NAME)) {
  customElements.define(TAG_NAME, OdrlPolicyEditorElement);
}

export { OdrlPolicyEditorElement, TAG_NAME };
export type {
  EmbeddedConfig,
  EmbeddedEventMap,
  EditorMode,
  EmbeddedThemePreset,
  OnEventCallback,
  TabVisibility,
} from './EmbeddedContext';
