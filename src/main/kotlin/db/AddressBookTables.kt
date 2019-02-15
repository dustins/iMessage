package db

import org.jetbrains.exposed.dao.IntIdTable

object Name : IntIdTable("zabcdRecord", "Z_PK") {
    val firstName = varchar("zFirstname", 50).nullable()
    val lastName = varchar("zLastname", 50).nullable()
    val uid = varchar("zUniqueID", 50)
}

object PhoneNumber : IntIdTable("zabcdPhoneNumber", "Z_PK") {
    val owner = integer("zOwner")
    val fullNumber = varchar("zFullNumber", 20)
    val uid = varchar("zUniqueID", 50)
}

object EmailAddress : IntIdTable("zabcdEmailAddress", "Z_PK") {
    val owner = integer("zOwner")
    val address = varchar("zAddressNormalized", 50)
    val uid = varchar("zUniqueID", 50)
}