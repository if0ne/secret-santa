package components

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
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