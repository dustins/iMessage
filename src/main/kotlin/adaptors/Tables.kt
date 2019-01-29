package adaptors

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Table

object Attachments : IntIdTable("attachment", "ROWID") {
    val filename = Attachments.text("filename").nullable()
    val mimetype = Attachments.text("mime_type").nullable()
}

object AttachmentMessageJoin : Table("message_attachment_join") {
    val attachmentID = AttachmentMessageJoin.integer("attachment_id")
    val messageID = AttachmentMessageJoin.integer("message_id")
}

object ChatMessageJoins : Table("chat_message_join") {
    val chatID = ChatMessageJoins.integer("chat_id")
    val messageID = ChatMessageJoins.integer("message_id")
    val date = ChatMessageJoins.long("message_date")
}

object Handle : IntIdTable("handle", "ROWID") {
    val contactInfo = Handle.text("id")
}

object Messages : IntIdTable("message", "ROWID") {
    val text = Messages.text("text").nullable()
    val date = Messages.long("date")
    val isFromMe = Messages.bool("is_from_me")
    val handleID = Messages.integer("handle_id")
}