package com.veepee.vpcore.route.activity

import android.app.Activity
import org.mockito.kotlin.mock
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
import com.veepee.vpcore.route.requireLinkParameter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
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
        val router = ActivityLinkRouterImpl(mappers)
        val intent = router.intentFor(activity, TestActivityBLink(activityBParameter))
        val parameter = intent.requireLinkParameter<TestActivityBParameter>()
        assertEquals(parameter, activityBParameter)
    }

    @Test
    fun `should raise an error when the there is no mapper for a given ActivityLink`() {

        val router = ActivityLinkRouterImpl(emptySet())

        val activity: Activity = mock()
        assertThrows(NoActivityNameMapperException::class.java) {
            router.intentFor(activity, TestActivityALink)
        }
    }

    private fun assertIntentFor(
        activityLink: ActivityLink<ActivityName>,
        clazz: Class<out Activity>
    ) {
        val activity: Activity = mock()
        val router = ActivityLinkRouterImpl(mappers)
        val intent = router.intentFor(activity, activityLink)

        assertEquals(intent.component!!.className, clazz.name)
    }
}
