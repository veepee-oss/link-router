package com.veepee.vpcore.route.link.deeplink

import com.veepee.vpcore.route.link.activity.ActivityLink
import com.veepee.vpcore.route.link.activity.ActivityName

interface DeepLinkMapper<T : DeepLink> {
    val supportedSchemes: Array<out Scheme>
    val supportedAuthority: String

    fun stack(deepLink: T): Array<ActivityLink<ActivityName>>

    fun canHandle(deepLink: DeepLink): Boolean {
        return deepLink.scheme in supportedSchemes && deepLink.authority == supportedAuthority
    }
}
