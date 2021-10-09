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
package com.veepee.vpcore.route.deeplink

import android.content.Context
import android.content.Intent
import com.veepee.vpcore.route.activity.route.TestActivityALink
import com.veepee.vpcore.route.activity.route.TestActivityBLink
import com.veepee.vpcore.route.activity.route.TestActivityBParameter
import com.veepee.vpcore.route.deeplink.route.TestUriDeepLinkMapper
import com.veepee.vpcore.route.link.activity.ActivityLinkRouter
import com.veepee.vpcore.route.link.deeplink.*
import com.veepee.vpcore.route.link.interceptor.Chain
import com.veepee.vpcore.route.link.interceptor.ChainFactory
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class DeepLinkRouterImplTest {
    private val chainFactory: ChainFactory<DeepLinkMapper<out DeepLink>, DeepLink> = mock()
    private val chain: Chain<DeepLinkMapper<out DeepLink>, DeepLink> = mock()
    private val activityLinkRouter: ActivityLinkRouter = mock()
    private val stackBuilderFactory: StackBuilderFactory = mock()
    private val stackBuilder: StackBuilder = mock()
    private val context: Context = mock()
    private val intentA: Intent = mock()
    private val intentB: Intent = mock()
    private val deepLink: DeepLink = mock()

    private val mappers: Set<DeepLinkMapper<DeepLink>> = setOf(TestUriDeepLinkMapper)

    @Before
    fun setup() {
        whenever(stackBuilderFactory.create(any())).thenReturn(stackBuilder)
        whenever(activityLinkRouter.intentFor(context, TestActivityALink))
            .thenReturn(intentA)
        whenever(
            activityLinkRouter.intentFor(
                context,
                TestActivityBLink(TestActivityBParameter("www.google.com"))
            )
        ).thenReturn(intentB)

        whenever(chainFactory.create()).thenReturn(chain)
        whenever(chain.next(any(), any())).thenReturn(deepLink)
    }

    @Test
    fun `should route DeepLink`() {
        val router = DeepLinkRouterImpl(
            mappers,
            activityLinkRouter,
            stackBuilderFactory,
            chainFactory
        )

        whenever(deepLink.authority).thenReturn("www.google.com")
        whenever(deepLink.scheme).thenReturn(TestScheme.MyScheme)

        router.route(context, deepLink)

        val order = inOrder(stackBuilder)

        order.verify(stackBuilder).addNextIntent(intentA)
        order.verify(stackBuilder).addNextIntent(intentB)
        order.verify(stackBuilder).startActivities()
    }

    @Test
    fun `should fail to route due missing mapper`() {
        val router = DeepLinkRouterImpl(
            emptySet(),
            activityLinkRouter,
            stackBuilderFactory,
            chainFactory
        )

        assertThrows(NoDeepLinkMapperException::class.java) {
            router.route(context, deepLink)
        }
    }
}
