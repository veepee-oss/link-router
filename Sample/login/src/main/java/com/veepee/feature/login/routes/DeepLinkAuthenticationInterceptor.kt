package com.veepee.feature.login.routes

import com.veepee.routes.AuthenticatedDeepLinkMapper
import com.veepee.routes.LoginStatus
import com.veepee.vpcore.route.link.deeplink.DeepLink
import com.veepee.vpcore.route.link.deeplink.DeepLinkMapper
import com.veepee.vpcore.route.link.deeplink.chain.DeepLinkInterceptor
import com.veepee.vpcore.route.link.interceptor.Chain


object DeepLinkAuthenticationInterceptor : DeepLinkInterceptor {

    override fun intercept(
        chain: Chain<DeepLinkMapper<out DeepLink>, DeepLink>,
        mapper: DeepLinkMapper<out DeepLink>,
        link: DeepLink
    ): DeepLink {
        if (mapper is AuthenticatedDeepLinkMapper) {
            if (!LoginStatus.isLogged) {
                return LoginDeepLink(link)
            }
        }
        return chain.next(mapper, link)
    }
}