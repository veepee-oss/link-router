package com.veepee.vpcore.route.activity.feature

import android.app.Activity
import com.veepee.vpcore.route.activity.route.TestActivityBParameter
import com.veepee.vpcore.route.requireLinkParameter

internal class TestActivityB : Activity() {
    private val param by lazy { intent.requireLinkParameter<TestActivityBParameter>() }
}
