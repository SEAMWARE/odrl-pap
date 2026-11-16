# Policy shapes handled by the editor

Technical reference for how the Builder, the Summary and the Raw tabs read and write ODRL policies. It applies to the standalone app (`pages/PolicyEditor.tsx`) and to the `<odrl-policy-editor>` web component (`web-component/EmbeddedApp.tsx`), which share the components and utilities listed in [Code map](#code-map).

General rule: the editor reads every shape listed here, writes back the shape it found, and only produces the default form (`odrl:` prefix, plain arrays) for parts that do not exist yet. Keys it does not edit are never modified, reordered or removed.

## Contents

1. [Permissions](#1-permissions)
2. [Term spelling](#2-term-spelling)
3. [Target](#3-target)
4. [Constraints](#4-constraints)
5. [Right operand literals](#5-right-operand-literals)
6. [Action](#6-action)
7. [`@context`](#7-context)
8. [Parts the PAP does not evaluate](#8-parts-the-pap-does-not-evaluate)
9. [Raw input and render errors](#9-raw-input-and-render-errors)
10. [Code map](#code-map)

## 1. Permissions

`odrl:permission` may be an object or an array of objects.

| Input | Builder edits | Written back |
|---|---|---|
| object | the object | object |
| array | `array[0]` | array; `array[0]` replaced, `array[1..]` kept by reference |
| missing | an empty permission | object under `odrl:permission` |

When the array has more than one entry the Builder shows a notice (`data-testid="multiple-permissions-notice"`) stating that only the first is editable. The Summary reads the same first permission. The backend maps every entry of an array (`OdrlMapper`), so the others stay effective.

Implementation: `utils/permission.ts` (`readPermission`, `writePermission`, `permissionCount`).

## 2. Term spelling

An ODRL term can be written prefixed (`odrl:target`), bare (`target`, as the ODRL JSON-LD context allows) or as the full IRI (`http://www.w3.org/ns/odrl/2/target`).

- **Read**: the three spellings are accepted, looked up in that order.
- **Write**: the value goes to the key the object already uses. If the term is absent, the prefixed key is used, since the editor's default `@context` defines only the `odrl` prefix.
- Mixed policies (some terms prefixed, some bare) keep each key as it is.

Applies to: `permission`, `target`, `assignee`, `action`, `constraint`, `refinement`, `leftOperand`, `operator`, `rightOperand`.

Implementation: `utils/odrlTerms.ts` (`getTerm`, `setTerm`, `termKey`, `findTermKey`, `removeTerm`). Values of terms (for example the action `odrl:read`) are not rewritten.

## 3. Target

The backend does not read the target only from `odrl:target`. `OdrlMapper` looks in the permission for any key of the `target` section of `mapping.json` and takes the rego rule from the key found; the key's value is the target id.

| Key | Rule |
|---|---|
| `odrl:target`, `odrl:uid` | `odrl_target.is_target(helper.target, id)` |
| `json:target`, `json:uid` | `odrl_target.is_target(generic.target, id)` |
| `json:payloadTarget` | `odrl_target.is_target(generic.get_value(%s), %s)` |

The target editor therefore treats the key as part of the value:

- **Custom Target (URL)** writes the plain target term (spelling per section 2).
- **Select from available targets** shows the options from `/mappings` (`mappings.targets`). The chosen option is the key; the text field is the value. Changing the option moves the value to the new key.
- Only one target key is kept in the permission: writing a key removes the other target keys. If the key does not change, the position of the key in the object is preserved.
- A permission loaded with a non-plain target key (for example `json:uid`) opens in *Select* mode with the option and value filled in. The Summary shows the target for any of these keys.

`json:payloadTarget` consumes two format arguments in `mapping.json`; `OdrlMapper.mapStringTarget` supplies one.

Implementation: `utils/target.ts` (`findTargetKey`, `writeTarget`, `isPlainTargetKey`), `components/TargetEditor.tsx` (`targetKey` prop, `setTarget(value, key?)`).

## 4. Constraints

All reads go through `readConstraintGroup` (`utils/constraints.ts`), used by both `ConstraintBuilder` and `PolicySummary`.

Accepted shapes of the constraint value (and of the operands of a logical constraint):

| Shape | Group |
|---|---|
| array | `and` |
| `{"@list": [...]}` | `and`, list wrapper remembered |
| lone constraint object | `and` with one constraint |
| `odrl:or` / `odrl:xone` / `odrl:and` / `odrl:andSequence`, with or without `"@type": "odrl:LogicalConstraint"` | the named group |
| operands of a logical constraint as array, `@list` or single object | normalised to an array |

Non-object entries (such as `null`) are dropped when reading. `andSequence` is displayed read-only.

Write behaviour of `ConstraintBuilder`:

- A logical constraint is written back as the object it was loaded as (its `@type`, or none, and any other keys) around the edited operands, as long as the grouping is unchanged. Changing the grouping selector writes the new form: `and` becomes the plain array, `or`/`xone` become `{"@type": "odrl:LogicalConstraint", "odrl:<type>": [...]}`.
- An `@list` stays an `@list`.
- A plain array stays an array.
- The property key follows section 2 (`constraint` or `refinement`, in the spelling in use).
- **Blank row**: *Add constraint* shows an empty row, but only constraints with at least one key are written to the policy; the backend rejects an empty constraint (`LeftOperandMapper`: "The provided constraint does not contain a left operand."). One blank row can exist at a time, and the add button is disabled while it does. Constraints that are empty on load are dropped on the first write.

## 5. Right operand literals

A literal right operand is written as `{"@value": "..."}`. `@type` is added when the user types a datatype and removed when the field is cleared; an empty `@type` is never written, including when a loaded `{"@value": "x", "@type": ""}` is edited. A plain string right operand is read as a literal.

## 6. Action

The Action dropdown reads the action as a string (`"odrl:read"`) or as an object with `@id` (`{"@id": "odrl:read"}`). Editing other fields leaves the action as loaded; choosing an action writes a string. The Summary accepts both forms.

## 7. `@context`

Entries are added with `addContextEntry` (`utils/jsonLdContext.ts`), which keeps what the context already holds:

| Current `@context` | After adding `prefix: uri` |
|---|---|
| absent | `{ prefix: uri }` |
| object | the object with the prefix added |
| string (remote context IRI) | `[iri, { prefix: uri }]` |
| array | the prefix added to the last object of the array; a new `{ prefix: uri }` appended if there is none |

`listContextEntries` lists prefixes and remote IRIs for display for all shapes; `removeContextPrefix` removes a prefix from an object or from the objects of an array and drops array objects left empty. Term definitions (`{"@id": ..., "@prefix": true}`) are displayed by their `@id`. New policies are created with `{ "odrl": "http://www.w3.org/ns/odrl/2/" }`.

## 8. Parts the PAP does not evaluate

`OdrlMapper` maps `permission` only. `prohibition`, `obligation`, `assigner` and `profile` are never read when generating rego (`odrl:assigner` is stored by `fillTopLevel` and not used afterwards; `OdrlConstants.PROFILE_KEY` is not referenced).

The Builder shows a notice (`data-testid="unsupported-parts-notice"`) naming the parts present: those four terms at policy level, and `assigner` inside the edited permission. The keys are not touched when the permission is edited; their values keep object identity.

Both notices (this one and the multiple-permissions one) are `info` alerts, so in the web component they take their colours from `--odrl-info-color`. Alerts follow the status tokens `--odrl-danger-color`, `--odrl-success-color`, `--odrl-info-color` and `--odrl-warning-color` (`theme/theme.css`, `.odrl-embedded-root .alert-*`): the status colour tints the background (10%) and the border (35%), and the text is the status colour mixed 40/60 with `--odrl-text-color`. With the default presets the text/background contrast measured in the rendered component is 5.3 to 7.7:1 (light) and 7.4 to 9.5:1 (dark) for info, success, danger and warning.

## 9. Raw input and render errors

- **Raw tabs** (page and web component): text that parses to anything other than a JSON object (`null`, array, number, string, boolean) is rejected with the message "The policy must be a JSON object."; the policy state and the `policy-changed` event are not updated. Invalid JSON keeps showing the parser message.
- **`ErrorBoundary`** (`components/ErrorBoundary.tsx`) wraps the Builder and the Summary in the page, in the web component, and the Summary inside the Builder. If rendering throws, the boundary shows the error message in an alert and the rest of the page, including the Raw tab, keeps working. It renders its children again when its `resetKey` (the policy object) changes.

## Code map

| Concern | Files |
|---|---|
| Permission array handling | `utils/permission.ts`, `components/PolicyBuilder.tsx` |
| Term spelling | `utils/odrlTerms.ts` |
| Target keys | `utils/target.ts`, `components/TargetEditor.tsx` |
| Constraint shapes and groups | `utils/constraints.ts`, `components/ConstraintBuilder.tsx`, `components/PolicySummary.tsx` |
| Collection types (`AssetCollection`, `PartyCollection`) | `components/collectionType.ts` |
| `@context` | `utils/jsonLdContext.ts`, `pages/PolicyEditor.tsx`, `web-component/EmbeddedApp.tsx` |
| Errors | `components/ErrorBoundary.tsx` |
| Strings (`en`, `es`) | `i18n/en.ts`, `i18n/es.ts`: `policyBuilder.multiplePermissionsNotice`, `policyBuilder.unsupportedPartsNotice`, `policyEditor.jsonNotObject`, `errorBoundary.*` |

### Tests

| Behaviour | Test file |
|---|---|
| Permission arrays | `components/PolicyBuilder.permissions.test.tsx` |
| Term spelling | `utils/odrlTerms.test.ts`, `components/PolicyBuilder.terms.test.tsx` |
| Target keys | `utils/target.test.ts`, `components/PolicyBuilder.target.test.tsx` |
| `@list` and logical operands | `components/ConstraintBuilder.list.test.tsx`, `components/PolicySummary.list.test.tsx` |
| Logical groups, blank rows | `components/ConstraintBuilder.groups.test.tsx` |
| Literals | `components/ConstraintBuilder.literal.test.tsx` |
| Action forms | `components/PolicyBuilder.action.test.tsx` |
| `@context` | `utils/jsonLdContext.test.ts`, `pages/PolicyEditor.test.tsx` |
| Ignored parts | `components/PolicyBuilder.unsupported.test.tsx` |
| Raw input | `pages/PolicyEditor.test.tsx`, `web-component/EmbeddedApp.test.tsx` |
| Error boundary | `components/ErrorBoundary.test.tsx` |

Run with `npx vitest run` in `frontend/`.
