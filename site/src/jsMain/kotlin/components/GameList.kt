package components

import components.basic.ButtonColor
import components.basic.ButtonType
import components.basic.santaButton
import components.basic.santaInput
import getMemberCount
import kotlinx.coroutines.launch
import kotlinx.css.*
import react.*
import react.router.dom.routeLink
import shared_models.model.Session
import shared_models.model.User
import styled.css
import styled.styledDiv
import styled.styledP
import styled.styledSpan

external interface GameListProps: RProps {
    var user: User
    var gameList: List<Session>
}

data class GameListState(
    var gameId: String,
): RState

class GameList: RComponent<GameListProps, GameListState>() {

    private fun RBuilder.gameLi(session: Session): ReactElement {
        return styledDiv {
            css {
                classes = mutableListOf("card", "w-75", "mx-auto")
                backgroundColor = Color("#F2D8C9")
                marginTop = (0.5).rem
            }
            styledDiv {
                css {
                    classes = mutableListOf("card-body")
                }
                styledP {
                    css {
                        fontWeight = FontWeight.bold
                        fontSize = (1.25).rem
                    }
                    +"Игра ${session.id}"
                }
                styledP {
                    css {
                        margin = "0"
                    }

                    var userCount = 0
                    mainScope.launch {
                        userCount = getMemberCount(session)
                    }
                    +"Количество участников: $userCount"
                }
                styledP {
                    css {
                        margin = "0"
                    }
                    +"Дата мероприятия: ${session.eventTimestamp}"
                }
                styledP {
                    +"Средняя цена подарка: "
                    styledSpan {
                        css {
                            fontWeight = FontWeight.bold
                        }
                        +"${session.budget}₽"
                    }
                }
                styledDiv {
                    css {
                        float = Float.right
                    }
                    routeLink("/game/${session.id}") {
                        santaButton {
                            text = "Подробнее"

                            color = ButtonColor.ORANGE
                            disabled = false
                            buttonType = ButtonType.DEFAULT
                        }
                    }
                }
            }
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                classes = mutableListOf("row")
            }
            styledDiv {
                css {
                    classes = mutableListOf("col-6")
                }
                santaInput {
                    type = components.basic.InputType.DEFAULT
                    id = "search"
                    placeholder = "Введите код игры"

                    onChange = { value, _ ->
                        setState(GameListState(value))
                    }
                }
            }

            styledDiv {
                css {
                    classes = mutableListOf("col-2")
                    padding = "0"
                }
                santaButton {
                    text = "Вступить"
                    disabled = false
                    color = ButtonColor.ORANGE
                    buttonType = ButtonType.FULL_WIDTH

                    onClick = {
                        //TODO: ПОПЫТКА ВХОДА В ИГРУ С ИДЕНТИФИКАТОРОМ gameId
                    }
                }
            }

            styledDiv {
                css {
                    classes = mutableListOf("col-2")
                    padding = "0"
                    marginLeft = 5.px
                }
                routeLink("/create_game") {
                    santaButton {
                        text = "Создать"
                        disabled = false
                        color = ButtonColor.DARK
                        buttonType = ButtonType.FULL_WIDTH
                    }
                }
            }
        }

        props.gameList.forEach {
            gameLi(it)
        }
    }
}