package adaptors

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ui.models.ChatMessageJoins
import ui.models.Message
import ui.models.Messages
import java.sql.Connection

fun fetchFromDB(chatID: Int, messageDB: String): List<Message> {
    Database.connect("jdbc:sqlite:$messageDB", "org.sqlite.JDBC")

    return transaction(Connection.TRANSACTION_SERIALIZABLE, 1) {
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
}