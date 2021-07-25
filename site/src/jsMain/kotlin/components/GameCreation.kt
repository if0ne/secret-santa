package components

import components.basic.ButtonColor
import components.basic.ButtonType
import components.basic.santaButton
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.onChange
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import react.router.dom.*
import styled.*
import styled.styledLabel

external interface GameCreationProps: RProps {

}

data class GameCreationState(var giftValue: String): RState

class GameCreation: RComponent<GameCreationProps, GameCreationState>() {

    init {
        state.giftValue = "1000"
    }

    private fun RBuilder.dateTimePicker(): ReactElement {
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
                }
            }
        }
    }

    override fun RBuilder.render() {

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
            dateTimePicker()

            br {}

            styledLabel {
                css {
                    classes = mutableListOf("form-label")
                }
                +"Дата начала секретного санты:"
            }
            dateTimePicker()

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
                            setState(GameCreationState((it.target as HTMLInputElement).value))
                        }
                    }
                }
            }

            santaButton {
                text = "Создать"
                color = ButtonColor.DARK
                buttonType = ButtonType.SUBMIT
                disabled = false
            }
        }
    }
}