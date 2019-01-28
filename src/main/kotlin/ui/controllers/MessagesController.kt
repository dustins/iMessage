package ui.controllers

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import tornadofx.Controller
import tornadofx.EventBus
import tornadofx.FXEvent
import tornadofx.observable
import ui.models.ChatMessageJoins
import ui.models.Message
import ui.models.Messages
import java.sql.Connection

class MessagesController : Controller() {

    val searchTerm = SimpleStringProperty()
    val isSearchVisible = SimpleBooleanProperty()
    val messageList = mutableListOf<Message>().observable()
    val settings: SettingsController by inject()
    val chatName = SimpleStringProperty()

    fun handleKeyPress(keyEvent: KeyEvent) {
        val findMac = KeyCodeCombination(KeyCode.F, KeyCombination.META_DOWN)
        if (findMac.match(keyEvent)) {
            isSearchVisible.value = true
            fire(SearchOpenedEvent())
        } else if (keyEvent.code == KeyCode.ESCAPE) {
            isSearchVisible.value = false
        }
    }

    fun fetch(chatID: Int) {
        chatName.value = chatID.toString()

        Database.connect("jdbc:sqlite:${settings.dblocation.valueSafe}", "org.sqlite.JDBC")

        val messages = transaction(Connection.TRANSACTION_SERIALIZABLE, 1) {
            addLogger(StdOutSqlLogger)

            // Get all the messages associated with that chatID
            Messages
                .innerJoin(ChatMessageJoins, { id }, { messageID })
                .selectAll()
                .andWhere { ChatMessageJoins.chatID eq chatID }
                .map {
                    Message(
                        it[Messages.text] ?: "<no text>",
                        it[Messages.date],
                        it[Messages.isFromMe],
                        it[Messages.handleID]
                    )
                }
        }
        messageList.clear()
        messageList.addAll(messages)
    }
}


// TODO I don't know where to put this
class SearchOpenedEvent : FXEvent(EventBus.RunOn.BackgroundThread)