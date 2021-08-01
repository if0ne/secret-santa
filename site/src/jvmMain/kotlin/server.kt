import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.http.content.resources
import io.ktor.http.content.static
import kotlinx.html.*

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.serialization.*

fun HTML.index() {
    head {
        title("Тайный Санта")

        link("https://fonts.googleapis.com")
        link("https://fonts.gstatic.com")
        link("https://fonts.googleapis.com/css2?family=Roboto:wght@100&display=swap")
        link("https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css", "stylesheet")
    }
    body {
        div {
            id = "root"
        }
        script(src = "/static/output.js") {}
        script(src = "https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js") {}
        script(src = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js") {}
    }
}

fun main() {
    embeddedServer(Netty,port = 8080,host = "127.0.0.1") {

        install(ContentNegotiation) {
            json()
        }

        install(CORS) {
            method(HttpMethod.Get)
            method(HttpMethod.Post)
            method(HttpMethod.Delete)
            anyHost()
        }

        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            static("/static") {
                resources()
            }
        }
    }.start(wait = true)
}