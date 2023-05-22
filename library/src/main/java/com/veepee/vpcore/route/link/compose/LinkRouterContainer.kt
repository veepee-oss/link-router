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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import com.veepee.vpcore.route.LinkRouter
import com.veepee.vpcore.route.link.compose.events.LinkRouterEventHandlerContainer

val LocalLinkRouter = staticCompositionLocalOf<LinkRouter> {
    error("no local Router provided")
}

val LocalComposableLinkRouter = staticCompositionLocalOf<ComposableLinkRouter> {
    error("no local Router provided")
}

@Composable
fun LinkRouterContainer(
    router: LinkRouter,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalLinkRouter provides router,
    ) {
        ComposableLinkRouterContainer(router, content)
    }
}

@Composable
fun ComposableLinkRouterContainer(
    router: ComposableLinkRouter,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalComposableLinkRouter provides router
    ) {
        content()
    }
}

@Composable
fun ComposableFor(link: ComposableLink<ComposableName>, modifier: Modifier = Modifier) {
    LocalComposableLinkRouter.current.ComposeFor(composableLink = link, modifier)
}

@Composable
inline fun <reified Event : ComposableEvent> ComposableFor(
    link: ComposableLinkWithEvent<ComposableName, Event>,
    modifier: Modifier = Modifier,
    noinline onEvent: (Event) -> Unit
) {
    LinkRouterEventHandlerContainer(onEvent = onEvent) {
        ComposableFor(link = link, modifier = modifier)
    }
}
