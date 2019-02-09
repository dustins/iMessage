package ui.viewmodels

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
    when {
        filename.isNullOrBlank() -> return context.error("Cannot be blank")
        !File(filename).exists() -> return context.error("File does not exist")
        !File(filename).isFile -> return context.error("Not a file")
        !File(filename).canRead() -> return context.error("Permission Denied")
    }

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
        return context.error(e.resultCode.message)
    }

    return context.success()
}


