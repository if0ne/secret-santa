package components

import components.basic.ButtonColor
import components.basic.santaButton
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import react.router.dom.*
import styled.*
import styled.styledDiv

external interface AppState : RState {
    var logged: Boolean
}

@JsExport
class Application : RComponent<RProps, AppState>() {

    private fun RBuilder.link(href: String, text: String) {
        routeLink(href, className = "nav-link") {
            styledDiv {
                css {
                    //classes = mutableListOf("nav-link")
                    +ComponentStyles.navbarLink
                }
                +text
            }
        }
    }

    private fun RBuilder.navbar() {
        styledNav {
            css {
                classes = mutableListOf("navbar", "navbar-expand-lg")
                backgroundColor = Color("#A6974B")
                color = Color.white
            }

            styledDiv {
                css {
                    classes = mutableListOf("container")
                    maxWidth = 820.px
                }

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
                        link("/login", "Войти")
                    }
                    li(classes = "nav-item"){
                        link("/rules", "Правила")
                    }
                }

                routeLink("/games") {
                    santaButton() {
                        text = "Создать"
                        disabled = false
                        color = ButtonColor.ORANGE
                    }
                }
            }
        }
    }

    override fun RBuilder.render() {
        browserRouter {
            styledDiv {
                css {
                    margin(0.px)
                    padding(0.px)
                    fontFamily = "'Roboto', sans-serif"
                }

                navbar()
                styledDiv {
                    css {
                        classes = mutableListOf("container")
                        maxWidth = 820.px
                    }

                    switch {
                        route("/", exact = true) {
                            child(Profile::class) {}
                        }
                        route("/login", strict = true) {
                            child(Login::class) {}
                        }
                        route("/signup", strict = true) {
                            child(Signup::class) {}
                        }
                        route("/games", strict = true) {
                            child(GameList::class) {}
                        }
                        redirect(from = "/redirect", to = "/redirected")
                    }
                }
            }
        }
    }
}