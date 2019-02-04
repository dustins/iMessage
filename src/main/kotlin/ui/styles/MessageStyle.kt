package ui.styles

import javafx.geometry.Pos
import javafx.scene.paint.Color
import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass
import tornadofx.px

class MessageStyle : Stylesheet() {
    companion object {
        val fromMe by cssclass()
        val message by cssclass()
        val notFromMe by cssclass()
    }

    init {
        message {
            backgroundRadius += box(5.px)
            borderRadius += box(5.px)
            padding = box(5.px)
            maxWidth = 300.px
            backgroundColor += Color.TRANSPARENT
        }
        fromMe {
            alignment = Pos.CENTER_RIGHT
            backgroundColor += Color.LIGHTGREEN
        }
        notFromMe {
            alignment = Pos.CENTER_LEFT
            backgroundColor += Color.LIGHTGRAY
        }
    }
}