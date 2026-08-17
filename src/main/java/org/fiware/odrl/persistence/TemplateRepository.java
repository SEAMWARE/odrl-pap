package org.fiware.odrl.persistence;

import org.openapi.quarkus.odrl_yaml.model.Template;
import org.openapi.quarkus.odrl_yaml.model.TemplateCreate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Repository interface for template CRUD operations.
 *
 * <p>Mirrors the {@link PolicyRepository} pattern: a thin contract over
 * persistence that translates between the generated OpenAPI model types
 * ({@link Template}, {@link TemplateCreate}) and the JPA entity layer.</p>
 */
public interface TemplateRepository {

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
        return ThreadLocalRandom.current().ints(ASCII_LOWER_A, ASCII_LOWER_Z + 1)
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
     * Retrieve a template by its identifier, regardless of service scope.
     *
     * <p>Intended for internal use (e.g. id-uniqueness checks). Endpoints must
     * use the scope-aware {@link #getGeneralTemplate(String)} or
     * {@link #getServiceTemplate(String, String)} to avoid cross-scope access.</p>
     *
     * @param id the template identifier
     * @return an {@link Optional} containing the template, or empty if not found
     */
    Optional<Template> getTemplate(String id);

    /**
     * Retrieve a general (service-unscoped) template by its identifier.
     *
     * @param id the template identifier
     * @return an {@link Optional} containing the template, or empty if no general
     *         template with that id exists
     */
    Optional<Template> getGeneralTemplate(String id);

    /**
     * Retrieve a template by its identifier, scoped to a specific service.
     *
     * @param serviceId the owning service identifier
     * @param id        the template identifier
     * @return an {@link Optional} containing the template, or empty if no template
     *         with that id is owned by the given service
     */
    Optional<Template> getServiceTemplate(String serviceId, String id);

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

    /**
     * Delete a general (service-unscoped) template by its identifier.
     *
     * <p>Service-scoped templates are never touched, so the general endpoint
     * cannot delete a template owned by a service.</p>
     *
     * @param id the template identifier
     * @return {@code true} if a matching general template was deleted, {@code false} otherwise
     */
    boolean deleteGeneralTemplate(String id);

    /**
     * Delete a template by its identifier, scoped to a specific service.
     *
     * <p>Only deletes the template if it is owned by the given service, so one
     * service cannot delete another service's (or a general) template.</p>
     *
     * @param serviceId the owning service identifier
     * @param id        the template identifier
     * @return {@code true} if a matching service-scoped template was deleted, {@code false} otherwise
     */
    boolean deleteServiceTemplate(String serviceId, String id);
}
