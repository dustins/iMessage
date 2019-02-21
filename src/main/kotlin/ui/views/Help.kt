package ui.views

import javafx.geometry.Pos
import javafx.scene.image.Image
import tornadofx.*

class Help : View("Help") {

    private val imageURLs = arrayOf(
        Image("help/help_1.png"),
        Image("help/help_2.png"),
        Image("help/help_3.png")
    )

    private val labels = arrayOf(
        "Find \"Security and Privacy\" in System Preferences",
        "Go to \"Full Disk Access\", unlock to make changes, and click the + button",
        "Navigate to iMessage in the pop-up Finder window"
    )

    override val root = vbox {
        pagination(imageURLs.size) {
            setPageFactory {
                vbox {
                    prefWidth = 800.0
                    prefHeight = 600.0
                    label(labels[it]) {
                        prefHeight = 30.0
                        alignment = Pos.CENTER
                        style {
                            fontSize = 16.px
                        }
                        paddingAll = 20.0
                    }
                    imageview(imageURLs[it]) {
                        fitWidth = 800.0
                        fitHeight = 530.0
                        isPreserveRatio = true
                        alignment = Pos.CENTER
                    }
                }
            }
        }
    }
}