package com.veepee.feature.login.routes

import com.veepee.routes.Schemas
import com.veepee.vpcore.route.link.ParcelableParameter
import com.veepee.vpcore.route.link.deeplink.DeepLink
import com.veepee.vpcore.route.link.deeplink.Scheme
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

const val LOGIN_AUTHORITY = "login"

@Parcelize
data class LoginDeepLink(override val parameter: LoginDeepLinkParameter) : DeepLink {
    constructor(deepLink: DeepLink) : this(LoginDeepLinkParameter(deepLink))

    @IgnoredOnParcel
    override val scheme: Scheme = Schemas.MyApp
    @IgnoredOnParcel
    override val authority: String = LOGIN_AUTHORITY
}

@Parcelize
data class LoginDeepLinkParameter(val deepLink: DeepLink) : ParcelableParameter