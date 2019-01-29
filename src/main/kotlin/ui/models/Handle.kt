package ui.models

import org.jetbrains.exposed.dao.IntIdTable

object Handle : IntIdTable("handle", "ROWID") {
    val contactInfo = text("id")
}