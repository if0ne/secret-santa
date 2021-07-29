package ru.tinkoff.santa.rest.session

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import ru.tinkoff.sanata.shared_models.model.SessionState
import ru.tinkoff.sanata.shared_models.request.CreateSessionRequest
import ru.tinkoff.sanata.shared_models.request.JoinByGuidRequest
import ru.tinkoff.sanata.shared_models.request.JoinRequest
import ru.tinkoff.sanata.shared_models.request.LeaveRequest
import ru.tinkoff.sanata.shared_models.response.SessionInfoResponse
import ru.tinkoff.santa.rest.gift.GiftService
import ru.tinkoff.santa.rest.user.UserService
import ru.tinkoff.santa.rest.user_session.UserSessionService
import ru.tinkoff.santa.rest.user_session_gift.UserSessionGiftService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun Application.sessionModule() {
    val userService: UserService by closestDI().instance()
    val sessionService: SessionService by closestDI().instance()
    val giftService: GiftService by closestDI().instance()
    val userSessionService: UserSessionService by closestDI().instance()
    val userSessionGiftService: UserSessionGiftService by closestDI().instance()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    routing {
        route("/session") {
            route("/create") {
                post {
                    val request = call.receive<CreateSessionRequest>()
                    val hostId = request.hostId
                    var id = 0
                    if (hostId != null) {
                        id = hostId
                    } else {
                        val telegramHostId = request.hostTelegramId
                        if (telegramHostId != null) {
                            val user = userService.getByTelegramId(telegramHostId)
                            if (user != null) {
                                id = user.id
                            }
                        }
                    }
                    sessionService.create(
                        SessionState.LOBBY,
                        request.description,
                        id,
                        request.budget,
                        LocalDateTime.parse(request.eventDateTime, formatter),
                        LocalDateTime.parse(request.dateTimeToChoose, formatter),
                        request.minPlayersQuantity!!
                    )
                    userSessionService.create(
                        id,
                        sessionService.getByHostId(id).last().id
                    )
                    call.respond(HttpStatusCode.Created)
                }
            }

            route("/join") {
                post {
                    val request = call.receive<JoinRequest>()
                    userSessionService.create(request.userId, request.sessionId)
                    call.respond(HttpStatusCode.OK)
                }
            }

            route("/joinByGuid") {
                post {
                    val request = call.receive<JoinByGuidRequest>()
                    val session = sessionService.getByGuid(UUID.fromString(request.sessionGuid))
                    if (session != null) {
                        val userSession = userSessionService.getByUserIdAndSessionId(request.userId, session.id)
                        if (userSession == null) {
                            userSessionService.create(request.userId, session.id)
                            call.respond(HttpStatusCode.OK)
                        }
                    }
                }
            }

            route("/leave") {
                delete {
                    val request = call.receive<LeaveRequest>()
                    val userSession = userSessionService.getByUserIdAndSessionId(request.userId, request.sessionId)
                    if (userSession != null) {
                        val userSessionGifts = userSessionGiftService.getByUserSessionId(userSession.id)
                        userSessionGifts.forEach {
                            userSessionGiftService.delete(it.id)
                        }
                        userSessionService.delete(userSession.id)
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }

            route("/numberUsers/{id}") {
                get {
                    val sessionId = call.parameters["id"]?.toInt()
                    if (sessionId != null) {
                        val sessions = userSessionService.getBySessionId(sessionId)
                        val numberUsers = sessions.size
                        call.respond(HttpStatusCode.OK, numberUsers)
                    }
                }
            }

            route("/{id}") {
                get {
                    val sessionId = call.parameters["id"]?.toInt()
                    if (sessionId != null) {
                        val sessionInfoResponse = SessionInfoResponse(
                            sessionService.getById(sessionId)!!,
                            userSessionService.getBySessionId(sessionId).map {
                                Pair(
                                    userService.getById(it.userId)!!,
                                    userSessionGiftService.getByUserSessionId(it.id).map { userSessionGift ->
                                        giftService.getById(userSessionGift.giftId)!!
                                    }
                                )
                            }
                        )
                        call.respond(HttpStatusCode.OK, sessionInfoResponse)
                    }
                }
            }

            route("/user/{id}") {
                get {
                    call.respond(HttpStatusCode.OK, userSessionService.getByUserId(call.parameters["id"]!!.toInt()))
                }
            }

            route("/tg_user/{id}") {
                get {
                    val user = userService.getByTelegramId(call.parameters["id"]!!.toLong())
                    if (user != null) {
                        val userSession = userSessionService.getByUserId(user.id)
                        val sessionInfoResponse = userSession.groupBy {
                            it.sessionId
                        }.map {
                            SessionInfoResponse(
                                sessionService.getById(it.key)!!,
                                it.value.map { userSession ->
                                    Pair(
                                        userService.getById(userSession.userId)!!,
                                        userSessionGiftService.getByUserSessionId(userSession.id)
                                            .map { userSessionGift ->
                                                giftService.getById(userSessionGift.giftId)!!
                                            }
                                    )
                                }
                            )
                        }
                        call.respond(HttpStatusCode.OK, sessionInfoResponse)
                    }
                }
            }
        }
    }
}