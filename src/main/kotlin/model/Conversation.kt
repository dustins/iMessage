package model

data class Conversation(
    var id: Int,
    val contacts: MutableList<Contact>,
    val lastSentMessage: Long
)
