package ui.styles

import tornadofx.Stylesheet
import tornadofx.importStylesheet

class ScrollbarStyle : Stylesheet() {
    init {
        importStylesheet("/css/scrollbar.css")
    }
}