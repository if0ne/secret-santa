import kotlinx.css.Color
import kotlinx.css.color
import kotlinx.css.properties.TextDecoration
import kotlinx.css.textDecoration
import styled.StyleSheet

object ComponentStyles : StyleSheet("ComponentStyles") {

    val navbarLink by css {
        color = Color.white
        textDecoration = TextDecoration.none

        hover {
            color = Color("#322C40")
        }
    }

}