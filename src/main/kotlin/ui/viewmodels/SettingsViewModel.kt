package ui.viewmodels

import javafx.beans.property.SimpleStringProperty
import mu.KotlinLogging
import org.sqlite.SQLiteErrorCode
import org.sqlite.SQLiteException
import tornadofx.*
import java.io.File
import java.sql.DriverManager

class Settings {
    val messageDBProperty = SimpleStringProperty("${System.getProperty("user.home")}/Library/Messages/chat.db")
    val addressBookDBProperty = SimpleStringProperty(findAddressBook())
}

class SettingsModel(settings: Settings? = Settings()) : ItemViewModel<Settings>(settings) {
    val messageDB = bind(Settings::messageDBProperty)
    val addressBookDB = bind(Settings::addressBookDBProperty)
}

fun findAddressBook(): String? {
    val logger = KotlinLogging.logger {}

    val rootDir = "${System.getProperty("user.home")}/Library/Application Support/AddressBook/Sources"
    logger.info("Checking for address book in: $rootDir")

    //For now we'll assume the biggest address book is the one we want.
    //In the future we'd like to merge all available address books
    val db = File(rootDir).walk()
        .filter { it.toString().endsWith("db") }
        .onEach { logger.info("Found candidate address book: $it") }
        .maxBy { it.length() }

    logger.info("Chosen address book db: ${db?.path}")

    return db?.path
}

fun validateDB(context: ValidationContext, filename: String?): ValidationMessage? {
    return when {
        filename.isNullOrBlank() -> context.error("Cannot be blank")
        !File(filename).exists() -> context.error("File does not exist")
        !File(filename).isFile -> context.error("Not a file")
        !File(filename).canRead() -> context.error("Permission Denied")
        else -> when (val code = canConnect(filename)) {
            SQLiteErrorCode.SQLITE_OK -> context.success()
            else -> context.error(code.message)
        }
    }
}

fun canConnect(filename: String): SQLiteErrorCode {
    // connection.isValid returns "true" even if the file is not a database.
    // Therefore, we must a different approach to test the connection
    Class.forName("org.sqlite.JDBC")
    try {
        val connection = DriverManager.getConnection("jdbc:sqlite:$filename")
        val tables = connection.metaData.getTables(null, null, "%", null)
        tables.next()
    } catch (e: SQLiteException) {
        // This is rather lazy, but there are a lot of ways the connection could fail
        // Also, the resultCode messages are better than the exception messages IMO
        return e.resultCode
    }

    return SQLiteErrorCode.SQLITE_OK
}

