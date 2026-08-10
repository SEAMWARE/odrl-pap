package org.fiware.odrl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.fiware.odrl.jsonld.JsonLdHandler;
import org.fiware.odrl.mapping.*;
import org.fiware.odrl.persistence.PolicyRepository;
import org.fiware.odrl.persistence.ServiceEntity;
import org.fiware.odrl.persistence.ServiceRepository;
import org.fiware.odrl.persistence.TemplateRepository;
import org.fiware.odrl.verification.TypeVerifier;
import org.openapi.quarkus.odrl_yaml.api.ServiceApi;
import org.openapi.quarkus.odrl_yaml.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JAX-RS resource handling service-level operations including service CRUD,
 * service-scoped policy management, and service-scoped template management.
 * Implements the generated {@link ServiceApi} interface from the OpenAPI specification.
 */
@Slf4j
public class ServiceResource extends ApiResource implements ServiceApi {

    private static final String MAIN_POLICY_ID = "main";

    private static final String POLICY_PACKAGE = "policy";
    private static final String DATA_PACKAGE = "data";
    private static final String METHODS_PACKAGE = "methods";
    private static final int DEFAULT_PAGE_SIZE = 25;
    private static final int DEFAULT_PAGE = 0;

    @Inject
    TemplateRepository templateRepository;

    protected ServiceResource(ObjectMapper objectMapper, JsonLdHandler jsonLdHandler, OdrlMapper odrlMapper, MappingConfiguration mappingConfiguration, PolicyRepository policyRepository, ServiceRepository serviceRepository, Instance<TypeVerifier> typeVerifiers, LeftOperandMapper leftOperandMapper, ConstraintMapper constraintMapper, OperatorMapper operatorMapper, RightOperandMapper rightOperandMapper) {
        super(objectMapper, jsonLdHandler, odrlMapper, mappingConfiguration, policyRepository, serviceRepository, typeVerifiers, leftOperandMapper, constraintMapper, operatorMapper, rightOperandMapper);
    }

    @Override
    public Response createService(ServiceCreate serviceCreate) {
        assureNotReserved(serviceCreate.getId());
        String packageName = serviceRepository.createService(serviceCreate.getId());
        return Response.ok(new PolicyPath().policyPath(String.format("%s/%s", packageName, MAIN_POLICY_ID))).build();
    }

    @Override
    public Response createServicePolicy(String serviceId, Map<String, Object> requestBody) {
        return createServicePolicyWithId(serviceId, PolicyRepository.generatePolicyId(), requestBody);
    }

    @Override
    public Response createServicePolicyWithId(String serviceId, String id, Map<String, Object> requestBody) {
        return checkNotFound(serviceId)
                .orElse(super.createPolicyWithId(id, Optional.of(serviceId), requestBody));
    }

    @Override
    public Response deleteService(String serviceId) {
        assureNotReserved(serviceId);
        serviceRepository.deleteService(serviceId);
        return Response.noContent().build();
    }

    @Override
    public Response deleteServicePolicyById(String serviceId, String id) {
        policyRepository.getPolicy(id)
                .filter(p -> p.serviceId().isPresent() && p.serviceId().get().equals(serviceId))
                .ifPresent(pw -> policyRepository.deletePolicy(id));
        return Response.noContent().build();
    }

    @Override
    public Response deleteServicePolicyByUid(String serviceId, String uid) {

        policyRepository.getPolicyByUid(uid)
                .filter(p -> p.serviceId().isPresent() && p.serviceId().get().equals(serviceId))
                .ifPresent(pw -> policyRepository.deletePolicyByUid(uid));
        return Response.noContent().build();
    }

