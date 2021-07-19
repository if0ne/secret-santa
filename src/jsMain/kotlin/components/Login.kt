package components

import components.basic.ButtonColor
import components.basic.santaButton

import kotlinx.html.InputType
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*

class Login: RComponent<RProps,RState>() {

    override fun RBuilder.render() {
        p {
            +"Вход"
        }

        form("auth-form") {
            p {
                +"E-mail"
            }
            input(name = "login", type = InputType.email) {}
            br {}
            p {
                +"Password"
            }
            input(name = "password", type = InputType.password) {}
            br {}
        }

        santaButton {
            color = ButtonColor.ORANGE
            text = "Войти"
            disabled = false
        }
    }
}