package ui.models

import org.jetbrains.exposed.dao.IntIdTable

object Messages : IntIdTable("message", "ROWID") {
    val text = text("text").nullable()
    val date = long("date")
    val isFromMe = bool("is_from_me")
    val handleID = integer("handle_id")
}

data class Message(
    val text: String?,
    val date: Long,
    val isFromMe: Boolean,
    val contactInfo: String?,
    val filename: String?,
    val mimeType: String?
)
