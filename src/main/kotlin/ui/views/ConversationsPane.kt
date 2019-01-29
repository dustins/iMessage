package ui.views

import javafx.scene.layout.Priority
import tornadofx.*
import ui.controllers.MessagesController

class ConversationsPane : View("Conversations") {
    val chatIDs = mutableListOf<Int>().observable()
    val messageController: MessagesController by inject()

    init {
        for (i in 1..100) {
            chatIDs.add(i)
        }
    }

    override val root = vbox {
        prefWidth = 100.0
        listview(chatIDs) {
            onUserSelect {
                messageController.loadChat(it)
            }
            vgrow = Priority.ALWAYS
        }
    }
}
