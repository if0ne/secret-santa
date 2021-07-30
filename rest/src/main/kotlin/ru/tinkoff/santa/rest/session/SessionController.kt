package ru.tinkoff.santa.rest.session

import ru.tinkoff.sanata.shared_models.model.Session
import ru.tinkoff.sanata.shared_models.model.SessionState
import ru.tinkoff.santa.rest.user.UserService
import ru.tinkoff.santa.rest.user_session.UserSessionService
import java.time.LocalDateTime
import java.util.*

class SessionController(
    private val sessionService: SessionService,
    private val userService: UserService,
    private val userSessionService: UserSessionService
) {
    fun create(
        description: String?,
        hostId: Int?,
        hostTelegramId: Long?,
        budget: Int,
        eventDateTime: LocalDateTime,
        dateTimeToChoose: LocalDateTime,
        minPlayersQuantity: Int = 3
    ) {
        val userId = userService.getRealUserId(hostId, hostTelegramId)
        sessionService.create(
            SessionState.LOBBY,
            description,
            userId,
            budget,
            eventDateTime,
            dateTimeToChoose,
            minPlayersQuantity
        )
        userSessionService.create(
            userId,
            sessionService.getByHostId(userId).last().id
        )
    }

    fun joinOnSession(userId: Int, sessionGuid: UUID): Session {
        val session = sessionService.getByGuid(sessionGuid)
        if (session != null) {
            userSessionService.create(userId, session.id)
            return session
        } else {
            throw Exception()
        }
    }

    fun getUserSessions(userId: Int): List<Session> {
        if (userService.getById(userId) == null) {
            throw Exception()
        }
        return userSessionService.getByUserId(userId).map {
            sessionService.getById(it.sessionId)!!
        }
    }

    fun getUsersNumberInSession(sessionId: Int): Int {
        if (sessionService.getById(sessionId) == null) {
            throw Exception()
        }
        return userSessionService.getBySessionId(sessionId).size
    }
}