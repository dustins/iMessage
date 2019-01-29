package ui.models

import org.jetbrains.exposed.sql.Table

object AttachmentMessageJoin : Table("message_attachment_join") {
    val attachmentID = integer("attachment_id")
    val messageID = integer("message_id")
}