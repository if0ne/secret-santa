package components

import components.basic.ButtonColor
import components.basic.ButtonType
import components.basic.santaButton
import components.basic.santaInput
import kotlinx.coroutines.launch
import kotlinx.css.*
import org.w3c.dom.events.Event
import react.*
import react.dom.attrs
import react.router.dom.routeLink
import shared_models.model.User
import shared_models.request.ConnectGuidRequest
import styled.css
import styled.styledDiv
import styled.styledImg
import styled.styledP
import telegramConnect

fun RBuilder.buildImage(href: String): ReactElement {
    return styledDiv {
        css {
            classes = mutableListOf("col-4")
            height = 200.px
            width = 200.px
            display = Display.inlineBlock
            position = Position.relative
            overflow = Overflow.hidden
            borderRadius = LinearDimension("50%")
        }
        styledImg {
            css {
                height = LinearDimension("100%")
                width = LinearDimension.auto
                marginLeft = LinearDimension("-50px")
            }

            attrs {
                src = href
            }
        }
    }
}

external interface ProfileProps: RProps {
    var user: User

    var logoutCallback: (Event) -> Unit
}

data class ProfileState(var newUser: User, var isEditTelegram: Boolean, var telegramCode: String): RState

class Profile(props: ProfileProps): RComponent<ProfileProps, ProfileState>(props) {

    init {
        setState(ProfileState(props.user, false, ""))
    }

    override fun RBuilder.render() {
        styledP {
            css {
                +ComponentStyles.pageTitle
            }

            +"Профиль"
        }

        styledDiv {
            css {
                classes = mutableListOf("row")
            }
            buildImage(props.user.avatarUrl ?: "https://www.pinclipart.com/picdir/big/564-5646085_santa-claus-logo-png-clipart.png")
            styledDiv {
                css {
                    classes = mutableListOf("col-8")
                }

                styledP {
                    css {
                        fontWeight = FontWeight.bold
                        fontSize = (1.25).rem
                        margin = "0"
                    }
                    +"${props.user.lastName} ${props.user.firstName} ${props.user.middleName ?: ""}"
                }

                styledP {
                    css {
                        margin = "0"
                    }
                    +props.user.email
                }

                styledDiv {
                    css {
                        classes = mutableListOf("row")
                        marginTop = 50.px
                    }

                    styledDiv {
                        css {
                            classes = mutableListOf("col")
                        }

                        santaButton {
                            color = ButtonColor.ORANGE
                            text = "Редактировать"
                            disabled = true
                            buttonType = ButtonType.WIDTH_WITH_MARGIN
                        }

                        santaButton {
                            color = if (props.user.telegramId != null) ButtonColor.GREEN else ButtonColor.DARK
                            text = if (props.user.telegramId != null) "Telegram подключен" else "Telegram"
                            disabled = props.user.telegramId != null
                            buttonType = ButtonType.WIDTH_WITH_MARGIN

                            onClick = {
                                setState(ProfileState(state.newUser, !state.isEditTelegram, ""))
                            }
                        }
                    }

                    styledDiv {
                        css {
                            classes = mutableListOf("col")
                        }

                        routeLink("/games") {
                            santaButton {
                                color = ButtonColor.ORANGE
                                text = "Мои игры"
                                disabled = false
                                buttonType = ButtonType.WIDTH_WITH_MARGIN
                            }
                        }
                        santaButton {
                            color = ButtonColor.ORANGE
                            text = "Выйти"
                            disabled = false
                            buttonType = ButtonType.WIDTH_WITH_MARGIN

                            onClick = props.logoutCallback
                        }
                    }
                }
            }
        }

        if (state.isEditTelegram) {
            styledDiv {
                css {
                    classes = mutableListOf("row")
                }

                styledDiv {
                    css {
                        classes = mutableListOf("col-4")
                    }

                    styledP {
                        css {
                            fontWeight = FontWeight.bold
                            fontSize = (1.25).rem
                            margin = "0"
                        }
                        +"Введите код"
                    }
                    santaInput {
                        type = components.basic.InputType.DEFAULT
                        id = "code"

                        validation = null

                        onChange = { value, _ ->
                            setState(ProfileState(state.newUser, state.isEditTelegram, value))
                        }
                    }
                    santaButton {
                        color = ButtonColor.ORANGE
                        text = "Связать"
                        disabled = false
                        buttonType = ButtonType.WIDTH_WITH_MARGIN

                        onClick = {
                            mainScope.launch {
                                val response = telegramConnect(ConnectGuidRequest(
                                    props.user.id,
                                    state.telegramCode
                                ))

                                //TODO: Сделать коллбек к кеш юзеру
                            }
                        }
                    }
                }
            }
        }
    }
}