package ui.models

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Messages : IntIdTable("message", "ROWID") {
    val text = text("text").nullable()
}

class Message(rowid: EntityID<Int>) : IntEntity(rowid) {
    companion object : IntEntityClass<Message>(Messages)

    var text by Messages.text
}