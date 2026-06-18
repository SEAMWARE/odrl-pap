package dspace.leftOperand_test

import data.dspace.leftOperand

# ---------- membership_type() ----------
test_membership_type_single_vc if {
	subject := {"claims": {"vc": [
		{"type": ["VerifiableCredential", "MembershipCredential"], "credentialSubject": [{"membershipType": "Full"}]},
	]}}
	leftOperand.membership_type(subject) == "Full"
}

test_membership_type_multiple_vcs if {
	subject := {"claims": {"vc": [
		{"type": ["VerifiableCredential"], "credentialSubject": [{"other": "data"}]},
		{"type": ["VerifiableCredential", "MembershipCredential"], "credentialSubject": [{"membershipType": "Associate"}]},
	]}}
	leftOperand.membership_type(subject) == "Associate"
}

test_membership_type_no_membership_credential if {
	subject := {"claims": {"vc": [
		{"type": ["VerifiableCredential"], "credentialSubject": [{"membershipType": "Full"}]},
	]}}
	not leftOperand.membership_type(subject)
}

test_membership_type_empty_vcs if {
	subject := {"claims": {"vc": []}}
	not leftOperand.membership_type(subject)
}

# ---------- scope() ----------
test_scope_returns_scope if {
	request := {"payload": {"scope": "catalog"}}
	leftOperand.scope(request) == "catalog"
}

test_scope_returns_contract_negotiation if {
	request := {"payload": {"scope": "contract.negotiation"}}
	leftOperand.scope(request) == "contract.negotiation"
}

# ---------- participant() ----------
test_participant_returns_identity if {
	request := {"subject": {"identity": "did:example:org1"}}
	leftOperand.participant(request) == "did:example:org1"
}
