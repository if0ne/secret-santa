package ru.tinkoff.santa.rest.gift_giving

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class GiftGivingDao(private val database: Database) {
    fun getAll(): List<GiftGiving> = transaction(database) {
        GiftsGivings.selectAll().map(::extractGiftGiving)
    }

    fun create(sessionId: Int, giftGivingUserId: Int, giftReceivingUserId: Int): GiftGiving = transaction(database) {
        GiftGiving(
            GiftsGivings.insertAndGetId {
                it[GiftsGivings.sessionId] = sessionId
                it[GiftsGivings.giftGivingUserId] = giftGivingUserId
                it[GiftsGivings.giftReceivingUserId] = giftReceivingUserId
            }.value, sessionId, giftGivingUserId, giftReceivingUserId
        )
    }

    fun delete(id: Int) = transaction(database) {
        GiftsGivings.deleteWhere { GiftsGivings.id eq id }
    }
}

private fun extractGiftGiving(row: ResultRow) = GiftGiving(
    row[GiftsGivings.id].value,
    row[GiftsGivings.sessionId],
    row[GiftsGivings.giftGivingUserId],
    row[GiftsGivings.giftReceivingUserId]
)