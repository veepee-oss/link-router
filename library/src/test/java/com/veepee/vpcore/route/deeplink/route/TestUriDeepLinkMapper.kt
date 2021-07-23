package com.veepee.vpcore.route.deeplink.route

import com.veepee.vpcore.route.activity.route.TestActivityALink
import com.veepee.vpcore.route.activity.route.TestActivityBLink
import com.veepee.vpcore.route.activity.route.TestActivityBParameter
import com.veepee.vpcore.route.deeplink.TestScheme
import com.veepee.vpcore.route.link.activity.ActivityLink
import com.veepee.vpcore.route.link.activity.ActivityName
import com.veepee.vpcore.route.link.deeplink.DeepLink
import com.veepee.vpcore.route.link.deeplink.DeepLinkMapper
import com.veepee.vpcore.route.link.deeplink.Scheme

internal object TestUriDeepLinkMapper : DeepLinkMapper<DeepLink> {
    override val supportedSchemes: Array<Scheme> = arrayOf(TestScheme.MyScheme)
    override val supportedAuthority: String = "www.google.com"

    override fun stack(deepLink: DeepLink): Array<ActivityLink<ActivityName>> {
        return arrayOf(
            TestActivityALink,
            TestActivityBLink(TestActivityBParameter(deepLink.authority))
        )
    }

    override fun canHandle(deepLink: DeepLink): Boolean {
        return deepLink.scheme in supportedSchemes
    }
}
