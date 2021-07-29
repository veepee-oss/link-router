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
package com.veepee.vpcore.route.deeplink.route

import com.veepee.vpcore.route.activity.route.TestActivityALink
import com.veepee.vpcore.route.activity.route.TestActivityBLink
import com.veepee.vpcore.route.activity.route.TestActivityBParameter
import com.veepee.vpcore.route.deeplink.TestScheme
import com.veepee.vpcore.route.link.activity.ActivityLink
import com.veepee.vpcore.route.link.activity.ActivityName
import com.veepee.vpcore.route.link.deeplink.DeepLink
import com.veepee.vpcore.route.link.deeplink.DeepLinkMapper
import com.veepee.vpcore.route.link.deeplink.Scheme

internal object TestUriDeepLinkMapper : DeepLinkMapper<DeepLink> {
    override val supportedSchemes: Array<Scheme> = arrayOf(TestScheme.MyScheme)
    override val supportedAuthority: String = "www.google.com"

    override fun stack(deepLink: DeepLink): Array<ActivityLink<ActivityName>> {
        return arrayOf(
            TestActivityALink,
            TestActivityBLink(TestActivityBParameter(deepLink.authority))
        )
    }

    override fun canHandle(deepLink: DeepLink): Boolean {
        return deepLink.scheme in supportedSchemes
    }
}
