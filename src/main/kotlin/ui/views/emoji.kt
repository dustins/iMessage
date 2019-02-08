package ui.views

import javafx.scene.text.Font
import javafx.scene.text.FontSmoothingType
import tornadofx.*
import java.awt.*

class Emoji : App(EmojiView::class)

class EmojiView : View("emoji") {

    val emoji = 0xF47D.toChar()

    override val root = vbox {
        label("Emoji in a label $emoji" ) {
            style {
                fontFamily = "Apple Color Emoji"
            }
        }
        textflow {
            text {
                text = "Should be alien smiley: $emoji"
                fontSmoothingType = FontSmoothingType.LCD
                font = Font.loadFont(javaClass.getResourceAsStream("/emoji.ttc"), 12.0)
            }
        }
        textflow {
            text {
                text = "$emoji"
                font = loadFont("/Apple Color Emoji.ttc", 12)
            }
        }
    }
}

class Test : Frame() {

    val emoji = "\uF602"

    init {
        val b = TextField("Emoji: \uF602")
        add(b)
        setSize(200, 200)
        isVisible = true
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("Emoji: \uF602")
            Test()
        }
    }
}

fun main(args: Array<String>) {
//    GraphicsEnvironment
//        .getLocalGraphicsEnvironment()
//        .availableFontFamilyNames
//        .forEach {
//            println(it)
//        }
    launch<Emoji>(args)
}