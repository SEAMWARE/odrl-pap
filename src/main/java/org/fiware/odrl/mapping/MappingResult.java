package org.fiware.odrl.mapping;

import io.quarkus.qute.ImmutableList;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author <a href="https://github.com/wistefan">Stefan Wiedemann</a>
 */
public class MappingResult {


    @Getter
    private boolean failed = false;
    private final List<String> failureReasons = new ArrayList<>();

    private final Set<String> imports = new HashSet<>();

    // set default allow := false
    private final Set<String> rules = new HashSet<>();

    @Getter
    @Setter
    private String uid;

    /**
     * Default import alias for the generic JSON evaluation utils package
     * ({@code utils.generic}). Always included so that policies using
     * {@code json:*} namespace mappings can reference {@code generic.payload},
     * {@code generic.subject}, etc. The generic helper is always bundled
     * alongside the gateway-specific helper.
     */
    private static final String GENERIC_UTILS_IMPORT = "import data.utils.generic as generic";

    /** Default import alias for the PEP-specific gateway utils package. */
    private static final String HELPER_UTILS_IMPORT = "import data.utils.helper as helper";

    /** Rego v1 import required by all generated policies. */
    private static final String REGO_V1_IMPORT = "import rego.v1";

    public MappingResult() {
        imports.add(REGO_V1_IMPORT);
        imports.add(HELPER_UTILS_IMPORT);
        imports.add(GENERIC_UTILS_IMPORT);
    }

    public MappingResult addFailure(String reason, String... parameters) {
        failureReasons.add(String.format(reason, parameters));
        failed = true;
        return this;
    }

    public List<String> getFailureReasons() {
        return ImmutableList.copyOf(failureReasons);
    }

    public MappingResult addImport(String importPackage) {
        imports.add(String.format("import data.%s", importPackage));
        return this;
    }

    public MappingResult addRule(String rule) {
        rules.add(rule);
        return this;
    }

    public String getRego(String packageName) {

        StringJoiner regoJoiner = new StringJoiner(System.getProperty("line.separator"));

        regoJoiner.add(String.format("package %s", packageName));
        regoJoiner.add("");
        imports.forEach(regoJoiner::add);
        regoJoiner.add("");
        regoJoiner.add("is_allowed if {");
        rules.forEach(regoJoiner::add);
        regoJoiner.add("}");

        return regoJoiner.toString();
    }
}
