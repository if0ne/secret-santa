package ru.tinkoff.santa.rest.user

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.singleton
import ru.tinkoff.sanata.shared_models.request.AuthenticationRequest
import ru.tinkoff.santa.rest.user.authentication.AuthenticationController
import java.util.*

fun Application.userModule() {
    val service: UserService by closestDI().instance()
    val authenticationController: AuthenticationController by closestDI().instance()

    routing {
        route("/authorization") {
            post {
                with (call.receive<AuthenticationRequest>()) {
                    call.respond(HttpStatusCode.OK, authenticationController.authorize(this.login, this.password))
                }
            }
        }
        route("/test") {
            get {
                service.create(
                    null,
                    "test",
                    "test",
                    Base64.getEncoder().encode("Ofezum41".toByteArray()),
                    "test",
                    "test",
                    null,
                    null,
                    null
                )
            }
        }
    }
}

fun DI.Builder.userComponents() {
    bind<UserDao>() with singleton { UserDao(instance()) }
    bind<UserService>() with singleton { UserService(instance()) }
    bind<AuthenticationController>() with singleton { AuthenticationController(instance()) }
}