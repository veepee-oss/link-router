package com.veepee.vpcore.route.link.activity

import com.veepee.vpcore.route.link.Link
import com.veepee.vpcore.route.link.ParcelableParameter

interface ActivityLink<out T : ActivityName> : Link {
    val activityName: T
    override val parameter: ParcelableParameter?
}
