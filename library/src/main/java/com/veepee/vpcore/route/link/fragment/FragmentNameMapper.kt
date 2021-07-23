package com.veepee.vpcore.route.link.fragment

import androidx.fragment.app.Fragment

interface FragmentNameMapper<T : FragmentName> {
    val supportedNames: Array<T>
    fun map(fragmentLink: FragmentLink<T>): Class<out Fragment>
}
