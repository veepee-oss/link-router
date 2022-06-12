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
package com.veepee.vpcore.route.activity

import android.app.Activity
import com.veepee.vpcore.route.activity.feature.TestActivityA
import com.veepee.vpcore.route.activity.feature.TestActivityB
import com.veepee.vpcore.route.activity.feature.TestActivityNameMapper
import com.veepee.vpcore.route.activity.route.TestActivityALink
import com.veepee.vpcore.route.activity.route.TestActivityBLink
import com.veepee.vpcore.route.activity.route.TestActivityBParameter
import com.veepee.vpcore.route.link.activity.ActivityLink
import com.veepee.vpcore.route.link.activity.ActivityLinkRouterImpl
import com.veepee.vpcore.route.link.activity.ActivityName
import com.veepee.vpcore.route.link.activity.ActivityNameMapper
import com.veepee.vpcore.route.link.activity.NoActivityNameMapperException
import com.veepee.vpcore.route.link.activity.chain.ActivityLinkInterceptor
import com.veepee.vpcore.route.link.interceptor.Chain
import com.veepee.vpcore.route.link.interceptor.ChainFactoryImpl
import com.veepee.vpcore.route.requireLinkParameter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ActivityLinkRouterTest {

    private val mappers = setOf<ActivityNameMapper<out ActivityName>>(TestActivityNameMapper)
    private val activityBParameter = TestActivityBParameter("id")

    @Test
    fun `should return intent for registered ActivityNameMappers`() {

        assertIntentFor(TestActivityALink, TestActivityA::class.java)
        assertIntentFor(TestActivityBLink(activityBParameter), TestActivityB::class.java)
    }

    @Test
    fun `should retrieve parameter from intent`() {
        val activity: Activity = mock()
        val router = ActivityLinkRouterImpl(mappers, ChainFactoryImpl(emptyList()))
        val intent = router.intentFor(activity, TestActivityBLink(activityBParameter))
        val parameter = intent.requireLinkParameter<TestActivityBParameter>()
        assertEquals(parameter, activityBParameter)
    }

    @Test
    fun `should raise an error when the there is no mapper for a given ActivityLink`() {

        val router = ActivityLinkRouterImpl(emptySet(), ChainFactoryImpl(emptyList()))

        val activity: Activity = mock()
        assertThrows(NoActivityNameMapperException::class.java) {
            router.intentFor(activity, TestActivityALink)
        }
    }

    @Test
    fun `should intercept ActivityLink and return a different one`() {
        val interceptor = object : ActivityLinkInterceptor {
            override fun intercept(
                chain: Chain<ActivityNameMapper<out ActivityName>, ActivityLink<ActivityName>>,
                mapper: ActivityNameMapper<out ActivityName>,
                link: ActivityLink<ActivityName>
            ): ActivityLink<ActivityName> {
                if (link is TestActivityALink) {
                    return TestActivityBLink(activityBParameter)
                }
                return chain.next(mapper, link)
            }
        }
        val router = ActivityLinkRouterImpl(mappers, ChainFactoryImpl(listOf(interceptor)))
        val activity: Activity = mock()
        val intent = router.intentFor(activity, TestActivityALink)
        assertEquals(intent.component!!.className, TestActivityB::class.java.name)
    }

    private fun assertIntentFor(
        activityLink: ActivityLink<ActivityName>,
        clazz: Class<out Activity>
    ) {
        val activity: Activity = mock()
        val router = ActivityLinkRouterImpl(mappers, ChainFactoryImpl(emptyList()))
        val intent = router.intentFor(activity, activityLink)

        assertEquals(intent.component!!.className, clazz.name)
    }
}
