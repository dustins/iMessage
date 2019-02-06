package ui.controllers

import db.fetchContacts
import db.fetchConversations
import javafx.beans.property.SimpleBooleanProperty
import model.Conversation
import tornadofx.Controller
import tornadofx.observable

class ConversationController : Controller() {

    val conversations = mutableListOf<Conversation>().observable()
    private val settings: SettingsController by inject()
    val isLoadingProperty = SimpleBooleanProperty(false)

    fun loadConversations() {
        isLoadingProperty.value = true
        runAsync {
            val contacts = fetchContacts(settings.addressDB.valueSafe)
            fetchConversations(settings.messageDB.valueSafe, contacts)
        } ui {
            isLoadingProperty.value = false
            conversations.addAll(it)
        }
    }
}
