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
package com.veepee.vpcore.route

import com.veepee.vpcore.route.link.activity.ActivityLinkRouter
import com.veepee.vpcore.route.link.activity.ActivityLinkRouterImpl
import com.veepee.vpcore.route.link.activity.ActivityName
import com.veepee.vpcore.route.link.activity.ActivityNameMapper
import com.veepee.vpcore.route.link.activity.chain.ActivityLinkInterceptor
import com.veepee.vpcore.route.link.deeplink.*
import com.veepee.vpcore.route.link.deeplink.chain.DeepLinkInterceptor
import com.veepee.vpcore.route.link.fragment.FragmentLinkRouter
import com.veepee.vpcore.route.link.fragment.FragmentLinkRouterImpl
import com.veepee.vpcore.route.link.fragment.FragmentName
import com.veepee.vpcore.route.link.fragment.FragmentNameMapper
import com.veepee.vpcore.route.link.fragment.chain.FragmentLinkInterceptor
import com.veepee.vpcore.route.link.interceptor.ChainFactoryImpl

/**
 * Router delegates detail implementations to other implementations, but is the responsible for
 * retrieving Intents or Fragments and for routing DeepLinks.
 * */
interface Router : DeepLinkRouter, ActivityLinkRouter, FragmentLinkRouter {

    interface Builder {
        fun add(deepLinkMapper: DeepLinkMapper<out DeepLink>): Builder
        fun add(deepLinkInterceptor: DeepLinkInterceptor): Builder
        fun add(activityLinkInterceptor: ActivityLinkInterceptor): Builder
        fun add(fragmentLinkInterceptor: FragmentLinkInterceptor): Builder
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

    private val activityLinkInterceptorsRegistry = mutableListOf<ActivityLinkInterceptor>()

    private val fragmentLinkInterceptorsRegistry = mutableListOf<FragmentLinkInterceptor>()

    override fun add(deepLinkMapper: DeepLinkMapper<out DeepLink>): Router.Builder {
        deepLinkMappersRegistry.add(deepLinkMapper)
        return this
    }

    override fun add(deepLinkInterceptor: DeepLinkInterceptor): Router.Builder {
        deepLinkInterceptorsRegistry.add(deepLinkInterceptor)
        return this
    }

    override fun add(activityLinkInterceptor: ActivityLinkInterceptor): Router.Builder {
        activityLinkInterceptorsRegistry.add(activityLinkInterceptor)
        return this
    }

    override fun add(fragmentLinkInterceptor: FragmentLinkInterceptor): Router.Builder {
        fragmentLinkInterceptorsRegistry.add(fragmentLinkInterceptor)
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
        builder.activityLinkInterceptorsRegistry.addAll(activityLinkInterceptorsRegistry)
        builder.fragmentLinkInterceptorsRegistry.addAll(fragmentLinkInterceptorsRegistry)
        return builder
    }

    override fun build(): Router {
        val activityLinkRouter = ActivityLinkRouterImpl(
            activityNameMappersRegistry.toSet(),
            ChainFactoryImpl(
                activityLinkInterceptorsRegistry.toList()
            )
        )

        val fragmentLinkRouter = FragmentLinkRouterImpl(
            fragmentNameMappersRegistry.toSet(),
            ChainFactoryImpl(
                fragmentLinkInterceptorsRegistry.toList()
            )
        )

        val deepLinkRouter = DeepLinkRouterImpl(
            deepLinkMappersRegistry.toSet(),
            activityLinkRouter,
            StackBuilderFactoryImpl(),
            ChainFactoryImpl(
                deepLinkInterceptorsRegistry.toList()
            )
        )
        return RouterImpl(
            deepLinkRouter = deepLinkRouter,
            activityLinkRouter = activityLinkRouter,
            fragmentLinkRouter = fragmentLinkRouter
        )
    }
}
