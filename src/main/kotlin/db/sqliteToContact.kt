package db

import model.Contact
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

fun fetchFromDB(addressDB: String): List<Contact> {
    Database.connect("jdbc:sqlite:$addressDB", "org.sqlite.JDBC")

    val contacts = mutableMapOf<Int, Contact>()

    transaction(Connection.TRANSACTION_SERIALIZABLE, 1) {
        addLogger(StdOutSqlLogger)

        Name
            .leftJoin(EmailAddress, { Name.id }, { EmailAddress.owner })
            .leftJoin(PhoneNumber, { Name.id }, { PhoneNumber.owner })
            .selectAll()
            .forEach {
                val key = it[Name.id].value
                if (!contacts.containsKey(key)) {
                    contacts[key] = Contact (
                        firstName = it[Name.firstName],
                        lastName = it[Name.lastName],
                        email = mutableListOf(),
                        number = mutableListOf()
                    )
                }

                contacts[key]!!.email.add(it[EmailAddress.address])
                contacts[key]!!.number.add(it[PhoneNumber.fullNumber])
            }
    }

    return contacts.values.toList()
}

fun main() {
    fetchFromDB("/Users/Kevin/Desktop/address.db")
        .forEach { println(it) }
}