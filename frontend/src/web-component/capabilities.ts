/**
 * Capability modes for the embedded editor.
 *
 * Every operation that needs a backend is modelled as a named *capability*
 * with a *mode* that says where it is served from:
 *
 * - `remote` — served by the PAP API (the historical behaviour, and the default).
 * - `none`   — the capability is switched off: no request is made and the UI
 *              that depends on it is not rendered.
 * - `event`  — the host answers through a Custom Event: the UI stays, but
 *              instead of calling the PAP the editor hands the result to the
 *              embedder. Only {@link SUPPORTED_CAPABILITY_MODES} says which
 *              capabilities understand it; today that is `save`, where it
 *              turns the editor into a pure policy builder.
 *
 * To add a capability, add it to {@link CAPABILITIES}, list the modes it
 * accepts in {@link SUPPORTED_CAPABILITY_MODES} and read it through
 * {@link isRemote} (or compare the mode directly when it has more than two
 * states) — nothing else needs to know the full list.
 */

/** Operations that can be served by the PAP backend. */
export const CAPABILITIES = ['load', 'save', 'validate', 'templates', 'mappings'] as const;

/** Where a capability is served from. */
export const CAPABILITY_MODES = ['remote', 'none', 'event'] as const;

export type Capability = (typeof CAPABILITIES)[number];
export type CapabilityMode = (typeof CAPABILITY_MODES)[number];
export type CapabilityModes = Record<Capability, CapabilityMode>;

/** Mode used when a capability's attribute is absent or invalid. */
export const DEFAULT_CAPABILITY_MODE: CapabilityMode = 'remote';

/**
 * Modes each capability accepts.
 *
 * A mode a capability has no handling for is not merely useless, it would be
 * misleading — `templates="event"` suggests the host is asked for templates
 * when nothing emits such an event. Listing the modes per capability keeps
 * those combinations out of the public contract, and parsing falls back to
 * {@link DEFAULT_CAPABILITY_MODE} for them just as it does for a typo.
 */
export const SUPPORTED_CAPABILITY_MODES: Record<Capability, readonly CapabilityMode[]> = {
  load: ['remote', 'none'],
  save: ['remote', 'none', 'event'],
  validate: ['remote', 'none'],
  templates: ['remote', 'none'],
  mappings: ['remote', 'none'],
};

/** Every capability served remotely: the behaviour prior to capability flags. */
export const DEFAULT_CAPABILITIES: CapabilityModes = Object.fromEntries(
  CAPABILITIES.map((capability) => [capability, DEFAULT_CAPABILITY_MODE]),
) as CapabilityModes;

/**
 * Parses a raw attribute value into a {@link CapabilityMode}.
 *
 * Unknown values — and modes the capability does not support — fall back to
 * the default so a typo never silently disables (or, worse, silently enables)
 * a backend call.
 *
 * @param capability - The capability the value was read for.
 * @param raw - The attribute value, or `null` when the attribute is absent.
 */
export function parseCapabilityMode(capability: Capability, raw: string | null): CapabilityMode {
  const value = raw?.trim().toLowerCase();
  return (SUPPORTED_CAPABILITY_MODES[capability] as readonly string[]).includes(value ?? '')
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
    CAPABILITIES.map((capability) => [
      capability,
      parseCapabilityMode(capability, getAttribute(capability)),
    ]),
  ) as CapabilityModes;
}

/** Whether the capability is served by the PAP backend. */
export function isRemote(modes: CapabilityModes, capability: Capability): boolean {
  return modes[capability] === 'remote';
}

/** Whether the capability is answered by the host through a Custom Event. */
export function isEvent(modes: CapabilityModes, capability: Capability): boolean {
  return modes[capability] === 'event';
}

/** Whether the capability is available at all, remotely or through the host. */
export function isEnabled(modes: CapabilityModes, capability: Capability): boolean {
  return modes[capability] !== 'none';
}
