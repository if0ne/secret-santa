package components

import components.basic.ButtonColor
import components.basic.ButtonType
import components.basic.santaButton
import components.basic.santaInput
import kotlinx.css.LinearDimension
import kotlinx.css.marginTop
import org.w3c.dom.events.Event

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.br
import react.router.dom.routeLink
import styled.css
import styled.styledDiv
import styled.styledP

external interface LoginProps: RProps {
    var logginCallback: (Event) -> Unit
}

class Login: RComponent<LoginProps, RState>() {

    override fun RBuilder.render() {

        styledP {
            css {
                +ComponentStyles.pageTitle
            }

            +"Вход"
        }
        styledDiv {
            css {
                classes = mutableListOf("col-6")
            }

            santaInput {
                label = "E-mail"
                type = components.basic.InputType.EMAIL
                id = "email"
            }

            santaInput {
                label = "Пароль"
                type = components.basic.InputType.PASSWORD
                id = "password"
            }

            santaButton {
                color = ButtonColor.ORANGE
                text = "Войти"
                disabled = false
                buttonType = ButtonType.SUBMIT

                onClick = props.logginCallback
            }
            br {}
            routeLink("/signup") {
                styledP {
                    css {
                        marginTop = LinearDimension("10px")
                    }
                    +"У меня нет аккаунта"
                }
            }
        }
    }
}