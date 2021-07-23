package components

import components.basic.ButtonColor
import components.basic.ButtonType
import components.basic.santaButton
import components.basic.santaInput
import kotlinx.css.fontSize
import kotlinx.css.rem
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.router.dom.routeLink
import styled.css
import styled.styledDiv
import styled.styledP

class Welcome: RComponent<RProps,RState>() {

    override fun RBuilder.render() {
        styledP {
            css {
                +ComponentStyles.pageTitle
            }

            +"Хо-хо-хо!"
        }
        styledDiv {
            css {
                classes = mutableListOf("col-3")

            }

            styledP {
                css {
                    fontSize = (1.5).rem
                }
                +"Добро\nпожаловать\nв дом Санты!"
            }
        }

        styledDiv {
            css {
                classes = mutableListOf("col-6")
            }
            routeLink("/signup") {
                santaButton {
                    color = ButtonColor.ORANGE
                    text = "Начать игру"
                    disabled = false
                    buttonType = ButtonType.SUBMIT
                }
            }

        }
    }

}