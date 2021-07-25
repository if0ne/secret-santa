import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import styled.StyleSheet

object ComponentStyles : StyleSheet("ComponentStyles") {

    val navbarLink by css {
        color = Color.white
        textDecoration = TextDecoration.none

        hover {
            color = Color("#322C40")
        }
    }

    val pageTitle by css {
        fontSize = (1.75).rem
        fontWeight = FontWeight.bold
    }

    val buttonSubmit by css {
        width = LinearDimension("50%")
        marginTop = (0.5).rem
    }

    val marginTop by css {
        marginTop = (0.5).rem
    }

    val buttonWidthFull by css {
        width = LinearDimension("100%")
    }
}