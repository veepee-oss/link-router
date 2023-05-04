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
import com.veepee.vpcore.route.link.activity.ActivityLinkRouterBuilder
import com.veepee.vpcore.route.link.activity.ActivityName
import com.veepee.vpcore.route.link.activity.ActivityNameMapper
import com.veepee.vpcore.route.link.activity.chain.ActivityLinkInterceptor
import com.veepee.vpcore.route.link.compose.ComposableLinkRouter
import com.veepee.vpcore.route.link.compose.ComposableName
import com.veepee.vpcore.route.link.compose.ComposableNameMapper
import com.veepee.vpcore.route.link.compose.ComposeLinkRouterBuilder
import com.veepee.vpcore.route.link.compose.chain.ComposableLinkInterceptor
import com.veepee.vpcore.route.link.deeplink.DeepLink
import com.veepee.vpcore.route.link.deeplink.DeepLinkMapper
import com.veepee.vpcore.route.link.deeplink.DeepLinkRouter
import com.veepee.vpcore.route.link.deeplink.DeepLinkRouterBuilder
import com.veepee.vpcore.route.link.deeplink.StackBuilderFactory
import com.veepee.vpcore.route.link.deeplink.chain.DeepLinkInterceptor
import com.veepee.vpcore.route.link.fragment.FragmentLinkRouter
import com.veepee.vpcore.route.link.fragment.FragmentLinkRouterBuilder
import com.veepee.vpcore.route.link.fragment.FragmentName
import com.veepee.vpcore.route.link.fragment.FragmentNameMapper
import com.veepee.vpcore.route.link.fragment.chain.FragmentLinkInterceptor

/**
 * Router delegates detail implementations to other implementations, but is the responsible for
 * retrieving Intents or Fragments and for routing DeepLinks.
 * */
interface LinkRouter :
    DeepLinkRouter,
    ActivityLinkRouter,
    FragmentLinkRouter,
    ComposableLinkRouter {

    interface Builder {
        fun add(activityNameMapper: ActivityNameMapper<out ActivityName>): Builder
        fun add(activityLinkInterceptor: ActivityLinkInterceptor): Builder

        fun add(fragmentNameMapper: FragmentNameMapper<out FragmentName>): Builder
        fun add(fragmentLinkInterceptor: FragmentLinkInterceptor): Builder

        fun add(deepLinkMapper: DeepLinkMapper<out DeepLink>): Builder
        fun add(deepLinkInterceptor: DeepLinkInterceptor): Builder

        fun add(composableLinkInterceptor: ComposableLinkInterceptor): Builder
        fun add(composableNameMapper: ComposableNameMapper<out ComposableName>): Builder

        fun newBuilder(): Builder
        fun build(): LinkRouter
        fun setStackBuilderFactory(stackBuilderFactory: StackBuilderFactory): Builder
    }
}

val GlobalRouterBuilder: LinkRouterBuilder = LinkRouterBuilder(
    activityLinkRouterBuilder = ActivityLinkRouterBuilder(),
    fragmentLinkRouterBuilder = FragmentLinkRouterBuilder(),
    deepLinkRouterBuilder = DeepLinkRouterBuilder(),
    composableLinkRouterBuilder = ComposeLinkRouterBuilder()
)

internal class LinkRouterImpl(
    private val deepLinkRouter: DeepLinkRouter,
    private val activityLinkRouter: ActivityLinkRouter,
    private val fragmentLinkRouter: FragmentLinkRouter,
    private val composableLinkRouter: ComposableLinkRouter,
) : LinkRouter,
    DeepLinkRouter by deepLinkRouter,
    ActivityLinkRouter by activityLinkRouter,
    FragmentLinkRouter by fragmentLinkRouter,
    ComposableLinkRouter by composableLinkRouter

class LinkRouterBuilder(
    private val activityLinkRouterBuilder: ActivityLinkRouter.Builder,
    private val fragmentLinkRouterBuilder: FragmentLinkRouter.Builder,
    private val deepLinkRouterBuilder: DeepLinkRouter.Builder,
    private val composableLinkRouterBuilder: ComposableLinkRouter.Builder

) : LinkRouter.Builder {

    override fun add(activityNameMapper: ActivityNameMapper<out ActivityName>): LinkRouter.Builder {
        activityLinkRouterBuilder.add(activityNameMapper)
        return this
    }

    override fun add(activityLinkInterceptor: ActivityLinkInterceptor): LinkRouter.Builder {
        activityLinkRouterBuilder.add(activityLinkInterceptor)
        return this
    }

    override fun add(fragmentNameMapper: FragmentNameMapper<out FragmentName>): LinkRouter.Builder {
        fragmentLinkRouterBuilder.add(fragmentNameMapper)
        return this
    }

    override fun add(fragmentLinkInterceptor: FragmentLinkInterceptor): LinkRouter.Builder {
        fragmentLinkRouterBuilder.add(fragmentLinkInterceptor)
        return this
    }

    override fun add(deepLinkMapper: DeepLinkMapper<out DeepLink>): LinkRouter.Builder {
        deepLinkRouterBuilder.add(deepLinkMapper)
        return this
    }

    override fun add(deepLinkInterceptor: DeepLinkInterceptor): LinkRouter.Builder {
        deepLinkRouterBuilder.add(deepLinkInterceptor)
        return this
    }

    override fun add(composableNameMapper: ComposableNameMapper<out ComposableName>): LinkRouter.Builder {
        composableLinkRouterBuilder.add(composableNameMapper)
        return this
    }

    override fun add(composableLinkInterceptor: ComposableLinkInterceptor): LinkRouter.Builder {
        composableLinkRouterBuilder.add(composableLinkInterceptor)
        return this
    }

    override fun setStackBuilderFactory(stackBuilderFactory: StackBuilderFactory): LinkRouter.Builder {
        deepLinkRouterBuilder.setStackBuilderFactory(stackBuilderFactory)
        return this
    }

    override fun newBuilder(): LinkRouter.Builder {
        return LinkRouterBuilder(
            activityLinkRouterBuilder.newBuilder(),
            fragmentLinkRouterBuilder.newBuilder(),
            deepLinkRouterBuilder.newBuilder(),
            composableLinkRouterBuilder.newBuilder()
        )
    }

    override fun build(): LinkRouter {
        val activityLinkRouter = activityLinkRouterBuilder.build()
        val fragmentLinkRouter = fragmentLinkRouterBuilder.build()
        val deepLinkRouter = deepLinkRouterBuilder.build(activityLinkRouter)
        val composableLinkRouter = composableLinkRouterBuilder.build()
        return LinkRouterImpl(
            deepLinkRouter = deepLinkRouter,
            activityLinkRouter = activityLinkRouter,
            fragmentLinkRouter = fragmentLinkRouter,
            composableLinkRouter = composableLinkRouter,
        )
    }
}
