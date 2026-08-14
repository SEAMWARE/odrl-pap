package org.fiware.odrl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.fiware.odrl.persistence.PolicyEntity;
import org.fiware.odrl.persistence.RepositoryTestProfile;
import org.fiware.odrl.persistence.ServiceEntity;
import org.fiware.odrl.persistence.ServiceRepository;
import org.fiware.odrl.persistence.TemplateEntity;
import org.fiware.odrl.persistence.TemplateRepository;
import org.openapi.quarkus.odrl_yaml.model.ServiceCreate;
import org.openapi.quarkus.odrl_yaml.model.Template;
import org.openapi.quarkus.odrl_yaml.model.TemplateCreate;
import org.openapi.quarkus.odrl_yaml.model.TemplatePlaceholder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive API-level tests for the template CRUD endpoints.
 *
 * <p>Tests cover both general (non-service-scoped) and service-scoped
 * template operations, including create, get, list (with pagination),
 * update, delete, validation error handling, 404 responses, service-scoped
 * isolation, and cascade deletion.</p>
 *
 * <p>Uses the Quarkus H2 test resource for an in-memory database with
 * {@code drop-and-create} schema generation.</p>
 */
@Slf4j
@QuarkusTest
@TestProfile(RepositoryTestProfile.class)
public class TemplateApiTest {

    /** Number of templates to create when testing pagination behaviour. */
    private static final int PAGINATION_TEMPLATE_COUNT = 7;

    /** Small page size used to verify pagination splits across pages. */
    private static final int SMALL_PAGE_SIZE = 3;

    @Inject
    TemplateResource templateResource;

    @Inject
    ServiceResource serviceResource;

    @Inject
    ServiceRepository serviceRepository;

    @Inject
    TemplateRepository templateRepository;

    @Inject
    ObjectMapper objectMapper;

    @BeforeEach
    @Transactional
    public void cleanBefore() {
        TemplateEntity.deleteAll();
        PolicyEntity.deleteAll();
        ServiceEntity.deleteAll();
    }

    @AfterEach
    @Transactional
    public void cleanAfter() {
        TemplateEntity.deleteAll();
        PolicyEntity.deleteAll();
        ServiceEntity.deleteAll();
    }

    // =========================================================================
    // General template CRUD tests
    // =========================================================================

    @Nested
    @DisplayName("General Template CRUD")
    class GeneralTemplateCrud {

        @Test
        @DisplayName("Create a general template returns 201 with generated ID")
        void createTemplate_returns201() {
            TemplateCreate create = buildSimpleAccessTemplate();
            Response response = templateResource.createTemplate(create);

            assertEquals(HttpStatus.SC_CREATED, response.getStatus(),
                    "Creating a valid template should return 201");

            Template template = response.readEntity(Template.class);
            assertNotNull(template.getId(), "Template should have a generated ID");
            assertEquals(create.getName(), template.getName(),
                    "Template name should match the creation payload");
            assertEquals(create.getDescription(), template.getDescription(),
                    "Template description should match the creation payload");
            assertNotNull(template.getOdrl(), "Template ODRL should be present");
            assertNotNull(template.getPlaceholders(), "Template placeholders should be present");
            assertEquals(create.getPlaceholders().size(), template.getPlaceholders().size(),
                    "Placeholder count should match");
        }

        @Test
        @DisplayName("Get template by ID returns 200 with correct data")
        void getTemplateById_returns200() {
            Template created = createAndAssertTemplate(buildSimpleAccessTemplate());

            Response getResponse = templateResource.getTemplateById(created.getId());
            assertEquals(HttpStatus.SC_OK, getResponse.getStatus(),
                    "Getting an existing template should return 200");

            Template retrieved = getResponse.readEntity(Template.class);
            assertEquals(created.getId(), retrieved.getId(),
                    "Retrieved template ID should match");
            assertEquals(created.getName(), retrieved.getName(),
                    "Retrieved template name should match");
            assertEquals(created.getNaturalLanguage(), retrieved.getNaturalLanguage(),
                    "Retrieved natural language should match");
        }

        @Test
        @DisplayName("Get non-existent template returns 404")
        void getTemplateById_notFound_returns404() {
            Response response = templateResource.getTemplateById("nonexistent");
            assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus(),
                    "Getting a non-existent template should return 404");
        }

        @Test
        @DisplayName("List templates returns empty list when none exist")
        void getTemplates_emptyList() {
            Response response = templateResource.getTemplates(0, 25);
            assertEquals(HttpStatus.SC_OK, response.getStatus(),
                    "Listing templates should return 200");

            List<Template> templates = readTemplateList(response);
            assertTrue(templates.isEmpty(),
                    "Template list should be empty when no templates exist");
        }

        @Test
        @DisplayName("List templates returns all created templates")
        void getTemplates_returnsAll() {
            createAndAssertTemplate(buildSimpleAccessTemplate());
            createAndAssertTemplate(buildDateConstrainedTemplate());

            Response response = templateResource.getTemplates(0, 25);
            assertEquals(HttpStatus.SC_OK, response.getStatus());

            List<Template> templates = readTemplateList(response);
            assertEquals(2, templates.size(),
                    "Should return both created templates");
        }

