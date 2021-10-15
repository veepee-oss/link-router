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
package com.veepee.feature.login

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.veepee.routes.LoginStatus
import com.veepee.routes.login.LoginActivityParameter
import com.veepee.routes.login.LoginActivityParameter.ActivityLinkParameter
import com.veepee.routes.login.LoginActivityParameter.DeepLinkParameter
import com.veepee.routes.router
import com.veepee.vpcore.route.requireLinkParameter

class LoginActivity : AppCompatActivity(R.layout.login_activity) {
    // You may need to forward the result to the calling Activity as
    // this Activity may have been started by a launcher too.
    private val launcher = registerForActivityResult(StartActivityForResult()) {
        setResult(it.resultCode, it.data)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.login_button)
            .setOnClickListener {
                LoginStatus.isLogged = true
                when (val parameter = requireLinkParameter<LoginActivityParameter>()) {
                    is DeepLinkParameter -> {
                        router.route(this, parameter.deepLink)
                        finish()
                    }
                    is ActivityLinkParameter -> {
                        val intent = router.intentFor(this, parameter.activityLink)
                        launcher.launch(intent)
                    }
                }
            }
    }
}
