package ru.tinkoff.santa.rest.plugin

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import ru.tinkoff.santa.rest.gift.exception.GiftNotFoundException
import ru.tinkoff.santa.rest.guid.exception.GuidException
import ru.tinkoff.santa.rest.session.exception.SessionException
import ru.tinkoff.santa.rest.user.authentication.AuthenticationException
import ru.tinkoff.santa.rest.user.exception.UserNotFoundException
import ru.tinkoff.santa.rest.user.registration.RegistrationException

fun Application.exceptionHandlerModule() {
    install(StatusPages) {
        exception<AuthenticationException> {
            call.respond(HttpStatusCode.Unauthorized, it.statusCode)
        }
        exception<RegistrationException> {
            call.respond(HttpStatusCode.NotAcceptable, it.statusCode)
        }
        exception<IllegalArgumentException> {
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<UserNotFoundException> {
            call.respond(HttpStatusCode.NotFound)
        }
        exception<GiftNotFoundException> {
            call.respond(HttpStatusCode.NotFound)
        }
        exception<SessionException> {
            call.respond(HttpStatusCode.InternalServerError, it.errorCode)
        }
        exception<GuidException> {
            call.respond(HttpStatusCode.InternalServerError, it.errorCode)
        }
    }
}