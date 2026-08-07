package org.fiware.odrl.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.openapi.quarkus.odrl_yaml.model.Template;
import org.openapi.quarkus.odrl_yaml.model.TemplateCreate;
import org.openapi.quarkus.odrl_yaml.model.TemplatePlaceholder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JPA/Panache-backed implementation of {@link TemplateRepository}.
 *
 * <p>Follows the same conventions as {@link PersistentPolicyRepository}:
 * {@code @Transactional} on mutation methods, Panache page/sort for
 * pagination, and service-scoping via the {@code serviceEntity}
 * relationship on {@link TemplateEntity}.</p>
 *
 * <p>The ODRL skeleton and placeholder definitions are stored as serialised
 * JSON strings in {@code TEXT} columns. Conversion between the API model
 * types and the raw JSON is handled via Jackson {@link ObjectMapper}.</p>
 */
@Slf4j
@ApplicationScoped
public class PersistentTemplateRepository implements TemplateRepository {

    /** Column used for default ordering in paginated queries. */
    private static final String DEFAULT_SORT = "id";

    /** Jackson type reference for deserialising the ODRL skeleton map. */
    private static final TypeReference<Map<String, Object>> MAP_TYPE_REF = new TypeReference<>() {};

    /** Jackson type reference for deserialising the placeholder list. */
    private static final TypeReference<List<TemplatePlaceholder>> PLACEHOLDER_LIST_TYPE_REF = new TypeReference<>() {};

    @Inject
    ObjectMapper objectMapper;

    @Override
    @Transactional
    public Template createTemplate(TemplateCreate templateCreate, Optional<ServiceEntity> service) {
        String id = getUniqueId();

        // Re-attach the service entity within this transaction to avoid detached-entity errors
        Optional<ServiceEntity> managedService = service
                .map(ServiceEntity::getServiceId)
                .flatMap(ServiceEntity::findByServiceId);

        TemplateEntity entity = new TemplateEntity();
        entity.setTemplateId(id);
        entity.setName(templateCreate.getName());
        entity.setDescription(templateCreate.getDescription());
        entity.setOdrl(serialiseMap(templateCreate.getOdrl()));
        entity.setNaturalLanguage(templateCreate.getNaturalLanguage());
        entity.setPlaceholders(serialisePlaceholders(templateCreate.getPlaceholders()));
        managedService.ifPresent(entity::setServiceEntity);

        entity.persist();
        log.debug("Created template with id {}", id);
        return toApiModel(entity);
    }

    @Override
    @Transactional
    public Template updateTemplate(String id, TemplateCreate templateCreate) {
        TemplateEntity entity = TemplateEntity.findByTemplateId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Template with id %s does not exist.", id)));

        entity.setName(templateCreate.getName());
        entity.setDescription(templateCreate.getDescription());
        entity.setOdrl(serialiseMap(templateCreate.getOdrl()));
        entity.setNaturalLanguage(templateCreate.getNaturalLanguage());
        entity.setPlaceholders(serialisePlaceholders(templateCreate.getPlaceholders()));

        entity.persist();
        log.debug("Updated template with id {}", id);
        return toApiModel(entity);
    }

    @Override
    public Optional<Template> getTemplate(String id) {
        return TemplateEntity.findByTemplateId(id)
                .map(this::toApiModel);
    }

    @Override
    public List<Template> getTemplates(int page, int pageSize) {
        PanacheQuery<TemplateEntity> query = TemplateEntity.find(
                "serviceEntity is null", Sort.ascending(DEFAULT_SORT));
        List<TemplateEntity> entities = query.page(Page.of(page, pageSize)).list();
        return entities.stream().map(this::toApiModel).toList();
    }

    @Override
    public List<Template> getTemplatesByServiceId(String serviceId, int page, int pageSize) {
        PanacheQuery<TemplateEntity> query = TemplateEntity.find(
                "serviceEntity.serviceId = ?1", Sort.ascending(DEFAULT_SORT), serviceId);
        List<TemplateEntity> entities = query.page(Page.of(page, pageSize)).list();
        return entities.stream().map(this::toApiModel).toList();
    }

    @Override
    @Transactional
    public void deleteTemplate(String id) {
        log.debug("Try to delete template {}", id);
        TemplateEntity.findByTemplateId(id)
                .ifPresent(PanacheEntityBase::delete);
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    /**
     * Generate a unique template identifier that does not collide with any
     * existing template.
     *
     * @return a unique 10-character lowercase identifier
     */
    private String getUniqueId() {
        String generatedId = TemplateRepository.generateTemplateId();
        if (getTemplate(generatedId).isPresent()) {
            return getUniqueId();
        }
        return generatedId;
    }

    /**
     * Convert a {@link TemplateEntity} to the generated OpenAPI {@link Template} model.
     *
     * @param entity the entity to convert
     * @return the API model representation
     */
    private Template toApiModel(TemplateEntity entity) {
        Template template = new Template();
        template.setId(entity.getTemplateId());
        template.setName(entity.getName());
        template.setDescription(entity.getDescription());
        template.setOdrl(deserialiseMap(entity.getOdrl()));
        template.setNaturalLanguage(entity.getNaturalLanguage());
        template.setPlaceholders(deserialisePlaceholders(entity.getPlaceholders()));
        return template;
    }

    /**
     * Serialise a map to a JSON string for storage.
     *
     * @param map the map to serialise (may be {@code null})
     * @return the JSON string, or {@code null} if the input is {@code null}
     */
    private String serialiseMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialise ODRL map to JSON", e);
        }
    }

    /**
     * Deserialise a JSON string back into a map.
     *
     * @param json the JSON string (may be {@code null})
     * @return the deserialised map, or an empty map if input is {@code null}
     */
    private Map<String, Object> deserialiseMap(String json) {
        if (json == null) {
            return Collections.emptyMap();
        }
        try {
            return objectMapper.readValue(json, MAP_TYPE_REF);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialise ODRL JSON from database", e);
        }
    }

    /**
     * Serialise a list of placeholder definitions to a JSON string for storage.
     *
     * @param placeholders the placeholder list (may be {@code null})
     * @return the JSON string, or {@code null} if the input is {@code null}
     */
    private String serialisePlaceholders(List<TemplatePlaceholder> placeholders) {
        if (placeholders == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(placeholders);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialise placeholders to JSON", e);
        }
    }

    /**
     * Deserialise a JSON string back into a list of placeholder definitions.
     *
     * @param json the JSON string (may be {@code null})
     * @return the deserialised list, or an empty list if input is {@code null}
     */
    private List<TemplatePlaceholder> deserialisePlaceholders(String json) {
        if (json == null) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, PLACEHOLDER_LIST_TYPE_REF);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialise placeholders JSON from database", e);
        }
    }
}
