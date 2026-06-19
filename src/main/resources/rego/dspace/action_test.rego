package dspace.action_test

import data.dspace.action

# ---------- is_read() ----------
# Should return true if the scope is not contract.negotiation or transfer.process
test_is_read_true_catalog if {
	action.is_read({"scope": "catalog"})
}

test_is_read_true_arbitrary_scope if {
	action.is_read({"scope": "some.other.scope"})
}

# Should return false if the scope is contract.negotiation
test_is_read_false_contract_negotiation if {
	action.is_read({"scope": "contract.negotiation"}) == false
}

# Should return false if the scope is transfer.process
test_is_read_false_transfer_process if {
	action.is_read({"scope": "transfer.process"}) == false
}

# ---------- is_use() ----------
# Should return true for any scope
test_is_use_catalog if {
	action.is_use({"scope": "catalog"})
}

test_is_use_contract_negotiation if {
	action.is_use({"scope": "contract.negotiation"})
}

test_is_use_transfer_process if {
	action.is_use({"scope": "transfer.process"})
}

test_is_use_empty_object if {
	action.is_use({})
}
