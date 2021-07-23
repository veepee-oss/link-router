package com.veepee.vpcore.route.link.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.veepee.vpcore.route.setLinkParameter

interface FragmentLinkRouter {
    fun fragmentFor(fragmentLink: FragmentLink<FragmentName>): Fragment
}

@Suppress("UNCHECKED_CAST")
internal class FragmentLinkRouterImpl(
    fragmentMappers: Set<FragmentNameMapper<out FragmentName>>
) : FragmentLinkRouter {

    private val fragmentLinkMapper =
        fragmentMappers.flatMap { mapper ->
            mapper.supportedNames.map { fragmentName ->
                fragmentName to mapper as FragmentNameMapper<FragmentName>
            }
        }.toMap()

    override fun fragmentFor(fragmentLink: FragmentLink<FragmentName>): Fragment {
        val fragment =
            fragmentLinkMapper[fragmentLink.fragmentName]?.map(fragmentLink)?.newInstance()
                ?: throw NoFragmentNameMapperException(fragmentLink)

        return fragment.apply {
            arguments = Bundle().setLinkParameter(fragmentLink.parameter)
        }
    }
}
