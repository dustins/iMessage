package ui.controllers

import adaptors.fetchFromDB
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import tornadofx.Controller
import tornadofx.EventBus
import tornadofx.FXEvent
import tornadofx.observable
import model.Message

class MessagesController : Controller() {

    val searchTerm = SimpleStringProperty()
    val isSearchVisible = SimpleBooleanProperty()
    val messageList = mutableListOf<Message>().observable()
    val settings: SettingsController by inject()
    val chatName = SimpleStringProperty()

    fun handleKeyPress(keyEvent: KeyEvent) {
        val findMac = KeyCodeCombination(KeyCode.F, KeyCombination.META_DOWN)
        if (findMac.match(keyEvent)) {
            isSearchVisible.value = true
            fire(SearchOpenedEvent())
        } else if (keyEvent.code == KeyCode.ESCAPE) {
            isSearchVisible.value = false
        }
    }

    fun loadChat(chatID: Int) {
        chatName.value = chatID.toString()
        messageList.clear()
        runAsync {
            fetchFromDB(chatID, settings.messageDB.valueSafe)
        } ui {
            messageList.addAll(it)
        }
    }
}

// TODO I don't know where to put this
class SearchOpenedEvent : FXEvent(EventBus.RunOn.BackgroundThread)