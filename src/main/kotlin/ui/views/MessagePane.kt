package ui.views

import javafx.geometry.Pos
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*
import ui.controllers.MessagesController


class MessagePane : View("Messages") {

    private val controller: MessagesController by inject()

    override val root = stackpane {
        vbox {
            hiddenWhen(controller.isLoadingProperty)
            hbox {
                label(controller.displayName) {
                    style {
                        fontSize = 18.px
                        fontFamily = "Helvetica"
                        fontWeight = FontWeight.BOLD
                    }
                    paddingAll = 3.0
                }
                prefHeight = 50.0
                alignment = Pos.CENTER
            }
            listview(controller.messageList) {
                cellFormat {
                    graphic = MessageCell(it).root
                }
                vgrow = Priority.ALWAYS
            }
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
