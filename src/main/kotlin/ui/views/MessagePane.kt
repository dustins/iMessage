package ui.views

import javafx.geometry.Pos
import javafx.scene.layout.Priority
import tornadofx.*
import ui.controllers.MessagesController
import ui.styles.MessageStyle
import utils.getImage


class MessagePane : View("Messages") {

    private val controller: MessagesController by inject()

    override val root = vbox {
        hbox {
            label(controller.chatName) {
                style {
                    fontSize = 24.px
                    fontFamily = "Helvetica"
                }
            }
            alignment = Pos.CENTER
        }
        listview(controller.messageList) {
            vgrow = Priority.ALWAYS
            cellFormat {
                graphic = vbox {
                    label(it.contactInfo ?: "") {
                        isManaged = !it.isFromMe
                    }
                    hbox {
                        alignment = if (it.isFromMe) Pos.CENTER_RIGHT else Pos.CENTER_LEFT
                        label(it.text ?: "") {
                            if (it.attachment.mimeType?.contains("image") == true) {
                                graphic = vbox {
                                    imageview(getImage(it.attachment.filename!!)) {
                                        fitWidth = 300.0
                                        isPreserveRatio = true
                                    }
                                    label(it.text ?: "") {
                                        isWrapText = true
                                    }
                                }
                            }
                            isWrapText = true
                            addClass(MessageStyle.message)
                            if (it.isFromMe) addClass(MessageStyle.fromMe) else addClass(MessageStyle.notFromMe)
                        }
                    }
                }

            }
        }

        paddingAll = 10.0
        spacing = 4.0
    }
}

