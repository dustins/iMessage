package ui.controllers

import db.fetchContacts
import db.fetchConversations
import model.Conversation
import tornadofx.Controller
import tornadofx.observable

class ConversationController : Controller() {

    val conversations = mutableListOf<Conversation>().observable()
    private val settings: SettingsController by inject()

    fun loadConversations() {
        runAsync {
            val contacts = fetchContacts(settings.addressDB.valueSafe)
            fetchConversations(settings.messageDB.valueSafe, contacts)
        } ui {
            conversations.addAll(it)
        }
    }
}
