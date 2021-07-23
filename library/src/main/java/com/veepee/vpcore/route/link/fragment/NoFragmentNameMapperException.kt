package com.veepee.vpcore.route.link.fragment

internal class NoFragmentNameMapperException(fragmentLink: FragmentLink<FragmentName>) :
    IllegalArgumentException("$fragmentLink has no registered FragmentNameMapper registered for it.")
