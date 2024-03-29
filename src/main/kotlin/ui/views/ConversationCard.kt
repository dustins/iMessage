package ui.views

import javafx.geometry.Pos
import javafx.scene.control.OverrunStyle
import javafx.scene.image.Image
import javafx.scene.shape.Circle
import javafx.scene.text.FontWeight
import model.Conversation
import tornadofx.*
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConversationCard(private val conversation: Conversation) : View("ConversationCard") {

    override val root = hbox {
        imageview {
            image = findImage(conversation.contacts.firstOrNull()?.uid)
            clip = Circle(25.0, 25.0,25.0)
            fitWidth = 50.0
            fitHeight = 50.0
            paddingAll = 5.0
        }
        vbox {
            prefWidth = 180.0
            paddingLeft = 5.0
            hbox {
                label(conversation.displayName) {
                    alignment = Pos.TOP_LEFT
                    minWidth = 110.0
                    isWrapText = false
                    textOverrun = OverrunStyle.ELLIPSIS
                    style {
                        fontFamily = "Helvetica"
                        fontWeight = FontWeight.EXTRA_BOLD
                    }
                }
                label(formatDate(conversation.lastSentMessage.date)) {
                    alignment = Pos.TOP_RIGHT
                    minWidth = 65.0
                    style {
                        fontFamily = "Helvetica"
                        fontWeight = FontWeight.LIGHT
                    }
                }
            }
            label {
                text = conversation
                    .lastSentMessage
                    .text
                    .replace(Regex("[\n\t\r]"), " ")
                paddingTop = 5.0
                textOverrun = OverrunStyle.ELLIPSIS
            }
        }
    }
}

fun formatDate(date: LocalDateTime): String {
    if (date.toLocalDate() == LocalDate.now()) {
        return date.format(DateTimeFormatter.ofPattern("hh:mm a"))
    }
    return date.format(DateTimeFormatter.ofPattern("M/d/yy"))
}

fun findImage(uid: String?): Image {
    val rootDir = "${System.getProperty("user.home")}/Library/Application Support/AddressBook/"
    val image = File(rootDir).walk().find { it.name == uid }

    return Image(image?.toURI()?.toString() ?: "default_avatar.png")
}