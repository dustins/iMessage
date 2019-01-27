package ui.views

import javafx.application.Platform
import javafx.geometry.HPos
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import tornadofx.*
import ui.controllers.MessagesController
import ui.controllers.SearchOpenedEvent
import utils.highlight
import javax.swing.GroupLayout

class MessagePane : View("MessagePane") {

    private val controller: MessagesController by inject()
    private val filteredMessages = SortedFilteredList(controller.messageList)

    override val root = vbox {
        hbox {
            label {
                text = "Family"
                style {
                    fontSize = 24.px
                    fontFamily = "Helvetica"
                }
            }
            alignment = Pos.CENTER
        }
        listview(filteredMessages) {
            vgrow = Priority.ALWAYS
        }

        textfield(controller.searchTerm) {
            subscribe<SearchOpenedEvent> {
                Platform.runLater { requestFocus() }
            }
            filteredMessages.filterWhen(textProperty()) { query, item ->
                item.contains(query, true)
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

