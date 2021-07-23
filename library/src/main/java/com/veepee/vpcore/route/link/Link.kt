package com.veepee.vpcore.route.link

import android.os.Parcelable

/**
 * Basic representation of a destination.
 * */
interface Link {
    val parameter: Parameter?
}

/**
 * Extra information passed to a destination.
 * */
interface Parameter

/**
 * Basic representation of a destination parameter to Activities and Fragments.
 * We require it to implement Parcelable as a way of having a standard serialization method
 * */
interface ParcelableParameter : Parameter, Parcelable
