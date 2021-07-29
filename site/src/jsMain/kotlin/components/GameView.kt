package components

import components.basic.*
import kotlinx.css.*
import react.*
import react.dom.attrs
import react.dom.p
import styled.*

external interface GameViewProps: RProps {
    //var user: User
    //var session: Session
    //var gifts: List<Gifts>
    var isStarted: Boolean
    var gameId: String
    var isHost: Boolean
}

data class GameViewState(
    var isNewGift: Boolean,
    var gifts: MutableList<Pair<String, String>>,

    var giftName: Pair<String, Boolean>,
    var giftDesc: String
): RState

class GameView: RComponent<GameViewProps, GameViewState>() {

    init {
        state.gifts = mutableListOf()
        state.gifts.add(Pair("PSP", "Да да, то самое PSP"))
        state.gifts.add(Pair("NETFLIX", "Подписка на нетфликс ФУЛЛ ХД на 3 месяца"))
        state.gifts.add(Pair("Кровь, пот и пиксели", "Та самая книжка про кранчи в геймдеве"))
    }

    //TODO: СДЕЛАТЬ ВЫВОД ИНФОРМАЦИИ ОБ ИГРОКЕ
    private fun RBuilder.memberCard(fi: String /*user: User*/): ReactElement {
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
                        src = "https://sun9-58.userapi.com/impg/0V3I5v6gOeymr5JhhGqNil_8xcXsHjFIHoCuzw/XvkGuQh2MBo.jpg?size=737x799&quality=96&sign=3551602923167e36a67ecb15607969c3&type=album"
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
                        +fi
                    }
                    if (props.isHost && !props.isStarted) {
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

    //TODO: СДЕЛАТЬ ВЫВОДИ ИНФОРМАЦИИ О ПОДАРКЕ
    private fun RBuilder.giftCard(name: String, desc: String /* gift: Gift*/): ReactElement {
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
                        +name
                    }
                    p(classes = "card-text") {
                        +desc
                    }
                }
            }
        }
    }

    //TODO: ВЫВОД ИНФОРМАЦИИ ОБ ИГРЕ
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
                +"Игра №${props.gameId}"
            }

            styledP {
                css {
                    margin = "0"
                }
                +"Дата мероприятия: 25.12.2021"
            }

            styledP {
                +"Средняя цена подарка: "
                styledSpan {
                    css {
                        fontWeight = FontWeight.bold
                    }
                    +"1000₽"
                }
            }
        }
    }

    //TODO: ВЫВОД ИНФОРМАЦИИ ОБ ИГРЕ И ТВОИХ ПОЖЕЛАНИЙ
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
                        +"1175 623 2215"
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
                    giftCard(item.first,item.second)
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
                                    newGifts.add(Pair(state.giftName.first, state.giftDesc))
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

                //TODO: ВЫВОДИ УЧАСТНИКОВ
                memberCard("Асташкин Максим")
                memberCard("Асташкин Максим")
                memberCard("Асташкин Максим")
                memberCard("Асташкин Максим")

                memberCard("Асташкин Максим")
                memberCard("Асташкин Максим")
                memberCard("Асташкин Максим")
                memberCard("Асташкин Максим")
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
                memberCard("Асташкин Максим")
            }

            styledDiv {
                css {
                    classes = mutableListOf("row","row-cols-1","row-cols-md-3","g-4")
                }

                for (item in state.gifts) {
                    giftCard(item.first,item.second)
                }
            }
        }
    }

    override fun RBuilder.render() {
        if (!props.isStarted) {
            unstartedGame()
        } else {
            startedGame()
        }
    }
}