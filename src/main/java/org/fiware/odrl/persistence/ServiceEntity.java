package org.fiware.odrl.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;
import java.util.Optional;

/**
 * JPA entity representing a managed service.
 *
 * <p>Each service owns a collection of {@link PolicyEntity policies} and
 * {@link TemplateEntity templates} that are cascade-deleted when the
 * service is removed.</p>
 */
@RegisterForReflection
@Entity(name = ServiceEntity.TABLE_NAME)
@Data
public class ServiceEntity extends PanacheEntity {

    public static final String TABLE_NAME = "service_entity";

    private String serviceId;
    private String packageName;

    @OneToMany(mappedBy = "serviceEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PolicyEntity> policies;

    /** Templates scoped to this service; cascade-deleted when the service is removed. */
    @OneToMany(mappedBy = "serviceEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TemplateEntity> templates;

    /**
     * Find a service entity by its unique service identifier.
     *
     * @param serviceId the service identifier to search for
     * @return an {@link Optional} containing the matching entity, or empty if not found
     */
    public static Optional<ServiceEntity> findByServiceId(String serviceId) {
        return Optional.ofNullable(find("serviceId", serviceId).firstResult());
    }

    public ServiceEntity id(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public ServiceEntity packageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

}
