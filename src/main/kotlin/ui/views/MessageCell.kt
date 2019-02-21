package ui.views

import javafx.geometry.Pos
import model.Message
import tornadofx.*
import ui.styles.MessageStyle

class MessageCell(message: Message, isShowName: Boolean) : View("Message") {

    override val root = vbox {
        label(message.contact.displayName) {
            isManaged = isShowName
        }
        hbox {
            alignment = if (message.isFromMe) {
                Pos.CENTER_RIGHT
            } else {
                Pos.CENTER_LEFT
            }

            label {
                if (message.attachment.isImage) {
                    graphic = imageview {
                        image = message.attachment.load()
                        fitWidth = 300.0
                        isPreserveRatio = true
                    }
                } else {
                    text = message.text
                }

                isWrapText = true

                addClass(MessageStyle.message)
                if (message.isFromMe) {
                    addClass(MessageStyle.fromMe)
                } else {
                    addClass(MessageStyle.notFromMe)
                }
            }
        }
    }
}


