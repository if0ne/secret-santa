package ru.tinkoff.santa.rest.user_session

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction

class UserSessionDao(private val database: Database) {
    fun getAll(): List<UserSession> = transaction(database) {
        UsersSessions.selectAll().map(::extractUserSession)
    }

    fun getById(id: Int): UserSession? = transaction(database) {
        runCatching {
            extractUserSession(UsersSessions.select { UsersSessions.id eq id }.first())
        }.getOrNull()
    }

    fun getByUserId(userId: Int): List<UserSession> = transaction(database) {
        UsersSessions.select { UsersSessions.userId eq userId }.map(::extractUserSession)
    }

    fun getBySessionId(sessionId: Int): List<UserSession> = transaction(database) {
        UsersSessions.select { UsersSessions.sessionId eq sessionId }.map(::extractUserSession)
    }

    fun create(userId: Int, sessionId: Int) = transaction(database) {
        UsersSessions.insert {
            wrapUserSessionToUpdateBuilder(it, userId, sessionId)
        }
    }

    fun update(id: Int, userId: Int, sessionId: Int) = transaction(database) {
        UsersSessions.update({ UsersSessions.id eq id }) {
            wrapUserSessionToUpdateBuilder(it, userId, sessionId)
        }
    }

    fun delete(id: Int) = transaction(database) {
        UsersSessions.deleteWhere { UsersSessions.id eq id }
    }
}

private fun wrapUserSessionToUpdateBuilder(updateBuilder: UpdateBuilder<Number>, userId: Int, sessionId: Int) {
    updateBuilder[UsersSessions.userId] = userId
    updateBuilder[UsersSessions.sessionId] = sessionId
}

private fun extractUserSession(row: ResultRow) = UserSession(
    row[UsersSessions.id].value,
    row[UsersSessions.userId],
    row[UsersSessions.sessionId]
)