package system

import data.json.action as json_action
import data.json.leftOperand as json_lo
import data.odrl.operator as odrl_operator
import data.odrl.target as odrl_target
import data.utils.generic as generic
import data.utils.helper as helper
import data.vc.assignee as vc_assignee
import rego.v1

is_allowed if {
odrl_target.is_target(helper.target,"urn:example:resource:2")
json_action.is_use(generic.payload)
vc_assignee.is_any
odrl_operator.eq_operator(json_lo.payload_type(generic.payload),"document")
}
