package ui.views

import javafx.geometry.Pos
import model.Message
import model.SampleMessage
import plugins.openGoogleEarth
import tornadofx.*
import ui.styles.MessageStyle
import ui.styles.ScrollbarStyle

class Test : App(TestView::class, MessageStyle::class, ScrollbarStyle::class)

class TestView : View("test") {
    override val root = MessageCell(SampleMessage(), false).root
}

fun main(args: Array<String>) {
    launch<Test>(args)
}

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
                    graphic = vbox {
                        imageview {
                            image = message.attachment.load()
                            fitWidth = 300.0
                            isPreserveRatio = true
                        }
                        button {
                            graphic = imageview("earth.png") {
                                fitWidth = 24.0
                                fitHeight = 24.0
                            }
                            action {
                                openGoogleEarth(message.attachment)
                            }
                        }
                        alignment = Pos.CENTER_RIGHT
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