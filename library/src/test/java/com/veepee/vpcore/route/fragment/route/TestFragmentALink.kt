package com.veepee.vpcore.route.fragment.route

import com.veepee.vpcore.route.link.ParcelableParameter
import com.veepee.vpcore.route.link.fragment.FragmentLink

internal object TestFragmentALink : FragmentLink<TestFragmentName> {
    override val fragmentName: TestFragmentName = TestFragmentName.FragmentA
    override val parameter: ParcelableParameter? = null
}
