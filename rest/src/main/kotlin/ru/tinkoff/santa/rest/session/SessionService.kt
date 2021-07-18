package ru.tinkoff.santa.rest.session

import java.sql.Timestamp
import java.util.*

class SessionService(private val sessionDao: SessionDao) {
    fun getAll(): List<Session> = sessionDao.getAll()

    fun getById(id: Int): Session? = sessionDao.getById(id)

    fun getByHostId(hostId: Int): List<Session> = sessionDao.getByHostId(hostId)

    fun getByGuid(guid: UUID): Session? = sessionDao.getByGuid(guid)

    fun create(
        currentState: SessionState,
        description: String?,
        hostId: Int,
        budget: Int,
        eventTimestamp: Timestamp,
        timestampToChoose: Timestamp,
        minPlayersQuantity: Int = 3,
    ) = sessionDao.create(
        currentState,
        description,
        hostId,
        budget,
        minPlayersQuantity,
        eventTimestamp,
        timestampToChoose
    )

    fun update(
        id: Int,
        currentState: SessionState,
        description: String?,
        hostId: Int,
        budget: Int,
        minPlayersQuantity: Int,
        eventTimestamp: Timestamp,
        timestampToChoose: Timestamp
    ) = sessionDao.update(
        id,
        currentState,
        description,
        hostId,
        budget,
        minPlayersQuantity,
        eventTimestamp,
        timestampToChoose
    )

    fun delete(id: Int) = sessionDao.delete(id)
}