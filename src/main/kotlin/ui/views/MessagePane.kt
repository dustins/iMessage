package ui.views

import javafx.geometry.Pos
import javafx.scene.layout.Priority
import model.Contact
import model.displayName
import model.lookup
import tornadofx.*
import ui.controllers.MessagesController


class MessagePane : View("Messages") {

    private val controller: MessagesController by inject()

    override val root = vbox {
        hbox {
            label(controller.displayName) {
                style {
                    fontSize = 24.px
                    fontFamily = "Helvetica"
                }
            }
            alignment = Pos.CENTER
        }
        listview(controller.messageList) {
            cellFormat {
                graphic = MessageCell(it).root
            }
            vgrow = Priority.ALWAYS
        }

        paddingAll = 10.0
        spacing = 4.0
    }
}
