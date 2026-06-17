package dcp.leftOperand

import rego.v1

## dcp:membershipType
# retrieves the membershipType from a MembershipCredential in the vc claims
membership_type(subject) := result if {
	some vc in subject.claims.vc
	some t in vc.type
	t == "MembershipCredential"
	some cs in vc.credentialSubject
	result := cs.membershipType
}

## dcp:scope
# retrieves the scope from the payload
scope(request) := request.payload.scope

## dcp:participant
# retrieves the participant from the payload
participant(request) := request.subject.identity