package components

import kotlinx.css.*
import react.*
import react.dom.attrs
import react.dom.h5
import react.dom.p
import styled.*

external interface GameViewProps: RProps {
    var isStarted: Boolean
    var gameId: String
}

data class GameViewState(var isNewGift: Boolean): RState

class GameView: RComponent<GameViewProps, GameViewState>() {

    private fun RBuilder.memberCard(fi: String): ReactElement {
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
                }
            }
        }
    }

    private fun RBuilder.giftCard(name: String, desc: String): ReactElement {
        return styledDiv {
            css {
                classes = mutableListOf("col")
            }
            styledDiv {
                css {
                    classes = mutableListOf("card")
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

    override fun RBuilder.render() {
        if (!props.isStarted) {
            //ИНФА
            styledDiv {
                css {
                    classes = mutableListOf("row")
                }

                styledDiv {
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
                    classes = mutableListOf("row", "row-cols-1", "row-cols-md-4", "g-4")
                }

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
}