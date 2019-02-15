package ui.views

import javafx.scene.paint.Color
import tornadofx.*
import ui.controllers.ConversationController
import ui.viewmodels.SettingsModel
import ui.viewmodels.validateDB

class SettingsPane : View("Settings") {

    private val model: SettingsModel by inject()
    private val controller: ConversationController by inject()

    init {
        runLater {
            model.validationContext.validate()
        }
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