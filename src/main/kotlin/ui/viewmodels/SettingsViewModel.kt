package ui.viewmodels

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import org.sqlite.SQLiteErrorCode
import org.sqlite.SQLiteException
import plugins.GoogleEarthPlugin
import tornadofx.ItemViewModel
import tornadofx.ValidationContext
import tornadofx.ValidationMessage
import java.io.File
import java.sql.DriverManager

class Settings {
    val messageDBProperty = SimpleStringProperty("${System.getProperty("user.home")}/Library/Messages/chat.db")
    val addressBookDBProperty = SimpleStringProperty(findAddressBook())
    val isPluginEnabledProperty = SimpleBooleanProperty()
}

class SettingsModel(settings: Settings? = Settings()) : ItemViewModel<Settings>(settings) {
    val messageDB = bind(Settings::messageDBProperty)
    val addressBookDB = bind(Settings::addressBookDBProperty)
    val isPluginEnabled = bind(Settings::isPluginEnabledProperty)

    private val plugin = GoogleEarthPlugin()

    override fun onCommit() {
        super.onCommit()
        if (isPluginEnabled.value) {
            plugin.unload()
            plugin.load()
        } else {
            plugin.unload()
        }
    }
}

fun findAddressBook(): String? {
    val rootDir = "${System.getProperty("user.home")}/Library/Application Support/AddressBook/Sources"

    //For now we'll assume the biggest address book is the one we want.
    //In the future we'd like to merge all available address books
    val db = File(rootDir).walk()
        .filter { it.toString().endsWith("db") }
        .maxBy { it.length() }

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