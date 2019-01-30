package utils

import javafx.scene.image.Image
import java.io.File

fun getImage(filename: String): Image {
    val file = File(
        filename.replaceFirst(
            Regex("^~"),
            System.getProperty("user.home")
        )
    )

    if (!file.exists()) {
        println("File does not exist! $file")
    } else if (!file.canRead()) {
        println("Permission Denied! $file")
    }

    return Image(file.toURI().toString())
}