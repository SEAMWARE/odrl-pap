package org.openapi.quarkus.odrl_yaml.api;

import java.util.List;
import java.util.Map;


import org.openapi.quarkus.odrl_yaml.model.Mappings;
import org.openapi.quarkus.odrl_yaml.model.ValidationRequest;
import org.openapi.quarkus.odrl_yaml.model.ValidationResponse;
/**
  * ODRL PAP
  * <p>ODRL PAP</p>
  */
@jakarta.ws.rs.Path("")
@org.eclipse.microprofile.rest.client.inject.RegisterRestClient(configKey="odrl_yaml")
@io.quarkiverse.openapi.generator.annotations.GeneratedClass(value="odrl.yaml", tag = "Ui")
@jakarta.enterprise.context.ApplicationScoped
public interface UiApi {

     /**
     * Gets the supported by the PAP.
     *
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="getMappings", method="GET", path="/mappings")
    @jakarta.ws.rs.GET
    @jakarta.ws.rs.Path("/mappings")
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("getMappings")
    public jakarta.ws.rs.core.Response getMappings(
    );

     /**
     * Validates a policy with a demo request
     *
     * @param validationRequest 
     */
    @io.quarkiverse.openapi.generator.markers.OperationMarker(name="", openApiSpecId="odrl_yaml", operationId="validatePolicy", method="POST", path="/validate")
    @jakarta.ws.rs.POST
    @jakarta.ws.rs.Path("/validate")
    @jakarta.ws.rs.Consumes({"application/json"})
    @jakarta.ws.rs.Produces({"application/json"})
    @io.quarkiverse.openapi.generator.annotations.GeneratedMethod("validatePolicy")
    public jakarta.ws.rs.core.Response validatePolicy(
        ValidationRequest validationRequest
    );

}