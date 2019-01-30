package ui.views

import javafx.geometry.Pos
import model.Message
import tornadofx.*
import ui.styles.MessageStyle
import utils.ImageLoader
import utils.hasImage

class MessageCell(message: Message) : View("Message") {

    override val root = vbox {
        label(message.contactInfo ?: "") {
            isManaged = !message.isFromMe
        }
        hbox {
            alignment = if (message.isFromMe) {
                Pos.CENTER_RIGHT
            } else {
                Pos.CENTER_LEFT
            }

            label {
                if (message.hasImage()) {
                    graphic = imageview(ImageLoader().load(message.attachment.filename)) {
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
