package components

import components.basic.ButtonColor
import components.basic.ButtonType
import components.basic.santaButton
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.router.dom.*
import styled.*
import styled.styledLabel
import kotlin.js.Date

external interface GameCreationProps: RProps {
    //var user: User
}

data class GameCreationState(
    var giftValue: String,
    var presentDate: Date,
    var startDate: Date,

    var isWrongConfig: Boolean,
    var isCreated: Boolean
): RState

class GameCreation: RComponent<GameCreationProps, GameCreationState>() {

    init {
        state.giftValue = "1000"
        state.presentDate = Date("2021-12-25T12:00:00")
        state.startDate = Date("2021-12-25T12:00:00")
        state.isWrongConfig = false
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
                        val withTime = Date(
                            pureDate.getFullYear(),
                            pureDate.getMonth(),
                            pureDate.getDate(),
                            state.presentDate.getHours(),
                            state.presentDate.getMinutes()
                        )
                        setState(GameCreationState(
                            state.giftValue,
                            withTime,
                            state.startDate,
                            state.isWrongConfig,
                            state.isCreated
                        ))
                    },
                    {
                        val pureTime = Date((it.target as HTMLInputElement).value)
                        val withDate = Date(
                            state.presentDate.getFullYear(),
                            state.presentDate.getMonth(),
                            state.presentDate.getDate(),
                            pureTime.getHours(),
                            pureTime.getHours()
                        )
                        setState(GameCreationState(
                            state.giftValue,
                            withDate,
                            state.startDate,
                            state.isWrongConfig,
                            state.isCreated
                        ))
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
                        val withTime = Date(
                            pureDate.getFullYear(),
                            pureDate.getMonth(),
                            pureDate.getDate(),
                            state.startDate.getHours(),
                            state.startDate.getMinutes()
                        )
                        setState(GameCreationState(
                            state.giftValue,
                            state.presentDate,
                            withTime,
                            state.isWrongConfig,
                            state.isCreated
                        ))
                    },
                    {
                        val pureTime = Date((it.target as HTMLInputElement).value)
                        val withDate = Date(
                            state.startDate.getFullYear(),
                            state.startDate.getMonth(),
                            state.startDate.getDate(),
                            pureTime.getHours(),
                            pureTime.getHours()
                        )
                        setState(GameCreationState(
                            state.giftValue,
                            state.presentDate,
                            withDate,
                            state.isWrongConfig,
                            state.isCreated
                        ))
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
                                setState(GameCreationState(
                                    (it.target as HTMLInputElement).value,
                                    state.presentDate,
                                    state.startDate,
                                    state.isWrongConfig,
                                    state.isCreated
                                ))
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
                        if (state.startDate >= state.presentDate) {
                            //TODO: POST-запрос с созданием игры
                            setState(GameCreationState(state.giftValue, state.presentDate, state.startDate, state.isWrongConfig, true))
                        } else {
                            setState(GameCreationState(state.giftValue, state.presentDate, state.startDate, true, state.isCreated))
                        }
                    }
                }

                if (state.isWrongConfig) {
                    styledLabel {
                        css {
                            classes = mutableListOf("form-text")
                            color = Color("#8C1F1F")
                        }

                        +("Дата выбора подарка должна быть раньше, чем дата начала игры")
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
    //TODO: СДЕЛАТЬ СРАВНЕНИЕ ДАТ
    return 0
}
