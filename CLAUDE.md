# ODRL PAP (Policy Administration Point)

## Overview
A Quarkus-based service that manages ODRL (Open Digital Rights Language) policies, translates them into Rego rules, and evaluates them via the Open Policy Agent (OPA). Currently focused on HTTP request evaluation from API gateways (APISIX, Kong).

## Tech Stack
- Language: Java 17
- Build: Maven 3 (wrapper: `./mvnw`)
- Framework: Quarkus 3.30.6 (Jakarta EE, REST, Panache ORM)
- Test: JUnit 5, Rest-Assured, TestContainers, Mockito, MockServer
- Database: PostgreSQL (production), H2 (tests)
- Code Generation: OpenAPI Generator (Quarkus plugin)
- JSON-LD: Titanium JSON-LD 1.7.0

## Project Structure
```
api/
  odrl.yaml              # Main OpenAPI spec (generates UiApi, model classes)
src/main/java/org/fiware/odrl/
  BundleResource.java    # OPA bundle endpoint (serves rego policies/methods/data)
  ValidationResource.java # /validate and /mappings endpoints (implements UiApi)
  Pep.java               # Enum: APISIX, KONG (selects rego utils helper)
  GeneralConfig.java     # Config interface (organization-did, pep, supported-sub-types)
  PathsConfiguration.java # External rego file paths config
  jsonld/
    JsonLdHandler.java   # JSON-LD expand+compact pipeline
    CompactionContext.java # Wraps the compaction context document
  mapping/
    OdrlMapper.java      # Core: maps ODRL policy JSON -> Rego code
    MappingConfiguration.java # HashMap<OdrlAttribute, NamespacedMap> loaded from mapping.json
    MappingResult.java    # Holds generated rego (imports, rules, uid)
    OdrlAttribute.java    # Enum: LEFT_OPERAND, RIGHT_OPERAND, OPERATOR, etc.
    NamespacedMap.java    # Map<String, Map<String, RegoMethod>>
    LeftOperandMapper.java, ConstraintMapper.java, OperatorMapper.java, RightOperandMapper.java
  ServiceResource.java  # Service + service-policy endpoints (implements ServiceApi)
  ApiResource.java       # Base class: createPolicyWithId(id, serviceId, policy)
  persistence/
    PolicyRepository.java # JPA repository for policies
    ServiceRepository.java # Service CRUD interface
    PersistentServiceRepository.java # JPA impl of ServiceRepository
    ServiceEntity.java    # JPA entity (serviceId, packageName, policies list)
    PolicyEntity.java     # Has optional ManyToOne link to ServiceEntity
  rego/
    RegoMethod.java       # Record(regoPackage, regoMethod, description)
    MappingResult.java    # Generated rego output container
    DataResponse.java     # OPA response record
src/main/resources/
  mapping.json            # Central config: maps ODRL concepts -> rego methods
  compaction-context.jsonld # JSON-LD compaction context (odrl, dome-op, xsd)
  rego-resources.txt      # Lists all rego files to load as classpath resources
  rego/
    utils/apisix.rego     # APISIX PEP helper (package utils.helper)
    utils/kong.rego       # Kong PEP helper (package utils.helper)
    odrl/                 # Core ODRL rego modules (action, operator, target, assignee, etc.)
    dome/                 # DOME marketplace rego modules
    ngsi-ld/              # NGSI-LD rego modules
    tmf/                  # TM Forum rego modules
    vc/                   # Verifiable Credential rego modules
    gaia-x/               # Gaia-X rego modules
    http/                 # HTTP-specific rego (path operator, body value extractor)
src/test/
  java/org/fiware/odrl/
    OdrlTest.java         # Base test class with utilities
    OdrlTestIT.java       # Integration tests (TestContainers + OPA)
    OdrlApiTest.java      # API-level tests
    jsonld/JsonLdHandlerTest.java
    mapping/              # Per-mapper unit tests
  resources/
    examples/             # Test policies organized by domain (dome/, odrl/, ngsi-ld/, gaia-x/)
    application.properties # Test config (pep=kong, H2 database)
```

## Build & Test
```bash
# Build (skip tests)
./mvnw clean package -DskipTests

# Run all tests
./mvnw test

# Run integration tests
./mvnw verify -Pit

# Run a single test class
./mvnw test -Dtest=OdrlApiTest

# Run with dev mode
./mvnw quarkus:dev
```

## Backend Service Endpoints (fully implemented)
The backend supports service-level policy management via `ServiceResource.java`:
- `POST /service` — Create a service (`ServiceCreate { id }` → `PolicyPath { policyPath }`)
- `GET /service` — List all services (paginated → `ServiceList`)
- `GET /service/{service-id}` — Get service detail (→ `Service { id, policyPath, policies }`)
- `DELETE /service/{service-id}` — Delete service + cascade-delete all policies
- `POST /service/{service-id}/policy` — Create policy under service
- `GET /service/{service-id}/policy` — List policies under service (paginated)
- `PUT /service/{service-id}/policy/{id}` — Create/update policy under service
- `GET /service/{service-id}/policy/{id}` — Get policy by ID under service
- `DELETE /service/{service-id}/policy/{id}` — Delete policy by ID under service
- `GET /service/{service-id}/policy/odrl/{id}` — Get by ODRL UID under service
- `DELETE /service/{service-id}/policy/odrl/{id}` — Delete by ODRL UID under service

Reserved service IDs: `policy`, `data`, `methods`. Reserved policy ID: `main`.

