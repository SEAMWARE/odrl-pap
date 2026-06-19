package system

import data.dspace.action as dspace_action
import data.odrl.target as odrl_target
import data.utils.generic as generic
import data.utils.helper as helper
import data.vc.assignee as vc_assignee
import rego.v1

is_allowed if {
odrl_target.is_target(helper.target,"urn:example:asset:2")
dspace_action.is_use(generic.payload)
vc_assignee.is_any
}
