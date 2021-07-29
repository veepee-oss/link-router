package com.veepee.routes

import android.net.Uri
import com.veepee.vpcore.route.link.deeplink.UriDeepLink
import com.veepee.vpcore.route.link.deeplink.UriParameter

fun UriDeepLink.Companion.create(uri: Uri): UriDeepLink =
    UriDeepLink(UriParameter(uri)) { scheme -> Schemas.valueOf(scheme) }
