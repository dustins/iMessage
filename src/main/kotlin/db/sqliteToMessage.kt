package db

import model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

fun fetchMessages(messageDB: String, conversation: Conversation): List<Message> {
    Database.connect("jdbc:sqlite:$messageDB", "org.sqlite.JDBC")

    return transaction(Connection.TRANSACTION_SERIALIZABLE, 1) {
        addLogger(StdOutSqlLogger)

        Messages
            .leftJoin(Handles, { Messages.handleID }, { Handles.id })
            .innerJoin(ChatMessageJoin, { Messages.id }, { ChatMessageJoin.messageID })
            .leftJoin(AttachmentMessageJoin, { Messages.id }, { AttachmentMessageJoin.messageID })
            .leftJoin(Attachments, { AttachmentMessageJoin.attachmentID }, { Attachments.id })
            .selectAll()
            .andWhere { ChatMessageJoin.chatID eq conversation.id }
            .map { dbToMessage(it, conversation.contacts) }
    }
}

fun dbToMessage(dbRow: ResultRow, contacts: List<Contact>): Message {
    return Message(
        text = dbRow[Messages.text],
        date = dbRow[Messages.date],
        isFromMe = dbRow[Messages.isFromMe],
        contact = contacts.lookup(dbRow[Handles.contactInfo]),
        attachment = Attachment(
            filename = dbRow[Attachments.filename],
            mimeType = dbRow[Attachments.mimetype]
        )
    )
}
