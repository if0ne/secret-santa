package components

import components.basic.*
import createGift
import getSessionInformation
import kotlinx.coroutines.launch
import kotlinx.css.*
import react.*
import react.dom.attrs
import react.dom.p
import removeMemberFromGame
import shared_models.model.Gift
import shared_models.model.Session
import shared_models.model.SessionState
import shared_models.model.User
import shared_models.request.CreateGiftRequest
import shared_models.request.LeaveRequest
import shared_models.request.UserSessionInfoRequest
import shared_models.response.UserInfoAboutSessionResponse
import styled.*

external interface GameViewProps: RProps {
    var user: User
    var gameId: Int
}

data class GameViewState(
    var isNewGift: Boolean,
    var info: UserInfoAboutSessionResponse,

    var giftName: Pair<String, Boolean>,
    var giftDesc: String
): RState

class GameView(props: GameViewProps): RComponent<GameViewProps, GameViewState>(props) {

    init {
        state.info = UserInfoAboutSessionResponse(
            props.user,
            Session(0, "", SessionState.LOBBY, "", 0, 0, 0, "", ""),
            listOf(),
            listOf(),
            props.user,
            listOf()
        )
        mainScope.launch {
            val session = getSessionInformation(UserSessionInfoRequest(props.user.id, props.gameId))
            setState(GameViewState(state.isNewGift, session!!, state.giftName, state.giftDesc))
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
                        src = user.avatarUrl ?: "https://www.pinclipart.com/picdir/big/564-5646085_santa-claus-logo-png-clipart.png"
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
                        +"${user.lastName} ${user.firstName}"
                    }
                    if (props.user.id == state.info.session.hostId &&
                        state.info.session.currentState == SessionState.LOBBY &&
                        props.user.id != user.id) {
                        santaButton {
                            text = "Удалить"
                            color = ButtonColor.RED
                            buttonType = ButtonType.FULL_WIDTH

                            onClick = {
                                mainScope.launch {
                                    removeMemberFromGame(LeaveRequest(
                                        user.id,
                                        state.info.session.id
                                    ))

                                    val newUsers = state.info.users.filter { it.id != user.id }
                                    val newInfo = UserInfoAboutSessionResponse(
                                        state.info.user,
                                        state.info.session,
                                        newUsers,
                                        state.info.userGifts,
                                        state.info.giftReceivingUser,
                                        state.info.receivingUserGifts
                                    )
                                    setState(GameViewState(state.isNewGift, newInfo, state.giftName, state.giftDesc))
                                }
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
                +"Игра №${state.info.session.id}"
            }

            styledP {
                css {
                    margin = "0"
                }
                +"Дата мероприятия: ${state.info.session.eventTimestamp}"
            }

            styledP {
                +"Средняя цена подарка: "
                styledSpan {
                    css {
                        fontWeight = FontWeight.bold
                    }
                    +"${state.info.session.budget}₽"
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
                        +(state.info.session.guid ?: "")
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

                for (item in state.info.userGifts) {
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
                                state.info,
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
                                state.info,
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
                            setState(GameViewState(!state.isNewGift, state.info, state.giftName, state.giftDesc))
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
                                    mainScope.launch {
                                        val gift = createGift(CreateGiftRequest(
                                            props.user.id,
                                            state.info.session.id,
                                            state.giftName.first,
                                            state.giftDesc
                                        ))
                                        if (gift != null) {
                                            val newGifts = state.info.userGifts as MutableList
                                            newGifts.add(gift)
                                            val newInfo = UserInfoAboutSessionResponse(
                                                state.info.user,
                                                state.info.session,
                                                state.info.users,
                                                newGifts,
                                                state.info.giftReceivingUser,
                                                state.info.receivingUserGifts
                                            )
                                            setState(GameViewState(false, newInfo, Pair("", false), ""))
                                        }
                                    }
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

                state.info.users.forEach {
                    memberCard(it)
                }
            }
        }
    }

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

                memberCard(state.info.giftReceivingUser!!)
            }

            styledDiv {
                css {
                    classes = mutableListOf("row","row-cols-1","row-cols-md-3","g-4")
                }

                state.info.receivingUserGifts.forEach {
                    giftCard(it)
                }
            }
        }
    }

    override fun RBuilder.render() {
        if (state.info.session.currentState == SessionState.LOBBY) {
            unstartedGame()
        } else {
            startedGame()
        }
    }
}