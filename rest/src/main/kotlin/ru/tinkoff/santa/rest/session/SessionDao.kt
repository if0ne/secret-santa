package ru.tinkoff.santa.rest.session

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Timestamp
import java.util.*

class SessionDao(private val database: Database) {
    fun getAll(): List<Session> = transaction(database) {
        Sessions.selectAll().map(::extractSession)
    }

    fun getById(id: Int): Session? = transaction(database) {
        runCatching {
            extractSession(Sessions.select { Sessions.id eq id }.first())
        }.getOrNull()
    }

    fun getByHostId(hostId: Int): List<Session> = transaction(database) {
        Sessions.select { Sessions.hostId eq hostId }.map(::extractSession)
    }

    fun getByGuid(guid: UUID): Session? = transaction(database) {
        runCatching {
            extractSession(Sessions.select { Sessions.guid eq guid }.first())
        }.getOrNull()
    }

    fun create(
        currentState: SessionState,
        description: String?,
        hostId: Int,
        minPlayersQuantity: Int,
        eventTimestamp: Timestamp,
        timestampToChoose: Timestamp
    ) = transaction(database) {
        Sessions.insert {
            it[guid] = UUID.randomUUID()
            it[Sessions.currentState] = currentState
            it[Sessions.description] = description
            it[Sessions.hostId] = hostId
            it[Sessions.minPlayersQuantity] = minPlayersQuantity
            it[Sessions.eventTimestamp] = eventTimestamp.toInstant()
            it[Sessions.timestampToChoose] = timestampToChoose.toInstant()
        }
    }

    fun update(
        id: Int,
        currentState: SessionState,
        description: String?,
        hostId: Int,
        minPlayersQuantity: Int,
        eventTimestamp: Timestamp,
        timestampToChoose: Timestamp
    ) = transaction(database) {
        Sessions.update({ Sessions.id eq id }) {
            it[Sessions.currentState] = currentState
            it[Sessions.description] = description
            it[Sessions.hostId] = hostId
            it[Sessions.minPlayersQuantity] = minPlayersQuantity
            it[Sessions.eventTimestamp] = eventTimestamp.toInstant()
            it[Sessions.timestampToChoose] = timestampToChoose.toInstant()
        }
    }

    fun delete(id: Int) {
        Sessions.deleteWhere {
            Sessions.id eq id
        }
    }
}

private fun extractSession(row: ResultRow): Session =
    Session(
        row[Sessions.id].value,
        row[Sessions.guid],
        row[Sessions.currentState],
        row[Sessions.description],
        row[Sessions.hostId],
        row[Sessions.minPlayersQuantity],
        Timestamp.from(row[Sessions.eventTimestamp]),
        Timestamp.from(row[Sessions.timestampToChoose])
    )