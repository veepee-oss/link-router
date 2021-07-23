package com.veepee.routes.feature_a

import com.veepee.vpcore.route.link.ParcelableParameter
import com.veepee.vpcore.route.link.activity.ActivityLink

object ActivityALink : ActivityLink<FeatureAActivityNames> {
    override val activityName: FeatureAActivityNames = FeatureAActivityNames.ActivityA
    override val parameter: ParcelableParameter? = null
}
