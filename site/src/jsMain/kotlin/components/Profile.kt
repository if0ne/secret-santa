package components

import components.basic.ButtonColor
import components.basic.ButtonType
import components.basic.santaButton
import components.basic.santaInput
import kotlinx.css.*
import org.w3c.dom.events.Event
import react.*
import react.dom.attrs
import react.router.dom.routeLink
import styled.css
import styled.styledDiv
import styled.styledImg
import styled.styledP

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
    var user: String

    var logoutCallback: (Event) -> Unit
}

data class ProfileState(var isEditTelegram: Boolean): RState

class Profile: RComponent<ProfileProps, ProfileState>() {

    init {
        state.isEditTelegram = false
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
            buildImage(/*props.user.avatarUrl ?: */"https://sun9-76.userapi.com/impg/TGQS7CIdiA7adC4PdmwiYXYbfaDXkhlRi11DRA/PmcveX_CFfY.jpg?size=1080x698&quality=96&sign=1421d6e98144995f9a6ea914a4cb2d79&type=album")
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
                    +"Асташкин Максим Сергеевич"
                    //+"${props.user.lastName} ${props.user.firstName} ${props.user.middleName ?: ""}"
                }

                styledP {
                    css {
                        margin = "0"
                    }
                    +"maksim.astash@gmail.com"
                    //+props.user.email
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
                            color = ButtonColor.DARK
                            text = "Telegram"
                            disabled = false
                            buttonType = ButtonType.WIDTH_WITH_MARGIN

                            onClick = {
                                setState(ProfileState(!state.isEditTelegram))
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
                    }
                    santaButton {
                        color = ButtonColor.ORANGE
                        text = "Связать"
                        disabled = false
                        buttonType = ButtonType.WIDTH_WITH_MARGIN
                    }
                }
            }
        }
    }
}