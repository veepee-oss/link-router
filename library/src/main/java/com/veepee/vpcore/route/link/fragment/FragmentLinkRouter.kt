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
package com.veepee.vpcore.route.link.fragment

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.veepee.vpcore.route.link.interceptor.ChainFactory
import com.veepee.vpcore.route.setLinkParameter

interface FragmentLinkRouter {
    fun fragmentFor(fragmentLink: FragmentLink<FragmentName>): Fragment
    fun dialogFragmentFor(fragmentLink: DialogFragmentLink<DialogFragmentName>): DialogFragment
}

@Suppress("UNCHECKED_CAST")
internal class FragmentLinkRouterImpl(
    fragmentMappers: Set<FragmentNameMapper<out FragmentName>>,
    private val chainFactory: ChainFactory<FragmentNameMapper<out FragmentName>, FragmentLink<FragmentName>>
) : FragmentLinkRouter {

    private val fragmentLinkMapper =
        fragmentMappers.flatMap { mapper ->
            mapper.supportedNames.map { fragmentName ->
                fragmentName to mapper as FragmentNameMapper<FragmentName>
            }
        }.toMap()

    override fun fragmentFor(fragmentLink: FragmentLink<FragmentName>): Fragment {
        val chain = chainFactory.create()
        val mapper =
            fragmentLinkMapper[fragmentLink.fragmentName] ?: throw NoFragmentNameMapperException(
                fragmentLink
            )
        val newFragmentLink = chain.next(mapper, fragmentLink)
        if (newFragmentLink != fragmentLink) {
            return fragmentFor(newFragmentLink)
        }
        val fragment = mapper.map(fragmentLink).newInstance()
        return fragment.apply {
            arguments = Bundle().setLinkParameter(fragmentLink.parameter)
        }
    }

    override fun dialogFragmentFor(fragmentLink: DialogFragmentLink<DialogFragmentName>): DialogFragment {
        return fragmentFor(fragmentLink) as DialogFragment
    }
}
