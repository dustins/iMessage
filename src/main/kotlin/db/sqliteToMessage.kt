package db

import model.Attachment
import model.Message
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

fun fetchMessages(chatID: Int, messageDB: String): List<Message> {
    Database.connect("jdbc:sqlite:$messageDB", "org.sqlite.JDBC")

    return transaction(Connection.TRANSACTION_SERIALIZABLE, 1) {
        addLogger(StdOutSqlLogger)

        Messages
            .leftJoin(Handles, { Messages.handleID }, { Handles.id })
            .innerJoin(ChatMessageJoin, { Messages.id }, { ChatMessageJoin.messageID })
            .leftJoin(AttachmentMessageJoin, { Messages.id }, { AttachmentMessageJoin.messageID })
            .leftJoin(Attachments, { AttachmentMessageJoin.attachmentID }, { Attachments.id })
            .selectAll()
            .andWhere { ChatMessageJoin.chatID eq chatID }
            .map { dbToMessage(it) }
    }
}

fun dbToMessage(dbRow: ResultRow): Message {
    return Message(
        text = dbRow[Messages.text],
        date = dbRow[Messages.date],
        isFromMe = dbRow[Messages.isFromMe],
        contactInfo = dbRow[Handles.contactInfo],
        attachment = Attachment(
            filename = dbRow[Attachments.filename],
            mimeType = dbRow[Attachments.mimetype]
        )
    )
}
