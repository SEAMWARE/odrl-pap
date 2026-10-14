package org.fiware.odrl.rego;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A single rego-method as configured in the mapping configuration.
 *
 * <p>The {@code regoMethod} is a {@link String#format(String, Object...)} template. Mappings that need
 * additional information from the policy - for example {@code http:bodyValue}, which needs the path of
 * the body-element to read - declare that information as a format parameter (e.g.
 * {@code http_lo.body_value(helper.http_part.body, %s)}). Such a template must never be emitted as-is,
 * since the unresolved parameter would end up in the generated rego and break its compilation.</p>
 *
 * @param regoPackage the rego package to import, including its alias
 * @param regoMethod  the rego-method template, potentially containing format parameters
 * @param description a human-readable description, served via the mappings endpoint
 * @author <a href="https://github.com/wistefan">Stefan Wiedemann</a>
 */
@RegisterForReflection
public record RegoMethod(String regoPackage, String regoMethod, String description) {

    /**
     * Matches a single format specifier, e.g. {@code %s} or {@code %1$d}, as supported by
     * {@link java.util.Formatter}.
     */
    private static final Pattern FORMAT_SPECIFIER_PATTERN =
            Pattern.compile("%(\\d+\\$)?([-#+ 0,(]*)?(\\d+)?(\\.\\d+)?([a-zA-Z])");

    /**
     * Literal percent-escape, which does not denote a parameter and is therefore removed before counting.
     */
    private static final String ESCAPED_PERCENT = "%%";

    public RegoMethod(String regoPackage, String regoMethod) {
        this(regoPackage, regoMethod, "");
    }

    /**
     * Returns the number of parameters the rego-method template expects, e.g. 1 for
     * {@code http_lo.body_value(helper.http_part.body, %s)} and 0 for {@code http_lo.path(helper.http_part)}.
     *
     * @return the number of format parameters declared by the template, 0 if the template is {@code null}
     */
    public int parameterCount() {
        if (regoMethod == null) {
            return 0;
        }
        Matcher matcher = FORMAT_SPECIFIER_PATTERN.matcher(regoMethod.replace(ESCAPED_PERCENT, ""));
        int parameterCount = 0;
        while (matcher.find()) {
            parameterCount++;
        }
        return parameterCount;
    }

    /**
     * Does the rego-method template expect at least one parameter to be provided by the policy?
     *
     * @return true if the template declares format parameters
     */
    @JsonIgnore
    public boolean isParameterized() {
        return parameterCount() > 0;
    }
}
