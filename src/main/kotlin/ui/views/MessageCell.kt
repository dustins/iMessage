//package ui.views
//
//import tornadofx.*
//import ui.models.Message
//import ui.models.MessageModel
//
//class MessageCellFragment : ListCellFragment<Message>() {
//    val message = MessageModel().bindTo(this)
//
//    override val root = vbox {
//        vbox {
//            label("${message.handleID}")
//            label("${message.date}")
//            paddingBottom = 4.0
//        }
//        label(message.text)
//    }
//}