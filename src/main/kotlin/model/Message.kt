package model

import javafx.scene.image.Image
import mu.KotlinLogging
import java.io.File
import java.time.LocalDateTime

data class Message(
    val text: String,
    val date: LocalDateTime,
    val isFromMe: Boolean,
    val contact: Contact,
    val attachment: Attachment
)

data class Attachment(val filename: String?, val mimeType: String?) {

    val isImage: Boolean
        get() = this.mimeType?.contains("image") == true

    fun load(): Image {
        val logger = KotlinLogging.logger {}

        if (this.filename.isNullOrBlank()) {
            logger.warn("Filename is blank")
            return Image("placeholder.jpg")
        }

        val file = File(
            this.filename.replaceFirst(
                Regex("^~"),
                System.getProperty("user.home")
            )
        )

        if (!file.exists()) {
            logger.warn("File does not exist: $file")
            return Image("placeholder.jpg")
        }
        if (!file.canRead()) {
            logger.warn("Permission Denied: $file")
            return Image("placeholder.jpg")
        }

        return Image(file.toURI().toString())
    }
}
