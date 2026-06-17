package system

import data.json.action as json_action
import data.odrl.target as odrl_target
import data.utils.generic as generic
import data.utils.helper as helper
import data.vc.assignee as vc_assignee
import rego.v1

is_allowed if {
odrl_target.is_target(helper.target,"urn:example:resource:1")
json_action.is_read(generic.payload)
vc_assignee.is_any
}
