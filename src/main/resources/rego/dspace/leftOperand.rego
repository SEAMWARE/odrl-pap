package dspace.leftOperand

import rego.v1

## dspace:membershipType
# retrieves the membershipType from a MembershipCredential in the vc claims,
# handling both "vc" and "verifiableCredential" keys and both array and single-object forms
membership_type(subject) := result if {
	some vc in subject.claims.vc
	result := _membership_type_from_vc(vc)
}

membership_type(subject) := result if {
	is_object(subject.claims.vc)
	result := _membership_type_from_vc(subject.claims.vc)
}

membership_type(subject) := result if {
	some vc in subject.claims.verifiableCredential
	result := _membership_type_from_vc(vc)
}

membership_type(subject) := result if {
	is_object(subject.claims.verifiableCredential)
	result := _membership_type_from_vc(subject.claims.verifiableCredential)
}

# extracts membershipType from a single VC object of type MembershipCredential,
# handling credentialSubject as either an array or a single object
_membership_type_from_vc(vc) := result if {
	some t in vc.type
	t == "MembershipCredential"
	some cs in vc.credentialSubject
	result := cs.membershipType
}

_membership_type_from_vc(vc) := result if {
	some t in vc.type
	t == "MembershipCredential"
	is_object(vc.credentialSubject)
	result := vc.credentialSubject.membershipType
}

## dspace:scope
# retrieves the scope from the payload
scope(request) := request.payload.scope

## dspace:participant
# retrieves the participant from the payload
participant(request) := request.subject.identity