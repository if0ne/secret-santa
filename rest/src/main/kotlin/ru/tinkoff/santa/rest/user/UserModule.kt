package ru.tinkoff.santa.rest.user

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import ru.tinkoff.sanata.shared_models.request.AuthenticationRequest
import ru.tinkoff.sanata.shared_models.request.RegistrationRequest
import ru.tinkoff.santa.rest.user.authentication.AuthenticationController
import ru.tinkoff.santa.rest.user.exception.UserNotFoundException
import ru.tinkoff.santa.rest.user.registration.RegistrationController
import ru.tinkoff.santa.rest.user.registration.RegistrationController.Companion.isPasswordSafety
import java.lang.IllegalArgumentException

fun Application.userModule() {
    val userService: UserService by closestDI().instance()
    val authenticationController: AuthenticationController by closestDI().instance()
    val registrationController: RegistrationController by closestDI().instance()
    val userController: UserController by closestDI().instance()

    routing {
        route("hello") {
            get {
                call.respondText("Hello")
            }
        }
        route("/user") {
            route("/authorization") {
                post {
                    runCatching {
                        call.receive<AuthenticationRequest>()
                    }.onSuccess {
                        call.respond(HttpStatusCode.OK, authenticationController.authenticate(it.login, it.password))
                    }.onFailure {
                        throw IllegalArgumentException()
                    }
                }
            }
            route("/registration") {
                post {
                    runCatching {
                        call.receive<RegistrationRequest>()
                    }.onSuccess {
                        call.respond(
                            HttpStatusCode.Created,
                            registrationController.register(
                                it.nickname,
                                it.email,
                                it.password,
                                it.firstName,
                                it.lastName,
                                it.middleName
                            )
                        )
                    }.onFailure {
                        throw IllegalArgumentException()
                    }
                }
            }
            route("/emailfree/{email}") {
                get {
                    call.respond(HttpStatusCode.OK, userService.getByEmail(call.parameters["email"].toString()) == null)
                }
            }
            route("/nicknamefree/{nickname}") {
                get {
                    call.respond(
                        HttpStatusCode.OK,
                        userService.getByNickname(call.parameters["nickname"].toString()) == null
                    )
                }
            }
            route("/checkpassword") {
                post {
                    call.respond(HttpStatusCode.OK, call.receiveText().isPasswordSafety())
                }
            }
            route("/info/{id}") {
                get {
                    call.respond(
                        HttpStatusCode.OK,
                        userService.getById(call.parameters["id"]?.toInt() ?: throw IllegalArgumentException())
                            ?: throw UserNotFoundException()
                    )
                }
            }
        }
    }
}