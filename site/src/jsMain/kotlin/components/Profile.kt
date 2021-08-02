package components

import components.basic.ButtonColor
import components.basic.ButtonType
import components.basic.santaButton
import components.basic.santaInput
import connectAvatar
import getUserById
import kotlinext.js.asJsObject
import kotlinx.coroutines.launch
import kotlinx.css.*
import org.w3c.dom.events.Event
import react.*
import react.dom.attrs
import react.router.dom.routeLink
import shared_models.model.User
import shared_models.request.ConnectGuidRequest
import shared_models.request.SetAvatarUrlRequest
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
    var changeProfileCallback: (User) -> Unit
}

data class ProfileState(
    var newUser: User,
    var isEditTelegram: Boolean,
    var telegramCode: String,
    var isEditProfile: Boolean,
    var avatarHref: String,
): RState

class Profile(props: ProfileProps): RComponent<ProfileProps, ProfileState>(props) {

    init {
        state.newUser = props.user
        setState(ProfileState(props.user, false, "", false, ""))
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
            buildImage(state.newUser.avatarUrl ?: "https://www.pinclipart.com/picdir/big/564-5646085_santa-claus-logo-png-clipart.png")
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
                    +"${state.newUser.lastName} ${state.newUser.firstName} ${state.newUser.middleName ?: ""}"
                }

                styledP {
                    css {
                        margin = "0"
                    }
                    +state.newUser.email
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
                            disabled = state.isEditTelegram
                            buttonType = ButtonType.WIDTH_WITH_MARGIN

                            onClick = {
                                setState(ProfileState(state.newUser, state.isEditTelegram, state.telegramCode, !state.isEditProfile, ""))
                            }
                        }

                        santaButton {
                            color = if (props.user.telegramId != null) ButtonColor.GREEN else ButtonColor.DARK
                            text = if (props.user.telegramId != null) "Telegram подключен" else "Telegram"
                            disabled = props.user.telegramId != null || state.isEditProfile
                            buttonType = ButtonType.WIDTH_WITH_MARGIN

                            onClick = {
                                setState(ProfileState(state.newUser, !state.isEditTelegram, "", state.isEditProfile, state.avatarHref))
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
                            setState(ProfileState(state.newUser, state.isEditTelegram, value, state.isEditProfile, state.avatarHref))
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

                                if (response) {
                                    val newUser = getUserById(props.user.id)
                                    setState(ProfileState(newUser!!, false, "", false, ""))
                                    props.changeProfileCallback(newUser)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (state.isEditProfile) {
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
                        +"Введите ссылку на картинку"
                    }
                    santaInput {
                        type = components.basic.InputType.DEFAULT
                        id = "avatar"

                        validation = null

                        onChange = { value, _ ->
                            setState(ProfileState(state.newUser, state.isEditTelegram, state.telegramCode, state.isEditProfile, value))
                        }
                    }
                    santaButton {
                        color = ButtonColor.ORANGE
                        text = "Сохранить"
                        disabled = false
                        buttonType = ButtonType.WIDTH_WITH_MARGIN

                        onClick = {
                            mainScope.launch {
                                val response = connectAvatar(SetAvatarUrlRequest(
                                    props.user.id,
                                    state.avatarHref
                                ))

                                if (response) {
                                    val newUser = getUserById(props.user.id)
                                    setState(ProfileState(newUser!!, false, "", false, ""))
                                    props.changeProfileCallback(newUser)
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}