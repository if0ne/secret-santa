package ru.tinkoff.santa.rest.guid

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class GuidDao(private val database: Database) {
    fun getAll(): List<Guid> = transaction(database) {
        Guids.selectAll().map(::extractGuid)
    }

    fun getByGuid(guid: UUID): Guid? = transaction(database) {
        runCatching {
            extractGuid(Guids.select {Guids.telegramGuid eq guid}.first())
        }.getOrNull()
    }

    fun create(telegramId: Long): UUID = transaction(database) {
        val uuid = UUID.randomUUID()
        Guids.insert {
            it[telegramGuid] = uuid
            it[Guids.telegramId] = telegramId
        }
        uuid
    }

    fun delete(guid: UUID) = transaction(database) {
        Guids.deleteWhere { Guids.telegramGuid eq guid }
    }
}

private fun extractGuid(row: ResultRow): Guid =
    Guid(
        row[Guids.telegramGuid],
        row[Guids.telegramId]
    )