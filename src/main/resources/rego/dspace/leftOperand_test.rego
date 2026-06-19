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

# ---------- membership_type() with "vc" as single object ----------
test_membership_type_vc_single_object if {
	subject := {"claims": {"vc": {
		"type": ["VerifiableCredential", "MembershipCredential"],
		"credentialSubject": [{"membershipType": "Full"}],
	}}}
	leftOperand.membership_type(subject) == "Full"
}

test_membership_type_vc_single_object_no_membership if {
	subject := {"claims": {"vc": {
		"type": ["VerifiableCredential"],
		"credentialSubject": [{"membershipType": "Full"}],
	}}}
	not leftOperand.membership_type(subject)
}

# ---------- membership_type() with "verifiableCredential" as array ----------
test_membership_type_verifiable_credential_array if {
	subject := {"claims": {"verifiableCredential": [
		{"type": ["VerifiableCredential", "MembershipCredential"], "credentialSubject": [{"membershipType": "Associate"}]},
	]}}
	leftOperand.membership_type(subject) == "Associate"
}

test_membership_type_verifiable_credential_array_multiple if {
	subject := {"claims": {"verifiableCredential": [
		{"type": ["VerifiableCredential"], "credentialSubject": [{"other": "data"}]},
		{"type": ["VerifiableCredential", "MembershipCredential"], "credentialSubject": [{"membershipType": "Premium"}]},
	]}}
	leftOperand.membership_type(subject) == "Premium"
}

test_membership_type_verifiable_credential_array_empty if {
	subject := {"claims": {"verifiableCredential": []}}
	not leftOperand.membership_type(subject)
}

test_membership_type_verifiable_credential_array_no_membership if {
	subject := {"claims": {"verifiableCredential": [
		{"type": ["VerifiableCredential"], "credentialSubject": [{"membershipType": "Full"}]},
	]}}
	not leftOperand.membership_type(subject)
}

# ---------- membership_type() with "verifiableCredential" as single object ----------
test_membership_type_verifiable_credential_single_object if {
	subject := {"claims": {"verifiableCredential": {
		"type": ["VerifiableCredential", "MembershipCredential"],
		"credentialSubject": [{"membershipType": "Gold"}],
	}}}
	leftOperand.membership_type(subject) == "Gold"
}

test_membership_type_verifiable_credential_single_object_no_membership if {
	subject := {"claims": {"verifiableCredential": {
		"type": ["VerifiableCredential"],
		"credentialSubject": [{"membershipType": "Gold"}],
	}}}
	not leftOperand.membership_type(subject)
}

# ---------- membership_type() with credentialSubject as single object ----------
test_membership_type_vc_array_credential_subject_object if {
	subject := {"claims": {"vc": [
		{"type": ["MembershipCredential"], "credentialSubject": {"id": "did:web:example", "membershipType": "Full"}},
	]}}
	leftOperand.membership_type(subject) == "Full"
}

test_membership_type_vc_single_credential_subject_object if {
	subject := {"claims": {"vc": {
		"type": ["MembershipCredential"],
		"credentialSubject": {"id": "did:web:example", "membershipType": "Associate"},
	}}}
	leftOperand.membership_type(subject) == "Associate"
}

test_membership_type_verifiable_credential_array_credential_subject_object if {
	subject := {"claims": {"verifiableCredential": [
		{"type": ["MembershipCredential"], "credentialSubject": {"id": "did:web:example", "membershipType": "Premium"}},
	]}}
	leftOperand.membership_type(subject) == "Premium"
}

test_membership_type_verifiable_credential_single_credential_subject_object if {
	subject := {"claims": {"verifiableCredential": {
		"type": ["MembershipCredential"],
		"credentialSubject": {"id": "did:web:fancy-marketplace.biz", "membershipType": "FullMember"},
	}}}
	leftOperand.membership_type(subject) == "FullMember"
}

test_membership_type_credential_subject_object_no_membership_type if {
	subject := {"claims": {"vc": [
		{"type": ["MembershipCredential"], "credentialSubject": {"id": "did:web:example"}},
	]}}
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
