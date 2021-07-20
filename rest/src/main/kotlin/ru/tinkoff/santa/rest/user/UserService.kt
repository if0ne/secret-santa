package ru.tinkoff.santa.rest.user

import java.util.*

class UserService(private val userDao: UserDao) {
    fun getAll(): List<User> = userDao.getAll()

    fun getById(id: Int): User? = userDao.getById(id)

    fun getByTelegramGuid(telegramGuid: UUID): User? = userDao.getByTelegramGuid(telegramGuid)

    fun getByEmail(email: String): User? = userDao.getByEmail(email)

    fun getByNickname(nickname: String): User? = userDao.getByNickname(nickname)

    fun getByTelegramId(telegramId: Long): User? = userDao.getByTelegramId(telegramId)

    fun create(
        telegramGuid: UUID?,
        nickname: String,
        email: String,
        password: ByteArray,
        firstName: String,
        lastName: String,
        middleName: String?,
        avatarUrl: String?,
        telegramId: Long?
    ) = userDao.create(
        telegramGuid,
        nickname,
        email,
        password,
        firstName,
        lastName,
        middleName,
        avatarUrl,
        telegramId
    )

    fun update(
        id: Int,
        telegramGuid: UUID?,
        nickname: String,
        email: String,
        password: ByteArray,
        firstName: String,
        lastName: String,
        middleName: String?,
        avatarUrl: String?,
        telegramId: Long?
    ) = userDao.update(
        id,
        telegramGuid,
        nickname,
        email,
        password,
        firstName,
        lastName,
        middleName,
        avatarUrl,
        telegramId
    )

    fun delete(id: Int) = userDao.delete(id)
}