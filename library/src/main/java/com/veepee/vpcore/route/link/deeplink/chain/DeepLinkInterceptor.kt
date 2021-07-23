package com.veepee.vpcore.route.link.deeplink.chain

import com.veepee.vpcore.route.link.deeplink.DeepLink
import com.veepee.vpcore.route.link.deeplink.DeepLinkMapper

interface DeepLinkInterceptor {
    fun intercept(chain: Chain, deepLinkMapper: DeepLinkMapper<out DeepLink>, deepLink: DeepLink): DeepLink
}
