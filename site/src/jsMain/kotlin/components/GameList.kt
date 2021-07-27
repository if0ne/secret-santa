package components

import components.basic.ButtonColor
import components.basic.ButtonType
import components.basic.santaButton
import components.basic.santaInput
import kotlinx.css.*
import react.*
import react.dom.p
import react.router.dom.routeLink
import styled.css
import styled.styledDiv
import styled.styledP
import styled.styledSpan

class GameList: RComponent<RProps,RState>() {

    private fun RBuilder.gameLi(id: String): ReactElement {
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
                    +"Игра $id"
                }
                styledP {
                    css {
                        margin = "0"
                    }
                    +"Количество участников: 8"
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
                styledDiv {
                    css {
                        float = Float.right
                    }
                    routeLink("/game/$id") {
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

        gameLi("5")
        gameLi("6")
    }
}