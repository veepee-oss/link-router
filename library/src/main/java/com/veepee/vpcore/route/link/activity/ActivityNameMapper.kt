package com.veepee.vpcore.route.link.activity

import android.app.Activity

interface ActivityNameMapper<T : ActivityName> {
    val supportedNames: Array<T>
    fun map(activityLink: ActivityLink<T>): Class<out Activity>
}
