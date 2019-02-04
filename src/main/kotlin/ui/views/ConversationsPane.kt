package ui.views

import javafx.scene.layout.Priority
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
        prefWidth = 275.0
        listview(controller.conversations) {
            cellFormat {
                graphic = ConversationCard(it).root
            }
            onUserSelect {
                messageController.loadConversation(it)
            }
            vgrow = Priority.ALWAYS
        }
    }
}
