package com.veepee.vpcore.route.link.deeplink

import android.os.Parcelable
import com.veepee.vpcore.route.link.Link

interface DeepLink : Link, Parcelable {
    val scheme: Scheme
    val authority: String
}

interface Scheme {
    val value: String
}
