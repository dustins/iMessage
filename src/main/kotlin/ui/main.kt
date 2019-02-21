package ui

import mu.KotlinLogging
import tornadofx.*
import ui.styles.MessageStyle
import ui.styles.ScrollbarStyle
import ui.views.AppMenuBar
import ui.views.ConversationsPane
import ui.views.MessagePane
import ui.views.SettingsPane

class Main : App(MainView::class, MessageStyle::class, ScrollbarStyle::class)

class MainView : View("iMessage") {

    init {
        runLater {
            find<SettingsPane>().openWindow()
        }
    }

    override val root = borderpane {
        top(AppMenuBar::class)
        left(ConversationsPane::class)
        center(MessagePane::class)

        prefWidth = 700.0
        prefHeight = 800.0
    }
}

fun main(args: Array<String>) {
    KotlinLogging.logger{}.info("Starting app with args [${args.joinToString()}]")
    launch<Main>(args)
}