package ru.tinkoff.santa.rest.user_session_gift

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction

class UserSessionGiftDao(private val database: Database) {
    fun getAll(): List<UserSessionGift> = transaction(database) {
        UsersSessionsGifts.selectAll().map(::extractUserSessionGift)
    }

    fun getById(id: Int): UserSessionGift? = transaction(database) {
        runCatching {
            extractUserSessionGift(UsersSessionsGifts.select { UsersSessionsGifts.id eq id }.first())
        }.getOrNull()
    }

    fun getByUserSessionId(userSessionId: Int): List<UserSessionGift> = transaction(database) {
        UsersSessionsGifts.select { UsersSessionsGifts.userSessionId eq userSessionId }.map(::extractUserSessionGift)
    }

    fun getByGiftId(giftId: Int): UserSessionGift? = transaction(database) {
        runCatching {
            extractUserSessionGift(UsersSessionsGifts.select { UsersSessionsGifts.giftId eq giftId }.first())
        }.getOrNull()
    }

    fun create(userSessionId: Int, giftId: Int) = transaction(database) {
        UsersSessionsGifts.insert {
            wrapUserSessionGiftToUpdateBuilder(it, userSessionId, giftId)
        }
    }

    fun update(id: Int, userSessionId: Int, giftId: Int) = transaction(database) {
        UsersSessionsGifts.update({ UsersSessionsGifts.id eq id }) {
            wrapUserSessionGiftToUpdateBuilder(
                it,
                userSessionId,
                giftId
            )
        }
    }

    fun delete(id: Int) = transaction(database) {
        UsersSessionsGifts.deleteWhere { UsersSessionsGifts.id eq id }
    }
}


private fun wrapUserSessionGiftToUpdateBuilder(updateBuilder: UpdateBuilder<Number>, userSessionId: Int, giftId: Int) {
    updateBuilder[UsersSessionsGifts.userSessionId] = userSessionId
    updateBuilder[UsersSessionsGifts.giftId] = giftId
}

private fun extractUserSessionGift(row: ResultRow) = UserSessionGift(
    row[UsersSessionsGifts.id].value,
    row[UsersSessionsGifts.userSessionId],
    row[UsersSessionsGifts.giftId]
)