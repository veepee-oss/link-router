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
package com.veepee.routes.login

import com.veepee.routes.login.LoginFragmentParameter.FragmentLinkParameter
import com.veepee.vpcore.route.link.ParcelableParameter
import com.veepee.vpcore.route.link.fragment.FragmentLink
import com.veepee.vpcore.route.link.fragment.FragmentName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginFragmentLink(
    override val parameter: LoginFragmentParameter
) : FragmentLink<LoginFragmentName> {

    constructor(fragmentLink: FragmentLink<FragmentName>) : this(
        FragmentLinkParameter(
            fragmentLink
        )
    )

    @IgnoredOnParcel
    override val fragmentName: LoginFragmentName = LoginFragmentName
}


sealed class LoginFragmentParameter : ParcelableParameter {
    @Parcelize
    data class FragmentLinkParameter(
        val fragmentLink: FragmentLink<FragmentName>
    ) : LoginFragmentParameter()

}