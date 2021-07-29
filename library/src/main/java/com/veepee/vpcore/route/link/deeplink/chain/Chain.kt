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
package com.veepee.vpcore.route.link.deeplink.chain

import com.veepee.vpcore.route.link.deeplink.DeepLink
import com.veepee.vpcore.route.link.deeplink.DeepLinkMapper

interface Chain {
    fun next(deepLinkMapper: DeepLinkMapper<out DeepLink>, deepLink: DeepLink): DeepLink
}

interface ChainFactory {
    fun create(): Chain
}

internal object DefaultInterceptor : DeepLinkInterceptor {
    override fun intercept(
        chain: Chain,
        deepLinkMapper: DeepLinkMapper<out DeepLink>,
        deepLink: DeepLink
    ): DeepLink {
        return deepLink
    }
}

internal class ChainFactoryImpl(
    private val initialInterceptors: List<DeepLinkInterceptor>
) : ChainFactory {
    override fun create(): Chain {
        return ChainImpl(initialInterceptors + DefaultInterceptor)
    }
}

internal class ChainImpl(private val interceptors: List<DeepLinkInterceptor>) : Chain {

    private var currentIndex = -1

    override fun next(deepLinkMapper: DeepLinkMapper<out DeepLink>, deepLink: DeepLink): DeepLink {
        return interceptors[++currentIndex].intercept(this, deepLinkMapper, deepLink)
    }
}
