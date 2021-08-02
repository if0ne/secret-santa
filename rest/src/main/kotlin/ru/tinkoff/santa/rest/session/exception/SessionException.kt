package ru.tinkoff.santa.rest.session.exception

import ru.tinkoff.sanata.shared_models.status.SessionErrorCode

class SessionException(val errorCode: SessionErrorCode) : Exception()