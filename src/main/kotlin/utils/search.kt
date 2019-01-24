package utils

import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.scene.text.TextFlow

fun highlight(
    text: String,
    searchTerm: String
): TextFlow {

    if (searchTerm.isEmpty()) {
        return TextFlow(Text(text))
    }

    // Normally we lose the delimiter we split on, but we want to keep it
    val sanitizedSearch = Regex.escape(searchTerm)
    val keepSearchTerm = "((?<=$sanitizedSearch)|(?=$sanitizedSearch))"
        .toRegex(RegexOption.IGNORE_CASE)

    val richText = text
        .split(keepSearchTerm)
        .map {
            if (it.equals(searchTerm, true))
                createHighlight(it)
            else
                Text(it)
        }

    return TextFlow(*richText.toTypedArray())
}

private fun createHighlight(s: String): Text {
    val text = Text(s)
    text.fill = Color.ORANGE
    return text
}
