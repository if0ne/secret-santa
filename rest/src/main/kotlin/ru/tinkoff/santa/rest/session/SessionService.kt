package ru.tinkoff.santa.rest.session

import ru.tinkoff.sanata.shared_models.model.Session
import ru.tinkoff.sanata.shared_models.model.SessionState
import java.time.LocalDateTime
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
        eventDateTime: LocalDateTime,
        dateTimeToChoose: LocalDateTime,
        minPlayersQuantity: Int = 3,
    ) = sessionDao.create(
        currentState,
        description,
        hostId,
        budget,
        minPlayersQuantity,
        eventDateTime,
        dateTimeToChoose
    )

    fun update(
        id: Int,
        currentState: SessionState,
        description: String?,
        hostId: Int,
        budget: Int,
        minPlayersQuantity: Int,
        eventDateTime: LocalDateTime,
        dateTimeToChoose: LocalDateTime,
    ) = sessionDao.update(
        id,
        currentState,
        description,
        hostId,
        budget,
        minPlayersQuantity,
        eventDateTime,
        dateTimeToChoose
    )

    fun delete(id: Int) = sessionDao.delete(id)
}