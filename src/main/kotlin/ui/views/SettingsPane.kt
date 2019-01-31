package ui.views

import tornadofx.*
import ui.controllers.SettingsController

class SettingsPane : View("Settings") {

    private val settings: SettingsController by inject()

    override val root = vbox {
        vbox {
            label("Chat DB File Location")
            hbox {
                textfield(settings.messageDB) {
                    prefWidth = 300.0
                }
                paddingBottom = 10.0
            }
            label("Address Book DB File Location")
            hbox {
                textfield(settings.addressDB) {
                    prefWidth = 300.0
                }
                paddingBottom = 10.0
            }
        }
        paddingAll = 10.0
    }
}