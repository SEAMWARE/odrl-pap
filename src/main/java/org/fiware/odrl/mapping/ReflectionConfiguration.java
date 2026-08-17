package org.fiware.odrl.mapping;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.openapi.quarkus.odrl_yaml.model.Mapping;
import org.openapi.quarkus.odrl_yaml.model.Mappings;
import org.openapi.quarkus.odrl_yaml.model.Policy;
import org.openapi.quarkus.odrl_yaml.model.PolicyPath;
import org.openapi.quarkus.odrl_yaml.model.Service;
import org.openapi.quarkus.odrl_yaml.model.ServiceListInner;
import org.openapi.quarkus.odrl_yaml.model.Template;
import org.openapi.quarkus.odrl_yaml.model.TemplateCreate;
import org.openapi.quarkus.odrl_yaml.model.TemplatePlaceholder;
import org.openapi.quarkus.odrl_yaml.model.ValidationResponse;

/**
 * Register the output objects for reflection.
 *
 * These generated OpenAPI models are serialized/deserialized by Jackson via
 * runtime reflection over their getters. In a native image, reflection is only
 * available for classes explicitly registered here — otherwise Jackson finds no
 * accessible properties and serializes them as empty objects ({@code {}}).
 *
 * The template models ({@link Template}, {@link TemplateCreate},
 * {@link TemplatePlaceholder} and its nested {@code TypeEnum}, which uses
 * {@code @JsonValue}/{@code @JsonCreator}) must be included so that endpoints
 * such as {@code GET /template} serialize correctly in native builds.
 *
 * @author <a href="https://github.com/wistefan">Stefan Wiedemann</a>
 */
@RegisterForReflection(targets = {Policy.class, Service.class, ServiceListInner.class, PolicyPath.class, ValidationResponse.class, Mappings.class, Mapping.class,
        Template.class, TemplateCreate.class, TemplatePlaceholder.class, TemplatePlaceholder.TypeEnum.class})
public class ReflectionConfiguration {
}
