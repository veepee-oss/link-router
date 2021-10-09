package com.veepee.routes

import com.veepee.vpcore.route.link.deeplink.DeepLink
import com.veepee.vpcore.route.link.deeplink.DeepLinkMapper
import com.veepee.vpcore.route.link.deeplink.UriDeepLink

interface AuthenticatedDeepLinkMapper<T : DeepLink> : DeepLinkMapper<T>

interface AuthenticatedUriDeepLinkMapper : AuthenticatedDeepLinkMapper<UriDeepLink> {
    override fun canHandle(deepLink: DeepLink): Boolean {
        return super.canHandle(deepLink) && deepLink is UriDeepLink
    }
}