import kotlinx.css.Color
import kotlinx.css.color
import styled.StyleSheet

object ComponentStyles : StyleSheet("ComponentStyles") {

    val navbarLink by css {
        color = Color.white

        hover {
            color = Color("#322C40")
        }
    }

}