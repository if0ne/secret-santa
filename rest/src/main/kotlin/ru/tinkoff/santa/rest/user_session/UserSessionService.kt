package ru.tinkoff.santa.rest.user_session

class UserSessionService(private val userSessionDao: UserSessionDao) {
    fun getAll(): List<UserSession> = userSessionDao.getAll()

    fun getById(id: Int): UserSession? = userSessionDao.getById(id)

    fun getByUserId(userId: Int): List<UserSession> = userSessionDao.getByUserId(userId)

    fun getBySessionId(sessionId: Int): List<UserSession> = userSessionDao.getBySessionId(sessionId)



    fun create(userId: Int, sessionId: Int) = userSessionDao.create(userId, sessionId)

    fun update(id: Int, userId: Int, sessionId: Int) = userSessionDao.update(id, userId, sessionId)

    fun delete(id: Int) = userSessionDao.delete(id)
}