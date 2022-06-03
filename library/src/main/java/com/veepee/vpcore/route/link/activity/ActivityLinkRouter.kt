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
package com.veepee.vpcore.route.link.activity

import android.content.Context
import android.content.Intent
import com.veepee.vpcore.route.link.activity.chain.ActivityLinkInterceptor
import com.veepee.vpcore.route.link.interceptor.ChainFactory
import com.veepee.vpcore.route.link.interceptor.ChainFactoryImpl
import com.veepee.vpcore.route.setLinkParameter

interface ActivityLinkRouter {
    fun intentFor(context: Context, activityLink: ActivityLink<ActivityName>): Intent

    interface Builder {
        fun add(activityLinkInterceptor: ActivityLinkInterceptor): Builder
        fun add(activityNameMapper: ActivityNameMapper<out ActivityName>): Builder
        fun newBuilder(): Builder
        fun build(): ActivityLinkRouter
    }
}

class ActivityLinkRouterBuilder(
    private val activityNameMappersRegistry: MutableSet<ActivityNameMapper<out ActivityName>> = mutableSetOf(),
    private val activityLinkInterceptorsRegistry: MutableList<ActivityLinkInterceptor> = mutableListOf()
) : ActivityLinkRouter.Builder {

    override fun add(activityNameMapper: ActivityNameMapper<out ActivityName>): ActivityLinkRouter.Builder {
        activityNameMappersRegistry.add(activityNameMapper)
        return this
    }

    override fun add(activityLinkInterceptor: ActivityLinkInterceptor): ActivityLinkRouter.Builder {
        activityLinkInterceptorsRegistry.add(activityLinkInterceptor)
        return this
    }

    override fun newBuilder(): ActivityLinkRouter.Builder {
        return ActivityLinkRouterBuilder(
            activityNameMappersRegistry.toMutableSet(),
            activityLinkInterceptorsRegistry.toMutableList()
        )
    }

    override fun build(): ActivityLinkRouter {
        return ActivityLinkRouterImpl(
            activityNameMappersRegistry.toSet(),
            ChainFactoryImpl(activityLinkInterceptorsRegistry.toList())
        )
    }
}

@Suppress("UNCHECKED_CAST")
internal class ActivityLinkRouterImpl(
    activityMappers: Set<ActivityNameMapper<out ActivityName>>,
    private val chainFactory: ChainFactory<ActivityNameMapper<out ActivityName>, ActivityLink<ActivityName>>
) : ActivityLinkRouter {

    // Creates a map of ActivityNames and ActivityNameMappers.
    // This takes more memory, but the routing will be faster.
    private val activityLinkMappers by lazy {
        activityMappers.flatMap { mapper ->
            mapper.supportedNames.map { activityName ->
                activityName to mapper as ActivityNameMapper<ActivityName>
            }
        }.toMap()
    }

    override fun intentFor(context: Context, activityLink: ActivityLink<ActivityName>): Intent {
        val chain = chainFactory.create()

        val mapper =
            activityLinkMappers[activityLink.activityName] ?: throw NoActivityNameMapperException(
                activityLink
            )

        val newActivityLink = chain.next(mapper, activityLink)
        if (newActivityLink != activityLink) {
            return intentFor(context, newActivityLink)
        }
        val activityClass = mapper.map(activityLink)
        return Intent(context, activityClass).setLinkParameter(activityLink.parameter)
    }
}
