package db

import model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

fun fetchConversations(messageDB: String, contacts: List<Contact>): List<Conversation> {
    Database.connect("jdbc:sqlite:$messageDB", "org.sqlite.JDBC")

    val conversations = mutableMapOf<Int, Conversation>()
    transaction(Connection.TRANSACTION_SERIALIZABLE, 1) {
        addLogger(StdOutSqlLogger)

        ChatHandleJoin
            .leftJoin(Handles, { ChatHandleJoin.handleID }, { Handles.id })
            .selectAll()
            .forEach { mapToConversation(conversations, it, contacts) }
    }

    val messages = fetchLastMessageByChat(messageDB, contacts)

    return conversations.values
        .filter { messages.containsKey(it.id) }
        .map { Conversation(it.id, it.contacts, messages.getValue(it.id)) }
        .sortedByDescending { it.lastSentMessage.date }
}

// TODO Make this faster if possible
// This seems to be the most expensive operation related to startup, and cursory
// measurements have it between 600ms and 3000ms
fun fetchLastMessageByChat(messageDB: String, contacts: List<Contact>): Map<Int, Message> {
    Database.connect("jdbc:sqlite:$messageDB", "org.sqlite.JDBC")

    val messages = mutableMapOf<Int, Message>()
    transaction(Connection.TRANSACTION_SERIALIZABLE, 1) {
        addLogger(StdOutSqlLogger)

        ChatMessageJoin
            .leftJoin(Messages, { ChatMessageJoin.messageID }, { Messages.id })
            .leftJoin(Handles, { Messages.handleID }, { Handles.id })
            .leftJoin(AttachmentMessageJoin, { Messages.id }, { AttachmentMessageJoin.messageID })
            .leftJoin(Attachments, { AttachmentMessageJoin.attachmentID }, { Attachments.id })
            .selectAll()
            .groupBy(ChatMessageJoin.chatID)
            .orderBy(ChatMessageJoin.date.max(), false)
            .forEach { messages[it[ChatMessageJoin.chatID]] = dbToMessage(it, contacts) }
    }

    return messages
}

fun mapToConversation(conversations: MutableMap<Int, Conversation>, dbRow: ResultRow, contacts: List<Contact>) {
    val key = dbRow[ChatHandleJoin.chatID]
    if (!conversations.containsKey(key)) {
        // TODO replace SampleMessage with the real deal
        conversations[key] = Conversation(key, mutableListOf(), SampleMessage())
    }

    if (!dbRow[Handles.contactInfo].isNullOrBlank()) {
        conversations[key]!!.contacts.add(contacts.lookup(dbRow[Handles.contactInfo]))
    }
}
