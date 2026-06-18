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
  persistence/
    PolicyRepository.java # JPA repository for policies
    ServiceRepository.java
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
