package com.veepee.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.veepee.routes.create
import com.veepee.routes.feature_a.ActivityALink
import com.veepee.routes.feature_b.ActivityBLink
import com.veepee.routes.feature_b.ActivityBParameter
import com.veepee.routes.router
import com.veepee.vpcore.route.link.deeplink.UriDeepLink

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val uri = intent.data
        if (uri != null) {
            val deepLink = UriDeepLink.create(uri)
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
