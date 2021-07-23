package com.veepee.vpcore.route.fragment.route

import com.veepee.vpcore.route.link.ParcelableParameter
import com.veepee.vpcore.route.link.fragment.FragmentLink
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class TestFragmentBParameter(val id: String) : ParcelableParameter

internal class TestFragmentBLink(override val parameter: TestFragmentBParameter) :
    FragmentLink<TestFragmentName> {
    override val fragmentName: TestFragmentName = TestFragmentName.FragmentB
}
