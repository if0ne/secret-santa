package components

import ComponentStyles
import components.basic.ButtonColor
import components.basic.ButtonType
import components.basic.santaButton
import createSession
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.attrs
import react.dom.br
import react.dom.defaultValue
import react.dom.div
import react.router.dom.routeLink
import shared_models.model.User
import shared_models.request.CreateSessionRequest
import styled.*
import kotlin.js.Date

external interface GameCreationProps : RProps {
    var user: User
}

enum class WrongConfig(val message: String) {
    NONE(""),
    NULLABLE("Дата задана неправильно"),
    BEFORE("Дата выбора подарка должна быть раньше, чем дата начала игры"),
    EARLY("Дата должна быть позже, чем сегодняшняя дата")
}

//ПРОСТО ЛУЧШЕ НЕ ТРОГАТЬ
//ЖЕЛАТЕЛЬНО ПЕРЕПИСАТЬ
fun Date.toOurFormat(): String =
    "${this.getFullYear()}-${this.getMonth()}-${this.getDate()} ${if (this.getHours() < 10) "0"+this.getHours().toString() else this.getHours()}:${if (this.getMinutes() < 10) "0"+this.getMinutes() else this.getMinutes()}"

data class GameCreationState(
    var giftValue: String,
    var presentDate: Date?,
    var startDate: Date?,

    var wrongConfig: WrongConfig,
    var isCreated: Boolean
) : RState

class GameCreation : RComponent<GameCreationProps, GameCreationState>() {

    init {
        state.giftValue = "1000"
        state.presentDate = Date("2021-12-25T12:00:00")
        state.startDate = Date("2021-12-25T12:00:00")
        state.wrongConfig = WrongConfig.NONE
    }

    private fun RBuilder.dateTimePicker(callbackDate: (Event) -> Unit, callbackTime: (Event) -> Unit): ReactElement {
        return styledDiv {
            css {
                classes = mutableListOf("form-control")
                width = LinearDimension("75%")
            }
            styledInput {
                css {
                    width = 10.rem
                    padding = "0.25rem 0 0.25rem 0.5rem"
                    border = "none"
                }
                attrs {
                    type = InputType.date
                    defaultValue = "2021-12-25"

                    onChangeFunction = callbackDate
                }
            }
            styledSpan {
                css {
                    height = 1.rem
                    marginRight = (0.25).rem
                    marginLeft = (0.25).rem
                    borderRight = "1px solid #ddd"
                }
            }
            styledInput {
                css {
                    width = (5.5).rem
                    padding = "0.25rem 0.5rem 0.25rem 0"
                    border = "none"
                }
                attrs {
                    type = InputType.time
                    defaultValue = "12:00"

                    onChangeFunction = callbackTime
                }
            }
        }
    }

