package ru.tinkoff.santa.rest.session

import ru.tinkoff.sanata.shared_models.model.Session
import ru.tinkoff.sanata.shared_models.model.SessionState
import ru.tinkoff.sanata.shared_models.status.SessionErrorCode
import ru.tinkoff.santa.rest.session.exception.SessionException
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

    fun startSession(sessionId: Int) = setCurrentState(sessionId, SessionState.GAME)

    fun finishSession(sessionId: Int) = setCurrentState(sessionId, SessionState.FINISH)

    private fun setCurrentState(sessionId: Int, state: SessionState) {
        val session = getById(sessionId)
        if (session != null) {
            update(
                session.id,
                state,
                session.description,
                session.hostId,
                session.budget,
                session.minPlayersQuantity,
                session.eventTimestamp,
                session.timestampToChoose
            )
        }
    }

    fun checkSession(sessionId: Int) {
        if (getById(sessionId) == null) {
            throw SessionException(SessionErrorCode.SESSION_NOT_FOUND)
        }
    }

    fun checkAndGetSession(sessionId: Int): Session =
        getById(sessionId) ?: throw SessionException(SessionErrorCode.SESSION_NOT_FOUND)

    fun checkAndGetSessionByGuid(sessionGuid: UUID): Session =
        getByGuid(sessionGuid) ?: throw SessionException(SessionErrorCode.SESSION_NOT_FOUND)

    fun checkCurrentStateIsLobby(sessionId: Int) {
        when (checkAndGetSession(sessionId).currentState) {
            SessionState.GAME -> throw SessionException(SessionErrorCode.SESSION_ALREADY_STARTED)
            SessionState.FINISH -> throw SessionException(SessionErrorCode.SESSION_ALREADY_FINISHED)
        }
    }

    fun checkCurrentStateIsGame(sessionId: Int) {
        when (checkAndGetSession(sessionId).currentState) {
            SessionState.LOBBY -> throw SessionException(SessionErrorCode.SESSION_NOT_STARTED)
            SessionState.FINISH -> throw SessionException(SessionErrorCode.SESSION_ALREADY_FINISHED)
        }
    }

    fun checkUsersIsEnoughForStart(sessionId: Int, usersNumber: Int) {
        val session = checkAndGetSession(sessionId)
        if (usersNumber < session.minPlayersQuantity) {
            throw SessionException(SessionErrorCode.NOT_ENOUGH_USERS_FOR_START)
        }
    }

    fun delete(id: Int) = sessionDao.delete(id)
}