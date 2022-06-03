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
package com.veepee.vpcore.route.link.interceptor

interface Chain<Mapper, Link> {
    fun next(
        mapper: Mapper,
        link: Link
    ): Link
}

interface ChainFactory<Mapper, Link> {
    fun create(): Chain<Mapper, Link>
}

internal class ChainFactoryImpl<Mapper, Link>(
    private val initialInterceptors: List<LinkInterceptor<Mapper, Link>>
) : ChainFactory<Mapper, Link> {
    override fun create(): Chain<Mapper, Link> {
        return ChainImpl(initialInterceptors + DefaultInterceptor())
    }
}

internal class ChainImpl<Mapper, Link>(
    private val interceptors: List<LinkInterceptor<Mapper, Link>>
) : Chain<Mapper, Link> {

    private var currentIndex = -1

    override fun next(
        mapper: Mapper,
        link: Link
    ): Link {
        return interceptors[++currentIndex].intercept(this, mapper, link)
    }
}
