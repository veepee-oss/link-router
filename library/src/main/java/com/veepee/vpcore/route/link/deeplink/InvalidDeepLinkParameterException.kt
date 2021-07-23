package com.veepee.vpcore.route.link.deeplink

class InvalidDeepLinkParameterException(deepLink: DeepLink, message: String = "") :
    IllegalArgumentException("$deepLink lacks parameters. Message: $message")
