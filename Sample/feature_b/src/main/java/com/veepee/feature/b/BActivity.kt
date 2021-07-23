package com.veepee.feature.b

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.veepee.routes.feature_a.FragmentALink
import com.veepee.routes.router

class BActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.b_activity)

        val fragment = router.fragmentFor(FragmentALink)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
