/**
 * Copyright (c) 2021, Veepee
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose
 * with or without fee is hereby  granted, provided that the above copyright notice
 * and this permission notice appear in all copies.
 *
 * THE SOFTWARE  IS PROVIDED "AS IS"  AND THE AUTHOR DISCLAIMS  ALL WARRANTIES WITH
 * REGARD TO THIS SOFTWARE INCLUDING  ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS.  IN NO  EVENT  SHALL THE  AUTHOR  BE LIABLE  FOR  ANY SPECIAL,  DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS
 * OF USE, DATA  OR PROFITS, WHETHER IN AN ACTION OF  CONTRACT, NEGLIGENCE OR OTHER
 * TORTIOUS ACTION, ARISING OUT OF OR  IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 */
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