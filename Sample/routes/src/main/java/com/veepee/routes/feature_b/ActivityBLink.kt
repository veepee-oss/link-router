package com.veepee.routes.feature_b

import com.veepee.vpcore.route.link.ParcelableParameter
import com.veepee.vpcore.route.link.activity.ActivityLink
import kotlinx.parcelize.Parcelize

class ActivityBLink(
    override val parameter: ActivityBParameter
) : ActivityLink<FeatureBActivityNames> {
    override val activityName: FeatureBActivityNames = FeatureBActivityNames.ActivityB
}

@Parcelize
data class ActivityBParameter(val id: String) : ParcelableParameter
