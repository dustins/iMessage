package ui.models

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleStringProperty
import org.jetbrains.exposed.dao.IntIdTable
import tornadofx.*

object Messages : IntIdTable("message", "ROWID") {
    val text = text("text").nullable()
    val date = long("date")
    val isFromMe = bool("is_from_me")
    val handleID = integer("handle_id")
}

data class Message(val text: String, val date: Long, val isFromMe: Boolean, val handleID: Int)



//class Message(text: String, date: Long, isFromMe: Boolean, handleID: Int) {
//    val textProperty = SimpleStringProperty(this, "text", text)
//    var text by textProperty
//
//    val dateProperty = SimpleLongProperty(this, "date", date)
//    var date by dateProperty
//
//    val isFromMeProperty = SimpleBooleanProperty(this, "isFromMe", isFromMe)
//    var isFromMe by isFromMeProperty
//
//    val handleIDProperty = SimpleIntegerProperty(this, "handleID", handleID)
//    var handleID by handleIDProperty
//}
//
//class MessageModel : ItemViewModel<Message>() {
//    val text = bind(Message::textProperty)
//    val date = bind(Message::date)
//    val isFromMe = bind(Message::isFromMeProperty)
//    val handleID = bind(Message::handleID)
//}