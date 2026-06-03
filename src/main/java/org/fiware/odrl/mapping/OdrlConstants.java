package org.fiware.odrl.mapping;

import java.util.List;
import java.util.Set;

/**
 * @author <a href="https://github.com/wistefan">Stefan Wiedemann</a>
 */
public class OdrlConstants {

    public static final String CONTEXT_KEY = "@context";
    public static final String TYPE_KEY = "@type";
    public static final String ID_KEY = "@id";
    public static final String VALUE_KEY = "@value";
    public static final String GRAPH_KEY = "@graph";
    public static final String LIST_KEY = "@list";

    public static final String STRING_TYPE = "xsd:string";
    public static final String DATE_TYPE = "xsd:date";

    /**
     * XSD types that represent integer values and should be parsed as Long.
     */
    public static final Set<String> INTEGER_TYPES = Set.of(
            "xsd:integer", "xsd:long", "xsd:int", "xsd:short", "xsd:byte",
            "xsd:nonNegativeInteger", "xsd:positiveInteger",
            "xsd:nonPositiveInteger", "xsd:negativeInteger",
            "xsd:unsignedLong", "xsd:unsignedInt", "xsd:unsignedShort", "xsd:unsignedByte"
    );

    /**
     * XSD types that represent floating-point values and should be parsed as Double.
     */
    public static final Set<String> DECIMAL_TYPES = Set.of("xsd:decimal", "xsd:float", "xsd:double");
    public static final String TYPE_POLICY = "odrl:Policy";
    public static final String TYPE_PERMISSION = "odrl:Permission";
    public static final String TYPE_PARTY = "odrl:Party";
    public static final String TYPE_PARTY_COLLECTION = "odrl:PartyCollection";
    public static final String TYPE_CONSTRAINT = "odrl:Constraint";
    public static final String TYPE_LOGICAL_CONSTRAINT = "odrl:LogicalConstraint";
    public static final String TYPE_ASSET_COLLECTION = "odrl:AssetCollection";
    public static final String TYPE_ASSET = "odrl:Asset";


    public static final String PROFILE_KEY = "odrl:profile";
    public static final String ASSIGNER_KEY = "odrl:assigner";
    public static final String PERMISSION_KEY = "odrl:permission";
    public static final String ASSIGNEE_KEY = "odrl:assignee";
    public static final String SOURCE_KEY = "odrl:source";
    public static final String REFINEMENT_KEY = "odrl:refinement";
    public static final String LEFT_OPERAND_KEY = "odrl:leftOperand";
    public static final String OPERATOR_KEY = "odrl:operator";
    public static final String RIGHT_OPERAND_KEY = "odrl:rightOperand";
    public static final String ODRL_UID_KEY = "odrl:uid";
    public static final String TARGET_KEY = "odrl:target";
    public static final String ACTION_KEY = "odrl:action";
    public static final String CONSTRAINT_KEY = "odrl:constraint";

    public static final List<String> SUPPORTED_POLICY_TYPES = List.of(TYPE_POLICY, "odrl:Agreement");

    private OdrlConstants() {
        // prevent instantiation
    }
}
