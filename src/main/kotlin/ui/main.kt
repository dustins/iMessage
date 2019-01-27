package ui

import javafx.geometry.Side
import javafx.scene.control.TabPane
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import tornadofx.*
import ui.controllers.MessagesController
import ui.controllers.SettingsController
import ui.models.ChatMessageJoin
import ui.models.Message
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

fun fetch(dblocation: String): List<String> {
    Database.connect("jdbc:sqlite:$dblocation", "org.sqlite.JDBC")

    return transaction(Connection.TRANSACTION_SERIALIZABLE, 1) {
        addLogger(StdOutSqlLogger)

        // Get the most recent chat:
        // select chat_id from chat_message_join order by message_date desc limit 1;
        val chatID = ChatMessageJoin.all().last().chatID
        println(chatID)

        // Get all the message IDs associated with that chat_id:
        // select message_id from chat_message_join where chat_id = 259 order by message_date desc limit 10;
        val messageIDs = ChatMessageJoin.all()
            .filter { it.chatID == chatID }
            .map { it.messageID }
            .toSet()

        // Get all the messages for that chat
        Message
            .all()
            .filter { messageIDs.contains(it.id.value) }
            .map { it.text?:"<no text>"}
    }
}

fun main(args: Array<String>) {
    launch<Main>(args)
}