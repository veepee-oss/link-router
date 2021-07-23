package com.veepee.vpcore.route.activity.route

import com.veepee.vpcore.route.link.ParcelableParameter
import com.veepee.vpcore.route.link.activity.ActivityLink

internal object TestActivityALink : ActivityLink<TestActivityName> {
    override val activityName: TestActivityName = TestActivityName.ActivityA
    override val parameter: ParcelableParameter? = null
}
