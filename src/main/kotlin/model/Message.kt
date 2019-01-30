package model

data class Message(
    val text: String?,
    val date: Long,
    val isFromMe: Boolean,
    val contactInfo: String?,
    val attachment: Attachment
)

data class Attachment(
    val filename: String?,
    val mimeType: String?
)