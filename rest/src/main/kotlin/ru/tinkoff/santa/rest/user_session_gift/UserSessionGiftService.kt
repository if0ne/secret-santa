package ru.tinkoff.santa.rest.user_session_gift

class UserSessionGiftService(private val userSessionGiftDao: UserSessionGiftDao) {
    fun getAll(): List<UserSessionGift> = userSessionGiftDao.getAll()

    fun getById(id: Int): UserSessionGift? = userSessionGiftDao.getById(id)

    fun getByUserSessionId(userSessionId: Int): List<UserSessionGift> =
        userSessionGiftDao.getByUserSessionId(userSessionId)

    fun getByGiftId(giftId: Int): UserSessionGift? = userSessionGiftDao.getByGiftId(giftId)

    fun create(userSessionId: Int, giftId: Int) = userSessionGiftDao.create(userSessionId, giftId)

    fun update(id: Int, userSessionId: Int, giftId: Int) = userSessionGiftDao.update(id, userSessionId, giftId)

    fun delete(id: Int) = userSessionGiftDao.delete(id)
}