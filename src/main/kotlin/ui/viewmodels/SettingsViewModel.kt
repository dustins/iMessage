package ui.viewmodels

import org.sqlite.SQLiteErrorCode
import org.sqlite.SQLiteException
import tornadofx.ItemViewModel
import tornadofx.ValidationContext
import tornadofx.ValidationMessage
import tornadofx.toProperty
import java.io.File
import java.sql.DriverManager

data class SettingsModel(val addressBookDB: String? = null, val messageDB: String? = null)

class SettingsViewModel : ItemViewModel<SettingsModel>() {
    val addressBookDB = bind { item?.addressBookDB?.toProperty() }
    val messageDB = bind { item?.messageDB?.toProperty() }
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

