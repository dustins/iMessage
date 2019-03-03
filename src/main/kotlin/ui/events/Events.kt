package ui.events

import javafx.scene.Node
import model.Message
import tornadofx.EventBus
import tornadofx.FXEvent

class MessageAddedEvent(val messageNode: Node, val message: Message) : FXEvent(EventBus.RunOn.BackgroundThread)
