package com.veepee.vpcore.route.fragment.feature

import androidx.fragment.app.Fragment
import com.veepee.vpcore.route.fragment.route.TestFragmentName
import com.veepee.vpcore.route.link.fragment.FragmentLink
import com.veepee.vpcore.route.link.fragment.FragmentNameMapper

internal object TestFragmentNameMapper : FragmentNameMapper<TestFragmentName> {
    override val supportedNames: Array<TestFragmentName> = TestFragmentName.values()

    override fun map(fragmentLink: FragmentLink<TestFragmentName>): Class<out Fragment> {
        return when (fragmentLink.fragmentName) {
            TestFragmentName.FragmentA -> {
                TestFragmentA::class.java
            }
            TestFragmentName.FragmentB -> {
                TestFragmentB::class.java
            }
        }
    }
}
