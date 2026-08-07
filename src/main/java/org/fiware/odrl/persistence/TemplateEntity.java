package org.fiware.odrl.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.List;
import java.util.Optional;

/**
 * JPA entity for persisting policy templates.
 *
 * <p>A template is an ODRL policy skeleton with {@code {{PLACEHOLDER}}} tokens,
 * a natural-language description, and typed placeholder definitions. Templates
 * may optionally be scoped to a {@link ServiceEntity}; general (unscoped)
 * templates have a {@code null} service reference.</p>
 *
 * <p>The {@code odrl} and {@code placeholders} fields store serialised JSON
 * text and are mapped to {@code TEXT} columns so they can hold arbitrarily
 * large payloads.</p>
 */
@RegisterForReflection
@Entity(name = TemplateEntity.TABLE_NAME)
@Data
public class TemplateEntity extends PanacheEntity {

    /** Database table name for template entities. */
    public static final String TABLE_NAME = "template_entity";

    /** Unique template identifier (auto-generated, 10-character lowercase string). */
    private String templateId;

    /** Human-readable name of the template. */
    private String name;

    /** Human-readable description of what the template does. */
    private String description;

    /** ODRL policy skeleton stored as a serialised JSON string. */
    @Column(columnDefinition = "TEXT")
    private String odrl;

    /** Natural-language sentence using {@code {{PLACEHOLDER}}} keys. */
    @Column(columnDefinition = "TEXT")
    private String naturalLanguage;

    /** Placeholder definitions stored as a serialised JSON array string. */
    @Column(columnDefinition = "TEXT")
    private String placeholders;

    /**
     * Optional link to a service. When {@code null} the template is a
     * general (root-level) template; when set, it is scoped to the linked
     * service and will be cascade-deleted when the service is removed.
     */
    @ManyToOne(optional = true)
    @JoinColumn(name = "serviceId")
    private ServiceEntity serviceEntity;

    /**
     * Find a template entity by its unique template identifier.
     *
     * @param templateId the template identifier to search for
     * @return an {@link Optional} containing the matching entity, or empty if not found
     */
    public static Optional<TemplateEntity> findByTemplateId(String templateId) {
        return Optional.ofNullable(find("templateId", templateId).firstResult());
    }

    /**
     * Return all general (service-unscoped) templates.
     *
     * @return list of template entities where {@code serviceEntity} is {@code null}
     */
    public static List<TemplateEntity> findByServiceEntityIsNull() {
        return list("serviceEntity is null");
    }
}
