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

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.veepee.routes.Schemas
import com.veepee.routes.feature_a.ActivityALink
import com.veepee.routes.feature_b.ActivityBLink
import com.veepee.routes.feature_b.ActivityBParameter
import com.veepee.routes.router
import com.veepee.vpcore.route.link.deeplink.UriDeepLink
import com.veepee.vpcore.route.link.deeplink.UriParameter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val uri = intent.data
        if (uri != null) {
            val deepLink = UriDeepLink(UriParameter(uri), Schemas.MyApp)
            router.route(this, deepLink)
            finish()
        }

        findViewById<View>(R.id.activityAButton).setOnClickListener {
            val intent = router.intentFor(this, ActivityALink)
            startActivity(intent)
        }

        findViewById<View>(R.id.activityBButton).setOnClickListener {
            val parameter = ActivityBParameter("some id")
            val intent = router.intentFor(this, ActivityBLink(parameter))
            startActivity(intent)
        }
    }
}
