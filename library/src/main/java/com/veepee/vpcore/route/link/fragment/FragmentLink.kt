package com.veepee.vpcore.route.link.fragment

import com.veepee.vpcore.route.link.Link
import com.veepee.vpcore.route.link.ParcelableParameter

interface FragmentLink<out T : FragmentName> : Link {
    val fragmentName: T
    override val parameter: ParcelableParameter?
}
