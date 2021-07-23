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
