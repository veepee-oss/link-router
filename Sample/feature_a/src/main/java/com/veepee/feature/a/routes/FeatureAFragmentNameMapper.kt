package com.veepee.feature.a.routes

import androidx.fragment.app.Fragment
import com.veepee.feature.a.AFragment
import com.veepee.routes.feature_a.FeatureAFragmentNames
import com.veepee.vpcore.route.link.fragment.FragmentLink
import com.veepee.vpcore.route.link.fragment.FragmentNameMapper

object FeatureAFragmentNameMapper : FragmentNameMapper<FeatureAFragmentNames> {
    override val supportedNames: Array<FeatureAFragmentNames> = FeatureAFragmentNames.values()

    override fun map(fragmentLink: FragmentLink<FeatureAFragmentNames>): Class<out Fragment> {
        return when (fragmentLink.fragmentName) {
            FeatureAFragmentNames.FragmentA -> AFragment::class.java
        }
    }
}
