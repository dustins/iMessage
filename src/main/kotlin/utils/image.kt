package utils

import javafx.scene.image.Image
import model.Message
import mu.KotlinLogging
import java.io.File


class ImageLoader {

    private val logger = KotlinLogging.logger{}

    companion object {
        private val placeholderImage = Image(ImageLoader::class.java.getResource("/placeholder.jpg").toURI().toString())
    }

    fun load(filename: String?): Image {

        if (filename == null) {
            logger.warn("filename is null")
            return placeholderImage
        }

        val file = File(
            filename.replaceFirst(
                Regex("^~"),
                System.getProperty("user.home")
            )
        )

        if (!file.exists()) {
            logger.warn("File does not exist: $file")
            return placeholderImage
        } else if (!file.canRead()) {
            logger.warn("Permission Denied: $file")
            return placeholderImage
        }

        logger.info("Loading file: $file")
        return Image(file.toURI().toString())
    }
}

fun Message.hasImage(): Boolean {
    return (this.attachment.mimeType?.contains("image") == true) &&
            !this.attachment.filename.isNullOrBlank()
}

