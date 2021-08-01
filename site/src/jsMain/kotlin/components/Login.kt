package components

import components.basic.ButtonColor
import components.basic.ButtonType
import components.basic.santaButton
import components.basic.santaInput
import kotlinx.css.Color
import kotlinx.css.LinearDimension
import kotlinx.css.color
import kotlinx.css.marginTop

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.br
import react.router.dom.routeLink
import shared_models.model.User
import styled.css
import styled.styledDiv
import styled.styledP

external interface LoginProps: RProps {
    var logginCallback: (String, String, () -> Unit) -> Unit
    var user: User?
}

data class LoginState(
    var login: String,
    var password: String,
    var isShowedMessage: Boolean,
): RState

class Login: RComponent<LoginProps, LoginState>() {

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

                validation = null

                onChange = { value, _ ->
                    setState(LoginState(value, state.password, state.isShowedMessage))
                }
            }

            santaInput {
                label = "Пароль"
                type = components.basic.InputType.PASSWORD
                id = "password"

                validation = null

                onChange = { value, _ ->
                    setState(LoginState(state.login, value, state.isShowedMessage))
                }
            }

            santaButton {
                color = ButtonColor.ORANGE
                text = "Войти"
                disabled = false
                buttonType = ButtonType.SUBMIT

                onClick = {
                    props.logginCallback(state.login,state.password) {
                        setState(LoginState(
                            state.login,
                            state.password,
                            props.user == null
                        ))
                    }
                }
            }

            br{}

            if (state.isShowedMessage) {
                styledP {
                    css {
                        classes = mutableListOf("form-text")
                        color = Color("#8C1F1F")
                    }
                    +"Неверный логин или пароль"
                }
            }

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