package components

import components.basic.ButtonColor
import components.basic.ButtonType
import components.basic.santaButton
import kotlinx.css.*
import react.*
import react.dom.attrs
import react.dom.svg
import styled.css
import styled.styledDiv
import styled.styledImg
import styled.styledP


fun RBuilder.buildImage(href: String): ReactElement {
    return styledDiv {
        css {
            classes = mutableListOf("col-4")
            height = 200.px
            width = 200.px
            display = Display.inlineBlock
            position = Position.relative
            overflow = Overflow.hidden
            borderRadius = LinearDimension("50%")
        }
        styledImg {
            css {
                height = LinearDimension("100%")
                width = LinearDimension.auto
                marginLeft = LinearDimension("-50px")
            }

            attrs {
                src = href
            }
        }
    }
}

class Profile: RComponent<RProps,RState>() {

    override fun RBuilder.render() {
        styledP {
            css {
                +ComponentStyles.pageTitle
            }

            +"Профиль"
        }

        styledDiv {
            css {
                classes = mutableListOf("row")
            }
            buildImage("https://sun9-47.userapi.com/impf/c848624/v848624074/19f3bb/9e6Trlyf1o4.jpg?size=2560x1707&quality=96&sign=cc31343bd89d4700186721803dbb97da&type=album")
            styledDiv {
                css {
                    classes = mutableListOf("col-8")
                }

                styledP {
                    css {
                        fontWeight = FontWeight.bold
                        fontSize = (1.25).rem
                        margin = "0"
                    }

                    +"Асташкин Максим Сергеевич"
                }

                styledP {
                    css {
                        margin = "0"
                    }
                    +"maksim.astash@gmail.com"
                }

                styledDiv {
                    css {
                        classes = mutableListOf("row")
                        marginTop = 50.px
                    }

                    styledDiv {
                        css {
                            classes = mutableListOf("col")
                        }

                        santaButton {
                            color = ButtonColor.ORANGE
                            text = "Редактировать"
                            disabled = true
                            buttonType = ButtonType.FULL_WIDTH
                        }

                        santaButton {
                            color = ButtonColor.DARK
                            text = "Telegram"
                            disabled = false
                            buttonType = ButtonType.FULL_WIDTH
                        }
                    }

                    styledDiv {
                        css {
                            classes = mutableListOf("col")
                        }

                        santaButton {
                            color = ButtonColor.ORANGE
                            text = "Мои игры"
                            disabled = false
                            buttonType = ButtonType.FULL_WIDTH
                        }

                        santaButton {
                            color = ButtonColor.ORANGE
                            text = "Выйти"
                            disabled = false
                            buttonType = ButtonType.FULL_WIDTH
                        }
                    }

                    //ЗАГЛУШКИ
                    /*styledDiv {
                        css {
                            classes = mutableListOf("col")
                        }
                    }

                    styledDiv {
                        css {
                            classes = mutableListOf("col")
                        }
                    }*/
                }
            }
        }


    }
}