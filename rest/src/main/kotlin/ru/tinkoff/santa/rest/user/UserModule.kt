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
import ru.tinkoff.sanata.shared_models.request.SetAvatarUrlRequest
import ru.tinkoff.santa.rest.user.authentication.AuthenticationController
import ru.tinkoff.santa.rest.user.exception.UserNotFoundException
import ru.tinkoff.santa.rest.user.registration.RegistrationController
import ru.tinkoff.santa.rest.user.registration.RegistrationController.Companion.isPasswordSafety

fun Application.userModule() {
    val userService: UserService by closestDI().instance()
    val authenticationController: AuthenticationController by closestDI().instance()
    val registrationController: RegistrationController by closestDI().instance()
    val userController: UserController by closestDI().instance()

    routing {
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
                                it.phone,
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

            route("/avatarUrl") {
                post {
                    runCatching {
                        call.receive<SetAvatarUrlRequest>()
                    }.onSuccess {
                        userService.setAvatarUrl(it.userId, it.avatarUrl)
                        call.respond(HttpStatusCode.OK)
                    }.onFailure {
                        throw IllegalArgumentException()
                    }
                }
            }

            route("telegram/{id}"){
                get{
                    val userTelegramId = call.parameters["id"]?.toLong()
                    if (userTelegramId != null) {
                        call.respond(HttpStatusCode.OK, userService.checkAndGetUserByTelegramId(userTelegramId))
                    } else {
                        throw IllegalArgumentException()
                    }
                }
            }

            route("/emailfree/{email}") {
                get {
                    call.respond(HttpStatusCode.OK, userService.getByEmail(call.parameters["email"].toString()) == null)
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