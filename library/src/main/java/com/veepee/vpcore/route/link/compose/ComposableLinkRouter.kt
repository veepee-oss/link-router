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
package com.veepee.vpcore.route.link.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.veepee.vpcore.route.link.compose.chain.ComposableLinkInterceptor
import com.veepee.vpcore.route.link.interceptor.ChainFactory
import com.veepee.vpcore.route.link.interceptor.ChainFactoryImpl

interface ComposableLinkRouter {

    @Composable
    fun ComposeFor(link: ComposableLink<ComposableName, ComposableEvent>, modifier: Modifier)

    @Composable
    fun ComposeFor(link: ComposableLink<ComposableName, ComposableEvent>)

    interface Builder {
        fun add(composableNameMapper: ComposableNameMapper<out ComposableName>): Builder
        fun add(priority: Int, composableLinkInterceptor: ComposableLinkInterceptor): Builder
        fun newBuilder(): Builder
        fun build(): ComposableLinkRouter
    }
}

class ComposeLinkRouterBuilder(
    private val composableNameMappersRegistry: MutableSet<ComposableNameMapper<out ComposableName>> = mutableSetOf(),
    private val composableLinkInterceptorsRegistry: MutableMap<Int, List<ComposableLinkInterceptor>> = mutableMapOf()
) : ComposableLinkRouter.Builder {

    override fun add(
        priority: Int,
        composableLinkInterceptor: ComposableLinkInterceptor
    ): ComposableLinkRouter.Builder {
        val interceptors = composableLinkInterceptorsRegistry.getOrDefault(priority, emptyList())
            .toMutableList()
            .apply {
                add(composableLinkInterceptor)
            }
        composableLinkInterceptorsRegistry[priority] = interceptors
        return this
    }

    override fun add(composableNameMapper: ComposableNameMapper<out ComposableName>): ComposableLinkRouter.Builder {
        composableNameMappersRegistry.add(composableNameMapper)
        return this
    }

    override fun newBuilder(): ComposableLinkRouter.Builder {
        return ComposeLinkRouterBuilder(
            composableNameMappersRegistry.toMutableSet(),
            composableLinkInterceptorsRegistry.toMutableMap()
        )
    }

    override fun build(): ComposableLinkRouter {
        return ComposableLinkRouterImpl(
            composableNameMappersRegistry.toSet(),
            ChainFactoryImpl(composableLinkInterceptorsRegistry.toSortedMap().values.flatten())
        )
    }
}

@Suppress("UNCHECKED_CAST")
internal class ComposableLinkRouterImpl(
    composableNameMappers: Set<ComposableNameMapper<out ComposableName>>,
    private val chainFactory: ChainFactory<ComposableNameMapper<out ComposableName>, ComposableLink<ComposableName, ComposableEvent>>
) : ComposableLinkRouter {

    private val composableLinkMapper =
        composableNameMappers.flatMap { mapper ->
            mapper.supportedNames.map { composableName ->
                composableName to mapper as ComposableNameMapper<ComposableName>
            }
        }.toMap()

    @Composable
    override fun ComposeFor(link: ComposableLink<ComposableName, ComposableEvent>) {
        ComposeFor(link = link, modifier = Modifier)
    }

    @Composable
    override fun ComposeFor(link: ComposableLink<ComposableName, ComposableEvent>, modifier: Modifier) {
        val chain = chainFactory.create()
        val mapper = composableLinkMapper[link.composableName]
            ?: throw NoComposableNameMapperException(link)
        val newComposableLink = chain.next(mapper, link)
        if (newComposableLink != link) {
            return ComposeFor(newComposableLink, modifier)
        }
        mapper.Map(link, modifier)
    }
}
