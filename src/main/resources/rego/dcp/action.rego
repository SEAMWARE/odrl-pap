package dcp.action

import rego.v1

## dcp:read
# allows if the request scope is not a contract negotiation or transfer process
default is_read(payload) := false

is_read(payload) if {
	payload.scope != "contract.negotiation"
	payload.scope != "transfer.process"
}

## dcp:use
# allows any request scope unconditionally
is_use(_) := true
