package com.veepee.vpcore.route.link.compose.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.veepee.vpcore.route.link.compose.ComposableEvent

@Composable
inline fun <reified Event : ComposableEvent> LinkRouterEventHandlerContainer(
    current: LinkRouterEventHandler<out ComposableEvent> = LocalLinkRouterEventHandler.current,
    noinline onEvent: (Event) -> Unit,
    crossinline content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalLinkRouterEventHandler provides LinkRouterEventHandler(
            parent = current,
            isAssignableFrom = { event ->
                Event::class.java.isAssignableFrom(event::class.java)
            },
            onEvent = onEvent
        )
    ) {
        content()
    }
}

class LinkRouterEventHandler<Event : ComposableEvent> constructor(
    private val parent: LinkRouterEventHandler<out ComposableEvent>? = null,
    private val isAssignableFrom: (event: Any) -> Boolean,
    private val onEvent: (Event) -> Unit
) {
    @Suppress("UNCHECKED_CAST")
    fun publish(event: ComposableEvent) {
        if (isAssignableFrom(event)) {
            onEvent(event as Event)
        } else if (parent == null) {
            throw NoParentEventHandlerException()
        } else {
            parent.publish(event)
        }
    }
}

class NoParentEventHandlerException : RuntimeException("No parent LocalEventHandler found!")
class NoEventHandlerException(event: ComposableEvent) :
    RuntimeException("Your event \"$event\" reached the topmost EventHandler one in the compose hierarchy without a handler!")

val LocalLinkRouterEventHandler: ProvidableCompositionLocal<LinkRouterEventHandler<out ComposableEvent>> =
    compositionLocalOf {
        LinkRouterEventHandler(
            isAssignableFrom = { true },
            onEvent = { event ->
                throw NoEventHandlerException(event)
            }
        )
    }
