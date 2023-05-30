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
package com.veepee.vpcore.route.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.veepee.vpcore.route.composable.feature.TestComposablesNameMapper
import com.veepee.vpcore.route.composable.route.TestComposableALink
import com.veepee.vpcore.route.composable.route.TestComposableBLink
import com.veepee.vpcore.route.composable.route.TestComposableBParameter
import com.veepee.vpcore.route.link.compose.ComposableEvent
import com.veepee.vpcore.route.link.compose.ComposableFor
import com.veepee.vpcore.route.link.compose.ComposableLink
import com.veepee.vpcore.route.link.compose.ComposableLinkRouterContainer
import com.veepee.vpcore.route.link.compose.ComposableLinkRouterImpl
import com.veepee.vpcore.route.link.compose.ComposableName
import com.veepee.vpcore.route.link.compose.ComposableNameMapper
import com.veepee.vpcore.route.link.compose.NoComposableNameMapperException
import com.veepee.vpcore.route.link.compose.chain.ComposableLinkInterceptor
import com.veepee.vpcore.route.link.interceptor.Chain
import com.veepee.vpcore.route.link.interceptor.ChainFactoryImpl
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(
    instrumentedPackages = ["androidx.loader.content"]
)
class ComposableLinkRouterTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val mappers: Set<ComposableNameMapper<out ComposableName>> =
        setOf(TestComposablesNameMapper)

    @Test
    fun `should fail to route due to missing composable name mapper`() {
        val router = ComposableLinkRouterImpl(emptySet(), ChainFactoryImpl(emptyList()))
        Assert.assertThrows(NoComposableNameMapperException::class.java) {
            composeTestRule.setContent {
                router.ComposeFor(TestComposableALink())
            }
        }
    }

    @Test
    fun `should route and display text within composable`() {
        val router = ComposableLinkRouterImpl(mappers, ChainFactoryImpl(emptyList()))

        composeTestRule.setContent {
            router.ComposeFor(TestComposableALink())
        }

        composeTestRule
            .onNodeWithText("Hello World")
            .assertIsDisplayed()
    }

    @Test
    fun `should route and display text given to composable`() {
        val router = ComposableLinkRouterImpl(mappers, ChainFactoryImpl(emptyList()))
        composeTestRule.setContent {
            router.ComposeFor(TestComposableBLink(TestComposableBParameter("message")))
        }

        composeTestRule
            .onNodeWithText("message")
            .assertIsDisplayed()
    }

    @Test
    fun `should route and consume result`() {
        val router = ComposableLinkRouterImpl(mappers, ChainFactoryImpl(emptyList()))
        var message = ""
        composeTestRule.setContent {
            ComposableLinkRouterContainer(router) {
                ComposableFor(
                    TestComposableBLink(TestComposableBParameter("message"))
                ) { event ->
                    message = event.foo
                }
            }
        }

        composeTestRule
            .onNodeWithText("message")
            .performClick()

        Assert.assertEquals(message, "bar")
    }

    @Test
    fun `should intercept route and display other composable`() {
        val router = ComposableLinkRouterImpl(
            mappers,
            ChainFactoryImpl(listOf(object : ComposableLinkInterceptor {
                override fun intercept(
                    chain: Chain<ComposableNameMapper<out ComposableName>, ComposableLink<ComposableName, ComposableEvent>>,
                    mapper: ComposableNameMapper<out ComposableName>,
                    link: ComposableLink<ComposableName, ComposableEvent>
                ): ComposableLink<ComposableName, ComposableEvent> {
                    if (link is TestComposableBLink) {
                        return TestComposableALink()
                    }
                    return chain.next(mapper, link)
                }
            }))
        )

        composeTestRule.setContent {
            router.ComposeFor(TestComposableBLink(TestComposableBParameter("message")))
        }

        composeTestRule
            .onNodeWithText("Hello World")
            .assertIsDisplayed()
    }
}
