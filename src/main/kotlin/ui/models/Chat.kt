package ui.models

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Chats : IntIdTable("chat", "ROWID") {
    val lastReadTimestamp = integer("last_read_message_timestamp")
}

class Chat(rowid: EntityID<Int>) : IntEntity(rowid) {
    companion object : IntEntityClass<Chat>(Chats)

    var lastReadTimestamp by Chats.lastReadTimestamp
}