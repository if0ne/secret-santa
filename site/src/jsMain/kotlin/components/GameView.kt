package components

import components.basic.*
import kotlinx.css.*
import react.*
import react.dom.attrs
import react.dom.p
import shared_models.model.Gift
import shared_models.model.Session
import shared_models.model.SessionState
import shared_models.model.User
import styled.*

external interface GameViewProps: RProps {
    var user: User
    var session: Session
    var gifts: List<Gift>
}

data class GameViewState(
    var isNewGift: Boolean,
    var gifts: MutableList<Gift>,

    var giftName: Pair<String, Boolean>,
    var giftDesc: String
): RState

class GameView: RComponent<GameViewProps, GameViewState>() {

    override fun GameViewState.init() {
        setState {
            gifts = props.gifts as MutableList<Gift> //TODO: Может сломаться
        }
    }

    private fun RBuilder.memberCard(user: User): ReactElement {
        return styledDiv {
            css {
                classes = mutableListOf("col")
            }

            styledDiv {
                css {
                    classes = mutableListOf("card")
                    border = "none"
                }
                styledImg {
                    css {
                        classes = mutableListOf("card-img-top")
                        borderRadius = LinearDimension("50%")
                    }

                    attrs {
                        src = user.avatarUrl ?: ""
                    }
                }
                styledDiv {
                    css {
                        classes = mutableListOf("card-body")
                    }
                    styledP {
                        css {
                            textAlign = TextAlign.center
                            fontWeight = FontWeight.bold
                            margin = "0"
                        }
                        +"${props.user.lastName} ${props.user.firstName}"
                    }
                    if (props.user.id == props.session.hostId &&
                        props.session.currentState == SessionState.LOBBY) {
                        santaButton {
                            text = "Удалить"
                            color = ButtonColor.RED
                            buttonType = ButtonType.FULL_WIDTH

                            onClick = {
                                //TODO: POST-запрос с удалением участника
                            }
                        }
                    }
                }
            }
        }
    }

    private fun RBuilder.giftCard(gift: Gift): ReactElement {
        return styledDiv {
            css {
                classes = mutableListOf("col")
            }
            styledDiv {
                css {
                    classes = mutableListOf("card")
                    backgroundColor = Color("#F2D8C9")
                }
                styledDiv {
                    css {
                        classes = mutableListOf("card-body")
                    }
                    styledH5 {
                        css {
                            classes = mutableListOf("card-title")
                            fontWeight = FontWeight.bold
                        }
                        +gift.name
                    }
                    p(classes = "card-text") {
                        +(gift.description ?: "")
                    }
                }
            }
        }
    }

    private fun RBuilder.gameInfo(): ReactElement {
        return styledDiv {
            css {
                classes = mutableListOf("col")
            }

            styledP {
                css {
                    fontWeight = FontWeight.bold
                    fontSize = (1.25).rem
                    margin = "0"
                }
                +"Игра №${props.session.id}"
            }

            styledP {
                css {
                    margin = "0"
                }
                +"Дата мероприятия: ${props.session.eventTimestamp}"
            }

            styledP {
                +"Средняя цена подарка: "
                styledSpan {
                    css {
                        fontWeight = FontWeight.bold
                    }
                    +"${props.session.budget}₽"
                }
            }
        }
    }

