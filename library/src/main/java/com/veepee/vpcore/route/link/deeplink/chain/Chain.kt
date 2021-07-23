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
