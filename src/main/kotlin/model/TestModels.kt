package model

import java.time.LocalDateTime

fun SampleConversation(): Conversation {
    return Conversation(
        1,
        mutableListOf(SampleContact()),
        SampleMessage()
    )
}

fun SampleMessage(): Message {
    return Message(
        text = "Hey what's up",
        date = LocalDateTime.now(),
        isFromMe = false,
        contact = SampleContact(),
        attachment = SampleAttachment()
    )
}

fun SampleContact(): Contact {
    return Contact(
        firstName = "Jane",
        lastName = "Doe",
        number = mutableListOf("555-867-5309"),
        email = mutableListOf("jane@gmail.com")
    )
}

fun SampleAttachment(): Attachment {
    return Attachment(null, null)
}