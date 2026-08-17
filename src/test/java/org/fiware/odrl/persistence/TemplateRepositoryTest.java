package org.fiware.odrl.persistence;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openapi.quarkus.odrl_yaml.model.Template;
import org.openapi.quarkus.odrl_yaml.model.TemplateCreate;
import org.openapi.quarkus.odrl_yaml.model.TemplatePlaceholder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link PersistentTemplateRepository}.
 *
 * <p>Uses the Quarkus test profile with an H2 in-memory database and
 * {@code drop-and-create} DDL generation, so each test run starts fresh.</p>
 */
@QuarkusTest
@TestProfile(RepositoryTestProfile.class)
class TemplateRepositoryTest {

    /** Default page index for unpaginated queries. */
    private static final int DEFAULT_PAGE = 0;

    /** Default page size for test queries. */
    private static final int DEFAULT_PAGE_SIZE = 100;

    @Inject
    TemplateRepository templateRepository;

    @Inject
    ServiceRepository serviceRepository;

    @AfterEach
    @Transactional
    void cleanUp() {
        TemplateEntity.deleteAll();
        ServiceEntity.deleteAll();
    }

    // -------------------------------------------------------------------------
    // ID generation
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("generateTemplateId produces 10-character lowercase strings")
    void testGenerateTemplateId() {
        String id = TemplateRepository.generateTemplateId();
        assertNotNull(id);
        assertEquals(TemplateRepository.TEMPLATE_ID_LENGTH, id.length(),
                "Generated ID should be exactly 10 characters");
        assertTrue(id.chars().allMatch(c -> c >= 'a' && c <= 'z'),
                "Generated ID should only contain lowercase letters");
    }

    // -------------------------------------------------------------------------
    // CRUD — General (service-unscoped) templates
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("createTemplate persists a general template and returns it with an ID")
    void testCreateGeneralTemplate() {
        TemplateCreate create = buildSampleTemplateCreate("Test Template");

        Template result = templateRepository.createTemplate(create, Optional.empty());

        assertNotNull(result.getId(), "Created template must have an auto-generated ID");
        assertEquals("Test Template", result.getName());
        assertEquals("A test description", result.getDescription());
        assertEquals("Allow access to {{RESOURCE_ID}}", result.getNaturalLanguage());
        assertNotNull(result.getOdrl());
        assertEquals(1, result.getPlaceholders().size());
        assertEquals("RESOURCE_ID", result.getPlaceholders().get(0).getKey());
    }

    @Test
    @DisplayName("getTemplate retrieves a previously created template")
    void testGetTemplate() {
        Template created = templateRepository.createTemplate(
                buildSampleTemplateCreate("Find Me"), Optional.empty());

        Optional<Template> found = templateRepository.getTemplate(created.getId());

        assertTrue(found.isPresent(), "Template should be retrievable by its ID");
        assertEquals(created.getId(), found.get().getId());
        assertEquals("Find Me", found.get().getName());
    }