        @Test
        @DisplayName("Update template returns 200 with updated data")
        void updateTemplate_returns200() {
            Template created = createAndAssertTemplate(buildSimpleAccessTemplate());

            TemplateCreate updatePayload = buildSimpleAccessTemplate();
            String updatedName = "Updated Template Name";
            updatePayload.setName(updatedName);

            Response response = templateResource.updateTemplate(created.getId(), updatePayload);
            assertEquals(HttpStatus.SC_OK, response.getStatus(),
                    "Updating an existing template should return 200");

            Template updated = response.readEntity(Template.class);
            assertEquals(updatedName, updated.getName(),
                    "Updated name should be reflected");
            assertEquals(created.getId(), updated.getId(),
                    "Template ID should not change on update");
        }

        @Test
        @DisplayName("Update non-existent template returns 404")
        void updateTemplate_notFound_returns404() {
            TemplateCreate updatePayload = buildSimpleAccessTemplate();
            Response response = templateResource.updateTemplate("nonexistent", updatePayload);
            assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus(),
                    "Updating a non-existent template should return 404");
        }

        @Test
        @DisplayName("Delete template returns 204")
        void deleteTemplate_returns204() {
            Template created = createAndAssertTemplate(buildSimpleAccessTemplate());

            Response response = templateResource.deleteTemplateById(created.getId());
            assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatus(),
                    "Deleting a template should return 204");

            // Verify the template is gone
            Response getResponse = templateResource.getTemplateById(created.getId());
            assertEquals(HttpStatus.SC_NOT_FOUND, getResponse.getStatus(),
                    "Deleted template should no longer be retrievable");
        }

        @Test
        @DisplayName("Delete non-existent template returns 204 (no-op)")
        void deleteTemplate_nonExistent_returns204() {
            Response response = templateResource.deleteTemplateById("nonexistent");
            assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatus(),
                    "Deleting a non-existent template should still return 204");
        }

        @Test
        @DisplayName("Created template preserves ODRL with placeholder tokens")
        void createTemplate_preservesPlaceholders() {
            TemplateCreate create = buildSimpleAccessTemplate();
            Template created = createAndAssertTemplate(create);

            Response getResponse = templateResource.getTemplateById(created.getId());
            Template retrieved = getResponse.readEntity(Template.class);

            // Verify ODRL contains the placeholder tokens
            String odrlJson = serialiseToJson(retrieved.getOdrl());
            assertTrue(odrlJson.contains("{{RESOURCE_ID}}"),
                    "ODRL should contain RESOURCE_ID placeholder token");
            assertTrue(odrlJson.contains("{{ASSIGNEE}}"),
                    "ODRL should contain ASSIGNEE placeholder token");
        }

        @Test
        @DisplayName("Created template preserves placeholder options")
        void createTemplate_preservesOptions() {
            TemplateCreate create = buildSimpleAccessTemplate();
            Template created = createAndAssertTemplate(create);

            Response getResponse = templateResource.getTemplateById(created.getId());
            Template retrieved = getResponse.readEntity(Template.class);

            TemplatePlaceholder assigneePlaceholder = retrieved.getPlaceholders().stream()
                    .filter(p -> "ASSIGNEE".equals(p.getKey()))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("ASSIGNEE placeholder not found"));

            assertNotNull(assigneePlaceholder.getOptions(),
                    "ASSIGNEE placeholder should have options");
            assertEquals(3, assigneePlaceholder.getOptions().size(),
                    "ASSIGNEE placeholder should have 3 options");
            assertTrue(assigneePlaceholder.getOptions().containsAll(List.of("admin", "user", "viewer")),
                    "ASSIGNEE options should contain all expected values");
        }
    }

    // =========================================================================
    // Pagination tests
    // =========================================================================

    @Nested
    @DisplayName("Pagination")
    class PaginationTests {

        @Test
        @DisplayName("Pagination returns correct page sizes for general templates")
        void paginateGeneralTemplates() {
            // Create multiple templates
            for (int i = 0; i < PAGINATION_TEMPLATE_COUNT; i++) {
                TemplateCreate tc = buildSimpleAccessTemplate();
                tc.setName("Template " + i);
                createAndAssertTemplate(tc);
            }

            // First page
            Response firstPage = templateResource.getTemplates(0, SMALL_PAGE_SIZE);
            List<Template> page0 = readTemplateList(firstPage);
            assertEquals(SMALL_PAGE_SIZE, page0.size(),
                    "First page should have exactly " + SMALL_PAGE_SIZE + " items");

            // Second page
            Response secondPage = templateResource.getTemplates(1, SMALL_PAGE_SIZE);
            List<Template> page1 = readTemplateList(secondPage);
            assertEquals(SMALL_PAGE_SIZE, page1.size(),
                    "Second page should have exactly " + SMALL_PAGE_SIZE + " items");

            // Third page (partial — only 1 remaining)
            int expectedLastPageSize = PAGINATION_TEMPLATE_COUNT - (2 * SMALL_PAGE_SIZE);
            Response thirdPage = templateResource.getTemplates(2, SMALL_PAGE_SIZE);
            List<Template> page2 = readTemplateList(thirdPage);
            assertEquals(expectedLastPageSize, page2.size(),
                    "Third page should have " + expectedLastPageSize + " item(s)");

            // Beyond last page
            Response emptyPage = templateResource.getTemplates(3, SMALL_PAGE_SIZE);
            List<Template> page3 = readTemplateList(emptyPage);
            assertTrue(page3.isEmpty(),
                    "Page beyond available data should be empty");
        }

        @Test
        @DisplayName("Pagination returns correct page sizes for service-scoped templates")
        void paginateServiceTemplates() {
            String serviceId = "paginate-svc";
            serviceRepository.createService(serviceId);

            for (int i = 0; i < PAGINATION_TEMPLATE_COUNT; i++) {
                TemplateCreate tc = buildSimpleAccessTemplate();
                tc.setName("Service Template " + i);
                Response r = serviceResource.createServiceTemplate(serviceId, tc);
                assertEquals(HttpStatus.SC_CREATED, r.getStatus());
            }

            // First page
            Response firstPage = serviceResource.getServiceTemplates(serviceId, 0, SMALL_PAGE_SIZE);
            List<Template> page0 = readTemplateList(firstPage);
            assertEquals(SMALL_PAGE_SIZE, page0.size(),
                    "First page should have exactly " + SMALL_PAGE_SIZE + " items");

            // Second page
            Response secondPage = serviceResource.getServiceTemplates(serviceId, 1, SMALL_PAGE_SIZE);
            List<Template> page1 = readTemplateList(secondPage);
            assertEquals(SMALL_PAGE_SIZE, page1.size(),
                    "Second page should have exactly " + SMALL_PAGE_SIZE + " items");

            // Third page (partial)
            int expectedLastPageSize = PAGINATION_TEMPLATE_COUNT - (2 * SMALL_PAGE_SIZE);
            Response thirdPage = serviceResource.getServiceTemplates(serviceId, 2, SMALL_PAGE_SIZE);
            List<Template> page2 = readTemplateList(thirdPage);
            assertEquals(expectedLastPageSize, page2.size(),
                    "Third page should have " + expectedLastPageSize + " item(s)");
        }

        @Test
        @DisplayName("Default pagination uses page 0 and page size 25 when nulls are passed")
        void defaultPagination() {
            createAndAssertTemplate(buildSimpleAccessTemplate());

            Response response = templateResource.getTemplates(null, null);
            assertEquals(HttpStatus.SC_OK, response.getStatus());

            List<Template> templates = readTemplateList(response);
            assertEquals(1, templates.size(),
                    "Default pagination should return the single created template");
        }
    }

    // =========================================================================
    // Service-scoped template CRUD tests
    // =========================================================================

    @Nested
    @DisplayName("Service-Scoped Template CRUD")
    class ServiceScopedTemplateCrud {

        @Test
        @DisplayName("Create a service-scoped template returns 201")
        void createServiceTemplate_returns201() {
            String serviceId = "test-service";
            serviceRepository.createService(serviceId);

            TemplateCreate create = buildSimpleAccessTemplate();
            Response response = serviceResource.createServiceTemplate(serviceId, create);

            assertEquals(HttpStatus.SC_CREATED, response.getStatus(),
                    "Creating a service-scoped template should return 201");

            Template template = response.readEntity(Template.class);
            assertNotNull(template.getId(), "Template should have a generated ID");
            assertEquals(create.getName(), template.getName());
        }

        @Test
        @DisplayName("Create service template for non-existent service returns 404")
        void createServiceTemplate_serviceNotFound_returns404() {
            TemplateCreate create = buildSimpleAccessTemplate();
            Response response = serviceResource.createServiceTemplate("nonexistent-svc", create);

            assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus(),
                    "Creating a template for a non-existent service should return 404");
        }

        @Test
        @DisplayName("Get service template by ID returns 200")
        void getServiceTemplateById_returns200() {
            String serviceId = "svc-get-test";
            serviceRepository.createService(serviceId);

            Template created = createServiceTemplateAndAssert(serviceId, buildSimpleAccessTemplate());

            Response response = serviceResource.getServiceTemplateById(serviceId, created.getId());
            assertEquals(HttpStatus.SC_OK, response.getStatus(),
                    "Getting a service-scoped template should return 200");

            Template retrieved = response.readEntity(Template.class);
            assertEquals(created.getId(), retrieved.getId());
        }

        @Test
        @DisplayName("Get service template for non-existent service returns 404")
        void getServiceTemplateById_serviceNotFound_returns404() {
            Response response = serviceResource.getServiceTemplateById("nonexistent-svc", "any-id");
            assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus());
        }

        @Test
        @DisplayName("Get non-existent template under existing service returns 404")
        void getServiceTemplateById_templateNotFound_returns404() {
            String serviceId = "svc-no-template";
            serviceRepository.createService(serviceId);

            Response response = serviceResource.getServiceTemplateById(serviceId, "nonexistent-template");
            assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus());
        }

        @Test
        @DisplayName("List service templates returns only templates for that service")
        void getServiceTemplates_returnsCorrectTemplates() {
            String serviceId = "svc-list-test";
            serviceRepository.createService(serviceId);

            createServiceTemplateAndAssert(serviceId, buildSimpleAccessTemplate());
            createServiceTemplateAndAssert(serviceId, buildDateConstrainedTemplate());

            Response response = serviceResource.getServiceTemplates(serviceId, 0, 25);
            assertEquals(HttpStatus.SC_OK, response.getStatus());

            List<Template> templates = readTemplateList(response);
            assertEquals(2, templates.size(),
                    "Should return exactly the 2 templates created for this service");
        }

        @Test
        @DisplayName("List service templates for non-existent service returns 404")
        void getServiceTemplates_serviceNotFound_returns404() {
            Response response = serviceResource.getServiceTemplates("nonexistent-svc", 0, 25);
            assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus());
        }

        @Test
        @DisplayName("Update service template returns 200 with updated data")
        void updateServiceTemplate_returns200() {
            String serviceId = "svc-update-test";
            serviceRepository.createService(serviceId);

            Template created = createServiceTemplateAndAssert(serviceId, buildSimpleAccessTemplate());

            TemplateCreate updatePayload = buildSimpleAccessTemplate();
            String updatedName = "Updated Service Template";
            updatePayload.setName(updatedName);

            Response response = serviceResource.updateServiceTemplate(
                    serviceId, created.getId(), updatePayload);
            assertEquals(HttpStatus.SC_OK, response.getStatus());

            Template updated = response.readEntity(Template.class);
            assertEquals(updatedName, updated.getName());
            assertEquals(created.getId(), updated.getId());
        }

        @Test
        @DisplayName("Update service template for non-existent service returns 404")
        void updateServiceTemplate_serviceNotFound_returns404() {
            TemplateCreate updatePayload = buildSimpleAccessTemplate();
            Response response = serviceResource.updateServiceTemplate(
                    "nonexistent-svc", "any-id", updatePayload);
            assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus());
        }

        @Test
        @DisplayName("Update non-existent template under existing service returns 404")
        void updateServiceTemplate_templateNotFound_returns404() {
            String serviceId = "svc-update-missing";
            serviceRepository.createService(serviceId);

            TemplateCreate updatePayload = buildSimpleAccessTemplate();
            Response response = serviceResource.updateServiceTemplate(
                    serviceId, "nonexistent-template", updatePayload);
            assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus());
        }

        @Test
        @DisplayName("Delete service template returns 204")
        void deleteServiceTemplate_returns204() {
            String serviceId = "svc-delete-test";
            serviceRepository.createService(serviceId);

            Template created = createServiceTemplateAndAssert(serviceId, buildSimpleAccessTemplate());

            Response response = serviceResource.deleteServiceTemplateById(serviceId, created.getId());
            assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatus());

            // Verify the template is gone
            Response getResponse = serviceResource.getServiceTemplateById(serviceId, created.getId());
            assertEquals(HttpStatus.SC_NOT_FOUND, getResponse.getStatus(),
                    "Deleted template should no longer be retrievable");
        }

        @Test
        @DisplayName("Delete service template for non-existent service returns 404")
        void deleteServiceTemplate_serviceNotFound_returns404() {
            Response response = serviceResource.deleteServiceTemplateById("nonexistent-svc", "any-id");
            assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus());
        }
    }

    // =========================================================================
    // Service-scoped isolation tests
    // =========================================================================

    @Nested
    @DisplayName("Service-Scoped Isolation")
    class ServiceScopeIsolation {

        @Test
        @DisplayName("Templates from one service are not visible under another service")
        void templatesAreIsolatedBetweenServices() {
            String serviceA = "service-a";
            String serviceB = "service-b";
            serviceRepository.createService(serviceA);
            serviceRepository.createService(serviceB);

            // Create templates under service A
            TemplateCreate tc = buildSimpleAccessTemplate();
            tc.setName("Service A Template");
            createServiceTemplateAndAssert(serviceA, tc);

            // Create templates under service B
            TemplateCreate tc2 = buildDateConstrainedTemplate();
            tc2.setName("Service B Template");
            createServiceTemplateAndAssert(serviceB, tc2);

            // List under service A — should see only 1
            Response responseA = serviceResource.getServiceTemplates(serviceA, 0, 25);
            List<Template> templatesA = readTemplateList(responseA);
            assertEquals(1, templatesA.size(),
                    "Service A should have exactly 1 template");
            assertEquals("Service A Template", templatesA.get(0).getName());

            // List under service B — should see only 1
            Response responseB = serviceResource.getServiceTemplates(serviceB, 0, 25);
            List<Template> templatesB = readTemplateList(responseB);
            assertEquals(1, templatesB.size(),
                    "Service B should have exactly 1 template");
            assertEquals("Service B Template", templatesB.get(0).getName());
        }

        @Test
        @DisplayName("Service-scoped templates are not visible in general template list")
        void serviceScopedTemplatesNotInGeneralList() {
            String serviceId = "scoped-svc";
            serviceRepository.createService(serviceId);

            // Create a general template
            TemplateCreate generalTc = buildSimpleAccessTemplate();
            generalTc.setName("General Template");
            createAndAssertTemplate(generalTc);

            // Create a service-scoped template
            TemplateCreate serviceTc = buildDateConstrainedTemplate();
            serviceTc.setName("Scoped Template");
            createServiceTemplateAndAssert(serviceId, serviceTc);

            // List general templates — should see only the general one
            Response response = templateResource.getTemplates(0, 25);
            List<Template> generalTemplates = readTemplateList(response);
            assertEquals(1, generalTemplates.size(),
                    "General list should have exactly 1 template");
            assertEquals("General Template", generalTemplates.get(0).getName(),
                    "General list should only contain the general template");
        }

        @Test
        @DisplayName("General templates are not visible under a service")
        void generalTemplatesNotInServiceList() {
            String serviceId = "isolated-svc";
            serviceRepository.createService(serviceId);

            // Create a general template
            createAndAssertTemplate(buildSimpleAccessTemplate());

            // List under the service — should be empty
            Response response = serviceResource.getServiceTemplates(serviceId, 0, 25);
            List<Template> serviceTemplates = readTemplateList(response);
            assertTrue(serviceTemplates.isEmpty(),
                    "Service should not see general templates");
        }

        @Test
        @DisplayName("Getting a template through another service returns 404")
        void getServiceTemplateById_crossService_returns404() {
            serviceRepository.createService("service-a");
            serviceRepository.createService("service-b");
            Template ownedByA = createServiceTemplateAndAssert("service-a", buildSimpleAccessTemplate());

            Response response = serviceResource.getServiceTemplateById("service-b", ownedByA.getId());
            assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus(),
                    "A template owned by service-a must not be readable through service-b");
        }

        @Test
        @DisplayName("Updating a template through another service returns 404 and does not overwrite it")
        void updateServiceTemplate_crossService_returns404_andDoesNotOverwrite() {
            serviceRepository.createService("service-a");
            serviceRepository.createService("service-b");
            TemplateCreate original = buildSimpleAccessTemplate();
            original.setName("Owned by A");
            Template ownedByA = createServiceTemplateAndAssert("service-a", original);

            TemplateCreate overwrite = buildDateConstrainedTemplate();
            overwrite.setName("Hijacked by B");
            Response response = serviceResource.updateServiceTemplate("service-b", ownedByA.getId(), overwrite);
            assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus(),
                    "A template owned by service-a must not be updatable through service-b");

            // The original template must be untouched.
            Template stillA = serviceResource.getServiceTemplateById("service-a", ownedByA.getId())
                    .readEntity(Template.class);
            assertEquals("Owned by A", stillA.getName(),
                    "Cross-service update must not overwrite the template");
        }

        @Test
        @DisplayName("Deleting a template through another service does not delete it")
        void deleteServiceTemplateById_crossService_doesNotDelete() {
            serviceRepository.createService("service-a");
            serviceRepository.createService("service-b");
            Template ownedByA = createServiceTemplateAndAssert("service-a", buildSimpleAccessTemplate());

            serviceResource.deleteServiceTemplateById("service-b", ownedByA.getId());

            assertEquals(HttpStatus.SC_OK,
                    serviceResource.getServiceTemplateById("service-a", ownedByA.getId()).getStatus(),
                    "A template owned by service-a must survive a delete issued under service-b");
        }

        @Test
        @DisplayName("General endpoints cannot read a service-scoped template")
        void getTemplateById_serviceScoped_returns404() {
            serviceRepository.createService("scoped-svc");
            Template scoped = createServiceTemplateAndAssert("scoped-svc", buildSimpleAccessTemplate());

            Response response = templateResource.getTemplateById(scoped.getId());
            assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus(),
                    "GET /template/{id} must not return a service-scoped template");
        }

        @Test
        @DisplayName("General endpoints cannot update a service-scoped template")
        void updateTemplate_serviceScoped_returns404() {
            serviceRepository.createService("scoped-svc");
            Template scoped = createServiceTemplateAndAssert("scoped-svc", buildSimpleAccessTemplate());

            Response response = templateResource.updateTemplate(scoped.getId(), buildDateConstrainedTemplate());
            assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus(),
                    "PUT /template/{id} must not update a service-scoped template");
        }

        @Test
        @DisplayName("General endpoints cannot delete a service-scoped template")
        void deleteTemplateById_serviceScoped_doesNotDelete() {
            serviceRepository.createService("scoped-svc");
            Template scoped = createServiceTemplateAndAssert("scoped-svc", buildSimpleAccessTemplate());

            templateResource.deleteTemplateById(scoped.getId());

            assertEquals(HttpStatus.SC_OK,
                    serviceResource.getServiceTemplateById("scoped-svc", scoped.getId()).getStatus(),
                    "DELETE /template/{id} must not delete a service-scoped template");
        }
    }

    // =========================================================================
    // Cascade deletion tests
    // =========================================================================

    @Nested
    @DisplayName("Cascade Deletion")
    class CascadeDeletion {

        @Test
        @DisplayName("Deleting a service cascades to its templates")
        void deleteService_cascadesToTemplates() {
            String serviceId = "cascade-svc";
            serviceRepository.createService(serviceId);

            Template created = createServiceTemplateAndAssert(serviceId, buildSimpleAccessTemplate());
            String templateId = created.getId();

            // Delete the service
            serviceResource.deleteService(serviceId);

            // Verify the template row is truly gone. A scope-agnostic repository
            // lookup is used here rather than the general GET endpoint, which now
            // (correctly) 404s for any service-scoped id regardless of cascade.
            assertTrue(templateRepository.getTemplate(templateId).isEmpty(),
                    "Template should be cascade-deleted when its service is deleted");
        }

        @Test
        @DisplayName("Deleting a service with multiple templates cascades all of them")
        void deleteService_cascadesMultipleTemplates() {
            String serviceId = "cascade-multi-svc";
            serviceRepository.createService(serviceId);

            Template t1 = createServiceTemplateAndAssert(serviceId, buildSimpleAccessTemplate());
            Template t2 = createServiceTemplateAndAssert(serviceId, buildDateConstrainedTemplate());

            // Delete the service
            serviceResource.deleteService(serviceId);

            // Both template rows should be truly gone (scope-agnostic lookup).
            assertTrue(templateRepository.getTemplate(t1.getId()).isEmpty(),
                    "First template should be cascade-deleted");
            assertTrue(templateRepository.getTemplate(t2.getId()).isEmpty(),
                    "Second template should be cascade-deleted");
        }
    }

    // =========================================================================
    // Validation tests
    // =========================================================================

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("Creating a template with duplicate placeholder keys returns 400")
        void createTemplate_duplicateKeys_returns400() {
            TemplateCreate tc = new TemplateCreate();
            tc.setName("Duplicate Key Template");
            tc.setOdrl(Map.of("odrl:target", "{{RESOURCE_ID}}"));
            tc.setNaturalLanguage("Access {{RESOURCE_ID}}");
            tc.setPlaceholders(List.of(
                    createPlaceholder("RESOURCE_ID", "Resource 1", "string"),
                    createPlaceholder("RESOURCE_ID", "Resource 2", "string")
            ));

            Response response = templateResource.createTemplate(tc);
            assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatus(),
                    "Duplicate placeholder keys should be rejected with 400");

            String errorBody = response.readEntity(String.class);
            assertTrue(errorBody.contains("Duplicate placeholder key"),
                    "Error message should mention duplicate key");
        }

        @Test
        @DisplayName("Creating a template with orphaned placeholder key returns 400")
        void createTemplate_orphanedKey_returns400() {
            TemplateCreate tc = new TemplateCreate();
            tc.setName("Orphaned Key Template");
            tc.setOdrl(Map.of("odrl:target", "some-value"));
            tc.setNaturalLanguage("No references");
            tc.setPlaceholders(List.of(
                    createPlaceholder("MISSING_REF", "Missing", "string")
            ));

            Response response = templateResource.createTemplate(tc);
            assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatus(),
                    "Orphaned placeholder key should be rejected with 400");

            String errorBody = response.readEntity(String.class);
            assertTrue(errorBody.contains("not referenced"),
                    "Error message should mention unreferenced key");
        }

        @Test
        @DisplayName("Creating a template with empty placeholders returns 400")
        void createTemplate_emptyPlaceholders_returns400() {
            TemplateCreate tc = new TemplateCreate();
            tc.setName("Empty Placeholders");
            tc.setOdrl(Map.of("odrl:target", "value"));
            tc.setPlaceholders(List.of());

            Response response = templateResource.createTemplate(tc);
            assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatus(),
                    "Empty placeholder list should be rejected with 400");
        }

        @Test
        @DisplayName("Creating a template with null placeholders returns 400")
        void createTemplate_nullPlaceholders_returns400() {
            TemplateCreate tc = new TemplateCreate();
            tc.setName("Null Placeholders");
            tc.setOdrl(Map.of("odrl:target", "value"));
            tc.setPlaceholders(null);

            Response response = templateResource.createTemplate(tc);
            assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatus(),
                    "Null placeholder list should be rejected with 400");
        }

        @Test
        @DisplayName("Creating a template with blank placeholder key returns 400")
        void createTemplate_blankKey_returns400() {
            TemplateCreate tc = new TemplateCreate();
            tc.setName("Blank Key Template");
            tc.setOdrl(Map.of("odrl:target", "value"));
            tc.setPlaceholders(List.of(
                    createPlaceholder("", "Blank Key", "string")
            ));

            Response response = templateResource.createTemplate(tc);
            assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatus(),
                    "Blank placeholder key should be rejected with 400");
        }

        @Test
        @DisplayName("Updating a template with invalid payload returns 400")
        void updateTemplate_invalidPayload_returns400() {
            Template created = createAndAssertTemplate(buildSimpleAccessTemplate());

            // Update with invalid payload (orphaned key)
            TemplateCreate invalidUpdate = new TemplateCreate();
            invalidUpdate.setName("Invalid Update");
            invalidUpdate.setOdrl(Map.of("odrl:target", "no-placeholders"));
            invalidUpdate.setPlaceholders(List.of(
                    createPlaceholder("ORPHANED", "Orphan", "string")
            ));

            Response response = templateResource.updateTemplate(created.getId(), invalidUpdate);
            assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatus(),
                    "Updating with an orphaned key should be rejected with 400");
        }

        @Test
        @DisplayName("Creating a service template with invalid payload returns 400")
        void createServiceTemplate_invalidPayload_returns400() {
            String serviceId = "validation-svc";
            serviceRepository.createService(serviceId);

            TemplateCreate tc = new TemplateCreate();
            tc.setName("Invalid Service Template");
            tc.setOdrl(Map.of("odrl:target", "value"));
            tc.setPlaceholders(List.of());

            Response response = serviceResource.createServiceTemplate(serviceId, tc);
            assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatus(),
                    "Empty placeholders should be rejected with 400");
        }

        @Test
        @DisplayName("Updating a service template with invalid payload returns 400")
        void updateServiceTemplate_invalidPayload_returns400() {
            String serviceId = "svc-val-update";
            serviceRepository.createService(serviceId);

            Template created = createServiceTemplateAndAssert(serviceId, buildSimpleAccessTemplate());

            TemplateCreate invalidUpdate = new TemplateCreate();
            invalidUpdate.setName("Invalid");
            invalidUpdate.setOdrl(Map.of("odrl:target", "no-ref"));
            invalidUpdate.setPlaceholders(List.of(
                    createPlaceholder("ORPHAN", "Orphan", "string")
            ));

            Response response = serviceResource.updateServiceTemplate(
                    serviceId, created.getId(), invalidUpdate);
            assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatus(),
                    "Invalid update payload should be rejected with 400");
        }

        @DisplayName("Valid placeholder types should be accepted")
        @ParameterizedTest
        @ValueSource(strings = {"string", "number", "boolean", "xsd:date"})
        void createTemplate_validTypes_accepted(String type) {
            TemplateCreate tc = new TemplateCreate();
            tc.setName("Type Test: " + type);
            tc.setOdrl(Map.of("odrl:target", "{{VALUE}}"));
            tc.setNaturalLanguage("Test {{VALUE}}");
            tc.setPlaceholders(List.of(
                    createPlaceholder("VALUE", "Test Value", type)
            ));

            Response response = templateResource.createTemplate(tc);
            assertEquals(HttpStatus.SC_CREATED, response.getStatus(),
                    "Placeholder type '" + type + "' should be accepted");

            Template template = response.readEntity(Template.class);
            assertEquals(type, template.getPlaceholders().get(0).getType().toString(),
                    "Placeholder type should be preserved");
        }
    }

    // =========================================================================
    // Template from JSON fixture files
    // =========================================================================

    @Nested
    @DisplayName("JSON Fixture Files")
    class JsonFixtureTests {

        @Test
        @DisplayName("Simple access template JSON fixture can be created and retrieved")
        void simpleAccessFixture() throws IOException {
            TemplateCreate tc = loadTemplateFixture("/examples/templates/simple_access_template.json");

            Response response = templateResource.createTemplate(tc);
            assertEquals(HttpStatus.SC_CREATED, response.getStatus(),
                    "Simple access template fixture should be valid");

            Template created = response.readEntity(Template.class);
            assertEquals("Simple Access Policy", created.getName());
            assertEquals(2, created.getPlaceholders().size());
        }

        @Test
        @DisplayName("Date-constrained template JSON fixture can be created and retrieved")
        void dateConstrainedFixture() throws IOException {
            TemplateCreate tc = loadTemplateFixture("/examples/templates/date_constrained_template.json");

            Response response = templateResource.createTemplate(tc);
            assertEquals(HttpStatus.SC_CREATED, response.getStatus(),
                    "Date-constrained template fixture should be valid");

            Template created = response.readEntity(Template.class);
            assertEquals("Date-Constrained Access Policy", created.getName());
            assertEquals(2, created.getPlaceholders().size());

            // Verify the xsd:date type placeholder
            TemplatePlaceholder datePlaceholder = created.getPlaceholders().stream()
                    .filter(p -> "EXPIRY_DATE".equals(p.getKey()))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("EXPIRY_DATE placeholder not found"));
            assertEquals(TemplatePlaceholder.TypeEnum.XSD_COLON_DATE, datePlaceholder.getType(),
                    "Expiry date placeholder should have xsd:date type");
        }

        @Test
        @DisplayName("Role-based template JSON fixture can be created and retrieved")
        void roleBasedFixture() throws IOException {
            TemplateCreate tc = loadTemplateFixture("/examples/templates/role_based_template.json");

            Response response = templateResource.createTemplate(tc);
            assertEquals(HttpStatus.SC_CREATED, response.getStatus(),
                    "Role-based template fixture should be valid");

            Template created = response.readEntity(Template.class);
            assertEquals("Role-Based Access Policy", created.getName());
            assertEquals(3, created.getPlaceholders().size());
        }
    }

    // =========================================================================
    // Helper methods
    // =========================================================================

    /**
     * Creates a general template via the resource and asserts it was created successfully.
     *
     * @param create the template creation payload
     * @return the created template with its generated ID
     */
    private Template createAndAssertTemplate(TemplateCreate create) {
        Response response = templateResource.createTemplate(create);
        assertEquals(HttpStatus.SC_CREATED, response.getStatus(),
                "Template creation should succeed");
        Template template = response.readEntity(Template.class);
        assertNotNull(template.getId(), "Template should have a generated ID");
        return template;
    }

    /**
     * Creates a service-scoped template and asserts it was created successfully.
     *
     * @param serviceId the service to scope the template to
     * @param create    the template creation payload
     * @return the created template with its generated ID
     */
    private Template createServiceTemplateAndAssert(String serviceId, TemplateCreate create) {
        Response response = serviceResource.createServiceTemplate(serviceId, create);
        assertEquals(HttpStatus.SC_CREATED, response.getStatus(),
                "Service template creation should succeed");
        Template template = response.readEntity(Template.class);
        assertNotNull(template.getId(), "Template should have a generated ID");
        return template;
    }

    /**
     * Reads a template list from a JAX-RS response, handling both direct list
     * and entity-based deserialization.
     *
     * @param response the HTTP response
     * @return the deserialized list of templates
     */
    @SuppressWarnings("unchecked")
    private List<Template> readTemplateList(Response response) {
        Object entity = response.getEntity();
        if (entity instanceof List<?>) {
            return (List<Template>) entity;
        }
        // Fallback: try reading via entity deserialization
        return objectMapper.convertValue(entity,
                new TypeReference<List<Template>>() {});
    }

    /**
     * Builds a simple access template with two placeholders (RESOURCE_ID and ASSIGNEE).
     *
     * @return a valid TemplateCreate payload
     */
    private static TemplateCreate buildSimpleAccessTemplate() {
        TemplateCreate tc = new TemplateCreate();
        tc.setName("Simple Access Policy");
        tc.setDescription("A template for granting read access to a specific resource");
        tc.setOdrl(Map.of(
                "@context", "http://www.w3.org/ns/odrl.jsonld",
                "@type", "odrl:Set",
                "odrl:permission", Map.of(
                        "odrl:target", "{{RESOURCE_ID}}",
                        "odrl:action", "odrl:read",
                        "odrl:assignee", "{{ASSIGNEE}}"
                )
        ));
        tc.setNaturalLanguage("Allow {{ASSIGNEE}} to read {{RESOURCE_ID}}");

        TemplatePlaceholder resourceId = createPlaceholder("RESOURCE_ID", "Resource Identifier", "string");
        resourceId.setDescription("The URI of the resource to grant access to");

        TemplatePlaceholder assignee = createPlaceholder("ASSIGNEE", "Assignee", "string");
        assignee.setDescription("The party who receives access");
        assignee.setOptions(List.of("admin", "user", "viewer"));

        tc.setPlaceholders(List.of(resourceId, assignee));
        return tc;
    }

    /**
     * Builds a date-constrained template with two placeholders (RESOURCE_ID and EXPIRY_DATE).
     *
     * @return a valid TemplateCreate payload
     */
    private static TemplateCreate buildDateConstrainedTemplate() {
        TemplateCreate tc = new TemplateCreate();
        tc.setName("Date-Constrained Access Policy");
        tc.setDescription("A template for granting access within a date range");
        tc.setOdrl(Map.of(
                "@context", "http://www.w3.org/ns/odrl.jsonld",
                "@type", "odrl:Set",
                "odrl:permission", Map.of(
                        "odrl:target", "{{RESOURCE_ID}}",
                        "odrl:action", "odrl:use",
                        "odrl:constraint", Map.of(
                                "odrl:leftOperand", "odrl:dateTime",
                                "odrl:operator", "odrl:lteq",
                                "odrl:rightOperand", "{{EXPIRY_DATE}}"
                        )
                )
        ));
        tc.setNaturalLanguage("Allow use of {{RESOURCE_ID}} until {{EXPIRY_DATE}}");

        TemplatePlaceholder resourceId = createPlaceholder("RESOURCE_ID", "Resource Identifier", "string");
        TemplatePlaceholder expiryDate = createPlaceholder("EXPIRY_DATE", "Expiry Date", "xsd:date");
        expiryDate.setDescription("The date after which access is no longer permitted");

        tc.setPlaceholders(List.of(resourceId, expiryDate));
        return tc;
    }

    /**
     * Creates a {@link TemplatePlaceholder} with the specified key, name, and type.
     *
     * @param key  the placeholder key (e.g. "RESOURCE_ID")
     * @param name the display name
     * @param type the type string (e.g. "string", "number", "boolean", "xsd:date")
     * @return a configured placeholder instance
     */
    private static TemplatePlaceholder createPlaceholder(String key, String name, String type) {
        TemplatePlaceholder p = new TemplatePlaceholder();
        p.setKey(key);
        p.setName(name);
        if (type != null) {
            p.setType(TemplatePlaceholder.TypeEnum.fromString(type));
        }
        return p;
    }

    /**
     * Loads a template creation payload from a JSON test fixture file.
     *
     * @param resourcePath the classpath resource path
     * @return the deserialized TemplateCreate
     * @throws IOException if the file cannot be read
     */
    private TemplateCreate loadTemplateFixture(String resourcePath) throws IOException {
        return objectMapper.readValue(
                getClass().getResourceAsStream(resourcePath),
                TemplateCreate.class);
    }

    /**
     * Serialises a map to its JSON string representation.
     *
     * @param map the map to serialise
     * @return the JSON string
     */
    private String serialiseToJson(Map<String, Object> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            fail("Failed to serialise map to JSON: " + e.getMessage());
            return "";
        }
    }
}
