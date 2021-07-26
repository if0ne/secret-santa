package components

import components.basic.ButtonColor
import components.basic.ButtonType
import components.basic.santaButton
import components.basic.santaInput
import kotlinx.css.*
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.br
import react.router.dom.routeLink
import styled.css
import styled.styledDiv
import styled.styledP

data class SignupState(
    var firstName: Pair<String, Boolean>,
    var lastName: Pair<String, Boolean>,
    var middleName: String,
    var email: Pair<String, Boolean>,
    var password: Pair<String, Boolean>,
    var phone: Pair<String, Boolean>,
    var signed: Boolean,
): RState

class Signup: RComponent<RProps, SignupState>() {

    init {
        state.signed = false
    }

    override fun RBuilder.render() {
        if (!state.signed) {
            styledP {
                css {
                    +ComponentStyles.pageTitle
                }

                +"Регистрация"
            }
            styledDiv {
                css {
                    classes = mutableListOf("row")
                }

                styledDiv {
                    css {
                        classes = mutableListOf("col-6")
                    }

                    santaInput {
                        label = "Имя"
                        type = components.basic.InputType.DEFAULT
                        id = "FirstName"

                        error = "Заполните поле"

                        validation = {
                            it.isNotEmpty()
                        }

                        onChange = { value, valid ->
                            setState(SignupState(
                                Pair(value, valid),
                                state.lastName,
                                state.middleName,
                                state.email,
                                state.password,
                                state.phone,
                                state.signed
                            ))
                        }
                    }

                    santaInput {
                        label = "Отчество"
                        type = components.basic.InputType.DEFAULT
                        id = "MiddleName"

                        validation = null

                        onChange = { value, _ ->
                            setState(SignupState(
                                state.firstName,
                                state.lastName,
                                value,
                                state.email,
                                state.password,
                                state.phone,
                                state.signed
                            ))
                        }
                    }

                    santaInput {
                        label = "E-mail"
                        type = components.basic.InputType.EMAIL
                        id = "Email"

                        error = "Неверный формат почты"

                        validation = {
                            val regexPattern: String = "^(.+)@(.+)\$"
                            val regex = Regex(regexPattern)
                            regex.containsMatchIn(it)
                        }

                        onChange = { value, valid ->
                            setState(SignupState(
                                state.firstName,
                                state.lastName,
                                state.middleName,
                                Pair(value, valid),
                                state.password,
                                state.phone,
                                state.signed
                            ))
                        }
                    }
                }

                styledDiv {
                    css {
                        classes = mutableListOf("col-6")
                    }

                    santaInput {
                        label = "Фамилия"
                        type = components.basic.InputType.DEFAULT
                        id = "LastName"

                        error = "Заполните поле"

                        validation = {
                            it.isNotEmpty()
                        }

                        onChange = { value, valid ->
                            setState(SignupState(
                                state.firstName,
                                Pair(value, valid),
                                state.middleName,
                                state.email,
                                state.password,
                                state.phone,
                                state.signed
                            ))
                        }
                    }

                    santaInput {
                        label = "Номер телефона"
                        type = components.basic.InputType.DEFAULT
                        id = "Phone"

                        error = "Неверный формат телефона"

                        validation = {
                            val regexPattern: String = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}\$"
                            val regex = Regex(regexPattern)
                            regex.containsMatchIn(it)
                        }

                        onChange = { value, valid ->
                            setState(SignupState(
                                state.firstName,
                                state.lastName,
                                state.middleName,
                                state.email,
                                state.password,
                                Pair(value, valid),
                                state.signed
                            ))
                        }
                    }

                    santaInput {
                        label = "Пароль"
                        type = components.basic.InputType.PASSWORD
                        id = "Password"

                        error = "Пароль слабый"

                        validation = {
                            val regexPattern: String = "(?=.*[0-9])(?=.*[!@#\$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#\$%^&*]{6,}"
                            val regex = Regex(regexPattern)
                            regex.containsMatchIn(it)
                        }

                        onChange = { value, valid ->
                            setState(SignupState(
                                state.firstName,
                                state.lastName,
                                state.middleName,
                                state.email,
                                Pair(value, valid),
                                state.phone,
                                state.signed
                            ))
                        }
                    }
                }
            }

            styledDiv {
                css {
                    classes = mutableListOf("row")
                }
                styledDiv {
                    css {
                        classes = mutableListOf("col-6")
                    }

                    santaButton {
                        color = ButtonColor.ORANGE
                        text = "Зарегистрироваться"
                        disabled = false
                        buttonType = ButtonType.SUBMIT

                        onClick = {
                            if (state.firstName.second &&
                                state.lastName.second &&
                                state.email.second &&
                                state.phone.second &&
                                state.password.second) {
                                //TODO: ОТПРАВИТЬ ЗАПРОС НА РЕГИСТРАЦИЮ
                                //TODO: УЧИТВЫВАТЬ, ЧТО ПОЛЬЗОВАТЕЛЬ СОЗДАН
                                setState(SignupState(
                                    state.firstName,
                                    state.lastName,
                                    state.middleName,
                                    state.email,
                                    state.password,
                                    state.phone,
                                    true
                                ))
                            }
                        }
                    }

                    br {}
                    routeLink("/login") {
                        styledP {
                            css {
                                marginTop = LinearDimension("10px")
                            }
                            +"У меня уже есть аккаунт"
                        }
                    }
                }
            }
        } else {
            styledP {
                css {
                    +ComponentStyles.pageTitle
                    marginBottom = 0.px
                }

                +"Вы зарегистрированы"
            }

            routeLink("/login") {
                styledP {
                    css {
                        margin = "0"
                        fontSize = (1.25).rem
                    }
                    +"Войти"
                }
            }
        }
    }
}