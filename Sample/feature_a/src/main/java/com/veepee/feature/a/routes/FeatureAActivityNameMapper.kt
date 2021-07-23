package com.veepee.feature.a.routes

import android.app.Activity
import com.veepee.feature.a.AActivity
import com.veepee.routes.feature_a.FeatureAActivityNames
import com.veepee.vpcore.route.link.activity.ActivityLink
import com.veepee.vpcore.route.link.activity.ActivityNameMapper

object FeatureAActivityNameMapper : ActivityNameMapper<FeatureAActivityNames> {
    override val supportedNames: Array<FeatureAActivityNames> = FeatureAActivityNames.values()

    override fun map(activityLink: ActivityLink<FeatureAActivityNames>): Class<out Activity> {
        return when (activityLink.activityName) {
            FeatureAActivityNames.ActivityA -> AActivity::class.java
        }
    }
}
