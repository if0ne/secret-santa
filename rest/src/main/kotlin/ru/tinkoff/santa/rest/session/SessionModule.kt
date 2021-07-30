package ru.tinkoff.santa.rest.session

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import ru.tinkoff.sanata.shared_models.request.CreateSessionRequest
import ru.tinkoff.sanata.shared_models.request.JoinByGuidRequest
import ru.tinkoff.sanata.shared_models.request.LeaveRequest
import ru.tinkoff.sanata.shared_models.response.SessionInfoResponse
import ru.tinkoff.santa.rest.gift.GiftService
import ru.tinkoff.santa.rest.gift_giving.GiftGivingService
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
    val giftGivingService: GiftGivingService by closestDI().instance()
    val sessionController: SessionController by closestDI().instance()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    routing {
        route("/session") {
            route("/create") {
                post {
                    // с датами переделать и исключения в контроллере
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
                        )
                        call.respond(HttpStatusCode.Created)
                    }.onFailure {
                        throw IllegalArgumentException()
                    }
                }
            }

            route("/joinByGuid") {
                // исправить что человек может зайти 2 раза в одну сессию
                post {
                    runCatching {
                        call.receive<JoinByGuidRequest>()
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

            route("/usersNumber/{id}") {
                get {
                    val sessionId = call.parameters["id"]?.toInt()
                    if (sessionId != null) {
                        call.respond(HttpStatusCode.OK, sessionController.getUsersNumberInSession(sessionId))
                    } else {
                        call.respond(HttpStatusCode.BadRequest)
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
                    val userId = call.parameters["id"]?.toInt()
                    if (userId != null) {
                        call.respond(HttpStatusCode.OK, sessionController.getUserSessions(userId))
                    } else {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }

            route("/tg_user/{id}") {
                // тут сделать как выше, но сначала найти userId
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