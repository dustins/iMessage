package db

import model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

fun fetchConversations(messageDB: String, contacts: List<Contact>): List<Conversation> {
    Database.connect("jdbc:sqlite:$messageDB", "org.sqlite.JDBC")

    val chatIdToContacts = mutableMapOf<Int, MutableList<Contact>>()
    transaction(Connection.TRANSACTION_SERIALIZABLE, 1) {
        registerInterceptor(TimingInterceptor())
        ChatHandleJoin
            .leftJoin(Handles, { ChatHandleJoin.handleID }, { Handles.id })
            .selectAll()
            .forEach { mapToContacts(chatIdToContacts, it, contacts) }
    }

    val messages = fetchLastMessageByChat(messageDB, contacts)

    return chatIdToContacts
        .filter { messages.containsKey(it.key) }
        .map { Conversation(it.key, it.value, messages.getValue(it.key)) }
        .sortedByDescending { it.lastSentMessage.date }
}

// TODO Make this faster if possible
// This seems to be the most expensive operation related to startup, and cursory
// measurements have it between 600ms and 3000ms
fun fetchLastMessageByChat(messageDB: String, contacts: List<Contact>): Map<Int, Message> {
    Database.connect("jdbc:sqlite:$messageDB", "org.sqlite.JDBC")

    val messages = mutableMapOf<Int, Message>()
    transaction(Connection.TRANSACTION_SERIALIZABLE, 1) {
        registerInterceptor(TimingInterceptor())
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

@Suppress("UselessCallOnNotNull") // db results can definitely be null
fun mapToContacts(chatIdToContacts: MutableMap<Int, MutableList<Contact>>, dbRow: ResultRow, contacts: List<Contact>) {
    val key = dbRow[ChatHandleJoin.chatID]

    if (!dbRow[Handles.contactInfo].isNullOrBlank()) {
        chatIdToContacts.putIfAbsent(key, mutableListOf())
        chatIdToContacts.getValue(key).add(contacts.lookup(dbRow[Handles.contactInfo]))
    }
}
