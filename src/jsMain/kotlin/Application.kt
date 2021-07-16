import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.style
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import styled.css
import styled.styledDiv

@JsExport
class Application : RComponent<RProps,RState>() {

    private fun RBuilder.navbar() {

    }

    override fun RBuilder.render() {
        navbar()
        styledDiv {
            css {
                classes = mutableListOf("alert alert-warning text-center")
            }
            +"Тест bootstrap'а"
        }
    }
}