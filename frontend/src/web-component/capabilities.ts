/**
 * Capability modes for the embedded editor.
 *
 * Every operation that needs a backend is modelled as a named *capability*
 * with a *mode* that says where it is served from. Today there are two modes:
 *
 * - `remote` — served by the PAP API (the historical behaviour, and the default).
 * - `none`   — the capability is switched off: no request is made and the UI
 *              that depends on it is not rendered.
 *
 * To add a mode later (e.g. `event`, where the host answers through a Custom
 * Event) extend {@link CAPABILITY_MODES} and handle it where the capability is
 * consumed; the attribute parsing here does not need to change. To add a new
 * capability, add it to {@link CAPABILITIES} and read it through
 * {@link isRemote} — nothing else needs to know the full list.
 */

/** Operations that can be served by the PAP backend. */
export const CAPABILITIES = ['load', 'save', 'validate', 'templates', 'mappings'] as const;

/** Where a capability is served from. */
export const CAPABILITY_MODES = ['remote', 'none'] as const;

export type Capability = (typeof CAPABILITIES)[number];
export type CapabilityMode = (typeof CAPABILITY_MODES)[number];
export type CapabilityModes = Record<Capability, CapabilityMode>;

/** Mode used when a capability's attribute is absent or invalid. */
export const DEFAULT_CAPABILITY_MODE: CapabilityMode = 'remote';

/** Every capability served remotely: the behaviour prior to capability flags. */
export const DEFAULT_CAPABILITIES: CapabilityModes = Object.fromEntries(
  CAPABILITIES.map((capability) => [capability, DEFAULT_CAPABILITY_MODE]),
) as CapabilityModes;

/**
 * Parses a raw attribute value into a {@link CapabilityMode}.
 *
 * Unknown values fall back to the default so a typo never silently disables
 * (or, worse, silently enables) a backend call.
 *
 * @param raw - The attribute value, or `null` when the attribute is absent.
 */
export function parseCapabilityMode(raw: string | null): CapabilityMode {
  const value = raw?.trim().toLowerCase();
  return (CAPABILITY_MODES as readonly string[]).includes(value ?? '')
    ? (value as CapabilityMode)
    : DEFAULT_CAPABILITY_MODE;
}

/**
 * Resolves all capability modes from a getter for the host's attributes.
 *
 * @param getAttribute - Reads an attribute by name (e.g. `el.getAttribute`).
 */
export function resolveCapabilities(
  getAttribute: (name: string) => string | null,
): CapabilityModes {
  return Object.fromEntries(
    CAPABILITIES.map((capability) => [capability, parseCapabilityMode(getAttribute(capability))]),
  ) as CapabilityModes;
}

/** Whether the capability is served by the PAP backend. */
export function isRemote(modes: CapabilityModes, capability: Capability): boolean {
  return modes[capability] === 'remote';
}
