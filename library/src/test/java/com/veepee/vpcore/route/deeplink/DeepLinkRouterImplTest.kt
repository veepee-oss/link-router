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
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DeepLinkRouterImplTest {
    private val chainFactory: ChainFactory<DeepLinkMapper<out DeepLink>, DeepLink> = mockk(relaxed = true)
    private val chain: Chain<DeepLinkMapper<out DeepLink>, DeepLink> = mockk(relaxed = true)
    private val activityLinkRouter: ActivityLinkRouter = mockk(relaxed = true)
    private val stackBuilderFactory: StackBuilderFactory = mockk(relaxed = true)
    private val stackBuilder: StackBuilder = mockk(relaxed = true)
    private val context: Context = mockk(relaxed = true)
    private val intentA: Intent = mockk(relaxed = true)
    private val intentB: Intent = mockk(relaxed = true)
    private val deepLink: DeepLink = mockk(relaxed = true)

    private val mappers: Set<DeepLinkMapper<DeepLink>> =
        setOf(GoogleUriDeepLinkMapper, BingUriDeepLinkMapper)

    @Before
    fun setup() {
        every { stackBuilderFactory.create(any()) } returns stackBuilder
        every { activityLinkRouter.intentFor(context, TestActivityALink) } returns intentA
        every {
            activityLinkRouter.intentFor(
                context,
                TestActivityBLink(TestActivityBParameter("www.google.com"))
            )
        } returns intentB

        every { chainFactory.create() } returns chain
        every { chain.next(any(), any()) } returns deepLink
    }

    @Test
    fun `should route DeepLink`() {
        val router = DeepLinkRouterImpl(
            initialDeepLinkMappers = mappers,
            activityLinkRouter = activityLinkRouter,
            stackBuilderFactory = stackBuilderFactory,
            chainFactory = chainFactory
        )

        every { deepLink.authority } returns "www.google.com"
        every { deepLink.scheme } returns TestScheme.MyScheme

        router.route(context, deepLink)

        verifyOrder {
            stackBuilder.addNextIntent(intentA)
            stackBuilder.addNextIntent(intentB)
            stackBuilder.startActivities()
        }
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

        verify(exactly = 1) { stackBuilderFactory.create(context) }
        verify(exactly = 1) { stackBuilder.addNextIntent(intentA) }
        verify(exactly = 1) { stackBuilder.addNextIntent(intentB) }
        verify(exactly = 1) { stackBuilder.startActivities() }
    }
}
