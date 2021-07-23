package com.veepee.vpcore.route.link.activity

import android.content.Context
import android.content.Intent
import com.veepee.vpcore.route.setLinkParameter

interface ActivityLinkRouter {
    fun intentFor(context: Context, activityLink: ActivityLink<ActivityName>): Intent
}

@Suppress("UNCHECKED_CAST")
internal class ActivityLinkRouterImpl(
    activityMappers: Set<ActivityNameMapper<out ActivityName>>
) : ActivityLinkRouter {

    // Creates a map of ActivityNames and ActivityNameMappers.
    // This takes more memory, but the routing will be faster.
    private val activityLinkMappers by lazy {
        activityMappers.flatMap { mapper ->
            mapper.supportedNames.map { activityName ->
                activityName to mapper as ActivityNameMapper<ActivityName>
            }
        }.toMap()
    }

    override fun intentFor(context: Context, activityLink: ActivityLink<ActivityName>): Intent {
        return mapToIntent(context, activityLink)
    }

    private fun mapToIntent(
        context: Context,
        activityLink: ActivityLink<ActivityName>
    ): Intent {
        val mapper =
            activityLinkMappers[activityLink.activityName] ?: throw NoActivityNameMapperException(
                activityLink
            )
        val activityClass = mapper.map(activityLink)

        return Intent(context, activityClass).setLinkParameter(activityLink.parameter)
    }
}
