package db

import model.Contact
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

fun fetchContacts(addressDB: String): List<Contact> {
    Database.connect("jdbc:sqlite:$addressDB", "org.sqlite.JDBC")

    val contacts = mutableMapOf<Int, Contact>()

    transaction(Connection.TRANSACTION_SERIALIZABLE, 1) {
        addLogger(StdOutSqlLogger)

        Name
            .leftJoin(EmailAddress, { Name.id }, { EmailAddress.owner })
            .leftJoin(PhoneNumber, { Name.id }, { PhoneNumber.owner })
            .selectAll()
            .forEach { mapContact(contacts, it) }
    }

    return contacts.values.toList()
}

@Suppress("UselessCallOnNotNull") // db results can definitely be null
fun mapContact(contacts: MutableMap<Int, Contact>, dbRow: ResultRow) {
    val key = dbRow[Name.id].value
    if (!contacts.containsKey(key)) {
        contacts[key] = Contact (
            firstName = dbRow[Name.firstName],
            lastName = dbRow[Name.lastName],
            email = mutableListOf(),
            number = mutableListOf()
        )
    }

    if (!dbRow[EmailAddress.address].isNullOrBlank()) {
        contacts[key]!!.email.add(dbRow[EmailAddress.address])
    }
    if (!dbRow[PhoneNumber.fullNumber].isNullOrBlank()) {
        contacts[key]!!.number.add(dbRow[PhoneNumber.fullNumber])
    }
}

