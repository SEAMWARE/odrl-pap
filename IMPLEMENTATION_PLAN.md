# Implementation Plan: Support evaluation of non-HTTP requests

## Overview
Extend the ODRL PAP to support evaluation of policies against arbitrary JSON payloads, not just HTTP requests forwarded from API gateways. This involves creating a new generic rego helper for JSON value extraction, extending the validation API to accept arbitrary JSON input, adding new mappings for non-HTTP evaluation, and using JSON-LD context to route policies to the correct evaluation context (HTTP gateway vs. generic JSON).

## Steps

### Step 1: Create generic rego helper and extend Pep enum

**Goal:** Add a new `generic.rego` utils helper that extracts values from arbitrary JSON payloads instead of HTTP request structures, and wire it into the PEP selection infrastructure.

**Files to create:**
- `src/main/resources/rego/utils/generic.rego` — New helper file with `package utils.helper` that provides:
  - `request` — the full input request object
  - `payload` — the JSON payload (from `input.request.payload`)
  - `entity` — alias for the payload (for compatibility with existing mappings that use `helper.entity`)
  - `target` — extracted from a `target` field in the payload (or a configurable path)
  - `organization_did` — same as existing helpers (from `data.data.organizationDid`)
  - `get_value(path)` — generic value extraction using `walk()` with dot-notation paths (similar to `http.leftOperand.body_value` in `rego/http/leftOperand.rego`)
  - `subject` — extracted from payload (e.g., `payload.subject` or `payload.issuer`)

**Files to modify:**
- `src/main/java/org/fiware/odrl/Pep.java` — Add `GENERIC("generic")` enum value. This allows `general.pep=generic` in config.
- `src/main/resources/rego-resources.txt` — Add `rego/utils/generic.rego` entry alongside the existing `rego/utils/apisix.rego` and `rego/utils/kong.rego` entries (append to each cumulative block that lists utils files).
- `src/main/resources/application.properties` — Add a comment documenting the new `generic` PEP option.

**Acceptance criteria:**
- `generic.rego` compiles as valid Rego and uses `package utils.helper`.
- Setting `general.pep=generic` selects the generic helper and filters out apisix/kong helpers in `BundleResource.initMethods()`.
- The generic helper provides `get_value` for JSONPath-like extraction using the same `walk()` approach as `http.leftOperand.body_value`.
- Existing PEP values (`apisix`, `kong`) continue to work unchanged.

### Step 2: Extend OpenAPI spec and validation request model

**Goal:** Update the API to accept arbitrary JSON payloads for validation alongside the existing HTTP `TestRequest`.

**Files to modify:**
- `api/odrl.yaml` — Add new schemas and update the validation endpoint:
  - Add `GenericJsonInput` schema: an object with a required `payload` property (type: object, arbitrary JSON) and an optional `subject` property (type: object, for identity/credential info).
  - Update `ValidationRequest` to add a new optional `jsonInput` property of type `GenericJsonInput`, alongside the existing `testRequest` property. The two are mutually exclusive — a request provides one or the other.
  - Add an `inputType` property (enum: `"http"`, `"json"`) to `ValidationRequest` to explicitly indicate which input format is being used. Defaults to `"http"` for backward compatibility.
  - Update the `validatePolicy` operation description to document both input modes.

**Acceptance criteria:**
- The OpenAPI spec validates and generates updated model classes via `./mvnw compile`.
- `ValidationRequest` has both `testRequest` (existing) and `jsonInput` (new) properties.
- The `inputType` discriminator makes it unambiguous which input is active.
- Backward compatibility: requests using only `testRequest` continue to work without providing `inputType`.

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
- Rego methods in the new modules reference `helper.payload` and `helper.subject` from the generic utils helper (Step 1).
- Existing mappings under `odrl`, `dome-op`, `http`, etc. remain unchanged.
- The `/mappings` endpoint returns the new `json:*` entries alongside existing ones.

### Step 4: Update ValidationResource and add JSON-LD context routing

**Goal:** Modify the backend to handle both HTTP and generic JSON validation flows, and use JSON-LD context to route policies to the correct evaluation context.

**Files to modify:**
- `src/main/java/org/fiware/odrl/ValidationResource.java` — Update `validatePolicy()`:
  - Detect input type: check `inputType` field, or infer from presence of `jsonInput` vs. `testRequest`.
  - For HTTP input (existing): keep current behavior — wrap as `{"input": {"request": testRequest}}`.
  - For JSON input (new): wrap as `{"input": {"request": {"payload": jsonInput.payload, "subject": jsonInput.subject}}}` so the generic rego helper can access it via `input.request.payload`.
  - Extract and log the input type for debugging.

- `src/main/resources/compaction-context.jsonld` — Add a new namespace for the PAP evaluation context:
  - Add `"pap": { "@id": "https://odrl-pap.io/context#", "@prefix": true }` to enable policies to declare their evaluation context.

- `src/main/java/org/fiware/odrl/jsonld/JsonLdHandler.java` — Add a method `detectEvaluationContext(String compactedJson)` that:
  - Parses the compacted JSON and looks for a `pap:evaluationContext` field.
  - Returns an enum/string indicating `"http"` (default) or `"json"`.
  - This enables policies themselves to declare which evaluation context they need.

- `src/main/java/org/fiware/odrl/mapping/OdrlMapper.java` — If needed, pass context information to the mapper so that mapping resolution can be context-aware (e.g., prefer `json:*` mappings over `http:*` mappings when the policy declares a non-HTTP context). This may involve adding an optional `evaluationContext` parameter to `mapOdrl()` that influences how ambiguous mappings are resolved.

**Acceptance criteria:**
- Sending a `ValidationRequest` with `jsonInput` and `inputType: "json"` evaluates the policy against the JSON payload.
- Sending a `ValidationRequest` with `testRequest` (no `inputType` or `inputType: "http"`) continues to work exactly as before.
- The compaction context supports a `pap:evaluationContext` field in policies.
- `JsonLdHandler.detectEvaluationContext()` correctly extracts the evaluation context from a compacted policy.
- Error handling: if `inputType` is `"json"` but `jsonInput` is missing (or vice versa), return a clear error.

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
- Parameterized tests for the new mapping entries.
- Test coverage for error cases (missing payload, wrong input type, etc.).
