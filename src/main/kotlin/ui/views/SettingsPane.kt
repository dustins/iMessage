package ui.views

import javafx.scene.paint.Color
import tornadofx.*
import ui.controllers.ConversationController
import ui.viewmodels.SettingsViewModel
import ui.viewmodels.validateDB
import java.io.File

class SettingsPane : View("Settings") {

    private val model: SettingsViewModel by inject()
    private val controller: ConversationController by inject()

    init {
        model.messageDB.value = "${System.getProperty("user.home")}/Library/Messages/chat.db"
        model.addressBookDB.value = findAddressBook()
    }

    override val root = form {
        prefWidth = 400.0
        fieldset {
            field("Message DB:") {
                textfield(model.messageDB).validator {
                    validateDB(this, it)
                }
            }
            field("Address DB:") {
                textfield(model.addressBookDB).validator {
                    validateDB(this, it)
                }
            }
        }
        buttonbar {
            button("Cancel") {
                action {
                    model.rollback()
                    close()
                }
            }
            button("Save") {
                enableWhen(model.valid)
                style {
                    baseColor = c("#2278ff")
                    textFill = Color.WHITE
                }
                action {
                    model.commit()
                    controller.loadConversations()
                    close()
                }
            }
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