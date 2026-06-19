# Additional Compaction Contexts

The PAP's `/validate` endpoint accepts an optional `additionalContexts` field in the
`ValidationRequest`. This allows callers to control how ODRL policy terms are compacted
during JSON-LD processing, without modifying the policy itself.

## Problem

A single ODRL policy may be used across different components (MCP, DCP, etc.), each of
which interprets certain terms differently. The policy is authored in standard ODRL:

```json
{
  "odrl:permission": {
    "odrl:action": {
      "@id": "odrl:use"
    }
  }
}
```

For the PAP to route `odrl:use` to the correct Rego implementation, it needs to know
which protocol context applies. The goal is to remap selected terms (e.g., `odrl:use`
to `mcp:use`) while keeping the base ODRL structure (`odrl:permission`,
`odrl:constraint`, etc.) intact.

## Mechanism: JSON-LD 1.1 Scoped Contexts

The PAP uses standard JSON-LD 1.1 expand-then-compact processing. During compaction,
namespace prefixes from the compaction context determine how full IRIs are shortened.
JSON-LD 1.1 [scoped contexts](https://www.w3.org/TR/json-ld11/#scoped-contexts) allow
attaching a different context to a specific term, so that only the values of that term
are compacted with the overridden prefix.

### How it works

1. The policy is **expanded** to full IRIs (e.g., `odrl:use` becomes
   `http://www.w3.org/ns/odrl/2/use`).
2. The expanded policy is **compacted** using the PAP's base compaction context merged
   with any `additionalContexts` provided in the request.
3. A **scoped context** on a term like `odrl:action` overrides the active prefix only
   within that term's values. The rest of the document uses the base `odrl:` prefix as
   usual.

### Base compaction context

The PAP ships with a default compaction context
(`src/main/resources/compaction-context.jsonld`):

```json
{
  "odrl":    { "@id": "http://www.w3.org/ns/odrl/2/",     "@prefix": true },
  "dome-op": { "@id": "https://github.com/DOME-Marketplace/dome-odrl-profile#", "@prefix": true },
  "xsd":     { "@id": "http://www.w3.org/2001/XMLSchema#", "@prefix": true },
  "pap":     { "@id": "https://odrl-pap.io/context#",      "@prefix": true },
  "json":    { "@id": "https://odrl-pap.io/json#",         "@prefix": true }
}
```

Without additional contexts, all terms in `http://www.w3.org/ns/odrl/2/` are compacted
with the `odrl:` prefix.

## Constructing an Additional Context

### Remap a single term (scoped context)

To remap only the **action** value from `odrl:use` to `mcp:use`, provide a scoped
context on the `odrl:action` term:

```json
{
  "odrl:action": {
    "@id": "http://www.w3.org/ns/odrl/2/action",
    "@type": "@id",
    "@context": {
      "odrl": null,
      "mcp": { "@id": "http://www.w3.org/ns/odrl/2/", "@prefix": true }
    }
  }
}
```

| Field | Purpose |
|-------|---------|
| `"odrl:action"` | The term whose values should be remapped. Uses the compact IRI form; the `odrl` prefix is resolved from the base context. |
| `"@id"` | The full IRI of the term (`http://www.w3.org/ns/odrl/2/action`). Must match the expanded form of the term key. |
| `"@type": "@id"` | Declares that the term's values are IRI references. This is required for the compaction algorithm to apply prefix compaction to the values. |
| `"@context"` | The **scoped context** that overrides the active context within this term's values only. |
| `"odrl": null` | **Removes** the `odrl` prefix inside the scope. Without this, both `odrl` and `mcp` would be valid prefixes for the same namespace, and the compactor might still choose `odrl`. |
| `"mcp": { ... }` | **Adds** the replacement prefix. Any IRI in `http://www.w3.org/ns/odrl/2/` within this scope now compacts with `mcp:`. |

### Remap multiple terms

To remap values of several properties, add a scoped context entry for each one. For
example, to remap both actions and left operands:

```json
{
  "odrl:action": {
    "@id": "http://www.w3.org/ns/odrl/2/action",
    "@type": "@id",
    "@context": {
      "odrl": null,
      "mcp": { "@id": "http://www.w3.org/ns/odrl/2/", "@prefix": true }
    }
  },
  "odrl:leftOperand": {
    "@id": "http://www.w3.org/ns/odrl/2/leftOperand",
    "@type": "@id",
    "@context": {
      "odrl": null,
      "mcp": { "@id": "http://www.w3.org/ns/odrl/2/", "@prefix": true }
    }
  }
}
```

Each scoped context is independent. You can use different replacement prefixes per term
if needed (e.g., `mcp` for actions, `dcp` for operands).

## Full Request Example

Given this policy (shared across components, cannot be modified):

```json
{
  "@context": {
    "odrl": "http://www.w3.org/ns/odrl/2/"
  },
  "@type": "odrl:Offer",
  "@id": "dc1e013a-67e5-4393-bc31-495d780a6a24",
  "odrl:assigner": { "@id": "did:web:mp-operations.org" },
  "odrl:permission": {
    "odrl:action": { "@id": "odrl:use" },
    "odrl:target": { "@id": "ASSET-1" },
    "odrl:constraint": {
      "odrl:leftOperand": { "@id": "odrl:dayOfWeek" },
      "odrl:operator": { "@id": "odrl:lt" },
      "odrl:rightOperand": { "@value": 6 }
    }
  }
}
```

A caller that wants `odrl:use` interpreted as `mcp:use` sends:

```json
{
  "policy": {
    "@context": {
      "odrl": "http://www.w3.org/ns/odrl/2/"
    },
    "@type": "odrl:Offer",
    "@id": "dc1e013a-67e5-4393-bc31-495d780a6a24",
    "odrl:assigner": { "@id": "did:web:mp-operations.org" },
    "odrl:permission": {
      "odrl:action": { "@id": "odrl:use" },
      "odrl:target": { "@id": "ASSET-1" },
      "odrl:constraint": {
        "odrl:leftOperand": { "@id": "odrl:dayOfWeek" },
        "odrl:operator": { "@id": "odrl:lt" },
        "odrl:rightOperand": { "@value": 6 }
      }
    }
  },
  "additionalContexts": [
    {
      "odrl:action": {
        "@id": "http://www.w3.org/ns/odrl/2/action",
        "@type": "@id",
        "@context": {
          "odrl": null,
          "mcp": { "@id": "http://www.w3.org/ns/odrl/2/", "@prefix": true }
        }
      }
    }
  ],
  "testRequest": {
    "method": "GET",
    "host": "example.com",
    "path": "/assets/ASSET-1",
    "protocol": "https"
  }
}
```

### Resulting compacted policy (internal)

After expand + compact, the PAP sees:

```json
{
  "@type": "odrl:Offer",
  "odrl:permission": {
    "odrl:action": "mcp:use",
    "odrl:target": { "@id": "ASSET-1" },
    "odrl:constraint": {
      "odrl:leftOperand": "odrl:dayOfWeek",
      "odrl:operator": "odrl:lt",
      "odrl:rightOperand": { "@value": 6 }
    }
  }
}
```

- `odrl:permission`, `odrl:constraint`, `odrl:target` etc. are **unchanged**.
- `odrl:use` became **`mcp:use`** inside `odrl:action`.
- The ODRL-to-Rego mapper resolves `mcp:use` via `mapping.json` to the appropriate
  Rego method.

## Important Notes

- **`"odrl": null` is required** in the scoped context. Without it, both `odrl` and the
  replacement prefix map to the same namespace IRI, and the compaction algorithm may
  still choose `odrl`.
- **`"@type": "@id"` is required** on the term definition. Without it, the compaction
  algorithm treats values as plain strings and does not apply prefix compaction.
- **`"@id"` must match** the full IRI expansion of the term key. For `odrl:action` this
  is `http://www.w3.org/ns/odrl/2/action`.
- The replacement prefix (`mcp`, `dcp`, etc.) and the terms it produces (e.g.,
  `mcp:use`) must have corresponding entries in `mapping.json` for the PAP to map them
  to Rego methods.
- String values that are not `@id` references (e.g., `"odrl:dayOfWeek"` when used as a
  plain string value rather than `{"@id": "odrl:dayOfWeek"}`) are **not affected** by
  prefix remapping. JSON-LD compaction only applies prefix resolution to `@id` and
  `@type` values, and to property keys.
