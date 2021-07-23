package components

import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.classes
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import styled.css
import styled.styledDiv

@JsExport
class Application : RComponent<RProps,RState>() {

    private fun RBuilder.navbar() {
        nav(classes = "navbar navbar-expand-lg navbar-dark bg-dark"){
            div(classes = "container"){
                a(href = "#", classes = "navbar-brand"){
                    +"Santa"
                }
                button(classes = "navbar-toggler", type = ButtonType.button){
                    attrs {
                        attributes["data-toggle"] = "collapse"
                        attributes["data-target"] = "#myNavbar"
                        attributes["aria-controls"] = "navbarContent"
                        attributes["aria-expanded"] = "false"
                    }
                    span(classes = "navbar-toggler-icon"){}
                }
                div(classes = "collapse navbar-collapse"){
                    attrs{
                        attributes["id"] = "myNavbar"
                    }
                    ul(classes = "navbar-nav mr-auto mb-2") {
                        li(classes = "nav-item"){
                            a(classes = "nav-link"){
                                +"first"
                            }
                        }
                        li(classes = "nav-item"){
                            a(classes = "nav-link"){
                                +"two"
                            }
                        }
                        li(classes = "nav-item"){
                            a(classes = "nav-link"){
                                +"three"
                            }
                        }
                        li(classes = "nav-item"){
                            a(classes = "nav-link"){
                                +"four"
                            }
                        }
                    }
                }
            }
        }
    }

    override fun RBuilder.render() {
        navbar()
        styledDiv {
            css {
                classes = mutableListOf("alert alert-warning text-center")
            }
            div {
                +"main"
            }
        }
        h1 {
            +"bottom"
        }

    }
}