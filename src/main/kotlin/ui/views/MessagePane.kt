package ui.views

import javafx.application.Platform
import javafx.scene.layout.Priority
import tornadofx.*
import ui.controllers.MessagesController
import ui.controllers.SearchOpenedEvent
import utils.highlight

class MessagePane : View("MessagePane") {

    private val controller: MessagesController by inject()
    private val filteredMessages = SortedFilteredList(controller.messageList)

    override val root = vbox {
        listview(filteredMessages) {
            cellFormat {
                graphic = highlight(it, controller.searchTerm.valueSafe)
            }
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

