package components

import components.basic.ButtonColor
import components.basic.ButtonType
import components.basic.santaButton
import components.basic.santaInput
import kotlinx.css.LinearDimension
import kotlinx.css.marginTop
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.br
import react.router.dom.routeLink
import styled.css
import styled.styledDiv
import styled.styledP

class Signup: RComponent<RProps,RState>() {

    override fun RBuilder.render() {
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
                }

                santaInput {
                    label = "Отчество"
                    type = components.basic.InputType.DEFAULT
                    id = "MiddleName"
                }

                santaInput {
                    label = "E-mail"
                    type = components.basic.InputType.EMAIL
                    id = "Email"
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
                }

                santaInput {
                    label = "Номер телефона"
                    type = components.basic.InputType.DEFAULT
                    id = "Phone"
                }

                santaInput {
                    label = "Пароль"
                    type = components.basic.InputType.PASSWORD
                    id = "Password"
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
    }
}