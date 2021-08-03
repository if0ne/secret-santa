package ru.tinkoff.santa.rest.session

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import ru.tinkoff.sanata.shared_models.model.Session
import ru.tinkoff.sanata.shared_models.model.SessionState
import java.time.LocalDateTime
import java.time.ZoneOffset
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
        budget: Int,
        minPlayersQuantity: Int,
        eventDateTime: LocalDateTime,
        dateTimeToChoose: LocalDateTime
    ) = transaction(database) {
        Sessions.insert {
            it[guid] = UUID.randomUUID()
            wrapSessionToUpdateBuilder(
                it,
                currentState,
                description,
                hostId,
                budget,
                minPlayersQuantity,
                eventDateTime,
                dateTimeToChoose
            )
        }
    }

    fun update(
        id: Int,
        currentState: SessionState,
        description: String?,
        hostId: Int,
        budget: Int,
        minPlayersQuantity: Int,
        eventDateTime: LocalDateTime,
        dateTimeToChoose: LocalDateTime
    ) = transaction(database) {
        Sessions.update({ Sessions.id eq id }) {
            wrapSessionToUpdateBuilder(
                it,
                currentState,
                description,
                hostId,
                budget,
                minPlayersQuantity,
                eventDateTime,
                dateTimeToChoose
            )
        }
    }

    fun delete(id: Int) = transaction(database) {
        Sessions.deleteWhere { Sessions.id eq id }
    }
}

private fun wrapSessionToUpdateBuilder(
    updateBuilder: UpdateBuilder<Number>,
    currentState: SessionState,
    description: String?,
    hostId: Int,
    budget: Int,
    minPlayersQuantity: Int,
    eventDateTime: LocalDateTime,
    dateTimeToChoose: LocalDateTime
) {
    updateBuilder[Sessions.currentState] = currentState
    updateBuilder[Sessions.description] = description
    updateBuilder[Sessions.hostId] = hostId
    updateBuilder[Sessions.budget] = budget
    updateBuilder[Sessions.minPlayersQuantity] = minPlayersQuantity
    updateBuilder[Sessions.eventTimestamp] = eventDateTime.toInstant(ZoneOffset.UTC)
    updateBuilder[Sessions.timestampToChoose] = dateTimeToChoose.toInstant(ZoneOffset.UTC)
}

private fun extractSession(row: ResultRow): Session =
    Session(
        row[Sessions.id].value,
        row[Sessions.guid],
        row[Sessions.currentState],
        row[Sessions.description],
        row[Sessions.hostId],
        row[Sessions.budget],
        row[Sessions.minPlayersQuantity],
        LocalDateTime.ofInstant(row[Sessions.eventTimestamp], ZoneOffset.UTC),
        LocalDateTime.ofInstant(row[Sessions.timestampToChoose], ZoneOffset.UTC)
    )