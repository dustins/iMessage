package model

data class Contact(
    val firstName: String?,
    val lastName: String?,
    val number: MutableList<String>,
    val email: MutableList<String>
)

// TODO Everything below this line is suspect
fun Contact.displayName(): String {
    return if (!this.firstName.isNullOrBlank()) {
        this.firstName
    } else if (!this.lastName.isNullOrBlank()) {
        this.lastName
    } else if (this.number.isNotEmpty()) {
        this.number[0]
    } else if (this.email.isNotEmpty()) {
        this.email[0]
    } else {
        "Unknown"
    }
}

fun List<Contact>.lookup(s: String?): Contact {
    if (s.isNullOrBlank()) {
        return Contact(null, null, mutableListOf(), mutableListOf())
    }
    if (s.contains("@")) {
        return findByEmail(s)
    }
    return findByNumber(s)
}

private fun List<Contact>.findByNumber(numToFind: String): Contact {
    this.forEach { contact ->
        contact.number.forEach { number ->
            if (normalizeNumber(number) == normalizeNumber(numToFind)) {
                return contact
            }
        }
    }
    return Contact(
        firstName = null,
        lastName = null,
        number = mutableListOf(numToFind),
        email = mutableListOf()
    )
}

private fun List<Contact>.findByEmail(emailToFind: String): Contact {
    this.forEach { contact ->
        contact.email.forEach { email ->
            if (email.contentEquals(emailToFind)) {
                return contact
            }
        }
    }
    return Contact(
        firstName = null,
        lastName = null,
        number = mutableListOf(),
        email = mutableListOf(emailToFind)
    )
}

private fun normalizeNumber(number: String): String {
    return number.replace(Regex("[^\\d]"), "").takeLast(10)
}