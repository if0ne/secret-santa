package ru.tinkoff.santa.rest.session

import ru.tinkoff.sanata.shared_models.model.Gift
import ru.tinkoff.sanata.shared_models.model.Session
import ru.tinkoff.sanata.shared_models.model.SessionState
import ru.tinkoff.sanata.shared_models.model.User
import ru.tinkoff.sanata.shared_models.response.UserInfoAboutSessionResponse
import ru.tinkoff.santa.rest.gift.GiftService
import ru.tinkoff.santa.rest.gift_giving.GiftGivingService
import ru.tinkoff.santa.rest.user.UserService
import ru.tinkoff.santa.rest.user_session.UserSessionService
import ru.tinkoff.santa.rest.user_session_gift.UserSessionGiftService
import java.time.LocalDateTime
import java.util.*

class SessionController(
    private val sessionService: SessionService,
    private val userService: UserService,
    private val giftService: GiftService,
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
        val session = sessionService.getById(sessionId) ?: throw Exception()
        if (session.currentState != SessionState.LOBBY) {
            throw Exception()
        }
        if (getUsersNumberInSession(sessionId) < session.minPlayersQuantity) {
            throw Exception()
        }
        val users = getUsersInSession(sessionId).shuffled()
        giftGivingService.create(sessionId, users.last().id, users.first().id)
        for (i in 1 until users.size) {
            giftGivingService.create(sessionId, users[i - 1].id, users[i].id)
        }
        sessionService.setCurrentState(session.id, SessionState.GAME)
    }

    fun finish(sessionId: Int) {
        val session = sessionService.getById(sessionId)
        if (session != null) {
            if (session.currentState != SessionState.GAME) {
                throw Exception()
            }
            sessionService.setCurrentState(sessionId, SessionState.FINISH)
        } else {
            throw Exception()
        }
    }

    fun joinOnSession(userId: Int, sessionGuid: UUID): Session {
        if (userService.getById(userId) == null) {
            throw Exception()
        }
        val session = sessionService.getByGuid(sessionGuid) ?: throw Exception()
        if (session.currentState != SessionState.LOBBY) {
            throw Exception()
        }
        if (userSessionService.getByUserIdAndSessionId(userId, session.id) != null) {
            throw Exception()
        }
        userSessionService.create(userId, session.id)
        return session
    }

    fun leaveOnSession(userId: Int, sessionId: Int) {
        if (userService.getById(userId) == null) {
            throw Exception()
        }
        val session = sessionService.getById(sessionId) ?: throw Exception()
        if (session.currentState != SessionState.LOBBY) {
            throw Exception()
        }
        val userSession = userSessionService.getByUserIdAndSessionId(userId, sessionId) ?: throw Exception()
        userSessionGiftService.deleteByUserSession(userSession.id)
        userSessionService.delete(userSession.id)
    }

    fun getUserInfoAboutSession(userId: Int, sessionId: Int): UserInfoAboutSessionResponse {
        val user = userService.getById(userId) ?: throw Exception()
        val session = sessionService.getById(sessionId) ?: throw Exception()
        val users = getUsersInSession(sessionId)
        val gifts = getGiftsByUserIdAndSessionId(userId, sessionId)
        val giftReceivingUser = getGiftReceivingUser(userId)
        val receivingUserGifts: List<Gift> = if (giftReceivingUser == null) {
            listOf<Gift>()
        } else {
            getGiftsByUserIdAndSessionId(giftReceivingUser.id, sessionId)
        }
        return UserInfoAboutSessionResponse(
            user,
            session,
            users,
            gifts,
            giftReceivingUser, receivingUserGifts
        )
    }

    private fun getGiftsByUserIdAndSessionId(userId: Int, sessionId: Int): List<Gift> {
        val userSession = userSessionService.getByUserIdAndSessionId(userId, sessionId) ?: throw Exception()
        return userSessionGiftService.getByUserSessionId(userSession.id).map {
            giftService.getById(it.giftId)!!
        }
    }

    private fun getGiftReceivingUser(userId: Int): User? {
        val giftGiving = giftGivingService.getByGiftGivingUserId(userId) ?: return null
        return userService.getById(giftGiving.giftReceivingUserId)
    }


    fun getUserSessionsByUserId(userId: Int): List<Session> {
        val user = userService.getById(userId) ?: throw Exception()
        return getUserSessions(user)
    }

    fun getUserSessionsByTelegramId(userTelegramId: Long): List<Session> {
        val user = userService.getByTelegramId(userTelegramId) ?: throw Exception()
        return getUserSessions(user)
    }

    private fun getUserSessions(user: User): List<Session> = userSessionService.getByUserId(user.id).map {
        sessionService.getById(it.sessionId)!!
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