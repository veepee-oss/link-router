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
package com.veepee.vpcore.route.link.deeplink

import android.content.Context
import com.veepee.vpcore.route.link.activity.ActivityLinkRouter
import com.veepee.vpcore.route.link.deeplink.chain.DeepLinkInterceptor
import com.veepee.vpcore.route.link.interceptor.ChainFactory
import com.veepee.vpcore.route.link.interceptor.ChainFactoryImpl

interface DeepLinkRouter {
    fun route(context: Context, deepLink: DeepLink)

    interface Builder {
        fun add(deepLinkMapper: DeepLinkMapper<out DeepLink>): Builder
        fun add(priority: Int, deepLinkInterceptor: DeepLinkInterceptor): Builder
        fun newBuilder(): Builder
        fun build(activityLinkRouter: ActivityLinkRouter): DeepLinkRouter
        fun setStackBuilderFactory(stackBuilderFactory: StackBuilderFactory): Builder
    }
}

class DeepLinkRouterBuilder(
    private val deepLinkMappersRegistry: MutableSet<DeepLinkMapper<out DeepLink>> = mutableSetOf(),
    private val deepLinkInterceptorsRegistry: MutableMap<Int, List<DeepLinkInterceptor>> = mutableMapOf()
) : DeepLinkRouter.Builder {
    private var stackBuilderFactory: StackBuilderFactory = StackBuilderFactoryImpl()
    override fun add(deepLinkMapper: DeepLinkMapper<out DeepLink>): DeepLinkRouter.Builder {
        deepLinkMappersRegistry.add(deepLinkMapper)
        return this
    }

    override fun add(priority: Int, deepLinkInterceptor: DeepLinkInterceptor): DeepLinkRouter.Builder {
        val interceptors = deepLinkInterceptorsRegistry.getOrDefault(priority, emptyList())
            .toMutableList()
            .apply { add(deepLinkInterceptor) }
        deepLinkInterceptorsRegistry[priority] = interceptors
        return this
    }

    override fun setStackBuilderFactory(stackBuilderFactory: StackBuilderFactory): DeepLinkRouter.Builder {
        this.stackBuilderFactory = stackBuilderFactory
        return this
    }

    override fun newBuilder(): DeepLinkRouter.Builder {
        return DeepLinkRouterBuilder(
            deepLinkMappersRegistry.toMutableSet(),
            deepLinkInterceptorsRegistry.toMutableMap()
        )
    }

    override fun build(activityLinkRouter: ActivityLinkRouter): DeepLinkRouter {
        return DeepLinkRouterImpl(
            initialDeepLinkMappers = deepLinkMappersRegistry.toSet(),
            activityLinkRouter = activityLinkRouter,
            stackBuilderFactory = stackBuilderFactory,
            chainFactory = ChainFactoryImpl(deepLinkInterceptorsRegistry.toSortedMap().values.flatten())
        )
    }
}

internal class DeepLinkRouterImpl(
    initialDeepLinkMappers: Set<DeepLinkMapper<out DeepLink>>,
    private val activityLinkRouter: ActivityLinkRouter,
    private val stackBuilderFactory: StackBuilderFactory,
    private val chainFactory: ChainFactory<DeepLinkMapper<out DeepLink>, DeepLink>
) : DeepLinkRouter {

    @Suppress("UNCHECKED_CAST")
    private val deepLinkMappers: Set<DeepLinkMapper<DeepLink>> =
        initialDeepLinkMappers.map { it as DeepLinkMapper<DeepLink> }.toSet()

    override fun route(context: Context, deepLink: DeepLink) {
        val mapper = deepLinkMappers.firstOrNull { mapper -> mapper.canHandle(deepLink) }
            ?: throw NoDeepLinkMapperException(deepLink)

        val chain = chainFactory.create()
        val newDeepLink = chain.next(mapper, deepLink)
        if (newDeepLink != deepLink) {
            route(context, newDeepLink)
            return
        }

        val stackBuilder = stackBuilderFactory.create(context)
        mapper.stack(deepLink)
            .map { activityLink -> activityLinkRouter.intentFor(context, activityLink) }
            .forEach { intent -> stackBuilder.addNextIntent(intent) }

        stackBuilder.startActivities()
    }
}
