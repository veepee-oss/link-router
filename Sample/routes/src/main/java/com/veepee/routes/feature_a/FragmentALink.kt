package com.veepee.routes.feature_a

import com.veepee.vpcore.route.link.ParcelableParameter
import com.veepee.vpcore.route.link.fragment.FragmentLink

object FragmentALink : FragmentLink<FeatureAFragmentNames> {
    override val fragmentName: FeatureAFragmentNames = FeatureAFragmentNames.FragmentA
    override val parameter: ParcelableParameter? = null
}
