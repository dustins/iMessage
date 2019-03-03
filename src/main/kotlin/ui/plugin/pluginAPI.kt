package ui.plugin

import tornadofx.Component
import tornadofx.EventContext
import ui.events.MessageAddedEvent

abstract class MessagePlugin : Component() {

    private val listener: EventContext.(MessageAddedEvent) -> Unit = {
        onMessageReceived(it)
    }

    abstract fun onMessageReceived(event: MessageAddedEvent)

    fun load() {
        subscribe(null, listener)
    }

    fun unload() {
        unsubscribe(listener)
    }
}