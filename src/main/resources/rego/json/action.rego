package json.action

import rego.v1

## json:read
# checks if the action field in the JSON payload indicates a read operation
default is_read(payload) := false

is_read(payload) if payload.action == "read"

## json:write
# checks if the action field in the JSON payload indicates a write operation
default is_write(payload) := false

is_write(payload) if payload.action == "write"

## json:create
# checks if the action field in the JSON payload indicates a create operation
default is_create(payload) := false

is_create(payload) if payload.action == "create"

## json:delete
# checks if the action field in the JSON payload indicates a delete operation
default is_delete(payload) := false

is_delete(payload) if payload.action == "delete"

## json:use
# checks if the action field in the JSON payload indicates any usage operation
default is_use(payload) := false

is_use(payload) if payload.action in ["read", "write", "create", "delete", "use", "modify"]
