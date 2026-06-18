package org.fiware.odrl.jsonld;

/**
 * Represents the evaluation context for a policy, determined by inspecting
 * the policy's JSON-LD namespaces. The evaluation context controls how the
 * validation input is structured and which rego helpers are invoked.
 *
 * <ul>
 *   <li>{@link #HTTP} — the default; wraps a {@code TestRequest} as
 *       {@code {"input": {"request": testRequest}}} for API gateway (APISIX/Kong)
 *       evaluation.</li>
 *   <li>{@link #JSON} — wraps a {@code GenericJsonInput} as
 *       {@code {"input": {"request": {"payload": ..., "subject": ...}}}} for
 *       arbitrary JSON payload evaluation.</li>
 * </ul>
 *
 * <p>This enum is <em>not</em> related to the {@link org.fiware.odrl.Pep} enum,
 * which selects the API gateway type. Evaluation context routing is driven
 * entirely by the policy's own JSON-LD context / namespace usage.</p>
 */
public enum EvaluationContext {

    /**
     * HTTP request evaluation context (default). Policies use HTTP-oriented
     * namespaces (e.g., {@code odrl:read}, {@code http:isInPath}).
     */
    HTTP,

    /**
     * Generic JSON payload evaluation context. Policies use the {@code json:}
     * namespace (e.g., {@code json:read}, {@code json:payloadValue}).
     */
    JSON
}
