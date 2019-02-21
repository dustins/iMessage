package model

data class Contact(
    val firstName: String?,
    val lastName: String?,
    val number: MutableList<String>,
    val email: MutableList<String>,
    val uid: String?
) {
    val displayName: String
        get() {
            return if (!firstName.isNullOrBlank()) {
                firstName
            } else if (!lastName.isNullOrBlank()) {
                lastName
            } else if (number.isNotEmpty()) {
                number[0]
            } else if (email.isNotEmpty()) {
                email[0]
            } else {
                "Unknown"
            }
        }
}

fun List<Contact>.lookup(s: String?): Contact {
    if (s.isNullOrBlank()) {
        return Contact(null, null, mutableListOf(), mutableListOf(), null)
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
        email = mutableListOf(),
        uid = null
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
        email = mutableListOf(emailToFind),
        uid = null
    )
}

private fun normalizeNumber(number: String): String {
    return number.replace(Regex("[^\\d]"), "").takeLast(10)
}