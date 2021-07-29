package com.veepee.feature.b.routes

import com.veepee.routes.Schemas
import com.veepee.routes.feature_a.ActivityALink
import com.veepee.routes.feature_b.ActivityBLink
import com.veepee.routes.feature_b.ActivityBParameter
import com.veepee.vpcore.route.link.activity.ActivityLink
import com.veepee.vpcore.route.link.activity.ActivityName
import com.veepee.vpcore.route.link.deeplink.DeepLink
import com.veepee.vpcore.route.link.deeplink.DeepLinkMapper
import com.veepee.vpcore.route.link.deeplink.Scheme
import com.veepee.vpcore.route.link.deeplink.UriDeepLink

// MyApp://featureB/someId
object FeatureBDeepLinkMapper : DeepLinkMapper<UriDeepLink> {
    override val supportedSchemes: Array<out Scheme> = Schemas.values()

    override val supportedAuthority: String = "featureB"

    override fun stack(deepLink: UriDeepLink): Array<ActivityLink<ActivityName>> {
        val id = deepLink.pathSegments.last()
        return arrayOf(
            ActivityALink,
            ActivityBLink(ActivityBParameter(id))
        )
    }

    override fun canHandle(deepLink: DeepLink): Boolean {
        return super.canHandle(deepLink) && deepLink is UriDeepLink
    }
}
