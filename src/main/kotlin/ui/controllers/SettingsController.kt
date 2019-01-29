package ui.controllers

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class SettingsController : Controller() {
    val messageDB = SimpleStringProperty("/Users/Kevin/Desktop/chat.db")
    val addressDB = SimpleStringProperty("/Users/Kevin/Desktop/address.db")
}