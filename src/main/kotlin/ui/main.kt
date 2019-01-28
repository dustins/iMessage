package ui

import javafx.geometry.Side
import javafx.scene.control.TabPane
import tornadofx.*
import ui.styles.MessageStyle
import ui.views.ConversationsPane
import ui.views.MessagePane
import ui.views.SettingsPane

class Main : App(MainView::class, MessageStyle::class)

class MainView : View("iMessage") {

    override val root = tabpane {
        tab("Messages") {
            borderpane {
                left(ConversationsPane::class)
                center(MessagePane::class)
            }
        }
        tab(SettingsPane::class)

        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        side = Side.LEFT

        prefWidth = 1024.0
        prefHeight = 768.0
    }
}

fun main(args: Array<String>) {
    launch<Main>(args)
}