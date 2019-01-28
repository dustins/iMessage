package ui.views

import javafx.application.Platform
import javafx.geometry.HPos
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import tornadofx.*
import ui.controllers.MessagesController
import ui.controllers.SearchOpenedEvent
import utils.highlight
import java.awt.Color
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
            cellFormat {
                graphic = cache {
                    hbox {
                        vbox {
                            vbox {
                                label(getName(it.handleID)) {
                                    style {
                                        fontSize = 10.px
                                        textFill = if (it.isFromMe) c("white") else c("black")
                                    }
                                }
                                paddingBottom = 2.0
                            }
                            label(it.text) {
                                style {
                                    textFill = if (it.isFromMe) c("white") else c("black")
                                }
                            }
                            style {
                                backgroundRadius += box(15.px)
                                backgroundColor += if (it.isFromMe) c(24, 112, 246) else c(223, 223, 223)
                            }
                            paddingAll = 5.0
                        }
                        maxWidth = 300.0
                        alignment = if (it.isFromMe) {
                            Pos.CENTER_RIGHT
                        } else {
                            Pos.CENTER_LEFT
                        }
                    }

                }
            }
            vgrow = Priority.ALWAYS
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

fun getName(h: Int): String {
    return when (h) {
        0 -> "Me"
        2 -> "Dad"
        4 -> "Mom"
        19 -> "Lisa"
        228 -> "Sommer"
        else -> "Makayla"
    }
}

