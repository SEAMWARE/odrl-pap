package system

import data.dcp.action as dcp_action
import data.dcp.leftOperand as dcp_lo
import data.odrl.operator as odrl_operator
import data.odrl.target as odrl_target
import data.utils.generic as generic
import data.utils.helper as helper
import data.vc.assignee as vc_assignee
import rego.v1

is_allowed if {
odrl_target.is_target(helper.target,"urn:example:asset:3")
dcp_action.is_use(generic.payload)
vc_assignee.is_any
odrl_operator.eq_operator(dcp_lo.membership_type(generic.subject),"full")
}
