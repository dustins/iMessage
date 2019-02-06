package ui.views

import javafx.geometry.Pos
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import tornadofx.*
import ui.controllers.ConversationController
import ui.controllers.MessagesController

class ConversationsPane : View("Conversations") {
    private val messageController: MessagesController by inject()
    private val controller: ConversationController by inject()

    init {
        controller.loadConversations()
    }

    override val root = stackpane {
        prefWidth = 275.0
        listview(controller.conversations) {
            hiddenWhen(controller.isLoadingProperty)
            cellFormat {
                graphic = ConversationCard(it).root
            }
            onUserSelect {
                messageController.loadConversation(it)
            }
            vgrow = Priority.ALWAYS
        }
        vbox {
            visibleWhen(controller.isLoadingProperty)
            imageview("loading.gif") {
                alignment = Pos.CENTER
            }
            useMaxWidth = true
            useMaxWidth = true
            style {
                backgroundColor += Color.WHITE
            }
        }
    }
}
