package ui.views

import javafx.scene.control.ButtonBar
import javafx.scene.paint.Color
import tornadofx.*
import ui.controllers.ConversationController
import ui.viewmodels.SettingsModel
import ui.viewmodels.validateDB

class SettingsPane : View("Settings") {

    private val model: SettingsModel by inject()
    private val controller: ConversationController by inject()

    override fun onDock() {
        model.validationContext.validate()
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
            button("Help", ButtonBar.ButtonData.LEFT) {
                graphic = imageview("help/help_icon.png") {
                    fitWidth = 18.0
                    fitHeight = 18.0
                }
                action {
                    find<Help>().openWindow()
                }
            }
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