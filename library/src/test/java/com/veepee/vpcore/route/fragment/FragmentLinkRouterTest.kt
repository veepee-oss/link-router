package com.veepee.vpcore.route.fragment

import com.veepee.vpcore.route.fragment.feature.TestFragmentA
import com.veepee.vpcore.route.fragment.feature.TestFragmentB
import com.veepee.vpcore.route.fragment.feature.TestFragmentNameMapper
import com.veepee.vpcore.route.fragment.route.TestFragmentALink
import com.veepee.vpcore.route.fragment.route.TestFragmentBLink
import com.veepee.vpcore.route.fragment.route.TestFragmentBParameter
import com.veepee.vpcore.route.link.fragment.FragmentLinkRouterImpl
import com.veepee.vpcore.route.link.fragment.FragmentName
import com.veepee.vpcore.route.link.fragment.FragmentNameMapper
import com.veepee.vpcore.route.link.fragment.NoFragmentNameMapperException
import com.veepee.vpcore.route.requireLinkParameter
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FragmentLinkRouterTest {

    private val mappers: Set<FragmentNameMapper<out FragmentName>> = setOf(TestFragmentNameMapper)
    private val testFragmentBParameter = TestFragmentBParameter("id")

    @Test
    fun `should return fragment for registered FragmentNameMapper`() {
        val router = FragmentLinkRouterImpl(mappers)
        val fragmentA = router.fragmentFor(TestFragmentALink)
        assertEquals(fragmentA::class.java, TestFragmentA::class.java)

        val fragmentB = router.fragmentFor(TestFragmentBLink(testFragmentBParameter))
        assertEquals(fragmentB::class.java, TestFragmentB::class.java)
    }

    @Test
    fun `should retrieve parameter from Fragment`() {
        val router = FragmentLinkRouterImpl(mappers)
        val fragment = router.fragmentFor(TestFragmentBLink(testFragmentBParameter))
        assertEquals(
            fragment.requireLinkParameter<TestFragmentBParameter>(),
            testFragmentBParameter
        )
    }

    @Test
    fun `should raise an error when the there is no mapper for a given ActivityLink`() {

        val router = FragmentLinkRouterImpl(emptySet())

        Assert.assertThrows(NoFragmentNameMapperException::class.java) {
            router.fragmentFor(TestFragmentALink)
        }
    }
}
