package model

data class Contact(
    var firstName: String?,
    var lastName: String?,
    val number: MutableList<String>,
    val email: MutableList<String>
)