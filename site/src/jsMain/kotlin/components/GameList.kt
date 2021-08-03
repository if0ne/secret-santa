package components

import components.basic.ButtonColor
import components.basic.ButtonType
import components.basic.santaButton
import components.basic.santaInput
import getMemberCount
import getUserSessions
import joinByGuid
import kotlinx.coroutines.launch
import kotlinx.css.*
import react.*
import react.router.dom.routeLink
import removeMemberFromGame
import shared_models.model.Session
import shared_models.model.SessionState
import shared_models.model.User
import shared_models.request.JoinRequest
import shared_models.request.LeaveRequest
import styled.css
import styled.styledDiv
import styled.styledP
import styled.styledSpan

external interface GameListProps: RProps {
    var user: User
}

data class GameListState(
    var gameId: String,
    var gameList: MutableList<Pair<Session, Int>>
): RState

class GameList(props: GameListProps) : RComponent<GameListProps,GameListState>(props) {

    init {
        state.gameList = mutableListOf()
        mainScope.launch {
            val userSessions = getUserSessions(props.user) as MutableList<Session>
            val session = userSessions.map {
                Pair(it, getMemberCount(it))
            }
            setState(GameListState("", session as MutableList<Pair<Session, Int>>))
        }
    }

    private fun RBuilder.activeGameLi(session: Session,userCount: Int): ReactElement {
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
                    styledSpan {
                        css {
                            marginLeft = 15.px
                        }
                        santaButton {
                            text = "Выйти"

                            color = ButtonColor.RED
                            disabled = false
                            buttonType = ButtonType.DEFAULT

                            onClick = {
                                mainScope.launch {
                                    removeMemberFromGame(
                                        LeaveRequest(
                                            props.user.id,
                                            session.id
                                        )
                                    )
                                    //ИСПРАВИТЬ ПРОСТЫМ УДАЛЕНИЕМ ИЗ СПИСКА
                                    val userSessions = getUserSessions(props.user) as MutableList<Session>
                                    val sessions = userSessions.map {
                                        Pair(it,getMemberCount(it))
                                    }
                                    setState(GameListState("",sessions as MutableList<Pair<Session,Int>>))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun RBuilder.inactiveGameLi(session: Session,userCount: Int): ReactElement {
        return styledDiv {
            css {
                classes = mutableListOf("card", "w-75", "mx-auto")
                backgroundColor = Color("#B8BFA8")
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
                        setState(GameListState(value, state.gameList))
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
                        mainScope.launch {
                            val session = joinByGuid(JoinRequest(props.user.id, state.gameId))

                            if (session != null) {
                                val newList = state.gameList
                                newList.add(Pair(session, 1))
                                setState(GameListState(state.gameId, newList))
                            }
                        }
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

        state.gameList.filter {
            it.first.currentState == SessionState.LOBBY || it.first.currentState == SessionState.GAME
        }.forEach {
            activeGameLi(it.first, it.second)
        }

        val inActiveGame = state.gameList.filter { it.first.currentState == SessionState.FINISH }

        if (inActiveGame.isNotEmpty()) {
            styledP {
                css {
                    fontWeight = FontWeight.bold
                    fontSize = 1.rem
                    textAlign = TextAlign.center
                    margin = ""
                }
                +"Ваши прошлые игры:"
            }

            inActiveGame.forEach {
                inactiveGameLi(it.first, it.second)
            }
        }
    }
}