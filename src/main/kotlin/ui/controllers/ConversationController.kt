package ui.controllers

import db.fetchContacts
import db.fetchConversations
import javafx.beans.property.SimpleBooleanProperty
import model.Conversation
import tornadofx.Controller
import tornadofx.observable
import ui.viewmodels.SettingsViewModel

class ConversationController : Controller() {

    val conversations = mutableListOf<Conversation>().observable()
    private val settings: SettingsViewModel by inject()
    val isLoadingProperty = SimpleBooleanProperty(false)

    fun loadConversations() {
        conversations.clear()
        isLoadingProperty.value = true
        runAsync {
            val contacts = fetchContacts(settings.addressBookDB.value)
            fetchConversations(settings.messageDB.value, contacts)
        } ui {
            isLoadingProperty.value = false
            conversations.addAll(it)
        }
    }
}
