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
    constructor(
        uri: Uri,
        schemeFactory: (scheme: String) -> Scheme
    ) : this(UriParameter(uri), schemeFactory)

    constructor(
        url: String,
        schemeFactory: (scheme: String) -> Scheme
    ) : this(Uri.parse(url), schemeFactory)

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
