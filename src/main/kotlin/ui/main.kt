package ui

import tornadofx.App
import tornadofx.View
import tornadofx.borderpane
import tornadofx.launch
import ui.styles.MessageStyle
import ui.views.AppMenuBar
import ui.views.ConversationsPane
import ui.views.MessagePane

class Main : App(MainView::class, MessageStyle::class)

class MainView : View("iMessage") {

    override val root = borderpane {
        top(AppMenuBar::class)
        left(ConversationsPane::class)
        center(MessagePane::class)

        prefWidth = 1024.0
        prefHeight = 768.0
    }
}

fun main(args: Array<String>) {
    launch<Main>(args)
}