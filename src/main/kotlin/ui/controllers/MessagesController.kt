package ui.controllers

import db.fetchMessages
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import model.Conversation
import model.Message
import model.displayName
import tornadofx.Controller
import tornadofx.observable

class MessagesController : Controller() {

    var displayName = SimpleStringProperty()
    val messageList = mutableListOf<Message>().observable()
    private val settings: SettingsController by inject()
    val isLoadingProperty = SimpleBooleanProperty(false)

    fun loadConversation(conversation: Conversation) {
        messageList.clear()
        displayName.value = conversation.contacts.joinToString { it.displayName() }

        isLoadingProperty.value = true
        runAsync {
            fetchMessages(settings.messageDB.valueSafe, conversation)
        } ui {
            messageList.addAll(it)
            isLoadingProperty.value = false
        }
    }
}
