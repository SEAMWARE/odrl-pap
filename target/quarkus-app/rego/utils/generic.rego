package utils.generic

import rego.v1

## organization_did
# did of the organization running the PAP
organization_did := data.data.organizationDid

## request
# the full input request object
request := input.request

## payload
# the JSON payload from the input request
payload := request.payload

## entity
# alias for the payload, for compatibility with existing mappings that use helper.entity
entity := payload

## subject
# the subject/identity information from the input request
subject := request.subject

## target
# the target extracted from the payload's target field
target := payload.target

## get_value
# extracts a value from the payload at a given dot-notation path (e.g., "$.my.field")
# uses walk() for JSONPath-like extraction, similar to http.leftOperand.body_value
get_value(value_path) := property if {
	# split the path into an array of keys - e.g. "$.my.fancy.property" becomes ["my","fancy","property"]
	key_array := split(trim_prefix(value_path, "$."), ".")

	# walk through the payload, providing tuples of path array & value
	walk(payload, walked_tuple)

	# check that we found the property keyed by our path
	walked_tuple[0] == key_array

	# return the actual value
	property = walked_tuple[1]
}

## get_subject_value
# extracts a value from the subject at a given dot-notation path
get_subject_value(value_path) := property if {
	# split the path into an array of keys
	key_array := split(trim_prefix(value_path, "$."), ".")

	# walk through the subject object
	walk(subject, walked_tuple)

	# check that we found the property keyed by our path
	walked_tuple[0] == key_array

	# return the actual value
	property = walked_tuple[1]
}
