package ngsild.leftOperand

import rego.v1

# helper method to retrieve the type from the path
type_from_path(http_part) := tfe if {
	path_without_query := split(http_part.path, "?")[0]
	path_elements := split(path_without_query, "/")
	id_elements := split(path_elements[count(path_elements) - 1], ":")
	tfe = id_elements[2]
} else := tfq if {
	tfq = http_part.query.type
}

# helper to retrieve the type from the body
type_from_body(body) := body.type

# helper to retrieve the type from a batch body (entityOperations/create, upsert, update: an array of entities) -
# the type all of its entities have; undefined when they do not all have the same one, or one is not a string
type_from_batch(body) := tfb if {
	is_array(body)
	count(body) > 0
	every entity in body {
		is_string(entity.type)
	}
	types := {entity.type | some entity in body}
	count(types) == 1
	some tfb in types
}

## ngsi-ld:entityType
# retrieves the type from an entity, either from the request path or from the body - for a batch, the type all of its entities have
entity_type(http_part) := tfp if {
	tfp = type_from_path(http_part)
} else := tfb if {
	tfb = type_from_body(http_part.body)
} else := tfa if {
	tfa = type_from_batch(http_part.body)
}

## ngsi-ld:<property>
# retrieves the value of the property, only applies to properties of type "Property".
# The method should be concretized in the mapping.json, to match a concrete property.
# F.e.: ngsi-ld:brandName = property_value("brandName", http_part.body)
property_value(property_name, body) := body[property_name].value

## ngsi-ld:<property>_observedAt
# retrieves the observedAt of the property The method should be concretized in the mapping.json,
# to match a concrete property.
# F.e.: ngsi-ld:brandName_observedAt = property_value("brandName", http_part.body)
property_observed_at(property_name, body) := body[property_name].observedAt

## ngsi-ld:<property>_modifiedAt
# retrieves the modifiedAt of the property The method should be concretized in the mapping.json,
# to match a concrete property.
# F.e.: ngsi-ld:brandName_modifiedAt= property_value("brandName", http_part.body)
property_observed_at(property_name, body) := body[property_name].modifiedAt

## ngsi-ld:<relationship>
# retrieves the object of the relationship, only applies to properties of type "Relationship".
# The method should be concretized in the mapping.json, to match a concrete property.
# F.e.: ngsi-ld:owningCompany = relationship_object("owningCompany", http_part.body)
relationship_object(relationship_name, body) := body[relationship_name].object

## ngsi-ld:entityTypeGroup
# returns the entity type - let the operator do the comparison
entity_type_group(http_part) := entity_type(http_part)
