package ui.views

import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import tornadofx.*
import ui.controllers.MessagesController
import ui.controllers.SearchOpenedEvent
import ui.styles.MessageStyle


class MessagePane : View("Messages") {

    private val controller: MessagesController by inject()
    private val filteredMessages = SortedFilteredList(controller.messageList)

    override val root = vbox {
        hbox {
            label(controller.chatName) {
                style {
                    fontSize = 24.px
                    fontFamily = "Helvetica"
                }
            }
            alignment = Pos.CENTER
        }
        listview(filteredMessages) {
            vgrow = Priority.ALWAYS
            cellFormat {
                graphic = vbox {
                    label(it.handleID.toString()) {
                        isVisible = !it.isFromMe
                        isManaged = !it.isFromMe
                    }
                    hbox {
                        alignment = if (it.isFromMe) Pos.CENTER_RIGHT else Pos.CENTER_LEFT
                        label(it.text) {
                            isWrapText = true
                            addClass(MessageStyle.message)
                            if (it.isFromMe) addClass(MessageStyle.fromMe) else addClass(MessageStyle.notFromMe)
                        }
                    }
                }

            }
        }

        textfield(controller.searchTerm) {
            subscribe<SearchOpenedEvent> {
                Platform.runLater { requestFocus() }
            }
            filteredMessages.filterWhen(textProperty()) { query, item ->
                item.text.contains(query, true)
            }
            managedProperty().bind(visibleProperty())
            visibleWhen(controller.isSearchVisible)
        }

        setOnKeyPressed {
            controller.handleKeyPress(it)
        }

        paddingAll = 10.0
        spacing = 4.0
    }
}

