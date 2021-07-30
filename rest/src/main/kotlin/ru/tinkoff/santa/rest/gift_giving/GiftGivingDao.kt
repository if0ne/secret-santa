package ru.tinkoff.santa.rest.gift_giving

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class GiftGivingDao(private val database: Database) {
    fun getAll(): List<GiftGiving> = transaction(database) {
        GiftsGivings.selectAll().map(::extractGiftGiving)
    }
}

private fun extractGiftGiving(row: ResultRow) = GiftGiving(
    row[GiftsGivings.id].value,
    row[GiftsGivings.sessionId],
    row[GiftsGivings.giftGivingUserId],
    row[GiftsGivings.giftReceivingUserId]
)