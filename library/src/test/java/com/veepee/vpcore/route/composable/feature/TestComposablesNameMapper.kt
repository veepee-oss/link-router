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
package com.veepee.vpcore.route.composable.feature

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.veepee.vpcore.route.composable.route.TestComposableALink
import com.veepee.vpcore.route.composable.route.TestComposableBLink
import com.veepee.vpcore.route.composable.route.TestComposableBLinkEvent
import com.veepee.vpcore.route.composable.route.TestComposableName
import com.veepee.vpcore.route.link.compose.ComposableEvent
import com.veepee.vpcore.route.link.compose.ComposableLink
import com.veepee.vpcore.route.link.compose.ComposableNameMapper
import com.veepee.vpcore.route.link.compose.events.LocalLinkRouterEventHandler

object TestComposablesNameMapper : ComposableNameMapper<TestComposableName> {
    override val supportedNames: Array<TestComposableName> = TestComposableName.values()

    @Composable
    override fun Map(link: ComposableLink<TestComposableName, ComposableEvent>, modifier: Modifier) {
        val handler = LocalLinkRouterEventHandler.current
        when (link) {
            is TestComposableALink -> TestComposableA(modifier = modifier)
            is TestComposableBLink -> TestComposableB(
                link.parameter.message,
                modifier = modifier.clickable {
                    handler.publish(TestComposableBLinkEvent("bar"))
                }
            )
        }
    }
}
