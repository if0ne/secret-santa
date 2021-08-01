package ru.tinkoff.santa.rest.user

import ru.tinkoff.sanata.shared_models.model.User
import ru.tinkoff.santa.rest.user.exception.UserNotFoundException

class UserService(private val userDao: UserDao) {
    fun getAll(): List<User> = userDao.getAll()

    fun getById(id: Int): User? = userDao.getById(id)

    fun getByPhone(phone: String): User? = userDao.getByPhone(phone)

    fun getByEmail(email: String): User? = userDao.getByEmail(email)

    fun getByTelegramId(telegramId: Long): User? = userDao.getByTelegramId(telegramId)

    fun create(
        phone: String,
        email: String,
        password: ByteArray,
        firstName: String,
        lastName: String,
        middleName: String?,
        avatarUrl: String?,
        telegramId: Long?
    ) = userDao.create(
        phone,
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
        phone: String,
        email: String,
        password: ByteArray,
        firstName: String,
        lastName: String,
        middleName: String?,
        avatarUrl: String?,
        telegramId: Long?
    ) = userDao.update(
        id,
        phone,
        email,
        password,
        firstName,
        lastName,
        middleName,
        avatarUrl,
        telegramId
    )

    fun setAvatarUrl(userId: Int, url: String) {
        val user = checkAndGetUser(userId)
        update(
            userId,
            user.phone,
            user.email,
            user.password,
            user.firstName,
            user.lastName,
            user.middleName,
            url,
            user.telegramId
        )
    }

    fun setTelegramId(userId: Int, telegramId: Long) {
        val user = getById(userId)
        if (user != null) {
            update(
                userId,
                user.phone,
                user.email,
                user.password,
                user.firstName,
                user.lastName,
                user.middleName,
                user.avatarUrl,
                telegramId
            )
        }
    }

    fun checkUser(userId: Int) {
        if (getById(userId) == null) {
            throw UserNotFoundException()
        }
    }

    fun checkAndGetUser(userId: Int): User = getById(userId) ?: throw UserNotFoundException()

    fun checkAndGetUserByTelegramId(userTelegramId: Long): User =
        getByTelegramId(userTelegramId) ?: throw UserNotFoundException()

    fun delete(id: Int) = userDao.delete(id)
}