    @Transactional
    @Override
    public Response getService(String serviceId) {
        return serviceRepository.getService(serviceId)
                .map(serviceEntity -> {
                    Service service = new Service()
                            .id(serviceId)
                            .policyPath(String.format("%s/%s", serviceEntity.getPackageName(), MAIN_POLICY_ID));
                    serviceEntity.getPolicies()
                            .stream()
                            .map(pe -> new Policy()
                                    .odrlUid(pe.getUid())
                                    .id(pe.getPolicyId())
                                    .odrl(pe.getOdrl().getPolicy())
                                    .rego(pe.getRego().getPolicy())
                            )
                            .forEach(service::addPoliciesItem);
                    return service;
                })
                .map(Response::ok)
                .map(Response.ResponseBuilder::build)
                .orElse(Response.status(HttpStatus.SC_NOT_FOUND).build());
    }

    @Transactional
    @Override
    public Response getServicePolicies(String serviceId, Integer page, Integer pageSize) {
        return checkNotFound(serviceId)
                .orElse(
                        Response.ok(policyRepository.getPoliciesByServiceId(serviceId, Optional.ofNullable(page).orElse(0), Optional.ofNullable(pageSize).orElse(DEFAULT_PAGE_SIZE))
                                .stream()
                                .map(policyEntry -> new Policy()
                                        .id(policyEntry.regoId())
                                        .odrlUid(policyEntry.odrlUid())
                                        .odrl(policyEntry.odrl().policy())
                                        .rego(policyEntry.rego().policy()))
                                .toList()).build());
    }

    @Transactional
    @Override
    public Response getServicePolicyById(String serviceId, String id) {
        return checkNotFound(serviceId).orElseGet(() -> policyRepository.getPolicy(id)
                .filter(policyWrapper -> policyWrapper.serviceId().isPresent() && policyWrapper.serviceId().get().equals(serviceId))
                .map(super::toPolicy)
                .map(Response::ok)
                .map(Response.ResponseBuilder::build)
                .orElse(Response.status(HttpStatus.SC_NOT_FOUND).build()));
    }

    @Transactional
    @Override
    public Response getServicePolicyByUid(String serviceId, String uid) {
        return checkNotFound(serviceId).orElseGet(() -> policyRepository.getPolicyByUid(uid)
                .filter(policyWrapper -> policyWrapper.serviceId().isPresent() && policyWrapper.serviceId().get().equals(serviceId))
                .map(super::toPolicy)
                .map(Response::ok)
                .map(Response.ResponseBuilder::build)
                .orElse(Response.status(HttpStatus.SC_NOT_FOUND).build()));
    }

    @Transactional
    @Override
    public Response getServices(Integer page, Integer pageSize) {
        return Response.ok(
                serviceRepository.getServices(Optional.ofNullable(page).orElse(0), Optional.ofNullable(pageSize).orElse(DEFAULT_PAGE_SIZE))
                        .stream()
                        .map(ServiceEntity::getServiceId)
                        .map(serviceId -> new ServiceListInner().id(serviceId).policyPath(String.format("%s/%s", serviceId, MAIN_POLICY_ID)))
                        .toList()).build();
    }

    private Optional<Response> checkNotFound(String serviceId) {
        if (serviceRepository.getService(serviceId).isEmpty()) {
            return Optional.of(Response.status(HttpStatus.SC_NOT_FOUND).entity(String.format(String.format("Service %s does not exist.", serviceId))).build());
        }
        return Optional.empty();
    }

    private void assureNotReserved(String serviceId) {
        if (List.of(POLICY_PACKAGE, DATA_PACKAGE, METHODS_PACKAGE).contains(serviceId)) {
            throw new IllegalArgumentException(String.format("%s cannot be used as service id.", serviceId));
        }
    }

    // --- Service-scoped template operations ---

