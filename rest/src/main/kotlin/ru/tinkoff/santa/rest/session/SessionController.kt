package ru.tinkoff.santa.rest.session

import ru.tinkoff.sanata.shared_models.model.Session
import ru.tinkoff.sanata.shared_models.model.SessionState
import ru.tinkoff.sanata.shared_models.model.User
import ru.tinkoff.santa.rest.gift_giving.GiftGivingService
import ru.tinkoff.santa.rest.user.UserService
import ru.tinkoff.santa.rest.user_session.UserSessionService
import ru.tinkoff.santa.rest.user_session_gift.UserSessionGiftService
import java.time.LocalDateTime
import java.util.*

class SessionController(
    private val sessionService: SessionService,
    private val userService: UserService,
    private val userSessionService: UserSessionService,
    private val userSessionGiftService: UserSessionGiftService,
    private val giftGivingService: GiftGivingService
) {
    fun create(
        description: String?,
        hostId: Int?,
        hostTelegramId: Long?,
        budget: Int,
        eventDateTime: LocalDateTime,
        dateTimeToChoose: LocalDateTime,
        minPlayersQuantity: Int?
    ) {
        val userId = userService.getRealUserId(hostId, hostTelegramId)
        if (userService.getById(userId) == null) {
            throw Exception()
        }
        if (minPlayersQuantity == null) {
            sessionService.create(
                SessionState.LOBBY,
                description,
                userId,
                budget,
                eventDateTime,
                dateTimeToChoose
            )
        } else {
            sessionService.create(
                SessionState.LOBBY,
                description,
                userId,
                budget,
                eventDateTime,
                dateTimeToChoose,
                minPlayersQuantity
            )
        }
        userSessionService.create(
            userId,
            sessionService.getByHostId(userId).last().id
        )
    }

    fun start(sessionId: Int) {
        val session = sessionService.getById(sessionId)
        if (session != null) {
            if (session.currentState != SessionState.LOBBY) {
                throw Exception()
            }
            if (getUsersNumberInSession(sessionId) < session.minPlayersQuantity) {
                throw Exception()
            }
            var users = getUsersInSession(sessionId).shuffled()
            giftGivingService.create(sessionId, users.last().id, users.first().id)
            for (i in 1 until users.size) {
                giftGivingService.create(sessionId, users[i - 1].id, users[i].id)
            }
            sessionService.setCurrentState(session.id, SessionState.GAME)
        } else {
            throw Exception()
        }
    }

    fun joinOnSession(userId: Int, sessionGuid: UUID): Session {
        if (userService.getById(userId) == null) {
            throw Exception()
        }
        val session = sessionService.getByGuid(sessionGuid)
        if (session != null) {
            userSessionService.create(userId, session.id)
            return session
        } else {
            throw Exception()
        }
    }

    fun leaveOnSession(userId: Int, sessionId: Int) {
        if (userService.getById(userId) == null) {
            throw Exception()
        }
        if (sessionService.getById(sessionId) == null) {
            throw Exception()
        }
        val userSession = userSessionService.getByUserIdAndSessionId(userId, sessionId)
        if (userSession != null) {
            userSessionGiftService.deleteByUserSession(userSession.id)
            userSessionService.delete(userSession.id)
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

    private fun getUsersInSession(sessionId: Int): List<User> {
        if (sessionService.getById(sessionId) == null) {
            throw Exception()
        }
        return userSessionService.getBySessionId(sessionId).map {
            userService.getById(it.userId)!!
        }
    }

    fun getUsersNumberInSession(sessionId: Int): Int = getUsersInSession(sessionId).size
}