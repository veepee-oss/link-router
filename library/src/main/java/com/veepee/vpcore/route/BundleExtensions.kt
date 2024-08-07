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
package com.veepee.vpcore.route

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.IntentCompat
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.veepee.vpcore.route.link.Parameter
import com.veepee.vpcore.route.link.ParcelableParameter

val LINK_PARAMETER = "${Parameter::class.java.name}.LINK_PARAMETER"
val LINK_PARAMETER_REQUEST_KEY = "${LINK_PARAMETER}_REQUEST_KEY"

inline fun <reified T : ParcelableParameter> Activity.requireLinkParameter(): T {
    return intent.requireLinkParameter()
}

inline fun <reified T : ParcelableParameter> Intent.requireLinkParameter(): T {
    return getLinkParameter()!!
}

inline fun <reified T : ParcelableParameter> Intent.getLinkParameter(): T? {
    return IntentCompat.getParcelableExtra(this, LINK_PARAMETER, T::class.java)
}

inline fun <reified T : ParcelableParameter> Bundle.requireLinkParameter(): T {
    return getLinkParameter()!!
}

inline fun <reified T : ParcelableParameter> Bundle.getLinkParameter(): T? {
    return BundleCompat.getParcelable(this, LINK_PARAMETER, T::class.java)
}

inline fun <reified T : ParcelableParameter> Fragment.requireLinkParameter(): T {
    return getLinkParameter()!!
}

inline fun <reified T : ParcelableParameter> Fragment.getLinkParameter(): T? {
    val args = arguments ?: return null
    return BundleCompat.getParcelable(args, LINK_PARAMETER, T::class.java)
}

fun FragmentManager.setLinkParameterResult(
    requestKey: String = LINK_PARAMETER_REQUEST_KEY,
    parameter: ParcelableParameter
) {
    setFragmentResult(requestKey, parameter.asBundle())
}

inline fun <reified T : ParcelableParameter> FragmentManager.setLinkParameterResultListener(
    requestKey: String = LINK_PARAMETER_REQUEST_KEY,
    lifecycleOwner: LifecycleOwner,
    crossinline listener: (T) -> Unit
) {
    setFragmentResultListener(
        requestKey,
        lifecycleOwner
    ) { _, bundle -> listener(bundle.requireLinkParameter()) }
}

fun Intent.setLinkParameter(parameter: ParcelableParameter?): Intent {
    putExtras(Bundle().setLinkParameter(parameter))
    return this
}

fun Bundle.setLinkParameter(parameter: ParcelableParameter?): Bundle {
    putParcelable(LINK_PARAMETER, parameter)
    return this
}

fun ParcelableParameter.asBundle(): Bundle {
    return Bundle().setLinkParameter(this)
}
