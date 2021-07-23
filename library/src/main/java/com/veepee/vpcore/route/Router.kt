package com.veepee.vpcore.route

import com.veepee.vpcore.route.link.activity.ActivityLinkRouter
import com.veepee.vpcore.route.link.activity.ActivityLinkRouterImpl
import com.veepee.vpcore.route.link.activity.ActivityName
import com.veepee.vpcore.route.link.activity.ActivityNameMapper
import com.veepee.vpcore.route.link.deeplink.DeepLink
import com.veepee.vpcore.route.link.deeplink.DeepLinkMapper
import com.veepee.vpcore.route.link.deeplink.DeepLinkRouter
import com.veepee.vpcore.route.link.deeplink.DeepLinkRouterImpl
import com.veepee.vpcore.route.link.deeplink.StackBuilderFactoryImpl
import com.veepee.vpcore.route.link.deeplink.chain.ChainFactoryImpl
import com.veepee.vpcore.route.link.deeplink.chain.DeepLinkInterceptor
import com.veepee.vpcore.route.link.fragment.FragmentLinkRouter
import com.veepee.vpcore.route.link.fragment.FragmentLinkRouterImpl
import com.veepee.vpcore.route.link.fragment.FragmentName
import com.veepee.vpcore.route.link.fragment.FragmentNameMapper

/**
 * Router delegates detail implementations to other implementations, but is the responsible for
 * retrieving Intents or Fragments and for routing DeepLinks.
 * */
interface Router : DeepLinkRouter, ActivityLinkRouter, FragmentLinkRouter {

    interface Builder {
        fun add(deepLinkMapper: DeepLinkMapper<out DeepLink>): Builder
        fun add(deepLinkInterceptor: DeepLinkInterceptor): Builder
        fun add(activityNameMapper: ActivityNameMapper<out ActivityName>): Builder
        fun add(fragmentNameMapper: FragmentNameMapper<out FragmentName>): Builder
        fun newBuilder(): Builder
        fun build(): Router
    }
}

val GlobalRouterBuilder: RouterBuilder = RouterBuilder()

internal class RouterImpl(
    private val deepLinkRouter: DeepLinkRouter,
    private val activityLinkRouter: ActivityLinkRouter,
    private val fragmentLinkRouter: FragmentLinkRouter
) : Router,
    DeepLinkRouter by deepLinkRouter,
    ActivityLinkRouter by activityLinkRouter,
    FragmentLinkRouter by fragmentLinkRouter

class RouterBuilder : Router.Builder {
    private val activityNameMappersRegistry = mutableSetOf<ActivityNameMapper<out ActivityName>>()

    private val fragmentNameMappersRegistry = mutableSetOf<FragmentNameMapper<out FragmentName>>()

    private val deepLinkMappersRegistry = mutableSetOf<DeepLinkMapper<out DeepLink>>()

    private val deepLinkInterceptorsRegistry = mutableListOf<DeepLinkInterceptor>()

    override fun add(deepLinkMapper: DeepLinkMapper<out DeepLink>): Router.Builder {
        deepLinkMappersRegistry.add(deepLinkMapper)
        return this
    }

    override fun add(deepLinkInterceptor: DeepLinkInterceptor): Router.Builder {
        deepLinkInterceptorsRegistry.add(deepLinkInterceptor)
        return this
    }

    override fun add(activityNameMapper: ActivityNameMapper<out ActivityName>): Router.Builder {
        activityNameMappersRegistry.add(activityNameMapper)
        return this
    }

    override fun add(fragmentNameMapper: FragmentNameMapper<out FragmentName>): Router.Builder {
        fragmentNameMappersRegistry.add(fragmentNameMapper)
        return this
    }

    override fun newBuilder(): Router.Builder {
        val builder = RouterBuilder()
        builder.activityNameMappersRegistry.addAll(activityNameMappersRegistry)
        builder.fragmentNameMappersRegistry.addAll(fragmentNameMappersRegistry)
        builder.deepLinkMappersRegistry.addAll(deepLinkMappersRegistry)
        builder.deepLinkInterceptorsRegistry.addAll(deepLinkInterceptorsRegistry)
        return builder
    }

    override fun build(): Router {
        val activityLinkRouter = ActivityLinkRouterImpl(activityNameMappersRegistry.toSet())
        val fragmentLinkRouter = FragmentLinkRouterImpl(fragmentNameMappersRegistry.toSet())

        val deepLinkRouter = DeepLinkRouterImpl(
            deepLinkMappersRegistry.toSet(),
            activityLinkRouter,
            StackBuilderFactoryImpl(),
            ChainFactoryImpl(
                deepLinkInterceptorsRegistry
            )
        )
        return RouterImpl(
            deepLinkRouter = deepLinkRouter,
            activityLinkRouter = activityLinkRouter,
            fragmentLinkRouter = fragmentLinkRouter
        )
    }
}
