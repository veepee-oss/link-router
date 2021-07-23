package com.veepee.vpcore.route.activity.route

import com.veepee.vpcore.route.link.ParcelableParameter
import com.veepee.vpcore.route.link.activity.ActivityLink
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class TestActivityBParameter(val id: String) : ParcelableParameter

internal data class TestActivityBLink(
    override val parameter: TestActivityBParameter
) : ActivityLink<TestActivityName> {
    override val activityName: TestActivityName = TestActivityName.ActivityB
}
