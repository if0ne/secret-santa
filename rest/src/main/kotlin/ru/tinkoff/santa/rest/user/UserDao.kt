package ru.tinkoff.santa.rest.user

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import ru.tinkoff.sanata.shared_models.model.User

class UserDao(private val database: Database) {
    fun getAll(): List<User> = transaction(database) {
        Users.selectAll().map(::extractUser)
    }

    fun getById(id: Int): User? = transaction(database) {
        runCatching {
            extractUser(Users.select { Users.id eq id }.first())
        }.getOrNull()
    }

    fun getByPhone(phone: String): User? = transaction(database) {
        runCatching {
            extractUser(Users.select { Users.phone eq phone }.first())
        }.getOrNull()
    }

    fun getByEmail(email: String): User? = transaction(database) {
        runCatching {
            extractUser(Users.select { Users.email eq email }.first())
        }.getOrNull()
    }

    fun getByTelegramId(telegramId: Long): User? = transaction(database) {
        runCatching {
            extractUser(Users.select { Users.telegramId eq telegramId }.first())
        }.getOrNull()
    }

    fun create(
        phone: String,
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
                phone,
                email,
                password,
                firstName,
                lastName,
                middleName,
                avatarUrl,
                telegramId
            )
        }.value, phone, email, password, firstName, lastName, middleName, avatarUrl, telegramId)
    }

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
    ) = transaction {
        Users.update({ Users.id eq id }) {
            wrapUserToUpdateBuilder(
                it,
                phone,
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

    fun delete(id: Int) = transaction(database) {
        Users.deleteWhere { Users.id eq id }
    }
}

private fun wrapUserToUpdateBuilder(
    updateBuilder: UpdateBuilder<Number>,
    phone: String,
    email: String,
    password: ByteArray,
    firstName: String,
    lastName: String,
    middleName: String?,
    avatarUrl: String?,
    telegramId: Long?
) {
    updateBuilder[Users.phone] = phone
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
    row[Users.phone],
    row[Users.email],
    row[Users.password],
    row[Users.firstName],
    row[Users.lastName],
    row[Users.middleName],
    row[Users.avatarUrl],
    row[Users.telegramId]
)