    private fun RBuilder.unstartedGame(): ReactElement {
        return styledDiv {
            //ИНФА
            styledDiv {
                css {
                    classes = mutableListOf("row")
                }

                gameInfo()

                styledDiv {
                    css {
                        classes = mutableListOf("col")
                    }
                    styledP {
                        css {
                            fontWeight = FontWeight.bold
                            fontSize = (1.25).rem
                            textAlign = TextAlign.center
                        }
                        +"Код для входа"
                    }

                    styledP {
                        css {
                            textAlign = TextAlign.center
                            fontSize = (1.25).rem
                        }
                        +(props.session.guid ?: "")
                    }
                }
            }

            //ВАШИ ПОЖЕЛАНИЯ
            styledP {
                css {
                    fontWeight = FontWeight.bold
                    fontSize = (1.25).rem
                    textAlign = TextAlign.center
                    margin = ""
                }
                +"Ваши пожелания:"
            }

            styledDiv {
                css {
                    classes = mutableListOf("row","row-cols-1","row-cols-md-3","g-4")
                }

                for (item in state.gifts) {
                    giftCard(item)
                }
            }

            //ДОБАВЛЕНИЕ
            styledDiv {
                css {
                    classes = mutableListOf("col-6")
                }
                if (state.isNewGift) {
                    santaInput {
                        label = "Название"
                        id = "gift_name"
                        type = InputType.DEFAULT

                        error = "Заполните это поле"

                        validation = {
                            it.isNotEmpty()
                        }

                        onChange = { value, valid ->
                            setState(GameViewState(
                                state.isNewGift,
                                state.gifts,
                                Pair(value, valid),
                                state.giftDesc
                            ))
                        }
                    }

                    santaTextArea {
                        label = "Описание"
                        id = "gift_desc"

                        onChange = { value ->
                            setState(GameViewState(
                                state.isNewGift,
                                state.gifts,
                                state.giftName,
                                value
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
                        classes = mutableListOf("col-3")
                    }
                    santaButton {
                        text = if (!state.isNewGift) "Добавить" else "Отмена"
                        color = ButtonColor.DARK
                        disabled = false
                        buttonType = ButtonType.WIDTH_WITH_MARGIN

                        onClick = {
                            setState(GameViewState(!state.isNewGift, state.gifts, state.giftName, state.giftDesc))
                        }
                    }
                }

                styledDiv {
                    css {
                        classes = mutableListOf("col-3")
                    }

                    if (state.isNewGift) {
                        santaButton {
                            text = "Добавить"
                            color = ButtonColor.ORANGE
                            disabled = false
                            buttonType = ButtonType.WIDTH_WITH_MARGIN

                            onClick = {
                                if (state.giftName.second) {
                                    val newGifts = state.gifts
                                    //TODO: ЗАПРОС НА ДОБАВЛЕНИЯ ПОДАРКА, ПОЛУЧЕНИЕ ЕГО ID
                                    newGifts.add(Gift(0, "", state.giftName.first, state.giftDesc))
                                    setState(GameViewState(!state.isNewGift,newGifts, state.giftName, state.giftDesc))
                                }
                            }
                        }
                    }
                }
            }

            //УЧАСТНИКИ
            styledP {
                css {
                    fontWeight = FontWeight.bold
                    fontSize = (1.25).rem
                    textAlign = TextAlign.center
                }
                +"Участники:"
            }
            styledDiv {
                css {
                    classes = mutableListOf("row","row-cols-1","row-cols-md-4","g-4")
                }

                //TODO: ВЫВОД УЧАСТНИКОВ

            }
        }
    }

    //TODO: ВЫВОД  ИНФОРАЦИИ КОМУ ТЫ ДАРИШЬ ПОДАРОК
    private fun RBuilder.startedGame(): ReactElement {
        return styledDiv {
            styledDiv {
                css {
                    classes = mutableListOf("row")
                }

                gameInfo()
            }

            styledP {
                css {
                    fontWeight = FontWeight.bold
                    fontSize = (1.25).rem
                    textAlign = TextAlign.center
                    margin = ""
                }
                +"Вы делаете подарок для:"
            }

            styledDiv {
                css {
                    classes = mutableListOf("mx-auto")
                    width = 18.rem
                }
                //TODO: ВЫВОД КОМУ ДАРИТЬ
                //memberCard("Асташкин Максим")
            }

            styledDiv {
                css {
                    classes = mutableListOf("row","row-cols-1","row-cols-md-3","g-4")
                }

                //TODO: ПОЛУЧЕНИЕ ПОДАРКОВ, КОМУ ДАРИТЬ
                for (item in state.gifts) {
                    giftCard(item)
                }
            }
        }
    }

    override fun RBuilder.render() {
        if (props.session.currentState == SessionState.LOBBY) {
            unstartedGame()
        } else {
            startedGame()
        }
    }
}