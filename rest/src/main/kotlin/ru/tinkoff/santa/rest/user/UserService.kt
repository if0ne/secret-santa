package ru.tinkoff.santa.rest.user

class UserService(private val userDao: UserDao) {
    fun getAll(): List<User> = userDao.getAll()

    fun getById(id: Int): User? = userDao.getById(id)

    fun getByEmail(email: String): User? = userDao.getByEmail(email)

    fun getByNickname(nickname: String): User? = userDao.getByNickname(nickname)

    fun getByTelegramId(telegramId: Long): User? = userDao.getByTelegramId(telegramId)

    fun create(
        nickname: String,
        email: String,
        password: ByteArray,
        firstName: String,
        lastName: String,
        middleName: String?,
        avatarUrl: String?,
        telegramId: Long?
    ) = userDao.create(
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