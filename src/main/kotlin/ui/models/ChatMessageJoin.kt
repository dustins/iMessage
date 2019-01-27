package ui.models

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object ChatMessageJoins : IntIdTable("chat_message_join", "ROWID") {
    val chatID = integer("chat_id")
    val messageID = integer("message_id")
    val date = integer("message_date")
}

class ChatMessageJoin(rowid: EntityID<Int>) : IntEntity(rowid) {
    companion object : IntEntityClass<ChatMessageJoin>(ChatMessageJoins)

    var chatID by ChatMessageJoins.chatID
    var messageID by ChatMessageJoins.messageID
    var date by ChatMessageJoins.date
}