    @Test
    @DisplayName("getTemplate returns empty for non-existent ID")
    void testGetTemplateNotFound() {
        Optional<Template> result = templateRepository.getTemplate("nonexistent");
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("getTemplates lists only general (service-unscoped) templates")
    void testGetTemplatesFiltersServiceScoped() {
        // Create a general template
        templateRepository.createTemplate(
                buildSampleTemplateCreate("General"), Optional.empty());

        // Create a service-scoped template
        serviceRepository.createService("test-service");
        Optional<ServiceEntity> service = serviceRepository.getService("test-service");
        templateRepository.createTemplate(
                buildSampleTemplateCreate("Scoped"), service);

        List<Template> generalTemplates = templateRepository.getTemplates(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);

        assertEquals(1, generalTemplates.size(),
                "Only general templates should be returned");
        assertEquals("General", generalTemplates.get(0).getName());
    }

    @Test
    @DisplayName("updateTemplate modifies an existing template")
    void testUpdateTemplate() {
        Template created = templateRepository.createTemplate(
                buildSampleTemplateCreate("Original"), Optional.empty());

        TemplateCreate update = buildSampleTemplateCreate("Updated");
        update.setDescription("New description");

        Template updated = templateRepository.updateTemplate(created.getId(), update);

        assertEquals(created.getId(), updated.getId(), "ID must not change on update");
        assertEquals("Updated", updated.getName());
        assertEquals("New description", updated.getDescription());
    }

    @Test
    @DisplayName("updateTemplate throws for non-existent template")
    void testUpdateTemplateNotFound() {
        TemplateCreate update = buildSampleTemplateCreate("Ghost");
        assertThrows(IllegalArgumentException.class,
                () -> templateRepository.updateTemplate("nonexistent", update));
    }

    @Test
    @DisplayName("deleteTemplate removes the template")
    void testDeleteTemplate() {
        Template created = templateRepository.createTemplate(
                buildSampleTemplateCreate("Delete Me"), Optional.empty());

        templateRepository.deleteTemplate(created.getId());

        assertFalse(templateRepository.getTemplate(created.getId()).isPresent(),
                "Deleted template should no longer be retrievable");
    }

    @Test
    @DisplayName("deleteTemplate is a no-op for non-existent IDs")
    void testDeleteTemplateNonExistent() {
        // Should not throw
        templateRepository.deleteTemplate("nonexistent");
    }

    // -------------------------------------------------------------------------
    // CRUD — Service-scoped templates
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("createTemplate with service scopes the template to that service")
    void testCreateServiceScopedTemplate() {
        serviceRepository.createService("svc-1");
        Optional<ServiceEntity> service = serviceRepository.getService("svc-1");

        templateRepository.createTemplate(
                buildSampleTemplateCreate("Scoped"), service);

        List<Template> scopedTemplates = templateRepository.getTemplatesByServiceId(
                "svc-1", DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
        assertEquals(1, scopedTemplates.size());
        assertEquals("Scoped", scopedTemplates.get(0).getName());

        // Must NOT appear in general templates
        List<Template> generalTemplates = templateRepository.getTemplates(
                DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
        assertTrue(generalTemplates.isEmpty(),
                "Service-scoped template must not appear in general listing");
    }

    @Test
    @DisplayName("getTemplatesByServiceId isolates templates per service")
    void testServiceIsolation() {
        serviceRepository.createService("svc-a");
        serviceRepository.createService("svc-b");

        templateRepository.createTemplate(buildSampleTemplateCreate("A-tmpl"),
                serviceRepository.getService("svc-a"));
        templateRepository.createTemplate(buildSampleTemplateCreate("B-tmpl"),
                serviceRepository.getService("svc-b"));

        List<Template> aTemplates = templateRepository.getTemplatesByServiceId(
                "svc-a", DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
        List<Template> bTemplates = templateRepository.getTemplatesByServiceId(
                "svc-b", DEFAULT_PAGE, DEFAULT_PAGE_SIZE);

        assertEquals(1, aTemplates.size());
        assertEquals("A-tmpl", aTemplates.get(0).getName());
        assertEquals(1, bTemplates.size());
        assertEquals("B-tmpl", bTemplates.get(0).getName());
    }

    @Test
    @DisplayName("Deleting a service cascades to its templates")
    void testServiceDeletionCascadesToTemplates() {
        serviceRepository.createService("cascade-svc");
        Optional<ServiceEntity> service = serviceRepository.getService("cascade-svc");

        Template t = templateRepository.createTemplate(
                buildSampleTemplateCreate("Cascade Me"), service);

        serviceRepository.deleteService("cascade-svc");

        assertFalse(templateRepository.getTemplate(t.getId()).isPresent(),
                "Template should be cascade-deleted when its owning service is deleted");
    }

    // -------------------------------------------------------------------------
    // Pagination
    // -------------------------------------------------------------------------

    @ParameterizedTest(name = "page {0} returns correct subset")
    @ValueSource(ints = {0, 1, 2})
    @DisplayName("Pagination returns correct page subsets")
    void testPagination(int page) {
        int pageSize = 2;
        int totalTemplates = 5;

        for (int i = 0; i < totalTemplates; i++) {
            templateRepository.createTemplate(
                    buildSampleTemplateCreate("Template " + i), Optional.empty());
        }

        List<Template> result = templateRepository.getTemplates(page, pageSize);

        int expectedSize = Math.min(pageSize, Math.max(0, totalTemplates - page * pageSize));
        assertEquals(expectedSize, result.size(),
                "Page " + page + " should contain " + expectedSize + " templates");
    }

    // -------------------------------------------------------------------------
    // JSON round-trip
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("ODRL and placeholders survive JSON round-trip through the database")
    void testJsonRoundTrip() {
        TemplateCreate create = new TemplateCreate();
        create.setName("JSON Round Trip");
        create.setOdrl(Map.of(
                "@context", "http://www.w3.org/ns/odrl.jsonld",
                "@type", "Set",
                "permission", List.of(Map.of(
                        "target", "{{TARGET}}",
                        "action", "use"
                ))
        ));
        create.setNaturalLanguage("Allow use of {{TARGET}}");

        TemplatePlaceholder ph1 = new TemplatePlaceholder();
        ph1.setKey("TARGET");
        ph1.setName("Target Resource");
        ph1.setDescription("The resource to grant access to");
        ph1.setType(TemplatePlaceholder.TypeEnum.STRING);
        ph1.setOptions(List.of("resource-a", "resource-b", "resource-c"));

        TemplatePlaceholder ph2 = new TemplatePlaceholder();
        ph2.setKey("EXPIRY");
        ph2.setName("Expiry Date");
        ph2.setType(TemplatePlaceholder.TypeEnum.XSD_COLON_DATE);

        create.setPlaceholders(List.of(ph1, ph2));

        Template created = templateRepository.createTemplate(create, Optional.empty());
        Template retrieved = templateRepository.getTemplate(created.getId()).orElseThrow();

        // Verify ODRL map
        assertEquals("http://www.w3.org/ns/odrl.jsonld", retrieved.getOdrl().get("@context"));
        assertEquals("Set", retrieved.getOdrl().get("@type"));

        // Verify placeholders
        assertEquals(2, retrieved.getPlaceholders().size());

        TemplatePlaceholder p1 = retrieved.getPlaceholders().get(0);
        assertEquals("TARGET", p1.getKey());
        assertEquals("Target Resource", p1.getName());
        assertEquals("The resource to grant access to", p1.getDescription());
        assertEquals(TemplatePlaceholder.TypeEnum.STRING, p1.getType());
        assertEquals(List.of("resource-a", "resource-b", "resource-c"), p1.getOptions());

        TemplatePlaceholder p2 = retrieved.getPlaceholders().get(1);
        assertEquals("EXPIRY", p2.getKey());
        assertEquals("Expiry Date", p2.getName());
        assertEquals(TemplatePlaceholder.TypeEnum.XSD_COLON_DATE, p2.getType());
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Build a minimal but valid {@link TemplateCreate} for testing.
     *
     * @param name the template name
     * @return a populated {@link TemplateCreate} instance
     */
    private TemplateCreate buildSampleTemplateCreate(String name) {
        TemplateCreate create = new TemplateCreate();
        create.setName(name);
        create.setDescription("A test description");
        create.setOdrl(Map.of("odrl:target", "{{RESOURCE_ID}}"));
        create.setNaturalLanguage("Allow access to {{RESOURCE_ID}}");

        TemplatePlaceholder placeholder = new TemplatePlaceholder();
        placeholder.setKey("RESOURCE_ID");
        placeholder.setName("Resource ID");
        placeholder.setDescription("The target resource identifier");
        placeholder.setType(TemplatePlaceholder.TypeEnum.STRING);

        create.setPlaceholders(List.of(placeholder));
        return create;
    }
}
