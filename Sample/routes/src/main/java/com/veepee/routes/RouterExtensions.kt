package com.veepee.routes

import com.veepee.vpcore.route.GlobalRouterBuilder
import com.veepee.vpcore.route.Router

val router: Router by lazy { GlobalRouterBuilder.build() }
