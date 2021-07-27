package components.basic

import kotlinx.css.Color
import kotlinx.css.color
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.attrs
import styled.*

external interface TextAreaProps: RProps {
    var id: String
    var label: String?
    var error: String?

    var validation: ((String) -> Boolean)?
}

data class TextAreaState(var isRight: Boolean, val value: String): RState

class TextArea : RComponent<TextAreaProps,TextAreaState>() {

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
            styledTextarea {
                css {
                    classes = mutableListOf("form-control")
                }

                attrs {
                    id = props.id
                    onChangeFunction = {
                        val value = (it.target as HTMLInputElement).value
                        val valid =  if (props.validation != null) props.validation!!(value) else state.isRight
                        setState(TextAreaState(valid, value))
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

fun RBuilder.santaTextArea(handler: TextAreaProps.() -> Unit): ReactElement {
    return child(TextArea::class) {
        this.attrs(handler)
    }
}