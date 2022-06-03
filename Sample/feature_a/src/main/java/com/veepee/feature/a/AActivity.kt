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
package com.veepee.feature.a

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import com.veepee.routes.feature_a.FragmentALink
import com.veepee.routes.feature_b.FeatureBComposableLink
import com.veepee.routes.router
import com.veepee.vpcore.route.link.compose.ComposableFor
import com.veepee.vpcore.route.link.compose.LinkRouterContainer

class AActivity : AppCompatActivity(R.layout.a_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.setFragmentResultListener(
            FragmentALink.REQUEST_KEY,
            this
        ) { key, _ ->
            Toast.makeText(this, "Result From $key", Toast.LENGTH_LONG).show()
        }
        if (savedInstanceState == null) {
            val fragment = router.fragmentFor(FragmentALink)
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment)
                .commit()
        }
        findViewById<ComposeView>(R.id.composeView).setContent {
            MyRootComposition()
        }
    }
}

@Composable
private fun MyRootComposition(modifier: Modifier = Modifier) {

    LinkRouterContainer(router = router) {
        Column {
            ComposableFor(
                FeatureBComposableLink("1 - AActivity sent this text to a Composable in B Module"),
                modifier
                    .fillMaxWidth()
                    .background(Color.Yellow)
            )
            ComposableFor(
                FeatureBComposableLink("2 - AActivity sent this text to a Composable in B Module"),
                modifier
                    .fillMaxWidth()
                    .background(Color.Green)
            )
        }

    }
}
