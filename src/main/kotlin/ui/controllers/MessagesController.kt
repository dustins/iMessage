package ui.controllers

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import tornadofx.*
import ui.models.Message

class MessagesController : Controller() {

    val searchTerm = SimpleStringProperty()
    val isSearchVisible = SimpleBooleanProperty()
    val messageList = mutableListOf<Message>().observable()

    fun handleKeyPress(keyEvent: KeyEvent) {
        val findMac = KeyCodeCombination(KeyCode.F, KeyCombination.META_DOWN)
        if (findMac.match(keyEvent)) {
            isSearchVisible.value = true
            fire(SearchOpenedEvent())
        } else if (keyEvent.code == KeyCode.ESCAPE) {
            isSearchVisible.value = false
        }
    }
}


// TODO I don't know where to put this
class SearchOpenedEvent : FXEvent(EventBus.RunOn.BackgroundThread)