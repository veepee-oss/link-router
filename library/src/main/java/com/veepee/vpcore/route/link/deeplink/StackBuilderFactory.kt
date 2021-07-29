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

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

internal interface StackBuilderFactory {
    fun create(context: Context): StackBuilder
}

internal class StackBuilderFactoryImpl : StackBuilderFactory {
    override fun create(context: Context): StackBuilder {
        return StackBuilderImpl(context)
    }
}

internal interface StackBuilder {
    fun addNextIntent(intent: Intent)
    fun startActivities()
}

internal class StackBuilderImpl(private val context: Context) : StackBuilder {
    private val intents = mutableListOf<Intent>()
    override fun addNextIntent(intent: Intent) {
        intents.add(intent)
    }

    override fun startActivities() {
        val intents = this.intents.toTypedArray()

        if (context !is Activity) {
            intents[0].addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_TASK_ON_HOME
            )
        }
        ContextCompat.startActivities(context, intents)
    }
}
