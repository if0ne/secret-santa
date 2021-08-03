package ru.tinkoff.santa.rest.session

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import ru.tinkoff.sanata.shared_models.request.CreateSessionRequest
import ru.tinkoff.sanata.shared_models.request.JoinRequest
import ru.tinkoff.sanata.shared_models.request.LeaveRequest
import ru.tinkoff.sanata.shared_models.request.UserSessionInfoRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun Application.sessionModule() {
    val sessionController: SessionController by closestDI().instance()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    routing {
        route("/session") {
            route("/notifications") {
                get {
                    call.respond(HttpStatusCode.OK, sessionController.updateSessionsStatesAndGetChangeNotifications())
                }
            }

            route("/create") {
                post {
                    runCatching {
                        call.receive<CreateSessionRequest>()
                    }.onSuccess {
                        sessionController.create(
                            it.description,
                            it.hostId,
                            it.hostTelegramId,
                            it.budget,
                            LocalDateTime.parse(it.eventDateTime, formatter),
                            LocalDateTime.parse(it.dateTimeToChoose, formatter),
                            it.minPlayersQuantity
                        )
                        call.respond(HttpStatusCode.Created)
                    }.onFailure {
                        throw IllegalArgumentException()
                    }
                }
            }

            route("/join") {
                post {
                    runCatching {
                        call.receive<JoinRequest>()
                    }.onSuccess {
                        call.respond(
                            HttpStatusCode.OK,
                            sessionController.joinOnSession(it.userId, UUID.fromString(it.sessionGuid))
                        )
                    }.onFailure {
                        throw IllegalArgumentException()
                    }
                }
            }

            route("/leave") {
                delete {
                    runCatching {
                        call.receive<LeaveRequest>()
                    }.onSuccess {
                        sessionController.leaveOnSession(it.userId, it.sessionId)
                        call.respond(HttpStatusCode.OK)
                    }.onFailure {
                        throw IllegalArgumentException()
                    }
                }
            }

            route("/usersNumber/{id}") {
                get {
                    val sessionId = call.parameters["id"]?.toInt()
                    if (sessionId != null) {
                        call.respond(HttpStatusCode.OK, sessionController.getUsersNumberInSession(sessionId))
                    } else {
                        throw IllegalArgumentException()
                    }
                }
            }

            route("/userInfo") {
                post {
                    runCatching {
                        call.receive<UserSessionInfoRequest>()
                    }.onSuccess {
                        call.respond(
                            HttpStatusCode.OK,
                            sessionController.getUserInfoAboutSession(it.userId, it.sessionId)
                        )
                    }.onFailure {
                        throw IllegalArgumentException()
                    }
                }
            }

            route("/user/{id}") {
                get {
                    val userId = call.parameters["id"]?.toInt()
                    if (userId != null) {
                        call.respond(HttpStatusCode.OK, sessionController.getSessionsByUserId(userId))
                    } else {
                        throw IllegalArgumentException()
                    }
                }
            }

            route("/tg_user/{id}") {
                get {
                    val userTelegramId = call.parameters["id"]?.toLong()
                    if (userTelegramId != null) {
                        call.respond(HttpStatusCode.OK, sessionController.getSessionsByUserTelegramId(userTelegramId))
                    } else {
                        throw IllegalArgumentException()
                    }
                }
            }
        }
    }
}