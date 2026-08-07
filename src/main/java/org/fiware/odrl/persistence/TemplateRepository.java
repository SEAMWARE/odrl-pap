package org.fiware.odrl.persistence;

import org.openapi.quarkus.odrl_yaml.model.Template;
import org.openapi.quarkus.odrl_yaml.model.TemplateCreate;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Repository interface for template CRUD operations.
 *
 * <p>Mirrors the {@link PolicyRepository} pattern: a thin contract over
 * persistence that translates between the generated OpenAPI model types
 * ({@link Template}, {@link TemplateCreate}) and the JPA entity layer.</p>
 */
public interface TemplateRepository {

    /** Shared random instance used for template ID generation. */
    Random RANDOM = new Random();

    /** Length of auto-generated template identifiers. */
    int TEMPLATE_ID_LENGTH = 10;

    /** ASCII code for lowercase letter 'a' (start of random character range). */
    int ASCII_LOWER_A = 97;

    /** ASCII code for lowercase letter 'z' (end of random character range, inclusive). */
    int ASCII_LOWER_Z = 122;

    /**
     * Generate a random 10-character lowercase template identifier.
     *
     * <p>Uses the same algorithm as {@link PolicyRepository#generatePolicyId()}.</p>
     *
     * @return a random 10-character string of lowercase letters
     */
    static String generateTemplateId() {
        return RANDOM.ints(ASCII_LOWER_A, ASCII_LOWER_Z + 1)
                .limit(TEMPLATE_ID_LENGTH)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /**
     * Create a new template, optionally scoped to a service.
     *
     * @param templateCreate the template data to persist
     * @param service        the owning service, or empty for a general template
     * @return the persisted template including its generated ID
     */
    Template createTemplate(TemplateCreate templateCreate, Optional<ServiceEntity> service);

    /**
     * Update an existing template by its identifier.
     *
     * @param id             the template identifier
     * @param templateCreate the new template data
     * @return the updated template
     * @throws jakarta.ws.rs.NotFoundException if no template with the given ID exists
     */
    Template updateTemplate(String id, TemplateCreate templateCreate);

    /**
     * Retrieve a template by its identifier.
     *
     * @param id the template identifier
     * @return an {@link Optional} containing the template, or empty if not found
     */
    Optional<Template> getTemplate(String id);

    /**
     * List general (service-unscoped) templates with pagination.
     *
     * @param page     zero-based page index
     * @param pageSize number of templates per page
     * @return a list of templates for the requested page
     */
    List<Template> getTemplates(int page, int pageSize);

    /**
     * List templates scoped to a specific service with pagination.
     *
     * @param serviceId the service identifier
     * @param page      zero-based page index
     * @param pageSize  number of templates per page
     * @return a list of templates for the requested page
     */
    List<Template> getTemplatesByServiceId(String serviceId, int page, int pageSize);

    /**
     * Delete a template by its identifier. No-op if the template does not exist.
     *
     * @param id the template identifier
     */
    void deleteTemplate(String id);
}
