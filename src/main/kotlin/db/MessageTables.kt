package db

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Table

object Attachments : IntIdTable("attachment", "ROWID") {
    val filename = text("filename").nullable()
    val mimetype = text("mime_type").nullable()
}

object AttachmentMessageJoin : Table("message_attachment_join") {
    val attachmentID = integer("attachment_id")
    val messageID = integer("message_id")
}

object ChatMessageJoin : Table("chat_message_join") {
    val chatID = integer("chat_id")
    val messageID = integer("message_id")
    val date = long("message_date")
}

object ChatHandleJoin : Table("chat_handle_join") {
    val chatID = integer("chat_id")
    val handleID = integer("handle_id")
}

object Handles : IntIdTable("handle", "ROWID") {
    val contactInfo = text("id")
}

object Messages : IntIdTable("message", "ROWID") {
    val text = text("text").nullable()
    val date = long("date")
    val isFromMe = bool("is_from_me")
    val handleID = integer("handle_id")
}