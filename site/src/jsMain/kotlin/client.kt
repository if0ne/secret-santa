import components.Application

import react.dom.render
import kotlinx.browser.document
import kotlinx.browser.window
import react.router.dom.useHistory

fun main() {
    window.onload = {
        render(document.getElementById("root")) {
            child(Application::class) {}
        }
    }
}
