package ru.tinkoff.santa.rest.session

import ru.tinkoff.sanata.shared_models.model.*
import ru.tinkoff.sanata.shared_models.response.UserInfoAboutSessionResponse
import ru.tinkoff.santa.rest.gift.GiftService
import ru.tinkoff.santa.rest.gift_giving.GiftGivingService
import ru.tinkoff.santa.rest.user.UserService
import ru.tinkoff.santa.rest.user.exception.UserNotFoundException
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
        val userId = getRealUserId(hostId, hostTelegramId)
        userService.checkUser(userId)
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

    private fun getRealUserId(id: Int?, telegramId: Long?): Int {
        if (id != null) {
            return id
        }
        if (telegramId != null) {
            val user = userService.getByTelegramId(telegramId)
            if (user != null) {
                return user.id
            }
        }
        throw UserNotFoundException()
    }

    fun start(sessionId: Int) {
        sessionService.checkSession(sessionId)
        sessionService.checkCurrentStateIsLobby(sessionId)
        sessionService.checkUsersIsEnoughForStart(sessionId, getUsersNumberInSession(sessionId))
        val users = getUsersInSession(sessionId).shuffled()
        createGiftGivingsForSession(sessionId, users)
        sessionService.startSession(sessionId)
    }

    private fun createGiftGivingsForSession(sessionId: Int, users: List<User>) {
        var giftReceivingUser = users.first()
        var giftGivingUser = users.last()
        giftGivingService.create(sessionId, giftGivingUser.id, giftReceivingUser.id)
        val iterator = (users - giftReceivingUser).iterator()
        while (iterator.hasNext()) {
            giftGivingUser = iterator.next()
            giftGivingService.create(sessionId, giftReceivingUser.id, giftGivingUser.id)
            giftReceivingUser = giftGivingUser
        }
    }

    fun finish(sessionId: Int) {
        sessionService.checkSession(sessionId)
        sessionService.checkCurrentStateIsGame(sessionId)
        sessionService.finishSession(sessionId)
        // мб удалять данные из таблиц
    }

    private fun prematureFinish(sessionId: Int) {
        sessionService.checkSession(sessionId)
        sessionService.finishSession(sessionId)
    }

    fun joinOnSession(userId: Int, sessionGuid: UUID): Session {
        userService.checkUser(userId)
        val session = sessionService.checkAndGetSessionByGuid(sessionGuid)
        userSessionService.checkUserSessionNotExists(userId, session.id)
        sessionService.checkCurrentStateIsLobby(session.id)
        userSessionService.create(userId, session.id)
        return session
    }

    fun leaveOnSession(userId: Int, sessionId: Int) {
        userService.checkUser(userId)
        sessionService.checkSession(sessionId)
        sessionService.checkCurrentStateIsLobby(sessionId)
        val userSession = userSessionService.checkAndGetUserSession(userId, sessionId)
        userSessionGiftService.deleteByUserSession(userSession.id)
        userSessionService.delete(userSession.id)
    }

    fun getUserInfoAboutSession(userId: Int, sessionId: Int): UserInfoAboutSessionResponse {
        val user = userService.checkAndGetUser(userId)
        val session = sessionService.checkAndGetSession(sessionId)
        val userSession = userSessionService.checkAndGetUserSession(userId, sessionId)
        val users = getUsersInSession(sessionId)
        val gifts = getGiftsByUserIdAndSessionId(userSession.id)
        val giftReceivingUser = getGiftReceivingUser(userId, sessionId)
        val receivingUserGifts: List<Gift> = if (giftReceivingUser == null) {
            listOf()
        } else {
            val giftReceivingUserSession = userSessionService.checkAndGetUserSession(giftReceivingUser.id, sessionId)
            getGiftsByUserIdAndSessionId(giftReceivingUserSession.id)
        }
        return UserInfoAboutSessionResponse(
            user,
            session,
            users,
            gifts,
            giftReceivingUser, receivingUserGifts
        )
    }

    private fun getGiftsByUserIdAndSessionId(userSessionId: Int): List<Gift> =
        userSessionGiftService.getByUserSessionId(userSessionId).map {
            giftService.getById(it.giftId)!!
        }

    private fun getGiftReceivingUser(userId: Int, sessionId: Int): User? {
        val giftGiving = giftGivingService.getByGiftGivingByUserIdAndSessionId(userId, sessionId) ?: return null
        return userService.getById(giftGiving.giftReceivingUserId)
    }


    fun getSessionsByUserId(userId: Int): List<Session> =
        getSessionsByUser(userService.checkAndGetUser(userId))


    fun getSessionsByUserTelegramId(userTelegramId: Long): List<Session> =
        getSessionsByUser(userService.checkAndGetUserByTelegramId(userTelegramId))


    private fun getSessionsByUser(user: User): List<Session> =
        userSessionService.getByUserId(user.id).map {
            sessionService.getById(it.sessionId)!!
        }

    private fun getUsersInSession(sessionId: Int): List<User> = userSessionService.getBySessionId(sessionId).map {
        userService.getById(it.userId)!!
    }

    fun updateSessionsStatesAndGetChangeNotifications(): List<ChangeNotification> =
        updateSessionsStates().map {
            ChangeNotification(
                it,
                getUsersInSession(it.id)
            )
        }


    private fun updateSessionsStates(): List<Session> {
        return sessionService.getAll().filter {
            checkAndUpdateSessionState(it.id)
        }.map { sessionService.getById(it.id)!! }
    }

    private fun checkAndUpdateSessionState(sessionId: Int): Boolean {
        val session = sessionService.checkAndGetSession(sessionId)
        return when (session.currentState) {
            SessionState.LOBBY -> isSessionNeedStart(sessionId)
            SessionState.GAME -> isSessionNeedFinish(sessionId)
            else -> false
        }
    }

    private fun isSessionNeedStart(sessionId: Int): Boolean {
        val time = LocalDateTime.now()
        val session = sessionService.checkAndGetSession(sessionId)
        if (time >= session.timestampToChoose) {
            if (getUsersNumberInSession(sessionId) >= session.minPlayersQuantity) {
                start(sessionId)
            } else {
                prematureFinish(sessionId)
            }
            return true
        }
        return false
    }

    private fun isSessionNeedFinish(sessionId: Int): Boolean {
        val time = LocalDateTime.now()
        val session = sessionService.checkAndGetSession(sessionId)
        if (time >= session.eventTimestamp) {
            finish(sessionId)
            return true
        }
        return false
    }

    fun getUsersNumberInSession(sessionId: Int): Int = getUsersInSession(sessionId).size
}