package ru.tinkoff.santa.rest.plugin

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import ru.tinkoff.santa.rest.user.authentication.AuthenticationException

fun Application.exceptionHandlerModule() {
    install(StatusPages) {
        exception<AuthenticationException> {
            call.respond(HttpStatusCode.Unauthorized, it.statusCode)
        }

        exception<ContentTransformationException> {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}