    private fun RBuilder.configPanel(): ReactElement {
        return div {
            styledP {
                css {
                    +ComponentStyles.pageTitle
                }

                +"Создание игры"
            }

            styledDiv {
                css {
                    classes = mutableListOf("col-6")
                }

                styledLabel {
                    css {
                        classes = mutableListOf("form-label")
                    }
                    +"Дата выбора подарка:"
                }
                dateTimePicker(
                    {
                        val pureDate = Date((it.target as HTMLInputElement).value)
                        if (!pureDate.getTime().isNaN()) {
                            val withTime = Date(
                                pureDate.getFullYear(),
                                pureDate.getMonth(),
                                pureDate.getDate(),
                                state.presentDate?.getHours() ?: 0,
                                state.presentDate?.getMinutes() ?: 0
                            )
                            setState(
                                GameCreationState(
                                    state.giftValue,
                                    withTime,
                                    state.startDate,
                                    state.wrongConfig,
                                    state.isCreated
                                )
                            )
                        } else {
                            setState(
                                GameCreationState(
                                    state.giftValue,
                                    null,
                                    state.startDate,
                                    state.wrongConfig,
                                    state.isCreated
                                )
                            )
                        }
                    },
                    {
                        val pureTime = ((it.target as HTMLInputElement).value).split(":")
                        val withDate = Date(
                            state.presentDate?.getFullYear() ?: 0,
                            state.presentDate?.getMonth() ?: 0,
                            state.presentDate?.getDate() ?: 0,
                            pureTime[0].toInt(),
                            pureTime[1].toInt()
                        )
                        setState(
                            GameCreationState(
                                state.giftValue,
                                withDate,
                                state.startDate,
                                state.wrongConfig,
                                state.isCreated
                            )
                        )
                    }
                )

                br {}

                styledLabel {
                    css {
                        classes = mutableListOf("form-label")
                    }
                    +"Дата начала секретного санты:"
                }
                dateTimePicker(
                    {
                        val pureDate = Date((it.target as HTMLInputElement).value)
                        if (!pureDate.getTime().isNaN()) {
                            val withTime = Date(
                                pureDate.getFullYear(),
                                pureDate.getMonth(),
                                pureDate.getDate(),
                                state.startDate?.getHours() ?: 0,
                                state.startDate?.getMinutes() ?: 0
                            )
                            setState(
                                GameCreationState(
                                    state.giftValue,
                                    state.presentDate,
                                    withTime,
                                    state.wrongConfig,
                                    state.isCreated
                                )
                            )
                        } else {
                            GameCreationState(
                                state.giftValue,
                                state.presentDate,
                                null,
                                state.wrongConfig,
                                state.isCreated
                            )
                        }
                    },
                    {
                        val pureTime = ((it.target as HTMLInputElement).value).split(":")
                        val withDate = Date(
                            state.startDate?.getFullYear() ?: 0,
                            state.startDate?.getMonth() ?: 0,
                            state.startDate?.getDate() ?: 0,
                            pureTime[0].toInt(),
                            pureTime[1].toInt()
                        )
                        setState(
                            GameCreationState(
                                state.giftValue,
                                state.presentDate,
                                withDate,
                                state.wrongConfig,
                                state.isCreated
                            )
                        )
                    }
                )

                br {}

                styledLabel {
                    attrs {
                        id = "giftValue"
                    }
                    +"Цена подарка: "
                    styledSpan {
                        css {
                            fontWeight = FontWeight.bold
                        }

                        +"${state.giftValue} ₽"
                    }
                    styledInput {
                        css {
                            classes = mutableListOf("form-range")
                            width = LinearDimension("75%")
                        }

                        attrs {
                            id = "giftValue"
                            type = InputType.range
                            min = "100"
                            max = "10000"
                            step = "100"
                            defaultValue = "1000"

                            onChangeFunction = {
                                setState(
                                    GameCreationState(
                                        (it.target as HTMLInputElement).value,
                                        state.presentDate,
                                        state.startDate,
                                        state.wrongConfig,
                                        state.isCreated
                                    )
                                )
                            }
                        }
                    }
                }

                santaButton {
                    text = "Создать"
                    color = ButtonColor.DARK
                    buttonType = ButtonType.SUBMIT
                    disabled = false

                    onClick = {
                        if (state.startDate != null && state.presentDate != null) {
                            if (state.presentDate!!.getTime() > Date.now()) {
                                if (state.startDate!! >= state.presentDate!!) {
                                    mainScope.launch {
                                        try {
                                            createSession(CreateSessionRequest(
                                                "",
                                                props.user.id,
                                                props.user.telegramId,
                                                state.giftValue.toInt(),
                                                state.startDate!!.toOurFormat(),
                                                state.presentDate!!.toOurFormat(),
                                                3
                                            ))
                                            setState(
                                                GameCreationState(
                                                    state.giftValue,
                                                    state.presentDate,
                                                    state.startDate,
                                                    state.wrongConfig,
                                                    true
                                                )
                                            )
                                        } catch (ex: Exception) {}
                                    }
                                } else {
                                    setState(
                                        GameCreationState(
                                            state.giftValue,
                                            state.presentDate,
                                            state.startDate,
                                            WrongConfig.BEFORE,
                                            state.isCreated
                                        )
                                    )
                                }
                            } else {
                                setState(
                                    GameCreationState(
                                        state.giftValue,
                                        state.presentDate,
                                        state.startDate,
                                        WrongConfig.EARLY,
                                        state.isCreated
                                    )
                                )
                            }
                        } else {
                            setState(
                                GameCreationState(
                                    state.giftValue,
                                    state.presentDate,
                                    state.startDate,
                                    WrongConfig.NULLABLE,
                                    state.isCreated
                                )
                            )
                        }
                    }
                }

                br{}

                if (state.wrongConfig != WrongConfig.NONE) {
                    styledLabel {
                        css {
                            classes = mutableListOf("form-text")
                            color = Color("#8C1F1F")
                        }

                        +state.wrongConfig.message
                    }
                }
            }
        }
    }

    private fun RBuilder.createdGame(): ReactElement {
        return div {
            styledP {
                css {
                    +ComponentStyles.pageTitle
                    marginBottom = 0.px
                }

                +"Игра успешно создана"
            }

            routeLink("/games") {
                styledP {
                    css {
                        margin = "0"
                        fontSize = (1.25).rem
                    }
                    +"Вернуться"
                }
            }
        }
    }

    override fun RBuilder.render() {
        if (state.isCreated) {
            createdGame()
        } else {
            configPanel()
        }
    }
}

private operator fun Date.compareTo(date: Date): Int {
    return this.getTime().compareTo(date.getTime())
}
