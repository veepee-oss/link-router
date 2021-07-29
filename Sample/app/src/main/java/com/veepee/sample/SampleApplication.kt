package com.veepee.sample

import android.app.Application
import com.veepee.feature.a.routes.FeatureAActivityNameMapper
import com.veepee.feature.a.routes.FeatureADeepLinkMapper
import com.veepee.feature.a.routes.FeatureAFragmentNameMapper
import com.veepee.feature.b.routes.FeatureBActivityNameMapper
import com.veepee.feature.b.routes.FeatureBDeepLinkMapper
import com.veepee.vpcore.route.GlobalRouterBuilder

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        with(GlobalRouterBuilder) {
            add(FeatureAActivityNameMapper)
            add(FeatureBActivityNameMapper)
            add(FeatureAFragmentNameMapper)
            add(FeatureBDeepLinkMapper)
            add(FeatureADeepLinkMapper)
        }
    }
}
