package model

data class Conversation(
    var id: Int,
    val contacts: MutableList<Contact>,
    val lastSentMessage: Message
)

fun Conversation.displayName(): String {
    return this.contacts.joinToString { it.displayName() }
}
