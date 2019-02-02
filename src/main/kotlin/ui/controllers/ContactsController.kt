package ui.controllers

import db.fetchContacts
import model.Contact
import tornadofx.Controller
import tornadofx.observable

class ContactsController : Controller() {

    val contacts = mutableListOf<Contact>().observable()
    val settings: SettingsController by inject()

    fun loadContacts() {
        contacts.clear()
        runAsync {
            fetchContacts(settings.addressDB.valueSafe)
        } ui {
            contacts.addAll(it)
        }
    }

    fun find(s: String): Contact? {
        if (s.contains("@")) {
            return findByEmail(s)
        }
        return findByNumber(s)
    }

    private fun findByEmail(s: String): Contact? {
        contacts.forEach {contact ->
            contact.email.forEach { email ->
                if (email.contentEquals(s)) {
                    return contact
                }
            }
        }
        return null
    }

    private fun findByNumber(s: String): Contact? {
        contacts.forEach {contact ->
            contact.number.forEach { number ->
                if (normalizeNumber(number) == normalizeNumber(s)) {
                    return contact
                }
            }
        }
        return null
    }
}

fun normalizeNumber(number: String): String {
    return number.replace(Regex("[^\\d]"), "").takeLast(10)
}