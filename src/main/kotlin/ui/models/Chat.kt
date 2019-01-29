package ui.models

import org.jetbrains.exposed.dao.IntIdTable

object Chats : IntIdTable("chat", "ROWID") {
    val lastReadTimestamp = long("last_read_message_timestamp")
}