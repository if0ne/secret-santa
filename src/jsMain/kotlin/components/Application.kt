package components

import components.basic.ButtonColor
import components.basic.santaButton
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import kotlinx.css.properties.Transitions
import kotlinx.css.properties.s
import kotlinx.css.properties.transition

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import react.router.dom.browserRouter
import react.router.dom.navLink
import react.router.dom.route

import react.router.dom.switch

import styled.css
import styled.styledA
import styled.styledDiv
import styled.styledNav

@JsExport
class Application : RComponent<RProps,RState>() {

    private fun RBuilder.link(href: String, text: String) {
        styledA(href = href) {
            css {
                classes = mutableListOf("nav-link")
                +ComponentStyles.navbarLink
            }
            +text
        }
    }

    private fun RBuilder.navbar() {
        styledNav {
            css {
                classes = mutableListOf("navbar", "navbar-expand-lg")
                backgroundColor = Color("#A6974B")
                color = Color.white
            }

            div(classes = "container"){
                styledA(href = "#") {
                    css {
                        classes = mutableListOf("navbar-bra nd")
                        color = Color.white
                        fontWeight = FontWeight.bold
                        textDecoration = TextDecoration.none
                    }

                    css.hover {
                        color = Color("#322C40")
                    }

                    +"Тайный Санта"
                }
                ul(classes = "navbar-nav ms-md-auto") {
                    li(classes = "nav-item"){
                        link("#", "Войти")
                    }
                    li(classes = "nav-item"){
                        link("#", "Правила")
                    }
                }

                santaButton() {
                    text = "Создать"
                    disabled = false
                    color = ButtonColor.ORANGE
                }
            }
        }
    }

    override fun RBuilder.render() {

        styledDiv {
            css {
                margin(0.px)
                padding(0.px)
                fontFamily = "'Roboto', sans-serif"
            }

            navbar()
            styledDiv {
                css {
                    classes = mutableListOf("container px-4 px-md-3")
                }
            }
        }

    }
}