package com.veepee.feature.b.routes

import android.app.Activity
import com.veepee.feature.b.BActivity
import com.veepee.routes.feature_b.FeatureBActivityNames
import com.veepee.vpcore.route.link.activity.ActivityLink
import com.veepee.vpcore.route.link.activity.ActivityNameMapper

object FeatureBActivityNameMapper : ActivityNameMapper<FeatureBActivityNames> {
    override val supportedNames: Array<FeatureBActivityNames> = FeatureBActivityNames.values()

    override fun map(activityLink: ActivityLink<FeatureBActivityNames>): Class<out Activity> {
        return when (activityLink.activityName) {
            FeatureBActivityNames.ActivityB -> BActivity::class.java
        }
    }
}
