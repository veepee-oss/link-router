
package com.veepee.vpcore.route

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.veepee.vpcore.route.link.Parameter
import com.veepee.vpcore.route.link.ParcelableParameter

internal val LINK_PARAMETER = "${Parameter::class.java.name}.LINK_PARAMETER"

fun <T : ParcelableParameter> Activity.requireLinkParameter(): T {
    return intent.requireLinkParameter()
}

fun <T : ParcelableParameter> Intent.requireLinkParameter(): T {
    return getLinkParameter()!!
}

fun <T : ParcelableParameter> Intent.getLinkParameter(): T? {
    return getParcelableExtra(LINK_PARAMETER)
}

fun <T : ParcelableParameter> Bundle.requireLinkParameter(): T {
    return getLinkParameter()!!
}

fun <T : ParcelableParameter> Bundle.getLinkParameter(): T? {
    return getParcelable(LINK_PARAMETER)
}

fun <T : ParcelableParameter> Fragment.requireLinkParameter(): T {
    return getLinkParameter()!!
}

fun <T : ParcelableParameter> Fragment.getLinkParameter(): T? {
    return arguments?.getParcelable(LINK_PARAMETER)
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
