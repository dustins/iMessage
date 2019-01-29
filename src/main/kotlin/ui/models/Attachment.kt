package ui.models

import org.jetbrains.exposed.dao.IntIdTable

object Attachments : IntIdTable("attachment", "ROWID") {
    val filename = text("filename").nullable()
    val mimetype = text("mime_type").nullable()
}