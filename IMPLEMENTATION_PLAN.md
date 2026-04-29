# Implementation Plan: Support evaluation of non-HTTP requests

## Overview
Extend the ODRL PAP to support evaluation of policies against arbitrary JSON payloads, not just HTTP requests forwarded from API gateways. The `Pep` enum is NOT modified — it stays exclusively for API gateway selection (apisix/kong). Instead, the policy's own JSON-LD `@context` drives the selection of the appropriate evaluation helpers (HTTP vs. generic JSON). This involves creating a new generic rego helper for JSON value extraction (loaded alongside the gateway helper), extending the validation API to accept arbitrary JSON input, adding new `json:*` namespace mappings for non-HTTP evaluation, and using JSON-LD context to route policies to the correct evaluation context.

## Steps

### Step 1: Create generic rego helper and update BundleResource loading

**Goal:** Add a new `generic.rego` utils helper that extracts values from arbitrary JSON payloads instead of HTTP request structures. The `Pep` enum is NOT modified — it stays exclusively for API gateway selection (apisix/kong). Instead, the generic helper is loaded alongside the gateway-specific helper so that it is available whenever a policy's JSON-LD context indicates non-HTTP evaluation.

**Files to create:**
- `src/main/resources/rego/utils/generic.rego` — New helper file with `package utils.generic` (NOT `utils.helper` — to avoid colliding with the gateway-specific helper) that provides:
  - `request` — the full input request object
  - `payload` — the JSON payload (from `input.request.payload`)
  - `entity` — alias for the payload (for compatibility with existing mappings that use `helper.entity`)
  - `target` — extracted from a `target` field in the payload (or a configurable path)
  - `organization_did` — same as existing helpers (from `data.data.organizationDid`)
  - `get_value(path)` — generic value extraction using `walk()` with dot-notation paths (similar to `http.leftOperand.body_value` in `rego/http/leftOperand.rego`)
  - `subject` — extracted from payload (e.g., `payload.subject` or `payload.issuer`)

**Files to modify:**
- `src/main/java/org/fiware/odrl/BundleResource.java` — Update `initMethods()` to always load `rego/utils/generic.rego` alongside the PEP-specific utils file. The generic helper uses a distinct package (`utils.generic`) so it does not conflict with `utils.helper` from the gateway helpers.
- `src/main/resources/rego-resources.txt` — Add `rego/utils/generic.rego` entry alongside the existing `rego/utils/apisix.rego` and `rego/utils/kong.rego` entries (append to each cumulative block that lists utils files).

**Acceptance criteria:**
- `generic.rego` compiles as valid Rego and uses `package utils.generic`.
- The `Pep` enum is NOT modified — `apisix` and `kong` remain the only values.
- `BundleResource.initMethods()` always loads `generic.rego` in addition to the selected gateway helper.
- The generic helper provides `get_value` for JSONPath-like extraction using the same `walk()` approach as `http.leftOperand.body_value`.
- Existing PEP values (`apisix`, `kong`) continue to work unchanged.

### Step 2: Extend OpenAPI spec and validation request model

**Goal:** Update the API to accept arbitrary JSON payloads for validation alongside the existing HTTP `TestRequest`.

**Files to modify:**
- `api/odrl.yaml` — Add new schemas and update the validation endpoint:
  - Add `GenericJsonInput` schema: an object with a required `payload` property (type: object, arbitrary JSON) and an optional `subject` property (type: object, for identity/credential info).
  - Update `ValidationRequest` to add a new optional `jsonInput` property of type `GenericJsonInput`, alongside the existing `testRequest` property. The two are mutually exclusive — a request provides one or the other.
  - No explicit `inputType` discriminator is needed: the policy's JSON-LD `@context` determines which evaluation path is used (see Step 4). The presence of `jsonInput` vs. `testRequest` indicates which input format is being sent.
  - Update the `validatePolicy` operation description to document both input modes.

**Acceptance criteria:**
- The OpenAPI spec validates and generates updated model classes via `./mvnw compile`.
- `ValidationRequest` has both `testRequest` (existing) and `jsonInput` (new) properties.
- The policy's LD-context (not an explicit discriminator) drives evaluation routing — see Step 4.
- Backward compatibility: requests using only `testRequest` continue to work without any changes.

### Step 3: Add generic JSON mappings and rego modules

**Goal:** Create new mapping entries in `mapping.json` and corresponding rego modules that work with generic JSON payloads rather than HTTP requests.

**Files to create:**
- `src/main/resources/rego/json/action.rego` — Generic action evaluator for non-HTTP contexts. Instead of checking HTTP methods (GET=read, POST=create), provides action checks based on an `action` field in the JSON payload (e.g., `payload.action == "read"`).
- `src/main/resources/rego/json/leftOperand.rego` — Left operand extractors for generic JSON:
  - `payload_value(payload, path)` — Extract a value from the payload at a given dot-notation path (delegates to walk-based extraction).
  - `subject_value(subject, path)` — Extract a value from the subject/identity object.

**Files to modify:**
- `src/main/resources/mapping.json` — Add new `json` namespace entries:
  - `action.json.*` — Actions like `read`, `write`, `create`, `delete`, `use` that check `payload.action` field instead of HTTP method.
  - `leftOperand.json.*` — Left operands like `payloadValue` (extract value by path from payload), `subjectValue` (extract from subject), `payloadType` (get type field from payload).
  - `target.json.*` — Target matching using `helper.payload.target` or a generic path-based target.
  - `assignee.json.*` — Assignee matching from `helper.payload.subject` or `helper.subject`.
