package model

import java.time.LocalDateTime

data class Message(
    val text: String,
    val date: LocalDateTime,
    val isFromMe: Boolean,
    val contact: Contact,
    val attachment: Attachment
)

data class Attachment(
    val filename: String?,
    val mimeType: String?
)
