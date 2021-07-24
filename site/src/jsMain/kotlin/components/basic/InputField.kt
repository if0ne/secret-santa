package components.basic

import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import kotlinx.css.properties.Transitions
import kotlinx.css.properties.transition
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.attrs
import react.dom.br
import react.dom.input
import styled.css

import styled.styledDiv
import styled.styledInput
import styled.styledLabel

enum class InputType(val type: kotlinx.html.InputType) {
    DEFAULT(kotlinx.html.InputType.text),
    EMAIL(kotlinx.html.InputType.email),
    PASSWORD(kotlinx.html.InputType.password),
}

external interface InputProps: RProps {
    var id: String
    var label: String?
    var error: String?
    var placeholder: String?

    var validation: ((String) -> Boolean)?

    var type: InputType
}

data class InputState(var isRight: Boolean,val value: String): RState

class InputField : RComponent<InputProps, InputState>() {

    init {
        state.isRight = true
    }

    override fun RBuilder.render() {
        styledDiv {
            if (props.label != null) {
                styledLabel {
                    css {
                        classes = mutableListOf("form-label")
                    }
                    attrs {
                        id = props.id
                    }
                    +props.label!!
                }
            }
            styledInput(type = props.type.type) {
                css {
                    classes = mutableListOf("form-control")
                }
                attrs {
                    id = props.id
                    placeholder = props.placeholder ?: ""
                    onChangeFunction = {
                        val value = (it.target as HTMLInputElement).value
                        val valid =  if (props.validation != null) props.validation!!(value) else state.isRight
                        setState(InputState(valid, value))
                    }
                }
            }
            if (!state.isRight) {
                styledLabel {
                    css {
                        classes = mutableListOf("form-text")
                        color = Color("#8C1F1F")
                    }
                    attrs {
                        id = props.id
                    }

                    +(props.error ?: "")
                }
            }
        }
    }

}

fun RBuilder.santaInput(handler: InputProps.() -> Unit): ReactElement {
    return child(InputField::class) {
        this.attrs(handler)
    }
}