    /**
     * Creates a new policy template scoped to the specified service.
     *
     * <p>Validates that the service exists and that placeholder definitions
     * are consistent before persisting. Returns HTTP 201 on success.</p>
     *
     * @param serviceId      the service to scope the template to
     * @param templateCreate the template creation payload
     * @return HTTP 201 with the created {@link Template}, HTTP 404 if service not found,
     *         or HTTP 400 on validation failure
     */
    @Override
    public Response createServiceTemplate(String serviceId, TemplateCreate templateCreate) {
        Optional<ServiceEntity> serviceOpt = serviceRepository.getService(serviceId);
        if (serviceOpt.isEmpty()) {
            return Response.status(HttpStatus.SC_NOT_FOUND)
                    .entity(String.format("Service %s does not exist.", serviceId))
                    .build();
        }

        java.util.List<String> validationErrors = TemplateResource.validateTemplate(templateCreate);
        if (!validationErrors.isEmpty()) {
            return Response.status(HttpStatus.SC_BAD_REQUEST)
                    .entity(String.join("; ", validationErrors))
                    .build();
        }

        Template template = templateRepository.createTemplate(templateCreate, serviceOpt);
        return Response.status(HttpStatus.SC_CREATED).entity(template).build();
    }

    /**
     * Deletes a service-scoped policy template by its identifier.
     *
     * <p>If the service does not exist, returns HTTP 404. Otherwise deletes the
     * template (no-op if the template does not exist) and returns HTTP 204.</p>
     *
     * @param serviceId  the service the template belongs to
     * @param templateId the unique identifier of the template to delete
     * @return HTTP 204 on success, or HTTP 404 if the service does not exist
     */
    @Override
    public Response deleteServiceTemplateById(String serviceId, String templateId) {
        return checkNotFound(serviceId)
                .orElseGet(() -> {
                    templateRepository.deleteTemplate(templateId);
                    return Response.noContent().build();
                });
    }

    /**
     * Retrieves a service-scoped policy template by its identifier.
     *
     * @param serviceId  the service the template belongs to
     * @param templateId the unique identifier of the template
     * @return HTTP 200 with the {@link Template}, or HTTP 404 if service or template not found
     */
    @Override
    public Response getServiceTemplateById(String serviceId, String templateId) {
        return checkNotFound(serviceId)
                .orElseGet(() -> templateRepository.getTemplate(templateId)
                        .map(Response::ok)
                        .map(Response.ResponseBuilder::build)
                        .orElse(Response.status(HttpStatus.SC_NOT_FOUND).build()));
    }

    /**
     * Lists policy templates scoped to the specified service with pagination.
     *
     * @param serviceId the service to list templates for
     * @param page      zero-based page index (defaults to 0)
     * @param pageSize  number of templates per page (defaults to 25)
     * @return HTTP 200 with a list of {@link Template} objects, or HTTP 404 if service not found
     */
    @Override
    public Response getServiceTemplates(String serviceId, Integer page, Integer pageSize) {
        return checkNotFound(serviceId)
                .orElseGet(() -> {
                    java.util.List<Template> templates = templateRepository.getTemplatesByServiceId(
                            serviceId,
                            Optional.ofNullable(page).orElse(DEFAULT_PAGE),
                            Optional.ofNullable(pageSize).orElse(DEFAULT_PAGE_SIZE));
                    return Response.ok(templates).build();
                });
    }

    /**
     * Updates a service-scoped policy template.
     *
     * <p>Validates that the service and template exist and that placeholder
     * definitions are consistent before updating.</p>
     *
     * @param serviceId      the service the template belongs to
     * @param templateId     the unique identifier of the template to update
     * @param templateCreate the updated template payload
     * @return HTTP 200 with the updated {@link Template}, HTTP 404 if service or template not found,
     *         or HTTP 400 on validation failure
     */
    @Override
    public Response updateServiceTemplate(String serviceId, String templateId, TemplateCreate templateCreate) {
        Optional<Response> notFound = checkNotFound(serviceId);
        if (notFound.isPresent()) {
            return notFound.get();
        }

        if (templateRepository.getTemplate(templateId).isEmpty()) {
            return Response.status(HttpStatus.SC_NOT_FOUND).build();
        }

        java.util.List<String> validationErrors = TemplateResource.validateTemplate(templateCreate);
        if (!validationErrors.isEmpty()) {
            return Response.status(HttpStatus.SC_BAD_REQUEST)
                    .entity(String.join("; ", validationErrors))
                    .build();
        }

        Template updated = templateRepository.updateTemplate(templateId, templateCreate);
        return Response.ok(updated).build();
    }
}
