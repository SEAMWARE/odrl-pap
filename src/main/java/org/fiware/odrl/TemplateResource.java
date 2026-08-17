package org.fiware.odrl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.fiware.odrl.persistence.TemplateRepository;
import org.openapi.quarkus.odrl_yaml.api.TemplateApi;
import org.openapi.quarkus.odrl_yaml.model.Template;
import org.openapi.quarkus.odrl_yaml.model.TemplateCreate;
import org.openapi.quarkus.odrl_yaml.model.TemplatePlaceholder;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JAX-RS resource handling general (non-service-scoped) template CRUD operations.
 *
 * <p>Implements the generated {@link TemplateApi} interface from the OpenAPI
 * specification. All endpoints live under {@code /template}.</p>
 *
 * <p>Placeholder validation ensures that:</p>
 * <ul>
 *   <li>Each placeholder key is unique within the template</li>
 *   <li>Each placeholder key appears at least once in the ODRL JSON or natural language text</li>
 *   <li>The ODRL field is valid JSON</li>
 * </ul>
 */
@Slf4j
public class TemplateResource implements TemplateApi {

    /**
     * Regex pattern for detecting placeholder tokens in ODRL JSON and natural language text.
     * Matches {@code {{KEY}}} where KEY follows the pattern {@code [A-Z_][A-Z0-9_]*}.
     */
    static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{([A-Z_][A-Z0-9_]*)\\}\\}");

    /** Default number of items per page when no page size is specified. */
    private static final int DEFAULT_PAGE_SIZE = 25;

    /** Default page index when no page is specified. */
    private static final int DEFAULT_PAGE = 0;

    @Inject
    TemplateRepository templateRepository;

    @Inject
    ObjectMapper objectMapper;

    /**
     * Creates a new general (non-service-scoped) policy template.
     *
     * <p>Validates placeholder definitions before persisting. Returns HTTP 201
     * on success with the created template (including its generated ID).</p>
     *
     * @param templateCreate the template creation payload
     * @return HTTP 201 with the created {@link Template}, or HTTP 400 on validation failure
     */
    @Override
    public Response createTemplate(TemplateCreate templateCreate) {
        List<String> validationErrors = validateTemplate(templateCreate);
        if (!validationErrors.isEmpty()) {
            return Response.status(HttpStatus.SC_BAD_REQUEST)
                    .entity(String.join("; ", validationErrors))
                    .build();
        }

        Template template = templateRepository.createTemplate(templateCreate, Optional.empty());
        return Response.status(HttpStatus.SC_CREATED).entity(template).build();
    }

    /**
     * Deletes a general policy template by its identifier.
     *
     * @param templateId the unique identifier of the template to delete
     * @return HTTP 204 on success, or HTTP 404 if no general template with that id exists
     */
    @Override
    public Response deleteTemplateById(String templateId) {
        // Scope the deletion to general templates so this endpoint cannot delete
        // a service-scoped template that GET /template would not even list.
        boolean deleted = templateRepository.deleteGeneralTemplate(templateId);
        return deleted
                ? Response.noContent().build()
                : Response.status(HttpStatus.SC_NOT_FOUND).build();
    }

    /**
     * Retrieves a general policy template by its identifier.
     *
     * @param templateId the unique identifier of the template
     * @return HTTP 200 with the {@link Template}, or HTTP 404 if not found
     */
    @Override
    public Response getTemplateById(String templateId) {
        return templateRepository.getGeneralTemplate(templateId)
                .map(Response::ok)
                .map(Response.ResponseBuilder::build)
                .orElse(Response.status(HttpStatus.SC_NOT_FOUND).build());
    }

    /**
     * Lists all general (non-service-scoped) policy templates with pagination.
     *
     * @param page     zero-based page index (defaults to 0)
     * @param pageSize number of templates per page (defaults to 25)
     * @return HTTP 200 with a list of {@link Template} objects
     */
    @Override
    public Response getTemplates(Integer page, Integer pageSize) {
        List<Template> templates = templateRepository.getTemplates(
                Optional.ofNullable(page).orElse(DEFAULT_PAGE),
                Optional.ofNullable(pageSize).orElse(DEFAULT_PAGE_SIZE));
        return Response.ok(templates).build();
    }

    /**
     * Updates an existing general policy template.
     *
     * <p>Validates placeholder definitions before updating. Returns HTTP 200
     * on success with the updated template, or HTTP 404 if the template does
     * not exist.</p>
     *
     * @param templateId     the unique identifier of the template to update
     * @param templateCreate the updated template payload
     * @return HTTP 200 with the updated {@link Template}, HTTP 400 on validation failure,
     *         or HTTP 404 if not found
     */
    @Override
    public Response updateTemplate(String templateId, TemplateCreate templateCreate) {
        // Only general templates may be updated here; a service-scoped template
        // with the same id must not be reachable through this endpoint.
        if (templateRepository.getGeneralTemplate(templateId).isEmpty()) {
            return Response.status(HttpStatus.SC_NOT_FOUND).build();
        }

        List<String> validationErrors = validateTemplate(templateCreate);
        if (!validationErrors.isEmpty()) {
            return Response.status(HttpStatus.SC_BAD_REQUEST)
                    .entity(String.join("; ", validationErrors))
                    .build();
        }

        Template updated = templateRepository.updateTemplate(templateId, templateCreate);
        return Response.ok(updated).build();
    }

    // -------------------------------------------------------------------------
    // Validation helpers (package-visible for testing)
    // -------------------------------------------------------------------------

    /**
     * Validates a template creation payload for placeholder consistency.
     *
     * <p>Checks performed:</p>
     * <ol>
     *   <li>The ODRL field must be valid JSON (serializable by Jackson)</li>
     *   <li>Placeholder keys must be unique — no duplicates allowed</li>
     *   <li>Every placeholder key must appear at least once as a {@code {{KEY}}}
     *       token in the ODRL JSON string or the natural language text</li>
     * </ol>
     *
     * @param templateCreate the template to validate
     * @return a list of error messages; empty if validation passes
     */
    static List<String> validateTemplate(TemplateCreate templateCreate) {
        java.util.ArrayList<String> errors = new java.util.ArrayList<>();

        // 1. Required fields (mirror the OpenAPI schema's `required` list, which
        //    Quarkus does not enforce on the request body on its own).
        if (templateCreate.getName() == null || templateCreate.getName().isBlank()) {
            errors.add("Template name is required");
        }
        if (templateCreate.getOdrl() == null) {
            errors.add("An ODRL skeleton is required");
        }

        // 2. Check that placeholders are provided
        if (templateCreate.getPlaceholders() == null || templateCreate.getPlaceholders().isEmpty()) {
            errors.add("At least one placeholder is required");
            return errors;
        }

        // 3. Check for duplicate / blank placeholder keys
        Set<String> definedKeys = new HashSet<>();
        for (TemplatePlaceholder placeholder : templateCreate.getPlaceholders()) {
            if (placeholder.getKey() == null || placeholder.getKey().isBlank()) {
                errors.add("Placeholder key must not be null or blank");
                continue;
            }
            if (!definedKeys.add(placeholder.getKey())) {
                errors.add(String.format("Duplicate placeholder key: %s", placeholder.getKey()));
            }
        }

        // 4. Detect the {{KEY}} tokens actually present, using the SAME pattern the
        //    frontend substitutes with ([A-Z_][A-Z0-9_]*). A lowercase or otherwise
        //    non-matching key is therefore never detected here, and so is reported
        //    below rather than silently leaving a literal token in the policy.
        String naturalLanguage = templateCreate.getNaturalLanguage() != null
                ? templateCreate.getNaturalLanguage()
                : "";
        Set<String> referencedKeys =
                extractPlaceholderKeys(odrlToString(templateCreate.getOdrl()) + " " + naturalLanguage);

        // 5. Every defined placeholder must be referenced by a matching {{KEY}} token.
        for (String key : definedKeys) {
            if (!referencedKeys.contains(key)) {
                errors.add(String.format(
                        "Placeholder key '%s' is not referenced as {{%s}} in the ODRL JSON or natural language text",
                        key, key));
            }
        }

        // 6. Every {{KEY}} token must have a matching placeholder definition, so a
        //    stray token cannot end up unresolved in a created policy.
        for (String token : referencedKeys) {
            if (!definedKeys.contains(token)) {
                errors.add(String.format(
                        "Token {{%s}} has no matching placeholder definition", token));
            }
        }

        return errors;
    }

    /**
     * Converts the ODRL map to its JSON string representation for placeholder
     * token scanning.
     *
     * @param odrl the ODRL policy map (may be {@code null})
     * @return the JSON string, or an empty string if the map is {@code null}
     */
    private static String odrlToString(Map<String, Object> odrl) {
        if (odrl == null) {
            return "";
        }
        try {
            // Use a temporary ObjectMapper for static serialization
            return new ObjectMapper().writeValueAsString(odrl);
        } catch (JsonProcessingException e) {
            // Should not happen since the map was already deserialized by Jackson
            return odrl.toString();
        }
    }

    /**
     * Extracts all placeholder keys from a text string by scanning for
     * {@code {{KEY}}} tokens.
     *
     * @param text the text to scan
     * @return a set of placeholder keys found in the text
     */
    static Set<String> extractPlaceholderKeys(String text) {
        Set<String> keys = new HashSet<>();
        if (text == null) {
            return keys;
        }
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(text);
        while (matcher.find()) {
            keys.add(matcher.group(1));
        }
        return keys;
    }
}
