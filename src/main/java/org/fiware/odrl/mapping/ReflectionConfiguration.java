package org.fiware.odrl.mapping;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.openapi.quarkus.odrl_yaml.model.Mapping;
import org.openapi.quarkus.odrl_yaml.model.Mappings;
import org.openapi.quarkus.odrl_yaml.model.Policy;
import org.openapi.quarkus.odrl_yaml.model.PolicyPath;
import org.openapi.quarkus.odrl_yaml.model.Service;
import org.openapi.quarkus.odrl_yaml.model.ServiceListInner;
import org.openapi.quarkus.odrl_yaml.model.ValidationResponse;

/**
 * Register the output objects for reflection
 *
 * @author <a href="https://github.com/wistefan">Stefan Wiedemann</a>
 */
@RegisterForReflection(targets = {Policy.class, Service.class, ServiceListInner.class, PolicyPath.class, ValidationResponse.class, Mappings.class, Mapping.class})
public class ReflectionConfiguration {
}
