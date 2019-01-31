package db

import model.Attachment
import model.Message
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

fun fetchFromDB(chatID: Int, messageDB: String): List<Message> {
    Database.connect("jdbc:sqlite:$messageDB", "org.sqlite.JDBC")

    return transaction(Connection.TRANSACTION_SERIALIZABLE, 1) {
        addLogger(StdOutSqlLogger)

        Messages
            .leftJoin(Handle, { Messages.handleID }, { Handle.id })
            .innerJoin(ChatMessageJoins, { Messages.id }, { ChatMessageJoins.messageID })
            .leftJoin(AttachmentMessageJoin, { Messages.id }, { AttachmentMessageJoin.messageID })
            .leftJoin(Attachments, { AttachmentMessageJoin.attachmentID }, { Attachments.id })
            .selectAll()
            .andWhere { ChatMessageJoins.chatID eq chatID }
            .map { dbToMessage(it) }
    }
}

fun dbToMessage(dbRow: ResultRow): Message {
    return Message(
        text = dbRow[Messages.text],
        date = dbRow[Messages.date],
        isFromMe = dbRow[Messages.isFromMe],
        contactInfo = dbRow[Handle.contactInfo],
        attachment = Attachment(
            filename = dbRow[Attachments.filename],
            mimeType = dbRow[Attachments.mimetype]
        )
    )
}
