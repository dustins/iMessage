package ui.controllers

import db.fetchMessages
import javafx.beans.property.SimpleStringProperty
import model.Message
import tornadofx.Controller
import tornadofx.observable

class MessagesController : Controller() {

    val messageList = mutableListOf<Message>().observable()
    val settings: SettingsController by inject()
    val chatName = SimpleStringProperty()

    fun loadChat(chatID: Int) {
        chatName.value = chatID.toString()
        messageList.clear()
        runAsync {
            fetchMessages(chatID, settings.messageDB.valueSafe)
        } ui {
            messageList.addAll(it)
        }
    }
}