- `src/main/resources/rego-resources.txt` — Add the new `rego/json/action.rego` and `rego/json/leftOperand.rego` entries.

**Acceptance criteria:**
- New rego modules compile as valid Rego.
- `mapping.json` has a `json` namespace under `action`, `leftOperand`, `target`, and `assignee` with at least 2-3 entries each.
- Rego methods in the new modules reference `generic.payload` and `generic.subject` from the generic utils helper (Step 1, package `utils.generic`).
- Existing mappings under `odrl`, `dome-op`, `http`, etc. remain unchanged.
- The `/mappings` endpoint returns the new `json:*` entries alongside existing ones.

### Step 4: Update ValidationResource and add JSON-LD context routing

**Goal:** Modify the backend to handle both HTTP and generic JSON validation flows. The key architectural decision: the policy's own JSON-LD `@context` determines which evaluation path (HTTP vs. generic JSON) is used. The `Pep` enum is NOT involved in this selection — it remains solely for choosing the API gateway (apisix/kong). Instead, when a policy declares a non-HTTP LD-context namespace, the system routes it to the generic evaluation path.

**Files to modify:**
- `src/main/java/org/fiware/odrl/ValidationResource.java` — Update `validatePolicy()`:
  - After compacting the policy via `JsonLdHandler`, call `detectEvaluationContext()` to determine if the policy targets HTTP or generic JSON evaluation.
  - For HTTP context (existing/default): keep current behavior — wrap as `{"input": {"request": testRequest}}`.
  - For JSON context (detected from LD-context): wrap as `{"input": {"request": {"payload": jsonInput.payload, "subject": jsonInput.subject}}}` so the generic rego helper can access it via `input.request.payload`.
  - Extract and log the detected evaluation context for debugging.

- `src/main/resources/compaction-context.jsonld` — Add a new namespace for the PAP evaluation context:
  - Add `"pap": { "@id": "https://odrl-pap.io/context#", "@prefix": true }` to enable policies to declare their evaluation context.

- `src/main/java/org/fiware/odrl/jsonld/JsonLdHandler.java` — Add a method `detectEvaluationContext(String compactedJson)` that:
  - Parses the compacted JSON-LD and inspects the policy's `@context` or namespace prefixes to detect non-HTTP evaluation targets.
  - Checks for `pap:evaluationContext` field or the presence of `json:*` namespaced terms in the policy.
  - Returns an enum/string indicating `"http"` (default) or `"json"`.
  - This is the central routing mechanism: the policy's LD-context drives helper selection, NOT the Pep enum.

- `src/main/java/org/fiware/odrl/mapping/OdrlMapper.java` — The mapper already resolves mappings by namespace prefix (e.g., `odrl:read` → `action.odrl.read`, `http:isInPath` → `operator.http.isInPath`). Policies using `json:*` namespaced terms will naturally resolve to `action.json.*`, `leftOperand.json.*`, etc. from the mapping config (added in Step 3). No special "evaluation context parameter" is needed — the LD-context compaction already produces the correct namespace prefixes that the mapper handles.

**Acceptance criteria:**
- Sending a `ValidationRequest` with `jsonInput` evaluates the policy against the JSON payload when the policy's LD-context indicates non-HTTP evaluation.
- Sending a `ValidationRequest` with `testRequest` continues to work exactly as before for policies with HTTP LD-contexts.
- The compaction context supports a `pap:evaluationContext` field in policies.
- `JsonLdHandler.detectEvaluationContext()` correctly detects the evaluation context from a policy's LD-context/namespace usage.
- The `Pep` enum is not used for this routing — helper selection is driven entirely by the policy's JSON-LD context.
- Error handling: if a JSON-context policy is submitted without `jsonInput` (or vice versa), return a clear error.

### Step 5: Add tests for non-HTTP evaluation

**Goal:** Add comprehensive unit and integration tests for the entire non-HTTP evaluation path.

**Files to create:**
- `src/test/resources/examples/json/` — New test directory with example policies and expected rego for non-HTTP evaluation:
  - At least 2 example policies that use `json:*` actions and operands.
  - Each example has `_NNNN.json` (ODRL policy with `pap:evaluationContext` context) and `NNNN.rego` (expected rego output).
  - At least 1 example JSON payload for validation testing.

- `src/test/java/org/fiware/odrl/GenericJsonValidationTest.java` — Test class covering:
  - Unit test: generic rego helper correctly extracts values from JSON payloads.
  - Unit test: `json:*` mapping entries resolve correctly.
  - API test: `POST /validate` with `jsonInput` returns correct allow/deny results.
  - Backward compatibility test: existing HTTP validation tests still pass.

**Files to modify:**
- `src/test/java/org/fiware/odrl/OdrlTest.java` — Add utility methods for creating test JSON payloads (similar to existing `postReq()` for HTTP requests).
- `src/test/java/org/fiware/odrl/mapping/LeftOperandMapperTest.java` (or new test) — Add parameterized tests for `json:payloadValue`, `json:subjectValue` left operand mappings.

**Acceptance criteria:**
- All new tests pass with `./mvnw test`.
- All existing tests continue to pass (no regressions).
- At least 2 end-to-end validation test cases for non-HTTP payloads (one allow, one deny).
- Test policies use JSON-LD contexts with `json:*` namespaced terms to trigger non-HTTP evaluation.
- Parameterized tests for the new mapping entries.
- Test coverage for error cases (missing payload, wrong input type, etc.).
- Verify that the `Pep` enum is not involved in non-HTTP evaluation — only the policy's LD-context drives routing.
