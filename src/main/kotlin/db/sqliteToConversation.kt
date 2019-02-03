package db

import model.Contact
import model.Conversation
import model.SampleMessage
import model.lookup
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

    return conversations.values.toList()
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
