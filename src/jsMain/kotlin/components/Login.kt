package components

import ComponentStyles.buttonSubmit
import components.basic.ButtonColor
import components.basic.santaButton
import components.basic.santaInput
import kotlinx.css.CSSBuilder
import kotlinx.css.LinearDimension
import kotlinx.css.br
import kotlinx.css.marginTop

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.br
import react.dom.p
import react.router.dom.routeLink
import styled.css
import styled.styledDiv
import styled.styledP

class Login: RComponent<RProps,RState>() {

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
                validation = {
                    false
                }
                error = "Неверный формат почты"
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
                isSubmit = true
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