## Key Conventions
- OpenAPI-first: Models are generated from `api/odrl.yaml` via quarkus-openapi-generator
- PEP selection: `general.pep` config property selects which `rego/utils/*.rego` helper is loaded
- Rego helpers use `package utils.helper` and are referenced in mapping.json as `helper.*`
- mapping.json structure: `{ attribute: { namespace: { key: { regoPackage, regoMethod, description } } } }`
- JSON-LD pipeline: expand -> compact (using compaction-context.jsonld) before ODRL mapping
- Test examples: each has a `_NNNN.json` (ODRL policy) and `NNNN.rego` (expected rego output)
- Lombok `@Slf4j` for logging; `@ApplicationScoped` / `@RequestScoped` CDI scoping

## Important Files
- `api/odrl.yaml` - OpenAPI spec (source of truth for API models)
- `src/main/resources/mapping.json` - ODRL-to-Rego mapping configuration
- `src/main/resources/compaction-context.jsonld` - JSON-LD compaction context
- `src/main/resources/rego-resources.txt` - Registry of all rego files to bundle
- `src/main/java/org/fiware/odrl/Pep.java` - PEP type enum
- `src/main/java/org/fiware/odrl/BundleResource.java` - Rego bundle serving + PEP helper selection
- `src/main/java/org/fiware/odrl/ValidationResource.java` - Policy validation endpoint
- `src/main/java/org/fiware/odrl/mapping/OdrlMapper.java` - ODRL-to-Rego mapper
- `src/main/resources/rego/utils/apisix.rego` - APISIX helper (reference for new helpers)
- `src/main/resources/rego/http/leftOperand.rego` - Has `body_value()` with JSONPath-like walk

## Frontend (React SPA)
- **Stack:** React 19, TypeScript 5.8, Vite 7, Bootstrap 5, React-Bootstrap
- **Location:** `frontend/`
- **API Client:** Auto-generated from `api/odrl.yaml` via `openapi-typescript-codegen` (`npm run generate-api`)
- **Services:** `PapService` (policy CRUD), `UiService` (GET /mappings, POST /validate). Note: `ServiceService` does NOT exist yet — must be generated via `npm run generate-api`
- **Entry:** `frontend/src/main.tsx` → `App.tsx` (React Router v7)

### Frontend Structure
```
frontend/src/
  api/              # Auto-generated OpenAPI client (models, services, core)
  components/
    Baukasten.tsx       # Main visual policy builder (dropdowns from /mappings)
    ConstraintBuilder.tsx # AND/OR/XONE constraint editor with operand dropdowns
    TargetEditor.tsx    # Simple target or AssetCollection with refinements
    AssigneeEditor.tsx  # Simple assignee or PartyCollection with refinements
    ValidationEditor.tsx # HTTP test request builder (method, host, path, headers, body, JWT helper)
    PolicySummary.tsx   # Read-only policy display + raw JSON toggle
    Layout.tsx          # Navbar + route outlet
  pages/
    PolicyList.tsx      # Policy CRUD table
    PolicyEditor.tsx    # Tabs: visual builder ("Baukasten") / raw JSON editor + validation modal
  services/
    api.ts              # OpenAPI base URL + auth header config
  theme/
    theme.css           # CSS custom properties (primary: #0B2B40, secondary: #F07D00)
```

### Frontend Build & Dev
```bash
cd frontend
npm install
npm run dev          # Vite dev server (proxies /mappings, /policy, /validate to backend)
npm run build        # tsc -b && vite build → dist/
npm run generate-api # Regenerate OpenAPI client from ../api/odrl.yaml
npm run lint         # ESLint
```

### Frontend Environment
- `VITE_API_PROXY_TARGET` — Dev proxy target (default: http://localhost:8080)
- `VITE_API_BASE_URL` — Production API base URL (default: /api)

### Frontend Web Component (npm Package)
- **Package:** `@seamware/odrl-policy-editor` (scoped, public)
- **Build:** `npm run build:component` → `dist-component/odrl-policy-editor.js` (single self-contained ES module)
- **Config:** `frontend/vite.component.config.ts` (library build, inlines all CSS/deps)
- **Entry:** `frontend/src/web-component/index.ts` → registers `<odrl-policy-editor>` custom element
- **Shadow DOM:** Full style isolation; Bootstrap + theme CSS inlined
- **Attributes:** `api-base-url`, `auth-token`, `mode`, `policy-id`, `theme`, `locale`, `policy-context`
- **JS Properties:** `i18nStrings`, `themeConfig`, `template`, `policyContext`
- **Events:** `policy-created`, `policy-updated`, `policy-validated`, `editor-cancelled`

### Frontend Docker
- Multi-stage: Node 18 build → Nginx 1.21 serve (port 80)
- Nginx reverse-proxies API paths (`/policy`, `/mappings`, `/validate`) to `PAP_BACKEND_URL`. Note: `/service` is NOT proxied yet
- `PAP_BACKEND_URL` injected at container start via `envsubst` (no rebuild needed)
- Health check at `/healthz`

## CI/CD Workflows (`.github/workflows/`)
- `test.yaml` — Runs on every push: Java Maven tests + OPA rego tests
- `it.yaml` — Integration tests (currently partially disabled)
- `check.yml` — PR check: validates semver label (major/minor/patch)
- `pre-release.yaml` — On PR events: builds + pushes pre-release Docker images to quay.io
- `release.yaml` — On push to main: generates semver version, builds multi-arch Docker images, pushes to quay.io, creates GitHub release
- **Registry:** `quay.io/fiware/odrl-pap` (backend image)
- **Secrets used:** `QUAY_USERNAME`, `QUAY_PASSWORD`, `GITHUB_TOKEN`
- **Version generation:** `zwaldowski/semver-release-action` from PR labels
