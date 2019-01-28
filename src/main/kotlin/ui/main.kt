package ui

import javafx.geometry.Side
import javafx.scene.control.TabPane
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import tornadofx.*
import ui.controllers.MessagesController
import ui.controllers.SettingsController
import ui.models.ChatMessageJoins
import ui.models.Message
import ui.models.Messages
import ui.views.MessagePane
import ui.views.SettingsPane
import java.sql.Connection

class Main : App(MainView::class)

class MainView : View("iMessage") {

    override val root = tabpane {
        tab(MessagePane::class)
        tab(SettingsPane::class)
        tab(Fetch::class)

        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        side = Side.LEFT

        prefWidth = 1024.0
        prefHeight = 768.0
    }
}

class Fetch : View("Fetch") {

    val settings: SettingsController by inject()
    val messageController: MessagesController by inject()

    override val root = vbox {
        button {
            text = "Fetch Messages"
            action {
                runAsync {
                    fetch(settings.dblocation.valueSafe)
                } ui {
                    messageController.messageList.addAll(it)
                }
            }
        }
    }
}

fun fetch(dblocation: String): List<Message> {
    Database.connect("jdbc:sqlite:$dblocation", "org.sqlite.JDBC")

    return transaction(Connection.TRANSACTION_SERIALIZABLE, 1) {
        addLogger(StdOutSqlLogger)

        // Get the latest chatID
        val chatID = ChatMessageJoins
            .selectAll()
            .orderBy(ChatMessageJoins.id to false) // Ordering by id instead of date for speed
            .limit(1)
            .first()[ChatMessageJoins.chatID]

        // Get all the messages associated with that chatID
        Messages
            .innerJoin(ChatMessageJoins, { Messages.id }, { ChatMessageJoins.messageID })
            .selectAll()
            .andWhere { ChatMessageJoins.chatID eq chatID }
            .map {
                Message(
                    it[Messages.text]?:"<no text>",
                    it[Messages.date],
                    it[Messages.isFromMe],
                    it[Messages.handleID]
                )
            }
    }
}

fun main(args: Array<String>) {
    launch<Main>(args)
}