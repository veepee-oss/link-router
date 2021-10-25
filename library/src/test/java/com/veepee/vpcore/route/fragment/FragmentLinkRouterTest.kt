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
package com.veepee.vpcore.route.fragment

import com.veepee.vpcore.route.fragment.feature.TestFragmentA
import com.veepee.vpcore.route.fragment.feature.TestFragmentB
import com.veepee.vpcore.route.fragment.feature.TestFragmentNameMapper
import com.veepee.vpcore.route.fragment.route.TestFragmentALink
import com.veepee.vpcore.route.fragment.route.TestFragmentBLink
import com.veepee.vpcore.route.fragment.route.TestFragmentBParameter
import com.veepee.vpcore.route.link.fragment.FragmentLink
import com.veepee.vpcore.route.link.fragment.FragmentLinkRouterImpl
import com.veepee.vpcore.route.link.fragment.FragmentName
import com.veepee.vpcore.route.link.fragment.FragmentNameMapper
import com.veepee.vpcore.route.link.fragment.NoFragmentNameMapperException
import com.veepee.vpcore.route.link.fragment.chain.FragmentLinkInterceptor
import com.veepee.vpcore.route.link.interceptor.Chain
import com.veepee.vpcore.route.link.interceptor.ChainFactoryImpl
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
        val router = FragmentLinkRouterImpl(mappers, ChainFactoryImpl(emptyList()))
        val fragmentA = router.fragmentFor(TestFragmentALink)
        assertEquals(fragmentA::class.java, TestFragmentA::class.java)

        val fragmentB = router.fragmentFor(TestFragmentBLink(testFragmentBParameter))
        assertEquals(fragmentB::class.java, TestFragmentB::class.java)
    }

    @Test
    fun `should retrieve parameter from Fragment`() {
        val router = FragmentLinkRouterImpl(mappers, ChainFactoryImpl(emptyList()))
        val fragment = router.fragmentFor(TestFragmentBLink(testFragmentBParameter))
        assertEquals(
            fragment.requireLinkParameter<TestFragmentBParameter>(),
            testFragmentBParameter
        )
    }

    @Test
    fun `should raise an error when the there is no mapper for a given ActivityLink`() {
        val router = FragmentLinkRouterImpl(emptySet(), ChainFactoryImpl(emptyList()))

        Assert.assertThrows(NoFragmentNameMapperException::class.java) {
            router.fragmentFor(TestFragmentALink)
        }
    }

    @Test
    fun `should intercept fragmentA and return fragmentB`() {
        val interceptor = object : FragmentLinkInterceptor {
            override fun intercept(
                chain: Chain<FragmentNameMapper<out FragmentName>, FragmentLink<FragmentName>>,
                mapper: FragmentNameMapper<out FragmentName>,
                link: FragmentLink<FragmentName>
            ): FragmentLink<FragmentName> {
                if (link is TestFragmentALink) {
                    return TestFragmentBLink(testFragmentBParameter)
                }
                return chain.next(mapper, link)
            }
        }
        val router = FragmentLinkRouterImpl(mappers, ChainFactoryImpl(listOf(interceptor)))

        val fragment = router.fragmentFor(TestFragmentALink)
        assertEquals(
            fragment.requireLinkParameter<TestFragmentBParameter>(),
            testFragmentBParameter
        )
    }
}
