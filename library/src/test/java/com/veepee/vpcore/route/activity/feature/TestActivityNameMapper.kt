package com.veepee.vpcore.route.activity.feature

import android.app.Activity
import com.veepee.vpcore.route.activity.route.TestActivityName
import com.veepee.vpcore.route.activity.route.TestActivityName.ActivityA
import com.veepee.vpcore.route.activity.route.TestActivityName.ActivityB
import com.veepee.vpcore.route.link.activity.ActivityLink
import com.veepee.vpcore.route.link.activity.ActivityNameMapper

internal object TestActivityNameMapper : ActivityNameMapper<TestActivityName> {
    override val supportedNames: Array<TestActivityName> = TestActivityName.values()

    override fun map(activityLink: ActivityLink<TestActivityName>): Class<out Activity> {
        return when (activityLink.activityName) {
            ActivityA -> {
                TestActivityA::class.java
            }
            ActivityB -> {
                TestActivityB::class.java
            }
        }
    }
}
