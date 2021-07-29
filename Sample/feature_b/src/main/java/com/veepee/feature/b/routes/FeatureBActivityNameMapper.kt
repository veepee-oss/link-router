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
package com.veepee.feature.b.routes

import android.app.Activity
import com.veepee.feature.b.BActivity
import com.veepee.routes.feature_b.FeatureBActivityNames
import com.veepee.vpcore.route.link.activity.ActivityLink
import com.veepee.vpcore.route.link.activity.ActivityNameMapper

object FeatureBActivityNameMapper : ActivityNameMapper<FeatureBActivityNames> {
    override val supportedNames: Array<FeatureBActivityNames> = FeatureBActivityNames.values()

    override fun map(activityLink: ActivityLink<FeatureBActivityNames>): Class<out Activity> {
        return when (activityLink.activityName) {
            FeatureBActivityNames.ActivityB -> BActivity::class.java
        }
    }
}
