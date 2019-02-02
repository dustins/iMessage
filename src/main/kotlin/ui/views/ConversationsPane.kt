package ui.views

import javafx.scene.layout.Priority
import model.displayName
import tornadofx.*
import ui.controllers.ConversationController
import ui.controllers.MessagesController

class ConversationsPane : View("Conversations") {
    private val messageController: MessagesController by inject()
    private val controller: ConversationController by inject()

    init {
        controller.loadConversations()
    }

    override val root = vbox {
        prefWidth = 100.0
        listview(controller.conversations) {
            cellFormat {
                text = it.contacts.joinToString { contact -> contact.displayName() }
            }
            onUserSelect {
                messageController.loadConversation(it)
            }
            vgrow = Priority.ALWAYS
        }
    }
}
