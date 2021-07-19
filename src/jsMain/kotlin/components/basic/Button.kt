package components.basic

import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.*
import react.dom.attrs
import styled.css
import styled.styledButton

enum class ButtonColor(val hex: String) {
    ORANGE("#D9765F"),
    DARK("#322C40"),
}

external interface ButtonProps: RProps {
    var text: String
    var color: ButtonColor
    var disabled: Boolean

    var onClick: (Event) -> Unit
}

class Button: RComponent<ButtonProps, RState>() {

    override fun RBuilder.render() {
        styledButton() {

            css {
                display = Display.inlineBlock
                backgroundColor = Color(props.color.hex)

                textAlign = TextAlign.center
                verticalAlign = VerticalAlign.middle

                border = "0"
                borderRadius = LinearDimension("25px")

                color = Color.white
                fontSize = LinearDimension("1rem")
                fontFamily = "'Roboto', sans-serif"

                padding((0.375).rem, (0.75).rem)
            }
            css.hover {
                backgroundColor = Color("#833C2C")
            }

            attrs {
                disabled = props.disabled
                onClickFunction = props.onClick
            }

            +props.text
            children()
        }
    }
}

fun RBuilder.santaButton(handler: ButtonProps.() -> Unit): ReactElement {
    return child(Button::class) {
        this.attrs(handler)
    }
}