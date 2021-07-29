package com.veepee.feature.a.routes

import com.veepee.routes.Schemas
import com.veepee.routes.feature_a.ActivityALink
import com.veepee.vpcore.route.link.activity.ActivityLink
import com.veepee.vpcore.route.link.activity.ActivityName
import com.veepee.vpcore.route.link.deeplink.DeepLink
import com.veepee.vpcore.route.link.deeplink.DeepLinkMapper
import com.veepee.vpcore.route.link.deeplink.Scheme
import com.veepee.vpcore.route.link.deeplink.UriDeepLink

// MyApp://featureA
object FeatureADeepLinkMapper : DeepLinkMapper<UriDeepLink> {
    override val supportedSchemes: Array<out Scheme> = Schemas.values()

    override val supportedAuthority: String = "featureA"

    override fun stack(deepLink: UriDeepLink): Array<ActivityLink<ActivityName>> {
        return arrayOf(ActivityALink)
    }

    override fun canHandle(deepLink: DeepLink): Boolean {
        return super.canHandle(deepLink) && deepLink is UriDeepLink
    }
}
