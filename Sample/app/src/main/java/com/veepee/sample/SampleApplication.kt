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
package com.veepee.sample

import android.app.Application
import com.veepee.feature.a.routes.FeatureAActivityNameMapper
import com.veepee.feature.a.routes.FeatureADeepLinkMapper
import com.veepee.feature.a.routes.FeatureAFragmentNameMapper
import com.veepee.feature.b.routes.FeatureBActivityNameMapper
import com.veepee.feature.b.routes.FeatureBDeepLinkMapper
import com.veepee.feature.b.routes.FeatureBDialogFragmentNameMapper
import com.veepee.feature.login.routes.*
import com.veepee.vpcore.route.GlobalRouterBuilder


class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        with(GlobalRouterBuilder) {
            add(FeatureAActivityNameMapper)
            add(FeatureBActivityNameMapper)
            add(LoginActivityNameMapper)

            add(FeatureAFragmentNameMapper)
            add(LoginFragmentNameMapper)
            add(FeatureBDialogFragmentNameMapper)

            add(FeatureBDeepLinkMapper)
            add(FeatureADeepLinkMapper)
            add(LoginDeepLinkMapper)

            add(DeepLinkAuthenticationInterceptor)
            add(ActivityLinkAuthenticationInterceptor)
            add(FragmentLinkAuthenticationInterceptor)
        }
    }
}
