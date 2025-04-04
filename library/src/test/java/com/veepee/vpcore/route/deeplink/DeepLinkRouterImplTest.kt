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
import android.net.Uri
import com.veepee.vpcore.route.GlobalRouterBuilder
import com.veepee.vpcore.route.activity.route.TestActivityALink
import com.veepee.vpcore.route.activity.route.TestActivityBLink
import com.veepee.vpcore.route.activity.route.TestActivityBParameter
import com.veepee.vpcore.route.deeplink.route.BingUriDeepLinkMapper
import com.veepee.vpcore.route.deeplink.route.GoogleUriDeepLinkMapper
import com.veepee.vpcore.route.link.activity.ActivityLinkRouter
import com.veepee.vpcore.route.link.deeplink.DeepLink
import com.veepee.vpcore.route.link.deeplink.DeepLinkMapper
import com.veepee.vpcore.route.link.deeplink.DeepLinkRouterImpl
import com.veepee.vpcore.route.link.deeplink.NoDeepLinkMapperException
import com.veepee.vpcore.route.link.deeplink.StackBuilder
import com.veepee.vpcore.route.link.deeplink.StackBuilderFactory
import com.veepee.vpcore.route.link.deeplink.StackBuilderImpl
import com.veepee.vpcore.route.link.deeplink.UriDeepLink
import com.veepee.vpcore.route.link.deeplink.UriParameter
import com.veepee.vpcore.route.link.deeplink.chain.DeepLinkInterceptor
import com.veepee.vpcore.route.link.interceptor.Chain
import com.veepee.vpcore.route.link.interceptor.ChainFactory
import com.veepee.vpcore.route.link.interceptor.ChainFactoryImpl
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
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

    private val mappers: Set<DeepLinkMapper<DeepLink>> =
        setOf(GoogleUriDeepLinkMapper, BingUriDeepLinkMapper)

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
            initialDeepLinkMappers = mappers,
            activityLinkRouter = activityLinkRouter,
            stackBuilderFactory = stackBuilderFactory,
            chainFactory = chainFactory
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
        GlobalRouterBuilder.newBuilder().setStackBuilderFactory {
            StackBuilderImpl(it)
        }.build()
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

    @Test
    fun `should intercept bing deeplink and forward to google deeplink`() {
        val interceptor = object : DeepLinkInterceptor {
            override fun intercept(
                chain: Chain<DeepLinkMapper<out DeepLink>, DeepLink>,
                mapper: DeepLinkMapper<out DeepLink>,
                link: DeepLink
            ): DeepLink {
                if (link.authority == "www.bing.com") {
                    return UriDeepLink(
                        UriParameter(Uri.parse("myscheme://www.google.com")),
                        TestScheme.MyScheme
                    )
                }
                return chain.next(mapper, link)
            }
        }
        val chainFactory = ChainFactoryImpl(listOf(interceptor))

        val router = DeepLinkRouterImpl(
            mappers,
            activityLinkRouter,
            stackBuilderFactory,
            chainFactory
        )

        router.route(
            context,
            UriDeepLink(
                UriParameter(Uri.parse("myscheme://www.bing.com")),
                TestScheme.MyScheme
            )
        )

        verify(stackBuilderFactory, times(1)).create(context)
        verify(stackBuilder, times(1)).addNextIntent(intentA)
        verify(stackBuilder, times(1)).addNextIntent(intentB)
        verify(stackBuilder, times(1)).startActivities()
    }
}
