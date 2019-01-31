package ui.views

import tornadofx.*

class AppMenuBar : View("Menu Bar") {
    override val root = menubar {
        isUseSystemMenuBar = true
        menu("File") {
            item("Settings").action { find<SettingsPane>().openWindow() }
        }
    }
}
