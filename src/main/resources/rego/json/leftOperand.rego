package json.leftOperand

import rego.v1

## json:payloadValue
# extracts a value from the JSON payload at a given dot-notation path (e.g., "$.my.field")
# delegates to walk-based extraction for JSONPath-like access
payload_value(payload, value_path) := property if {
	# split the path into an array of keys - e.g. "$.my.fancy.property" becomes ["my","fancy","property"]
	key_array := split(trim_prefix(value_path, "$."), ".")

	# walk through the payload, providing tuples of path array & value
	walk(payload, walked_tuple)

	# check that we found the property keyed by our path
	walked_tuple[0] == key_array

	# return the actual value
	property = walked_tuple[1]
}

## json:subjectValue
# extracts a value from the subject/identity object at a given dot-notation path
subject_value(subject, value_path) := property if {
	# split the path into an array of keys
	key_array := split(trim_prefix(value_path, "$."), ".")

	# walk through the subject object
	walk(subject, walked_tuple)

	# check that we found the property keyed by our path
	walked_tuple[0] == key_array

	# return the actual value
	property = walked_tuple[1]
}

## json:payloadType
# returns the type field from the JSON payload
payload_type(payload) := payload.type
