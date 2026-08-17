package org.fiware.odrl;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;
import org.fiware.odrl.persistence.RepositoryTestProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;

/**
 * HTTP-level tests for the general {@code /template} endpoints.
 *
 * <p>Unlike {@link TemplateApiTest}, which invokes the resources as plain Java
 * methods, these tests drive the endpoints over real HTTP via RestAssured so
 * that Jackson serialization of the generated {@code Template} /
 * {@code TemplatePlaceholder} models and the {@code {template-id}} path binding
 * are actually exercised. This is the layer that a missing
 * {@code @RegisterForReflection} registration would break (returning empty
 * {@code {}} objects), which method-level tests cannot catch.</p>
 */
@QuarkusTest
@TestProfile(RepositoryTestProfile.class)
public class TemplateHttpTest {

    /** A valid template creation payload with one placeholder. */
    private static final String VALID_TEMPLATE = """
            {
              "name": "HTTP Template",
              "description": "created via HTTP",
              "odrl": { "@type": "odrl:Policy", "odrl:target": "{{TARGET_ID}}" },
              "naturalLanguage": "Allow access to {{TARGET_ID}}",
              "placeholders": [
                { "key": "TARGET_ID", "name": "Target", "type": "string" }
              ]
            }
            """;

    @Test
    @DisplayName("Template round-trips over HTTP with fully serialized fields")
    void createListAndGet_serializeOverHttp() {
        // Create — the response body must carry the real fields, not an empty {}.
        String id = given()
                .contentType(ContentType.JSON)
                .body(VALID_TEMPLATE)
                .when()
                .post("/template")
                .then()
                .statusCode(201)
                .body("id", not(emptyOrNullString()))
                .body("name", equalTo("HTTP Template"))
                .body("placeholders[0].key", equalTo("TARGET_ID"))
                .body("placeholders[0].type", equalTo("string"))
                .extract()
                .path("id");

        // List — a missing reflection registration would serialize each row as
        // {} here; assert the created row comes back fully populated.
        given()
                .when()
                .get("/template")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1))
                .body("find { it.id == '" + id + "' }.name", equalTo("HTTP Template"));

        // Get by id — exercises the {template-id} path binding.
        given()
                .when()
                .get("/template/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo("HTTP Template"))
                .body("odrl.'@type'", equalTo("odrl:Policy"));

        // Clean up the row this test created.
        given()
                .when()
                .delete("/template/" + id)
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("Invalid template (missing required name) returns 400 over HTTP")
    void createInvalidTemplate_returns400() {
        String missingName = """
                {
                  "odrl": { "odrl:target": "{{X}}" },
                  "placeholders": [ { "key": "X", "name": "X", "type": "string" } ]
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(missingName)
                .when()
                .post("/template")
                .then()
                .statusCode(400);
    }
}
