package ui.models

import org.jetbrains.exposed.dao.IntIdTable

object ChatMessageJoins : IntIdTable("chat_message_join", "ROWID") {
    val chatID = integer("chat_id")
    val messageID = integer("message_id")
    val date = long("message_date")
}
