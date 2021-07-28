package ru.tinkoff.santa.rest.user

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import ru.tinkoff.sanata.shared_models.model.User
import java.util.*

class UserDao(private val database: Database) {
    fun getAll(): List<User> = transaction(database) {
        Users.selectAll().map(::extractUser)
    }

    fun getById(id: Int): User? = transaction(database) {
        runCatching {
            extractUser(Users.select { Users.id eq id }.first())
        }.getOrNull()
    }

    fun getByTelegramGuid(telegramGuid: UUID): User? = transaction(database) {
        runCatching {
            extractUser(Users.select { Users.telegramGuid eq telegramGuid }.first())
        }.getOrNull()
    }

    fun getByEmail(email: String): User? = transaction(database) {
        runCatching {
            extractUser(Users.select { Users.email eq email }.first())
        }.getOrNull()
    }

    fun getByNickname(nickname: String): User? = transaction(database) {
        runCatching {
            extractUser(Users.select { Users.nickname eq nickname }.first())
        }.getOrNull()
    }

    fun getByTelegramId(telegramId: Long): User? = transaction(database) {
        runCatching {
            extractUser(Users.select { Users.telegramId eq telegramId }.first())
        }.getOrNull()
    }

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
    ): User = transaction(database) {
        User(Users.insertAndGetId {
            wrapUserToUpdateBuilder(
                it,
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
        }.value, telegramGuid, nickname, email, password, firstName, lastName, middleName, avatarUrl, telegramId)
    }

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
    ) = transaction {
        Users.update({ Users.id eq id }) {
            wrapUserToUpdateBuilder(
                it,
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
        }
    }

    fun setTelegramId(userId: Int, telegramId: Long, telegramGuid: UUID) {
        val user = getById(userId)
        if (user != null) {
            update(
                user.id,
                telegramGuid,
                user.nickname,
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

    fun delete(id: Int) = transaction(database) {
        Users.deleteWhere { Users.id eq id }
    }
}

private fun wrapUserToUpdateBuilder(
    updateBuilder: UpdateBuilder<Number>,
    telegramGuid: UUID?,
    nickname: String,
    email: String,
    password: ByteArray,
    firstName: String,
    lastName: String,
    middleName: String?,
    avatarUrl: String?,
    telegramId: Long?
) {
    updateBuilder[Users.telegramGuid] = telegramGuid
    updateBuilder[Users.nickname] = nickname
    updateBuilder[Users.email] = email
    updateBuilder[Users.password] = password
    updateBuilder[Users.firstName] = firstName
    updateBuilder[Users.lastName] = lastName
    updateBuilder[Users.middleName] = middleName
    updateBuilder[Users.avatarUrl] = avatarUrl
    updateBuilder[Users.telegramId] = telegramId
}

private fun extractUser(row: ResultRow) = User(
    row[Users.id].value,
    row[Users.telegramGuid],
    row[Users.nickname],
    row[Users.email],
    row[Users.password],
    row[Users.firstName],
    row[Users.lastName],
    row[Users.middleName],
    row[Users.avatarUrl],
    row[Users.telegramId]
)