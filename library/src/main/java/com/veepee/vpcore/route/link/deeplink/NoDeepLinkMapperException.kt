package com.veepee.vpcore.route.link.deeplink

class NoDeepLinkMapperException(deepLink: DeepLink) :
    IllegalArgumentException("$deepLink has no registered DeepLinkMapper registered for it.")
