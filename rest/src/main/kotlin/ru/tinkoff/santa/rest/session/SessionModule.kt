package ru.tinkoff.santa.rest.session

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import ru.tinkoff.sanata.shared_models.model.SessionState
import java.time.LocalDateTime
import java.util.*

fun Application.sessionModule() {
    val sessionService: SessionService by closestDI().instance()

    routing{
        route("/session"){
            route("/user/{id}"){
                get{
                    call.respond(HttpStatusCode.OK, sessionService.getByHostId(call.parameters["id"]!!.toInt()))
                }
            }
        }
    }

}