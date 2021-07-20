package ru.tinkoff.santa.rest.session

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.singleton
import java.time.LocalDateTime

fun Application.sessionModule() {
    val service: SessionService by closestDI().instance()

    routing {
        route("/ses") {
            get {
                call.respond(HttpStatusCode.OK, service.getById(1)!!)
            }
        }
    }
}

fun DI.Builder.sessionComponents() {
    bind<SessionDao>() with singleton { SessionDao(instance()) }
    bind<SessionService>() with singleton { SessionService(instance()) }
}