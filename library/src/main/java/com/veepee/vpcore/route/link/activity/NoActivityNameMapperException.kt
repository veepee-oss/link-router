package com.veepee.vpcore.route.link.activity

internal class NoActivityNameMapperException(activityLink: ActivityLink<ActivityName>) :
    IllegalArgumentException("$activityLink has no registered ActivityNameMapper registered for it.")
