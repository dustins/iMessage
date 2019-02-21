package model

data class Conversation(
    val id: Int,
    val contacts: MutableList<Contact>,
    val lastSentMessage: Message
) {
    val displayName: String
        get() = contacts.joinToString { it.displayName }
}
