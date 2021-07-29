package components.basic

import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.onChange
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import react.*
import react.dom.attrs
import styled.*

external interface TextAreaProps: RProps {
    var id: String
    var label: String?

    var onChange: ((String) -> Unit)?
}

data class TextAreaState(val value: String): RState

class TextArea : RComponent<TextAreaProps,TextAreaState>() {

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
                        val value = (it.target as HTMLTextAreaElement).value
                        props.onChange?.let { it(value) }
                        setState(TextAreaState(value))
                    }
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