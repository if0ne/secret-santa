package ru.tinkoff.santa.rest.user_session

import ru.tinkoff.sanata.shared_models.status.SessionErrorCode
import ru.tinkoff.santa.rest.session.exception.SessionException

class UserSessionService(private val userSessionDao: UserSessionDao) {
    fun getAll(): List<UserSession> = userSessionDao.getAll()

    fun getById(id: Int): UserSession? = userSessionDao.getById(id)

    fun getByUserId(userId: Int): List<UserSession> = userSessionDao.getByUserId(userId)

    fun getBySessionId(sessionId: Int): List<UserSession> = userSessionDao.getBySessionId(sessionId)

    private fun getByUserIdAndSessionId(userId: Int, sessionId: Int): UserSession? =
        userSessionDao.getByUserIdAndSessionId(userId, sessionId)

    fun create(userId: Int, sessionId: Int) = userSessionDao.create(userId, sessionId)

    fun update(id: Int, userId: Int, sessionId: Int) = userSessionDao.update(id, userId, sessionId)

    fun checkUserSession(userId: Int, sessionId: Int) {
        if (getByUserIdAndSessionId(userId, sessionId) == null) {
            throw SessionException(SessionErrorCode.USER_NOT_IN_SESSION)
        }
    }

    fun checkUserSessionNotExists(userId: Int, sessionId: Int) {
        if (getByUserIdAndSessionId(userId, sessionId) != null) {
            throw SessionException(SessionErrorCode.USER_ALREADY_IN_SESSION)
        }
    }

    fun checkAndGetUserSession(userId: Int, sessionId: Int): UserSession =
        getByUserIdAndSessionId(userId, sessionId) ?: throw SessionException(SessionErrorCode.USER_NOT_IN_SESSION)

    fun delete(id: Int) = userSessionDao.delete(id)
}