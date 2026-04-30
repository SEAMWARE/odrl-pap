# REGO Methods


## dome

| ODRL Class | ODRL Key | Rego-Method | Description |
| --- | --- | --- | --- |
| action | dome-op:create | default | Check if the given request is a creation |
| leftOperand | dome-op:role | role(verifiable_credential, | retrieves the roles from the (lear) credential, that target the current organization |
| leftOperand | dome-op:currentParty | current_party(credential) | the current (organization)party, |
| leftOperand | dome-op:relatedParty | related_party(http_part) | get the entity from tm-forum and extract related party |
| leftOperand | dome-op:owner | owner(related_party) | filter the given list of related_party(ies) for one with role "Owner" |
| leftOperand | dome-op:relatedParty_role | related_party_role(entity) | return the role from the related party of an entity |
| leftOperand | dome-op:validFor_endDateTime | valid_for_end_date_time(entity) | return the end of the validity of an entity |
| leftOperand | dome-op:validFor_startDateTime | valid_for_start_date_time(entity) | return the start of the validity of an entity |

## odrl

| ODRL Class | ODRL Key | Rego-Method | Description |
| --- | --- | --- | --- |
| rightOperand | odrl:policyUsage | policy_usage | return the current time in ms, e.g. the time that the policy is used |
| operand | odrl:and | default | checks if all given constraints are true |
| operand | odrl:andSequence | default | checks if all given constraints are true |
| operand | odrl:or | default | check that at least one of the constraints is true |
| operand | odrl:xone | default | check that exactly one of the constraints is true |
| operator | odrl:eq | default | check that both operands are equal |
| operator | odrl:hasPart | default | check that the rightOperand is in the leftOperand |
| operator | odrl:gt | default | check that the leftOperand is greater than the rightOperand |
| operator | odrl:gteq | default | check that the leftOperand is greater or equal to the rightOperand |
| operator | odrl:isAllOf | default | check that the given sets are equal |
| operator | odrl:isAnyOf | default | check that the leftOperand is contained in the rightOperand set |
| operator | odrl:isNoneOf | default | check that the leftOperand is not contained in the rightOperand set |
| operator | odrl:isPartOf | default | check that the rightOperand is contained in the leftOperand set |
| operator | odrl:lt | default | check that the leftOperand is less than the rightOperand |
| operator | odrl:lteq | default | check that the leftOperand is less or equal to the rightOperand |
| operator | odrl:neq | default | check that the operands are unequal |
| operator | odrl:regex | regex_operator(leftOperand, | default regex_operator(leftOperand, rightOperand) := false |
| action | odrl:modify | default | checks if the given request is a modification |
| action | odrl:delete | default | checks if the given request is a deletion |
| action | odrl:read | default | checks if the given request is a read operation |
| action | odrl:use | default | checks if the given request is a usage |
| target | odrl:target,odrl:uid | default | check that the uid of the target is equal to the given uid |
| assignee | odrl:uid,odrl:assignee | default | is the given user id the same as the given uid |

## ordl

| ODRL Class | ODRL Key | Rego-Method | Description |
| --- | --- | --- | --- |
| leftOperand | odrl:currentTime | current_time | returns the current time in ms |
| leftOperand | odrl:dayOfWeek | day_of_week(ts) | Day of week from timestamp (0=Mon, 6=Sun) |
| leftOperand | odrl:hourOfDay | hour_of_day(ts) | Hour of day (UTC) from timestamp (0–23) |

## gaiax

| ODRL Class | ODRL Key | Rego-Method | Description |
| --- | --- | --- | --- |
| constraint | ovc:constraints | default | evaluates all provided constraints |
| constraint | ovc:credentialSubjectType | default | compares the credentials' subject-type with the provided one |
| leftOperand | ovc:leftOperand | getClaim(verifiable_credential, | retrieves the claim from the credential, using the json-path of the claim |

## json

| ODRL Class | ODRL Key | Rego-Method | Description |
| --- | --- | --- | --- |
| action | json:read | default | checks if the action field in the JSON payload indicates a read operation |
| action | json:write | default | checks if the action field in the JSON payload indicates a write operation |
| action | json:create | default | checks if the action field in the JSON payload indicates a create operation |
| action | json:delete | default | checks if the action field in the JSON payload indicates a delete operation |
| action | json:use | default | checks if the action field in the JSON payload indicates any usage operation |
| leftOperand | json:payloadValue | # | extracts a value from the JSON payload at a given dot-notation path (e.g., "$.my.field") |
| leftOperand | json:subjectValue | subject_value(subject, | extracts a value from the subject/identity object at a given dot-notation path |
| leftOperand | json:payloadType | payload_type(payload) | returns the type field from the JSON payload |

## utils

| ODRL Class | ODRL Key | Rego-Method | Description |
| --- | --- | --- | --- |
| helper | ## | organization_did | did of the organization running the PAP |
| helper | ## | request | the request as part of the policy input |
| helper | ## | body | the request body as json object |
| helper | ## | http_part | the http request |
| helper | ## | headers | the headers of the request |
| helper | ## | authorization | the (undecoded) authorization header |
| helper | ## | decoded_authorization | the decoded authorization jwt |
| helper | ## | decoded_token_payload | the decoded payload of the jwt |
| helper | ## | verifiable_credential | the verifiable credential received as part of the token |
| helper | ## | issuer | the issuer of the credential |
| helper | ## | token | the unprefixed bearer token |
| helper | ## | entity | the entity provided as http-body |
| helper | ## | target | the target of the request, found as the last part of the path |
| generic | organization_did | organization_did | did of the organization running the PAP |
| generic | request | request | the full input request object |
| generic | payload | payload | the JSON payload from the input request |
| generic | entity | entity | alias for the payload, for compatibility with existing mappings that use helper.entity |
| generic | subject | subject | the subject/identity information from the input request |
| generic | target | target | the target extracted from the payload's target field |
| generic | get_value | # | extracts a value from the payload at a given dot-notation path (e.g., "$.my.field") |
| generic | get_subject_value | get_subject_value(value_path) | extracts a value from the subject at a given dot-notation path |

## ngsild

| ODRL Class | ODRL Key | Rego-Method | Description |
| --- | --- | --- | --- |
| action | ngsild:create | default | Check if the given request is a creation |
| leftOperand | ngsi-ld:entityType | entity_type(http_part) | retrieves the type from an entity, either from the request path or from the body |
| leftOperand | ngsi-ld:<property> | # | retrieves the value of the property, only applies to properties of type "Property". |
| leftOperand | ngsi-ld:<property>_observedAt | # | retrieves the observedAt of the property The method should be concretized in the mapping.json, |
| leftOperand | ngsi-ld:<property>_modifiedAt | # | retrieves the modifiedAt of the property The method should be concretized in the mapping.json, |
| leftOperand | ngsi-ld:<relationship> | # | retrieves the object of the relationship, only applies to properties of type "Relationship". |
| leftOperand | ngsi-ld:entityTypeGroup | entity_type_group(http_part) | returns the entity type - let the operator do the comparison |

## tmf

| ODRL Class | ODRL Key | Rego-Method | Description |
| --- | --- | --- | --- |
| action | tmf:create | default | Check if the given request is a creation |
| leftOperand | tmf:lifecycleStatus | life_cycle_status(entity) | return the lifeCycleStatus of a given entity |
| leftOperand | tmf:resource | resource_type(http_part) | retrieves the type of the resource from the path |

## vc

| ODRL Class | ODRL Key | Rego-Method | Description |
| --- | --- | --- | --- |
| assignee | odrl:any | is_any | allows for any user |
| leftOperand | vc:role | role(verifiable_credential, | retrieves the roles from the credential, that target the current organization |
| leftOperand | vc:currentParty | current_party(credential) | the current (organization)party, |
| leftOperand | vc:type | types(verifiable_credential) | the type(s) of the current credential. Converted to array if string type. |

## http

| ODRL Class | ODRL Key | Rego-Method | Description |
| --- | --- | --- | --- |
| operator | http:isInPath | default | check that left operand is in the path of the right operand |
| leftOperand | http:path | path(http_part) | returns the currently requested path |
| leftOperand | http:bodyValue | body_value(body, | retrieves the value of the body content at the given path. |
