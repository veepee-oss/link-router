package com.veepee.vpcore.route.link.deeplink

import android.content.Context
import com.veepee.vpcore.route.link.activity.ActivityLinkRouter
import com.veepee.vpcore.route.link.deeplink.chain.ChainFactory

interface DeepLinkRouter {
    fun route(context: Context, deepLink: DeepLink)
}

internal class DeepLinkRouterImpl(
    initialDeepLinkMappers: Set<DeepLinkMapper<out DeepLink>>,
    private val activityRouter: ActivityLinkRouter,
    private val stackBuilderFactory: StackBuilderFactory,
    private val chainFactory: ChainFactory
) : DeepLinkRouter {

    @Suppress("UNCHECKED_CAST")
    private val deepLinkMappers: Set<DeepLinkMapper<DeepLink>> =
        initialDeepLinkMappers.map { it as DeepLinkMapper<DeepLink> }.toSet()

    override fun route(context: Context, deepLink: DeepLink) {
        val chain = chainFactory.create()
        val stackBuilder = stackBuilderFactory.create(context)
        val mapper = deepLinkMappers.firstOrNull { mapper -> mapper.canHandle(deepLink) }
            ?: throw NoDeepLinkMapperException(deepLink)

        val newDeepLink = chain.next(mapper, deepLink)
        if (newDeepLink != deepLink) {
            route(context, newDeepLink)
            return
        }

        mapper.stack(deepLink)
            .map { activityLink -> activityRouter.intentFor(context, activityLink) }
            .forEach { intent -> stackBuilder.addNextIntent(intent) }

        stackBuilder.startActivities()
    }
}
