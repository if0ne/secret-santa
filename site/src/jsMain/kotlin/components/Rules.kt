package components

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.css
import styled.styledP

class Rules: RComponent<RProps,RState>() {

    override fun RBuilder.render() {
        styledP {
            css {
                +ComponentStyles.pageTitle
            }

            +"Как играть?"
        }
        styledP {
            +"TODO: Здесь должны быть правила и описание, как пользоваться нашим приложением!"
        }
    }

}