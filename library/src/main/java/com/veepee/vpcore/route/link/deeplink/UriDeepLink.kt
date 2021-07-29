package com.veepee.vpcore.route.link.deeplink

import android.net.Uri
import com.veepee.vpcore.route.link.ParcelableParameter
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class UriDeepLink(
    override val parameter: UriParameter,
    private val schemeFactory: (scheme: String) -> Scheme
) : DeepLink {
    @IgnoredOnParcel
    override val scheme: Scheme = schemeFactory(parameter.uri.scheme!!)

    @IgnoredOnParcel
    override val authority: String = parameter.uri.authority!!

    @IgnoredOnParcel
    val pathSegments = parameter.pathSegments

    companion object
}

@Parcelize
data class UriParameter(val uri: Uri) : ParcelableParameter {

    @IgnoredOnParcel
    val path = uri.path?.drop(1)

    @IgnoredOnParcel
    val query = uri.queryMap()

    @IgnoredOnParcel
    val pathSegments: List<String> = uri.pathSegments ?: emptyList()

    private fun Uri.queryMap(): Map<String, String> {
        return queryParameterNames.map { queryParameter ->
            queryParameter to getQueryParameter(queryParameter)!!
        }.toMap()
    }
}
