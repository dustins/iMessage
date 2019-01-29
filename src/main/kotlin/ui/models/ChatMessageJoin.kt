package ui.models

import org.jetbrains.exposed.sql.Table

object ChatMessageJoins : Table("chat_message_join") {
    val chatID = integer("chat_id")
    val messageID = integer("message_id")
    val date = long("message_date")
}
